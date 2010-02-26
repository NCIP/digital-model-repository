package org.cvit.cabig.dmr.cmef.server;


import org.restlet.data.Status ;
import org.restlet.ext.json.JsonRepresentation ;
import org.restlet.representation.Representation ;
import org.restlet.resource.Get ;


public class JobResource extends AbstractJobResource {
    
    @Get("json")
    public Representation getJson() {
	setStatus(Status.SUCCESS_OK) ;
	return new JsonRepresentation(jsonJob(job)) ;
    }
    
    @Get("htm|html")
    public Representation getHtml() {
	return buildHtmlJob() ;
    }
}
