package org.cvit.cabig.dmr.cmef.condor;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.cvit.cabig.dmr.cmef.CmefService;
import org.cvit.cabig.dmr.cmef.CmefServiceException;
import org.cvit.cabig.dmr.cmef.SimpleFileStore;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;
import org.junit.Test;

public class CmefServiceTest extends TestCase{
	
	CmefService service;
	
	protected void setUp(){
		service = new CondorService();
	}

	protected void tearDown() {
		service = null;
	}

	@Test
	public void testStartJobWithRModel() throws CmefServiceException {
		printHeader("Executing Test 1: Starting R job");
		ComputationalModel model = TestCaseDefaults.getRDemoModel();
		ComputationJob job = TestCaseDefaults.getRDemoJob();
		job.setModel(model);
		job = service.startJob(model, job);
		printFooter("JobNumber: " + job.getJobNumber() + "\nJobStatus: " + job.getJobStatus() + 
						"\nDateSubmitted: " + job.getDateSubmitted());
		assertTrue(job.getJobNumber() != null && job.getJobStatus() != null && job.getDateSubmitted() != null);
	}

	@Test
	public void testStartJobWithCExecutableForWindows() throws CmefServiceException {
		printHeader("Executing Test 2: Starting C++ job for Windows");
		ComputationalModel model = TestCaseDefaults.getWindowsExecutableModel();
		ComputationJob job = TestCaseDefaults.getWindowsExecutableJob();
		job.setModel(model);
		job = service.startJob(model, job);
		printFooter("JobNumber: " + job.getJobNumber() + "\nJobStatus: " + job.getJobStatus() + 
				"\nDateSubmitted: " + job.getDateSubmitted());
		assertTrue(job.getJobNumber() != null && job.getJobStatus() != null && job.getDateSubmitted() != null);
	}

	@Test
	public void testStartJobWithCExecutableForWindows64() throws CmefServiceException {
		printHeader("Executing Test 3: Starting C++ job for Windows64");
		ComputationalModel model = TestCaseDefaults.getWindows64ExecutableModel();
		ComputationJob job = TestCaseDefaults.getWindowsExecutableJob();
		job.setModel(model);
		job = service.startJob(model, job);
		printFooter("JobNumber: " + job.getJobNumber() + "\nJobStatus: " + job.getJobStatus() + 
				"\nDateSubmitted: " + job.getDateSubmitted());
		assertTrue(job.getJobNumber() != null && job.getJobStatus() != null && job.getDateSubmitted() != null);
	}

	@Test
	public void testStartJobWithCExecutableForLinux() throws CmefServiceException {
		printHeader("Executing Test 4: Starting C++ job for Linux");
		ComputationalModel model = TestCaseDefaults.getLinuxExecutableModel();
		ComputationJob job = TestCaseDefaults.getLinuxExecutableJob();
		job.setModel(model);
		job = service.startJob(model, job);
		printFooter("JobNumber: " + job.getJobNumber() + "\nJobStatus: " + job.getJobStatus() + 
				"\nDateSubmitted: " + job.getDateSubmitted());
		assertTrue(job.getJobNumber() != null && job.getJobStatus() != null && job.getDateSubmitted() != null);
	}	

	@Test
	public void testStartJobWithJavaProgramForLinux() throws CmefServiceException {
		printHeader("Executing Test 5: Starting Java job for Linux");
		ComputationalModel model = TestCaseDefaults.getLinuxJavaModel();
		ComputationJob job = TestCaseDefaults.getLinuxJavaJob();
		job.setModel(model);
		job = service.startJob(model, job);
		printFooter("JobNumber: " + job.getJobNumber() + "\nJobStatus: " + job.getJobStatus() + 
				"\nDateSubmitted: " + job.getDateSubmitted());
		assertTrue(job.getJobNumber() != null && job.getJobStatus() != null && job.getDateSubmitted() != null);
	}	

	@Test
	public void testStartJobWithJavaProgramForWindows() throws CmefServiceException {
		printHeader("Executing Test 6: Starting Java job for Windows");
		ComputationalModel model = TestCaseDefaults.getWindowsJavaModelWithZip();
		ComputationJob job = TestCaseDefaults.getWindowsJavaJobWithZip();
		job.setModel(model);
		job = service.startJob(model, job);
		printFooter("JobNumber: " + job.getJobNumber() + "\nJobStatus: " + job.getJobStatus() + 
				"\nDateSubmitted: " + job.getDateSubmitted());
		assertTrue(job.getJobNumber() != null && job.getJobStatus() != null && job.getDateSubmitted() != null);
	}

	@Test
	public void testStopJob() {
		printHeader("Executing Test 7: Testing stopJob");
		ComputationalModel model = TestCaseDefaults.getStopTestDelayModel();
		ComputationJob job = TestCaseDefaults.getStopTestDelayJob();
		try {
			System.err.println("About to start job");
			job = service.startJob(model, job);
		
			System.err.println("Started Job " + job.getJobNumber());
			job = service.stopJob(job);
			printFooter("JobStatus: " + job.getJobStatus() + "\nDateCompleted: " + job.getDateCompleted());
			assertTrue(("Completed".equals(job.getJobStatus()) || "Removed".equals(job.getJobStatus())) && job.getDateCompleted() != null);
		} catch (CmefServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testListJobs() throws CmefServiceException  {
		printHeader("Executing Test 8: testing listJob");
		//Get List of Jobs already there
		List<ComputationJob> firstJobs = service.listJobs();
		
		//Add Job to job list
		ComputationalModel model = TestCaseDefaults.getRDemoModel();
		ComputationJob job = TestCaseDefaults.getRDemoJob();
		job = service.startJob(model, job);		

		//Get list of jobs with newly added job
		List<ComputationJob> secondJobs = service.listJobs();
		
		//Make sure first List does not contain new job,
		//but second list does
		boolean firstContains = false;
		boolean secondContains = false;
		if (firstJobs != null && secondJobs != null) {
			for (ComputationJob aJob : firstJobs){
				if (aJob.getJobNumber() == job.getJobNumber()) {
					firstContains = true;
					break;
				}
			}
			for (ComputationJob aJob : secondJobs){
				System.err.println("aJob: " + aJob.getJobNumber() + "\njob: " + job.getJobNumber());
				if ((int)aJob.getJobNumber() == (int)job.getJobNumber()) {
					System.err.println("In!");
					secondContains = true;
					break;
				}
			}	
		}
		printFooter("firstContains: " + firstContains + "\nsecondContains: " + secondContains);
		assertTrue(!firstContains && secondContains);
	}

	@Test
	public void testDeleteJob() {
		printHeader("Executing Test 9: testing deleteJob");
		try {
			ComputationalModel model = TestCaseDefaults.getDeleteTestSimpleModel();
			ComputationJob job = TestCaseDefaults.getDeleteTestSimpleJob();
			System.err.println("About to start job");
			job = service.startJob(model, job);
			while (!("Completed".equals(job.getJobStatus()) || "Removed".equals(job.getJobStatus()))) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				job = service.checkStatus(job);
			}
			
			List<ComputationJob> jobList = service.listJobs();
			boolean containsJobCurrently = false;
			for (ComputationJob aJob : jobList) {
				if (job.getJobNumber() == aJob.getJobNumber()) {
					containsJobCurrently = true;
					break;
				}
			}
			boolean containedJobOriginally = containsJobCurrently;
			
			ComputationJob jobToDelete = new ComputationJob();
			jobToDelete.setJobNumber(job.getJobNumber());
			service.deleteJob(jobToDelete);
			containsJobCurrently = false;
			jobList = service.listJobs();
			for (ComputationJob aJob : jobList) {
				if (jobToDelete.getJobNumber() == aJob.getJobNumber()) {
					containsJobCurrently = true;
					break;
				}
			}
			printFooter("containedJobOriginally: " + containedJobOriginally + "\ncontainsJobCurrently: " + containsJobCurrently);
			assert(containedJobOriginally && !containsJobCurrently);			
		} catch (CmefServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetJobResult() throws CmefServiceException, URISyntaxException  {
		printHeader("Executing Test 10: testing getJobResult");
		ComputationalModel model = TestCaseDefaults.getRDemoModel();
		ComputationJob job = TestCaseDefaults.getRDemoJob();
		job = service.startJob(model, job);
		
		boolean hasFilesBeforeGetResults = (job.getResultFiles() != null && !job.getResultFiles().isEmpty());
		
		String status = service.checkStatus(job).getJobStatus();
		while (!("Completed".equalsIgnoreCase(status) || "Removed".equalsIgnoreCase(status))){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			status = service.checkStatus(job).getJobStatus();
		}
		
		boolean hasFilesAfterGetResults = false;
		File storeDirectory = new File("store");
		storeDirectory.mkdir();
		job = service.getJobResult(job, new SimpleFileStore(storeDirectory, new URI("http://ubuntu.itsdev.infotechsoft.com:8080/gp")));
		
		Collection<org.cvit.cabig.dmr.cmef.domain.File> files = job.getResultFiles();
		if (files != null && files.size() > 0) {
			hasFilesAfterGetResults = true;
		}
		
		boolean hasFilesInTheStore = storeDirectory.list().length > 0;

		printFooter("hasFilesBeforeGetResults: " + hasFilesBeforeGetResults + "\nhasFilesAfterGetResults: " + hasFilesAfterGetResults +
				"\nhasFilesInTheStore: " + hasFilesInTheStore);
		assert(!hasFilesBeforeGetResults && hasFilesAfterGetResults && hasFilesInTheStore);
	}

	@Test
	public void testListJobResults() throws CmefServiceException  {		
		printHeader("Executing Test 11: testing listJobResults");
		//Add Job to job list
		ComputationalModel model = TestCaseDefaults.getLinuxJavaModel();
		ComputationJob job = TestCaseDefaults.getLinuxJavaJob();
		
//		Get List of Jobs already there
		List<ComputationJob> firstJobs = service.listJobResults();
		
		job = service.startJob(model, job);	
		String status = job.getJobStatus();
		
		while (!("Completed".equalsIgnoreCase(status) || "Removed".equalsIgnoreCase(status))){
			status = service.checkStatus(job).getJobStatus();
		}

		//Get list of jobs with newly finished job
		List<ComputationJob> secondJobs = service.listJobResults();
		
		//Make sure first List does not contain new job,
		//but second list does
		boolean firstContains = false;
		boolean secondContains = false;
		int job1;
		int job2 = job.getJobNumber();
		for (ComputationJob aJob : firstJobs){
			job1 = aJob.getJobNumber();
			if (job1 == job2) {
				firstContains = true;
				break;
			}
		}
		for (ComputationJob aJob : secondJobs){
			job1 = aJob.getJobNumber();
			if (job1 == job2) {
				secondContains = true;
				break;
			}
		}
		printFooter("firstContains: " + firstContains + "\nsecondContains: " + secondContains);
		assertTrue(!firstContains && secondContains);
	}

	@Test
	public void testCheckStatus() throws CmefServiceException  {
		printHeader("Executing Test 12: Testing checkStatus");
		ComputationalModel model = TestCaseDefaults.getLinuxBasicJavaModel();
		ComputationJob job = TestCaseDefaults.getLinuxBasicJavaJob();
		job.setModel(model);
		
		//Check original status to be null
		String firstStatus = job.getJobStatus();
		
		job = service.startJob(model, job);
		
		job = service.checkStatus(job);
		String status = job.getJobStatus();

		printFooter("firstStatus: " + firstStatus + "\nstatus: " + status);
		assertTrue(firstStatus == null && status != null);
	}
	
	private void printHeader(String message) {
		System.err.println("--------------------------------------");
		System.err.println(message);
		System.err.println("--------------------------------------");
	}
	
	private void printFooter(String message) {
		System.err.println("Result(s): ");
		System.err.println(message);
		System.err.println("--------------------------------------\n\n");
	}
}
