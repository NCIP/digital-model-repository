/*
 * caBIG™ Open Source Software License v.2 Digital Model Repository (DMR)
 * 
 * Copyright Notice. Copyright 2008 Massachusetts General Hospital (“caBIG™
 * Participant”). Digital Model Repository (DMR) was created with NCI funding
 * and is part of the caBIG™ initiative. The software subject to this notice and
 * license includes both human readable source code form and machine readable,
 * binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant
 * and You. “You (or “Your”) shall mean a person or an entity, and all other
 * entities that control, are controlled by, or are under common control with
 * the entity. “Control” for purposes of this definition means (i) the direct or
 * indirect power to cause the direction or management of such entity, whether
 * by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of
 * the outstanding shares, or (iii) beneficial ownership of such entity.
 * 
 * License. Provided that You agree to the conditions described below, caBIG™
 * Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caBIG™ Software, including any copyright or patent rights
 * therein, to (i) use, install, disclose, access, operate, execute, reproduce,
 * copy, modify, translate, market, publicly display, publicly perform, and
 * prepare derivative works of the caBIG™ Software in any manner and for any
 * purpose, and to have or permit others to do so; (ii) make, have made, use,
 * practice, sell, and offer for sale, import, and/or otherwise dispose of
 * caBIG™ Software (or portions thereof); (iii) distribute and have distributed
 * to and by third parties the caBIG™ Software and any modifications and
 * derivative works thereof; and (iv) sublicense the foregoing rights set out in
 * (i), (ii) and (iii) to third parties, including the right to license such
 * rights to further third parties. For sake of clarity, and not by way of
 * limitation, caBIG™ Participant shall have no right of accounting or right of
 * payment from You or Your sublicensees for the rights granted under this
 * License. This License is granted at no charge to You. Your downloading,
 * copying, modifying, displaying, distributing or use of caBIG™ Software
 * constitutes acceptance of all of the terms and conditions of this Agreement.
 * If you do not agree to such terms and conditions, you have no right to
 * download, copy, modify, display, distribute or use the caBIG™ Software.
 * 
 * 1. Your redistributions of the source code for the caBIG™ Software must
 * retain the above copyright notice, this list of conditions and the disclaimer
 * and limitation of liability of Article 6 below. Your redistributions in
 * object code form must reproduce the above copyright notice, this list of
 * conditions and the disclaimer of Article 6 in the documentation and/or other
 * materials provided with the distribution, if any.
 * 
 * 2. Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: “This product includes software
 * developed by Massachusetts General Hospital.” If You do not include such
 * end-user documentation, You shall include this acknowledgment in the caBIG™
 * Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3. You may not use the names ”Massachusetts General Hospital”, “MGH”, “The
 * National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™”
 * to endorse or promote products derived from this caBIG™ Software. This
 * License does not authorize You to use any trademarks, service marks, trade
 * names, logos or product names of either caBIG™ Participant, NCI or caBIG™,
 * except as required to comply with the terms of this License.
 * 
 * 4. For sake of clarity, and not by way of limitation, You may incorporate
 * this caBIG™ Software into Your proprietary programs and into any third party
 * proprietary programs. However, if You incorporate the caBIG™ Software into
 * third party proprietary programs, You agree that You are solely responsible
 * for obtaining any permission from such third parties required to incorporate
 * the caBIG™ Software into such third party proprietary programs and for
 * informing Your sublicensees, including without limitation Your end-users, of
 * their obligation to secure any required permissions from such third parties
 * before incorporating the caBIG™ Software into such third party proprietary
 * software programs. In the event that You fail to obtain such permissions, You
 * agree to indemnify caBIG™ Participant for any claims against caBIG™
 * Participant by such third parties, except to the extent prohibited by law,
 * resulting from Your failure to obtain such permissions.
 * 
 * 5. For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the caBIG™ Software, or any derivative works
 * of the caBIG™ Software as a whole, provided Your use, reproduction, and
 * distribution of the Work otherwise complies with the conditions stated in
 * this License.
 * 
 * 6. THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 * DISCLAIMED. IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU) Contributors:
 * INFOTECH Soft, Inc.
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
