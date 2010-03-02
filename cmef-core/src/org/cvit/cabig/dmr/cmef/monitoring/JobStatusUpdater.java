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
package org.cvit.cabig.dmr.cmef.monitoring;

import java.util.HashMap ;
import java.util.Map ;
import java.util.Map.Entry ;
import java.util.concurrent.ConcurrentHashMap ;

import org.cvit.cabig.dmr.cmef.CmefService ;
import org.cvit.cabig.dmr.cmef.CmefServiceException ;
import org.cvit.cabig.dmr.cmef.CmefService.JobState ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.util.BeanCopyUtils ;

public class JobStatusUpdater implements Runnable {
    private CmefService service ;
    
    private ConcurrentHashMap<JobListener, ComputationJob> listeners = new ConcurrentHashMap<JobListener, ComputationJob>() ;
    
    public JobStatusUpdater(CmefService cmefService) {
	if (cmefService == null) {
	    throw new IllegalArgumentException("CMEF Service must not be null.") ;
	}
	this.service = cmefService ;
    }
    
    public void registerJobListener(JobListener listener, ComputationJob job) {
	if (listener == null) {
	    throw new IllegalArgumentException("Job listener must not be null.") ;
	}
	if (job == null || job.getId() == null || "".equals(job.getId())) {
	    throw new IllegalArgumentException("Job must not be null and must have a valid Id.") ;
	}
	ComputationJob currJob = listeners.putIfAbsent(listener, job) ;
	if (currJob != null) {
	    throw new IllegalArgumentException(String.format("Provided listener already registered for job: %s.", currJob.getId())) ;
	}
    }
    
    public void unregisterJobListener(JobListener listener) {
	listeners.remove(listener) ;
    }
    
    @Override
    public void run() {
	Map<String, StatusUpdateResult> updatedJobs = new HashMap<String, StatusUpdateResult>() ;
	for (Entry<JobListener, ComputationJob> entry : listeners.entrySet()) {
	    JobListener currListener = entry.getKey() ;
	    ComputationJob currJob = entry.getValue() ;
	    if (updatedJobs.containsKey(currJob.getId())) {
		processUpdatedResult(updatedJobs.get(currJob.getId()), currListener) ;
	    } else {
		StatusUpdateResult result = checkForUpdate(currJob) ;
		updatedJobs.put(currJob.getId(), result) ;
		processUpdatedResult(result, currListener) ;
	    }
	}
    }
    
    private void processUpdatedResult(StatusUpdateResult result, JobListener listener) {
	if (result.wasUpdated()) {
	    listener.statusChanged(result.getUpdatedJob(), result.getCurrentJobState()) ;
	    listeners.replace(listener, result.getUpdatedJob()) ;
	}
    }
    
    public StatusUpdateResult checkForUpdate(ComputationJob job) {
	try {
	    ComputationJob oldJob = BeanCopyUtils.copyJob(job) ;
	    ComputationJob currJob = service.checkStatus(job) ;
	    if (!jobStatusesAreEqual(oldJob, currJob)) {
		return StatusUpdateResult.updated(currJob, service.checkJobState(currJob)) ;
	    }
	} catch (CmefServiceException e) {
	    //TODO: handle somehow...at least log...
	}
	return StatusUpdateResult.NOT_UPDATED ;
    }
	
    private boolean jobStatusesAreEqual(ComputationJob job1, ComputationJob job2) {
	if (job1 == null && job2 == null) {
	    return true ;
	}
	if (job1 == null || job2 == null) {
	    return false ;
	}
	return objectsMatch(job1.getDateSubmitted(), job2.getDateSubmitted()) && 
		objectsMatch(job1.getDateCompleted(), job2.getDateCompleted()) && 
		objectsMatch(job1.getJobStatus(), job2.getJobStatus()) ;
    }

    private boolean objectsMatch(Object first, Object second) {
	return (first == null && second == null) || (first != null && second != null && first.equals(second)) ;
    }
    
    public static class StatusUpdateResult {
	private boolean updated ;
	private ComputationJob updatedJob ;
	private JobState jobState ;
	
	public static final StatusUpdateResult NOT_UPDATED = new StatusUpdateResult(false, null, null) ;
	
	public static final StatusUpdateResult updated(ComputationJob updatedJob, JobState currentState) {
	    return new StatusUpdateResult(true, updatedJob, currentState) ;
	}
	
	private StatusUpdateResult(boolean updated, ComputationJob updatedJob, JobState jobState) {
	    if (updated && (updatedJob == null || jobState == null)) {
		throw new IllegalArgumentException("Must supply updated job and its current state.") ;
	    }
	    this.updated = updated ;
	    this.updatedJob = updatedJob ;
	    this.jobState = jobState ;
	}
	
	public boolean wasUpdated() {
	    return updated ;
	}
	
	public ComputationJob getUpdatedJob() {
	    if (!wasUpdated()) {
		throw new IllegalStateException("Job was not updated.") ;
	    }
	    return updatedJob ;
	}
	
	public JobState getCurrentJobState() {
	    if (!wasUpdated()) {
		throw new IllegalStateException("Job was not updated.") ;
	    }
	    return jobState ;
	}
    }
}
