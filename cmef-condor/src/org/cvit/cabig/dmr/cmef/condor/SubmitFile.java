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
