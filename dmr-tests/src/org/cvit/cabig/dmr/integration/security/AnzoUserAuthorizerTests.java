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

package org.cvit.cabig.dmr.integration.security;

import static org.junit.Assert.*;

import java.util.Properties;
import java.util.Set;

import org.cvit.cabig.dmr.AuthorizationException;
import org.cvit.cabig.dmr.InvalidUserIdException;
import org.cvit.cabig.dmr.anzo.AnzoUserAuthorizer;
import org.cvit.cabig.dmr.anzo.DmrServiceAuthenticationProvider;
import org.cvit.cabig.dmr.integration.AbstractAnzoTests;
import org.cvit.cabig.dmr.vocabulary.AnzoPredicates;
import org.cvit.cabig.dmr.vocabulary.CViT;
import org.cvit.cabig.dmr.vocabulary.RBAC;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openanzo.client.RemoteGraph;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.common.utils.ACLUtil;
import org.openanzo.model.INamedGraph;
import org.openanzo.server.repository.RepositoryProperties;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;

import com.infotechsoft.rdf.RdfFactory;
import com.infotechsoft.rdf.sesame.SesameRdfFactory;
import com.infotechsoft.rdf.vocabulary.Rdf;


public class AnzoUserAuthorizerTests extends AbstractAnzoTests {
    private static final String ADD_ROLE = "http://example.org/roles/Add" ;
    private static final String REMOVE_ROLE = "http://example.org/roles/Remove" ;
    private static final String EDIT_ROLE = "http://example.org/roles/Edit" ;
    
    private static final String GRAPH_URI = "http://example.org/graphs/Graph1" ;
    
    private static final String USER1_URI = "http://example.org/users/user1" ;
    private static final String USER1_ID = "user1" ;
    private static final String USER1_PW = "user1pw" ;
    
    private static final String PI_URI = "http://example.org/users/pi" ;
    private static final String PI_ID = "pi" ;
    private static final String PI_PW = "pipw" ;
    
    private RdfFactory rdfFactory = new SesameRdfFactory(getValueFactory()) ;
    
    @BeforeClass
    public static void initAnzo() {
	Properties props = getAnzoProperties() ;
	RepositoryProperties.setAuthenticationProviderClass(props, DmrServiceAuthenticationProvider.class.getName()) ;
	createAnzo(props) ;
    }
    
    @Override
    protected Set<Statement> getInitialStatements(Set<Statement> defaults) {
        //Add roles
	defaults.add(statement(uri(CViT.ROLE), uri(Rdf.TYPE), uri(RBAC.ROLE))) ;
	defaults.add(statement(uri(CViT.PRINCIPAL_INVESTIGATOR_ROLE), uri(Rdf.TYPE), uri(RBAC.ROLE))) ;
	defaults.add(statement(uri(ADD_ROLE), uri(Rdf.TYPE), uri(RBAC.ROLE))) ;
	defaults.add(statement(uri(REMOVE_ROLE), uri(Rdf.TYPE), uri(RBAC.ROLE))) ;
	defaults.add(statement(uri(EDIT_ROLE), uri(Rdf.TYPE), uri(RBAC.ROLE))) ;
        //Add a regular user
	defaults.add(statement(uri(USER1_URI), uri(Rdf.TYPE), uri(RBAC.USER))) ;
	defaults.add(statement(uri(USER1_URI), uri(AnzoPredicates.USER_ID), literal(USER1_ID))) ;
	defaults.add(statement(uri(USER1_URI), uri(AnzoPredicates.PASSWORD), literal(USER1_PW))) ;
	defaults.add(statement(uri(USER1_URI), uri(RBAC.IN_ROLE), uri(CViT.ROLE))) ;
        //Add a pi
	defaults.add(statement(uri(PI_URI), uri(Rdf.TYPE), uri(RBAC.USER))) ;
	defaults.add(statement(uri(PI_URI), uri(AnzoPredicates.USER_ID), literal(PI_ID))) ;
	defaults.add(statement(uri(PI_URI), uri(AnzoPredicates.PASSWORD), literal(PI_PW))) ;
	defaults.add(statement(uri(PI_URI), uri(RBAC.IN_ROLE), uri(CViT.ROLE))) ;
	defaults.add(statement(uri(PI_URI), uri(RBAC.IN_ROLE), uri(CViT.PRINCIPAL_INVESTIGATOR_ROLE))) ;
        return defaults ;
    }
    
    @Before
    public void addGraph() throws AnzoException {
	addNamedGraph(uri(GRAPH_URI)) ;
	RemoteGraph graph = null ;
	INamedGraph metaGraph = null ;
	try {
	    graph = getDatasetService().getRemoteGraph(uri(GRAPH_URI), false) ;
	    getDatasetService().begin() ;
	    URI aclUri = ACLUtil.getACL(graph) ;
	    metaGraph = graph.getMetaDataGraph() ;
	    metaGraph.add(
		statement(aclUri, uri(RBAC.ADD), uri(ADD_ROLE)),
		statement(aclUri, uri(RBAC.REMOVE), uri(REMOVE_ROLE)),
		statement(aclUri, uri(RBAC.ADD), uri(EDIT_ROLE)),
		statement(aclUri, uri(RBAC.REMOVE), uri(EDIT_ROLE))) ;
	    getDatasetService().commit() ;
	} catch (AnzoException e) {
	    getDatasetService().abort() ;
	    throw e ;
	} finally {
	    if (metaGraph != null) {
		metaGraph.close() ;
	    }
	    if (graph != null) {
		graph.close() ;
	    }
	}
	getDatasetService().getDatasetReplicator().replicate(true) ;
    }
    
    private AnzoUserAuthorizer newUser(String userId) throws InvalidUserIdException {
	return new AnzoUserAuthorizer(userId, getAuthenticationService(), getDatasetService()) ;
    }
    
    @Test
    public void canReturnUsersUri() throws InvalidUserIdException {
	AnzoUserAuthorizer user = newUser(USER1_ID) ;
	assertEquals(rdfFactory.createUri(USER1_URI), user.getUri()) ;
    }
    
    @Test(expected=InvalidUserIdException.class)
    public void throwsInvalidUserIdExceptionForInvalidUserId() throws InvalidUserIdException {
	newUser("anInvalidId") ;
    }
    
    @Test
    public void willNotAuthorizeNonPrincipalInvestigatorToAddEntry() throws InvalidUserIdException, AuthorizationException {
	AnzoUserAuthorizer user = newUser(USER1_ID) ;
	assertFalse(user.canAddEntry()) ;
    }
    
    @Test
    public void willAuthorizePrincipalInvestigatorToAddEntry() throws InvalidUserIdException, AuthorizationException {
	AnzoUserAuthorizer user = newUser(PI_ID) ;
	assertTrue(user.canAddEntry()) ;
    }
    
    @Test
    public void willAuthorizeUserWithAddAndRemovePermissionToEditGraph() throws InvalidUserIdException, AuthorizationException {
	addUserToRole(USER1_URI, EDIT_ROLE) ;
	
	AnzoUserAuthorizer user = newUser(USER1_ID) ;
	assertTrue(user.canEditObject(GRAPH_URI)) ;
    }
    
    @Test
    public void willNotAuthorizeUserWithOnlyAddPermissionToEditGraph() throws InvalidUserIdException, AuthorizationException {
	addUserToRole(USER1_URI, ADD_ROLE) ;
	
	AnzoUserAuthorizer user = newUser(USER1_ID) ;
	assertFalse(user.canEditObject(GRAPH_URI)) ;
    }
    
    @Test
    public void willNotAuthorizeUserWithOnlyRemovePermissionToEditGraph() throws InvalidUserIdException, AuthorizationException {
	addUserToRole(USER1_URI, REMOVE_ROLE) ;
	
	AnzoUserAuthorizer user = newUser(USER1_ID) ;
	assertFalse(user.canEditObject(GRAPH_URI)) ;
    }
    
    private void addUserToRole(String userUri, String roleUri) {
	RemoteGraph graph = null ;
	INamedGraph metaGraph = null ;
	try {
	    getDatasetService().begin() ;
	    graph = getDatasetService().getRemoteSystemGraph() ;
	    metaGraph = graph.getMetaDataGraph() ;
	    metaGraph.add(statement(uri(userUri), uri(RBAC.IN_ROLE), uri(roleUri))) ;
	    getDatasetService().commit() ;
	} catch (AnzoException e) {
	    try {
		getDatasetService().abort() ;
	    } catch (AnzoException e1) {
		//Ignore...
	    }
	    throw new AssertionError("Unable to update user role.") ;
	} finally {
	    if (metaGraph != null) {
		metaGraph.close() ;
	    }
	    if (graph != null) {
		graph.close() ;
	    }
	}
	try {
	    getDatasetService().getDatasetReplicator().replicate(true) ;
	} catch (AnzoException e) {
	    //Ignore...
	}
    }
}
