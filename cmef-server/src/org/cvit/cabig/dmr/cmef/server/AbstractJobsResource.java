/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server ;

import java.util.Collections ;
import java.util.HashMap ;
import java.util.Map ;

import org.cvit.cabig.dmr.cmef.AuthorizationException ;
import org.cvit.cabig.dmr.cmef.CmefServiceException ;
import org.cvit.cabig.dmr.cmef.RepositoryException ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.cvit.cabig.dmr.cmef.util.BeanCopyUtils ;
import org.genepattern.server.handler.GetJobStatusHandler ;
import org.restlet.data.Status ;
import org.restlet.resource.ResourceException ;

public abstract class AbstractJobsResource extends CmefServerResource {
    protected String entryName ;
    protected String modelName ;
    protected ComputationalModel model ;

    public AbstractJobsResource() {
	super() ;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit() ;
    
        this.entryName = (String) getRequestAttributes().get(CmefServer.ENTRY_VAR) ;
        this.modelName = (String) getRequestAttributes().get(CmefServer.MODEL_VAR) ;
        
        this.model = loadModel(modelName) ;
        setExisting(model != null) ;
    }

    protected ComputationJob addNewJob(ComputationJob job) {
        setParameters(job) ;
        return addJob(job) ;
    }

    protected ComputationJob startJob(ComputationJob job) {
        try {
            job = getCmefService().startJob(model, job) ;
            getMonitorService().registerJobListener(new JobSynchronizer(this), job) ;
        } catch (CmefServiceException e) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception starting job: " + e.getMessage(), e) ;
        }
        updateJob(job) ;
        return job ;
    }

    private void setParameters(ComputationJob job) {
        if (job.getParameterValues() == null) {
            return ;
        }
        Map<String, Parameter> paramMap = buildParameterMap() ;
        for (ParameterValue value : job.getParameterValues()) {
            String paramName = value.getParameter().getName() ;
            value.setParameter(paramMap.get(paramName)) ;
        }
    }

    private Map<String, Parameter> buildParameterMap() {
        if (model.getParameters() == null) {
            return Collections.emptyMap() ;
        }
        Map<String, Parameter> result = new HashMap<String, Parameter>() ;
        for (Parameter param : model.getParameters()) {
            result.put(param.getName(), BeanCopyUtils.shallowCopyParameter(param)) ;
        }
        return result ;
    }

    private ComputationJob addJob(ComputationJob job) {
        try {
            ComputationalModel model = modelRef(getResolver().getModelId(modelName)) ;
            model.setEntry(Collections.singleton(entryRef(getResolver().getEntryId(entryName)))) ;
            return getRepository().addJobToModel(job, model) ;
        } catch (RepositoryException e) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Exception adding job to CMEF repository: " + e.getMessage(), e) ;
        } catch (AuthorizationException e) {
	    throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "User " + getCurrentUser() + "is not authorized to add a job.", e) ;
	}
    }

}
