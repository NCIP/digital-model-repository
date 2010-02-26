package org.cvit.cabig.dmr.cmef.server;

import java.util.concurrent.Callable ;

import org.cvit.cabig.dmr.cmef.CmefRepository ;
import org.cvit.cabig.dmr.cmef.CmefService ;
import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.CmefService.JobState ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.util.BeanCopyUtils ;

public class SynchronizeJobTask implements Callable<ComputationJob> {
    private final CmefService service ;
    private final FileStore store ;
    private final CmefRepository repository ;
    private final ComputationJob job ;
    
    private volatile boolean called = false ;

    public SynchronizeJobTask(CmefService service, FileStore store, CmefRepository repository, ComputationJob job) {
	if (service == null) {
	    throw new IllegalArgumentException("CMEF Service must not be null.") ;
	}
	if (store == null) {
	    throw new IllegalArgumentException("File store must not be null.") ; 
	}
	if (repository == null) {
	    throw new IllegalArgumentException("CMEF Repository must not be null.") ;
	}
	if (job == null || job.getId() == null || "".equals(job.getId())) {
	    throw new IllegalArgumentException("Job must not be null and must have a valid Id.") ;
	}
	this.service = service ;
	this.store = store ;
	this.repository = repository ;
	this.job = BeanCopyUtils.copyJob(job) ;
    }
    
    public ComputationJob call() throws Exception {
	if (called) {
	    throw new IllegalStateException("Task has already been executed.") ;
	}
	called = true ;
	ComputationJob result = job ;
	JobState state = service.checkJobState(result) ;
        switch (state) {
            case RUNNING:
        	result = service.checkStatus(result) ;
        	repository.updateJob(result) ;
        	break ;
            case FINISHED:
        	result = service.getJobResult(result, store) ;
        	repository.updateJob(result) ;
        	service.deleteJob(result) ;
        	break ;
            case NOT_SUBMITTED:
        	break ;
        }
        return result ;
    }
}
