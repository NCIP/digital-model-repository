/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


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
