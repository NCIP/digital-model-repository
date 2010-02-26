package org.cvit.cabig.dmr.cmef.server;

public class CvitIdResolver implements IdResolver {
    private String cvitIdPrefix ;
    private String cmefIdPrefix ;

    public CvitIdResolver(String cvitIdPrefix, String cmefIdPrefix) {
	if (cvitIdPrefix == null || "".equals(cvitIdPrefix)) {
	    throw new IllegalArgumentException("Must specify a valid CViT id prefix.") ;
	}
	if (cmefIdPrefix == null || "".equals(cmefIdPrefix)) {
	    throw new IllegalArgumentException("Must specify a valid CMEF id prefix.") ;
	}
	this.cvitIdPrefix = cvitIdPrefix ;
	this.cmefIdPrefix = cmefIdPrefix ;
    }
    
    public String getEntryId(String entryName) {
	return getId(entryName, cvitIdPrefix) ;
    }

    public String getEntryName(String entryId) {
	return getName(entryId, cvitIdPrefix) ;
    }

    public String getJobId(String jobName) {
	return getId(jobName, cmefIdPrefix) ;
    }

    public String getJobName(String jobId) {
	return getName(jobId, cmefIdPrefix) ;
    }

    public String getModelId(String modelName) {
	return getId(modelName, cmefIdPrefix) ;
    }

    public String getModelName(String modelId) {
	return getName(modelId, cmefIdPrefix) ;
    }

    private String getId(String localName, String prefix) {
	return prefix + localName ;
    }
    
    private String getName(String id, String prefix) {
	return id.substring(prefix.length()) ;
    }
}
