/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


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
