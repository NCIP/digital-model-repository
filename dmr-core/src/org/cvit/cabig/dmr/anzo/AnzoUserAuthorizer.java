/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.anzo;

import info.aduna.collections.iterators.CloseableIterator;

import java.util.HashSet;
import java.util.Set;

import org.cvit.cabig.dmr.AuthorizationException;
import org.cvit.cabig.dmr.InvalidUserIdException;
import org.cvit.cabig.dmr.UserAuthorizer;
import org.cvit.cabig.dmr.vocabulary.CViT;
import org.openanzo.client.DatasetService;
import org.openanzo.client.RemoteGraph;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.common.exceptions.AnzoRuntimeException;
import org.openanzo.common.ontology.ACL;
import org.openanzo.common.ontology.AnzoFactory;
import org.openanzo.common.ontology.Role;
import org.openanzo.common.utils.ACLUtil;
import org.openanzo.model.INamedGraph;
import org.openanzo.services.IAuthenticationService;
import org.openrdf.model.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotechsoft.rdf.Uri;
import com.infotechsoft.rdf.sesame.URIAdapter;

public class AnzoUserAuthorizer implements UserAuthorizer {
    private final Logger logger = LoggerFactory.getLogger(AnzoUserAuthorizer.class) ;
    private String userId ;
    private URI userUri ;
    private IAuthenticationService authService ;
    private DatasetService datasetSvc ;
    
    public AnzoUserAuthorizer(String userId, IAuthenticationService authService, DatasetService datasetSvc) throws InvalidUserIdException {
	if (userId == null) {
	    throw new IllegalArgumentException("User id may not be null.") ;
	}
	if (authService == null) {
	    throw new IllegalArgumentException("Authentication service may not be null.") ;
	}
	if (datasetSvc == null) {
	    throw new IllegalArgumentException("Dataset service may not be null.")  ; 
	}
	try {
	    this.userUri = authService.authenticateUser(userId, "") ;
	    logger.debug("Authenticated user {} with URI {}.", userId, userUri) ;
	} catch (AnzoRuntimeException e) {
	    throw new InvalidUserIdException("Error finding user id: " + userId, e) ; 
	} catch (AnzoException e) {
	    throw new InvalidUserIdException("Exception while verifying user id: " + userId, e) ;
	}
	this.userId = userId ;
	this.authService = authService ;
	this.datasetSvc = datasetSvc ;
    }
    
    /* (non-Javadoc)
     * @see org.cvit.cabig.dmr.anzo.UserAuthorizor#getUri()
     */
    public Uri getUri() {
	return new URIAdapter(userUri) ;
    }

    /* (non-Javadoc)
     * @see org.cvit.cabig.dmr.anzo.UserAuthorizor#canAddEntry()
     */
    public boolean canAddEntry() throws AuthorizationException {
	return isPrimaryInvestigator() ;
    }
    
    /* (non-Javadoc)
     * @see org.cvit.cabig.dmr.anzo.UserAuthorizor#canEditGraph(com.infotechsoft.rdf.Uri)
     */
    public boolean canEditObject(String objectId) throws AuthorizationException {
	Set<URI> userRoles = getUserRoles() ;
	ACL acl = getAcl(uri(objectId)) ;
	Set<URI> addRoles = getAddRoles(acl) ;
	addRoles.retainAll(userRoles) ;
	Set<URI> removeRoles = getRemoveRoles(acl) ;
	removeRoles.retainAll(userRoles) ;
	return !addRoles.isEmpty() && !removeRoles.isEmpty() ;
    }

    private boolean isPrimaryInvestigator() throws AuthorizationException {
	return getUserRoles().contains(datasetSvc.getValueFactory().createURI(CViT.PRINCIPAL_INVESTIGATOR_ROLE)) ;
    }
    
    private Set<URI> getUserRoles() throws AuthorizationException {
	Set<URI> result = null ;
	try {
	    result = authService.getRolesForUser(userUri) ;
	    logger.debug("Retrieved roles for user {}: {}", userUri, result) ;
	    return result ;
	} catch (AnzoException e) {
	    throw new AuthorizationException("Error retrieving roles for: " + userId + ".", e) ;
	}
    }
    
    private ACL getAcl(URI graphUri) throws AuthorizationException {
	RemoteGraph graph = null ;
	INamedGraph metaGraph = null ;
	try {
	    graph = datasetSvc.getRemoteGraph(graphUri, false) ;
	    metaGraph = graph.getMetaDataGraph() ;
	    URI aclUri = ACLUtil.getACL(graph) ;
	    return AnzoFactory.getACL(aclUri, metaGraph) ;
	} catch (AnzoException e) {
	    throw new AuthorizationException("Error retrieving graphs ACL: " + graphUri + ".", e) ;
	} finally {
	    if (metaGraph != null) {
		metaGraph.close() ;
	    }
	    if (graph != null) {
		graph.close() ;
	    }
	}
    }
    
    private Set<URI> getRemoveRoles(ACL acl) {
	Set<URI> result = new HashSet<URI>() ;
	CloseableIterator<Role> iter = acl.getRemove() ;
	while (iter.hasNext()) {
	    result.add(uri(iter.next().uri())) ;
	}
	return result ;
    }
    
    private Set<URI> getAddRoles(ACL acl) {
	Set<URI> result = new HashSet<URI>() ;
	CloseableIterator<Role> iter = acl.getAdd() ;
	while (iter.hasNext()) {
	    result.add(uri(iter.next().uri())) ;
	}
	return result ;
    }
    
    private URI uri(String uri) {
	return datasetSvc.getValueFactory().createURI(uri) ;
    }
}
