/*******************************************************************************
 * caBIG™ Open Source Software License
 * Computational Model Execution Framework (CMEF) v1.0
 * 
 * Copyright Notice.
 * Copyright 2010 Massachusetts General Hospital (“caBIG™ Participant”).  Computational Model Execution Framework (CMEF) was created with NCI funding and is part of the caBIG™ initiative. The software subject to this notice and license includes both human readable source code form and machine readable, binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant and You.  “You (or “Your”) shall mean a person or an entity, and all other entities that control, are controlled by, or are under common control with the entity.  “Control” for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.  
 * 
 * License.  
 * Provided that You agree to the conditions described below, caBIG™ Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights in the caBIG™ Software, including any copyright or patent rights therein, to (i) use, install, disclose, access, operate, execute, reproduce, copy, modify, translate, market, publicly display, publicly perform, and prepare derivative works of the caBIG™ Software in any manner and for any purpose, and to have or permit others to do so; (ii) make, have made, use, practice, sell, and offer for sale, import, and/or otherwise dispose of caBIG™ Software (or portions thereof); (iii) distribute and have distributed to and by third parties the caBIG™ Software and any modifications and derivative works thereof; and (iv) sublicense the foregoing rights set out in (i), (ii) and (iii) to third parties, including the right to license such rights to further third parties.  For sake of clarity, and not by way of limitation, caBIG™ Participant shall have no right of accounting or right of payment from You or Your sublicensees for the rights granted under this License.  This License is granted at no charge to You.  Your downloading, copying, modifying, displaying, distributing or use of caBIG™ Software constitutes acceptance of all of the terms and conditions of this Agreement.  If you do not agree to such terms and conditions, you have no right to download, copy, modify, display, distribute or use the caBIG™ Software.  
 * 
 * 1.	Your redistributions of the source code for the caBIG™ Software must retain the above copyright notice, this list of conditions and the disclaimer and limitation of liability of Article 6 below.  Your redistributions in object code form must reproduce the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials provided with the distribution, if any.
 * 
 * 2.	Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: “This product includes software developed by Massachusetts General Hospital.”  If You do not include such end-user documentation, You shall include this acknowledgment in the caBIG™ Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3.	You may not use the names  ”Massachusetts General Hospital”, “MGH”, "INFOTECH Soft", “The National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™” to endorse or promote products derived from this caBIG™ Software.  This License does not authorize You to use any trademarks, service marks, trade names, logos or product names of either caBIG™ Participant, NCI or caBIG™, except as required to comply with the terms of this License.
 * 
 * 4.	For sake of clarity, and not by way of limitation, You may incorporate this caBIG™ Software into Your proprietary programs and into any third party proprietary programs.  However, if You incorporate the caBIG™ Software into third party proprietary programs, You agree that You are solely responsible for obtaining any permission from such third parties required to incorporate the caBIG™ Software into such third party proprietary programs and for informing Your sublicensees, including without limitation Your end-users, of their obligation to secure any required permissions from such third parties before incorporating the caBIG™ Software into such third party proprietary software programs.  In the event that You fail to obtain such permissions, You agree to indemnify caBIG™ Participant for any claims against caBIG™ Participant by such third parties, except to the extent prohibited by law, resulting from Your failure to obtain such permissions.
 * 
 * 5.	For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses of modifications of the caBIG™ Software, or any derivative works of the caBIG™ Software as a whole, provided Your use, reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.
 * 
 * 6.	THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED.  IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU)
 * Contributors: INFOTECH Soft, Inc.
 ******************************************************************************/
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
