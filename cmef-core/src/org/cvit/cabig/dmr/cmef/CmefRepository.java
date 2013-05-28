/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef ;

import java.util.Iterator ;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.domain.Entry ;

/**
 * Repository for storing CMEF objects.
 * Aggregates...
 * 
 * Thread safety...
 * 
 * @author rbradley
 *
 */
public interface CmefRepository {
    
    /**
     * Returns all {@link ComputationalModel}s associated with passed in entry. Only the id attribute of 
     * passed in entry needs to be provided. The returned {@link ComputationalModel}s do not 
     * include references to associated objects.
     * @param entry {@link Entry} to retrieve models for
     * @return {@link Iterator} over associated {@link ComputationalModel}s
     * @throws AuthorizationException 
     */
    public Iterator<ComputationalModel> listModelsForEntry(Entry entry) throws RepositoryException, AuthorizationException ;
    
    /**
     * Get the complete {@link ComputationalModel}, including all associated objects, using the id attribute of the passed in model.
     * @param model
     * @return
     * @throws AuthorizationException 
     */
    public ComputationalModel getModel(ComputationalModel model) throws RepositoryException, AuthorizationException ;
    
    public ComputationalModel addModelToEntry(ComputationalModel model, Entry entry) throws RepositoryException, AuthorizationException ;

    public ComputationalModel updateModel(ComputationalModel model) throws RepositoryException, AuthorizationException ;
    
    public Iterator<ComputationJob> listJobsForModel(ComputationalModel model) throws RepositoryException, AuthorizationException ;
    
    public ComputationJob addJobToModel(ComputationJob job, ComputationalModel model) throws RepositoryException, AuthorizationException ;
    
    public ComputationJob getJob(ComputationJob job) throws RepositoryException, AuthorizationException ;
    
    public ComputationJob updateJob(ComputationJob job) throws RepositoryException, AuthorizationException ;
}
