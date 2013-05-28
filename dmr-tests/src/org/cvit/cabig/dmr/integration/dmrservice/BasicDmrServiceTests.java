/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.integration.dmrservice;

import java.util.Arrays;

import org.cvit.cabig.dmr.AuthenticationException;
import org.cvit.cabig.dmr.AuthorizationException;
import org.cvit.cabig.dmr.DataSourceException;
import org.cvit.cabig.dmr.domain.DataClassification;
import org.cvit.cabig.dmr.domain.Entry;
import org.cvit.cabig.dmr.domain.Image;
import org.cvit.cabig.dmr.domain.Organization;
import org.cvit.cabig.dmr.domain.Paper;
import org.cvit.cabig.dmr.domain.Reference;
import org.cvit.cabig.dmr.vocabulary.CViT;
import org.junit.Before;
import org.junit.Test;

public class BasicDmrServiceTests extends AbstractDmrServiceTests {
    private Entry ENTRY ;
    private Paper PAPER ;
    private Image IMAGE ;
    
    @Before
    public void initTestData() {
        ENTRY = new Entry() ;
        ENTRY.setTitle("DMRService entry") ;
        ENTRY.setDescription("This is an entry that was added by the DMRService") ;
        ENTRY.setAbstractText("Abstract...") ;
        ENTRY.setConcept("Concept...") ;
        ENTRY.setHypothesis("Hypothesis...") ;
        ENTRY.setConclusion("Conclusion...") ;
        ENTRY.setNote("Note...") ;
        ENTRY.setKeywords(Arrays.asList("Cancer", "Metastasis")) ;
        
        PAPER = new Paper();
        PAPER.setTitle("DMRService Paper") ;
        PAPER.setDescription("This paper was added by the DMRService") ;
        PAPER.setSource("http://www.example.org/DMRServicePaper") ;
        PAPER.setComment("A comment...") ;
        
        IMAGE = new Image() ;
        IMAGE.setTitle("DMRService Image") ;
        IMAGE.setDescription("This image was added by the DMRService") ;
        IMAGE.setSource("http://www.oneworldmovement.org/Cancer%20cell%20-%20breast.jpg") ;
        IMAGE.setComment("A comment...") ;
    }
    
    @Before
    public void setUserToPi() throws AuthenticationException {
        getService().setUser(PI) ;
    }
    
    @Test public void canAddEntry() throws AuthorizationException, DataSourceException {
	Entry added = getService().addEntry(ENTRY, null) ;
	Entry returned = getService().getEntry(added.getId()) ;
	
	assertEqualEntries(added, returned) ;
    }
    
    @Test public void canAddEntryWithFundingOrganization() throws AuthorizationException, DataSourceException {
        Entry added = getService().addEntry(ENTRY, DOD) ;
        
        assertHasStatements(
                uri(added.getId()), 
                statement(
                        uri(added.getId()), 
                        uri(CViT.ObjectProperties.FUNDING_ORGANIZATION), 
                        uri(DOD.getId()))) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cannotAddEntryWithNonExistentFundingOrganization() throws AuthorizationException, DataSourceException {
        Organization org = new Organization() ;
        org.setId("invalid") ;
        getService().addEntry(ENTRY, org) ;
    }
    
    @Test public void canAddReferenceToEntry() throws AuthorizationException, DataSourceException {
        Reference added = getService().addReferenceToEntry(PAPER, ENTRY1) ;
        Reference returned = getService().getReference(added.getId()) ;
        
        assertEqualReferences(added, returned) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cannotAddReferenceToNonExistentEntry() throws AuthorizationException, DataSourceException {
        getService().addReferenceToEntry(PAPER, ENTRY) ;
    }
    
    @Test public void canAddDataToEntry() throws AuthorizationException, DataSourceException {
        DataClassification added = getService().addDataToEntry(IMAGE, ENTRY1) ;
        DataClassification returned = getService().getData(added.getId()) ;
        
        assertEqualData(added, returned) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cannotAddDataToNonExistentEntry() throws AuthorizationException, DataSourceException {
        getService().addDataToEntry(IMAGE, ENTRY) ;
    }
    
    @Test
    public void canUpdateEntry() throws AuthorizationException, DataSourceException {
        Entry entry1 = getService().getEntry(ENTRY1.getId()) ;
        entry1.setNote("My updated note...") ;
        getService().updateEntry(entry1) ;
        Entry returned = getService().getEntry(ENTRY1.getId()) ;
        
        assertEqualEntries(entry1, returned) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cannotUpdateNonExistentEntry() throws AuthorizationException, DataSourceException {
        ENTRY.setId("invalid") ;
        getService().updateEntry(ENTRY) ;
    }
    
    @Test public void canUpdateData() throws AuthorizationException, DataSourceException {
        DataClassification data1 = getService().getData(DATA1.getId()) ;
        data1.setComment("My updated comment...") ;
        getService().updateData(data1) ;
        DataClassification returned = getService().getData(DATA1.getId()) ;
        
        assertEqualData(data1, returned) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cannotUpdateNonExistentData() throws AuthorizationException, DataSourceException {
        IMAGE.setId("invalid") ;
        getService().updateData(IMAGE) ;
    }
    
    @Test public void canUpdateReference() throws AuthorizationException, DataSourceException {
        Reference ref1 = getService().getReference(REF1.getId()) ;
        ref1.setComment("My updated comment...") ;
        getService().updateReference(ref1) ;
        Reference returned = getService().getReference(REF1.getId()) ;
        
        assertEqualReferences(ref1, returned) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cannotUpdateNonExistentReference() throws AuthorizationException, DataSourceException {
        PAPER.setId("invalid") ;
        getService().updateReference(PAPER) ;
    }
}
