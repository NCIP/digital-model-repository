/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef;

public class UserCredentials {
    private String username ;
    private String password ;
    
    public UserCredentials(String username, String password) {
	if (username == null) {
	    throw new IllegalArgumentException("Username may not be null.") ;
	}
	if (password == null) {
	    throw new IllegalArgumentException("Password may not be null.") ;
	}
	this.username = username ;
	this.password = password ;
    }
    
    public String getUsername() {
	return username ;
    }
    
    public String getPassword() {
	return password ;
    }
}
