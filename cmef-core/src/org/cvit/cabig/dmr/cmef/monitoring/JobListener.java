package org.cvit.cabig.dmr.cmef.monitoring;

import org.cvit.cabig.dmr.cmef.CmefService ;
import org.cvit.cabig.dmr.cmef.CmefService.JobState ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;

public interface JobListener {

    public void statusChanged(ComputationJob job, JobState state) ;
}
