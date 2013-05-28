/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.condor;

import org.cvit.cabig.dmr.cmef.CmefService;
import org.cvit.cabig.dmr.cmef.CmefServiceException;
import org.cvit.cabig.dmr.cmef.CmefService.JobState;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.monitoring.JobListener ;

public class StateListener {
    CmefService service ;
    ComputationJob previousJob ;
    JobState previousState ;

    public StateListener(CmefService service) {
	this.service = service ;
    }

    public void checkForUpdate(ComputationJob job, JobListener listener) {
	// if status has changed (anything set by CmefService.checkStatus),
	// or state has changed call listener.statusChanged
	JobState currentState ;
	try {
	    currentState = service.checkJobState(job) ;

	    //If first time this method is run, or state has changed, 
	    //or status has changed, update status
	    if (previousState == null || (currentState != previousState) || (!jobStatusesAreEqual(job, previousJob))) {
		listener.statusChanged(job, currentState) ;
	    }
	    previousJob = job ;
	    previousState = currentState ;
	} catch (CmefServiceException e) {
	    //Do Nothing
	}
    }
	
    private boolean jobStatusesAreEqual(ComputationJob job1, ComputationJob job2) {
	if (job1 == null && job2 == null) {
	    return true ;
	}
	if (job1 == null || job2 == null) {
	    return false ;
	}
	return (objectsMatch(job1.getDateSubmitted(), job2.getDateSubmitted()) && 
		objectsMatch(job1.getDateCompleted(), job2.getDateCompleted()) && 
		objectsMatch(job1.getJobStatus(), job2.getJobStatus())) ;
    }

    private boolean objectsMatch(Object first, Object second) {
	return (first == null && second == null) || (first != null && second != null && first.equals(second)) ;
    }
}
