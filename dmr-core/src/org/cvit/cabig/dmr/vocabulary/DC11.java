/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.vocabulary;

public class DC11 {
    private DC11() { }
    
    public static final String NAMESPACE = "http://purl.org/dc/elements/1.1/" ;
    
    private static String uri(String local) {
	return NAMESPACE + local ;
    }
    
    public static final String TITLE = uri("title") ;
    public static final String SOURCE = uri("source") ;
    public static final String DESCRIPTION = uri("description") ;
    public static final String CONTRIBUTOR = uri("contributor") ;
    public static final String CREATOR = uri("creator") ;
}
