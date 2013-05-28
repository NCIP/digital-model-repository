/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.anzo;

import info.aduna.collections.iterators.CloseableIterator ;

import java.util.ArrayList ;
import java.util.HashSet ;
import java.util.List ;
import java.util.Set ;

import org.cvit.cabig.dmr.cmef.AuthenticationException ;
import org.cvit.cabig.dmr.cmef.AuthorizationException ;
import org.cvit.cabig.dmr.cmef.CmefAuthorizer ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.openanzo.client.DatasetService ;
import org.openanzo.client.RemoteGraph ;
import org.openanzo.common.exceptions.AnzoException ;
import org.openanzo.common.exceptions.AnzoRuntimeException ;
import org.openanzo.common.ontology.ACL ;
import org.openanzo.common.ontology.AnzoFactory ;
import org.openanzo.common.ontology.Role ;
import org.openanzo.common.utils.ACLUtil ;
import org.openanzo.model.INamedGraph ;
import org.openanzo.model.INamedGraphWithMetaData ;
import org.openanzo.services.IAuthenticationService ;
import org.openrdf.model.URI ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import com.infotechsoft.rdf.Uri ;
import com.infotechsoft.rdf.sesame.URIAdapter ;

public class AnzoCmefAuthorizer implements CmefAuthorizer {
    private final Logger logger = LoggerFactory.getLogger(AnzoCmefAuthorizer.class) ;
    private Uri userUri ;
    private UserCredentials user ;
    private IAuthenticationService authService ;
    private DatasetService datasetSvc ;

    public AnzoCmefAuthorizer(UserCredentials user, IAuthenticationService authService, DatasetService datasetSvc) throws AuthenticationException {
	if (user == null) {
	    throw new IllegalArgumentException("User credentials must not be null.") ;
	}
	if (authService == null) {
	    throw new IllegalArgumentException("Authentication service may not be null.") ;
	}
	if (datasetSvc == null) {
	    throw new IllegalArgumentException("Dataset service may not be null.")  ; 
	}
	try {
	    this.userUri = new URIAdapter(authService.authenticateUser(user.getUsername(), user.getPassword())) ;
	    logger.debug("Authenticated user {} with URI {}.", user.getUsername(), userUri) ;
	} catch (AnzoRuntimeException e) {
	    throw new AuthenticationException("Unable to authenticate user: " + user.getUsername(), e) ; 
	} catch (AnzoException e) {
	    throw new AuthenticationException("Exception while authenticating user: " + user.getUsername(), e) ;
	}
	this.user = user ;
	this.authService = authService ;
	this.datasetSvc = datasetSvc ;
    }
    
    @Override
    public boolean canEditResource(Uri id) throws AuthorizationException {
	Set<URI> userRoles = getUserRoles() ;
	ACL acl = getAcl(uri(id.toString())) ;
	Set<URI> addRoles = getAddRoles(acl) ;
	addRoles.retainAll(userRoles) ;
	Set<URI> removeRoles = getRemoveRoles(acl) ;
	removeRoles.retainAll(userRoles) ;
	return !addRoles.isEmpty() && !removeRoles.isEmpty() ;
    }

    @Override
    public boolean canViewResource(Uri id) {
	return true ;
    }

    @Override
    public Uri getUserUri() {
	return userUri ;
    }

    public void updateObjectsAclFromEntry(Set<String> objectIds, String entryId) throws AuthorizationException {
	//TODO: Only update object ACL if created by PI? (See CViTServlet line 438)
        logger.debug("Updating {} from {}.", objectIds, entryId) ;
	RemoteGraph entryGraph = null ;
	List<RemoteGraph> objectGraphs = new ArrayList<RemoteGraph>() ;
	try {
	    datasetSvc.begin() ;
	    entryGraph = datasetSvc.getRemoteGraph(uri(entryId), false) ;
	    ACL entryAcl = getAcl(entryGraph) ;
	    
	    List<ACL> objectAcls = new ArrayList<ACL>() ;
	    for (String objectId : objectIds) {
		RemoteGraph rg = datasetSvc.getRemoteGraph(uri(objectId), false) ;
		objectGraphs.add(rg) ;
		objectAcls.add(getAcl(rg)) ;
	    }

	    CloseableIterator<Role> readIter = entryAcl.getRead() ;
	    while (readIter.hasNext()) {
		Role role = readIter.next() ;
		for (ACL acl : objectAcls) {
		    acl.addRead(role) ;
		}
	    }
	    readIter.close() ;

	    CloseableIterator<Role> addIter = entryAcl.getAdd() ;
	    while (addIter.hasNext()) {
		Role role = addIter.next() ;
		for (ACL acl : objectAcls) {
		    acl.addAdd(role) ;
		}
	    }
	    addIter.close() ;

	    CloseableIterator<Role> removeIter = entryAcl.getRemove() ;
	    while (removeIter.hasNext()) {
		Role role = removeIter.next() ;
		for (ACL acl : objectAcls) {
		    acl.addRemove(role) ;
		}
	    }
	    removeIter.close() ;
	    datasetSvc.commit() ;
	} catch (AnzoException e) {
	    try { datasetSvc.abort() ; } catch (Exception ex) { }
	    throw new AuthorizationException("Error updating ACL for " + objectIds + " from " + entryId + ".", e) ;
	} finally {
	    if (entryGraph != null) {
		entryGraph.close() ;
	    }
	    for (RemoteGraph g : objectGraphs) {
		g.close() ;
	    }
	}
	try {
	    datasetSvc.getDatasetReplicator().replicate(true) ;
	} catch (AnzoException e) {
	    throw new AuthorizationException("Error while replicating dataset.", e) ;
	}
    }
    
    private ACL getAcl(INamedGraphWithMetaData graph) {
	URI aclUri = ACLUtil.getACL(graph) ;
	return AnzoFactory.getACL(aclUri, graph.getMetaDataGraph()) ;
    }
    
    private Set<URI> getUserRoles() throws AuthorizationException {
	Set<URI> result = null ;
	try {
	    result = authService.getRolesForUser(uri(userUri.toString())) ;
	    logger.debug("Retrieved roles for user {}: {}", userUri, result) ;
	    return result ;
	} catch (AnzoException e) {
	    throw new AuthorizationException("Error retrieving roles for: " + user.getUsername() + ".", e) ;
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
    
    private Set<URI> getAddRoles(ACL acl) {
	Set<URI> result = new HashSet<URI>() ;
	CloseableIterator<Role> iter = acl.getAdd() ;
	while (iter.hasNext()) {
	    result.add(uri(iter.next().uri())) ;
	}
	return result ;
    }
    
    private Set<URI> getRemoveRoles(ACL acl) {
	Set<URI> result = new HashSet<URI>() ;
	CloseableIterator<Role> iter = acl.getRemove() ;
	while (iter.hasNext()) {
	    result.add(uri(iter.next().uri())) ;
	}
	return result ;
    }
    
    private URI uri(String uri) {
	return datasetSvc.getValueFactory().createURI(uri) ;
    }
}
