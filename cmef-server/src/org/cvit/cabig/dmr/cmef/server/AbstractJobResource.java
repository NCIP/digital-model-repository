/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */

package org.cvit.cabig.dmr.cmef.server ;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.restlet.data.CharacterSet ;
import org.restlet.data.MediaType ;
import org.restlet.ext.freemarker.TemplateRepresentation ;
import org.restlet.representation.Representation ;
import org.restlet.resource.ResourceException ;

import freemarker.template.Configuration ;
import freemarker.template.SimpleHash ;

public abstract class AbstractJobResource extends CmefServerResource {
    protected String entryName ;
    protected String modelName ;
    protected String jobName ;
    protected ComputationalModel model ;
    protected ComputationJob job ;

    public AbstractJobResource() {
	super() ;
    }

    @Override
    protected void doInit() throws ResourceException {
	super.doInit() ;
	
        this.entryName = (String) getRequestAttributes().get(CmefServer.ENTRY_VAR) ;
        this.modelName = (String) getRequestAttributes().get(CmefServer.MODEL_VAR) ;
        this.jobName = (String) getRequestAttributes().get(CmefServer.JOB_VAR) ;
        
        this.model = loadModel(modelName) ; //TODO: this just needs to be model summary...
        if (model == null) {
            setExisting(false) ;
            return ;
        }
        this.job = loadJob(jobName) ;
        setExisting(job != null) ;
    }

    protected Representation buildHtmlJob() {
        SimpleHash root = new SimpleHash() ;
        root.put("entryName", entryName) ;
        root.put("modelName", modelName) ;
        root.put("jobName", jobName) ;
        root.put("model", model) ;
        root.put("job", job) ;
        root.put("isRunning", isRunning(job)) ;
        root.put("funcs", new TemplateFunctions()) ;
        
        Configuration config = getFreeMarkerConfig() ;
        Representation result = new TemplateRepresentation("job.html", config, root, MediaType.TEXT_HTML) ;
        result.setCharacterSet(CharacterSet.UTF_8) ;
        return result ;
    }

}
