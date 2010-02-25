package org.cvit.cabig.dmr.cmef;

import java.util.List;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;

public interface CmefService {
	public enum JobState {NOT_SUBMITTED, RUNNING, FINISHED};
	public ComputationJob startJob(ComputationalModel model, ComputationJob job) throws CmefServiceException;
	public ComputationJob stopJob(ComputationJob job) throws CmefServiceException;
	public List<ComputationJob> listJobs() throws CmefServiceException;
	public void deleteJob(ComputationJob job) throws CmefServiceException;
	public ComputationJob getJobResult(ComputationJob job, FileStore store) throws CmefServiceException;
	public List<ComputationJob> listJobResults() throws CmefServiceException;
	public ComputationJob checkStatus(ComputationJob job) throws CmefServiceException;
	public JobState checkJobState(ComputationJob job) throws CmefServiceException;
}
