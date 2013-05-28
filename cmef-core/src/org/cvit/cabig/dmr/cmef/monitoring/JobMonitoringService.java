/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.monitoring;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;

public interface JobMonitoringService {
    
    boolean isStarted() ;
    
    void start() ;
    
    boolean isShutdown() ;
    
    void shutdown() ;
    
    void registerJobListener(JobListener listener, ComputationJob job) ;
    
    void unregisterJobListener(JobListener listener) ;
}
