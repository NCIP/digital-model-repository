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
