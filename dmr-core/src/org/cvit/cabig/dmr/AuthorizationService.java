/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr;

public interface AuthorizationService {

    public UserAuthorizer getUserAuthorizer(String userId) throws InvalidUserIdException ;

    public void updateObjectsAclFromEntry(String objectId, String entryId) throws DataSourceException ;
}
