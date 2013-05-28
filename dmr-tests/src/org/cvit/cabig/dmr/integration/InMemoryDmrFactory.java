/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.integration;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.mapping.Mappings;

import org.cvit.cabig.dmr.CvitMappings;
import org.cvit.cabig.dmr.DmrQueryProcessor;
import org.cvit.cabig.dmr.GlobusSecurityService;
import org.cvit.cabig.dmr.anzo.Anzo;
import org.cvit.cabig.dmr.anzo.AnzoAuthorizationService;
import org.cvit.cabig.dmr.anzo.AnzoDMRService;
import org.cvit.cabig.dmr.anzo.DmrServiceAuthenticationProvider;
import org.openanzo.client.DatasetService;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.model.embedded.EmbeddedAuthenticationService;
import org.openanzo.model.embedded.EmbeddedModelService;
import org.openanzo.model.embedded.EmbeddedNotificationService;
import org.openanzo.model.embedded.EmbeddedReplicationService;
import org.openanzo.model.embedded.EmbeddedServiceBase;
import org.openanzo.server.repository.RepositoryProperties;
import org.openanzo.server.repository.rdb.RepositoryConnectionProperties;
import org.openanzo.services.IAuthenticationService;
import org.openanzo.services.IModelService;
import org.openanzo.services.INotificationService;
import org.openanzo.services.IReplicationService;
import org.openanzo.services.ModelServiceProperties;

import com.infotechsoft.cagrid.cql2sparql.Cql2SparqlProcessor;
import com.infotechsoft.rdf.mapping.ObjectConverter;
import com.infotechsoft.rdf.sesame.SesameRdfFactory;

public class InMemoryDmrFactory implements DmrFactory {
    private static final String SYSADMIN_ID = "sysadmin" ;
    private static final String SYSADMIN_PW = "sysadminpw" ;
    
    private StubSecurityService securityService = new StubSecurityService() ;
    
    private DmrQueryProcessor queryProcessor = null ;
    public DmrQueryProcessor getQueryProcessor() {
	if (queryProcessor == null) {
	    DmrQueryProcessor result = new DmrQueryProcessor() ;
	    try {
		result.initialize(
		    getDatasetService(), 
		    securityService, 
		    getWsddStream(), 
		    getClassToQnameMappings()) ;
	    } catch (InitializationException e) {
		throw new RuntimeException("Exception initializing DmrQueryProcessor.", e) ;
	    }
	    queryProcessor = result ;
	}
	return queryProcessor ;
    }

    private AnzoDMRService dmrService = null;
    public AnzoDMRService getService() {
	if (dmrService == null) {
	    Anzo anzo = new Anzo(getDatasetService()) ;
	    Cql2SparqlProcessor processor = new Cql2SparqlProcessor(anzo, CvitMappings.getMappers()) ;
	    processor.setPolymorphic(true) ;
	    dmrService = new AnzoDMRService(
		anzo,
		new AnzoAuthorizationService(getAuthenticationService(), getSystemService()),
		new ObjectConverter(CvitMappings.getMappers(), new SesameRdfFactory(getDatasetService().getValueFactory())),
		processor) ;
	}
	return dmrService ;
    }

    private DatasetService datasetService = null ;
    public DatasetService getDatasetService() {
	if (datasetService == null) {
	    Properties props = getAnzoProperties() ;
	    EmbeddedServiceBase userBase ;
	    try {
		userBase = new EmbeddedServiceBase(props) ;
	    } catch (AnzoException e) {
		throw new RuntimeException("Exception creating dataset service.", e) ;
	    }
	    IModelService modelSvc = new EmbeddedModelService(userBase) ;
	    INotificationService notificationSvc = new EmbeddedNotificationService(userBase) ;
	    IReplicationService replicationSvc = new EmbeddedReplicationService(userBase) ;
	    datasetService = new DatasetService(props, modelSvc, notificationSvc, replicationSvc) ;
	}
	return datasetService ;
    }
    
    private DatasetService systemService ;
    public DatasetService getSystemService() {
        if (systemService == null) {
            systemService = new DatasetService(
                    getAnzoProperties(),
                    new EmbeddedModelService(getSystemBase()),
                    new EmbeddedNotificationService(getSystemBase()),
                    new EmbeddedReplicationService(getSystemBase())) ;
            systemService.getModelService().setServiceUser(SYSADMIN_ID) ;
        }
        return systemService ;
    }
    
    private IAuthenticationService authenticationService = null ;
    public IAuthenticationService getAuthenticationService() {
	if (authenticationService == null) {
	    authenticationService = new EmbeddedAuthenticationService(getSystemBase()) ;
	    authenticationService.setServiceUser(SYSADMIN_ID) ;
	}
	return authenticationService ;
    }
    
    private EmbeddedServiceBase systemBase ;
    private EmbeddedServiceBase getSystemBase() {
        if (systemBase == null) {
            try {
                systemBase = new EmbeddedServiceBase(getAnzoProperties()) ;
            } catch (AnzoException e) {
                throw new RuntimeException("Exception while creating system base.", e) ;
            }
        }
        return systemBase ;
    }
    
    private InputStream getWsddStream() {
	return this.getClass().getResourceAsStream("server-config.wsdd") ;
    }
    
    private Mappings getClassToQnameMappings() {
	Mappings result ;
	Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("classToQname.xml")) ;
	try {
	    result = (Mappings) Utils.deserializeObject(reader, Mappings.class) ;
	} catch (Exception e) {
	    throw new RuntimeException("Exception reading mappings.", e) ;
	} finally {
	    close(reader) ;
	}
	return result ;
    }
    
    public class StubSecurityService implements GlobusSecurityService {
	private String username = "sysadmin" ;
	
	public String getCallerIdentity() throws Exception {
	    return "O=MyOrg,OU=MyUnit,CN=" + username ;
	}
	
	public void setUser(String username) {
	    if (username == null || username.length() < 1) {
		throw new IllegalArgumentException("Must specify non-empty username.") ;
	    }
	    this.username = username ;
	}
    }
    
    private Properties getAnzoProperties() {
	Properties result = new Properties() ;

	ModelServiceProperties.setUser(result, SYSADMIN_ID) ;
	ModelServiceProperties.setPassword(result, SYSADMIN_PW) ;

	RepositoryProperties.setInitFile(result, "resource:/org/cvit/cabig/dmr/integration/bootstrap.ttl") ;
	RepositoryProperties.setNotificationPublisherEnabled(result, false) ;
	RepositoryProperties.setResetEnabled(result, true) ;
	RepositoryProperties.setAuthenticationProviderClass(result, DmrServiceAuthenticationProvider.class.getName()) ;

	RepositoryConnectionProperties.setType(result, "ServerHSQL") ;
	RepositoryConnectionProperties.setUser(result, "sa") ;
	RepositoryConnectionProperties.setPassword(result, "") ;
	RepositoryConnectionProperties.setUrl(result, "jdbc:hsqldb:mem:anzodb") ;
	return result ;
    }
    
    private void close(Closeable closeable) {
	try {
	    closeable.close() ;
	} catch (IOException e) {
	    //IGNORE
	}
    }
}
