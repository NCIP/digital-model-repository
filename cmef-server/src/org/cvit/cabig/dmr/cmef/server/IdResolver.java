/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server;

public interface IdResolver {

    public String getEntryId(String entryName) ;
    
    public String getModelId(String modelName) ;
    
    public String getJobId(String jobName) ;
    
    public String getEntryName(String entryId) ;
    
    public String getModelName(String modelId) ;
    
    public String getJobName(String jobId) ;
}
