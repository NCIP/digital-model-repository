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

package org.cvit.cabig.dmr.integration.dmrservice ;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.cvit.cabig.dmr.anzo.AnzoDMRService;
import org.cvit.cabig.dmr.anzo.AnzoIo;
import org.cvit.cabig.dmr.anzo.AnzoIoException;
import org.cvit.cabig.dmr.domain.DataClassification;
import org.cvit.cabig.dmr.domain.Entry;
import org.cvit.cabig.dmr.domain.Image;
import org.cvit.cabig.dmr.domain.Organization;
import org.cvit.cabig.dmr.domain.Paper;
import org.cvit.cabig.dmr.domain.Reference;
import org.cvit.cabig.dmr.integration.InMemoryDmrFactory;
import org.junit.Before;
import org.openanzo.client.RemoteGraph;
import org.openanzo.common.exceptions.AnzoException;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDmrServiceTests {
    public Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    
    public static final String PI = "pi" ;
    public static final String CONTRIBUTOR = "co" ;
    
    public static final String ENTRY2_ID = "urn:lsid:telar.cambridgesemantics.com:telar0.544376318697641" ;
    
    //Entry with Contributor "co", Published to "reader"
    protected Entry ENTRY1 ;
    protected Image DATA1 ;
    protected Paper REF1 ;
    protected Organization DOD ;
    
    @Before
    public void initTestDataIds() {
        ENTRY1 = new Entry() ;
        ENTRY1.setId("urn:lsid:telar.cambridgesemantics.com:telar0.6345350254148658") ;
        
        DATA1 = new Image() ;
        DATA1.setId("urn:lsid:telar.cambridgesemantics.com:telar0.2224178270121437") ;
        
        REF1 = new Paper() ;
        REF1.setId("urn:lsid:telar.cambridgesemantics.com:telar0.24492447633762038") ;
        
        DOD = new Organization() ;
        DOD.setId("urn:lsid:telar.cambridgesemantics.com:cvit#dod") ;
    }
    
    private InMemoryDmrFactory factory = new InMemoryDmrFactory() ;

    public AbstractDmrServiceTests() {
        super() ;
    }

    protected File getDataFile(String... path) {
        StringBuilder relativePath = new StringBuilder() ;
        for (String part : path) {
            relativePath.append(part + File.separatorChar) ;
        }
        relativePath.deleteCharAt(relativePath.length() - 1) ;
        return new File(getDataDirectory(), relativePath.toString()) ;
    }

    protected File getDataDirectory() {
        return new File("./test-data/dmrservice-tests") ;
    }

    @Before
    public void initDmrService() {
        AnzoIo anzoIo = new AnzoIo() ;
        try {
            anzoIo.resetAnzo(factory.getDatasetService(), getDataFile("init.ttl"), getDataFile("manifest.ttl")) ;
        } catch (AnzoIoException e) {
            throw new RuntimeException("Exception while reseting dataset service.", e) ;
        }
    }

    protected AnzoDMRService getService() {
        return factory.getService() ;
    }
    
    protected static void assertEqualEntries(Entry expected, Entry actual) {
        assertEqualAttributes("id", expected.getId(), actual.getId()) ;
        assertEqualAttributes("title", expected.getTitle(), actual.getTitle()) ;
        assertEqualAttributes("description", expected.getDescription(), actual.getDescription()) ;
        assertEqualAttributes("abstract", expected.getAbstractText(), actual.getAbstractText()) ;
        assertEqualAttributes("concept", expected.getConcept(), actual.getConcept()) ;
        assertEqualAttributes("hypothesis", expected.getHypothesis(), actual.getHypothesis()) ;
        assertEqualAttributes("conclusion", expected.getConclusion(), actual.getConclusion()) ;
        assertEqualAttributes("note", expected.getNote(), actual.getNote()) ;
        assertEqualAttributes("keywords", asSet(expected.getKeywords()), asSet(actual.getKeywords())) ;
    }
    
    protected static void assertEqualData(DataClassification expected, DataClassification actual) {
        assertEqualAttributes("id", expected.getId(), actual.getId()) ;
        assertEqualAttributes("title", expected.getTitle(), actual.getTitle()) ;
        assertEqualAttributes("description", expected.getDescription(), actual.getDescription()) ;
        assertEqualAttributes("source", expected.getSource(), actual.getSource()) ;
        assertEqualAttributes("comment", expected.getComment(), actual.getComment()) ;
    }
    
    protected static void assertEqualReferences(Reference expected, Reference actual) {
        assertEqualAttributes("id", expected.getId(), actual.getId()) ;
        assertEqualAttributes("title", expected.getTitle(), actual.getTitle()) ;
        assertEqualAttributes("description", expected.getDescription(), actual.getDescription()) ;
        assertEqualAttributes("source", expected.getSource(), actual.getSource()) ;
        assertEqualAttributes("comment", expected.getComment(), actual.getComment()) ;
    }
    
    protected static void assertEqualAttributes(String attr, Object expected, Object actual) {
        if (expected == null) {
            if (actual != null) {
                throw new AssertionError("Attribute " + attr + ": Expected: null; but was: " + actual) ;
            }
            return ;
        }
        if (!expected.equals(actual)) {
            throw new AssertionError("Attribute " + attr + ": Expected: " + expected + "; but was: " + actual) ;
        }
    }
    
    protected void assertHasStatements(URI graphUri, Statement... statements) {
        RemoteGraph graph = null ;
        try {
        graph = factory.getSystemService().getRemoteGraph(graphUri, false) ;
        for (Statement stmt : statements) {
            if (!graph.contains(stmt)) {
                throw new AssertionError("Graph does not have statement: " + stmt + ".") ;
            }
        }
        } catch (AnzoException e) {
            throw new RuntimeException("Exception checking graph: " + graphUri + ".", e) ;
        } finally {
            if (graph != null) {
                graph.close() ;
            }
        }
    }
    
    protected void dumpDatasetService(File outputDir) {
        try {
            new AnzoIo().dumpAll(factory.getSystemService(), outputDir) ;
        } catch (Exception e) {
            throw new RuntimeException("Exception while dumping dataset service.", e) ;
        }
    }
    
    protected Statement statement(Resource subject, URI predicate, Value object) {
        return getValueFactory().createStatement(subject, predicate, object) ;
    }
    
    protected URI uri(String uri) {
        return getValueFactory().createURI(uri) ;
    }
    
    private ValueFactory getValueFactory() {
        return factory.getDatasetService().getValueFactory() ;
    }
    
    private static <T> Set<T> asSet(Collection<T> objects) {
        if (objects == null) {
            return null ;
        }
        Set<T> result = new HashSet<T>() ;
        result.addAll(objects) ;
        return result ;
    }
}
