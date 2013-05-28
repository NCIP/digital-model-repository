/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.integration.dmrservice ;

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

public class PiUserTests extends AbstractDmrServiceTests {

    @Before
    public void setUserToPi() throws AuthenticationException {
        getService().setUser("pi") ;
    }
    
    @Test public void piCanAddEntry() throws AuthorizationException, DataSourceException {
        Entry newEntry = new Entry() ;
        newEntry.setTitle("My Entry") ;
        getService().addEntry(newEntry, null) ;
    }

    @Test public void piCanAddDataToEntry() throws AuthorizationException, DataSourceException {
        DataClassification data = new Image() ;
        data.setTitle("My Image") ;
        getService().addDataToEntry(data, ENTRY1) ;
    }

    @Test public void piCanAddReferenceToEntry() throws AuthorizationException, DataSourceException {
        Reference ref = new Paper() ;
        ref.setTitle("My Paper") ;
        getService().addReferenceToEntry(ref, ENTRY1) ;
    }

    @Test public void piCanUpdateEntry() throws AuthorizationException, DataSourceException {
        ENTRY1.setNote("Updated note...") ;
        getService().updateEntry(ENTRY1) ;
    }

    @Test public void piCanUpdateData() throws AuthorizationException, DataSourceException {
        DATA1.setComment("Updated comment...") ;
        getService().updateData(DATA1) ;
    }

    @Test public void piCanUpdateReference() throws AuthorizationException, DataSourceException {
        REF1.setComment("Updated comment...") ;
        getService().updateReference(REF1) ;
    }
}
