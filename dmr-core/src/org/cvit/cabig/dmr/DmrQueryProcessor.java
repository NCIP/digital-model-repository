/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr;

import static com.infotechsoft.cagrid.cql.Utils.* ;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.cagrid.data.utilities.ResultsCreationException;
import gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.cvit.cabig.dmr.anzo.Anzo;
import org.cvit.cabig.dmr.anzo.DmrServiceAuthenticationProvider;
import org.openanzo.client.DatasetService;
import org.openanzo.model.embedded.EmbeddedModelService;
import org.openanzo.server.repository.RepositoryProperties;
import org.openanzo.server.repository.rdb.RepositoryConnectionProperties;
import org.openanzo.services.ModelServiceProperties;

import com.infotechsoft.cagrid.cql2sparql.Cql2SparqlProcessor;

public class DmrQueryProcessor extends CQLQueryProcessor {
    //Properties
    public static final String ANZO_PROPS = "anzo_properties" ;
    public static final String USER = "user" ;
    public static final String PASSWORD = "password" ;
    public static final String DB_TYPE = "database_type" ;
    public static final String DB_URL = "database_url" ;
    public static final String DB_USER = "database_user" ;
    public static final String DB_PASSWORD = "database_password" ;
    private static final Properties REQUIRED_PROPS ;
    static {
	REQUIRED_PROPS = new Properties() ;
	REQUIRED_PROPS.setProperty(USER, "username") ;
	REQUIRED_PROPS.setProperty(PASSWORD, "password") ;
	REQUIRED_PROPS.setProperty(DB_TYPE, "ServerMySQL") ;
	REQUIRED_PROPS.setProperty(DB_URL, "jdbc:mysql://localhost/cvit") ;
	REQUIRED_PROPS.setProperty(DB_USER, "dbusername") ;
	REQUIRED_PROPS.setProperty(DB_PASSWORD, "dbpassword") ;
    }
    
    //Collaborators
    private Anzo anzo ;
    private Cql2SparqlProcessor processor ;
    private GlobusSecurityService securitySvc ;
    
    public void setPolymorphic(boolean polymorphic) {
        processor.setPolymorphic(polymorphic) ;
    }
    
    public boolean isPolymorphic() {
        return processor.isPolymorphic() ;
    }
    
    @Override
    public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
	try {
	    String user = securitySvc.getCallerIdentity() ;
	    anzo.setCurrentUser(getUserId(user)) ;
	} catch (Exception e) {
	    throw new QueryProcessingException("Unable to retrieve callers identity.", e) ;
	}
	CQLQueryResults cqlResults = null ;
        switch (getQueryType(cqlQuery)) {
            case OBJECT:
                List<Object> resultObjects ;
                try {
                    resultObjects = processor.processObjectQuery(cqlQuery) ;
                } catch (com.infotechsoft.cagrid.cql2sparql.QueryProcessingException e) {
                    throw new QueryProcessingException("Error processing CQL query.", e) ;
                }
                try {
                    cqlResults = CQLResultsCreationUtil.createObjectResults(
                            resultObjects,
                            cqlQuery.getTarget().getName(),
                            getClassToQnameMappings()) ;
                } catch (ResultsCreationException e) {
                    throw new QueryProcessingException("Error creating CQL results object.", e) ;
                } catch (Exception e) {
                    throw new QueryProcessingException("Error getting class to qname mapping.", e) ;
                }
                return cqlResults ;
            case ATTRIBUTES:
                List<String[]> attrResults ;
		try {
		    attrResults = processor.processAttributeQuery(cqlQuery) ;
		} catch (com.infotechsoft.cagrid.cql2sparql.QueryProcessingException e) {
		    throw new QueryProcessingException("Error processing CQL query.", e) ;
		}
                Collection<String> attrNames = com.infotechsoft.cagrid.cql.Utils.getAttributes(cqlQuery) ;
                cqlResults = CQLResultsCreationUtil.createAttributeResults(
                    attrResults, 
                    cqlQuery.getTarget().getName(), 
                    attrNames.toArray(new String[attrNames.size()])) ;
                return cqlResults ;
            case COUNT:
        	long countResult ;
                try {
                    countResult = processor.processCountQuery(cqlQuery) ;
                } catch (com.infotechsoft.cagrid.cql2sparql.QueryProcessingException e) {
                    throw new QueryProcessingException("Error processing CQL query.", e) ;
                }
                cqlResults = CQLResultsCreationUtil.createCountResults(
                    countResult, 
                    cqlQuery.getTarget().getName()) ;
                return cqlResults ;
            default:
                throw new MalformedQueryException("Unable to determine query type.") ;
        }
    }
    
    public <T> List<T> processObjectQuery(Class<T> classOfResults, CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
        try {
            return processor.processObjectQuery(cqlQuery, classOfResults) ;
        } catch (com.infotechsoft.cagrid.cql2sparql.QueryProcessingException e) {
            throw new QueryProcessingException("Error processing CQL query.", e) ;
        }
    }
    
    @Override
    public Properties getRequiredParameters() {
        return REQUIRED_PROPS ;
    }
    
    @Override
    public Set<String> getPropertiesFromEtc() {
        return Collections.singleton(ANZO_PROPS) ;
    }
    
    @Override
    public void initialize(Properties parameters, InputStream wsdd) throws InitializationException {
        super.initialize(parameters, wsdd) ;
        DatasetService svc = new DatasetService(getAnzoProperties()) ;
        anzo = new Anzo(svc) ;
        if (!anzo.isConnected()) {
            throw new InitializationException("Error connecting to anzo.") ;
        }
        processor = new Cql2SparqlProcessor(anzo, getCvitMappers()) ;
        securitySvc = new GlobusSecurityService() {
            public String getCallerIdentity() throws Exception {
                return SecurityUtils.getCallerIdentity() ;
            }
        } ;
    }
    
    /**
     * Used to inject a pre-existing Dataset Service and a custom security service.  This method 
     * is intended for testing purposes only.
     * @param parameters Properties passed to CQLQueryProcessor
     * @param wsdd WSDD file passed to CQLQueryProcessor
     * @param datasetService Used to configure Cql2SparqlProcessor
     * @throws InitializationException
     */
    public void initialize(DatasetService datasetService, GlobusSecurityService securitySvc, InputStream wsdd, Mappings classToQname) throws InitializationException {
	super.initialize(getRequiredParameters(), wsdd) ;
	anzo = new Anzo(datasetService) ;
	if (!anzo.isConnected()) {
            throw new InitializationException("Error connecting to anzo.") ;
        }
        processor = new Cql2SparqlProcessor(anzo, getCvitMappers()) ;
        this.securitySvc = securitySvc ;
        this.classToQname = classToQname ;
    }
    
    private Properties getAnzoProperties() throws InitializationException {
	Properties result = new Properties() ;
	Properties props = getConfiguredParameters() ;
	
	ModelServiceProperties.setUser(result, props.getProperty(USER)) ;
	ModelServiceProperties.setPassword(result, props.getProperty(PASSWORD)) ;
	ModelServiceProperties.setTransportClass(result, EmbeddedModelService.class.getName()) ;
	
	RepositoryConnectionProperties.setType(result, props.getProperty(DB_TYPE)) ;
	RepositoryConnectionProperties.setUrl(result, props.getProperty(DB_URL)) ;
	RepositoryConnectionProperties.setUser(result, props.getProperty(DB_USER)) ;
	RepositoryConnectionProperties.setPassword(result, props.getProperty(DB_PASSWORD)) ;
	
	RepositoryProperties.setAuthenticationProviderClass(result, DmrServiceAuthenticationProvider.class.getName()) ;
	return result ;
    }

    private static com.infotechsoft.rdf.mapping.Mappers mappers = CvitMappings.getMappers() ;
    private static com.infotechsoft.rdf.mapping.Mappers getCvitMappers() {
	return mappers ;
    }
    
    private Mappings classToQname = null ;
    private Mappings getClassToQnameMappings() throws Exception {
	if (classToQname == null) {
	    classToQname = (Mappings) Utils.deserializeDocument(
		ServiceConfigUtil.getClassToQnameMappingsFile(), 
		Mappings.class) ;

	}
	return classToQname ;
    }
    
    private String getUserId(String userDn) {
	return userDn.substring(userDn.lastIndexOf('=') + 1) ;
    }
}
