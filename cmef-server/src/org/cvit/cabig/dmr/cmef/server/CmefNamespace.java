/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server;

import org.restlet.data.Reference ;

public class CmefNamespace {
    private CmefServer server ;
    private Reference appRoot ;
    private Reference host ;

    public CmefNamespace(CmefServer server, Reference appRoot, Reference host) {
	if (server == null) {
	    throw new IllegalArgumentException("Server must not be null.") ;
	}
	if (appRoot == null) {
	    throw new IllegalArgumentException("Application root must not be null.") ;
	}
	if (host == null) {
	    throw new IllegalArgumentException("Host must not be null.") ;
	}
	this.server = server ;
	this.appRoot = appRoot ;
	this.host = host ;
    }
    
    public Reference modelsRef(String entryName, boolean absolute) {
	return getRef(server.getRelativeModelsRef(entryName), absolute) ;
    }
    
    public Reference modelRef(String entryName, String modelName, boolean absolute) {
	return getRef(server.getRelativeModelRef(entryName, modelName), absolute) ;
    }
    
    public Reference jobsRef(String entryName, String modelName, boolean absolute) {
	return getRef(server.getRelativeJobsRef(entryName, modelName), absolute) ;
    }
    
    public Reference jobRef(String entryName, String modelName, String jobName, boolean absolute) {
	return getRef(server.getRelativeJobRef(entryName, modelName, jobName), absolute) ;
    }
    
    public Reference fileRef(String path, boolean absolute) {
	return getRef(new Reference("/files/" + path), absolute) ;
    }
    
    private Reference getRef(Reference trailingPart, boolean absolute) {
	return absolute ? getAbsoluteRef(trailingPart) : getRelativeRef(trailingPart) ;
    }
    
    private Reference getAbsoluteRef(Reference trailingPart) {
	return new Reference(host.toString() + getRelativeRef(trailingPart)) ;
    }
    
    private Reference getRelativeRef(Reference trailingPart) {
	return new Reference(appRoot.getRelativeRef(host).toString() + trailingPart) ;
    }
}
