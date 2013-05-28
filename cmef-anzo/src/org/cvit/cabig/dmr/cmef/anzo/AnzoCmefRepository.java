/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.anzo;

import static com.infotechsoft.cagrid.cql.CQLBuilder.attribute ;
import static com.infotechsoft.cagrid.cql.CQLBuilder.associationTo ;
import static com.infotechsoft.cagrid.cql.CQLBuilder.newQuery ;

import gov.nih.nci.cagrid.cqlquery.CQLQuery ;

import java.util.ArrayList ;
import java.util.Collection ;
import java.util.Collections ;
import java.util.HashMap ;
import java.util.HashSet ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;
import java.util.Set ;

import org.cvit.cabig.dmr.cmef.AuthorizationException ;
import org.cvit.cabig.dmr.cmef.CmefAuthorizer ;
import org.cvit.cabig.dmr.cmef.CmefRepository ;
import org.cvit.cabig.dmr.cmef.RepositoryException ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform ;
import org.cvit.cabig.dmr.cmef.util.BeanCopyUtils ;
import org.cvit.cabig.dmr.cmef.vocabulary.CMEF ;
import org.cvit.cabig.dmr.cmef.vocabulary.CViT ;
import org.cvit.cabig.dmr.cmef.vocabulary.DC11 ;
import org.cvit.cabig.dmr.domain.Entry ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import com.infotechsoft.cagrid.cql.CqlProcessor ;
import com.infotechsoft.cagrid.cql2sparql.QueryProcessingException ;
import com.infotechsoft.rdf.Resource ;
import com.infotechsoft.rdf.Statement ;
import com.infotechsoft.rdf.Uri ;
import com.infotechsoft.rdf.Value ;
import com.infotechsoft.rdf.mapping.MappingException ;
import com.infotechsoft.rdf.mapping.RdfMapper ;

//Metadata Patterns (value bound to URI of object):
//OPTIONAL { ?value  <http://openanzo.org/predicates/modified>  ?date  } . 
//    OPTIONAL { ?value  <http://purl.org/dc/elements/1.1/creator>  ?creator   .  
//    		OPTIONAL { ?creator  <http://xmlns.com/foaf/0.1/firstName>  ?cfname  } .  
//    		OPTIONAL { ?creator  <http://xmlns.com/foaf/0.1/surname>  ?clname  } . 
//    	}  
public class AnzoCmefRepository implements CmefRepository {
    private Logger logger = LoggerFactory.getLogger(AnzoCmefRepository.class) ;
    
    private AnzoService anzo ;
    private CqlProcessor queryProcessor ;
    private RdfMapper mapper ;
    private CmefAuthorizer authorizer ;
    
    public AnzoCmefRepository(AnzoService anzo, CqlProcessor queryProcessor, RdfMapper mapper, CmefAuthorizer authorizer) {
	if (anzo == null) {
	    throw new IllegalArgumentException("Anzo service may not be null.") ;
	}
	if (queryProcessor == null) {
	    throw new IllegalArgumentException("CQL query processor may not be null.") ;
	}
	if (mapper == null) {
	    throw new IllegalArgumentException("RDF mapper may not be null.") ;
	}
	if (authorizer == null) {
	    throw new IllegalArgumentException("CMEF authorizer may not be null.") ;
	}
	this.anzo = anzo ;
	this.queryProcessor = queryProcessor ;
	this.mapper = mapper ;
	this.authorizer = authorizer ;
    }
    
    @Override
    public Iterator<ComputationalModel> listModelsForEntry(Entry entry) throws RepositoryException, AuthorizationException {
	if (entry == null) {
	    throw new IllegalArgumentException("Entry must not be null.") ;
	}
	Set<String> referencedModels = getIdsOfReferencedModels(entry) ;
	List<ComputationalModel> models = new ArrayList<ComputationalModel>() ;
	for (String id : referencedModels) {
	    models.add(getShallowModel(id)) ;
	}
	return models.iterator() ;
    }

    @Override
    public ComputationalModel addModelToEntry(ComputationalModel model, Entry entry) throws RepositoryException, AuthorizationException {
	if (model == null) {
	    throw new IllegalArgumentException("Model may not be null.") ;
	}
	if (entry == null) {
	    throw new IllegalArgumentException("Entry may not be null.") ;
	}
	Entry actualEntry = getEntry(entry.getId()) ;
	Uri entryUri = uri(actualEntry.getId()) ;

	if (!authorizer.canEditResource(entryUri)) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to add model to entry " + entryUri + ".") ;
	}
	
	Uri modelUri ;
	Map<Uri, Set<Statement>> toAdd ;
	if (model.getId() != null) {
	    modelUri = uri(model.getId()) ;
	    toAdd = new HashMap<Uri, Set<Statement>>(1) ;
	} else {
	    modelUri = anzo.newId() ;
	    model.setId(modelUri.toString()) ;
	    toAdd = buildModelGraphs(model) ;
	    toAdd.get(modelUri).add(getCreatorStatement(modelUri)) ;
	}
	Statement entryRef = stmt(entryUri, uri(CMEF.ObjectProperties.MODELS), modelUri) ;
	toAdd.put(entryUri, Collections.singleton(entryRef)) ;
	anzo.updateGraphs(null, toAdd) ;
	Set<String> modelIds = getModelIds(model) ;
	authorizer.updateObjectsAclFromEntry(modelIds, actualEntry.getId()) ;
	return BeanCopyUtils.copyModel(model) ;
    }
    
    @Override
    public ComputationalModel getModel(ComputationalModel model) throws RepositoryException, AuthorizationException {
	if (model == null) {
	    throw new IllegalArgumentException("Model must not be null.") ;
	}
	return getModel(model.getId()) ;
    }

    @Override
    public ComputationalModel updateModel(ComputationalModel model) throws RepositoryException, AuthorizationException {
	if (model == null) {
	    throw new IllegalArgumentException("Model must not be null.") ;
	}
	Uri modelUri = uri(model.getId()) ;
	if (!authorizer.canEditResource(modelUri)) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to edit model "
					     + model.getId() + ".") ;
	}
	ComputationalModel oldModel = getModel(model.getId()) ;
	Map<Uri, Set<Statement>> oldGraphs = buildModelGraphs(oldModel) ;
	Map<Uri, Set<Statement>> newGraphs = buildModelGraphs(model) ;
	updateGraphs(oldGraphs, newGraphs) ;
	return BeanCopyUtils.copyModel(model) ;
    }
    
    @Override
    public Iterator<ComputationJob> listJobsForModel(ComputationalModel model) throws RepositoryException, AuthorizationException {
	if (model == null) {
	    throw new IllegalArgumentException("Model must not be null.") ;
	}
	Set<String> referencedJobs = getIdsOfReferencedJobs(model) ;
	List<ComputationJob> jobs = new ArrayList<ComputationJob>() ;
	for (String id : referencedJobs) {
	    jobs.add(getShallowJob(id)) ;
	}
	return jobs.iterator() ;
    }

    @Override
    public ComputationJob addJobToModel(ComputationJob job, ComputationalModel model) throws RepositoryException, AuthorizationException {
	if (job == null) {
	    throw new IllegalArgumentException("Job may not be null.") ;
	}
	if (model == null) {
	    throw new IllegalArgumentException("Model may not be null.") ;
	}
	Collection<Entry> entries = model.getEntry() ;
	if (entries.size() != 1) {
	    throw new IllegalArgumentException("Model must have association to a single Entry but it has: " + entries.size() + ".") ;
	}
	Entry entry = entries.iterator().next() ;
	Entry actualEntry = getEntry(entry.getId()) ;
	ComputationalModel actualModel = getModel(model.getId()) ;
	//TODO: probably want to "normalize" job (at least set Parameter id's if only name is set)...
	Uri entryUri = uri(actualEntry.getId()) ;
	Uri modelUri = uri(actualModel.getId()) ;
	
	if (!authorizer.canEditResource(entryUri)) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to add job to model " + modelUri + ".") ;
	}
	
	Uri jobUri ;
	Map<Uri, Set<Statement>> toAdd ;
	if (job.getId() != null) {
	    jobUri = uri(job.getId()) ;
	    toAdd = new HashMap<Uri, Set<Statement>>(1) ;
	} else {
	    jobUri = anzo.newId() ;
	    job.setId(jobUri.toString()) ;
	    toAdd = buildJobGraphs(job) ;
	    toAdd.get(jobUri).add(getCreatorStatement(jobUri)) ;
	}
	toAdd.get(jobUri).add(stmt(jobUri, uri(CMEF.ObjectProperties.MODEL), modelUri)) ;
	toAdd.put(entryUri, Collections.singleton(stmt(entryUri, uri(CMEF.ObjectProperties.JOBS), jobUri))) ;
	toAdd.put(modelUri, Collections.singleton(stmt(modelUri, uri(CMEF.ObjectProperties.JOB), jobUri))) ;
	anzo.updateGraphs(null, toAdd) ;
	Set<String> jobIds = getJobIds(job) ;
	authorizer.updateObjectsAclFromEntry(jobIds, actualEntry.getId()) ;
	return BeanCopyUtils.copyJob(job) ;
    }

    @Override
    public ComputationJob getJob(ComputationJob job) throws RepositoryException, AuthorizationException {
	if (job == null) {
	    throw new IllegalArgumentException("Job must not be null.") ;
	}
	return getJob(job.getId()) ;
    }

    @Override
    public ComputationJob updateJob(ComputationJob job) throws RepositoryException, AuthorizationException {
	if (job == null) {
	    throw new IllegalArgumentException("Job must not be null.") ;
	}
	Uri jobUri = uri(job.getId()) ;
	if (!authorizer.canEditResource(jobUri)) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to edit model "
					     + job.getId() + ".") ;
	}
	ComputationJob oldJob = getJob(job.getId()) ;
	Map<Uri, Set<Statement>> oldGraphs = buildJobGraphs(oldJob) ;
	Map<Uri, Set<Statement>> newGraphs = buildJobGraphs(job) ;
	updateGraphs(oldGraphs, newGraphs) ;
	return BeanCopyUtils.copyJob(job) ;
    }

    //TODO: make responsibility of collaborator (i.e. EntryRepository)
    public Entry getEntry(String id) throws AuthorizationException, RepositoryException {
	if (id == null) {
	    throw new IllegalArgumentException("Entry id may not be null.") ;
	}
	if (!authorizer.canViewResource(uri(id))) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to view entry " + id + ".") ;
	}
	CQLQuery query = newQuery().forObject(Entry.class.getName()).with(attribute("id").equalTo(id)).build() ;
	List<Entry> results ;
	try {
	    results = queryProcessor.processObjectQuery(query, Entry.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Unable to retrieve entry from repository.", e) ;
	}
	if (results.size() < 1) {
//	    throw new IllegalArgumentException("Entry with id <" + id + "> does not exist.") ;
	    //TODO: different impls of new collaborator can handle this in different ways...
	    Entry result = new Entry() ;
	    result.setId(id) ;
	    return result ;
	}
	if (results.size() > 1) {
	    logger.warn("Found more than one entry with id: {}.", id) ;
	}
	return results.get(0) ;
    }
    
    public ComputationalModel getModel(String id) throws RepositoryException, AuthorizationException {
	if (id == null || "".equals(id)) {
	    throw new IllegalArgumentException("Must provide valid id.") ;
	}
	ComputationalModel result = getShallowModel(id) ;
	addComputingPlatforms(result) ;
	addProgrammingPlatform(result) ;
	addParameters(result) ;
	addDocument(result) ;
	addFiles(result) ;
	return result ;
    }
    
    public ComputationJob getJob(String id) throws RepositoryException, AuthorizationException {
	if (id == null || "".equals(id)) {
	    throw new IllegalArgumentException("Must provide valid id") ;
	}
	ComputationJob result = getShallowJob(id) ;
	addResultFiles(result) ;
	addParameterValues(result) ;
	return result ;
    }
    
    private Statement getCreatorStatement(Uri subject) {
	return anzo.getRdfFactory().createStatement(subject, anzo.getRdfFactory().createUri(DC11.CREATOR),
	    authorizer.getUserUri()) ;
    }
    
    private ComputationalModel getShallowModel(String id) throws AuthorizationException, RepositoryException {
	if (id == null) {
	    throw new IllegalArgumentException("Model id may not be null.") ;
	}
	if (!authorizer.canViewResource(uri(id))) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to view model " + id + ".") ;
	}
	CQLQuery query = newQuery().forObject(ComputationalModel.class.getName()).with(attribute("id").equalTo(id)).build() ;
	List<ComputationalModel> results ;
	try {
	    results = queryProcessor.processObjectQuery(query, ComputationalModel.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Unable to retrieve model from repository.", e) ;
	}
	if (results.size() < 1) {
	    throw new IllegalArgumentException("Model with id <" + id + "> does not exist.") ;
	}
	if (results.size() > 1) {
	    logger.warn("Found more than one model with id: {}.", id) ;
	}
	return results.get(0) ;
    }
    
    private void addComputingPlatforms(ComputationalModel model) throws RepositoryException {
	if (model == null || model.getId() == null) {
	    throw new IllegalArgumentException("Model must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forObject(ComputingPlatform.class.getName()).with(
	    associationTo(ComputationalModel.class.getName()).withRole("model").with(
		attribute("id").equalTo(model.getId()))).build() ;
	List<ComputingPlatform> platforms ;
	try {
	    platforms = queryProcessor.processObjectQuery(query, ComputingPlatform.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving model's computing platforms from repository.", e) ;
	}
	model.setPlatforms(platforms) ;
    }
    
    private void addProgrammingPlatform(ComputationalModel model) throws RepositoryException {
	if (model == null || model.getId() == null) {
	    throw new IllegalArgumentException("Model must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forObject(ProgrammingPlatform.class.getName()).with(
	    associationTo(ComputationalModel.class.getName()).withRole("model").with(
		attribute("id").equalTo(model.getId()))).build() ;
	List<ProgrammingPlatform> platforms ;
	try {
	    platforms = queryProcessor.processObjectQuery(query, ProgrammingPlatform.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving model's programming platform from repository.", e) ;
	}
	if (platforms.size() > 1) {
	    logger.warn("Found more than one ProgrammingPlatform for model with id: {}.", model.getId()) ;
	}
	if (platforms.size() > 0) {
	    model.setProgram(platforms.get(0)) ;
	}
    }
    
    private void addParameters(ComputationalModel model) throws RepositoryException {
	if (model == null || model.getId() == null) {
	    throw new IllegalArgumentException("Model must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forObject(Parameter.class.getName()).with(
	    associationTo(ComputationalModel.class.getName()).withRole("model").with(
		attribute("id").equalTo(model.getId()))).build() ;
	List<Parameter> params ;
	try {
	    params = queryProcessor.processObjectQuery(query, Parameter.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving model's parameters from repository.", e) ;
	}
	model.setParameters(params) ;
    }
    
    private void addDocument(ComputationalModel model) throws RepositoryException {
	if (model == null || model.getId() == null) {
	    throw new IllegalArgumentException("Model must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forObject(File.class.getName()).with(
	    associationTo(ComputationalModel.class.getName()).withRole("documentModel").with(
		attribute("id").equalTo(model.getId()))).build() ;
	List<File> files ;
	try {
	    files = queryProcessor.processObjectQuery(query, File.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving model's document from repository.", e) ;
	}
	if (files.size() > 1) {
	    logger.warn("Found more than one document File for model with id: {}.", model.getId()) ;
	}
	if (files.size() > 0) {
	    model.setDocument(files.get(0)) ;
	}
    }
    
    private void addFiles(ComputationalModel model) throws RepositoryException {
	if (model == null || model.getId() == null) {
	    throw new IllegalArgumentException("Model must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forObject(File.class.getName()).with(
	    associationTo(ComputationalModel.class.getName()).withRole("fileModel").with(
		attribute("id").equalTo(model.getId()))).build() ;
	List<File> files ;
	try {
	    files = queryProcessor.processObjectQuery(query, File.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving model's files from repository.", e) ;
	}
	model.setFiles(files) ;
    }
    
    private ComputationJob getShallowJob(String id) throws RepositoryException, AuthorizationException {
	if (id == null) {
	    throw new IllegalArgumentException("Job id may not be null.") ;
	}
	if (!authorizer.canViewResource(uri(id))) {
	    throw new AuthorizationException("User " + anzo.getCurrentUser() + " is not authorized to view job " + id + ".") ;
	}
	CQLQuery query = newQuery().forObject(ComputationJob.class.getName()).with(attribute("id").equalTo(id)).build() ;
	List<ComputationJob> results ;
	try {
	    results = queryProcessor.processObjectQuery(query, ComputationJob.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Unable to retrieve job from repository.", e) ;
	}
	if (results.size() < 1) {
	    throw new IllegalArgumentException("Job with id <" + id + "> does not exist.") ;
	}
	if (results.size() > 1) {
	    logger.warn("Found more than one job with id: {}.", id) ;
	}
	return results.get(0) ;
    }
    
    private void addResultFiles(ComputationJob job) throws RepositoryException {
	if (job == null || job.getId() == null) {
	    throw new IllegalArgumentException("Job must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forObject(File.class.getName()).with(
	    associationTo(ComputationJob.class.getName()).withRole("job").with(
		attribute("id").equalTo(job.getId()))).build() ;
	List<File> files ;
	try {
	    files = queryProcessor.processObjectQuery(query, File.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving model's files from repository.", e) ;
	}
	job.setResultFiles(files) ;
    }
    
    private void addParameterValues(ComputationJob job) throws RepositoryException {
	if (job == null || job.getId() == null) {
	    throw new IllegalArgumentException("Job must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forObject(ParameterValue.class.getName()).with(
	    associationTo(ComputationJob.class.getName()).withRole("job").with(
		attribute("id").equalTo(job.getId()))).build() ;
	List<ParameterValue> values ;
	try {
	    values = queryProcessor.processObjectQuery(query, ParameterValue.class) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving job's parameter values from repository.", e) ;
	}
	addParameterToValues(values) ;
	job.setParameterValues(values) ;
    }
    
    private void addParameterToValues(List<ParameterValue> values) throws RepositoryException {
	if (values == null) {
	    throw new IllegalArgumentException("Value list must not be null.") ;
	}
	for (ParameterValue value : values) {
	    if (value.getId() == null) {
		throw new IllegalArgumentException("All values must have valid id set.") ;
	    }
	    CQLQuery query = newQuery().forObject(Parameter.class.getName()).with(
		associationTo(ParameterValue.class.getName()).withRole("value").with(
		    attribute("id").equalTo(value.getId()))).build() ;
	    List<Parameter> params ;
	    try {
		params = queryProcessor.processObjectQuery(query, Parameter.class) ;
	    } catch (QueryProcessingException e) {
		throw new RepositoryException("Exception retrieving parameter value's parameter from repository.", e) ;
	    }
	    if (params.size() < 1) {
		throw new IllegalArgumentException("No parameter found for parameter value with id: " + value.getId() + ".") ;
	    }
	    if (params.size() > 1) {
		logger.warn("Found multiple parameters for parameter value with id: " + value.getId() + ".") ;
	    }
	    value.setParameter(params.get(0)) ;
	}
    }
    
    private Map<Uri, Set<Statement>> buildModelGraphs(ComputationalModel model) throws RepositoryException {
	if (model == null || model.getId() == null) {
	    throw new IllegalArgumentException("Model must not be null, and must have id attribute set.") ;
	}
	Uri modelUri = uri(model.getId()) ;
	Map<Uri, Set<Statement>> result = new HashMap<Uri, Set<Statement>>() ;
	Set<Statement> modelGraph = toRdf(model) ;
	result.put(modelUri, modelGraph) ;
	if (model.getPlatforms() != null) {
	    for (ComputingPlatform plat : model.getPlatforms()) {
		Uri platUri ;
		if (plat.getId() != null) {
		    platUri = uri(plat.getId()) ;
		} else {
		    platUri = anzo.newId() ;
		    plat.setId(platUri.toString()) ;
		    result.put(platUri, toRdf(plat)) ;
		}
		modelGraph.add(stmt(modelUri, uri(CMEF.ObjectProperties.COMPUTING_PLATFORM), platUri)) ;
	    }
	}
	if (model.getProgram() != null) {
	    ProgrammingPlatform prog = model.getProgram() ;
	    Uri progUri ;
	    if (prog.getId() != null) {
		progUri = uri(prog.getId()) ;
	    } else {
		progUri = anzo.newId() ;
		prog.setId(progUri.toString()) ;
		result.put(progUri, toRdf(prog)) ;
	    }
	    modelGraph.add(stmt(modelUri, uri(CMEF.ObjectProperties.PROGRAMMING_PLATFORM), progUri)) ;
	}
	if (model.getParameters() != null) {
	    for (Parameter param : model.getParameters()) {
		Uri paramUri ;
		if (param.getId() != null) {
		    paramUri = uri(param.getId()) ;
		} else {
		    paramUri = anzo.newId() ;
		    param.setId(paramUri.toString()) ;
		    result.put(paramUri, toRdf(param)) ;
		}
		modelGraph.add(stmt(modelUri, uri(CMEF.ObjectProperties.PARAMETER), paramUri)) ;
	    }
	}
	if (model.getDocument() != null) {
	    File doc = model.getDocument() ;
	    Uri docUri ;
	    if (doc.getId() != null) {
		docUri = uri(doc.getId()) ;
	    } else {
		docUri = anzo.newId() ;
		doc.setId(docUri.toString()) ;
		result.put(docUri, toRdf(doc)) ;
	    }
	    modelGraph.add(stmt(modelUri, uri(CMEF.ObjectProperties.DOCUMENT), docUri)) ;
	}
	if (model.getFiles() != null) {
	    for (File file : model.getFiles()) {
		Uri fileUri ;
		if (file.getId() != null) {
		    fileUri = uri(file.getId()) ;
		} else {
		    fileUri = anzo.newId() ;
		    file.setId(fileUri.toString()) ;
		    result.put(fileUri, toRdf(file)) ;
		}
		modelGraph.add(stmt(modelUri, uri(CMEF.ObjectProperties.FILE), fileUri)) ;
	    }
	}
	return result ;
    }
    
    private Map<Uri,Set<Statement>> buildJobGraphs(ComputationJob job) throws RepositoryException {
	if (job == null || job.getId() == null) {
	    throw new IllegalArgumentException("Job must not be null, and must have id attribute set.") ;
	}
	Uri jobUri = uri(job.getId()) ;
	Map<Uri,Set<Statement>> result = new HashMap<Uri, Set<Statement>>() ;
	Set<Statement> jobGraph = toRdf(job) ;
	result.put(jobUri, jobGraph) ;
	if (job.getResultFiles() != null) {
	    for (File file : job.getResultFiles()) {
		Uri fileUri ;
		if (file.getId() != null) {
		    fileUri = uri(file.getId()) ;
		} else {
		    fileUri = anzo.newId() ;
		    file.setId(fileUri.toString()) ;
		    result.put(fileUri, toRdf(file)) ;
		}
		jobGraph.add(stmt(jobUri, uri(CMEF.ObjectProperties.RESULT), fileUri)) ;
	    }
	}
	if (job.getParameterValues() != null) {
	    for (ParameterValue value : job.getParameterValues()) {
		if (value.getParameter() == null || value.getParameter().getId() == null) {
		    throw new RepositoryException("Invalid job: ParameterValues must reference Parameter with id attribute set.") ;
		}
		Uri valueUri ;
		if (value.getId() != null) {
		    valueUri = uri(value.getId()) ;
		} else {
		    valueUri = anzo.newId() ;
		    value.setId(valueUri.toString()) ;
		    Set<Statement> valueGraph = toRdf(value) ;
		    valueGraph.add(stmt(valueUri, uri(CMEF.ObjectProperties.PARAMETER), uri(value.getParameter().getId()))) ;
		    result.put(valueUri, valueGraph) ;
		}
		jobGraph.add(stmt(jobUri, uri(CMEF.ObjectProperties.PARAMETER_VALUE), valueUri)) ;
	    }
	}
	return result ;
    }
    
    private Set<String> getIdsOfReferencedModels(Entry entry) throws RepositoryException {
	if (entry == null || entry.getId() == null) {
	    throw new IllegalArgumentException("Entry must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forAttributes(ComputationalModel.class.getName(), "id").with(
	    associationTo(Entry.class.getName()).withRole("entry").with(
		attribute("id").equalTo(entry.getId()))).build() ;
	List<String[]> tuples ;
	try {
	    tuples = queryProcessor.processAttributeQuery(query) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving entries' model ids from repository.", e) ;
	}
	Set<String> result = new HashSet<String>() ;
	for (String[] row : tuples) {
	    result.add(row[0]) ;
	}
	return result ;
    }
    
    private Set<String> getIdsOfReferencedJobs(ComputationalModel model) throws RepositoryException {
	if (model == null || model.getId() == null) {
	    throw new IllegalArgumentException("Model must not be null, and must have a valid id.") ;
	}
	CQLQuery query = newQuery().forAttributes(ComputationJob.class.getName(), "id").with(
	    associationTo(ComputationalModel.class.getName()).withRole("model").with(
		attribute("id").equalTo(model.getId()))).build() ;
	List<String[]> tuples ;
	try {
	    tuples = queryProcessor.processAttributeQuery(query) ;
	} catch (QueryProcessingException e) {
	    throw new RepositoryException("Exception retrieving models' job ids from repository.", e) ;
	}
	Set<String> result = new HashSet<String>() ;
	for (String[] row : tuples) {
	    result.add(row[0]) ;
	}
	return result ;
    }
    
    private Set<String> getModelIds(ComputationalModel model) {
	Set<String> result = new HashSet<String>() ;
	result.add(model.getId()) ;
	if (model.getProgram() != null) {
	    result.add(model.getProgram().getId()) ;
	}
	if (model.getDocument() != null) {
	    result.add(model.getDocument().getId()) ;
	}
	if (model.getPlatforms() != null) {
	    for (ComputingPlatform plat : model.getPlatforms()) {
		result.add(plat.getId()) ;
	    }
	}
	if (model.getFiles() != null) {
	    for (File file : model.getFiles()) {
		result.add(file.getId()) ;
	    }
	}
	if (model.getParameters() != null) {
	    for (Parameter param : model.getParameters()) {
		result.add(param.getId()) ;
	    }
	}
	return result ;
    }
    
    private Set<String> getJobIds(ComputationJob job) {
	Set<String> result = new HashSet<String>() ;
	result.add(job.getId()) ;
	if (job.getResultFiles() != null) {
	    for (File file : job.getResultFiles()) {
		result.add(file.getId()) ;
	    }
	}
	if (job.getParameterValues() != null) {
	    for (ParameterValue value : job.getParameterValues()) {
		result.add(value.getId()) ;
	    }
	}
	return result ;
    }
    
    private void updateGraphs(Map<Uri, Set<Statement>> oldGraphs, Map<Uri, Set<Statement>> newGraphs) throws RepositoryException {
	Map<Uri, Set<Statement>> removed = new HashMap<Uri, Set<Statement>>() ;
	Map<Uri, Set<Statement>> added = new HashMap<Uri, Set<Statement>>() ;
	
	Set<Uri> graphs = new HashSet<Uri>() ;
	graphs.addAll(oldGraphs.keySet()) ;
	graphs.addAll(newGraphs.keySet()) ;
	for (Uri graph : graphs) {
	    GraphDiff diff = diffGraphs(oldGraphs.get(graph), newGraphs.get(graph)) ;
	    if (!diff.getRemovedStatements().isEmpty()) {
		removed.put(graph, diff.getRemovedStatements()) ;
	    }
	    if (!diff.getAddedStatements().isEmpty()) {
		added.put(graph, diff.getAddedStatements()) ;
	    }
	}
	
	anzo.updateGraphs(removed, added) ;
    }
    
    private GraphDiff diffGraphs(Set<Statement> oldGraph, Set<Statement> newGraph) {
	if (oldGraph == null) {
	    oldGraph = Collections.emptySet() ;
	}
	if (newGraph == null) {
	    newGraph = Collections.emptySet() ;
	}
	Set<Statement> toRemove = new HashSet<Statement>() ;
	toRemove.addAll(oldGraph) ;
	toRemove.removeAll(newGraph) ;

	Set<Statement> toAdd = new HashSet<Statement>() ;
	toAdd.addAll(newGraph) ;
	toAdd.removeAll(oldGraph) ;

	return new GraphDiff(toRemove, toAdd) ;
    }
    
    private Set<Statement> toRdf(Object obj) throws RepositoryException {
	Set<Statement> result ;
	try {
	    result = mapper.toRdf(obj) ;
	} catch (MappingException e) {
	    throw new RepositoryException("Error converting " + obj.getClass().getSimpleName() + " to RDF.", e) ;
	}
	return result ;
    }
    
    private Uri uri(String uri) {
	try {
	    return anzo.getRdfFactory().createUri(uri) ;
	} catch (Throwable e) {
	    throw new RuntimeException("Exception creating uri: " + uri + ".") ;
	}
    }
    
    private Statement stmt(Resource s, Uri p, Value o) {
	return anzo.getRdfFactory().createStatement(s, p, o) ;
    }
    
    private static class GraphDiff {
	private Set<Statement> removed ;
	private Set<Statement> added ;
	
	public GraphDiff(Set<Statement> removed, Set<Statement> added) {
	    if (removed == null) {
		removed = Collections.emptySet() ;
	    }
	    if (added == null) {
		added = Collections.emptySet() ;
	    }
	    this.removed = removed ;
	    this.added = added ;
	}
	
	public boolean wereDifferent() {
	    return removed.size() > 0 || added.size() > 0 ;
	}
	
	public Set<Statement> getRemovedStatements() {
	    return removed ;
	}
	
	public Set<Statement> getAddedStatements() {
	    return added ;
	}
    }
}
