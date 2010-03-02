/*******************************************************************************
 * caBIG™ Open Source Software License
 * Computational Model Execution Framework (CMEF) v1.0
 * 
 * Copyright Notice.
 * Copyright 2010 Massachusetts General Hospital (“caBIG™ Participant”).  Computational Model Execution Framework (CMEF) was created with NCI funding and is part of the caBIG™ initiative. The software subject to this notice and license includes both human readable source code form and machine readable, binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant and You.  “You (or “Your”) shall mean a person or an entity, and all other entities that control, are controlled by, or are under common control with the entity.  “Control” for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.  
 * 
 * License.  
 * Provided that You agree to the conditions described below, caBIG™ Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights in the caBIG™ Software, including any copyright or patent rights therein, to (i) use, install, disclose, access, operate, execute, reproduce, copy, modify, translate, market, publicly display, publicly perform, and prepare derivative works of the caBIG™ Software in any manner and for any purpose, and to have or permit others to do so; (ii) make, have made, use, practice, sell, and offer for sale, import, and/or otherwise dispose of caBIG™ Software (or portions thereof); (iii) distribute and have distributed to and by third parties the caBIG™ Software and any modifications and derivative works thereof; and (iv) sublicense the foregoing rights set out in (i), (ii) and (iii) to third parties, including the right to license such rights to further third parties.  For sake of clarity, and not by way of limitation, caBIG™ Participant shall have no right of accounting or right of payment from You or Your sublicensees for the rights granted under this License.  This License is granted at no charge to You.  Your downloading, copying, modifying, displaying, distributing or use of caBIG™ Software constitutes acceptance of all of the terms and conditions of this Agreement.  If you do not agree to such terms and conditions, you have no right to download, copy, modify, display, distribute or use the caBIG™ Software.  
 * 
 * 1.	Your redistributions of the source code for the caBIG™ Software must retain the above copyright notice, this list of conditions and the disclaimer and limitation of liability of Article 6 below.  Your redistributions in object code form must reproduce the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials provided with the distribution, if any.
 * 
 * 2.	Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: “This product includes software developed by Massachusetts General Hospital.”  If You do not include such end-user documentation, You shall include this acknowledgment in the caBIG™ Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3.	You may not use the names  ”Massachusetts General Hospital”, “MGH”, "INFOTECH Soft", “The National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™” to endorse or promote products derived from this caBIG™ Software.  This License does not authorize You to use any trademarks, service marks, trade names, logos or product names of either caBIG™ Participant, NCI or caBIG™, except as required to comply with the terms of this License.
 * 
 * 4.	For sake of clarity, and not by way of limitation, You may incorporate this caBIG™ Software into Your proprietary programs and into any third party proprietary programs.  However, if You incorporate the caBIG™ Software into third party proprietary programs, You agree that You are solely responsible for obtaining any permission from such third parties required to incorporate the caBIG™ Software into such third party proprietary programs and for informing Your sublicensees, including without limitation Your end-users, of their obligation to secure any required permissions from such third parties before incorporating the caBIG™ Software into such third party proprietary software programs.  In the event that You fail to obtain such permissions, You agree to indemnify caBIG™ Participant for any claims against caBIG™ Participant by such third parties, except to the extent prohibited by law, resulting from Your failure to obtain such permissions.
 * 
 * 5.	For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses of modifications of the caBIG™ Software, or any derivative works of the caBIG™ Software as a whole, provided Your use, reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.
 * 
 * 6.	THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED.  IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU)
 * Contributors: INFOTECH Soft, Inc.
 ******************************************************************************/
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
