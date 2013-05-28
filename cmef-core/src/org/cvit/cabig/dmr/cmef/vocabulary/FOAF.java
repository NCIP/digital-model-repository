/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */

package org.cvit.cabig.dmr.cmef.vocabulary;

public class FOAF {
    private FOAF() { }
    
    public static final String NAMESPACE = "http://xmlns.com/foaf/0.1/" ;
    
    private static String uri(String local) {
	return NAMESPACE + local ;
    }
    
    public static final String FIRST_NAME = uri("firstName") ;
    public static final String SURNAME = uri("surname") ;
    public static final String MBOX = uri("mbox") ;
    public static final String PHONE = uri("phone") ;
    public static final String DEPICTION = uri("depiction") ;
    public static final String TITLE = uri("title") ;
}
