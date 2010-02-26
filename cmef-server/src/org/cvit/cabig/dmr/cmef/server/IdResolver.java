package org.cvit.cabig.dmr.cmef.server;

public interface IdResolver {

    public String getEntryId(String entryName) ;
    
    public String getModelId(String modelName) ;
    
    public String getJobId(String jobName) ;
    
    public String getEntryName(String entryId) ;
    
    public String getModelName(String modelId) ;
    
    public String getJobName(String jobId) ;
}
