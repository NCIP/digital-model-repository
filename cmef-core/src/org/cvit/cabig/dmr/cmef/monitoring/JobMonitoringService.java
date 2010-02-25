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
