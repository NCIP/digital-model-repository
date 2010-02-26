package org.cvit.cabig.dmr.cmef.server;

import java.io.IOException ;
import java.io.StringReader ;
import java.io.StringWriter ;
import java.io.Writer ;
import java.util.ArrayList ;
import java.util.Iterator ;
import java.util.List ;

import org.cvit.cabig.dmr.cmef.AuthorizationException ;
import org.cvit.cabig.dmr.cmef.RepositoryException ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.json.JSONArray ;
import org.json.JSONException ;
import org.json.JSONObject ;
import org.restlet.data.MediaType ;
import org.restlet.data.Status ;
import org.restlet.ext.json.JsonRepresentation ;
import org.restlet.representation.Representation ;
import org.restlet.representation.StringRepresentation ;
import org.restlet.resource.Get ;
import org.restlet.resource.Post ;
import org.restlet.resource.ResourceException ;

import freemarker.template.Configuration ;
import freemarker.template.SimpleHash ;
import freemarker.template.Template ;
import freemarker.template.TemplateException ;

public class JobsResource extends AbstractJobsResource {
    
    @Get("json")
    public Representation listJobsJson() {
	Iterator<ComputationJob> jobs = listJobs() ;
	
	JSONArray result = new JSONArray() ;
	while (jobs.hasNext()) {
	    ComputationJob job = jobs.next() ;
	    JSONObject jsonJob = jsonJob(job) ;
	    String href = getNamespace().jobRef(entryName, modelName, getResolver().getJobName(job.getId()), true).toString() ;
	    try {
		jsonJob.putOpt("href", href) ;
	    } catch (JSONException e) {
		throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception adding references to JSON: " + href + ".", e) ;
	    }
	    result.put(jsonJob) ;
	}
	setStatus(Status.SUCCESS_OK) ;
	return new JsonRepresentation(result) ;
    }
    
    @Get("htm|html")
    public Representation listJobsHtml() throws ResourceException {
	Iterator<ComputationJob> jobs = listJobs() ;
	
	List<ComputationJob> jobList = new ArrayList<ComputationJob>() ;
	while (jobs.hasNext()) {
	    jobList.add(jobs.next()) ;
	}
	
	SimpleHash root = new SimpleHash() ;
	root.put("entryName", entryName) ;
	root.put("modelName", modelName) ;
	root.put("model", model) ;
	root.put("jobs", jobList) ;
	root.put("funcs", new TemplateFunctions()) ;
	
	Configuration config = new Configuration() ;
	try {
	    Template template = new Template("jobs.html", new StringReader(HTML_FTL), config) ;
	    Writer out = new StringWriter() ;
	    template.process(root, out) ;
	    setStatus(Status.SUCCESS_OK) ;
	    return new StringRepresentation(out.toString(), MediaType.TEXT_HTML) ;
	} catch (IOException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception reading template.", e) ;
	} catch (TemplateException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Template error: " + e.getMessage(), e) ;
	}
    }
    
    @Post("json,json")
    public Representation newJob(JSONObject jobRep) {
	ComputationJob newJob = jobFromJson(jobRep) ;
	newJob = addNewJob(newJob) ;
	//TODO: don't start job when posted, use StartJob resource to start the job...SubmitJob resource will add job and auto start it...
	ComputationJob startedJob = startJob(newJob) ;
	
	JSONObject jsonResult = jsonJob(startedJob) ;
	Representation result = new JsonRepresentation(jsonResult) ;
	result.setIdentifier(getNamespace().jobRef(entryName, modelName, getResolver().getJobName(startedJob.getId()), false)) ;
	setStatus(Status.SUCCESS_CREATED) ;
	return result ;
    }
    
    private Iterator<ComputationJob> listJobs() {
	try {
	    return getRepository().listJobsForModel(modelRef(getResolver().getModelId(modelName))) ;
	} catch (RepositoryException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception retrieving models from CMEF repository.", e) ;
	} catch (AuthorizationException e) {
	    throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "User " + getCurrentUser() + " is not authorized to access jobs for model: " + modelRef(getResolver().getModelId(modelName)) + ".", e) ;
	}
    }
    
    private static final String HTML_FTL = 
	"<html>" +
		"<head><title>${entryName} ${model.title} Jobs</title></head>" +
		"<body>" +
			"<table id=\"jobs\">" +
				"<tr>" +
					"<th>Title</th>" +
					"<th>Model</th>" +
					"<th>Creator</th>" +
					"<th>Status</th>" +
					"<th>Date</th>" +
				"</tr>" +
				"<#list jobs as j>" +
					"<tr>" +
						"<td><a href=\"${funcs.jobUrl(entryName, modelName, funcs.jobName(j.id))}\">${j.title}</a></td>" +
						"<td><a href=\"${funcs.modelUrl(entryName, modelName)}\">${model.title}</a></td>" +
						"<td>creator???</td>" +
						"<td>${j.jobStatus!}</td>" +
						"<td>creation date???</td>" +
					"</tr>" +
				"</#list>" +
			"</table>" +
			"<a href=\"${funcs.jobsUrl(entryName, modelName)}/submitJob\">Submit Job</a>" +
		"</body>" +
	"</html>";
}
