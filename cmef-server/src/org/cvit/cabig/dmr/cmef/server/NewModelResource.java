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
