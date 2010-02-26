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
