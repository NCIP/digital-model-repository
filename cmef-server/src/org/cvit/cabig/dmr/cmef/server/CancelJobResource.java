package org.cvit.cabig.dmr.cmef.server;

import org.cvit.cabig.dmr.cmef.CmefServiceException ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.restlet.data.Form ;
import org.restlet.data.Status ;
import org.restlet.resource.Post ;
import org.restlet.resource.ResourceException ;

public class CancelJobResource extends AbstractJobResource {
    
    @Post("form")
    public void handleCommand(Form form) {
	if (form != null) {
	    String logMessage = form.getValues("log") ;
	    //TODO: log message...
	}
	cancelJob(job) ;
	//TODO: return 202 Accepted w/ representation stating "Canceling job..." and link to job page
	//can use HTTP-EQUIV meta tag to redirect client to job page after delay...
	redirectSeeOther(getNamespace().jobRef(entryName, modelName, jobName, true)) ;
    }
    
    private void cancelJob(ComputationJob job) {
	try {
	    job = getCmefService().stopJob(job) ;
	} catch (CmefServiceException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception canceling job: " + e.getMessage(), e) ;
	}
    }
}
