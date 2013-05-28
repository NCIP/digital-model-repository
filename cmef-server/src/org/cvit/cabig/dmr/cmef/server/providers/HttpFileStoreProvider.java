/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server.providers;

import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.cvit.cabig.dmr.cmef.server.HttpFileStore ;
import org.cvit.cabig.dmr.cmef.util.Provider ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;

public class HttpFileStoreProvider implements Provider<FileStore> {
    private String storeUrl ;
    private String rootUrl ;

    public HttpFileStoreProvider(String storeUrl, String rootUrl) {
	if (storeUrl == null || "".equals(storeUrl)) {
	    throw new IllegalArgumentException("Must provide a valid HTTP file store url.") ;
	}
	this.storeUrl = storeUrl ;
	this.rootUrl = rootUrl ;
    }
    
    public FileStore provide(UserCredentials credentials) throws ProviderException {
        return new HttpFileStore(storeUrl, rootUrl, credentials.getUsername()) ;
    }
}
