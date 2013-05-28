/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef ;

import java.util.ArrayList ;
import java.util.Collection ;
import java.util.Collections ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.List ;
import java.util.Map ;

import static org.cvit.cabig.dmr.cmef.util.BeanCopyUtils.* ;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.cvit.cabig.dmr.domain.Entry ;

//TODO: make thread safe!!!
public class InMemoryCmefRepository implements CmefRepository {
    private CmefUriGenerator uriGenerator ;

    private Map<String, List<String>> entryModels = new HashMap<String, List<String>>() ;
    private Map<String, ComputationalModel> models = new HashMap<String, ComputationalModel>() ;

    private Map<String, List<String>> modelJobs = new HashMap<String, List<String>>() ;
    private Map<String, ComputationJob> jobs = new HashMap<String, ComputationJob>() ;    
    
    public InMemoryCmefRepository(CmefUriGenerator uriGenerator) {
	if (uriGenerator == null) {
	    throw new IllegalArgumentException("CMEF URI Generator may not be null.") ;
	}
	this.uriGenerator = uriGenerator ;
    }

    public ComputationalModel addModelToEntry(ComputationalModel model, Entry entry) throws RepositoryException {
	if (entry.getId() == null || "".equals(entry.getId())) {
	    throw new IllegalArgumentException("Entry must contain a valid id.") ;
	}
	ComputationalModel storedModel = copyModel(model) ;
	setModelIds(entry, storedModel) ;
	addModelToEntry(storedModel.getId(), entry.getId()) ;
	models.put(storedModel.getId(), storedModel) ;
	return copyModel(storedModel) ;
    }

    public Iterator<ComputationalModel> listModelsForEntry(Entry entry) throws RepositoryException {
	List<String> modelIds = entryModels.get(entry.getId()) ;
	Collection<ComputationalModel> modelCollection = collectModels(modelIds) ;
	for (ComputationalModel model : modelCollection) {
	    model.setFiles(null) ;
	    model.setParameters(null) ;
	    model.setPlatforms(null) ;
	    model.setProgram(null) ;
	    model.setDocument(null) ;
	}
	return modelCollection.iterator() ;
    }

    public ComputationalModel getModel(ComputationalModel model) throws RepositoryException {
	if (model.getId() == null || "".equals(model.getId())) {
	    throw new IllegalArgumentException("Model must contain a valid id.") ;
	}
	ComputationalModel storedModel = models.get(model.getId()) ;
	return storedModel != null ? copyModel(storedModel) : null ;
    }

    public ComputationalModel updateModel(ComputationalModel model) throws RepositoryException {
	if (model == null) {
	    throw new IllegalArgumentException("Model may not be null.") ;
	}
	if (model.getId() == null || "".equals(model.getId())) {
	    throw new IllegalArgumentException("Model must contain a valid id.") ;
	}
	ComputationalModel old = models.remove(model.getId()) ;
	if (old == null) {
	    throw new RepositoryException("Repository does not contain a ComputationalModel with id: " + model.getId() + ".") ;
	}
	models.put(model.getId(), copyModel(model)) ;
	return copyModel(model) ;
    }

    public ComputationJob addJobToModel(ComputationJob job, ComputationalModel model) throws RepositoryException {
	if (job == null || model == null) {
	    throw new IllegalArgumentException("Neither Job nor Model may be null.") ;
	}
	if (model.getId() == null || "".equals(model.getId())) {
	    throw new IllegalArgumentException("Model must contain a valid id.") ;
	}
	ComputationJob storedJob = copyJob(job) ;
	setJobIds(model, storedJob) ;
	addJobToModel(storedJob.getId(), model.getId()) ;
	jobs.put(storedJob.getId(), storedJob) ;
	return copyJob(storedJob) ;
    }

    public Iterator<ComputationJob> listJobsForModel(ComputationalModel model) throws RepositoryException {
        List<String> jobIds = modelJobs.get(model.getId()) ;
        return collectJobs(jobIds).iterator() ;
    }

    public ComputationJob getJob(ComputationJob job) throws RepositoryException {
	if (job.getId() == null || "".equals(job.getId())) {
	    throw new IllegalArgumentException("Job must contain a valid id.") ;
	}
	ComputationJob storedJob = jobs.get(job.getId()) ;
	return storedJob != null ? copyJob(storedJob) : null ;
    }

    public ComputationJob updateJob(ComputationJob job) throws RepositoryException {
	if (job == null) {
	    throw new IllegalArgumentException("Job may not be null.") ;
	}
	if (job.getId() == null || "".equals(job.getId())) {
	    throw new IllegalArgumentException("Job must contain a valid id.") ;
	}
	ComputationJob old = jobs.remove(job.getId()) ;
	if (old == null) {
	    throw new RepositoryException("Repository does not contain a ComputationalModel with id: " + job.getId() + ".") ;
	}
	jobs.put(job.getId(), copyJob(job)) ;
	return copyJob(job) ;
    }

    public void setCurrentUser(UserCredentials user) throws AuthenticationException {
    // TODO Auto-generated method stub

    }
    
    private void addModelToEntry(String modelId, String entryId) {
	List<String> modelList = entryModels.get(entryId) ;
	if (modelList == null) {
	    modelList = new ArrayList<String>() ;
	    entryModels.put(entryId, modelList) ;
	}
	modelList.add(modelId) ;
    }
    
    private void addJobToModel(String jobId, String modelId) {
	List<String> jobList = modelJobs.get(modelId) ;
	if (jobList == null) {
	    jobList = new ArrayList<String>() ;
	    modelJobs.put(modelId, jobList) ;
	}
	jobList.add(jobId) ;
    }
    
    private Collection<ComputationalModel> collectModels(Collection<String> modelIds) {
	if (modelIds == null) {
	    return Collections.emptyList() ;
	}
	List<ComputationalModel> result = new ArrayList<ComputationalModel>(modelIds.size()) ;
	for (String id : modelIds) {
	    result.add(copyModel(models.get(id))) ;
	}
	return result ;
    }
    
    private Collection<ComputationJob> collectJobs(List<String> jobIds) {
	if (jobIds == null) {
	    return Collections.emptyList() ;
	}
	List<ComputationJob> result = new ArrayList<ComputationJob>(jobIds.size()) ;
	for (String id : jobIds) {
	    result.add(copyJob(jobs.get(id))) ;
	}
	return result ;
    }

    private void setModelIds(Entry entry, ComputationalModel model) {
	model.setId(uriGenerator.generateModelUri(entry, model)) ;
	if (model.getDocument() != null) {
	    model.getDocument().setId(uriGenerator.generateDocumentUri(entry, model, model.getDocument())) ;
	}
	if (model.getFiles() != null) {
	    for (File file : model.getFiles()) {
		file.setId(uriGenerator.generateModelFileUri(entry, model, file)) ;
	    }
	}
	if (model.getPlatforms() != null) {
	    for (ComputingPlatform plat : model.getPlatforms()) {
		plat.setId(uriGenerator.generatePlatformUri(entry, model, plat)) ;
	    }
	}
	if (model.getProgram() != null) {
	    model.getProgram().setId(uriGenerator.generateProgramUri(entry, model, model.getProgram())) ;
	}
	if (model.getParameters() != null) {
	    for (Parameter param : model.getParameters()) {
		param.setId(uriGenerator.generateParameterUri(entry, model, param)) ;
	    }
	}
    }
    
    private void setJobIds(ComputationalModel model, ComputationJob job) {
	job.setId(uriGenerator.generateJobUri(model, job)) ;
	if (job.getResultFiles() != null) {
	    for (File file : job.getResultFiles()) {
		file.setId(uriGenerator.generateResultFileUri(model, job, file)) ;
	    }
	}
	if (job.getParameterValues() != null) {
	    for (ParameterValue value : job.getParameterValues()) {
		value.setId(uriGenerator.generateParameterValueUri(model, job, value)) ;
	    }
	}
    }
}
