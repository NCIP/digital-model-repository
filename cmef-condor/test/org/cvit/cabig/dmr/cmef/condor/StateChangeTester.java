package org.cvit.cabig.dmr.cmef.condor;
import org.cvit.cabig.dmr.cmef.CmefServiceException;
import org.cvit.cabig.dmr.cmef.CmefService.JobState;
import org.cvit.cabig.dmr.cmef.condor.CondorService;
import org.cvit.cabig.dmr.cmef.condor.StateListener;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;
import org.cvit.cabig.dmr.cmef.monitoring.JobListener ;


public class StateChangeTester {
	public static void main(String[] args) throws CmefServiceException {
		CondorService service = new CondorService();
		ComputationalModel model = TestCaseDefaults.getWindowsExecutableModel();
		ComputationJob job = TestCaseDefaults.getWindowsExecutableJob();
		job.setModel(model);
		
		StateListener stList = new StateListener(new CondorService());
		
		job = service.startJob(model, job);	
		while (true) {
			stList.checkForUpdate(job, new List());
		}	
	}
}

class List implements JobListener {

	@Override
	public void statusChanged(ComputationJob job, JobState state) {
		System.err.println("State changed to " + state);
		
	}
	
}
