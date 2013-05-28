/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server;

import java.io.IOException ;
import java.io.InputStream ;
import java.util.Calendar ;
import java.util.Collections ;

import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.restlet.data.Method ;
import org.restlet.data.Status ;
import org.restlet.data.Tag ;
import org.restlet.representation.InputRepresentation ;
import org.restlet.resource.ClientResource ;

public class HttpFileStore implements FileStore {
    private String storeUrl ;
    private String rootUrl ;
    private String user ;

    public HttpFileStore(String storeUrl, String rootUrl, String user) {
	if (storeUrl == null || "".equals(storeUrl)) {
	    throw new IllegalArgumentException("Must specify HTTP file store URL.") ;
	}
	if (user == null || "".equals(user)) {
	    throw new IllegalArgumentException("Must specify user name.") ;
	}
	if (rootUrl == null || "".equals(rootUrl)) {
	    rootUrl = storeUrl ;
	}
	this.storeUrl = storeUrl ;
	this.rootUrl = rootUrl ;
	this.user = user ;
    }
    
    public File storeFile(String name, InputStream data) throws IOException {
	String localRoot = buildLocalRoot() ;
	String localName = getLocalFileName(name) ;
	String storeRoot = storeUrl + localRoot ;
	String extRoot = rootUrl + localRoot ;
	
	String fileUrl = null ;
	String extUrl = null ;
	boolean stored = false ;
	while (!stored) {
	    String storeName = buildStoreName(storeRoot, localName) ;
	    fileUrl = storeRoot + storeName ;
	    extUrl = extRoot + storeName ;
	    Status status = putFile(data, fileUrl) ;
	    if (!status.isSuccess()) {
		if (!Status.CLIENT_ERROR_PRECONDITION_FAILED.equals(status)) {
		    throw new IOException("Exception communicating with HTTP file store: " + status) ;
		}
	    } else {
		stored = true ;
	    }
	}

	File result = new File() ;
	result.setName(localName) ;
	result.setSource(extUrl) ;
	return result ;
    }
    
    public void deleteFile(String fileUri) throws IOException {
	ClientResource res = new ClientResource(fileUri) ;
	
	res.setMethod(Method.DELETE) ;
	res.handle() ;
	if (res.getStatus().isError()) {
	    throw new IOException("Error deleting " + fileUri + ": " + res.getStatus() + ".") ;
	}
    }

    private String buildLocalRoot() {
	Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        return new StringBuilder(user).append("/").append(year).append("/").append(month).append("/").append(day).append("/").toString() ;
    }
    
    private String getLocalFileName(String fileName) {
	int startIndex = Math.max(fileName.lastIndexOf("/"), fileName.lastIndexOf("\\")) + 1 ;
        return (startIndex < fileName.length()) ? fileName.substring(startIndex) : fileName ;
    }
    
    private String buildStoreName(String storeDir, String fileName) throws IOException {
	String storeName = removeIllegalURIChars(fileName) ;
	int count = 0 ;
	String result = storeName ;
	while (exists(storeDir + result)) {
	    result = count++ + "_" + storeName ;
	}
	return result ;
    }
    
    private boolean exists(String url) throws IOException {
	ClientResource res = new ClientResource(url) ;
	
	res.setMethod(Method.HEAD) ;
	res.handle() ;
	if (res.getStatus().isSuccess()) {
	    return true ;
	} else {
	    if (Status.CLIENT_ERROR_NOT_FOUND.equals(res.getStatus())) {
		return false ;
	    } else {
		throw new IOException("Exception communicating with HTTP file store: " + res.getStatus()) ;
	    }
	}
    }
    
    private Status putFile(InputStream data, String url) {
	ClientResource res = new ClientResource(url) ;
	
	res.setMethod(Method.PUT) ;
	res.getConditions().setNoneMatch(Collections.singletonList(Tag.ALL)) ;
	res.getRequest().setEntity(new InputRepresentation(data)) ;
	res.handle() ;
	return res.getStatus() ;
    }
    
    private static String removeIllegalURIChars(String s) {
        String s2 = removeSpaces(s);

        char[] chars = s2.toCharArray();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\'' || chars[i] == '"')
                b.append("_");
            else
                b.append(chars[i]);
        }

        return b.toString();
    }

    private static String removeSpaces(String s) {
        char[] chars = s.toCharArray();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ')
                b.append("_");
            else
                b.append(chars[i]);
        }
        return b.toString();
    }
}
