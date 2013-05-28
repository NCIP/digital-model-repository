/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.cvit.cabig.dmr.anzo.AnzoIo;
import org.cvit.cabig.dmr.anzo.AnzoIoException;
import org.cvit.cabig.dmr.vocabulary.AnzoPredicates;
import org.cvit.cabig.dmr.vocabulary.RBAC;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openanzo.client.DatasetService;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.model.INamedGraph;
import org.openanzo.model.embedded.EmbeddedAuthenticationService;
import org.openanzo.model.embedded.EmbeddedModelService;
import org.openanzo.model.embedded.EmbeddedNotificationService;
import org.openanzo.model.embedded.EmbeddedReplicationService;
import org.openanzo.model.embedded.EmbeddedServiceBase;
import org.openanzo.server.repository.RepositoryProperties;
import org.openanzo.server.repository.rdb.RepositoryConnectionProperties;
import org.openanzo.services.IAuthenticationService;
import org.openanzo.services.IModelService;
import org.openanzo.services.INotificationService;
import org.openanzo.services.IReplicationService;
import org.openanzo.services.ModelServiceProperties;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

import com.infotechsoft.rdf.vocabulary.Rdf;

/**
 * <p>Test fixture for running tests against Open Anzo server.</p>
 * 
 * <p>In order to initialize the Anzo services using specific properties, a subclass may create a 
 * <code>public static void</code> method annotated with the <code>@BeforeClass<code> annotation.  
 * This method should create a <code>java.util.Properties<code> class and set the desired properties.
 * To access the default set of properties use the <code>getAnzoProperties()</code> method.  Finally 
 * call the <code>createAnzoServices(Properties)</code> method.</p>
 * 
 * <p>Before each test is run the Anzo server is reset and loaded with an initial set of statements, 
 * retrieved by calling the <code>getInitialStatements(Set&lt;Statement&gt;)</code> abstract method.</p>
 * 
 * @author rbradley
 *
 */
//TODO: refactor to use DmrFactory...
public abstract class AbstractAnzoTests {
    protected static final String SYSADMIN_ROLE = "http://openanzo.org/Role/sysAdmin" ;
    protected static final String SYSADMIN_URI = "http://openanzo.org/users/sysAdmin" ;
    protected static final String SYSADMIN_ID = "sysadmin" ;
    protected static final String SYSADMIN_PW = "sysadminpw" ;
    
    /* ****************************************
     * Methods for initializing Anzo services *
     ******************************************/
    private static IModelService modelSvc ;
    private static INotificationService notificationSvc ;
    private static IReplicationService replicationSvc ;
    private static IAuthenticationService authenticationSvc ;
    private static DatasetService datasetSvc ;
    
    @BeforeClass
    public static void initializeAnzo() throws AnzoException {
	createAnzo(getAnzoProperties()) ;
    }
    
    protected static void createAnzo(Properties properties) {
	closeAnzo() ;
	EmbeddedServiceBase svcBase ;
	try {
	    svcBase = new EmbeddedServiceBase(properties) ;
	} catch (AnzoException e) {
	    throw new RuntimeException("Error instantiating Open Anzo services.") ;
	}
	modelSvc = new EmbeddedModelService(svcBase) ;
	notificationSvc = new EmbeddedNotificationService(svcBase) ;
	replicationSvc = new EmbeddedReplicationService(svcBase) ;
	authenticationSvc = new EmbeddedAuthenticationService(svcBase) ;
	datasetSvc = new DatasetService(properties, modelSvc, notificationSvc, replicationSvc) ;
    }
    
    private static Properties anzoProperties ;
    protected static Properties getAnzoProperties() {
	if (anzoProperties == null) {
	    anzoProperties = new Properties() ;
	    
	    ModelServiceProperties.setUser(anzoProperties, SYSADMIN_ID) ;
	    ModelServiceProperties.setPassword(anzoProperties, SYSADMIN_PW) ;
	    
	    RepositoryProperties.setInitFile(anzoProperties, getAbsoluteResourcePath(AbstractAnzoTests.class, "bootstrap.ttl")) ;
	    RepositoryProperties.setNotificationPublisherEnabled(anzoProperties, false) ;
	    RepositoryProperties.setResetEnabled(anzoProperties, true) ;
	    
	    RepositoryConnectionProperties.setType(anzoProperties, "ServerHSQL") ;
	    RepositoryConnectionProperties.setUser(anzoProperties, "sa") ;
	    RepositoryConnectionProperties.setPassword(anzoProperties, "") ;
	    RepositoryConnectionProperties.setUrl(anzoProperties, "jdbc:hsqldb:mem:anzodb") ;
	}
	return anzoProperties ;
    }
    
    protected static String getAbsoluteResourcePath(Class<?> clazz, String relative) {
        return "resource:/" + clazz.getPackage().getName().replaceAll("\\.", "/") + "/" + relative ;
    }
    
    @AfterClass
    public static void destroyAnzo() {
	closeAnzo() ;
    }
    
    private static void closeAnzo() {
	if (authenticationSvc != null) {
	    authenticationSvc = null ;
	}
	if (datasetSvc != null) {
	    datasetSvc.close() ;
	    datasetSvc = null ;
	}
    }
    
    /* ********************************************************
     * Methods for specifying the initial statements for Anzo *
     **********************************************************/
    @Before
    public void resetAnzo() throws AnzoException {
	datasetSvc.getModelService().setServiceUser(SYSADMIN_ID) ;
	datasetSvc.getModelService().setServicePassword(SYSADMIN_PW) ;
	datasetSvc.getModelService().reset(getInitialStatements(getDefaultStatements())) ;
    }
    
    protected abstract Set<Statement> getInitialStatements(Set<Statement> defaults) ;
    
    private Set<Statement> getDefaultStatements() {
        Set<Statement> defaultStatements = new HashSet<Statement>() ;
        defaultStatements.add(statement(uri(SYSADMIN_URI), uri(Rdf.TYPE), uri(RBAC.USER))) ;
        defaultStatements.add(statement(uri(SYSADMIN_URI), uri(RBAC.IN_ROLE), uri(SYSADMIN_ROLE))) ;
        defaultStatements.add(statement(uri(SYSADMIN_URI), uri(AnzoPredicates.USER_ID), literal(SYSADMIN_ID))) ;
        defaultStatements.add(statement(uri(SYSADMIN_URI), uri(AnzoPredicates.PASSWORD), literal(SYSADMIN_PW))) ;
        return defaultStatements ;
    }
    
    /* *************************************
     * Methods for accessing Anzo services *
     ***************************************/
    protected DatasetService getDatasetService() {
	return datasetSvc ;
    }
    
    protected ValueFactory getValueFactory() {
	return getDatasetService().getValueFactory() ;
    }
    
    protected IModelService getModelService() {
	return modelSvc ;
    }
    
    protected INotificationService getNotificationService() {
	return notificationSvc ;
    }
    
    protected IReplicationService getReplicationService() {
	return replicationSvc ;
    }
    
    protected IAuthenticationService getAuthenticationService() {
	return authenticationSvc ;
    }
    
    /* *********************************
     * Methods for adding data to anzo *
     ***********************************/
    
    protected void addNamedGraph(URI uri, Statement... statements) {
	INamedGraph graph ;
	try {
	    graph = getDatasetService().getRemoteGraph(uri, true) ;
	} catch (AnzoException e2) {
	    throw new RuntimeException("Exception adding named graph: " + uri + ".") ;
	}
	try {
	    datasetSvc.begin() ;
	    graph.add(statements) ;
	    datasetSvc.commit() ;
	} catch (AnzoException e) {
	    try {
		datasetSvc.abort() ;
	    } catch (AnzoException e1) { /* Ignore */ }
	    throw new RuntimeException("Exception adding named graph: " + uri + ".") ;
	} finally {
	    if (graph != null) {
		graph.close() ;
	    }
	}
	try {
	    getDatasetService().getDatasetReplicator().replicate(true) ;
	} catch (AnzoException e) {
	    throw new RuntimeException("Exception adding named graph: " + uri + ".") ;
	}
    }
    
    protected void loadGraphFromFile(URI uri, String filename) {
	Set<Statement> stmts = loadStatementsFromFile(filename) ;
	addNamedGraph(uri, stmts.toArray(new Statement[stmts.size()])) ;
    }
    
    protected void addGraphForEachSubject(String filename) {
	Set<Statement> stmts = loadStatementsFromFile(filename) ;
	Map<URI, Set<Statement>> toAdd = new HashMap<URI, Set<Statement>>() ;
	for (Statement stmt : stmts) {
	    URI uri = asURI(stmt.getSubject()) ;
	    Set<Statement> graphStmts = toAdd.get(uri) ;
	    if (graphStmts == null) {
		graphStmts = new HashSet<Statement>() ;
		toAdd.put(uri, graphStmts) ;
	    }
	    graphStmts.add(stmt) ;
	}
	for (Entry<URI, Set<Statement>> entry : toAdd.entrySet()) {
	    addNamedGraph(
		entry.getKey(), 
		entry.getValue().toArray(new Statement[entry.getValue().size()])) ;
	}
    }
    
    protected void loadManifest(File file) {
	AnzoIo anzoIo = new AnzoIo() ;
	try {
	    anzoIo.loadGraphsFromManifest(getDatasetService(), file) ;
	} catch (AnzoIoException e) {
	    throw new RuntimeException("Exception while loading manifest: " + file + ".", e) ;
	}
    }
    
    protected Set<Statement> loadStatementsFromFile(String filename) {
	StatementCollector collector = new StatementCollector() ;
	RDFParser parser = Rio.createParser(RDFFormat.forFileName(filename)) ;
	parser.setRDFHandler(collector) ;
	try {
	    InputStream rdfStream = getInputStream(filename) ;
	    parser.parse(rdfStream, "") ;
	    rdfStream.close() ;
	} catch (Exception e) {
	    throw new RuntimeException("Error loading statements from file: " + filename + ".", e) ;
	}
	Set<Statement> result = new HashSet<Statement>() ;
	result.addAll(collector.getStatements()) ;
	return result ;
    }
    
    private static final String RESOURCE_PREFIX = "resource:" ;
    /**
     * can use resource:/org/... for package resources...
     * @param location
     * @return
     */
    protected static InputStream getInputStream(String location) {
	if (location.startsWith(RESOURCE_PREFIX)) {
	    return ClassLoader.getSystemResourceAsStream(location.substring(RESOURCE_PREFIX.length() + 1)) ;
	} else {
	    try {
		return new FileInputStream(location) ;
	    } catch (FileNotFoundException e) {
		throw new RuntimeException("Exception open input stream to file: " + location + ".", e) ;
	    }
	}
    }
    
    private URI asURI(Resource resource) {
	if (resource instanceof URI) {
	    return (URI) resource ;
	}
	return uri(resource.toString()) ;
    }
    
    /* *****************
     * Factory methods *
     *******************/
    public <T> Set<T> set(T... objects) {
	Set<T> result = new HashSet<T>() ;
	for (T obj : objects) {
	    result.add(obj) ;
	}
	return result ;
    }
    
    public Statement statement(Resource subject, URI predicate, Value object) {
	return getValueFactory().createStatement(subject, predicate, object) ;
    }
    
    public URI uri(String uri) {
	return getValueFactory().createURI(uri) ;
    }
    
    public Value literal(String value) {
	return getValueFactory().createLiteral(value) ;
    }
}
