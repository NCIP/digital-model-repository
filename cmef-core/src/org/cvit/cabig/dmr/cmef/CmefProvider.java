/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef;

import org.cvit.cabig.dmr.cmef.monitoring.JobMonitoringService ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;

public interface CmefProvider {

    CmefRepository provideRepository(UserCredentials credentials) throws ProviderException ;
    
    CmefService provideService(UserCredentials credentials) throws ProviderException ;
    
    FileStore provideFileStore(UserCredentials credentials) throws ProviderException ;
    
    JobMonitoringService provideMonitoringService(UserCredentials credentials) throws ProviderException ;
}
