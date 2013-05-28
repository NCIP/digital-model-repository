/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef;

import java.util.Properties ;

import org.cvit.cabig.dmr.cmef.anzo.Anzo ;
import org.cvit.cabig.dmr.cmef.anzo.AnzoCmefRepository ;
import org.cvit.cabig.dmr.cmef.monitoring.JobMonitoringService ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;
import org.openanzo.client.DatasetService ;
import org.openanzo.common.exceptions.AnzoException ;
import org.openanzo.model.embedded.EmbeddedModelService ;
import org.openanzo.model.embedded.EmbeddedNotificationService ;
import org.openanzo.model.embedded.EmbeddedReplicationService ;
import org.openanzo.model.embedded.EmbeddedServiceBase ;
import org.openanzo.server.repository.RepositoryProperties ;
import org.openanzo.server.repository.rdb.RepositoryConnectionProperties ;
import org.openanzo.services.IModelService ;
import org.openanzo.services.INotificationService ;
import org.openanzo.services.IReplicationService ;
import org.openanzo.services.ModelServiceProperties ;

import com.infotechsoft.cagrid.cql2sparql.Cql2SparqlProcessor ;
import com.infotechsoft.rdf.mapping.ObjectConverter ;

public class TestCmefProvider implements CmefProvider {
    private static final String SYSADMIN_ID = "sysadmin" ;
    private static final String SYSADMIN_PW = "sysadminpw" ;
    public static UserCredentials ADMIN_CREDENTIALS = new UserCredentials(SYSADMIN_ID, SYSADMIN_PW) ;
    
    @Override
    public CmefRepository provideRepository(UserCredentials credentials) {
	Anzo anzo = new Anzo(getDatasetService(credentials)) ;
	Cql2SparqlProcessor processor = new Cql2SparqlProcessor(anzo, CmefMappings.getMappers()) ;
	processor.setPolymorphic(true) ;
	return new AnzoCmefRepository(
	    anzo, 
	    processor, 
	    new ObjectConverter(CmefMappings.getMappers(), anzo.getRdfFactory()), 
	    new NullAuthorizer(anzo.getRdfFactory().createUri("urn:its:nullAuthorizer"))) ;
    }

    @Override
    public CmefService provideService(UserCredentials credentials) {
        // TODO Auto-generated method stub
        return null ;
    }
    
    @Override
    public FileStore provideFileStore(UserCredentials credentials) {
        // TODO Auto-generated method stub
        return null ;
    }
    
    @Override
    public JobMonitoringService provideMonitoringService(UserCredentials credentials) throws ProviderException {
        // TODO Auto-generated method stub
        return null ;
    }
    
    public DatasetService getDatasetService(UserCredentials credentials) {
	Properties props = getAnzoProperties() ;
	EmbeddedServiceBase userBase ;
	try {
	    userBase = new EmbeddedServiceBase(props) ;
	} catch (AnzoException e) {
	    throw new RuntimeException("Exception creating dataset service.", e) ;
	}
	userBase.setServiceUser(credentials.getUsername()) ;
	userBase.setServicePassword(credentials.getPassword()) ;
	IModelService modelSvc = new EmbeddedModelService(userBase) ;
	INotificationService notificationSvc = new EmbeddedNotificationService(userBase) ;
	IReplicationService replicationSvc = new EmbeddedReplicationService(userBase) ;
	DatasetService result = new DatasetService(props, modelSvc, notificationSvc, replicationSvc) ;
	return result ;
    }
    
    private Properties getAnzoProperties() {
	Properties result = new Properties() ;

	ModelServiceProperties.setUser(result, SYSADMIN_ID) ;
	ModelServiceProperties.setPassword(result, SYSADMIN_PW) ;

	RepositoryProperties.setInitFile(result, "resource:/org/cvit/cabig/dmr/cmef/bootstrap.ttl") ;
	RepositoryProperties.setNotificationPublisherEnabled(result, false) ;
	RepositoryProperties.setResetEnabled(result, true) ;
//	RepositoryProperties.setAuthenticationProviderClass(result, DmrServiceAuthenticationProvider.class.getName()) ;

	RepositoryConnectionProperties.setType(result, "ServerHSQL") ;
	RepositoryConnectionProperties.setUser(result, "sa") ;
	RepositoryConnectionProperties.setPassword(result, "") ;
	RepositoryConnectionProperties.setUrl(result, "jdbc:hsqldb:mem:anzodb") ;
	return result ;
    }
}
