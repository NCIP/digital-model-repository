/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server.providers;

import org.cvit.cabig.dmr.cmef.CmefService ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.cvit.cabig.dmr.cmef.genepattern.GenePatternService ;
import org.cvit.cabig.dmr.cmef.util.Provider ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;
import org.genepattern.client.GPClient ;
import org.genepattern.webservice.AnalysisWebServiceProxy ;
import org.genepattern.webservice.TaskIntegratorProxy ;
import org.genepattern.webservice.WebServiceException ;

public class GenePatternServiceProvider implements Provider<CmefService> {
    private GenePatternService service ;

    public GenePatternServiceProvider(UserCredentials gpUser, String gpUrl, String workingDir, boolean useGrid) {
	if (useGrid) {
	    //TODO: need to at least namespace property name (i.e. org.cvit.cmef.service.genepattern.usegrid)...
	    System.setProperty("grid", "true") ;
	} else {
	    System.setProperty("grid", "false") ;
	}
	try {
	    TaskIntegratorProxy taskIntegrator = new TaskIntegratorProxy(gpUrl, gpUser.getUsername(), gpUser.getPassword()) ;
	    AnalysisWebServiceProxy analysis = new AnalysisWebServiceProxy(gpUrl, gpUser.getUsername(), gpUser.getPassword());
	    GPClient gpClient = new GPClient(gpUrl, gpUser.getUsername());
	    service = new GenePatternService(taskIntegrator, analysis, gpClient, gpUser, workingDir);
	} catch (WebServiceException e) {
	    throw new RuntimeException("Exception initializing CMEF Service, see cause.", e) ;
	}
    }
    
    public CmefService provide(UserCredentials credentials) throws ProviderException {
	return service ;
    }
}
