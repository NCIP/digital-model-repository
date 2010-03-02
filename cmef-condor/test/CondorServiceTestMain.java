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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cvit.cabig.dmr.cmef.FileStore;
import org.cvit.cabig.dmr.cmef.condor.CondorService;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform;
import org.cvit.cabig.dmr.cmef.domain.File;
import org.cvit.cabig.dmr.cmef.domain.Parameter;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform;

public class CondorServiceTestMain implements Runnable {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		new CondorServiceTestMain().run();
	}

	CondorService condorService;

	private CondorServiceTestMain() {
		condorService = new CondorService();
	}

	public void run() {

		testList(); // see if there's anything there

		testStart();

		testList();

		testListResults();

		testStart_Stop_Delete();

		testStart_Status_Result_Delete();

		testStart_Status_Result_LC2D();

		testStart_Status_Result_LC2DViz();

		// testDeleteAll();
	}

	private void testStart_Status_Result_LC2D() {
		ComputationalModel model = createModelLC2D_Linux();
		ComputationJob job = createJobLC2D(model);
		runStart_Status_Result(model, job);
	}

	private void testStart_Status_Result_LC2DViz() {
		ComputationalModel model = createModelLC2DViz_Linux();
		ComputationJob job = createJobLC2DViz(model);
		runStart_Status_Result(model, job);
	}

	private void testDeleteAll() {
		try {
			Collection<ComputationJob> jobs = condorService.listJobs();
			for (ComputationJob job : jobs) {
				try {
					condorService.deleteJob(job);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void testList() {
		try {
			List<ComputationJob> jobs = condorService.listJobs();
			System.err.println("testList found " + jobs.size() + " jobs.");
			for (ComputationJob job : jobs) {
				System.err.println("testList Job:" + job.getId() + ", "
						+ job.getTitle() + ", #" + job.getJobNumber() + ", "
						+ job.getJobStatus());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void testListResults() {
		try {
			condorService.listJobResults();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void testStart_Stop_Delete() {
		try {
			ComputationalModel model = createModelBasic();
			ComputationJob job = createJobBasic(model);
			condorService.startJob(model, job);
			System.out.println("testStart_Stop_Delete start: "
					+ job.getJobStatus());

			condorService.stopJob(job);
			System.out.println("testStart_Stop_Delete stop: "
					+ job.getJobStatus());

			condorService.checkStatus(job);
			System.out.println("testStart_Stop_Delete status: "
					+ job.getJobStatus());

			condorService.deleteJob(job);
			System.out.println("testStart_Stop_Delete delete: "
					+ job.getJobStatus());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void testStart_Status_Result_Delete() {
		ComputationalModel model = createModelBasic();
		ComputationJob job = createJobBasic(model);
		runStart_Status_Result(model, job);
		try {
			condorService.deleteJob(job);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void runStart_Status_Result(ComputationalModel model,
			ComputationJob job) {
		try {
			condorService.startJob(model, job);
			System.out.println("testStart_Status_Result_Delete start: "
					+ job.getJobStatus());

			condorService.checkStatus(job);
			System.out.println("testStart_Status_Result_Delete status: "
					+ job.getJobStatus());

			while (!job.getJobStatus().equalsIgnoreCase("completed")
					&& !job.getJobStatus().contains("Error")) {
				condorService.checkStatus(job);
				System.out.println("testStart_Status_Result_Delete status: "
						+ job.getJobStatus());
				try {
					Thread.sleep(10000); // 10 sec
				} catch (Exception ex) {
				}
			}

			FileStore store = new FileStore() {
				Map<String, org.cvit.cabig.dmr.cmef.domain.File> storedFiles = new TreeMap<String, org.cvit.cabig.dmr.cmef.domain.File>();

				@Override
				public void deleteFile(String fileUri) {
					System.err
							.println("FileStore::deleteFile(" + fileUri + ")");
					storedFiles.remove(fileUri);
				}

				@Override
				public File storeFile(String name, InputStream data)
						throws IOException {
					System.err.println("FileStore::storeFile(" + name + ", "
							+ data + ")");
					org.cvit.cabig.dmr.cmef.domain.File storedFile = new org.cvit.cabig.dmr.cmef.domain.File();
					storedFile.setSource("./" + name);
					storedFiles.put(storedFile.getSource(), storedFile);
					return storedFile;
				}

			};
			condorService.getJobResult(job, store);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void testStart() {
		try {
			ComputationalModel model = createModelBasic();
			ComputationJob job = createJobBasic(model);

			condorService.startJob(model, job);
			System.out.println("testStart Status: " + job.getJobStatus());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private ComputationalModel createModelBasic() {
		ComputationalModel model = new ComputationalModel();

		model.setId(String.valueOf(Math.random()).substring(2));
		model.setTitle("Model " + model.getId());
		model.setPlatforms(getPlatformLinux());
		model.setProgram(getPlatformJava());
		Collection<Parameter> params = new ArrayList<Parameter>();
		Parameter param = new Parameter();
		param.setModel(model);
		param.setId(String.valueOf(Math.random()));
		param.setDataType("Integer");
		param.setIsFile(Boolean.FALSE);
		param.setDefaultValue("3");
		param.setName("count");
		params.add(param);
		model.setParameters(params);
		model.setCommandLine("Basic.jar <count>");
		// files need to be stored on website?
		Collection<File> files = new ArrayList<File>();
		File file = new File();
		file.setName("Basic.jar");
		file.setSource("http://support.infotechsoft.com/cvit/Basic.jar");
		file.setId("file_basic_jar");
		file.setFileModel(model);
		files.add(file);
		model.setFiles(files);
		return model;
	}

	private ComputationJob createJobBasic(ComputationalModel model) {
		ComputationJob job = new ComputationJob();
		job.setId(model.getId()); // would be assigned by CViT
		job.setTitle("Job for " + job.getId());
		job.setModel(model);
		Collection<ParameterValue> paramValues = new ArrayList<ParameterValue>();
		for (Parameter param : model.getParameters()) {
			ParameterValue paramValue = new ParameterValue();
			paramValue.setParameter(param);
			paramValue.setId(String.valueOf(Math.random()));
			paramValue.setValue("15");
			paramValue.setJob(job);
			paramValues.add(paramValue);
		}
		job.setParameterValues(paramValues);

		return job;
	}

	private ComputationalModel createModelLC2D_Linux() {
		ComputationalModel model = new ComputationalModel();

		model.setId(String.valueOf(Math.random()).substring(2));
		model.setTitle("Model " + model.getId());
		model.setPlatforms(getPlatformLinux());
		model.setProgram(getPlatformJava());
		Collection<Parameter> params = new ArrayList<Parameter>();
		Parameter param = new Parameter();
		param.setModel(model);
		param.setId(String.valueOf(Math.random()));
		param.setDataType("float");
		param.setIsFile(Boolean.FALSE);
		param.setDefaultValue("1.0");
		param.setName("egf");
		params.add(param);
		model.setParameters(params);
		model.setCommandLine("LinuxLCModeling2D.exe <egf>");
		// files need to be stored on website?
		Collection<File> files = new ArrayList<File>();
		File file = new File();
		file.setName("LinuxLCModeling2D.exe");
		file
				.setSource("http://support.infotechsoft.com/cvit/LinuxLCModeling2D.exe");
		file.setId("file_LinuxLCModeling2D.exe_exe");
		file.setFileModel(model);
		files.add(file);
		model.setFiles(files);
		return model;
	}

	private ComputationJob createJobLC2D(ComputationalModel model) {
		ComputationJob job = new ComputationJob();
		job.setId(model.getId()); // would be assigned by CViT
		job.setTitle("Job for " + job.getId());
		job.setModel(model);
		Collection<ParameterValue> paramValues = new ArrayList<ParameterValue>();
		for (Parameter param : model.getParameters()) {
			ParameterValue paramValue = new ParameterValue();
			paramValue.setParameter(param);
			paramValue.setId(String.valueOf(Math.random()));
			paramValue.setValue("12.0");
			paramValue.setJob(job);
			paramValues.add(paramValue);
		}
		job.setParameterValues(paramValues);

		return job;
	}

	private ComputationalModel createModelLC2DViz_Linux() {
		ComputationalModel model = new ComputationalModel();

		model.setId(String.valueOf(Math.random()).substring(2));
		model.setTitle("Model " + model.getId());
		model.setPlatforms(getPlatformLinux());
		model.setProgram(getPlatformJava());
		Collection<Parameter> params = new ArrayList<Parameter>();
		Parameter param = new Parameter();
		param.setModel(model);
		param.setId(String.valueOf(Math.random()));
		param.setIsFile(Boolean.TRUE);
		param.setName("output.zip");
		params.add(param);
		model.setParameters(params);
		model.setCommandLine("LCModeling2DVisualizer.jar");
		// files need to be stored on website?
		Collection<File> files = new ArrayList<File>();
		File file = new File();
		file.setName("LCModeling2DVisualizer.jar");
		file
				.setSource("http://support.infotechsoft.com/cvit/LCModeling2DVisualizer.jar");
		file.setId("file_LCModeling2DVisualizer_jar");
		file.setFileModel(model);
		files.add(file);
		model.setFiles(files);
		return model;
	}

	private ComputationJob createJobLC2DViz(ComputationalModel model) {
		ComputationJob job = new ComputationJob();
		job.setId(model.getId()); // would be assigned by CViT
		job.setTitle("Job for " + job.getId());
		job.setModel(model);
		Collection<ParameterValue> paramValues = new ArrayList<ParameterValue>();
		for (Parameter param : model.getParameters()) {
			ParameterValue paramValue = new ParameterValue();
			paramValue.setParameter(param);
			paramValue.setId(String.valueOf(Math.random()));
			paramValue
					.setValue("http://support.infotechsoft.com/cvit/output.zip");
			paramValue.setJob(job);
			paramValues.add(paramValue);
		}
		job.setParameterValues(paramValues);

		return job;
	}

	private Collection<ComputingPlatform> getPlatformLinux() {
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform = new ComputingPlatform();
		platform.setOperatingSystemType("Linux");
		platforms.add(platform);
		return platforms;
	}

	private ProgrammingPlatform getPlatformJava() {
		ProgrammingPlatform platform = new ProgrammingPlatform();
		platform.setLanguageType("Java");
		return platform;
	}

	private ProgrammingPlatform getPlatformExec() {
		ProgrammingPlatform platform = new ProgrammingPlatform();
		platform.setLanguageType("C++");
		return platform;
	}
}
