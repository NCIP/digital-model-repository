/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr ;

import com.infotechsoft.rdf.Uri ;

public interface UserAuthorizer {

    public Uri getUri() ;

    public boolean canAddEntry() throws AuthorizationException ;

    public boolean canEditObject(String objectId) throws AuthorizationException ;

}