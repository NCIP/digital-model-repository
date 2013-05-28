/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */

package org.cvit.cabig.dmr.cmef.condor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform;
import org.cvit.cabig.dmr.cmef.domain.Parameter;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform;

/**
 * TODO implement hardware architecture *
 */
public class SubmitFile {
	Logger logger = Logger.getLogger("org.cvit.cabig.dmr.cmef");
	static final String CONDOR_SUBMIT_TEMPLATE_FILE = "java_condor_template.txt";
	/**
	 * HAS_R and X64 are custom-defined properties that must be set in the CONDOR_CONFIG
	 * file on nodes with R or nodes running a 64 bit architecture.
	 */
	static final String REQUIREMENTS_KEY = "requirements = ";
	static final String PARAM_LANG_R = "(HAS_R =?= True)";
	static final String PARAM_ARCH_64 = "(X64 =?= True)";
	static final String PARAM_WINDOWS_OS = "OpSys == \"WINNT51\"";
	static final String PARAM_LINUX_OS = "OpSys == \"LINUX\"";
	static final String PARAM_MEMORY_256 = "(Memory > 256)";

	static final String CONDOR_SUBMIT_CMD_OUTPUT_FILE = "submit";
	private static final String PARAM_ESCAPE_END = ">";
	private static final String PARAM_ESCAPE_START = "<";

	private Properties submitContents;
	protected File fileLocation;
	
	private String requirementsNeeded;

	protected SubmitFile(ComputationalModel model, ComputationJob job) {
		submitContents = new Properties();
		// add default values
		submitContents.put("universe", "java");
		submitContents.put("executable", "ExecutableWrapper.jar");
		submitContents.put("output", "output.txt");
		submitContents.put("error", "error.txt");
		submitContents.put("log", "runtime.txt");
		submitContents.put("should_transfer_files", "YES");
		submitContents.put("when_to_transfer_output", "ON_EXIT");
		try {
			// load file overrides
			submitContents
					.load(ClassLoader
							.getSystemResourceAsStream(SubmitFile.CONDOR_SUBMIT_TEMPLATE_FILE));
		} catch (Exception ex) {
			System.err
					.println("Could not load condor template properties from file: "
							+ SubmitFile.CONDOR_SUBMIT_TEMPLATE_FILE);
			// ex.printStackTrace();
		}
		// what files need to be copied/transferred
		submitContents.put("transfer_input_files", getNeededFiles(model));
		// what files are JAR files
		submitContents.put("jar_files", "ExecutableWrapper.jar "
				+ getNeededJars(model));
		// the commandline arguments
		submitContents.put("arguments", createCommandLine(model, job));

		// requirements
		StringBuilder builder = new StringBuilder(SubmitFile.REQUIREMENTS_KEY + SubmitFile.PARAM_MEMORY_256);
		if (model.getPlatforms() != null) {
			for (ComputingPlatform platform : model.getPlatforms()) {
				String os = platform.getOperatingSystemType();
				String arch = platform.getProcessorArchitecture();
				if ("Linux".equals(os)) {
					builder.append(" && " + SubmitFile.PARAM_LINUX_OS);
					if ("X64".equalsIgnoreCase(arch)) {
						builder.append(" && " + SubmitFile.PARAM_ARCH_64);
					}
					break;
				} else if ("Windows".equals(os)) {
					builder.append(" && " + SubmitFile.PARAM_WINDOWS_OS);
					if ("X64".equalsIgnoreCase(arch)) {
						builder.append(" && " + SubmitFile.PARAM_LANG_R);
					}
					break;
				}
				// else "Any" / no constraint
			}
		}
		// require node has R installed (Note: "HAS_R" is custom-defined condor
		// property)
		ProgrammingPlatform platform = model.getProgram();
		if (platform != null && "R".equals(platform.getLanguageType())) {
			builder.append(" && " + SubmitFile.PARAM_LANG_R);
		}
		requirementsNeeded = builder.toString();

//		logger.info("SubmitFile() -> " + submitContents.toString());
	}

	protected String createCommandLine(ComputationalModel model,
			ComputationJob job) {
		String modelCommandLine = model.getCommandLine();

		System.err.println("SubmitFile modelCommandLine: " + modelCommandLine); // XXX
																				// Debugging

		java.util.Map<String, String> parameterValues = new java.util.HashMap<String, String>();
		// store the default value in the map
		if (model.getParameters() != null) {
			for (Parameter modelParameter : model.getParameters()) {
				if (modelParameter.isFile == null
						|| modelParameter.isFile.equals(Boolean.FALSE)) {
					if (modelParameter.getDefaultValue() != null) {
						parameterValues.put(modelParameter.getName(),
								modelParameter.getDefaultValue());
					}
				}
			}
		}
		// overwrite with the provided parameter value
		if (job.getParameterValues() != null) {
			for (ParameterValue parameterValue : job.getParameterValues()) {
				Parameter parameter = parameterValue.getParameter();
				if (parameter.isFile == null
						|| parameter.isFile.equals(Boolean.FALSE)) {
					parameterValues.put(parameter.getName(), parameterValue
							.getValue());
				}
			}
		}
		// replace parameter placeholders in commandline with parameter values
		for (Map.Entry<String, String> entry : parameterValues.entrySet()) {
			System.err.println("SubmitFile3.1 modelCommandLine: " + modelCommandLine);
			// TODO XXX does PREFIX need to be added to command line?
			modelCommandLine = modelCommandLine.replaceAll(PARAM_ESCAPE_START
					+ entry.getKey() + PARAM_ESCAPE_END, entry.getValue());
		}
		// rebuild command line to use ExecutableWrapper
		int jarIndex = modelCommandLine.indexOf(".jar");
		if (jarIndex > 0) {
			System.err.println("SubmitFile4.1 modelCommandLine: " + modelCommandLine);
			int wsIndex = modelCommandLine.indexOf(" ");
			if (jarIndex < wsIndex || wsIndex < 0) {
				// first argument is a JAR file
				modelCommandLine = "ExecutableWrapper java -jar "
						+ modelCommandLine;
			}
			// if whitespace before JAR, assume JAR is a parameter, not an
			// executable
		} else {
			// first argument is NOT a JAR file
			modelCommandLine = "ExecutableWrapper " + modelCommandLine;
		}
		logger.info("SubmitFile.getCommandLine() -> " + modelCommandLine);

		System.err.println("SubmitFile8 modelCommandLine: " + modelCommandLine); // XXX
																				// Debugging

		return modelCommandLine;
	}

	/**
	 * Writes the condor_submit command file to the given directory.
	 */
	protected void outputFile(String directory) throws IOException {
		fileLocation = new File(directory
				+ System.getProperty("file.separator")
				+ SubmitFile.CONDOR_SUBMIT_CMD_OUTPUT_FILE);
		fileLocation.createNewFile();
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(fileLocation));

		if (submitContents != null) {
			submitContents.store(stream, "");
			stream.write(requirementsNeeded.getBytes());			
			stream.write("\r\nQueue".getBytes());
			stream.close();
		}
		logger.info("SubmitFile.outputFile(" + directory + ") -> "
				+ fileLocation.getAbsolutePath());
	}

	private String getNeededJars(ComputationalModel model) {
		Collection<org.cvit.cabig.dmr.cmef.domain.File> neededJars = model
				.getFiles();
		StringBuilder builder = new StringBuilder();
		if (neededJars != null) {
			for (org.cvit.cabig.dmr.cmef.domain.File fileName : neededJars) {
				if (Util.isArchive(fileName.getName())) {
					builder.append(fileName.getName() + " ");
				}
			}
		}
		return builder.toString().trim();
	}

	private String getNeededFiles(ComputationalModel model) {
		Collection<org.cvit.cabig.dmr.cmef.domain.File> neededFiles = model
				.getFiles();
		StringBuilder builder = new StringBuilder();
		if (neededFiles != null) {
			for (org.cvit.cabig.dmr.cmef.domain.File file : neededFiles) {
				builder.append(file.getName() + ",");
			}
		}
		return builder.toString().trim();
	}

	public File getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(File manifestLocation) {
		this.fileLocation = manifestLocation;
	}
}
