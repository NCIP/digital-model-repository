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
package org.cvit.cabig.dmr.cmef.util;

import java.util.ArrayList ;
import java.util.Collection ;
import java.util.Collections ;
import java.util.Date ;
import java.util.List ;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform ;

public class BeanCopyUtils {

    public static ComputationalModel copyModel(ComputationalModel model) {
	if (model == null) {
	    return null ;
	}
	ComputationalModel result = shallowCopyModel(model) ;
	result.setProgram(copyProgrammingPlatform(model.getProgram(), result)) ;
	result.setDocument(copyDocumentFile(model.getDocument(), result)) ;
	result.setFiles(copyModelFiles(model.getFiles(), result)) ;
	result.setPlatforms(copyComputingPlatforms(model.getPlatforms(), result)) ;
	result.setParameters(copyParameters(model.getParameters(), result)) ;
	return result ;
    }
    
    public static ComputationJob copyJob(ComputationJob job) {
	if (job == null) {
	    return null ;
	}
	ComputationJob result = shallowCopyJob(job) ;
	result.setResultFiles(copyResultFiles(job.getResultFiles(), job)) ;
	result.setParameterValues(copyParameterValues(job.getParameterValues(), job)) ;
	return result ;
    }
    
    public static ComputationalModel shallowCopyModel(ComputationalModel model) {
	if (model == null) {
	    return null ;
	}
	ComputationalModel result = new ComputationalModel() ;
	result.setId(model.getId()) ;
	result.setTitle(model.getTitle()) ;
	result.setDescription(model.getDescription()) ;
	result.setSource(model.getSource()) ;
	result.setComment(model.getComment()) ;
	result.setCommandLine(model.getCommandLine()) ;
	result.setVersion(model.getVersion()) ;
	return result ;
    }
    
    public static ComputationJob shallowCopyJob(ComputationJob job) {
	ComputationJob result = new ComputationJob() ;
	result.setId(job.getId()) ;
	result.setTitle(job.getTitle()) ;
	result.setDescription(job.getDescription()) ;
	result.setSource(job.getSource()) ;
	result.setComment(job.getComment()) ;
	result.setDateSubmitted(copyDate(job.getDateSubmitted())) ;
	result.setDateCompleted(copyDate(job.getDateCompleted())) ;
	result.setJobNumber(job.getJobNumber()) ;
	result.setJobStatus(job.getJobStatus()) ;
	result.setUserId(job.getUserId()) ;
	return result ;
    }
    
    public static ProgrammingPlatform copyProgrammingPlatform(ProgrammingPlatform program, ComputationalModel model) {
	return copyProgrammingPlatform(program, model == null ? null : Collections.singleton(model)) ;
    }
    
    public static ProgrammingPlatform copyProgrammingPlatform(ProgrammingPlatform program, Collection<ComputationalModel> models) {
	if (program == null) {
	    return null ;
	}
	if (models == null) {
	    models = Collections.emptySet() ;
	}
	ProgrammingPlatform result = new ProgrammingPlatform() ;
	result.setId(program.getId()) ;
	result.setLanguageType(program.getLanguageType()) ;
	result.setLanguageVersion(program.getLanguageVersion()) ;
	result.setModel(models) ;
	return result ;
    }
    
    public static File copyDocumentFile(File file, ComputationalModel model) {
	if (file == null) {
	    return null ;
	}
	File result = shallowCopyFile(file) ;
	result.setDocumentModel(model) ;
	return result ;
    }
    
    public static Collection<File> copyModelFiles(Collection<File> files, ComputationalModel model) {
	if (files == null) {
	    return null ;
	}
	List<File> result = new ArrayList<File>(files.size()) ;
	for (File f : files) {
	    File resultFile = shallowCopyFile(f) ;
	    resultFile.setFileModel(model) ;
	    result.add(resultFile) ;
	}
	return result ;
    }
    
    public static Collection<File> copyResultFiles(Collection<File> files, ComputationJob job) {
	if (files == null) {
	    return null ;
	}
	List<File> result = new ArrayList<File>(files.size()) ;
	for (File f : files) {
	    File resultFile = shallowCopyFile(f) ;
	    resultFile.setJob(job) ;
	    result.add(resultFile) ;
	}
	return result ;
    }
    
    public static Collection<ComputingPlatform> copyComputingPlatforms(Collection<ComputingPlatform> platforms, ComputationalModel model) {
	return copyComputingPlatforms(platforms, model == null ? null : Collections.singleton(model)) ;
    }
    
    public static Collection<ComputingPlatform> copyComputingPlatforms(Collection<ComputingPlatform> platforms, Collection<ComputationalModel> models) {
	if (platforms == null) {
	    return null ;
	}
	if (models == null) {
	    models = Collections.emptySet() ;
	}
	List<ComputingPlatform> result = new ArrayList<ComputingPlatform>(platforms.size()) ;
	for (ComputingPlatform p : platforms) {
	    ComputingPlatform copy = new ComputingPlatform() ;
	    copy.setId(p.getId()) ;
	    copy.setOperatingSystemType(p.getOperatingSystemType()) ;
	    copy.setProcessorArchitecture(p.getProcessorArchitecture()) ;
	    copy.setModel(models) ;
	    result.add(copy) ;
	}
	return result ;
    }
    
    public static Collection<ParameterValue> copyParameterValues(Collection<ParameterValue> values, ComputationJob job) {
	if (values == null) {
	    return null ;
	}
	List<ParameterValue> result = new ArrayList<ParameterValue>(values.size()) ;
	for (ParameterValue v : values) {
	    ParameterValue copy = shallowCopyParameterValue(v) ;
	    copy.setJob(job) ;
	    Parameter p = shallowCopyParameter(v.getParameter()) ;
	    if (p != null) {
		p.setValue(copy) ;
		copy.setParameter(p) ;
	    }
	    result.add(copy) ;
	}
	return result ;
    }
    
    public static ParameterValue shallowCopyParameterValue(ParameterValue value) {
	if (value == null) {
	    return null ;
	}
	ParameterValue result =  new ParameterValue() ;
	result.setId(value.getId()) ;
	result.setValue(value.getValue()) ;
	return result ;
    }
    
    public static Collection<Parameter> copyParameters(Collection<Parameter> parameters, ComputationalModel model) {
	if (parameters == null) {
	    return null ;
	}
	List<Parameter> result = new ArrayList<Parameter>(parameters.size()) ;
	for (Parameter p : parameters) {
	    Parameter copy = shallowCopyParameter(p) ;
	    copy.setModel(model) ;
	    result.add(copy) ;
	}
	return result ;
    }
    
    public static Parameter shallowCopyParameter(Parameter p) {
	if (p == null) {
	    return null ;
	}
	Parameter result = new Parameter() ;
	result.setId(p.getId()) ;
	result.setName(p.getName()) ;
	result.setDescription(p.getDescription()) ;
	result.setDataType(p.getDataType()) ;
	result.setPrefix(p.getPrefix()) ;
	result.setChoices(copyStringCollection(p.getChoices())) ;
	result.setDefaultValue(p.getDefaultValue()) ;
	result.setIsOptional(p.getIsOptional()) ;
	result.setIsFile(p.getIsFile()) ;
	return result ;
    }
    
    public static File shallowCopyFile(File file) {
	if (file == null) {
	    return null ;
	}
	File result = new File() ;
	result.setId(file.getId()) ;
	result.setName(file.getName()) ;
	result.setSource(file.getSource()) ;
	return result ;
    }
    
    public static Date copyDate(Date date) {
	if (date == null) {
	    return null ;
	}
	return new Date(date.getTime()) ;
    }
    
    public static boolean modelsAreEqual(ComputationalModel firstModel, ComputationalModel secondModel) {
    	
    	if (firstModel == null && secondModel == null) {
    		return true;
    	}
    	if (firstModel == null || secondModel == null) {
    		return false;
    	}
    	
    	//Checks instance Variables
		boolean isEqual = 	objectsMatch(firstModel.getId(), secondModel.getId()) &&
				objectsMatch(firstModel.getTitle(), secondModel.getTitle()) &&
				objectsMatch(firstModel.getDescription(), secondModel.getDescription()) &&
				objectsMatch(firstModel.getSource(), secondModel.getSource()) &&
				objectsMatch(firstModel.getComment(), secondModel.getComment()) &&
				objectsMatch(firstModel.getCommandLine(), secondModel.getCommandLine()) &&
				objectsMatch(firstModel.getVersion(), secondModel.getVersion()) ;
		
		if (!isEqual) {
			return false;
		}
		
		//Checks File attachments
		if (!checkCollection(firstModel.getFiles(), secondModel.getFiles(), 0)) {
			return false;
		}
		
		//Checks Paramater attachments
		if (!checkCollection(firstModel.getParameters(), secondModel.getParameters(), 1)) {
			return false;
		}
		
		//Checks ComputingPlatform attachments
		if (!checkCollection(firstModel.getPlatforms(), secondModel.getPlatforms(), 2)) {
			return false;
		}
		
		//Check Document
		isEqual = isEqual && 
					filesAreEqual(firstModel.getDocument(), secondModel.getDocument()) &&
						progPlatformAreEqual(firstModel.getProgram(), secondModel.getProgram())	;
		
		return isEqual;
    }
    
    public static boolean jobsAreEqual(ComputationJob firstJob, ComputationJob secondJob) {
    	
    	if (firstJob == null && secondJob == null) {
    		return true;
    	}
    	if (firstJob == null || secondJob == null) {
    		return false;
    	}
    	//Checks instance Variables
		boolean isEqual = 	objectsMatch(firstJob.getId(), secondJob.getId()) &&
				objectsMatch(firstJob.getTitle(), secondJob.getTitle() ) &&
				objectsMatch(firstJob.getDescription(), secondJob.getDescription() ) &&
				objectsMatch(firstJob.getSource(), secondJob.getSource() ) &&
				objectsMatch(firstJob.getComment(), secondJob.getComment() ) &&
				objectsMatch(firstJob.getDateSubmitted(), secondJob.getDateSubmitted() ) &&
				objectsMatch(firstJob.getDateCompleted(), secondJob.getDateCompleted() ) &&
				objectsMatch(firstJob.getJobNumber(), secondJob.getJobNumber() ) &&
				objectsMatch(firstJob.getJobStatus(), secondJob.getJobStatus() ) &&
				objectsMatch(firstJob.getUserId(), secondJob.getUserId() ) ;
		
		if (!isEqual) {
			return false;
		}
		
		//Checks File attachments
		if (!checkCollection(firstJob.getResultFiles(), secondJob.getResultFiles(), 0)) {
			return false;
		}
		
		//Checks ParamaterValue attachments
		if (!checkCollection(firstJob.getParameterValues(), secondJob.getParameterValues(), 3)) {
			return false;
		}
		
		return isEqual;
    }
    
    public static boolean filesAreEqual(File firstFile, File secondFile) {
    	if (firstFile == null && secondFile == null) {
				return true;
		}
		else if (firstFile == null || secondFile == null) {
			return false;
		}
		//Checks instance Variables
		return  	objectsMatch(firstFile.getId(), secondFile.getId()) &&
					objectsMatch(firstFile.getSource(), secondFile.getSource()) &&
					objectsMatch(firstFile.getName(), secondFile.getName());
	}
    
    public static boolean parameterValuesAreEqual(ParameterValue firstParamValue, ParameterValue secondParamValue) {
    	if (firstParamValue == null && secondParamValue == null) {
			return true;
		}
		else if (firstParamValue == null || secondParamValue == null) {
			return false;
		}
		//Checks instance Variables
		return 	objectsMatch(firstParamValue.getId(), secondParamValue.getId()) &&
				objectsMatch(firstParamValue.getValue(), secondParamValue.getValue()) &&
				parametersAreEqual(firstParamValue.getParameter(), secondParamValue.getParameter());
	}
    
    public static boolean parametersAreEqual(Parameter firstParameter, Parameter secondParameter) {
    	if (firstParameter == null && secondParameter == null) {
			return true;
		}
		else if (firstParameter == null || secondParameter == null) {
			return false;
		}
		//Checks instance Variables
		boolean isEqual = objectsMatch(firstParameter.getId(), secondParameter.getId()) &&
				objectsMatch(firstParameter.getName(), secondParameter.getName()) &&
					objectsMatch(firstParameter.getDescription(), secondParameter.getDescription()) &&
					objectsMatch(firstParameter.getDataType(), secondParameter.getDataType()) &&
					objectsMatch(firstParameter.getPrefix(), secondParameter.getPrefix()) &&
					objectsMatch(firstParameter.getDefaultValue(), secondParameter.getDefaultValue()) &&
					objectsMatch(firstParameter.getIsOptional(), secondParameter.getIsOptional()) &&
					objectsMatch(firstParameter.getIsFile(), secondParameter.getIsFile()) ;
		if (!isEqual) {
			return false;
		}
		
		Collection<String> firstSet = firstParameter.getChoices();
		Collection<String> secondSet = secondParameter.getChoices();
		if (firstSet == null && secondSet == null) {
			isEqual = true;
		}
		else if (firstSet == null || secondSet == null ||
				firstSet.size() != secondSet.size()) {
			isEqual = false;
		}
		else {
			isEqual = firstSet.containsAll(secondSet);
		}
		
		return isEqual;	
    }
    
    public static boolean progPlatformAreEqual(ProgrammingPlatform firstProgPlatform, ProgrammingPlatform secondProgPlatform) {
    	if (firstProgPlatform == null && secondProgPlatform == null) {
			return true;
		}
		else if (firstProgPlatform == null || secondProgPlatform == null) {
			return false;
		}
		//Checks instance Variables
		return 	objectsMatch(firstProgPlatform.getId(), secondProgPlatform.getId()) &&
				objectsMatch(firstProgPlatform.getLanguageType(), secondProgPlatform.getLanguageType()) &&
				objectsMatch(firstProgPlatform.getLanguageVersion(), secondProgPlatform.getLanguageVersion());
	}
    
    public static boolean compPlatformAreEqual(ComputingPlatform firstCompPlatform, ComputingPlatform secondCompPlatform) {
    	if (firstCompPlatform == null && secondCompPlatform == null) {
			return true;
		}
		else if (firstCompPlatform == null || secondCompPlatform == null) {
			return false;
		}
		//Checks instance Variables
		return 	objectsMatch(firstCompPlatform.getId(), secondCompPlatform.getId()) &&
				objectsMatch(firstCompPlatform.getOperatingSystemType(), secondCompPlatform.getOperatingSystemType()) &&
				objectsMatch(firstCompPlatform.getProcessorArchitecture(), secondCompPlatform.getProcessorArchitecture());
	}
    
    private static boolean objectsMatch(Object first, Object second) {
    	return (first == null && second == null) || 
				(first != null && second != null && first.equals(second));
    }
    
    /*
     * classType:
     * 				0 - File
     * 				1 - Parameter
     * 				2 - ComputingPlatform
     * 				3 - ParameterValue
     */
    private static boolean checkCollection(Collection<?> firstSet, Collection<?> secondSet, int classType) {
		if (firstSet == null && secondSet == null) {
			return true;
		}
		else if (firstSet != null && secondSet != null &&
				firstSet.size() != secondSet.size()) {
			return false;
		}
		else {
			int numFound = 0;
			for (Object o1 : firstSet) {
				boolean found = false;
				for (Object o2 : secondSet) {
					switch (classType) {					
					case 0:
						if (filesAreEqual((File)o1, (File)o2)) {
							numFound++;
							found = true;
						}
						break;
					case 1:
						if (parametersAreEqual((Parameter)o1, (Parameter)o2)) {
							numFound++;
							found = true;
						}
						break;
					case 2:
						if (compPlatformAreEqual((ComputingPlatform)o1, (ComputingPlatform)o2)) {
							numFound++;
							found = true;
						}
						break;
					case 3:
						if (parameterValuesAreEqual((ParameterValue)o1, (ParameterValue)o2)) {
							numFound++;
							found = true;
						}
						break;
					}
					if (found) {
						break;
					}
				}
			}
			return numFound == firstSet.size() ? true : false;
		}
    }
    
    private static Collection<String> copyStringCollection(Collection<String> strings) {
	if (strings == null) {
	    return null ;
	}
	Collection<String> result = new ArrayList<String>(strings.size()) ;
	result.addAll(strings) ;
	return result ;
    }
}
