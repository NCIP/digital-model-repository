package org.cvit.cabig.dmr.cmef;

import org.cvit.cabig.dmr.cmef.monitoring.JobMonitoringService ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;

public interface CmefProvider {

    CmefRepository provideRepository(UserCredentials credentials) throws ProviderException ;
    
    CmefService provideService(UserCredentials credentials) throws ProviderException ;
    
    FileStore provideFileStore(UserCredentials credentials) throws ProviderException ;
    
    JobMonitoringService provideMonitoringService(UserCredentials credentials) throws ProviderException ;
}
