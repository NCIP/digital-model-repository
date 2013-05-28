/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
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
