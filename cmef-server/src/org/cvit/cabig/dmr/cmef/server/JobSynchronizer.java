package org.cvit.cabig.dmr.cmef.server;

import org.cvit.cabig.dmr.cmef.CmefService.JobState ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.monitoring.JobListener ;

public class JobSynchronizer implements JobListener {
    private final CmefServerResource resource ;

    public JobSynchronizer(CmefServerResource resource) {
	if (resource == null) {
	    throw new IllegalArgumentException("CMEF Server Resource must not be null.") ;
	}
	this.resource = resource ;
    }

    public void statusChanged(ComputationJob job, JobState state) {
	if (state == JobState.FINISHED || state == JobState.NOT_SUBMITTED) {
	    resource.getMonitorService().unregisterJobListener(this) ;
	}
	SynchronizeJobTask task = new SynchronizeJobTask(resource.getCmefService(), resource.getFileStore(), resource.getRepository(), job) ;
	resource.getApplication().getTaskService().submit(task) ;
    }
    
}
