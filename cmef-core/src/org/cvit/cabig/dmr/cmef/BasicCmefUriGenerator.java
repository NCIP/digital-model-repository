package org.cvit.cabig.dmr.cmef;

import java.util.concurrent.atomic.AtomicLong ;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform ;
import org.cvit.cabig.dmr.domain.Entry ;

public class BasicCmefUriGenerator implements CmefUriGenerator {
    private AtomicLong counter ;
    private String prefix ;

    public BasicCmefUriGenerator(String prefix) {
	this(prefix, 0) ;
    }
    
    public BasicCmefUriGenerator(String prefix, long initialValue) {
	if (prefix == null || "".equals(prefix)) {
	    throw new IllegalArgumentException("Must specify a valid URI prefix.") ;
	}
	this.prefix = prefix ;
	this.counter = new AtomicLong(initialValue) ;
    }

    public String generateModelUri(Entry entry, ComputationalModel model) {
	return newId() ;
    }

    public String generateDocumentUri(Entry entry, ComputationalModel model, File document) {
	return newId() ;
    }
    
    public String generateModelFileUri(Entry entry, ComputationalModel model, File file) {
	return newId() ;
    }
    
    public String generateParameterUri(Entry entry, ComputationalModel model, Parameter parameter) {
	return newId() ;
    }
    
    public String generatePlatformUri(Entry entry, ComputationalModel model, ComputingPlatform platform) {
	return newId() ;
    }
    
    public String generateProgramUri(Entry entry, ComputationalModel model, ProgrammingPlatform programmingPlatform) {
	return newId() ;
    }

    public String generateJobUri(ComputationalModel model, ComputationJob job) {
	return newId() ;
    }
    
    public String generateResultFileUri(ComputationalModel model, ComputationJob job, File file) {
	return newId() ;
    }
    
    public String generateParameterValueUri(ComputationalModel model, ComputationJob job, ParameterValue value) {
	return newId() ;
    }
    
    private String newId() {
	return prefix + counter.getAndIncrement() ;
    }
}
