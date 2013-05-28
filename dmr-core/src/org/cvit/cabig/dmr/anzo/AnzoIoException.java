/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.anzo;

public class AnzoIoException extends Exception {

    public AnzoIoException(String msg) {
        super(msg) ;
    }
    
    public AnzoIoException(String msg, Throwable cause) {
        super(msg, cause) ;
    }
}
