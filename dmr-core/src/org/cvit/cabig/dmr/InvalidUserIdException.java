/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr;

public class InvalidUserIdException extends Exception {

    public InvalidUserIdException(String message) {
	this(message, null) ;
    }
    
    public InvalidUserIdException(String message, Throwable cause) {
	super(message, cause) ;
    }
}
