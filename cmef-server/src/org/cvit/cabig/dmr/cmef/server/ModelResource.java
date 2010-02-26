package org.cvit.cabig.dmr.cmef.server;

import org.restlet.data.Status ;
import org.restlet.ext.json.JsonRepresentation ;
import org.restlet.representation.Representation ;
import org.restlet.resource.Get ;

import freemarker.template.SimpleHash ;

public class ModelResource extends AbstractModelResource {
    @Get("json")
    public Representation getJson() {
	setStatus(Status.SUCCESS_OK) ;
	return new JsonRepresentation(jsonModel(model)) ;
    }
    
    @Get("htm|html")
    public Representation getHtml() {
	SimpleHash dataModel = new SimpleHash() ;
	dataModel.put("pageTitle", "Model: " + model.getTitle()) ;
	dataModel.put("bodyTemplate", "model.html") ;
	return buildHtmlModel(dataModel) ;
    }
}
