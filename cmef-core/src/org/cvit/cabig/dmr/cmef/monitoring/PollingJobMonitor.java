package org.cvit.cabig.dmr.cmef.monitoring;

import java.util.concurrent.Executors ;
import java.util.concurrent.ScheduledExecutorService ;
import java.util.concurrent.ScheduledFuture ;
import java.util.concurrent.TimeUnit ;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;

public class PollingJobMonitor implements JobMonitoringService {
    private final JobStatusUpdater statusUpdater ;
    private final int pollRate ;
    private final TimeUnit timeUnit ;
    
    private Object execLock = new Object() ;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1) ;
    
    private Object pollerLock = new Object() ;
    private ScheduledFuture<?> poller ;
    
    public PollingJobMonitor(JobStatusUpdater statusUpdater, int pollRate, TimeUnit timeUnit) {
	if (statusUpdater == null) {
	    throw new IllegalArgumentException("Status updated must not be null.") ;
	}
	if (pollRate < 1) {
	    throw new IllegalArgumentException("Poll rate must be greater than zero.") ;
	}
	if (timeUnit == null) {
	    throw new IllegalArgumentException("Time unit must not be null.") ;
	}
	this.statusUpdater = statusUpdater ;
	this.pollRate = pollRate ;
	this.timeUnit = timeUnit ;
    }
    
    @Override
    public void registerJobListener(JobListener listener, ComputationJob job) {
	statusUpdater.registerJobListener(listener, job) ;
    }
    
    @Override
    public void unregisterJobListener(JobListener listener) {
	statusUpdater.unregisterJobListener(listener) ;
    }
    
    @Override
    public boolean isStarted() {
	synchronized(pollerLock) {
	    return poller != null ;
	}
    }
    
    @Override
    public void start() {
	synchronized(pollerLock) {
	    if (isStarted()) {
		throw new IllegalStateException("Job Monitor has already been started.") ;
	    } else {
		synchronized(execLock) {
		    if (!isShutdown()) {
			poller = executor.scheduleWithFixedDelay(statusUpdater, 0, pollRate, timeUnit) ;
		    } else {
			throw new IllegalStateException("Executor has already been shutdown.") ;
		    }
		}
	    }
	}
    }
    
    @Override
    public boolean isShutdown() {
	synchronized (execLock) {
	    return executor.isShutdown() ;
	}
    }
    
    @Override
    public void shutdown() {
	synchronized (execLock) {
	    executor.shutdown() ;
	}
    }
}
