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

import java.util.ArrayList;
import java.util.Collection;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform;
import org.cvit.cabig.dmr.cmef.domain.File;
import org.cvit.cabig.dmr.cmef.domain.Parameter;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform;

public class TestCaseDefaults {

	public static ComputationalModel getWindowsExecutableModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("WindowsLCModeling2D.exe <egfr>");
		compModel.setTitle("WindowsExecutable");	
		compModel.setId("WinExec");
		compModel.setComment("This is the comment");
		compModel.setDescription("This is the description");
		compModel.setVersion("4.2");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("WindowsLCModeling2D.exe");
		file1.setSource("http://10.0.0.121:8080/gp/tests/WindowsLCModeling2D.exe");
		files.add(file1);
		compModel.setFiles(files);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Parameters
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Collection<Parameter> parameterValue = new ArrayList<Parameter>();
		Parameter param1 = new Parameter();
		param1.setDataType("Integer");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(false);
		param1.setName("egfr");
		param1.setPrefix("");		
		
		parameterValue.add(param1);
		
		compModel.setParameters(parameterValue);
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("prog_id");
		program.setLanguageType("C++");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("Plat_id");
		platform1.setOperatingSystemType("Windows");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationalModel getWindows64ExecutableModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("WindowsLCModeling2D.exe <egfr>");
		compModel.setTitle("WindowsExecutable");	
		compModel.setId("WinExec");
		compModel.setComment("This is the comment");
		compModel.setDescription("This is the description");
		compModel.setVersion("4.2");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("WindowsLCModeling2D.exe");
		file1.setSource("http://10.0.0.121:8080/gp/tests/WindowsLCModeling2D.exe");
		files.add(file1);
		compModel.setFiles(files);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Parameters
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Collection<Parameter> parameterValue = new ArrayList<Parameter>();
		Parameter param1 = new Parameter();
		param1.setDataType("Integer");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(false);
		param1.setName("egfr");
		param1.setPrefix("");		
		
		parameterValue.add(param1);
		
		compModel.setParameters(parameterValue);
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("prog_id");
		program.setLanguageType("C++");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("Plat_id");
		platform1.setOperatingSystemType("Windows");
		platform1.setProcessorArchitecture("x64");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getWindowsExecutableJob() {
		ComputationJob job = new ComputationJob();
		job.setComment("Comment");
		job.setDescription("Description");
		job.setId("job1");
		job.setSource("");
		job.setTitle("window Job");
		job.setUserId("uid");
		Collection<ParameterValue> parameterValue = new ArrayList<ParameterValue>();
		ParameterValue paramValue1 = new ParameterValue();
		paramValue1.setJob(job);
		Parameter param1 = new Parameter();
		
		param1.setDataType("Integer");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(false);
		param1.setName("egfr");
		param1.setPrefix("Param1 prefix");
		
		param1.setValue(paramValue1);
		paramValue1.setParameter(param1);
		paramValue1.setValue("19.4");
		
		parameterValue.add(paramValue1);
		
		job.setParameterValues(parameterValue);
		return job;
	}
	
	public static ComputationalModel getLinuxExecutableModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("LinuxLCModeling2D.exe <egfr>");
		compModel.setTitle("LinuxExecutable");	
		compModel.setId("LinExec");
		compModel.setComment("This is the comment");
		compModel.setDescription("This is the description");
		compModel.setVersion("4.2");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("LinuxLCModeling2D.exe");
		file1.setSource("http://10.0.0.121:8080/gp/tests/LinuxLCModeling2D.exe");
		files.add(file1);
		compModel.setFiles(files);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Parameters
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Collection<Parameter> parameterValue = new ArrayList<Parameter>();
		Parameter param1 = new Parameter();
		param1.setDataType("Integer");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(false);
		param1.setName("egfr");
		param1.setPrefix("");		
		
		parameterValue.add(param1);
		
		compModel.setParameters(parameterValue);
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("prog_id");
		program.setLanguageType("Java");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("Plat_id");
		platform1.setOperatingSystemType("Linux");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getLinuxExecutableJob() {
		ComputationJob job = new ComputationJob();
		job.setComment("Comment");
		job.setDescription("Description");
		job.setId("job1");
		job.setSource("");
		job.setTitle("window Job");
		job.setUserId("uid");
		Collection<ParameterValue> parameterValue = new ArrayList<ParameterValue>();
		ParameterValue paramValue1 = new ParameterValue();
		paramValue1.setJob(job);
		Parameter param1 = new Parameter();
		
		param1.setDataType("Integer");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(false);
		param1.setName("egfr");
		param1.setPrefix("Param1 prefix");
		
		param1.setValue(paramValue1);
		paramValue1.setParameter(param1);
		paramValue1.setValue("19.4");
		
		parameterValue.add(paramValue1);
		
		job.setParameterValues(parameterValue);
		return job;
	}

	public static ComputationalModel getWindowsPropertiesDemoModel1() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("CancerModel.exe");
		compModel.setTitle("WindowsPropertiesExecutable");	
		compModel.setId("WinPropExec");
		compModel.setComment("This is the comment");
		compModel.setDescription("This is the description");
		compModel.setVersion("4.2");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		File file2 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("CancerModel.exe");
		file1.setSource("http://10.0.0.121:8080/gp/tests/lcmodeling2d/CancerModel.exe");
		
		file2.setFileModel(compModel);
		file2.setId("2");
		file2.setName("Properties2.txt");
		file2.setSource("http://10.0.0.121:8080/gp/tests/lcmodeling2d/Properties2.txt");
		files.add(file1);
		files.add(file2);
		compModel.setFiles(files);		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("prog_id");
		program.setLanguageType("Java");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("Plat_id");
		platform1.setOperatingSystemType("Windows");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getWindowsPropertiesDemoJob1() {
		ComputationJob job = new ComputationJob();
		job.setComment("Comment");
		job.setDescription("Description");
		job.setId("job1");
		job.setSource("");
		job.setTitle("window Job");
		job.setUserId("uid");
		return job;
	}

	public static ComputationalModel getWindowsPropertiesDemoModel2() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("CancerModel.exe <Properties2.txt>");
		compModel.setTitle("WindowsPropertiesExecutable");	
		compModel.setId("WinPropExec");
		compModel.setComment("This is the comment");
		compModel.setDescription("This is the description");
		compModel.setVersion("4.2");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("CancerModel.exe");
		file1.setSource("http://10.0.0.121:8080/gp/tests/lcmodeling2d/CancerModel.exe");
		files.add(file1);
		compModel.setFiles(files);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Parameters
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Collection<Parameter> parameterValue = new ArrayList<Parameter>();
		Parameter param1 = new Parameter();
		param1.setDataType("File");
		param1.setDescription("Properties File");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(true);
		param1.setName("Properties2.txt");
		param1.setPrefix("Param1 prefix");		
		
		parameterValue.add(param1);
		
		compModel.setParameters(parameterValue);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("prog_id");
		program.setLanguageType("Java");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("Plat_id");
		platform1.setOperatingSystemType("Windows");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getWindowsPropertiesDemoJob2() {
		ComputationJob job = new ComputationJob();
		job.setComment("Comment");
		job.setDescription("Description");
		job.setId("job1");
		job.setSource("");
		job.setTitle("window Job");
		job.setUserId("uid");
		
		Collection<ParameterValue> parameterValue = new ArrayList<ParameterValue>();
		ParameterValue paramValue1 = new ParameterValue();
		paramValue1.setJob(job);
		Parameter param1 = new Parameter();
		
		param1.setDataType("File");
		param1.setDescription("prop file");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(true);
		param1.setName("Properties2.txt");
		param1.setPrefix("Param1 prefix");
		
		param1.setValue(paramValue1);
		paramValue1.setParameter(param1);
		paramValue1.setValue("http://10.0.0.121:8080/gp/tests/lcmodeling2d/Properties2.txt");
		
		parameterValue.add(paramValue1);
		
		job.setParameterValues(parameterValue);
		return job;
	}
	
	public static ComputationalModel getRDemoModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("<is.things.R> things.out");
		compModel.setTitle("RProgram");	
		compModel.setId("RProg");
		compModel.setComment("This is the comment");
		compModel.setDescription("This is the description");
		compModel.setVersion("4.2");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Parameters
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Collection<Parameter> parameterValue = new ArrayList<Parameter>();
		Parameter param1 = new Parameter();
		param1.setDataType("File");
		param1.setDescription("Properties File");
		param1.setId("1");
		param1.setIsOptional(true);
		param1.setIsFile(true);
		param1.setName("is.things.R");
		param1.setPrefix("Param1 prefix");
		
		parameterValue.add(param1);
		
		compModel.setParameters(parameterValue);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("prog_id");
		program.setLanguageType("R");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("Plat_id");
		platform1.setOperatingSystemType("any");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getRDemoJob() {
		ComputationJob job = new ComputationJob();
		job.setComment("Comment");
		job.setDescription("Description");
		job.setId("job1");
		job.setSource("");
		job.setTitle("R Job");
		job.setUserId("uid");
		
		Collection<ParameterValue> parameterValue = new ArrayList<ParameterValue>();
		ParameterValue paramValue1 = new ParameterValue();
		paramValue1.setJob(job);
		Parameter param1 = new Parameter();
		
		param1.setDataType("File");
		param1.setDescription("prop file");
		param1.setId("1");
		param1.setIsOptional(true);
		param1.setIsFile(true);
		param1.setName("is.things.R");
		param1.setPrefix("Param1 prefix");
		
		param1.setValue(paramValue1);
		paramValue1.setParameter(param1);
		paramValue1.setValue("http://10.0.0.121:8080/gp/tests/is.things.R");
		
		parameterValue.add(paramValue1);
		
		job.setParameterValues(parameterValue);
		return job;
	}
	
	public static ComputationalModel getLinuxJavaModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("2DVisualizer.jar");
		compModel.setTitle("Windows Visualizer Java Jar");	
		compModel.setId("VisJava");
		compModel.setComment("Java Visualizer");
		compModel.setDescription("Processes the output of the LCModeling2D C++ program " +
				"(the .txt files), and generates a set of JPG images and a QuickTime movie.");
		compModel.setVersion("1");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("2DVisualizer.jar");
		file1.setSource("http://10.0.0.121:8080/gp/tests/2DVisualizer.jar");
		files.add(file1);
		compModel.setFiles(files);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Parameters
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Collection<Parameter> parameterValue = new ArrayList<Parameter>();
		Parameter param1 = new Parameter();
		param1.setDataType("File");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(true);
		param1.setIsFile(true);
		param1.setName("component.txt");
		param1.setPrefix("Param1 prefix");		
		

		Parameter param2 = new Parameter();
		param2.setDataType("File");
		param2.setDefaultValue("1");
		param2.setDescription("EGFR number");
		param2.setId("2");
		param2.setIsOptional(true);
		param2.setIsFile(true);
		param2.setName("componentROC.txt");
		param2.setPrefix("Param1 prefix");	
		

		Parameter param3 = new Parameter();
		param3.setDataType("File");
		param3.setDefaultValue("1");
		param3.setDescription("EGFR number");
		param3.setId("3");
		param3.setIsOptional(true);
		param3.setIsFile(true);
		param3.setName("lcm_death.txt");
		param3.setPrefix("Param1 prefix");	
		

		Parameter param4 = new Parameter();
		param4.setDataType("File");
		param4.setDefaultValue("1");
		param4.setDescription("EGFR number");
		param4.setId("4");
		param4.setIsOptional(true);
		param4.setIsFile(true);
		param4.setName("lcm_migration.txt");
		param4.setPrefix("Param1 prefix");	
		

		Parameter param5 = new Parameter();
		param5.setDataType("File");
		param5.setDefaultValue("1");
		param5.setDescription("EGFR number");
		param5.setId("5");
		param5.setIsOptional(true);
		param5.setIsFile(true);
		param5.setName("lcm_proliferation.txt");
		param5.setPrefix("Param1 prefix");	
		

		Parameter param6 = new Parameter();
		param6.setDataType("File");
		param6.setDefaultValue("1");
		param6.setDescription("EGFR number");
		param6.setId("6");
		param6.setIsOptional(true);
		param6.setIsFile(true);
		param6.setName("lcm_quiescence.txt");
		param6.setPrefix("Param1 prefix");	
		

		Parameter param7 = new Parameter();
		param7.setDataType("File");
		param7.setDefaultValue("1");
		param7.setDescription("EGFR number");
		param7.setId("7");
		param7.setIsOptional(true);
		param7.setIsFile(true);
		param7.setName("phenotype_number.txt");
		param7.setPrefix("Param1 prefix");	
		
		parameterValue.add(param1);
		parameterValue.add(param2);
		parameterValue.add(param3);
		parameterValue.add(param4);
		parameterValue.add(param5);
		parameterValue.add(param6);
		parameterValue.add(param7);
		
		compModel.setParameters(parameterValue);
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("1");
		program.setLanguageType("Java");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("1");
		platform1.setOperatingSystemType("Windows");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getLinuxJavaJob() {
		ComputationJob job = new ComputationJob();
		job.setComment("Comment");
		job.setDescription("Description");
		job.setId("Win Java Job");
		job.setSource("");
		job.setTitle("");
		job.setUserId("userId");
		Collection<ParameterValue> parameterValue = new ArrayList<ParameterValue>();
		ParameterValue paramValue1 = new ParameterValue();
		ParameterValue paramValue2 = new ParameterValue();
		ParameterValue paramValue3 = new ParameterValue();
		ParameterValue paramValue4 = new ParameterValue();
		ParameterValue paramValue5 = new ParameterValue();
		ParameterValue paramValue6 = new ParameterValue();
		ParameterValue paramValue7 = new ParameterValue();
		paramValue1.setJob(job);
		paramValue2.setJob(job);
		paramValue3.setJob(job);
		paramValue4.setJob(job);
		paramValue5.setJob(job);
		paramValue6.setJob(job);
		paramValue7.setJob(job);
		
		Parameter param1 = new Parameter();
		param1.setDataType("File");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(true);
		param1.setName("component.txt");
		param1.setPrefix("Param1 prefix");		
		

		Parameter param2 = new Parameter();
		param2.setDataType("File");
		param2.setDefaultValue("1");
		param2.setDescription("EGFR number");
		param2.setId("2");
		param2.setIsOptional(false);
		param2.setIsFile(true);
		param2.setName("componentROC.txt");
		param2.setPrefix("Param1 prefix");	
		

		Parameter param3 = new Parameter();
		param3.setDataType("File");
		param3.setDefaultValue("1");
		param3.setDescription("EGFR number");
		param3.setId("3");
		param3.setIsOptional(false);
		param3.setIsFile(true);
		param3.setName("lcm_death.txt");
		param3.setPrefix("Param1 prefix");	
		

		Parameter param4 = new Parameter();
		param4.setDataType("File");
		param4.setDefaultValue("1");
		param4.setDescription("EGFR number");
		param4.setId("4");
		param4.setIsOptional(false);
		param4.setIsFile(true);
		param4.setName("lcm_migration.txt");
		param4.setPrefix("Param1 prefix");	
		

		Parameter param5 = new Parameter();
		param5.setDataType("File");
		param5.setDefaultValue("1");
		param5.setDescription("EGFR number");
		param5.setId("5");
		param5.setIsOptional(false);
		param5.setIsFile(true);
		param5.setName("lcm_proliferation.txt");
		param5.setPrefix("Param1 prefix");	
		

		Parameter param6 = new Parameter();
		param6.setDataType("File");
		param6.setDefaultValue("1");
		param6.setDescription("EGFR number");
		param6.setId("6");
		param6.setIsOptional(false);
		param6.setIsFile(true);
		param6.setName("lcm_quiescence.txt");
		param6.setPrefix("Param1 prefix");	
		

		Parameter param7 = new Parameter();
		param7.setDataType("File");
		param7.setDefaultValue("1");
		param7.setDescription("EGFR number");
		param7.setId("7");
		param7.setIsOptional(false);
		param7.setIsFile(true);
		param7.setName("phenotype_number.txt");
		param7.setPrefix("Param1 prefix");	

		
		param1.setValue(paramValue1);
		paramValue1.setParameter(param1);
		paramValue1.setValue("http://10.0.0.121:8080/gp/tests/JavaVisualizer/component.txt");
		
		param2.setValue(paramValue2);
		paramValue2.setParameter(param2);
		paramValue2.setValue("http://10.0.0.121:8080/gp/tests/JavaVisualizer/componentROC.txt");
		
		param3.setValue(paramValue3);
		paramValue3.setParameter(param3);
		paramValue3.setValue("http://10.0.0.121:8080/gp/tests/JavaVisualizer/lcm_death.txt");
		
		param4.setValue(paramValue4);
		paramValue4.setParameter(param4);
		paramValue4.setValue("http://10.0.0.121:8080/gp/tests/JavaVisualizer/lcm_migration.txt");
		
		param5.setValue(paramValue5);
		paramValue5.setParameter(param5);
		paramValue5.setValue("http://10.0.0.121:8080/gp/tests/JavaVisualizer/lcm_proliferation.txt");
		
		param6.setValue(paramValue6);
		paramValue6.setParameter(param6);
		paramValue6.setValue("http://10.0.0.121:8080/gp/tests/JavaVisualizer/lcm_quiescence.txt");
		
		param7.setValue(paramValue7);
		paramValue7.setParameter(param7);
		paramValue7.setValue("http://10.0.0.121:8080/gp/tests/JavaVisualizer/phenotype_number.txt");
		
		parameterValue.add(paramValue1);
		parameterValue.add(paramValue2);
		parameterValue.add(paramValue3);
		parameterValue.add(paramValue4);
		parameterValue.add(paramValue5);
		parameterValue.add(paramValue6);
		parameterValue.add(paramValue7);
		
		job.setParameterValues(parameterValue);
		return job;
	}
	
	public static ComputationalModel getWindowsJavaModelWithZip() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("LCModeling2DVisualizer.jar");
		compModel.setTitle("Visualizer Java Jar With Zip");	
		compModel.setId("VisJava");
		compModel.setComment("Java Visualizer");
		compModel.setDescription("Processes the output of the LCModeling2D C++ program " +
				"(the .txt files), and generates a set of JPG images and a QuickTime movie.");
		compModel.setVersion("1");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("LCModeling2DVisualizer.jar");
		file1.setSource("http://10.0.0.121:8080/gp/tests/JavaVisualizer/LCModeling2DVisualizer.jar");
		files.add(file1);
		compModel.setFiles(files);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Parameters
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Collection<Parameter> parameterValue = new ArrayList<Parameter>();
		Parameter param1 = new Parameter();
		param1.setDataType("File");
		param1.setDefaultValue("1");
		param1.setDescription("output zip!");
		param1.setId("1");
		param1.setIsOptional(true);
		param1.setIsFile(true);
		param1.setName("1_output.zip");
		param1.setPrefix("Param1 prefix");		
		
		parameterValue.add(param1);
		
		compModel.setParameters(parameterValue);
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("1");
		program.setLanguageType("Java");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("1");
		platform1.setOperatingSystemType("any");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getWindowsJavaJobWithZip() {
		ComputationJob job = new ComputationJob();
		job.setComment("Comment");
		job.setDescription("Description");
		job.setId("Win Java Job");
		job.setSource("");
		job.setTitle("");
		job.setUserId("userId");
		Collection<ParameterValue> parameterValue = new ArrayList<ParameterValue>();
		ParameterValue paramValue1 = new ParameterValue();
		paramValue1.setJob(job);
		
		Parameter param1 = new Parameter();
		param1.setDataType("File");
		param1.setDefaultValue("1");
		param1.setDescription("Output Zip");
		param1.setId("1");
		param1.setIsOptional(true);
		param1.setIsFile(true);
		param1.setName("1_output.zip");
		param1.setPrefix("Param1 prefix");		
		
		param1.setValue(paramValue1);
		paramValue1.setParameter(param1);
		paramValue1.setValue("http://10.0.0.121:8080/gp/tests/1_output.zip");
				
		parameterValue.add(paramValue1);
		
		job.setParameterValues(parameterValue);
		return job;
	}
	
	public static ComputationalModel getLinuxBasicJavaModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("Basic.jar <numOfTimes>");
		compModel.setTitle("Linux Java Jar");	
		compModel.setId("LinJava");
		compModel.setComment("This is the comment");
		compModel.setDescription("This is the description");
		compModel.setVersion("4.2");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("Basic.jar");
		file1.setSource("http://10.0.0.121:8080/gp/tests/Basic.jar");
		files.add(file1);
		compModel.setFiles(files);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Parameters
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		Collection<Parameter> parameterValue = new ArrayList<Parameter>();
		Parameter param1 = new Parameter();
		param1.setDataType("Integer");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(false);
		param1.setName("numOfTimes");
		param1.setPrefix("Param1 prefix");		
		
		parameterValue.add(param1);
		
		compModel.setParameters(parameterValue);
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("1");
		program.setLanguageType("Java");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("1");
		platform1.setOperatingSystemType("any");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getLinuxBasicJavaJob() {
		ComputationJob job = new ComputationJob();
		job.setComment("");
		job.setDescription("");
		job.setId("LinJava Job");
		job.setSource("");
		job.setTitle("");
		job.setUserId("peter");
		Collection<ParameterValue> parameterValue = new ArrayList<ParameterValue>();
		ParameterValue paramValue1 = new ParameterValue();
		paramValue1.setJob(job);
		Parameter param1 = new Parameter();
		
		param1.setDataType("Integer");
		param1.setDefaultValue("1");
		param1.setDescription("EGFR number");
		param1.setId("1");
		param1.setIsOptional(false);
		param1.setIsFile(false);
		param1.setName("numOfTimes");
		param1.setPrefix("Param1 prefix");
		
		param1.setValue(paramValue1);
		paramValue1.setParameter(param1);
		paramValue1.setValue("34");
		
		parameterValue.add(paramValue1);
		
		job.setParameterValues(parameterValue);
		return job;
	}
	
	public static ComputationalModel getRequiredComputationalModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("echo \"Hi\"");
		compModel.setTitle("Echo");	
		return compModel;
	}
	
	public static ComputationJob getRequiredComputationJob() {
		ComputationJob job = new ComputationJob();
		job.setId("1");
		job.setTitle("EchoJob");
		job.setUserId("");
		return job;
	}
	
	public static ComputationalModel getEmptyComputationalModel() {
		ComputationalModel compModel = new ComputationalModel();
		return compModel;
	}
	
	public static ComputationJob getEmptyComputationJob() {
		ComputationJob job = new ComputationJob();
		job.setId("1");
		job.setTitle("EmptyJob");
		job.setUserId("");
		return job;
	}
	
	
	public static ComputationalModel getStopTestDelayModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("Delay.jar");
		compModel.setTitle("Stop Test Delay Class");	
		compModel.setId("stop");
		compModel.setComment("Delay class for stopJob testing");
		compModel.setDescription("Creates a model for a jar file that puts the thread to sleep, " +
				"giving time to call stopJob on the executing job");
		compModel.setVersion("1");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("Delay.jar");
		file1.setSource("http://10.0.0.121:8080/gp/tests/Delay.jar");
		files.add(file1);
		compModel.setFiles(files);		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("1");
		program.setLanguageType("Java");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("1");
		platform1.setOperatingSystemType("any");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getStopTestDelayJob() {
		ComputationJob job = new ComputationJob();
		job.setId("Win Java Job");
		job.setTitle("stopJob");
		job.setUserId("peter");		
		return job;
	}
	
	public static ComputationalModel getDeleteTestSimpleModel() {
		ComputationalModel compModel = new ComputationalModel();
		compModel.setCommandLine("Simple.jar");
		compModel.setTitle("Delete Test Simple Class");	
		compModel.setId("delete");
		compModel.setComment("Simple class for deleteJob testing");
		compModel.setDescription("Creates a model for a jar file that prints a single line, " +
				"allowing us to call deleteJob on the finished job");
		compModel.setVersion("1");
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Resource Files
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<File> files = new ArrayList<File>();
		File file1 = new File();
		file1.setFileModel(compModel);
		file1.setId("1");
		file1.setName("Simple.jar");
		file1.setSource("http://10.0.0.121:8080/gp/tests/Simple.jar");
		files.add(file1);
		compModel.setFiles(files);		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ProgrammingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ProgrammingPlatform program = new ProgrammingPlatform();
		program.setId("1");
		program.setLanguageType("Java");
		program.setLanguageVersion("any");
		compModel.setProgram(program);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ComputingPlatform
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>();
		ComputingPlatform platform1 = new ComputingPlatform();
		platform1.setId("1");
		platform1.setOperatingSystemType("any");
		platform1.setProcessorArchitecture("any");
		platforms.add(platform1);
		compModel.setPlatforms(platforms);
		return compModel;
	}
	
	public static ComputationJob getDeleteTestSimpleJob() {
		ComputationJob job = new ComputationJob();
		job.setId("Java Job");
		job.setTitle("deleteJob");
		job.setUserId("peter");		
		return job;
	}
	
	
}
