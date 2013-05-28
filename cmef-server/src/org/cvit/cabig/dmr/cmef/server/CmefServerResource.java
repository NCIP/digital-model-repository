/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server;

import java.io.IOException ;
import java.io.InputStream ;

import org.cvit.cabig.dmr.cmef.AuthorizationException ;
import org.cvit.cabig.dmr.cmef.CmefRepository ;
import org.cvit.cabig.dmr.cmef.CmefService ;
import org.cvit.cabig.dmr.cmef.CmefServiceException ;
import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.RepositoryException ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.cvit.cabig.dmr.cmef.CmefService.JobState ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.json.JSONComputationJob ;
import org.cvit.cabig.dmr.cmef.json.JSONComputationalModel ;
import org.cvit.cabig.dmr.cmef.monitoring.JobMonitoringService ;
import org.cvit.cabig.dmr.domain.Entry ;
import org.json.JSONObject ;
import org.restlet.data.Status ;
import org.restlet.resource.ResourceException ;
import org.restlet.resource.ServerResource ;

import freemarker.template.Configuration ;

public abstract class CmefServerResource extends ServerResource {
    private static final JSONComputationalModel modelConverter = new JSONComputationalModel() ;
    private static final JSONComputationJob jobConverter = new JSONComputationJob() ;
    
    private UserCredentials user ;
    private CmefNamespace namespace ;
    private CmefRepository repository ;
    private FileStore fileStore ;
    
    @Override
    protected void doInit() throws ResourceException {
        super.doInit() ;
        String un = getChallengeResponse().getIdentifier() ;
        String pw = new String(getChallengeResponse().getSecret()) ;
        user = new UserCredentials(un, pw) ;
        repository = getCmefServer().getRepository(user) ;
        fileStore = getCmefServer().getFileStore(user) ;
        namespace = new CmefNamespace(getCmefServer(), getRootRef(), getHostRef()) ;
    }
    
    private CmefServer getCmefServer() {
	return (CmefServer) getApplication() ;
    }
    
    protected CmefRepository getRepository() {
	return repository ;
    }
    
    protected FileStore getFileStore() {
	return fileStore ;
    }
    
    protected CmefService getCmefService() {
	return getCmefServer().getCmefService() ;
    }
    
    protected JobMonitoringService getMonitorService() {
	return getCmefServer().getMonitorService() ;
    }
    
    protected Configuration getFreeMarkerConfig() {
	return getCmefServer().getFreeMarkerConfig() ;
    }
    
    protected UserCredentials getCurrentUser() {
	return user ;
    }
    
    /********************
    * Namespace methods *
    *********************/
    
    protected IdResolver getResolver() {
	return getCmefServer().getResolver() ;
    }
    
    protected CmefNamespace getNamespace() {
	return namespace ;
    }
    
    /***************************
     * JSON Conversion Methods *
     ***************************/
    
    protected ComputationalModel modelFromJson(JSONObject json) {
	return (ComputationalModel) modelConverter.fromJSON(json) ;
    }
    
    protected JSONObject jsonModel(ComputationalModel model) {
	return modelConverter.toJSON(model) ;
    }
    
    protected ComputationJob jobFromJson(JSONObject json) {
	return (ComputationJob) jobConverter.fromJSON(json) ;
    }
    
    protected JSONObject jsonJob(ComputationJob job) {
	return jobConverter.toJSON(job) ;
    }
    
    /**********************
     * Repository Methods *
     **********************/
    
    protected ComputationalModel loadModel(String modelName) {
        String modelId = getResolver().getModelId(modelName) ;
        try {
            return getRepository().getModel(modelRef(modelId)) ;
        } catch (RepositoryException e) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception retrieving model from CMEF repository: " + modelId + ".", e) ;
        } catch (AuthorizationException e) {
	    throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "Not authorized to access model: " + modelId + ".", e) ;
	    
	}
    }
    
    protected ComputationJob loadJob(String jobName) {
	String jobId = getResolver().getJobId(jobName) ;
	ComputationJob result ;
	try {
	    result = getRepository().getJob(jobRef(jobId)) ;
	} catch (RepositoryException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception retrieving job from CMEF repository: " + jobId + ".", e) ;
	} catch (AuthorizationException e) {
	    throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "User " + getCurrentUser() + " is not authorized to access job: " + jobId + ".", e) ;
	}
	if (result != null && isInCmefService(result)) {
	    SynchronizeJobTask syncTask = new SynchronizeJobTask(getCmefService(), getFileStore(), getRepository(), result) ;
            try {
		result = syncTask.call() ;
	    } catch (Exception e) {
		throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception checking status of running job: " + e.getMessage(), e) ;
	    }
        }
	return result ;
    }
    
//    private ComputationJob synchronizeJob(ComputationJob job) {
//	ComputationJob copy = BeanCopyUtils.copyJob(job) ;
//	try {
//	    copy = getCmefService().checkStatus(copy) ;
//	} catch (CmefServiceException e) {
//	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception checking status of running job: " + e.getMessage(), e) ;
//	}
//	if (isFinished(copy)) {
//	    transferCompletedJob(copy) ;
//	} else {
//	    updateJob(copy) ;
//	}
//	return copy ;
//    }
//    
//    private void transferCompletedJob(ComputationJob job) {
//	try {
//	    job = getCmefService().getJobResult(job, getFileStore()) ;
//	} catch (CmefServiceException e) {
//	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception retrieving job results: " + e.getMessage(), e) ;
//	}
//	if (isRunning(job)) {
//	    throw new IllegalStateException("Job is still running...") ;
//	}
//	updateJob(job) ;
//	try {
//	    getCmefService().deleteJob(job) ;
//	} catch (CmefServiceException e) {
//	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception removing job from CMEF service: " + e.getMessage(), e) ;
//	}
//    }
    
    protected boolean isInCmefService(ComputationJob job) {
	switch (checkJobState(job)) {
	    case NOT_SUBMITTED:
		return false ;
	    default:
		return true ;
	}
    }
    
    protected boolean isRunning(ComputationJob job) {
	switch (checkJobState(job)) {
	    case RUNNING:
		return true ;
	    default:
		return false ;
	}
    }
    
    protected boolean isFinished(ComputationJob job) {
	switch(checkJobState(job)) {
	    case FINISHED:
		return true ;
	    default:
		return false ;
	}
    }
    
    protected void updateJob(ComputationJob job) {
	try {
	    getRepository().updateJob(job) ;
	} catch (RepositoryException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception updating job in CMEF repository, see cause.", e) ;
	} catch (AuthorizationException e) {
	    throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "User " + getCurrentUser() + " is not authorized to update job: " + job.getId() + ".", e) ;
	}
    }
    
    protected JobState checkJobState(ComputationJob job) {
	try {
	    return getCmefService().checkJobState(job) ;
	} catch (CmefServiceException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception checking job state in CMEF service, see cause.", e) ;
	}
    }
    
    /****************
     * File Support *
     ****************/
    
    protected File storeFile(String name, InputStream data) {
	try {
	    return getFileStore().storeFile(name, data) ;
	} catch (IOException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception storing file in repository: " + e.getMessage(), e) ;
	}
    }
    
    /**************************
     * CMEF Reference Helpers *
     **************************/
    
    protected Entry entryRef(String id) {
	Entry result = new Entry() ;
	result.setId(id) ;
	return result ;
    }
    
    protected ComputationalModel modelRef(String id) {
	ComputationalModel result = new ComputationalModel() ;
	result.setId(id) ;
	return result ;
    }
    
    protected ComputationJob jobRef(String id) {
	ComputationJob result = new ComputationJob() ;
	result.setId(id) ;
	return result ;
    }
    
    /**
     * Allow access to CMEF Resource functions through templates.
     */
    public class TemplateFunctions {
	
	public String modelName(String id) {
	    return getResolver().getModelName(id) ;
	}
	
	public String jobName(String id) {
	    return getResolver().getJobName(id) ;
	}
	
	public String modelsUrl(String entryName) {
	    return getNamespace().modelsRef(entryName, false).toString() ;
	}
	
	public String modelUrl(String entryName, String modelName) {
	    return getNamespace().modelRef(entryName, modelName, false).toString() ;
	}
	
	public String jobsUrl(String entryName, String modelName) {
	    return getNamespace().jobsRef(entryName, modelName, false).toString() ;
	}
	
	public String jobUrl(String entryName, String modelName, String jobName) {
	    return getNamespace().jobRef(entryName, modelName, jobName, false).toString() ;
	}
	
	public String fileUrl(String relativePath) {
	    return getNamespace().fileRef(relativePath, false).toString() ;
	}
    }
}
