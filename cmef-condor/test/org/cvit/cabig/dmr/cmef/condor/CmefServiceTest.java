/*******************************************************************************
 * caBIG™ Open Source Software License
 * Computational Model Execution Framework (CMEF) v1.0
 * 
 * Copyright Notice.
 * Copyright 2010 Massachusetts General Hospital (“caBIG™ Participant”).  Computational Model Execution Framework (CMEF) was created with NCI funding and is part of the caBIG™ initiative. The software subject to this notice and license includes both human readable source code form and machine readable, binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant and You.  “You (or “Your”) shall mean a person or an entity, and all other entities that control, are controlled by, or are under common control with the entity.  “Control” for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.  
 * 
 * License.  
 * Provided that You agree to the conditions described below, caBIG™ Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights in the caBIG™ Software, including any copyright or patent rights therein, to (i) use, install, disclose, access, operate, execute, reproduce, copy, modify, translate, market, publicly display, publicly perform, and prepare derivative works of the caBIG™ Software in any manner and for any purpose, and to have or permit others to do so; (ii) make, have made, use, practice, sell, and offer for sale, import, and/or otherwise dispose of caBIG™ Software (or portions thereof); (iii) distribute and have distributed to and by third parties the caBIG™ Software and any modifications and derivative works thereof; and (iv) sublicense the foregoing rights set out in (i), (ii) and (iii) to third parties, including the right to license such rights to further third parties.  For sake of clarity, and not by way of limitation, caBIG™ Participant shall have no right of accounting or right of payment from You or Your sublicensees for the rights granted under this License.  This License is granted at no charge to You.  Your downloading, copying, modifying, displaying, distributing or use of caBIG™ Software constitutes acceptance of all of the terms and conditions of this Agreement.  If you do not agree to such terms and conditions, you have no right to download, copy, modify, display, distribute or use the caBIG™ Software.  
 * 
 * 1.	Your redistributions of the source code for the caBIG™ Software must retain the above copyright notice, this list of conditions and the disclaimer and limitation of liability of Article 6 below.  Your redistributions in object code form must reproduce the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials provided with the distribution, if any.
 * 
 * 2.	Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: “This product includes software developed by Massachusetts General Hospital.”  If You do not include such end-user documentation, You shall include this acknowledgment in the caBIG™ Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3.	You may not use the names  ”Massachusetts General Hospital”, “MGH”, "INFOTECH Soft", “The National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™” to endorse or promote products derived from this caBIG™ Software.  This License does not authorize You to use any trademarks, service marks, trade names, logos or product names of either caBIG™ Participant, NCI or caBIG™, except as required to comply with the terms of this License.
 * 
 * 4.	For sake of clarity, and not by way of limitation, You may incorporate this caBIG™ Software into Your proprietary programs and into any third party proprietary programs.  However, if You incorporate the caBIG™ Software into third party proprietary programs, You agree that You are solely responsible for obtaining any permission from such third parties required to incorporate the caBIG™ Software into such third party proprietary programs and for informing Your sublicensees, including without limitation Your end-users, of their obligation to secure any required permissions from such third parties before incorporating the caBIG™ Software into such third party proprietary software programs.  In the event that You fail to obtain such permissions, You agree to indemnify caBIG™ Participant for any claims against caBIG™ Participant by such third parties, except to the extent prohibited by law, resulting from Your failure to obtain such permissions.
 * 
 * 5.	For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses of modifications of the caBIG™ Software, or any derivative works of the caBIG™ Software as a whole, provided Your use, reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.
 * 
 * 6.	THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED.  IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU)
 * Contributors: INFOTECH Soft, Inc.
 ******************************************************************************/
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
