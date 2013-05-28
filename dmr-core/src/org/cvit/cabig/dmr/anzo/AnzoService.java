/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.anzo ;

import java.util.Map ;
import java.util.Set ;

import org.cvit.cabig.dmr.DataSourceException ;

import com.infotechsoft.rdf.RdfFactory ;
import com.infotechsoft.rdf.Statement ;
import com.infotechsoft.rdf.Uri ;

public interface AnzoService {

    public void updateGraphs(Map<Uri, Set<Statement>> toRemove, Map<Uri, Set<Statement>> toAdd) throws DataSourceException ;

    public void setCurrentUser(String userId) ;

    public String getCurrentUser() ;

    public RdfFactory getRdfFactory() ;

    public Uri newId() ;
}