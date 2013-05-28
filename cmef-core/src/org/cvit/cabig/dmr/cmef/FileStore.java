/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef;

import java.io.IOException ;
import java.io.InputStream ;

import org.cvit.cabig.dmr.cmef.domain.File ;

public interface FileStore {

    public File storeFile(String name, InputStream data) throws IOException ;
    
    public void deleteFile(String fileUri) throws IOException ;
}
