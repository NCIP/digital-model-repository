/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.vocabulary;

public class CMEF {
    private CMEF() {}
    
    public static final String NAMESPACE = "urn:lsid:cvit.org:vocabularies:cmef#" ;
    
    private static String uri(String local) {
	return NAMESPACE + local ;
    }
    
    public static class Types {
	private Types() {}
	public static final String MODEL = uri("ComputationalModel") ;
	public static final String JOB = uri("ComputationJob") ;
	public static final String PARAMETER = uri("Parameter") ;
	public static final String PARAMETER_VALUE = uri("ParameterValue") ;
	public static final String FILE = uri("File") ;
	public static final String COMPUTITNG_PLATFORM = uri("ComputingPlatform") ;
	public static final String PROGRAMMING_PLATFORM = uri("ProgrammingPlatform") ;
    }
    
    public static class ObjectProperties {
	private ObjectProperties() {}
	public static final String JOB = uri("job") ;
	public static final String MODEL = uri("model") ;
	public static final String COMPUTING_PLATFORM = uri("computingPlatform") ;
	public static final String PROGRAMMING_PLATFORM = uri("programmingPlatform") ;
	public static final String PARAMETER = uri("parameter") ;
	public static final String DOCUMENT = uri("document") ;
	public static final String FILE = uri("file") ;
	public static final String RESULT = uri("result") ;
	public static final String PARAMETER_VALUE = uri("parameterValue") ;
	public static final String MODELS = uri("models") ;
	public static final String JOBS = uri("jobs") ;
    }
    
    public static class DatatypeProperties {
	private DatatypeProperties() {}
	public static final String COMMAND_LINE = uri("commandLine") ;
	public static final String VERSION = uri("version") ;
	public static final String DATE_SUBMITTED = uri("dateSubmitted") ;
	public static final String DATE_COMPLETED = uri("dateCompleted") ;
	public static final String JOB_NUMBER = uri("jobNumber") ;
	public static final String JOB_STATUS = uri("jobStatus") ;
	public static final String USER_ID = uri("userId") ;
	public static final String OPSYS_TYPE = uri("operatingSystemType") ;
	public static final String PROC_TYPE = uri("processorArchitecture") ;
	public static final String LANG_TYPE = uri("languageType") ;
	public static final String LANG_VER = uri("languageVersion") ;
	public static final String DATA_TYPE = uri("dataType") ;
	public static final String PREFIX = uri("prefix") ;
	public static final String CHOICE = uri("choice") ;
	public static final String DEFAULT_VALUE = uri("defaultValue") ;
	public static final String OPTIONAL = uri("optional") ;
	public static final String IS_FILE = uri("isFile") ;
	public static final String PARAM_VALUE = uri("value") ;
    }
}
