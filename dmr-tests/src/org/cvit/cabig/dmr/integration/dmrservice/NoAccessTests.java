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
import org.junit.Before;
import org.junit.Test;

public class NoAccessTests extends AbstractDmrServiceTests {

    @Before
    public void setToNoAccessUser() throws AuthenticationException {
        getService().setUser("noaccess") ;
    }
    
    @Test(expected=AuthorizationException.class) 
    public void nonPiCanNotAddEntry() throws AuthorizationException, DataSourceException {
        Entry newEntry = new Entry() ;
        newEntry.setTitle("My Entry") ;
        newEntry = getService().addEntry(newEntry, null) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void userWithNoAccessCannotSeeEntry() throws AuthorizationException, DataSourceException {
        DataClassification data = new Image() ;
        data.setTitle("My Image") ;
        data = getService().addDataToEntry(data, ENTRY1) ;
    }
    
    @Test(expected=AuthorizationException.class)
    public void userWithNoAccessCannotUpdateEntry() throws AuthorizationException, DataSourceException {
        ENTRY1.setNote("This is my new note.") ;
        getService().updateEntry(ENTRY1) ;
    }
    
    @Test(expected=AuthorizationException.class)
    public void userWithNoAccessCannotUpdateData() throws AuthorizationException, DataSourceException {
        DATA1.setComment("This is a newer test note that I am trying to update") ;
        getService().updateData(DATA1) ;
    }
    
    @Test(expected=AuthorizationException.class)
    public void userWithNoAccessCannotUpdateReference() throws AuthorizationException, DataSourceException {
        REF1.setComment("This is a newer test note that I am trying to update") ;
        getService().updateReference(REF1) ;
    }
}
