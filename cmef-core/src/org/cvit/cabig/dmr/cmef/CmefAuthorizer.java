/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef;

import java.util.Set ;

import com.infotechsoft.rdf.Uri ;

public interface CmefAuthorizer {

    Uri getUserUri() ;

    boolean canEditResource(Uri id) throws AuthorizationException ;

    boolean canViewResource(Uri id) throws AuthorizationException ;

    void updateObjectsAclFromEntry(Set<String> modelIds, String id) throws AuthorizationException ;
}
