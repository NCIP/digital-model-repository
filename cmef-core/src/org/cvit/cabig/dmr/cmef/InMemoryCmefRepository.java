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
