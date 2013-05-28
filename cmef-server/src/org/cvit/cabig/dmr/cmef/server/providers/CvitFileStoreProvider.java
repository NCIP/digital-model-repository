/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server.providers;

import java.io.File ;

import org.cvit.cabig.dmr.cmef.CvitFileStore ;
import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.cvit.cabig.dmr.cmef.util.Provider ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;

public class CvitFileStoreProvider implements Provider<FileStore> {
    private File storeDir ;
    private String storeRootUrl ;

    public CvitFileStoreProvider(File storeDir, String storeRootUrl) {
	if (storeDir == null) {
	    throw new IllegalArgumentException("Store directory must not be null.") ;
	}
	if (storeRootUrl == null) {
	    throw new IllegalArgumentException("Store root URL must not be null.") ;
	}
	this.storeDir = storeDir ;
	//TODO: see about getting host from application context...
	this.storeRootUrl = storeRootUrl ;
    }
    
    public FileStore provide(UserCredentials credentials) throws ProviderException {
	return new CvitFileStore(storeDir, storeRootUrl, credentials.getUsername()) ;
    }
}
