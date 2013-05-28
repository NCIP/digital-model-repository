/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server ;

import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.restlet.data.CharacterSet ;
import org.restlet.data.MediaType ;
import org.restlet.ext.freemarker.TemplateRepresentation ;
import org.restlet.representation.Representation ;
import org.restlet.resource.ResourceException ;

import freemarker.template.SimpleHash ;

public abstract class AbstractModelResource extends CmefServerResource {
    protected String entryName ;
    protected String modelName ;
    protected ComputationalModel model ;

    public AbstractModelResource() {
	super() ;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit() ;
        
        this.entryName = (String) getRequestAttributes().get(CmefServer.ENTRY_VAR) ;
        this.modelName = (String) getRequestAttributes().get(CmefServer.MODEL_VAR) ;
        
        this.model = loadModel(modelName) ;
        setExisting(this.model != null) ;
    }

    protected Representation buildHtmlModelFragment() {
        SimpleHash root = getModelTemplateDataModel() ;
        return buildHtmlRepresentation("model.html", root) ;
    }

    protected Representation buildHtmlModel(SimpleHash htmlDataModel) {
        SimpleHash root = getModelTemplateDataModel() ;
        root.put("html", htmlDataModel) ;
        return buildHtmlRepresentation("skel.html", root) ;
    }

    private Representation buildHtmlRepresentation(String template, SimpleHash dataModel) {
        Representation result = new TemplateRepresentation(template, getFreeMarkerConfig(), dataModel, MediaType.TEXT_HTML) ;
        result.setCharacterSet(CharacterSet.UTF_8) ;
        return result ;
    }

    private SimpleHash getModelTemplateDataModel() {
        SimpleHash result = new SimpleHash() ;
        result.put("entryName", entryName) ;
        result.put("modelName", modelName) ;
        result.put("model", model) ;
        result.put("funcs", new TemplateFunctions()) ;
        return result ;
    }

}
