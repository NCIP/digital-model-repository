/*******************************************************************************
 * caBIG™ Open Source Software License
 * Computational Model Execution Framework (CMEF) v1.0
 * 
 * Copyright Notice.
 * Copyright 2010 Massachusetts General Hospital (“caBIG™ Participant”).  Computational Model Execution Framework (CMEF) was created with NCI funding and is part of the caBIG™ initiative. The software subject to this notice and license includes both human readable source code form and machine readable, binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant and You.  “You (or “Your”) shall mean a person or an entity, and all other entities that control, are controlled by, or are under common control with the entity.  “Control” for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.  
 * 
 * License.  
 * Provided that You agree to the conditions described below, caBIG™ Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights in the caBIG™ Software, including any copyright or patent rights therein, to (i) use, install, disclose, access, operate, execute, reproduce, copy, modify, translate, market, publicly display, publicly perform, and prepare derivative works of the caBIG™ Software in any manner and for any purpose, and to have or permit others to do so; (ii) make, have made, use, practice, sell, and offer for sale, import, and/or otherwise dispose of caBIG™ Software (or portions thereof); (iii) distribute and have distributed to and by third parties the caBIG™ Software and any modifications and derivative works thereof; and (iv) sublicense the foregoing rights set out in (i), (ii) and (iii) to third parties, including the right to license such rights to further third parties.  For sake of clarity, and not by way of limitation, caBIG™ Participant shall have no right of accounting or right of payment from You or Your sublicensees for the rights granted under this License.  This License is granted at no charge to You.  Your downloading, copying, modifying, displaying, distributing or use of caBIG™ Software constitutes acceptance of all of the terms and conditions of this Agreement.  If you do not agree to such terms and conditions, you have no right to download, copy, modify, display, distribute or use the caBIG™ Software.  
 * 
 * 1.	Your redistributions of the source code for the caBIG™ Software must retain the above copyright notice, this list of conditions and the disclaimer and limitation of liability of Article 6 below.  Your redistributions in object code form must reproduce the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials provided with the distribution, if any.
 * 
 * 2.	Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: “This product includes software developed by Massachusetts General Hospital.”  If You do not include such end-user documentation, You shall include this acknowledgment in the caBIG™ Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3.	You may not use the names  ”Massachusetts General Hospital”, “MGH”, "INFOTECH Soft", “The National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™” to endorse or promote products derived from this caBIG™ Software.  This License does not authorize You to use any trademarks, service marks, trade names, logos or product names of either caBIG™ Participant, NCI or caBIG™, except as required to comply with the terms of this License.
 * 
 * 4.	For sake of clarity, and not by way of limitation, You may incorporate this caBIG™ Software into Your proprietary programs and into any third party proprietary programs.  However, if You incorporate the caBIG™ Software into third party proprietary programs, You agree that You are solely responsible for obtaining any permission from such third parties required to incorporate the caBIG™ Software into such third party proprietary programs and for informing Your sublicensees, including without limitation Your end-users, of their obligation to secure any required permissions from such third parties before incorporating the caBIG™ Software into such third party proprietary software programs.  In the event that You fail to obtain such permissions, You agree to indemnify caBIG™ Participant for any claims against caBIG™ Participant by such third parties, except to the extent prohibited by law, resulting from Your failure to obtain such permissions.
 * 
 * 5.	For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses of modifications of the caBIG™ Software, or any derivative works of the caBIG™ Software as a whole, provided Your use, reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.
 * 
 * 6.	THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED.  IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU)
 * Contributors: INFOTECH Soft, Inc.
 ******************************************************************************/
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
