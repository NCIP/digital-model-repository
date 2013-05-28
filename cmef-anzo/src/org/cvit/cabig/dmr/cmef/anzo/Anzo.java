/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.anzo ;

import java.util.Collections ;
import java.util.Map;
import java.util.Set ;
import java.util.Map.Entry;

import org.cvit.cabig.dmr.cmef.RepositoryException ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.openanzo.client.DatasetService ;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.model.INamedGraph;
import org.openanzo.model.impl.query.QueryResult ;
import org.openrdf.model.URI ;
import org.openrdf.query.TupleQueryResult ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotechsoft.rdf.RdfFactory;
import com.infotechsoft.rdf.SparqlEndpoint;
import com.infotechsoft.rdf.Statement;
import com.infotechsoft.rdf.Uri;
import com.infotechsoft.rdf.mapping.BindingsHandler;
import com.infotechsoft.rdf.mapping.QueryExecutionException;
import com.infotechsoft.rdf.sesame.BindingSetAdapter;
import com.infotechsoft.rdf.sesame.SesameRdfFactory;

public class Anzo implements SparqlEndpoint, AnzoService {
    private final Logger logger = LoggerFactory.getLogger(Anzo.class) ;
    //ANZO Properties
    public static final String USER = "org.openanzo.modelService.user" ;
    public static final String PASSWORD = "org.openanzo.modelService.password" ;
    public static final String DBTYPE = "org.openanzo.repository.database.type" ;
    public static final String DBURL = "org.openanzo.repository.database.url" ;
    public static final String DBUSER = "org.openanzo.repository.database.user" ;
    public static final String DBPASSWORD = "org.openanzo.repository.database.password" ;
    public static final String PORT = "org.openanzo.modelService.port" ;
    public static final String HOST = "org.openanzo.modelService.host" ;
    public static final String TRANSPORT = "org.openanzo.modelService.transportClass" ;
    public static final String AUTH_PROVIDER = "org.openanzo.repository.authenticationProviderClass" ;
    public static final String RESET_ENABLED = "org.openanzo.repository.reset.enabled" ;
    public static final String INIT_FILE = "org.openanzo.repository.initFile" ;
    public static final String NOTIFICATION_ENABLED = "org.openanzo.notification.enabled" ;
    public static final String PUBLISHER_ENABLED = "org.openanzo.repository.notification.publisher.enabled" ;
    //ANZO Constants
    public static final String EMBEDDED_TRANSPORT = "org.openanzo.model.embedded.EmbeddedModelService" ;
    public static final String DB2_DB = "ServerDB2" ;
    public static final String MYSQL_DB = "ServerMySQL" ;
    public static final String HSQL_DB = "ServerHSQL" ;
    
    private static final Set<URI> EMPTY_SET = Collections.emptySet() ;
    private static final String ALL_NAMED = "http://openanzo.org/namedGraphs/reserved/graphs/ALL" ;

    private DatasetService anzo ;
    private SesameRdfFactory factory ;
    private Set<URI> allNamedGraphs ;

    public Anzo(DatasetService anzo) {
	if (anzo == null) {
	    throw new IllegalArgumentException("Anzo DatasetService may not be null.") ;
	}
	this.anzo = anzo ;
	this.factory = new SesameRdfFactory(anzo.getValueFactory()) ;
	this.allNamedGraphs = Collections.singleton(anzo.getValueFactory().createURI(ALL_NAMED)) ;
    }

    /**
     * This method will execute the passed in select query against all named graphs in 
     * Anzo and calls the passed in BindingsHandler with each result binding.
     * @param query The query to execute
     * @param handler Handler for results
     * @throws QueryExecutionException On any error during query execution/result iteration
     */
    public void executeQuery(String query, BindingsHandler handler) throws QueryExecutionException {
	QueryResult queryResult ;
	TupleQueryResult results = null ;
	try {
	    queryResult = anzo.getModelService().executeQuery(allNamedGraphs, EMPTY_SET, query) ;
	    results = queryResult.getSelectResult() ;
	} catch (Exception e) {
	    throw new QueryExecutionException("Error obtaining query result from Anzo.", e) ;
	}
	try {
	    while (results.hasNext()) {
		handler.handleBindings(new BindingSetAdapter(results.next())) ;
	    }
	} catch (Exception e) {
	    throw new QueryExecutionException("Error iterating result bindings.", e) ;
	} finally {
	    if (results != null) {
		try {
		    results.close() ;
		} catch (Exception e) {
		    logger.warn("Exception while closing query results: {}", e.getMessage()) ;
		}
	    }
	}
    }
    
    /* (non-Javadoc)
     * @see org.cvit.cabig.dmr.anzo.AnzoService#updateGraphs(java.util.Map, java.util.Map)
     */
    public void updateGraphs(Map<Uri, Set<Statement>> toRemove, Map<Uri, Set<Statement>> toAdd) throws RepositoryException {
	logGraphUpdate(toAdd, toRemove) ;
	try {
	    anzo.begin() ;
	    if (toRemove != null) {
		for (Entry<Uri, Set<Statement>> entry : toRemove.entrySet()) {
		    doRemove(entry.getValue(), entry.getKey()) ;
		}
	    }
	    if (toAdd != null) {
		for (Entry<Uri, Set<Statement>> entry : toAdd.entrySet()) {
		    doAdd(entry.getValue(), entry.getKey()) ;
		}
	    }
	    anzo.commit() ;
	} catch (Exception e) {
	    try {
		anzo.abort() ;
	    } catch (AnzoException e1) {
		logger.warn("Exception thrown while aborting transaction: {}", e1.getMessage()) ;
	    }
	    throw new RepositoryException("Error updating graphs.", e) ;
	}
	try {
	    anzo.getDatasetReplicator().replicate(true) ;
	} catch (AnzoException e) {
	    throw new RepositoryException("Error while replicating dataset.", e) ;
	}
    }
    
    /* (non-Javadoc)
     * @see org.cvit.cabig.dmr.anzo.AnzoService#setCurrentUser(java.lang.String)
     */
    public void setCurrentUser(UserCredentials user) {
	anzo.getModelService().setServiceUser(user.getUsername()) ;
	anzo.getModelService().setServicePassword(user.getPassword()) ;
    }
    
    /* (non-Javadoc)
     * @see org.cvit.cabig.dmr.anzo.AnzoService#getCurrentUser()
     */
    public String getCurrentUser() {
	return anzo.getModelService().getServiceUser() ;
    }

    /**
     * This method is used to check the connection to anzo.
     * @return true if connection ok, false otherwise
     */
    public boolean isConnected() {
        // TODO Best way to check this...
        return true ;
    }
    
    /* (non-Javadoc)
     * @see org.cvit.cabig.dmr.anzo.AnzoService#getRdfFactory()
     */
    public RdfFactory getRdfFactory() {
	return factory ;
    }
    
    //Based on code from WebContent\js\telar\js.js
    //TODO: this is probably better encapsulated within a UriGenerator class...newEntryId(), newPaperId()...
    public Uri newId() {
//	return getRdfFactory().createUri("urn:lsid:telar.cambridgesemantics.com:telar" + Math.random()) ;
	//Changed to cvit.org...
	return getRdfFactory().createUri("urn:lsid:cvit.org:cmef:" + Math.random()) ;
    }
    
    private void doRemove(Set<Statement> statements, Uri graphUri) throws RepositoryException {
	INamedGraph graph = null ;
	try {
	    graph = anzo.getRemoteGraph(factory.asSesameUri(graphUri), false) ;
	    if (graph != null) {
		for (Statement stmt : statements) {
		    graph.delete(factory.asSesameStatement(stmt)) ;
		}
	    }
	} catch (AnzoException e) {
	    throw new RepositoryException("Error removing statements from graph.", e) ;
	} finally {
	    if (graph != null) {
		graph.close() ;
	    }
	}
	
    }
    
    private void doAdd(Set<Statement> statements, Uri graphUri) throws RepositoryException {
	INamedGraph graph = null ;
	try {
	    graph = anzo.getRemoteGraph(factory.asSesameUri(graphUri), true) ;
	    for (Statement stmt : statements) {
		    graph.add(factory.asSesameStatement(stmt)) ;
	    }
	} catch (AnzoException e) {
	    throw new RepositoryException("Error adding statements to graph.", e) ;
	} finally {
	    if (graph != null) {
		graph.close() ;
	    }
	}
    }
    
    private void logGraphUpdate(Map<Uri, Set<Statement>> toAdd, Map<Uri, Set<Statement>> toRemove) {
	if (logger.isDebugEnabled()) {
	    if (toAdd != null) {
		for (java.util.Map.Entry<Uri, Set<Statement>> entry : toAdd.entrySet()) {
		    String graphUri = entry.getKey().toString() ;
		    logger.debug("Adding {} to graph: {}.", entry.getValue(), graphUri) ;
		}
	    }
	    if (toRemove != null) {
		for (java.util.Map.Entry<Uri, Set<Statement>> entry : toRemove.entrySet()) {
		    String graphUri = entry.getKey().toString() ;
		    logger.debug("Removing {} from graph: {}.", entry.getValue(), graphUri) ;
		}
	    }
	}
    }
}
