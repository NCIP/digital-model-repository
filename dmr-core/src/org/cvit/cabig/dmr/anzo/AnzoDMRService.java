/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.anzo ;

import static com.infotechsoft.cagrid.cql.CQLBuilder.* ;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cvit.cabig.dmr.AuthenticationException;
import org.cvit.cabig.dmr.AuthorizationException;
import org.cvit.cabig.dmr.AuthorizationService;
import org.cvit.cabig.dmr.DMRService;
import org.cvit.cabig.dmr.DataSourceException;
import org.cvit.cabig.dmr.InvalidUserIdException;
import org.cvit.cabig.dmr.UserAuthorizer;
import org.cvit.cabig.dmr.domain.DataClassification;
import org.cvit.cabig.dmr.domain.Entry;
import org.cvit.cabig.dmr.domain.Organization;
import org.cvit.cabig.dmr.domain.Reference;
import org.cvit.cabig.dmr.vocabulary.CViT;
import org.cvit.cabig.dmr.vocabulary.DC11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotechsoft.cagrid.cql.CqlProcessor;
import com.infotechsoft.cagrid.cql2sparql.QueryProcessingException;
import com.infotechsoft.rdf.Statement;
import com.infotechsoft.rdf.Uri;
import com.infotechsoft.rdf.mapping.MappingException;
import com.infotechsoft.rdf.mapping.RdfMapper;

public class AnzoDMRService implements DMRService {
    private Logger logger = LoggerFactory.getLogger(AnzoDMRService.class) ;
    
    //Collaborators:
    private AuthorizationService authService ;
    private RdfMapper mapper ;
    private CqlProcessor queryProcessor ;
    private AnzoService anzo ;
    
    //State
    private UserAuthorizer authorizer ;
    
    public AnzoDMRService(AnzoService anzo, AuthorizationService authService, RdfMapper mapper, CqlProcessor queryProcessor) {
	if (anzo == null) {
	    throw new IllegalArgumentException("Anzo service may not be null.") ;
	}
	if (authService == null) {
	    throw new IllegalArgumentException("Authorization service may not be null.") ;
	}
	if (mapper == null) {
	    throw new IllegalArgumentException("RDF mapper may not be null.") ;
	}
	if (queryProcessor == null) {
	    throw new IllegalArgumentException("CQL query processor may not be null.") ;
	}
	this.authService = authService ;
	this.mapper = mapper ;
	this.queryProcessor = queryProcessor ;
	this.anzo = anzo ;
    }
    
    public CqlProcessor getCqlProcessor() {
	return queryProcessor ;
    }
    
    public void setUser(String userId) throws AuthenticationException {
	try {
	    authorizer = authService.getUserAuthorizer(userId) ;
	} catch (InvalidUserIdException e) {
	    throw new AuthenticationException("Unable to authenticate user: " + userId + ".", e) ;
	}
        anzo.setCurrentUser(userId) ;
    }
    
    public Entry addEntry(Entry entry, Organization fundingOrganization) throws AuthorizationException, DataSourceException {
	if (!getAuthorizer().canAddEntry()) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " not authorized to add Entry.")	 ;
	}
	Organization org = null ;
	if (fundingOrganization != null) {
	    org = getOrganization(fundingOrganization.getId()) ;
	}
	Uri entryUri = anzo.newId() ;
	entry.setId(entryUri.toString()) ;
	Set<Statement> entryGraph = toRdf(entry) ;
	entryGraph.add(getCreatorStatement(entryUri)) ;
	entryGraph.add(anzo.getRdfFactory().createStatement(
	    entryUri,
	    anzo.getRdfFactory().createUri(CViT.ObjectProperties.PRINCIPLE_INVESTIGATOR),
	    authorizer.getUri())) ;
	if (org != null) {
	    entryGraph.add(anzo.getRdfFactory().createStatement(
		entryUri, 
		anzo.getRdfFactory().createUri(CViT.ObjectProperties.FUNDING_ORGANIZATION), 
		anzo.getRdfFactory().createUri(org.getId()))) ;
	}
	addStatements(entryGraph, entryUri) ;
        return entry ;
    }
    
    public Reference addReferenceToEntry(Reference reference, Entry entry) throws AuthorizationException, DataSourceException {
	if (reference == null) {
	    throw new IllegalArgumentException("Reference may not be null.") ;
	}
	if (entry == null) {
	    throw new IllegalArgumentException("Entry may not be null.") ;
	}
	Entry actualEntry = getEntry(entry.getId()) ;
	Uri entryUri = anzo.getRdfFactory().createUri(actualEntry.getId()) ;
	
	if (!getAuthorizer().canEditObject(entry.getId())) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to edit entry " + entry.getId() + ".") ;
	}
	
	Set<Statement> refGraph ;
	Uri refUri ;
	if (reference.getId() != null) {
	    Reference ref = getReference(reference.getId()) ;
	    refGraph = Collections.emptySet() ;
	    refUri = anzo.getRdfFactory().createUri(ref.getId()) ;
	} else {
	    refUri = anzo.newId() ;
	    reference.setId(refUri.toString()) ;
	    refGraph = toRdf(reference) ;
	    refGraph.add(getCreatorStatement(refUri)) ;
	}
	Statement entryRef = anzo.getRdfFactory().createStatement(
	    entryUri,
	    anzo.getRdfFactory().createUri(CViT.ObjectProperties.REFERENCES),
	    refUri) ;
	Map<Uri, Set<Statement>> toAdd = new HashMap<Uri, Set<Statement>>() ;
	toAdd.put(refUri, refGraph) ;
	toAdd.put(entryUri, Collections.singleton(entryRef)) ;
	anzo.updateGraphs(null, toAdd) ;
	authService.updateObjectsAclFromEntry(reference.getId(), actualEntry.getId()) ;
        return reference ;
    }
    
    public DataClassification addDataToEntry(DataClassification data, Entry entry) throws AuthorizationException, DataSourceException {
	if (data == null) {
	    throw new IllegalArgumentException("Data may not be null.") ;
	}
	if (entry == null) {
	    throw new IllegalArgumentException("Entry may not be null.") ;
	}
	Entry actualEntry = getEntry(entry.getId()) ;
	Uri entryUri = anzo.getRdfFactory().createUri(actualEntry.getId()) ;
	
	if (!getAuthorizer().canEditObject(entry.getId())) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to edit entry " + entry.getId() + ".") ;
	}
	
	Set<Statement> dataGraph ;
        Uri dataUri ;
        if (data.getId() != null) {
            DataClassification oldData = getData(data.getId()) ;
            dataGraph = Collections.emptySet() ;
            dataUri = anzo.getRdfFactory().createUri(oldData.getId()) ;
        } else {
            dataUri = anzo.newId() ;
            data.setId(dataUri.toString()) ;
            dataGraph = toRdf(data) ;
            dataGraph.add(getCreatorStatement(dataUri)) ;
        }
	Statement entryData = anzo.getRdfFactory().createStatement(
	    entryUri,
	    anzo.getRdfFactory().createUri(CViT.ObjectProperties.DATA),
	    dataUri) ;
	Map<Uri, Set<Statement>> toAdd = new HashMap<Uri, Set<Statement>>() ;
	toAdd.put(dataUri, dataGraph) ;
	toAdd.put(entryUri, Collections.singleton(entryData)) ;
	anzo.updateGraphs(null, toAdd) ;
	authService.updateObjectsAclFromEntry(data.getId(), actualEntry.getId()) ;
	return data ;
    }
    
    public void updateEntry(Entry entry) throws AuthorizationException, DataSourceException {
        if (entry == null) {
            throw new IllegalArgumentException("Entry may not be null.") ;
        }
        if (!canEdit(entry.getId())) {
            throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to edit entry " + entry.getId() + ".") ;
        }
	Entry oldEntry = getEntry(entry.getId()) ;
        Set<Statement> oldGraph = toRdf(oldEntry) ;
        Set<Statement> newGraph = toRdf(entry) ;
        updateGraph(entry.getId(), oldGraph, newGraph) ;
    }
    
    public void updateData(DataClassification data) throws AuthorizationException, DataSourceException {
	if (data == null) {
	    throw new IllegalArgumentException("Data may not be null.") ;
	}
	if (!canEdit(data.getId())) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to edit datum " + data.getId() + ".") ;
	}
        DataClassification oldData = getData(data.getId()) ;
        Set<Statement> oldGraph = toRdf(oldData) ;
        Set<Statement> newGraph = toRdf(data) ;
        updateGraph(data.getId(), oldGraph, newGraph) ;
    }
    
    public void updateReference(Reference reference) throws AuthorizationException, DataSourceException {
        if (reference == null) {
            throw new IllegalArgumentException("Reference may not be null.") ;
        }
        if (!canEdit(reference.getId())) {
            throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to edit reference " + reference.getId() + ".") ;
        }
	Reference oldRef = getReference(reference.getId()) ;
        Set<Statement> oldGraph = toRdf(oldRef) ;
        Set<Statement> newGraph = toRdf(reference) ;
        updateGraph(reference.getId(), oldGraph, newGraph) ;
    }
    
    private UserAuthorizer getAuthorizer() throws AuthorizationException {
	if (authorizer == null) {
	    throw new AuthorizationException("User authorizer is null.") ;
	}
	return authorizer ;
    }
    
    private boolean canEdit(String id) throws AuthorizationException {
	if (id == null) {
	    throw new IllegalArgumentException("Id may not be null.") ;
	}
	return getAuthorizer().canEditObject(id) ;
    }
    
    private void addStatements(Set<Statement> statements, Uri graphUri) throws DataSourceException {
	Map<Uri, Set<Statement>> toAdd = new HashMap<Uri, Set<Statement>>() ;
	toAdd.put(graphUri, statements) ;
	anzo.updateGraphs(null, toAdd) ;
    }
    
    private Set<Statement> toRdf(Object obj) throws DataSourceException {
	Set<Statement> result ;
	try {
	    result = mapper.toRdf(obj) ;
	} catch (MappingException e) {
	    throw new DataSourceException("Error converting " + obj.getClass().getSimpleName() + " to RDF.", e) ;
	}
	return result ;
    }
    
    private Statement getCreatorStatement(Uri subject) {
	return anzo.getRdfFactory().createStatement(
	    subject, 
	    anzo.getRdfFactory().createUri(DC11.CREATOR), 
	    authorizer.getUri()) ;
    }
    
    private void updateGraph(String graphUri, Set<Statement> oldGraph, Set<Statement> newGraph) throws DataSourceException {
	Set<Statement> toRemove = new HashSet<Statement>() ;
	toRemove.addAll(oldGraph) ;
	toRemove.removeAll(newGraph) ;
	
	Set<Statement> toAdd = new HashSet<Statement>() ;
	toAdd.addAll(newGraph) ;
	toAdd.removeAll(oldGraph) ;
	
	if (toRemove.size() > 0 || toAdd.size() > 0) {
	    Uri uri = anzo.getRdfFactory().createUri(graphUri) ;
	    anzo.updateGraphs(
		Collections.singletonMap(uri, toRemove), 
		Collections.singletonMap(uri, toAdd)) ;
	}
    }
    
    public Entry getEntry(String id) throws AuthorizationException, DataSourceException {
	if (id == null) {
	    throw new IllegalArgumentException("Entry id may not be null.") ;
	}
	CQLQuery query = newQuery().forObject(Entry.class.getName()).with(
	    attribute("id").equalTo(id)).build();
	List<Entry> results ;
	try {
	    results = queryProcessor.processObjectQuery(query, Entry.class) ;
	} catch (QueryProcessingException e) {
	    throw new DataSourceException("Unable to retrieve entry from datasource.", e) ;
	}
	if (results.size() < 1) {
	    throw new IllegalArgumentException("Entry with id <" + id + "> does not exist.") ;
	}
	if (results.size() > 1) {
	    logger.warn("Found more than one entry with id: {}.", id) ;
	}
	return results.get(0) ;
    }
    
    public Reference getReference(String id) throws AuthorizationException, DataSourceException {
	if (id == null) {
	    throw new IllegalArgumentException("Reference id may not be null.") ;
	}
        CQLQuery query = newQuery().forObject(Reference.class.getName()).with(attribute("id").equalTo(id)).build() ;
        List<Reference> results ;
        try {
            results = queryProcessor.processObjectQuery(query, Reference.class) ;
        } catch (QueryProcessingException e) {
            throw new DataSourceException("Unable to retrieve data classification from datasource.", e) ;
        }
        if (results.size() < 1) {
            throw new IllegalArgumentException("Reference with id <" + id + "> does not exist.") ;
        }
        if (results.size() > 1) {
	    logger.warn("Found more than one reference wiht id: {}.", id) ;
	}
        return results.get(0) ;
    }
    
    public DataClassification getData(String id) throws AuthorizationException, DataSourceException {
	if (id == null) {
	    throw new IllegalArgumentException("Data id may not be null.") ;
	}
        CQLQuery query = newQuery().forObject(DataClassification.class.getName()).with(
                attribute("id").equalTo(id)).build() ;
        List<DataClassification> results ;
        try {
            results = queryProcessor.processObjectQuery(query, DataClassification.class) ;
        } catch (QueryProcessingException e) {
            throw new DataSourceException("Unable to retrieve data classification from datasource.", e) ;
        }
	if (results.size() < 1) {
	    throw new IllegalArgumentException("Data with id <" + id + "> does not exist.") ;
	}
	if (results.size() > 1) {
	    logger.warn("Found more than one datum with id: {}.", id) ;
	}
	return results.get(0) ;
    }
    
    public Organization getOrganization(String id) throws DataSourceException {
	if (id == null) {
	    throw new IllegalArgumentException("Organization id may not be null.") ;
	}
	CQLQuery query = newQuery().forObject(Organization.class.getName()).with(
	    attribute("id").equalTo(id)).build() ;
	List<Organization> results ;
	try {
	    results = queryProcessor.processObjectQuery(query, Organization.class) ;
	} catch (QueryProcessingException e) {
	    throw new DataSourceException("Unable to retrieve organization from datasource.", e) ;
	}
	if (results.size() < 1) {
	    throw new IllegalArgumentException("Organization with id <" + id + "> does not exist.") ;
	}
	if (results.size() > 1) {
	    logger.warn("Found more than one organization with id: {}.", id) ;
	}
	return results.get(0) ;
    }
}
