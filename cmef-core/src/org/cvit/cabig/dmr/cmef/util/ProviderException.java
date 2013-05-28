/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.util;

public class ProviderException extends Exception {

    public ProviderException(String msg) {
	super(msg) ;
    }
    
    public ProviderException(String msg, Throwable cause) {
	super(msg, cause) ;
    }
}
