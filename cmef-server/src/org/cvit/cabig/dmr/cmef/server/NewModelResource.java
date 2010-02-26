package org.cvit.cabig.dmr.cmef.server;

import java.io.IOException ;
import java.io.InputStream ;
import java.net.URI ;
import java.net.URISyntaxException ;
import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;

import org.apache.commons.fileupload.FileItem ;
import org.apache.commons.fileupload.FileUploadException ;
import org.apache.commons.fileupload.disk.DiskFileItemFactory ;
import org.cvit.cabig.dmr.cmef.AuthorizationException ;
import org.cvit.cabig.dmr.cmef.RepositoryException ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform ;
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

public class NewModelResource extends CmefServerResource {
    private String entryName ;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit() ;
        
        this.entryName = (String) getRequestAttributes().get(CmefServer.ENTRY_VAR) ;
    }
    
    @Get("htm|html")
    public Representation getNewModelForm() {
	SimpleHash root = new SimpleHash() ;
	root.put("entryName", entryName) ;
	root.put("funcs", new TemplateFunctions()) ;
	
	return new TemplateRepresentation("newModel.html", getFreeMarkerConfig(), root, MediaType.TEXT_HTML) ;
    }
    
    @Post("multi")
    public Representation newModelFromForm(Representation formRep) {
	//TODO: rework to use FileUpload Streaming API...
	DiskFileItemFactory factory = new DiskFileItemFactory() ;
	RestletFileUpload upload = new RestletFileUpload(factory) ;
	List<FileItem> items ;
	try {
	    items = upload.parseRepresentation(formRep) ;
	} catch (FileUploadException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception parsing multipart form: " + e.getMessage(), e) ;
	}
	ComputationalModel model = parseModel(items) ;
	
	model = addModel(model) ;
	
	FileItem iframeItem = items.get(items.size() - 1) ;
	if (iframeItem.getString().equals("true")) {
	    return new StringRepresentation(buildIframeResponse(model), MediaType.TEXT_HTML) ;
	} else {
	    Reference modelRef = getNamespace().modelRef(entryName, getResolver().getModelName(model.getId()), true) ;
	    redirectSeeOther(modelRef) ;
	    return new StringRepresentation("Model created, URL: " + modelRef.toString() + ".") ;
	}
    }
    
    private ComputationalModel parseModel(List<FileItem> items) {
	int idx = 0 ;
	//TODO: refactor...cleanup...
	//TODO: verify values...
	ComputationalModel result = new ComputationalModel() ;
	result.setTitle(items.get(idx++).getString()) ;
	result.setVersion(items.get(idx++).getString()) ;
	result.setDescription(items.get(idx++).getString()) ;
	result.setComment(items.get(idx++).getString()) ;
	if (!items.get(idx).isFormField()) {
	    result.setDocument(storeFile(items.get(idx++))) ;
	} else {
	    String url = items.get(idx++).getString() ;
	    if (url != null) {
		File file = new File() ;
		//TODO: Should probably just get the local name
		file.setName(parseUri(url).getPath()) ;
		file.setSource(url) ;
		result.setDocument(file) ;
	    }
	}
	ProgrammingPlatform prog = new ProgrammingPlatform() ;
	prog.setLanguageType(items.get(idx++).getString()) ;
	prog.setLanguageVersion(items.get(idx++).getString()) ;
	result.setProgram(prog) ;
	ComputingPlatform comp = new ComputingPlatform() ;
	comp.setOperatingSystemType(items.get(idx++).getString()) ;
	comp.setProcessorArchitecture(items.get(idx++).getString()) ;
	result.setPlatforms(Collections.singleton(comp)) ;
	if (!items.get(idx).isFormField()) {
	    File file = storeFile(items.get(idx++)) ;
	    if (file != null) {
		result.setFiles(Collections.singleton(file)) ;
	    }
	} else {
	    String url = items.get(idx++).getString() ;
	    if (url != null) {
		File file = new File() ;
		//TODO: Should probably just get the local name
		file.setName(parseUri(url).getPath()) ;
		file.setSource(url) ;
		result.setFiles(Collections.singleton(file)) ;
	    }
	}
	List<Parameter> params = new ArrayList<Parameter>() ;
	boolean isParam = items.get(idx).getFieldName().startsWith("param") ;
	while (isParam) {
	    Parameter param = new Parameter() ;
	    param.setName(items.get(idx++).getString()) ;
	    param.setDescription(items.get(idx++).getString()) ;
	    param.setDataType(items.get(idx++).getString()) ;
	    if (param.getDataType().equals("file")) {
		param.setIsFile(Boolean.TRUE) ;
	    } else {
		param.setIsFile(Boolean.FALSE) ;
	    }
	    param.setPrefix(items.get(idx++).getString()) ;
	    List<String> choices = new ArrayList<String>() ;
	    boolean isChoice = items.get(idx).getFieldName().contains("choice") ;
	    while (isChoice) {
		choices.add(items.get(idx++).getString()) ;
		isChoice = items.get(idx).getFieldName().contains("choice") ;
	    }
	    param.setChoices(choices) ;
	    param.setDefaultValue(items.get(idx++).getString()) ;
	    if (items.get(idx).getFieldName().contains("opt")) {
		idx++ ;
		param.setIsOptional(Boolean.TRUE) ;
	    } else {
		param.setIsOptional(Boolean.FALSE) ;
	    }
	    params.add(param) ;
	    isParam = items.get(idx).getFieldName().startsWith("param") ;
	}
	result.setParameters(params) ;
	result.setCommandLine(items.get(idx++).getString()) ;
	return result ;
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
    
    private org.cvit.cabig.dmr.cmef.domain.File storeFile(FileItem item) {
	if (item.getName() == null || item.getName().equals("")) {
	    return null ;
	}
	InputStream is ;
	try {
	    is = item.getInputStream() ;
	} catch (IOException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception uploading files: " + e.getMessage(), e) ;
	}
	return storeFile(item.getName(), is) ;
    }
    
    private URI parseUri(String uri) {
	try {
	    return new URI(uri) ;
	} catch (URISyntaxException e) {
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid URL specified.", e) ;
	}
    }
    
    private String buildIframeResponse(ComputationalModel model) {
	StringBuilder response = new StringBuilder("<html><head><title>Model Created</title></head><body>") ;
	response.append("Added model: <span id=\"model-id\">").append(model.getId()).append("</span>") ;
	response.append("<script type=\"text/javascript\">if (window.parent.cmef) {") ;
	response.append("window.parent.cmef.addedModel('").append(model.getId()).append("');") ;
	response.append("}</script></body></html>") ;
	return response.toString() ;
    }
}
