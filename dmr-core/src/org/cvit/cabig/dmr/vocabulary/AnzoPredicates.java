/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.vocabulary;

public class AnzoPredicates {
    private AnzoPredicates() { }
    
    public static final String NAMESPACE = "http://openanzo.org/predicates/" ;
    
    private static String uri(String local) {
	return NAMESPACE + local ;
    }
    
    public static final String USER_ID = uri("userId") ;
    public static final String PASSWORD = uri("password") ;
    public static final String DEFAULT_ROLE = uri("defaultRole") ;
    public static final String USES_ACL = uri("usesAcl") ;
}
