/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr;

public class AuthorizationException extends Exception {

    public AuthorizationException(String msg) {
	this(msg, null) ;
    }
    
    public AuthorizationException(String msg, Throwable cause) {
	super(msg, cause) ;
    }
}
