/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.integration.dmrservice;

import org.cvit.cabig.dmr.AuthenticationException;
import org.cvit.cabig.dmr.AuthorizationException;
import org.cvit.cabig.dmr.DataSourceException;
import org.cvit.cabig.dmr.domain.DataClassification;
import org.cvit.cabig.dmr.domain.Entry;
import org.cvit.cabig.dmr.domain.Image;
import org.cvit.cabig.dmr.domain.Paper;
import org.cvit.cabig.dmr.domain.Reference;
import org.junit.Before;
import org.junit.Test;

public class ContributorTests extends AbstractDmrServiceTests {

    @Before
    public void setUserToContributor() throws AuthenticationException {
        getService().setUser(CONTRIBUTOR) ;
    }
    
    @Test
    public void piContributorCanAddEntry() throws AuthorizationException, DataSourceException {
        Entry entry = new Entry() ;
        entry.setTitle("My Title") ;
        entry = getService().addEntry(entry, null) ;
    }
    
    @Test
    public void canAddDataToEntryContributorOf() throws AuthorizationException, DataSourceException {
        DataClassification data = new Image() ;
        data.setTitle("My Image") ;
        data = getService().addDataToEntry(data, ENTRY1) ;
    }
    
    @Test(expected=AuthorizationException.class)
    public void cannotAddDataToEntryNotContributorOf() throws AuthorizationException, DataSourceException {
        Entry entry = new Entry() ;
        entry.setId(ENTRY2_ID) ;
        DataClassification data = new Image() ;
        data.setTitle("My Image") ;
        data = getService().addDataToEntry(data, entry) ;
    }
    
    @Test
    public void canAddReferenceToEntryContributorOf() throws AuthorizationException, DataSourceException {
        Reference ref = new Paper() ;
        ref.setTitle("My Image") ;
        ref = getService().addReferenceToEntry(ref, ENTRY1) ;
    }
    
    @Test(expected=AuthorizationException.class)
    public void cannotAddReferenceToEntryNotContributorOf() throws AuthorizationException, DataSourceException {
        Entry entry = new Entry() ;
        entry.setId(ENTRY2_ID) ;
        Reference ref = new Paper() ;
        ref.setTitle("My Image") ;
        ref = getService().addReferenceToEntry(ref, entry) ;
    }
    
    @Test
    public void canUpdateEntryContributorOf() throws AuthorizationException, DataSourceException {
        ENTRY1.setNote("This is a new test note that I am trying to update") ;
        getService().updateEntry(ENTRY1) ;
    }
    
    @Test(expected=AuthorizationException.class)
    public void cannotUpdateEntryNotContributorOf() throws AuthorizationException, DataSourceException {
        Entry entry = new Entry() ;
        entry.setId(ENTRY2_ID) ;
        entry.setNote("This is a new test note that I am trying to update") ;
        getService().updateEntry(entry) ;
    }
    
    @Test(expected=AuthorizationException.class)
    public void cannotUpdateDataForEntryNotContributorOf() throws AuthorizationException, DataSourceException {
        DATA1.setComment("This is a new test comment that I am trying to update") ;
        getService().updateData(DATA1) ;
    }
    
    @Test
    public void canUpdateReferenceForEntryContributorOf() throws AuthorizationException, DataSourceException {
        REF1.setComment("This is a new test comment that I am trying to update") ;
        getService().updateReference(REF1) ;
    }
}
