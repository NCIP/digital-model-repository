/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.anzo;

import info.aduna.collections.iterators.CloseableIterator;

import org.cvit.cabig.dmr.AuthorizationService;
import org.cvit.cabig.dmr.DataSourceException;
import org.cvit.cabig.dmr.InvalidUserIdException;
import org.cvit.cabig.dmr.UserAuthorizer;
import org.openanzo.client.DatasetService;
import org.openanzo.client.RemoteGraph;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.common.ontology.ACL;
import org.openanzo.common.ontology.AnzoFactory;
import org.openanzo.common.ontology.Role;
import org.openanzo.common.utils.ACLUtil;
import org.openanzo.model.INamedGraphWithMetaData;
import org.openanzo.services.IAuthenticationService;
import org.openrdf.model.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnzoAuthorizationService implements AuthorizationService {
    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    private IAuthenticationService authService ;
    private DatasetService datasetSvc ;
    
    public AnzoAuthorizationService(IAuthenticationService authService, DatasetService datasetSvc) {
	if (authService == null) {
	    throw new IllegalArgumentException("Authentication service may not be null.") ;
	}
	if (datasetSvc == null) {
	    throw new IllegalArgumentException("Dataset service may not be null.") ;
	}
	this.authService = authService ;
	this.datasetSvc = datasetSvc ;
    }
    
    public UserAuthorizer getUserAuthorizer(String userId) throws InvalidUserIdException {
	UserAuthorizer result = new AnzoUserAuthorizer(userId, authService, datasetSvc) ;
	return result ;
    }

    public void updateObjectsAclFromEntry(String objectId, String entryId) throws DataSourceException {
	//TODO: Only update object ACL if created by PI? (See CViTServlet line 438)
        logger.debug("Updating {} from {}.", objectId, entryId) ;
	RemoteGraph entryGraph = null ;
	RemoteGraph objectGraph = null ;
	try {
	    datasetSvc.begin() ;
	    entryGraph = datasetSvc.getRemoteGraph(uri(entryId), false) ;
            objectGraph = datasetSvc.getRemoteGraph(uri(objectId), false) ;
	    
	    ACL entryAcl = getAcl(entryGraph) ;
	    ACL objectAcl = getAcl(objectGraph) ;

	    CloseableIterator<Role> readIter = entryAcl.getRead() ;
	    while (readIter.hasNext()) {
		objectAcl.addRead(readIter.next()) ;
	    }
	    readIter.close() ;

	    CloseableIterator<Role> addIter = entryAcl.getAdd() ;
	    while (addIter.hasNext()) {
		objectAcl.addAdd(addIter.next()) ;
	    }
	    addIter.close() ;

	    CloseableIterator<Role> removeIter = entryAcl.getRemove() ;
	    while (removeIter.hasNext()) {
		objectAcl.addRemove(removeIter.next()) ;
	    }
	    removeIter.close() ;
	    datasetSvc.commit() ;
	} catch (AnzoException e) {
	    try { datasetSvc.abort() ; } catch (Exception ex) { }
	    if (entryGraph != null) {
		entryGraph.close() ;
	    }
	    throw new DataSourceException("Error updating ACL for " + objectId + " from " + entryId + ".", e) ;
	} finally {
	    if (entryGraph != null) {
		entryGraph.close() ;
	    }
	    if (objectGraph != null) {
		objectGraph.close() ;
	    }
	}
	try {
	    datasetSvc.getDatasetReplicator().replicate(true) ;
	} catch (AnzoException e) {
	    throw new DataSourceException("Error while replicating dataset.", e) ;
	}
    }
    
    private ACL getAcl(INamedGraphWithMetaData graph) {
	URI aclUri = ACLUtil.getACL(graph) ;
	return AnzoFactory.getACL(aclUri, graph.getMetaDataGraph()) ;
    }
    
    private URI uri(String uri) {
	return datasetSvc.getValueFactory().createURI(uri) ;
    }
}
