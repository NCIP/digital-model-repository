/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server;

import org.cvit.cabig.dmr.cmef.CmefService.JobState ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.json.JSONException ;
import org.json.JSONObject ;
import org.restlet.data.CacheDirective ;
import org.restlet.data.Status ;
import org.restlet.ext.json.JsonRepresentation ;
import org.restlet.resource.Get ;
import org.restlet.resource.ResourceException ;

public class JobStatusResource extends CmefServerResource {
    private String jobName ;
    private ComputationJob job ;
    private JobState jobState ;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit() ;
        
        this.jobName = (String) getRequestAttributes().get(CmefServer.JOB_VAR) ;
        this.job = loadJob(jobName) ;
        this.jobState = checkJobState(job) ;
        setExisting(job != null) ;
        
        //TODO: better cache control
        //Once job is completed response may be cached for some extended period of time (say 1yr...)
        getResponse().getCacheDirectives().add(CacheDirective.noCache()) ;
    }
    
    @Get("json")
    public JsonRepresentation checkStatus() {
	JSONObject result = new JSONObject() ;
	try {
	    result.put("id", job.getId()) ;
	    result.put("status", job.getJobStatus()) ;
	    result.put("state", jobState.toString()) ;
	} catch (JSONException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception creating JSON representation of job status.", e) ;
	}
	return new JsonRepresentation(result) ;
    }
}
