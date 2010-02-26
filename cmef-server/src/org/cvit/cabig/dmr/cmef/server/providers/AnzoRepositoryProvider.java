package org.cvit.cabig.dmr.cmef.server.providers;

import java.util.Properties ;

import org.cvit.cabig.dmr.cmef.AuthenticationException ;
import org.cvit.cabig.dmr.cmef.CmefAuthorizer ;
import org.cvit.cabig.dmr.cmef.CmefMappings ;
import org.cvit.cabig.dmr.cmef.CmefRepository ;
import org.cvit.cabig.dmr.cmef.NullAuthorizer ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.cvit.cabig.dmr.cmef.anzo.Anzo ;
import org.cvit.cabig.dmr.cmef.anzo.AnzoCmefAuthorizer ;
import org.cvit.cabig.dmr.cmef.anzo.AnzoCmefRepository ;
import org.cvit.cabig.dmr.cmef.util.Provider ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;
import org.openanzo.client.DatasetService ;
import org.openanzo.common.exceptions.AnzoException ;
import org.openanzo.model.embedded.EmbeddedAuthenticationService ;
import org.openanzo.model.embedded.EmbeddedModelService ;
import org.openanzo.model.embedded.EmbeddedNotificationService ;
import org.openanzo.model.embedded.EmbeddedReplicationService ;
import org.openanzo.model.embedded.EmbeddedServiceBase ;
import org.openanzo.server.repository.RepositoryProperties ;
import org.openanzo.server.repository.rdb.RepositoryConnectionProperties ;
import org.openanzo.services.IAuthenticationService ;
import org.openanzo.services.IModelService ;
import org.openanzo.services.INotificationService ;
import org.openanzo.services.IReplicationService ;
import org.openanzo.services.ModelServiceProperties ;

import com.infotechsoft.cagrid.cql2sparql.Cql2SparqlProcessor ;
import com.infotechsoft.rdf.mapping.ObjectConverter ;
import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection ;

public class AnzoRepositoryProvider implements Provider<CmefRepository> {
    public static final UserCredentials DEFUALT_ADMIN = new UserCredentials("sysadmin", "sysadminpw") ;
    
    public static Provider<CmefRepository> newInMemoryProvider() {
	return new AnzoRepositoryProvider(newInMemoryProperties(DEFUALT_ADMIN), DEFUALT_ADMIN) ;
    }
    
    public static Properties newInMemoryProperties(UserCredentials adminCredentials) {
	Properties result = new Properties() ;

	ModelServiceProperties.setUser(result, adminCredentials.getUsername()) ;
	ModelServiceProperties.setPassword(result, adminCredentials.getPassword()) ;

	RepositoryProperties.setInitFile(result, "resource:/org/cvit/cabig/dmr/cmef/bootstrap.ttl") ;
	RepositoryProperties.setNotificationPublisherEnabled(result, false) ;
	RepositoryProperties.setResetEnabled(result, true) ;

	RepositoryConnectionProperties.setType(result, "ServerHSQL") ;
	RepositoryConnectionProperties.setUser(result, "sa") ;
	RepositoryConnectionProperties.setPassword(result, "") ;
	RepositoryConnectionProperties.setUrl(result, "jdbc:hsqldb:mem:anzodb") ;
	return result ;
    }

    private Properties properties ;
    private UserCredentials adminUser ;
    
    public AnzoRepositoryProvider(Properties properties, UserCredentials adminUser) {
	if (properties == null) {
	    throw new IllegalArgumentException("Anzo properties must not be null.") ;
	}
	if (adminUser == null) {
	    throw new IllegalArgumentException("Must specify Anzo administrator credentials.") ;
	}
	this.properties = properties ;
	this.adminUser = adminUser ;
    }
    
    public CmefRepository provide(UserCredentials credentials) throws ProviderException {
	Anzo anzo = new Anzo(getDatasetService(credentials)) ;
	Cql2SparqlProcessor processor = new Cql2SparqlProcessor(anzo, CmefMappings.getMappers()) ;
	processor.setPolymorphic(true) ;
	CmefAuthorizer authorizer ;
	try {
	    authorizer = getCmefAuthorizer(credentials) ;
	} catch (AuthenticationException e) {
	    throw new ProviderException("Exception authenticating user.", e) ;
	}
	return new AnzoCmefRepository(
	    anzo, 
	    processor, 
	    new ObjectConverter(CmefMappings.getMappers(), anzo.getRdfFactory()), 
	    authorizer) ;
    }
    
    private DatasetService getDatasetService(UserCredentials credentials) {
	EmbeddedServiceBase userBase ;
	try {
	    userBase = new EmbeddedServiceBase(properties) ;
	} catch (AnzoException e) {
	    throw new RuntimeException("Exception creating dataset service.", e) ;
	}
	userBase.setServiceUser(credentials.getUsername()) ;
	userBase.setServicePassword(credentials.getPassword()) ;
	IModelService modelSvc = new EmbeddedModelService(userBase) ;
	INotificationService notificationSvc = new EmbeddedNotificationService(userBase) ;
	IReplicationService replicationSvc = new EmbeddedReplicationService(userBase) ;
	DatasetService result = new DatasetService(properties, modelSvc, notificationSvc, replicationSvc) ;
	return result ;
    }
    
    private CmefAuthorizer getCmefAuthorizer(UserCredentials user) throws AuthenticationException {
	DatasetService systemSvc = new DatasetService(properties) ;
	IAuthenticationService authenticationSvc = EmbeddedAuthenticationService.getInstance(systemSvc, properties) ;
	authenticationSvc.setServiceUser(adminUser.getUsername()) ;
	authenticationSvc.setServicePassword(adminUser.getPassword()) ;
	return new AnzoCmefAuthorizer(user, authenticationSvc, systemSvc) ;
    }
}
