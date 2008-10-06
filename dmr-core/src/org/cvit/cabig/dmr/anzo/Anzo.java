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

package org.cvit.cabig.dmr.anzo ;

import java.util.Collections ;
import java.util.Map;
import java.util.Set ;
import java.util.Map.Entry;

import org.cvit.cabig.dmr.DataSourceException;
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
    public void updateGraphs(Map<Uri, Set<Statement>> toRemove, Map<Uri, Set<Statement>> toAdd)
	throws DataSourceException {
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
	    throw new DataSourceException("Error updating graphs.", e) ;
	}
	try {
	    anzo.getDatasetReplicator().replicate(true) ;
	} catch (AnzoException e) {
	    throw new DataSourceException("Error while replicating dataset.", e) ;
	}
    }
    
    /* (non-Javadoc)
     * @see org.cvit.cabig.dmr.anzo.AnzoService#setCurrentUser(java.lang.String)
     */
    public void setCurrentUser(String userId) {
	anzo.getModelService().setServiceUser(userId) ;
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
	return getRdfFactory().createUri("urn:lsid:telar.cambridgesemantics.com:telar" + Math.random()) ;
    }
    
    private void doRemove(Set<Statement> statements, Uri graphUri) throws DataSourceException {
	INamedGraph graph = null ;
	try {
	    graph = anzo.getRemoteGraph(factory.asSesameUri(graphUri), false) ;
	    if (graph != null) {
		for (Statement stmt : statements) {
		    graph.delete(factory.asSesameStatement(stmt)) ;
		}
	    }
	} catch (AnzoException e) {
	    throw new DataSourceException("Error removing statements from graph.", e) ;
	} finally {
	    if (graph != null) {
		graph.close() ;
	    }
	}
	
    }
    
    private void doAdd(Set<Statement> statements, Uri graphUri) throws DataSourceException {
	INamedGraph graph = null ;
	try {
	    graph = anzo.getRemoteGraph(factory.asSesameUri(graphUri), true) ;
	    for (Statement stmt : statements) {
		    graph.add(factory.asSesameStatement(stmt)) ;
	    }
	} catch (AnzoException e) {
	    throw new DataSourceException("Error adding statements to graph.", e) ;
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
