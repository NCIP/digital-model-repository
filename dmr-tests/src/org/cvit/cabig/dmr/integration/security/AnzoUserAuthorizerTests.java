/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
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
