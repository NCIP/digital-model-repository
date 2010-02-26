package org.cvit.cabig.dmr.cmef.server;

import java.util.Collections ;
import java.util.HashMap ;
import java.util.Map ;

import org.cvit.cabig.dmr.cmef.AuthenticationException ;
import org.cvit.cabig.dmr.cmef.CmefProvider ;
import org.cvit.cabig.dmr.cmef.CmefRepository ;
import org.cvit.cabig.dmr.cmef.CmefService ;
import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.cvit.cabig.dmr.cmef.monitoring.JobMonitoringService ;
import org.cvit.cabig.dmr.cmef.server.providers.AnzoRepositoryProvider ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;
import org.restlet.Application ;
import org.restlet.Restlet ;
import org.restlet.data.MediaType ;
import org.restlet.data.Reference ;
import org.restlet.data.Status ;
import org.restlet.resource.ResourceException ;
import org.restlet.routing.Router ;
import org.restlet.routing.Template ;

import freemarker.template.Configuration ;

public class CmefServer extends Application {
    private CmefProvider provider ;
    private Configuration configuration ;
    private IdResolver resolver ;

    public static final String ENTRY_VAR = "entryName" ;
    public static final String MODEL_VAR = "modelName" ;
    public static final String JOB_VAR = "jobName" ;
    private Template modelsTemplate = new Template(String.format("/{%s}/models", ENTRY_VAR)) ;
    private Template modelTemplate = new Template(String.format("/{%s}/models/{%s}", ENTRY_VAR, MODEL_VAR)) ;
    private Template jobsTemplate = new Template(String.format("/{%s}/models/{%s}/jobs", ENTRY_VAR, MODEL_VAR)) ;
    private Template jobTemplate = new Template(String.format("/{%s}/models/{%s}/jobs/{%s}", ENTRY_VAR, MODEL_VAR, JOB_VAR)) ;
    
    public CmefServer(CmefProvider provider, Configuration configuration, IdResolver resolver) {
	if (provider == null) {
	    throw new IllegalArgumentException("CMEF Provider must not be null.") ;
	}
	if (configuration == null) {
	    throw new IllegalArgumentException("FreeMarker Configuration must not be null.") ;
	}
	if (resolver == null) {
	    throw new IllegalArgumentException("ID Resovler must not be null.") ;
	}
	this.provider = provider ;
	this.resolver = resolver ;
	this.configuration = configuration ;
	getMetadataService().addExtension("multi", MediaType.MULTIPART_FORM_DATA) ;
    }
    
    @Override
    public synchronized Restlet createInboundRoot() {
	Router router = new Router(getContext()) ;
	router.attach(modelsTemplate.getPattern(), ModelsResource.class) ;
	router.attach(modelsTemplate.getPattern() + "/new", NewModelResource.class) ;
	router.attach(modelTemplate.getPattern(), ModelResource.class) ;
	
	router.attach(jobsTemplate.getPattern(), JobsResource.class) ;
	router.attach(jobsTemplate.getPattern() + "/submitJob", SubmitJobResource.class) ;
	router.attach(jobTemplate.getPattern(), JobResource.class) ;
	router.attach(jobTemplate.getPattern() + "/cancel", CancelJobResource.class) ;
	
	router.attach("/jobStatus/{" + JOB_VAR + "}", JobStatusResource.class) ;
	
	return router ;
    }
    
    public IdResolver getResolver() {
	return resolver ;
    }
    
    public Reference getRelativeModelsRef(String entryName) {
	Map<String, String> values = Collections.singletonMap(ENTRY_VAR, entryName) ;
	return new Reference(modelsTemplate.format(values)) ;
    }
    
    public Reference getRelativeModelRef(String entryName, String modelName) {
	Map<String, String> values = new HashMap<String, String>(2) ;
	values.put(ENTRY_VAR, entryName) ;
	values.put(MODEL_VAR, modelName) ;
	return new Reference(modelTemplate.format(values)) ;
    }
    
    public Reference getRelativeJobsRef(String entryName, String modelName) {
	Map<String, String> values = new HashMap<String, String>(2) ;
	values.put(ENTRY_VAR, entryName) ;
	values.put(MODEL_VAR, modelName) ;
	return new Reference(jobsTemplate.format(values)) ;
    }
    
    public Reference getRelativeJobRef(String entryName, String modelName, String jobName) {
	Map<String, String> values = new HashMap<String, String>(3) ;
	values.put(ENTRY_VAR, entryName) ;
	values.put(MODEL_VAR, modelName) ;
	values.put(JOB_VAR, jobName) ;
	return new Reference(jobTemplate.format(values)) ;
    }

    public CmefRepository getRepository(UserCredentials user) {
	try {
	    return provider.provideRepository(user) ;
	} catch (ProviderException e) {
	    if (e.getCause() instanceof AuthenticationException) {
		throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "User not authorized to access resources.", e.getCause()) ;
	    } else {
		throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unable to complete request.", e) ;
	    }
	}
    }
    
    public CmefService getCmefService() {
	try {
	    return provider.provideService(AnzoRepositoryProvider.DEFUALT_ADMIN) ;
	} catch (ProviderException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unable to complete request.", e) ;
	}
    }
    
    public FileStore getFileStore(UserCredentials user) {
	try {
	    return provider.provideFileStore(user) ;
	} catch (ProviderException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unable to complete request.", e) ;
	}
    }
    
    public JobMonitoringService getMonitorService() {
	try {
	    return provider.provideMonitoringService(AnzoRepositoryProvider.DEFUALT_ADMIN) ;
	} catch (ProviderException e) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unable to complete request.", e) ;
	}
    }
    
    public Configuration getFreeMarkerConfig() {
	return configuration ;
    }
}
