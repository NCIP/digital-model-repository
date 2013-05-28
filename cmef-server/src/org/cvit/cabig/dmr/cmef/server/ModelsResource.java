/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


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
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.json.JSONArray ;
import org.json.JSONException ;
import org.json.JSONObject ;
import org.restlet.data.MediaType ;
import org.restlet.data.Reference ;
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

public class ModelsResource extends CmefServerResource {
    private String entryName ;
    
    @Override
    protected void doInit() throws ResourceException {
        super.doInit() ;
        this.entryName = (String) getRequestAttributes().get(CmefServer.ENTRY_VAR) ;
    }
    
    @Get("json")
    public Representation listModelsJson() {
	Iterator<ComputationalModel> models = listModels() ;
	
	JSONArray result = new JSONArray() ;
	while (models.hasNext()) {
	    ComputationalModel model = models.next() ;
	    JSONObject jsonModel = jsonModel(model) ;
	    String href = getNamespace().modelRef(entryName, getResolver().getModelName(model.getId()), true).toString() ;
	    String jobsRef = getNamespace().jobsRef(entryName, getResolver().getModelName(model.getId()), true).toString() ;
	    try {
		jsonModel.putOpt("href", href) ;
		jsonModel.putOpt("jobs", jobsRef) ;
	    } catch (JSONException e) {
		throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception adding references to JSON: " + href + ".", e) ;
	    }
	    result.put(jsonModel) ;
	}
	setStatus(Status.SUCCESS_OK) ;
	return new JsonRepresentation(result) ;	
    }
    
    @Get("htm|html")
    public Representation listModelsHtml() {
	Iterator<ComputationalModel> models = listModels() ;
	
	List<ComputationalModel> modelList = new ArrayList<ComputationalModel>() ;
	while (models.hasNext()) {
	    modelList.add(models.next()) ;
	}
	
	SimpleHash root = new SimpleHash() ;
	root.put("entryName", entryName) ;
	root.put("models", modelList) ;
	root.put("funcs", new TemplateFunctions()) ;
	
	Configuration config = new Configuration() ;
	try {
	    Template template = new Template("models.html", new StringReader(HTML_FTL), config) ;
	    Writer out = new StringWriter() ;
	    template.process(root, out) ;
	    setStatus(Status.SUCCESS_OK) ;
	    return new StringRepresentation(out.toString(), MediaType.TEXT_HTML) ;
	} catch (IOException e) {
	    setStatus(Status.SERVER_ERROR_INTERNAL) ;
	    return new StringRepresentation("Exception reading template: " + e.getMessage()) ;
	} catch (TemplateException e) {
	    setStatus(Status.SERVER_ERROR_INTERNAL) ;
	    return new StringRepresentation("Template error: " + e.getMessage()) ;
	}
    }
    
    @Post
    public Representation post(Representation rep) {
	throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, "Request format not supported, must be one of: " + MediaType.APPLICATION_JSON + ", " + MediaType.MULTIPART_FORM_DATA + ".") ;
    }
    
    @Post("json")
    public Representation newModel(Representation modelRep) {
	try {
	    JsonRepresentation jsonRep = new JsonRepresentation(modelRep) ;
	    JSONObject json = jsonRep.toJsonObject() ;
	    
	    ComputationalModel addedModel = addModel(modelFromJson(json)) ;
	    JSONObject addedJson = jsonModel(addedModel) ;
	    
	    Representation result = new JsonRepresentation(addedJson) ;
	    setStatus(Status.SUCCESS_CREATED) ;
	    Reference modelRef = getNamespace().modelRef(entryName, getResolver().getModelName(addedModel.getId()), false) ;
	    result.setIdentifier(modelRef) ;
	    return result ;
	} catch (IOException e) {
	    setStatus(Status.SERVER_ERROR_INTERNAL) ;
	    return new StringRepresentation("Exception reading input: " + e.getMessage()) ;
	} catch (JSONException e) {
	    setStatus(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY) ;
	    return new StringRepresentation("Exception parsing JSON object: " + e.getMessage()) ;
	}
    }
    
    private Iterator<ComputationalModel> listModels() {
	try {
	    return getRepository().listModelsForEntry(entryRef(getResolver().getEntryId(entryName))) ;
	} catch (RepositoryException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception retrieving models from CMEF repository: " + e.getMessage(), e) ;
	} catch (AuthorizationException e) {
	    throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "User " + getCurrentUser() + " is not authorized to view models for: " + entryRef(getResolver().getEntryId(entryName)) + ".", e) ;
	}
    }
    
    private ComputationalModel addModel(ComputationalModel model) {
	try {
	    return getRepository().addModelToEntry(model, entryRef(getResolver().getEntryId(entryName))) ;
	} catch (RepositoryException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception adding model to CMEF repository: " + e.getMessage(), e) ;
	} catch (AuthorizationException e) {
	    throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "User " + getCurrentUser() + " is not authorized to add a model.", e) ;

	}
    }
    
    private static final String HTML_FTL = 
	"<html>" +
		"<head><title>${entryName} Models</title></head>" +
		"<body>" +
			"<table class=\"models\">" +
				"<tr>" +
					"<th>Title</th>" +
					"<th>Description</th>" +
					"<th>Creator</th>" +
					"<th>Date</th>" +
				"</tr>" +
				"<#list models as m>" +
					"<tr>" +
						"<td><a href=\"${funcs.modelUrl(entryName, funcs.modelName(m.id))}\">${m.title}</a></td>" +
						"<td>${m.description}</td>" +
						"<td>creator???</td>" +
						"<td>creation date???</td>" +
					"</tr>" +
				"</#list>" +
			"</table>" +
			"<div class=\"links\">" +
				"<a href=\"${funcs.modelsUrl(entryName)}/new\">New Model</a>" +
			"</div>" +
		"</body>" +
	"</html>";
}
