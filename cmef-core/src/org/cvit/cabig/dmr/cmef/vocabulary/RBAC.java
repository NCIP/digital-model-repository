/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.cmef.vocabulary;

public class RBAC {
    private RBAC() { }
    
    public static final String NAMESPACE = "http://openanzo.org/RBAC#" ;
    
    private static String uri(String local) {
	return NAMESPACE + local ;
    }
    
    public static final String ROLE = uri("Role") ;
    public static final String IN_ROLE = uri("inRole") ;
    
    public static final String USER = uri("User") ;
    
    public static final String ACL = uri("ACL") ;
    public static final String ADD = uri("add") ;
    public static final String CHANGE_ACL = uri("changeNamedGraphACL") ;
    public static final String INSERT_GRAPH = uri("insertNamedGraph") ;
    public static final String READ = uri("read") ;
    public static final String REMOVE = uri("remove") ;
    public static final String REMOVE_GRAPH = uri("removeNamedGraph") ;
}
