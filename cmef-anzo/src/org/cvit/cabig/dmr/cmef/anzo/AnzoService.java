/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.cmef.anzo ;

import java.util.Map ;
import java.util.Set ;

import org.cvit.cabig.dmr.cmef.RepositoryException ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;

import com.infotechsoft.rdf.RdfFactory ;
import com.infotechsoft.rdf.Statement ;
import com.infotechsoft.rdf.Uri ;

public interface AnzoService {

    public void updateGraphs(Map<Uri, Set<Statement>> toRemove, Map<Uri, Set<Statement>> toAdd) throws RepositoryException ;

    //TODO: this should be updated in dmr-core project...
    public void setCurrentUser(UserCredentials user) ;

    public String getCurrentUser() ;

    public RdfFactory getRdfFactory() ;

    public Uri newId() ;
    
}