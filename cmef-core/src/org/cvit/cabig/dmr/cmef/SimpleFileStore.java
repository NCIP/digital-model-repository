package org.cvit.cabig.dmr.cmef;

import java.io.BufferedOutputStream ;
import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;
import java.net.URI ;

public class SimpleFileStore implements FileStore {
    private File directory ;
    private URI httpUri ;

    public SimpleFileStore(File directory, URI httpUri) {
	if (directory == null) {
	    throw new IllegalArgumentException("Directory may not be null.") ;
	}
	if (httpUri == null) {
	    throw new IllegalArgumentException("HTTP URI may not be null.") ;
	}
	if (!directory.exists() || !directory.isDirectory()) {
	    throw new IllegalArgumentException("Directory must exist and be a directory.") ;
	}
	if (!"http".equalsIgnoreCase(httpUri.getScheme()) || !httpUri.isAbsolute()) {
	    throw new IllegalArgumentException("Passed in URI must be absolute and use the HTTP scheme.") ;
	}
	this.directory = directory ;
	this.httpUri = httpUri ;
    }
    
    public org.cvit.cabig.dmr.cmef.domain.File storeFile(String name, InputStream data) throws IOException {
	File file = new File(directory, name) ;
	
	file.createNewFile() ;
	OutputStream os = new BufferedOutputStream(new FileOutputStream(file)) ;
	byte[] buff = new byte[8192] ;
	int bytesRead ;
	while ((bytesRead = data.read(buff)) != -1) {
	    os.write(buff, 0, bytesRead) ;
	}
	os.flush() ;
	os.close() ;
	data.close() ;
	
	org.cvit.cabig.dmr.cmef.domain.File result = new org.cvit.cabig.dmr.cmef.domain.File() ;
	result.setName(name) ;
	result.setSource(httpUri.resolve(name).toString()) ;
	return result ;
    }

    public void deleteFile(String fileUri) {
        throw new UnsupportedOperationException("Implement me!!!") ;
    }
}
