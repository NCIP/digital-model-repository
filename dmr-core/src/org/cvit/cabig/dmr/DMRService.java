/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr;

import org.cvit.cabig.dmr.domain.DataClassification;
import org.cvit.cabig.dmr.domain.Entry;
import org.cvit.cabig.dmr.domain.Organization;
import org.cvit.cabig.dmr.domain.Reference;

public interface DMRService {
    
    /**
     * This method will not be exposed through caGrid Service.
     * @param userId
     * @throws AuthenticationException 
     * @throws AuthorizationException
     */
    public void setUser(String userId) throws AuthenticationException ;
    
    public Entry addEntry(Entry newEntry, Organization fundingOrganization) throws AuthorizationException, DataSourceException ;
    
    public DataClassification addDataToEntry(DataClassification data, Entry entry) throws AuthorizationException, DataSourceException ;
    
    public Reference addReferenceToEntry(Reference reference, Entry entry) throws AuthorizationException, DataSourceException ;
    
    public void updateEntry(Entry entry) throws AuthorizationException, DataSourceException ;
    
    public void updateData(DataClassification data) throws AuthorizationException, DataSourceException ;
    
    public void updateReference(Reference reference) throws AuthorizationException, DataSourceException ;
}
