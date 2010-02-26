package org.cvit.cabig.dmr.cmef.server;

import java.io.InputStream ;
import java.util.ArrayList ;
import java.util.List ;

import org.apache.commons.fileupload.FileItemIterator ;
import org.apache.commons.fileupload.FileItemStream ;
import org.apache.commons.fileupload.util.Streams ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.restlet.data.MediaType ;
import org.restlet.data.Reference ;
import org.restlet.data.Status ;
import org.restlet.ext.fileupload.RestletFileUpload ;
import org.restlet.ext.freemarker.TemplateRepresentation ;
import org.restlet.representation.Representation ;
import org.restlet.representation.StringRepresentation ;
import org.restlet.resource.Get ;
import org.restlet.resource.Post ;
import org.restlet.resource.ResourceException ;

import freemarker.template.SimpleHash ;

public class SubmitJobResource extends AbstractJobsResource {

    @Get("html|htm")
    public Representation getSubmitJobForm() {
	SimpleHash root = new SimpleHash() ;
	root.put("entryName", entryName) ;
	root.put("modelName", modelName) ;
	root.put("model", model) ;
	root.put("funcs", new TemplateFunctions()) ;
	
	return new TemplateRepresentation("submitJob.html", getFreeMarkerConfig(), root, MediaType.TEXT_HTML) ;
    }
    
    private enum State { TITLE, DESC, COMMENTS, PARAMS} ;
    @Post("multi")
    public Representation submitJob(Representation formRep) {
	RestletFileUpload upload = new RestletFileUpload() ;
	ComputationJob job = new ComputationJob() ;
	boolean inIframe = false ;
	try {
	    FileItemIterator items = upload.getItemIterator(formRep) ;
	    List<ParameterValue> values = new ArrayList<ParameterValue>() ;
	    job.setParameterValues(values) ;
	    State state = State.TITLE ;
	    while (items.hasNext()) {
		FileItemStream item = items.next() ;
        	InputStream itemStream = item.openStream() ;
        	switch (state) {
        	    case TITLE:
        		job.setTitle(Streams.asString(itemStream)) ;
        		state = State.DESC ;
        		break ;
        	    case DESC:
        		job.setDescription(Streams.asString(itemStream)) ;
        		state = State.COMMENTS ;
        		break ;
        	    case COMMENTS:
        		job.setComment(Streams.asString(itemStream)) ;
        		state = State.PARAMS ;
        		break ;
        	    case PARAMS:
        		if (item.getFieldName().equals("iframe")) {
        		    inIframe = Boolean.parseBoolean(Streams.asString(itemStream)) ;
        		} else {
        		    Parameter param = new Parameter() ;
        		    param.setName(parseParamName(item.getFieldName())) ;
        		    ParameterValue value = new ParameterValue() ;
        		    if (item.isFormField()) {
        			value.setValue(Streams.asString(itemStream)) ;
        		    } else {
        			value.setValue(storeFile(item.getName(), itemStream).getSource()) ;
        		    }
        		    value.setJob(job) ;
        		    value.setParameter(param) ;
        		    param.setValue(value) ;
        		    values.add(value) ;
        		}
        		break ;
        	}
	    }
	} catch (Exception e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception processing submit job form: " + e.getMessage(), e) ;
	}
	
	job = addNewJob(job) ;
	ComputationJob startedJob = startJob(job) ;
	
	if (inIframe) {
	    return new StringRepresentation(buildIframeResponse(job), MediaType.TEXT_HTML) ;
	} else {
	    Reference jobRef = getNamespace().jobRef(entryName, modelName,getResolver().getJobName(startedJob.getId()), true) ;
	    redirectSeeOther(jobRef) ;
	    return new StringRepresentation("Job submitted, URL: " + jobRef.toString() + ".") ;
	}
    }
    
    private String parseParamName(String fieldName) {
	return fieldName.substring(0, fieldName.indexOf('[')) ;
    }
    
    private String buildIframeResponse(ComputationJob job) {
	StringBuilder response = new StringBuilder("<html><head><title>Job Submitted</title></head><body>") ;
	response.append("Submitted job: <span id=\"job-id\">").append(job.getId()).append("</span>") ;
	response.append("<script type=\"text/javascript\">if (window.parent.cmef) {") ;
	response.append("window.parent.cmef.addedJob('").append(job.getId()).append("');") ;
	response.append("}</script></body></html>") ;
	return response.toString() ;
    }
}
