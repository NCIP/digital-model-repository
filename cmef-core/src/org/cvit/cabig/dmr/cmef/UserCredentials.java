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
