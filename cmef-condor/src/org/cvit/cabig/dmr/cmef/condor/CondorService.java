/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.condor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.SysexMessage;

import org.cvit.cabig.dmr.cmef.CmefService;
import org.cvit.cabig.dmr.cmef.CmefServiceException;
import org.cvit.cabig.dmr.cmef.FileStore;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;
import org.cvit.cabig.dmr.cmef.domain.Parameter;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue;

/**
 * Implements the CmefService interface for Condor-backed execution.
 * 
 */
public class CondorService implements CmefService {
	Logger logger = Logger.getLogger("org.cvit.cabig.dmr.cmef");
	private static final String CONDOR_JOB_MAP_FILE = "CondorServiceMap.db";
	private static final String EXECUTABLE_WRAPPER_FILE = "ExecutableWrapper.jar"; // XXX
	// must
	// be
	// included
	// with
	// job

	// private static final String JOB_STATUS_RUNNING = "Running";
	// private static final String JOB_STATUS_ERROR = "Error";
	// private static final String JOB_STATUS_COMPLETE = "Complete";

	private static final String JOB_DIR = "jobs";

	private JobMap jobMap = new JobMap(CONDOR_JOB_MAP_FILE);
	private File jobDir = new File(JOB_DIR);

	public CondorService() {
		// make sure the root job directory exists
		if (!jobDir.exists())
			jobDir.mkdir();
	}

	/**
	 * @return ComputationJob will have the updated status. If job sent as parameter
	 * does not exist in the CMEF server, the returned ComputationJob will have same status as before.  
	 */
	@Override
	public ComputationJob checkStatus(ComputationJob job)
			throws CmefServiceException {
		String condorJobId = null;
		if (job.getJobNumber() != null)
			condorJobId = job.getJobNumber().toString();
		else if (job.getId() != null)
			condorJobId = jobMap.getCondorJobId(job.getId());
		if (condorJobId == null) {
			logger.warning("stopJob(): No Job Number");
			return job;
		}
		try {
			// first check history (if job is complete)
			InputStream condorStdout = runProcess("condor_history", "-xml", 
										String.valueOf(job.getJobNumber()), null);
			CondorMessageParser.updateStatus(condorStdout, job);
			condorStdout.close();
			if (job.getDateCompleted() == null) {
				// next (if not complete), check queue
				condorStdout = runProcess("condor_q", "-xml ", 
								String.valueOf(job.getJobNumber()), null);
				CondorMessageParser.updateStatus(condorStdout, job);
				condorStdout.close();
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not check job status for job '"
					+ job.getJobNumber() + "'", ex);
			throw new CmefServiceException(
					"Could not check job status for job '" + job.getJobNumber()
							+ "'", ex);
		}
		return job;
	}

	@Override
	public void deleteJob(ComputationJob job) throws CmefServiceException {
		String condorJobId = null;
		if (job.getJobNumber() != null)
			condorJobId = job.getJobNumber().toString();
		else if (job.getId() != null)
			condorJobId = jobMap.getCondorJobId(job.getId());
		if (condorJobId == null) {
			logger.warning("stopJob(): No Job Number");
			return;
		}
		try {
			stopJob(job);
		} catch (CmefServiceException ex) {
			// ignore
		}
		String jobDirString = jobMap.getCMEFJobDirByCondorJobId(condorJobId);
		logger.info("Starting deletion of Job '" + condorJobId + "' at '"
				+ jobDirString + "'...");
		CondorService.deltree(new File(jobDirString));
		jobMap.removeByCondorJobId(condorJobId);
		logger.info("Completed deletion of Job '" + condorJobId + "' at '"
				+ jobDirString + "'.");
	}

	@Override
	public ComputationJob getJobResult(ComputationJob job, FileStore store)
			throws CmefServiceException {
		if (store == null) {
			throw new CmefServiceException(new IllegalArgumentException(
					"FileStore can not be null"));
		}
		String condorJobId = null;
		if (job.getJobNumber() != null)
			condorJobId = job.getJobNumber().toString();
		else if (job.getId() != null)
			condorJobId = jobMap.getCondorJobId(job.getId());
		if (condorJobId == null) {
			logger.warning("getJobResult: No Job Number");
			return job;
		}

		/*
		 * returnedResultFiles: Collection of completed downloads to return to
		 * the job downloadCompleteFiles: files downloaded successfully which
		 * need to be removed if error resultFileArray: files returned by call
		 * to Condor
		 */
		Collection<String> downloadCompleteFiles = new ArrayList<String>();
		Collection<org.cvit.cabig.dmr.cmef.domain.File> returnedResultFiles = new ArrayList<org.cvit.cabig.dmr.cmef.domain.File>();

		String condorJobDir = jobMap.getCMEFJobDirByCondorJobId(condorJobId);
		File[] jobFiles = new File(condorJobDir).listFiles();
		
		//Get Model File(s)
		List<String> listOfModuleFiles = getModelFiles(condorJobDir); 
		
		for (File jobFile : jobFiles) {
		
			// TODO Check if part of model/job?
			// XXX Filter mode/job files
			if (!listOfModuleFiles.contains(jobFile.getName())) {
				try {
					org.cvit.cabig.dmr.cmef.domain.File downloadedFile = store
							.storeFile(jobFile.getName(), new BufferedInputStream(
									new FileInputStream(jobFile)));
					returnedResultFiles.add(downloadedFile);
					downloadCompleteFiles.add(downloadedFile.getSource());
				}
				// If exception, remove downloaded Files, and throw
				// CmefServiceException
				catch (IOException e) {
				    	boolean deleteError = false ;
				    	StringBuilder deleteErrors = new StringBuilder("Error cleaning up file(s): ") ;
					for (String url : downloadCompleteFiles) {
					    try {
						store.deleteFile(url) ;
					    } catch (IOException e1) {
						deleteError = true ;
						deleteErrors.append(url).append("; ");
					    }
					}
					StringBuilder error = new StringBuilder("Error handling remote file.") ;
					if (deleteError) {
					    deleteErrors.delete(deleteErrors.length() - 2, deleteErrors.length()) ;
					    error.append("\n").append(deleteErrors) ;
					}
					throw new CmefServiceException(error.toString(), e);
				}
			}
		}
		if (!returnedResultFiles.isEmpty())
			job.setResultFiles(returnedResultFiles);

		return checkStatus(job);
	}
	
	private List<String> getModelFiles(String condorJobLocation) throws CmefServiceException {
		List<String> listOfModuleFiles = new ArrayList<String>();
		Properties submitFileProps = new Properties();
		try {
			submitFileProps.load(new BufferedReader(new FileReader(new File(condorJobLocation, "submit"))));
			String modelFileString = submitFileProps.getProperty("transfer_input_files");
			String[] modelFileArray = modelFileString.split(",");
			for (int i = 0; i < modelFileArray.length; i++) {
				listOfModuleFiles.add(modelFileArray[i]);
			}
			listOfModuleFiles.add(EXECUTABLE_WRAPPER_FILE);
			listOfModuleFiles.add("submit");
		} catch (IOException e1) {
			throw new CmefServiceException("Submit File could not be read", e1);
		} 		
		return listOfModuleFiles;
	}

	@Override
	/*
	 * Only list jobs that are complete
	 */
	public List<ComputationJob> listJobResults() throws CmefServiceException {
		List<ComputationJob> jobs = new ArrayList<ComputationJob>();
		for (ComputationJob job : listJobs()) {
			if (isJobFinished(job.getJobStatus()))
				jobs.add(job);
		}
		return jobs;
	}

	@Override
	/*
	 * Retrieves the status of (all) jobs (from JobMap) (e.g., jobs that haven't
	 * been deleted)
	 */
	public List<ComputationJob> listJobs() throws CmefServiceException {
		List<ComputationJob> jobListToReturn = new ArrayList<ComputationJob>();
		Collection<String> condorJobIds = jobMap.listCondorJobIds();
		for (String condorJobId : condorJobIds) {
			try {
				ComputationJob job = new ComputationJob();
				job.setId(jobMap.getCMEFJobId(condorJobId));
				job.setJobNumber(new Integer(condorJobId));
				checkStatus(job);
				jobListToReturn.add(job);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Exception while listing jobs", ex);
			}
		}
		return jobListToReturn;
	}

	@Override
	// TODO: job.id = CMEFJobId, job.jobNumber = condorJobId
	public ComputationJob startJob(ComputationalModel model, ComputationJob job)
			throws CmefServiceException {

		// 1. Create a directory for the CMEF job to run within
		int i = 0;
		File cmefJobDir = new File(jobDir, "cmef-" + String.valueOf(i));
		while (cmefJobDir.exists())
			cmefJobDir = new File(jobDir, "cmef-" + String.valueOf(++i));
		cmefJobDir.mkdir();
		String cmefJobDirString = cmefJobDir.getAbsolutePath();

		// 2. Copy the CMEF model and job files into the directory
		// 2.a. Add Model Files
		Collection<org.cvit.cabig.dmr.cmef.domain.File> files = model
				.getFiles();
		if (files == null) // XXX Note: if there are no files in the model,
			// should we be running it?
			files = new ArrayList<org.cvit.cabig.dmr.cmef.domain.File>();

		// 2.b. Add Job Files
		Collection<ParameterValue> paramValues = job.getParameterValues();
		if (paramValues != null) {
			for (ParameterValue paramValue : paramValues) {
				Parameter parameter = paramValue.getParameter();
				if (parameter != null && parameter.getIsFile()) {
					org.cvit.cabig.dmr.cmef.domain.File file = new org.cvit.cabig.dmr.cmef.domain.File();
					file.setJob(job);
					file.setId(paramValue.getId());
					file.setSource(paramValue.getValue());
					file.setName(parameter.getName());
					files.add(file);
				}
			}
		}
		// 2.c. Copy the Files to the Job Dir
		for (org.cvit.cabig.dmr.cmef.domain.File file : files) {
			if (file != null)
				Util.downloadFile(file.getSource(), file.getName(),
						cmefJobDirString);
			java.io.File localFile = new java.io.File(cmefJobDir, file
					.getName());
			if (!localFile.canExecute())
				localFile.setExecutable(true);
		}
		// 2.c. Add ExecutableWrapper.jar to Job Dir
		try {
			InputStream is = Util.class.getResourceAsStream("/resources/ExecutableWrapper.jar");
			OutputStream os = new FileOutputStream(
					cmefJobDir.getAbsolutePath() + System.getProperty("file.separator") + "ExecutableWrapper.jar");
			
			byte[] buf = new byte[1024];
			
			int len;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
			is.close();
			os.close();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not copy "
					+ EXECUTABLE_WRAPPER_FILE + " to '" + cmefJobDir + "'", ex);
			throw new CmefServiceException("Could not copy "
					+ EXECUTABLE_WRAPPER_FILE + " to '" + cmefJobDir + "'", ex);
		}

		// 3. Submit the job to Condor
		// 3.a. Prepare the condor_submit configuration file
		SubmitFile submitFile = new SubmitFile(model, job);
		try {
			// 3.b. Place the submit file in the job directory
			submitFile.outputFile(cmefJobDirString);
		} catch (IOException ex) {
			logger.log(Level.SEVERE,
					"Exception while preparing Condor configuration", ex);
			throw new CmefServiceException(
					"Could not store Condor submit configuration.", ex);
		}
		try {
			// 3.c. call condor_submit
			InputStream condorSubmitStdout = runProcess("condor_submit",
					submitFile.getFileLocation().getName(), null, cmefJobDir);
			Integer condorJobId = CondorMessageParser
					.parseCondorSubmitStdout(condorSubmitStdout);
			condorSubmitStdout.close();

			// 4. Store job information in the JobMap <condorJobId, cmefJobId,
			// cmefJobDir>
			jobMap.put(condorJobId.toString(), job.getId(), cmefJobDir
					.getAbsolutePath().toString());
			job.setJobNumber(condorJobId);
		} catch (IOException ex) {
			logger.log(Level.SEVERE,
					"Exception while invoking Condor process.", ex);
			throw new CmefServiceException(
					"Exception while invoking Condor process", ex);
		}

		// 5. Update the ComputationJob object
		job.setDateSubmitted(new java.util.Date());

		// 6. return the job's current status
		return checkStatus(job);
	}

	@Override
	public ComputationJob stopJob(ComputationJob job)
			throws CmefServiceException {
		if (job == null || job.getJobNumber() == null) {
			logger.warning("stopJob(): No Job Number");
			return job;
		}
		try {
			jobMap.getCondorJobId(job.getId());
			InputStream condorSubmitStdout = runProcess("condor_rm", job
					.getJobNumber().toString(), null, null);
			condorSubmitStdout.close();
			job.setDateCompleted(new Date());
		} catch (IOException ex) {
			logger.log(Level.SEVERE,
					"Exception while invoking Condor process.", ex);
			throw new CmefServiceException(
					"Exception while invoking Condor process", ex);
		}
		return checkStatus(job);
	}

	private static InputStream runProcess(String command, String arg1, String arg2,
			File workingDir) throws IOException {
		ProcessBuilder processBuilder = null;;
		if (arg2 == null) 
			processBuilder = new ProcessBuilder(command, arg1);
		else 
			processBuilder = new ProcessBuilder(command, arg1, arg2);
		
		// run the process in the working dir
		if (workingDir != null)
			processBuilder.directory(workingDir);
		Process process = processBuilder.start();
		return process.getInputStream();
	}

	private static void deltree(File file) {
		// if FILE, delete it
		if (!file.isDirectory()) {
			try { // delete file
				file.delete();
			} catch (Exception ex) { // otherwise, schedule deletion on VM exit
				file.deleteOnExit();
			}
		} else {
			// list dir/files
			for (File jobFile : file.listFiles())
				deltree(jobFile);
			try { // delete dir
				file.delete();
			} catch (Exception ex) { // otherwise, schedule deletion on VM exit
				file.deleteOnExit();
			}
		}
	}

	private static boolean isJobFinished(String status) {
		return (status != null && (status.toLowerCase().contains("error") || status
				.equalsIgnoreCase("completed")));
	}

	@Override
	public JobState checkJobState(ComputationJob job) throws CmefServiceException {
		JobState returnState = null;
		String condorJobId = null;
		if (job.getId() != null)
			condorJobId = jobMap.getCondorJobId(job.getId());
		if (condorJobId == null) {
			logger.warning("stopJob(): No Job Number");
			returnState = JobState.NOT_SUBMITTED;
		}
		if (returnState == null) {
			try {
				// first check history (if job is complete)
				InputStream condorStdout = runProcess("condor_history", "-xml", 
											String.valueOf(job.getJobNumber()), null);
				boolean containsEntryInHistory = CondorMessageParser.containsEntryForInputStream(condorStdout);
				condorStdout.close();
				if (containsEntryInHistory) {
					returnState = JobState.FINISHED;
				}
				else {	// next (if not complete), check queue
					condorStdout = runProcess("condor_q", "-xml ", 
									String.valueOf(job.getJobNumber()), null);
					boolean containsEntryInQueue = CondorMessageParser.containsEntryForInputStream(condorStdout);
					condorStdout.close();
					if (containsEntryInQueue) {
						returnState = JobState.RUNNING;
					}
				}
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Could not check job state for job '"
						+ job.getJobNumber() + "'", ex);
				throw new CmefServiceException(
						"Could not check job state for job '" + job.getJobNumber()
								+ "'", ex);
			}
		}
		System.err.println("JobState is: " + returnState);
		return returnState == null ? JobState.NOT_SUBMITTED : returnState;
	}
}
