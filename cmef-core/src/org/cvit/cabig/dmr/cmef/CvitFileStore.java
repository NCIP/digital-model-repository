package org.cvit.cabig.dmr.cmef;

import java.io.BufferedOutputStream ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;
import java.util.Calendar ;

import org.cvit.cabig.dmr.cmef.domain.File ;

//TODO: file metadata, add from here (requires connection to RDF store) or extend file object to contain all metadata...
//g.add(uri, DC.TITLE, getStringLiteral(_origianlFileName));
//g.add(uri, DC.CREATOR, getURI(usrURI));
//g.add(uri, DC.DATE, getLiteral(DateFormat.getInstance().format(calendar.getTime()), XMLSchema.DATE));
//g.add(uri, DC.TYPE, getStringLiteral(item.getContentType()));
//g.add(uri, DC.SOURCE, getURI(URL_BASE + _url));
public class CvitFileStore implements FileStore {
    private java.io.File storeDir ;
    private String urlRoot ;
    private String user ;

    public CvitFileStore(java.io.File storeDir, String urlRoot, String user) {
	if (storeDir == null) {
	    throw new IllegalArgumentException("Store directory may not be null.") ;
	}
	if (!storeDir.exists() || !storeDir.isDirectory() || !storeDir.canWrite()) {
	    throw new IllegalArgumentException("Store directory must be an existing writable directory.") ;
	}
	if (urlRoot == null || "".equals(urlRoot)) {
	    throw new IllegalArgumentException("URL root must be a non-empty string.") ;
	}
	if (!urlRoot.endsWith("/")) {
	    urlRoot = urlRoot + "/" ;
	}
	if (user == null || "".equals(user)) {
	    throw new IllegalArgumentException("Must specify a non-empty username.") ;
	}
	this.storeDir = storeDir ;
	this.urlRoot = urlRoot ;
	this.user = user ;
    }
    
    @Override
    public File storeFile(String name, InputStream data) throws IOException {
	String localRoot = buildLocalRoot() ;
	String localName = getLocalFileName(name) ;
	java.io.File fileDir = getFileDir(localRoot) ;
	String storeName = buildStoreName(fileDir, localName) ;
	String localUrl = new StringBuilder(localRoot).append(storeName).toString() ;
	copy(data, openFile(storeDir, localUrl)) ;
	
	File result = new File() ;
	result.setName(localName) ;
	result.setSource(buildUrl(localUrl)) ;
	return result ;
    }
    
    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || "".equals(fileUrl)) {
            throw new IllegalArgumentException("File URL must be a non-empty string.") ;
        }
        if (!fileUrl.startsWith(urlRoot)) {
            throw new IllegalArgumentException("Specified file url is not from this File Store.") ;
        }
        String localUrl = fileUrl.substring(urlRoot.length()) ;
        java.io.File localFile = new java.io.File(storeDir, localUrl) ;
        if (localFile.exists()) {
            localFile.delete() ;
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
    
    private java.io.File getFileDir(String relativeDir) {
	java.io.File result = new java.io.File(storeDir, relativeDir) ;
	if (!result.exists()) {
	    result.mkdirs() ;
	}
	return result ;
    }
    
    private String buildStoreName(java.io.File fileDir, String fileName) {
	String storeName = removeIllegalURIChars(fileName) ;
	int count = 0 ;
	String result = storeName ;
	while (new java.io.File(fileDir, result).exists()) {
	    result = count++ + "_" + storeName ;
	}
	return result ;
    }
    
    private OutputStream openFile(java.io.File dir, String localName) throws IOException {
	return new BufferedOutputStream(new FileOutputStream(new java.io.File(dir, localName))) ;
    }
    
    private void copy(InputStream source, OutputStream destination) throws IOException {
	byte[] buff = new byte[8192] ;
	int bytesRead ;
	while ((bytesRead = source.read(buff)) != -1) {
	    destination.write(buff, 0, bytesRead) ;
	}
	destination.flush() ;
	destination.close() ;
	source.close() ;
    }
    
    private String buildUrl(String localUrl) {
	return urlRoot + localUrl ;
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
