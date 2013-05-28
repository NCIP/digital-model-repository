/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server.providers;

import org.cvit.cabig.dmr.cmef.CmefProvider ;
import org.cvit.cabig.dmr.cmef.CmefRepository ;
import org.cvit.cabig.dmr.cmef.CmefService ;
import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.cvit.cabig.dmr.cmef.monitoring.JobMonitoringService ;
import org.cvit.cabig.dmr.cmef.util.Provider ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;

public class DelegatingCmefProvider implements CmefProvider {
    private final Provider<CmefService> serviceProvider ;
    private final Provider<CmefRepository> repoProvider ;
    private final Provider<FileStore> storeProvider ;
    private final Provider<JobMonitoringService> monitoringProvider ;

    public DelegatingCmefProvider(
                                  Provider<CmefService> serviceProvider, 
                                  Provider<CmefRepository> repoProvider, 
                                  Provider<FileStore> storeProvider,
                                  Provider<JobMonitoringService> monitorProvider) {
	if (serviceProvider == null) {
	    throw new IllegalArgumentException("Service Provider must not be null.") ;
	}
	if (repoProvider == null) {
	    throw new IllegalArgumentException("Repository Provider must not be null.") ;
	}
	if (storeProvider == null) {
	    throw new IllegalArgumentException("File Store Provider must not be null.") ;
	}
	if (monitorProvider == null) {
	    throw new IllegalArgumentException("Job Monitoring Service Provider must not be null.") ;
	}
	this.serviceProvider = serviceProvider ;
	this.repoProvider = repoProvider ;
	this.storeProvider = storeProvider ;
	this.monitoringProvider = monitorProvider ;
    }
    
    public CmefService provideService(UserCredentials credentials) throws ProviderException {
	return serviceProvider.provide(credentials) ;
    }
    
    public CmefRepository provideRepository(UserCredentials credentials) throws ProviderException {
	return repoProvider.provide(credentials) ;
    }
    
    public FileStore provideFileStore(UserCredentials credentials) throws ProviderException {
	return storeProvider.provide(credentials) ;
    }

    public JobMonitoringService provideMonitoringService(UserCredentials credentials) throws ProviderException {
        return monitoringProvider.provide(credentials) ;
    }
}
