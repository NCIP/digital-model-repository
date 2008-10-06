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

package org.cvit.cabig.dmr.anzo;

import info.aduna.collections.iterators.CloseableIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.cvit.cabig.dmr.vocabulary.AnzoPredicates;
import org.openanzo.client.DatasetService;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.common.ontology.ACL;
import org.openanzo.common.ontology.AnzoFactory;
import org.openanzo.common.ontology.Role;
import org.openanzo.common.ontology.User;
import org.openanzo.model.Constants;
import org.openanzo.model.INamedGraph;
import org.openanzo.model.INamedGraphWithMetaData;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.util.GraphUtil;
import org.openrdf.model.util.GraphUtilException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;
import org.openrdf.rio.turtle.TurtleWriter;

import com.infotechsoft.rdf.vocabulary.Rdf;

public class AnzoIo {
    private static final String SYSTEM_GRAPH = "http://infotechsoft.com/anzo#SystemGraph" ;
    private static final String NAMED_GRAPH = "http://infotechsoft.com/anzo#NamedGraph" ;
    private static final String META_GRAPH = "http://infotechsoft.com/anzo#metaGraph" ;
    private static final String GRAPH_URI = "http://infotechsoft.com/anzo#uri" ;
    
    private long graphNumber = 1L ;

    public void exportInitialGraph(DatasetService anzo, File outputDir) throws AnzoIoException {
        if (anzo == null) {
            throw new IllegalArgumentException("Dataset service may not be null.") ;
        }
        if (outputDir == null) {
            throw new IllegalArgumentException("Output directory may not be null.") ;
        }
        prepareOutputDirectory(outputDir) ;
        createInitialGraph(anzo, outputDir) ;
    }
    
    public void exportNamedGraphs(DatasetService anzo, File outputDir) throws AnzoIoException {
        if (anzo == null) {
            throw new IllegalArgumentException("Dataset service may not be null.") ;
        }
        if (outputDir == null) {
            throw new IllegalArgumentException("Output directory may not be null.") ;
        }
        prepareOutputDirectory(outputDir) ;
        createNamedGraphs(anzo, outputDir) ;
    }
    
    public void resetAnzo(DatasetService anzo, File initStmts) throws AnzoIoException {
        resetAnzo(anzo, initStmts, null) ;
    }
    
    public void resetAnzo(DatasetService anzo, File initStmts, File manifest) throws AnzoIoException {
        if (anzo == null) {
            throw new IllegalArgumentException("Dataset service may not be null.") ;
        }
        if (initStmts == null) {
            throw new IllegalArgumentException("Must specifiy initial statements file.") ;
        }
        Graph stmts = loadGraph(initStmts) ;
        try {
            anzo.getModelService().reset(stmts) ;
        } catch (AnzoException e) {
            throw new AnzoIoException("Exception while reseting dataset service.", e) ;
        }
        if (manifest != null) {
            loadGraphsFromManifest(anzo, manifest) ;
        }
    }
    
    public void loadGraphsFromManifest(DatasetService anzo, File manifestFile) throws AnzoIoException {
        String baseUrl ;
        try {
            String manifestPath = manifestFile.getParentFile().getCanonicalPath() ;
            baseUrl = "file://" + manifestPath + File.separator ;
        } catch (IOException e) {
            throw new AnzoIoException("Error generating base url for manifest file: " + manifestFile.getPath() + ".", e) ;
        }
        Graph manifest = loadGraph(manifestFile, baseUrl) ;
        Iterator<Resource> namedGraphs = GraphUtil.getSubjectIterator(manifest, uri(Rdf.TYPE), uri(NAMED_GRAPH)) ;
        while (namedGraphs.hasNext()) {
            Resource namedGraph = namedGraphs.next() ;
            loadNamedGraph(anzo, manifest.match(namedGraph, null, null)) ;
        }
    }
    
    private void createInitialGraph(DatasetService anzo, File outputDir) throws AnzoIoException {
        INamedGraphWithMetaData graph = null ;
        INamedGraph metaGraph = null ;
        try {
            graph = anzo.getRemoteSystemGraph() ;
            metaGraph = graph.getMetaDataGraph() ;
            createMetaGraph(metaGraph, graph.getNamedGraphUri(), new File(outputDir, "init.ttl")) ;
        } catch (AnzoException e) {
            throw new AnzoIoException("Exception while creating initial graph.", e) ;
        } finally {
            if (metaGraph != null) {
                metaGraph.close() ;
            }
            if (graph != null) {
                graph.close() ;
            }
        }
    }
    
    private void createMetaGraph(INamedGraph metaGraph, URI graphUri, File graphFile) throws AnzoIoException {
        FileWriter fileWriter = null ;
        try {
            fileWriter = new FileWriter(graphFile) ;
            RDFWriter rdfWriter = new TurtleWriter(fileWriter) ;
            rdfWriter.startRDF() ;
            addMetagraphNamespaces(rdfWriter) ;
            createRoles(rdfWriter, metaGraph) ;
            createAcls(rdfWriter, metaGraph) ;
            createUsers(rdfWriter, metaGraph) ;
            createGraph(rdfWriter, metaGraph, graphUri) ;
            rdfWriter.endRDF() ;
        } catch (Exception e) {
            throw new AnzoIoException("Exception while creating initial graph.", e) ;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close() ;
                } catch (IOException e) {
                    throw new AnzoIoException("Exception while closing initial graph writer.") ;
                }
            }
        }
    }
    
    private void createNamedGraphs(DatasetService anzo, File outputDir) throws AnzoIoException {
        INamedGraph systemGraph = null ;
        URI systemGraphUri = null ;
        try {
            systemGraph = anzo.getRemoteSystemGraph() ;
            systemGraphUri = systemGraph.getNamedGraphUri() ;
        } catch (AnzoException e) {
            throw new AnzoIoException("Exception retrieving system graph URI.", e) ;
        } finally {
            systemGraph.close() ;
        }
        Writer writer = null ;
        try {
            writer = new FileWriter(new File(outputDir, "manifest.ttl")) ;
            RDFWriter manifest = newManifest(writer) ;
            Set<URI> namedGraphs = anzo.getModelService().getStoredNamedGraphs() ;
            for (URI graphUri : namedGraphs) {
                INamedGraphWithMetaData graph = null ;
                if (!graphUri.equals(systemGraphUri)) {
                    try {
                        graph = anzo.getRemoteGraph(graphUri, false) ;
                        createNamedGraph(graph, outputDir, manifest) ;
                    } finally {
                        if (graph != null) {
                            graph.close() ;
                        }
                    }
                }
            }
            manifest.endRDF() ;
        } catch (Exception e) {
            throw new AnzoIoException("Exception while exporting named graphs.", e) ;
        } finally {
            if (writer != null) {
                try {
                    writer.close() ;
                } catch (IOException e) {
                    throw new AnzoIoException("Exception closing manifest writer.", e) ;
                }
            }
        }
    }
    
    private void createNamedGraph(INamedGraphWithMetaData graph, File outputDir, RDFWriter manifest) throws AnzoIoException {
        String filePrefix = nextGraphFilenamePrefix() ;
        String graphFile = filePrefix + ".ttl" ;
        String metaFile = filePrefix + "_meta.ttl" ;
        writeGraph(graph, new File(outputDir, graphFile)) ;
        Writer writer = null ;
        INamedGraph metaGraph = null ;
        try {
            manifest.handleStatement(statement(uri(graphFile), uri(Rdf.TYPE), uri(NAMED_GRAPH))) ;
            manifest.handleStatement(statement(uri(graphFile), uri(GRAPH_URI), graph.getNamedGraphUri())) ;
            metaGraph = graph.getMetaDataGraph() ;
            writer = new FileWriter(new File(outputDir, metaFile)) ;
            RDFWriter rdfWriter = new TurtleWriter(writer) ;
            rdfWriter.startRDF() ;
            addMetagraphNamespaces(rdfWriter) ;
            createGraph(rdfWriter, metaGraph, graph.getNamedGraphUri()) ;
            Statement acl = getStatement(metaGraph, graph.getNamedGraphUri(), uri(AnzoPredicates.USES_ACL)) ;
            writeMatchingStatements(metaGraph, rdfWriter, (Resource) acl.getObject(), null, null) ;
            rdfWriter.endRDF() ;
            manifest.handleStatement(statement(uri(graphFile), uri(META_GRAPH), uri(metaFile))) ;
        } catch (Exception e) {
            throw new AnzoIoException("Exception while writing named graph: " + graph.getNamedGraphUri() + ".", e) ;
        } finally {
            if (metaGraph != null) {
                metaGraph.close() ;
            }
            if (writer != null) {
                try {
                    writer.close() ;
                } catch (IOException e) {
                    throw new AnzoIoException("Exception while closing writer.", e) ;
                }
            }
        }
    }
    
    private void createRoles(RDFWriter rdfWriter, INamedGraph graph) throws AnzoIoException {
        List<Role> roles = AnzoFactory.getAllRole(graph) ;
        for (Role role : roles) {
            sortAndWriteStatements(rdfWriter, role.listStatements()) ;
        }
    }
    
    private void createAcls(RDFWriter rdfWriter, INamedGraph graph) throws AnzoIoException {
        List<ACL> acls = AnzoFactory.getAllACL(graph) ;
        for (ACL acl : acls) {
            sortAndWriteStatements(rdfWriter, acl.listStatements()) ;
        }
    }
    
    private void createUsers(RDFWriter rdfWriter, INamedGraph graph) throws AnzoIoException {
        List<User> users = AnzoFactory.getAllUser(graph) ;
        for (User user : users) {
            Collection<Statement> stmts = user.listStatements() ;
            stmts.add(statement(user.resource(), uri(AnzoPredicates.PASSWORD), literal("welcome"))) ;
            sortAndWriteStatements(rdfWriter, stmts) ;
        }
    }
    
    private void createGraph(RDFWriter rdfWriter, INamedGraph graph, URI graphUri) throws AnzoIoException {
        List<Statement> stmts = new ArrayList<Statement>() ;
        stmts.add(getStatement(graph, graphUri, uri(Rdf.TYPE))) ;
        stmts.add(getStatement(graph, graphUri, uri(AnzoPredicates.USES_ACL))) ;
        writeStatements(rdfWriter, stmts) ;
    }
    
    private Statement getStatement(INamedGraph graph, Resource subject, URI predicate) {
        CloseableIterator<Statement> stmts = null ;
        try {
            stmts = graph.find(subject, predicate, null) ;
            while (stmts.hasNext()) {
                return stmts.next() ;
            }
        } finally {
            stmts.close() ;
        }
        throw new IllegalArgumentException("No statement found matching: " + subject + " " + predicate + " ANY.") ;
    }
    
    private void writeMatchingStatements(INamedGraph graph, RDFWriter rdfWriter, Resource subject, URI predicate, Value object) throws AnzoIoException {
        CloseableIterator<Statement> stmts = null ;
        try {
            stmts = graph.find(subject, predicate, object) ;
            while (stmts.hasNext()) {
                rdfWriter.handleStatement(stmts.next()) ;
            }
        } catch (RDFHandlerException e) {
            throw new AnzoIoException("Exception while writing statements.", e) ;
        } finally {
            stmts.close() ;
        }
    }

    private void sortAndWriteStatements(RDFWriter rdfWriter, Collection<Statement> stmts) throws AnzoIoException {
        writeStatements(rdfWriter, sortStatements(stmts)) ;
    }
    
    private void writeStatements(RDFWriter rdfWriter, Collection<Statement> stmts) throws AnzoIoException {
        for (Statement stmt : stmts) {
            try {
                rdfWriter.handleStatement(stmt) ;
            } catch (RDFHandlerException e) {
                throw new AnzoIoException("Exception while writing statement: " + stmt + ".", e) ;
            }
        }
    }
    
    public void dumpAll(DatasetService anzo, File outputDir) throws RDFHandlerException, IOException, AnzoException, AnzoIoException {
        if (anzo == null) {
            throw new IllegalArgumentException("Dataset service may not be null.") ;
        }
        if (outputDir == null) {
            throw new IllegalArgumentException("Output directory may not be null.") ;
        }
        
        prepareOutputDirectory(outputDir) ;
        
        Writer writer = new FileWriter(new File(outputDir, "manifest.ttl")) ;
        RDFWriter manifest = newManifest(writer) ;
        URI systemGraph = dumpSystemGraph(anzo, outputDir, manifest) ;
        Set<URI> namedGraphs = anzo.getModelService().getStoredNamedGraphs() ;
        for (URI graphUri : namedGraphs) {
            INamedGraphWithMetaData graph = null ;
            if (!graphUri.equals(systemGraph)) {
                try {
                    graph = anzo.getRemoteGraph(graphUri, false) ;
                    dumpNamedGraph(graph, outputDir, manifest) ;
                } finally {
                    if (graph != null) {
                        graph.close() ;
                    }
                }
            }
        }
        manifest.endRDF() ;
        writer.close() ;
    }
    
    private void loadNamedGraph(DatasetService anzo, Iterator<Statement> namedGraph) throws AnzoIoException {
        URI graphFile = null ;
        URI metaGraphFile = null ;
        URI graphUri = null ;
        while (namedGraph.hasNext()) {
            Statement stmt = namedGraph.next() ;
            if (stmt.getPredicate().equals(uri(Rdf.TYPE))) {
                graphFile = asUri(stmt.getSubject()) ;
            } else if (stmt.getPredicate().equals(uri(META_GRAPH))) {
                metaGraphFile = asUri(stmt.getObject()) ;
            } else if (stmt.getPredicate().equals(uri(GRAPH_URI))) {
                graphUri = asUri(stmt.getObject()) ;
            }
        }
        if (graphFile == null) {
            throw new IllegalArgumentException("Could not locate named graph file.") ;
        }
        if (metaGraphFile == null) {
            throw new IllegalArgumentException("Could not locate meta-graph for named graph: " + graphFile + ".") ;
        }
        if (graphUri == null) {
            throw new IllegalArgumentException("Could not locate URI for named graph: " + graphFile + ".") ;
        }
        INamedGraphWithMetaData graph = null ;
        try {
            anzo.begin() ;
            graph = getNewGraph(anzo, graphUri) ;
            addAllStatementsFromFile(graph, file(graphFile)) ;
            updateMetadataGraph(graph, file(metaGraphFile)) ;
            anzo.commit() ;
        } catch (Exception e) {
            try {
                anzo.abort() ;
            } catch (AnzoException e1) {
                // IGNORE
            }
            throw new AnzoIoException("Exception while loading named graph: " + graphFile + ".", e) ;
        } finally {
            if (graph != null) {
                graph.close() ;
            }
        }
        try {
            anzo.getDatasetReplicator().replicate(true) ;
        } catch (AnzoException e) {
            throw new AnzoIoException("Exception while replicating dataset.", e) ;
        }
    }
    
    private INamedGraphWithMetaData getNewGraph(DatasetService anzo, URI graphUri) throws AnzoIoException {
        INamedGraphWithMetaData result ;
        try {
            result = anzo.getRemoteGraph(graphUri, true) ;
        } catch (AnzoException e) {
            throw new AnzoIoException("Error creating remote graph: " + graphUri + ".", e) ;
        }
        result.clear() ;
        return result ;
    }
    
    private void updateMetadataGraph(INamedGraphWithMetaData graph, File metaGraphFile) throws AnzoIoException {
        INamedGraph metaGraph = null ;
        try {
            metaGraph = graph.getMetaDataGraph() ;
            Graph metaFile = loadGraph(metaGraphFile) ;
            URI newAcl = GraphUtil.getUniqueObjectURI(metaFile, graph.getNamedGraphUri(), uri(AnzoPredicates.USES_ACL)) ;
            AnzoFactory.createACL(newAcl, metaGraph) ;
            Iterator<Statement> acl = metaFile.match(newAcl, null, null) ;
            while (acl.hasNext()) {
                metaGraph.add(acl.next()) ;
            }
            metaGraph.add(graph.getNamedGraphUri(), uri(AnzoPredicates.USES_ACL), newAcl) ;
        } catch (GraphUtilException e) {
            throw new AnzoIoException("Unable to retrieve new ACL from meta graph file: " + metaGraphFile.getPath() + ".", e) ;
        } finally {
            if (metaGraph == null) {
                metaGraph.close() ;
            }
        }
    }
    
    private void addAllStatementsFromFile(INamedGraph graph, File graphFile) throws AnzoIoException {
        Graph fromFile = loadGraph(graphFile) ;
        graph.add(fromFile.toArray(new Statement[fromFile.size()])) ;
    }
    
    private Graph loadGraph(File graphFile) throws AnzoIoException {
        return loadGraph(graphFile, "") ;
    }
    
    private Graph loadGraph(File graphFile, String baseUrl) throws AnzoIoException {
        if (baseUrl == null) {
            baseUrl = "" ;
        }
        RDFParser parser = Rio.createParser(RDFFormat.forFileName(graphFile.getAbsolutePath())) ;
        Graph result = new GraphImpl() ;
        parser.setRDFHandler(new StatementCollector(result)) ;
        Reader reader = null ;
        try {
            reader = new FileReader(graphFile) ;
            parser.parse(reader, baseUrl) ;
        } catch (FileNotFoundException e) {
            throw new AnzoIoException("Unable to find file: " + graphFile.getPath(), e) ;
        } catch (RDFParseException e) {
            throw new AnzoIoException("Error parsing rdf file: " + graphFile.getPath(), e) ;
        } catch (RDFHandlerException e) {
            throw new AnzoIoException("Error handling rdf from file: " + graphFile.getPath(), e) ;
        } catch (IOException e) {
            throw new AnzoIoException("IOException while load graph from file: " + graphFile.getPath(), e) ;
        } finally {
            if (reader != null) {
                try {
                    reader.close() ;
                } catch (IOException e) {
                    throw new AnzoIoException("IOException while closing reader for rdf file: " + graphFile.getPath(), e) ;
                }
            }
        }
        return result ;
    }
    
    private URI dumpSystemGraph(DatasetService anzo, File outputDir, RDFWriter manifest) throws AnzoIoException {
        INamedGraphWithMetaData graph = null ;
        INamedGraph metaGraph = null ;
        URI result ;
        try {
            graph = anzo.getRemoteSystemGraph() ;
            result = graph.getNamedGraphUri() ;
            writeGraph(graph, new File(outputDir, "systemGraph.ttl")) ;
            metaGraph = graph.getMetaDataGraph() ;
            writeGraph(metaGraph, new File(outputDir, "systemGraphMetaGraph.ttl")) ;
        } catch (AnzoException e) {
            throw new AnzoIoException("Exception while dumping system graph.", e) ;
        } finally {
            if (metaGraph != null) {
                metaGraph.close() ;
            }
            if (graph != null) {
                graph.close() ;
            }
        }
        graph.close() ;
        try {
            manifest.handleStatement(anzo.getValueFactory().createStatement(
                    uri("systemGraph.ttl"), 
                    uri(Rdf.TYPE), 
                    uri(SYSTEM_GRAPH))) ;
            manifest.handleStatement(statement(uri("systemGraph.ttl"), uri(GRAPH_URI), result)) ;
            manifest.handleStatement(statement(
                    uri("systemGraph.ttl"),
                    uri(META_GRAPH),
                    uri("systemGraphMetaGraph.ttl"))) ;
        } catch (RDFHandlerException e) {
            throw new AnzoIoException("Exception while updating manifest.", e) ;
        }
        return result ;
    }
    
    private void dumpNamedGraph(INamedGraphWithMetaData graph, File outputDir, RDFWriter manifest) throws AnzoIoException {
        String filePrefix = nextGraphFilenamePrefix() ;
        String graphFile = filePrefix + ".ttl" ;
        String metaFile = filePrefix + "_meta.ttl" ;
        writeGraph(graph, new File(outputDir, graphFile)) ;
        INamedGraph metaGraph = null ;
        try {
            manifest.handleStatement(statement(uri(graphFile), uri(Rdf.TYPE), uri(NAMED_GRAPH))) ;
            manifest.handleStatement(statement(uri(graphFile), uri(GRAPH_URI), graph.getNamedGraphUri())) ;
            metaGraph = graph.getMetaDataGraph() ;
            writeGraph(metaGraph, new File(outputDir, metaFile)) ;
            manifest.handleStatement(statement(uri(graphFile), uri(META_GRAPH), uri(metaFile))) ;
        } catch (RDFHandlerException e) {
            throw new AnzoIoException("Exception while writing named graph: " + graph.getNamedGraphUri() + ".", e) ;
        } finally {
            if (metaGraph != null) {
                metaGraph.close() ;
            }
        }
    }
    
    private void writeGraph(INamedGraph graph, File graphFile) throws AnzoIoException {
        Writer writer = null ;
        try {
            writer = new FileWriter(graphFile) ;
            RDFWriter rdfWriter = new TurtleWriter(writer) ;
            rdfWriter.startRDF() ;
            addMetagraphNamespaces(rdfWriter) ;
            Set<Statement> stmts = groupStatementsBySubject(graph) ;
            for (Statement stmt : stmts) {
                rdfWriter.handleStatement(stmt) ;
            }
            rdfWriter.endRDF() ;
        } catch (Exception e) {
            throw new AnzoIoException("Exception while writing graph: " + graphFile + ".", e) ;
        } finally {
            if (writer != null) {
                try {
                    writer.close() ;
                } catch (IOException e) {
                    throw new AnzoIoException("Exception while closing writer for file: " + graphFile + ".", e) ;
                }
            }
        }
    }
    
    private RDFWriter newManifest(Writer manifest) throws AnzoIoException {
        RDFWriter result = new TurtleWriter(manifest) ;
        try {
            result.startRDF() ;
        } catch (RDFHandlerException e) {
            throw new AnzoIoException("Exception creating manifest.", e) ;
        }
        return result ;
    }
    
    private void addMetagraphNamespaces(RDFWriter rdfWriter) throws AnzoIoException {
        try {
            rdfWriter.handleNamespace("xsd", "http://www.w3.org/2001/XMLSchema#") ;
            rdfWriter.handleNamespace("pred", "http://openanzo.org/predicates/") ;
            rdfWriter.handleNamespace("type", "http://openanzo.org/types/") ;
            rdfWriter.handleNamespace("ngr", "http://openanzo.org/namedGraphs/") ;
            rdfWriter.handleNamespace("mgr", "http://openanzo.org/metadataGraphs/") ;
            rdfWriter.handleNamespace("rbac", "http://openanzo.org/RBAC#") ;
            rdfWriter.handleNamespace("role", "http://openanzo.org/Role/") ;
        } catch (RDFHandlerException e) {
            throw new AnzoIoException("Exception while writing RDF.", e) ;
        }
    }
    
    private String nextGraphFilenamePrefix() {
        return "graph_" + graphNumber++ ;
    }
    
    private Set<Statement> groupStatementsBySubject(INamedGraph graph) {
        Map<Resource, Set<Statement>> groups = new HashMap<Resource, Set<Statement>>() ;
        CloseableIterator<Statement> iter = null ;
        try {
            iter = graph.getStatements() ;
            while (iter.hasNext()) {
                Statement stmt = iter.next() ;
                Set<Statement> stmts = groups.get(stmt.getSubject()) ;
                if (stmts == null) {
                    stmts = getSortedStatementSet() ;
                    groups.put(stmt.getSubject(), stmts) ;
                }
                stmts.add(stmt) ;
            }
        } finally {
            if (iter != null) {
                iter.close() ;
            }
        }
        Set<Statement> result = new LinkedHashSet<Statement>() ;
        for (Set<Statement> list : groups.values()) {
            result.addAll(list) ;
        }
        return result ;
    }
    
    private SortedSet<Statement> sortStatements(Collection<Statement> stmts) {
        SortedSet<Statement> result = getSortedStatementSet() ;
        result.addAll(stmts) ;
        return result ;
    }
    
    private SortedSet<Statement> getSortedStatementSet() {
        return new TreeSet<Statement>(new Comparator<Statement>() {

            public int compare(Statement s1, Statement s2) {
                if (s1.equals(s2)) {
                    return 0 ;
                }
                if (s1.getPredicate().toString().equals(Rdf.TYPE)) {
                    return -1 ;
                } else {
                    return 1 ;
                }
            }
            
        }) ;
    }
    
    private URI uri(final String uri) {
        return new URI() {

            public String getLocalName() {
                return uri ;
            }

            public String getNamespace() {
                return "" ;
            }
            
            @Override
            public String toString() {
                return uri ;
            }
            
            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof URI)) {
                    return false ;
                }
                return uri.equals(obj.toString()) ;
            }
        } ;
    }
    
    private void prepareOutputDirectory(File outputDir) throws AnzoIoException {
        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                throw new AnzoIoException("Unable to create output directory.") ;
            }
        } else {
            if (!outputDir.isDirectory() || !outputDir.canWrite()) {
                throw new AnzoIoException("Passed in File must be a writable directory.") ;
            }
        }
    }
    
    private Statement statement(Resource subject, URI predicate, Value object) {
        return factory().createStatement(subject, predicate, object) ;
    }
    
    private Literal literal(String string) {
        return factory().createLiteral(string) ;
    }
    
    private ValueFactory factory() {
        return Constants.valueFactory ;
    }
    
    private URI asUri(Value value) {
        if (!(value instanceof URI)) {
            throw new IllegalArgumentException("Value is not a URI: " + value + ".") ;
        }
        return (URI) value ;
    }
    
    private File file(URI uri) throws AnzoIoException {
        String filename ;
        try {
            filename = new URL(uri.toString()).getFile() ;
        } catch (MalformedURLException e) {
            throw new AnzoIoException("Invalid uri for file: " + uri + ".", e) ;
        }
        return new File(filename) ;
    }
}
