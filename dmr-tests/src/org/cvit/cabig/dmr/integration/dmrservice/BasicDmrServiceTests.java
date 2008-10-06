/*
 * caBIG™ Open Source Software License v.2 Digital Model Repository (DMR)
 * 
 * Copyright Notice. Copyright 2008 Massachusetts General Hospital (“caBIG™
 * Participant”). Digital Model Repository (DMR) was created with NCI funding
 * and is part of the caBIG™ initiative. The software subject to this notice and
 * license includes both human readable source code form and machine readable,
 * binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant
 * and You. “You (or “Your”) shall mean a person or an entity, and all other
 * entities that control, are controlled by, or are under common control with
 * the entity. “Control” for purposes of this definition means (i) the direct or
 * indirect power to cause the direction or management of such entity, whether
 * by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of
 * the outstanding shares, or (iii) beneficial ownership of such entity.
 * 
 * License. Provided that You agree to the conditions described below, caBIG™
 * Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caBIG™ Software, including any copyright or patent rights
 * therein, to (i) use, install, disclose, access, operate, execute, reproduce,
 * copy, modify, translate, market, publicly display, publicly perform, and
 * prepare derivative works of the caBIG™ Software in any manner and for any
 * purpose, and to have or permit others to do so; (ii) make, have made, use,
 * practice, sell, and offer for sale, import, and/or otherwise dispose of
 * caBIG™ Software (or portions thereof); (iii) distribute and have distributed
 * to and by third parties the caBIG™ Software and any modifications and
 * derivative works thereof; and (iv) sublicense the foregoing rights set out in
 * (i), (ii) and (iii) to third parties, including the right to license such
 * rights to further third parties. For sake of clarity, and not by way of
 * limitation, caBIG™ Participant shall have no right of accounting or right of
 * payment from You or Your sublicensees for the rights granted under this
 * License. This License is granted at no charge to You. Your downloading,
 * copying, modifying, displaying, distributing or use of caBIG™ Software
 * constitutes acceptance of all of the terms and conditions of this Agreement.
 * If you do not agree to such terms and conditions, you have no right to
 * download, copy, modify, display, distribute or use the caBIG™ Software.
 * 
 * 1. Your redistributions of the source code for the caBIG™ Software must
 * retain the above copyright notice, this list of conditions and the disclaimer
 * and limitation of liability of Article 6 below. Your redistributions in
 * object code form must reproduce the above copyright notice, this list of
 * conditions and the disclaimer of Article 6 in the documentation and/or other
 * materials provided with the distribution, if any.
 * 
 * 2. Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: “This product includes software
 * developed by Massachusetts General Hospital.” If You do not include such
 * end-user documentation, You shall include this acknowledgment in the caBIG™
 * Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3. You may not use the names ”Massachusetts General Hospital”, “MGH”, “The
 * National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™”
 * to endorse or promote products derived from this caBIG™ Software. This
 * License does not authorize You to use any trademarks, service marks, trade
 * names, logos or product names of either caBIG™ Participant, NCI or caBIG™,
 * except as required to comply with the terms of this License.
 * 
 * 4. For sake of clarity, and not by way of limitation, You may incorporate
 * this caBIG™ Software into Your proprietary programs and into any third party
 * proprietary programs. However, if You incorporate the caBIG™ Software into
 * third party proprietary programs, You agree that You are solely responsible
 * for obtaining any permission from such third parties required to incorporate
 * the caBIG™ Software into such third party proprietary programs and for
 * informing Your sublicensees, including without limitation Your end-users, of
 * their obligation to secure any required permissions from such third parties
 * before incorporating the caBIG™ Software into such third party proprietary
 * software programs. In the event that You fail to obtain such permissions, You
 * agree to indemnify caBIG™ Participant for any claims against caBIG™
 * Participant by such third parties, except to the extent prohibited by law,
 * resulting from Your failure to obtain such permissions.
 * 
 * 5. For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the caBIG™ Software, or any derivative works
 * of the caBIG™ Software as a whole, provided Your use, reproduction, and
 * distribution of the Work otherwise complies with the conditions stated in
 * this License.
 * 
 * 6. THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 * DISCLAIMED. IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU) Contributors:
 * INFOTECH Soft, Inc.
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
