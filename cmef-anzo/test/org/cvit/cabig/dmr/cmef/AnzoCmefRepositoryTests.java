/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Collection ;
import java.util.Collections ;
import java.util.Iterator ;
import java.util.logging.Handler ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.util.logging.SimpleFormatter ;
import java.util.logging.StreamHandler ;

import org.cvit.cabig.dmr.cmef.CmefRepository ;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform ;
import org.cvit.cabig.dmr.domain.Entry ;
import org.junit.After ;
import org.junit.Before ;
import org.junit.Test ;

//TODO: extract AbstractRepositoryTests superclass, can extend to verify specific repository implementations...
public class AnzoCmefRepositoryTests {
    private CmefRepository repository ;
    
    @Before
    public void createRepository() {
	repository = new TestCmefProvider().provideRepository(TestCmefProvider.ADMIN_CREDENTIALS) ;
    }
    
    @Before
    public void enableLogging() {
	Handler handler = new StreamHandler(System.err, new SimpleFormatter()) ;
	handler.setLevel(Level.ALL) ;
	Logger.getLogger("").addHandler(handler) ;
	Logger.getLogger("").setLevel(Level.ALL) ;
    }
    
    @After
    public void flushErrorStream() {
	System.err.flush() ;
    }
    
    @Test
    public void canListModelsForEntry() throws Exception {
	Entry entry = entryRef("urn:lsid:org.cvit.cmef:1") ;
	ComputationalModel model1 = newTestModel() ;
	model1.setTitle("Model 1") ;
	model1 = repository.addModelToEntry(model1, entry) ;
	ComputationalModel model2 = newTestModel() ;
	model2.setTitle("Model 2") ;
	model2 = repository.addModelToEntry(model2, entry) ;
	
	Iterator<ComputationalModel> models = repository.listModelsForEntry(entry) ;
	
	//TODO: equality checks...
	while (models.hasNext()) {
	    ComputationalModel model = models.next() ;
	    System.out.printf("Returned model: %s.\n", model.getTitle()) ;
	}
    }
    
    @Test
    public void canAddAndGetModel() throws Exception {
	ComputationalModel model = newTestModel() ;
	
	Entry entry = entryRef("urn:lsid:org.cvit.cmef:1") ;
	model = repository.addModelToEntry(model, entry) ;
	ComputationalModel returnedModel = repository.getModel(model) ;
	
	//TODO: equality checks...
	System.out.printf("Got model: %s.\n", returnedModel.getTitle()) ;
    }
    
    @Test
    public void canUpdateModel() throws Exception {
	ComputationalModel model = newTestModel() ;
	Entry entry = entryRef("urn:lsid:org.cvit.cmef:1") ;
	model = repository.addModelToEntry(model, entry) ;
	
	model.setComment("Updated model...") ;
	Parameter updatedParam = new Parameter() ;
	updatedParam.setName("new_param") ;
	updatedParam.setDescription("A new param.") ;
	updatedParam.setDataType("text") ;
	updatedParam.setPrefix("-new") ;
	updatedParam.setChoices(Arrays.asList("Choice 1", "Choice 2")) ;
	updatedParam.setDefaultValue("Choice 1") ;
	model.setParameters(Collections.singleton(updatedParam)) ;
	repository.updateModel(model) ;
	ComputationalModel result = repository.getModel(model) ;
	
	//TODO: equality checks...
	System.out.printf("Got model: %s.\n", result.getTitle()) ;
    }
    
    @Test
    public void canListJobsForModel() throws Exception {
	ComputationalModel model = newTestModel() ;
	Entry entry = entryRef("urn:lsid:org.cvit.cmef:1") ;
	model = repository.addModelToEntry(model, entry) ;
	ComputationJob job1 = newTestJob(model) ;
	job1.setTitle("Job 1") ;
	job1 = repository.addJobToModel(job1, model) ;
	ComputationJob job2 = newTestJob(model) ;
	job2.setTitle("Job 2") ;
	job2 = repository.addJobToModel(job2, model) ;
	
	Iterator<ComputationJob> jobs = repository.listJobsForModel(model) ;
	
	while (jobs.hasNext()) {
	    ComputationJob job = jobs.next() ;
	    System.out.printf("Returned job: %s.\n", job.getTitle()) ;
	}
    }
    
    @Test
    public void canAddAndGetJob() throws Exception {
	ComputationalModel model = newTestModel() ;
	Entry entry = entryRef("urn:lsid:org.cvit.cmef:1") ;
	model = repository.addModelToEntry(model, entry) ;
	ComputationJob job = newTestJob(model) ;
	
	job = repository.addJobToModel(job, model) ;
	ComputationJob result = repository.getJob(job) ;
	
	//TODO: equality checks...
	System.out.printf("Got job: %s.\n", result.getId()) ;
    }
    
    @Test
    public void canUpdateJob() throws Exception {
	ComputationalModel model = newTestModel() ;
	Entry entry = entryRef("urn:lsid:org.cvit.cmef:1") ;
	model = repository.addModelToEntry(model, entry) ;
	ComputationJob job = newTestJob(model) ;
	job = repository.addJobToModel(job, model) ;
	
	job.setComment("updated job...") ;
	File file = new File() ;
	file.setName("new_file.txt") ;
	file.setSource("http://example.org/new_file.txt") ;
	job.setResultFiles(Collections.singleton(file)) ;
	repository.updateJob(job) ;
	ComputationJob result = repository.getJob(job) ;
	
	//TODO: equality checks...
	System.out.printf("Got job: %s.\n", result.getId()) ;
    }
    
    private static Entry entryRef(String id) {
	Entry result = new Entry() ;
	result.setId(id) ;
	return result ;
    }
    
    private static ComputationalModel newTestModel() {
	ComputationalModel result = new ComputationalModel() ;
	result.setTitle("Test") ;
	result.setDescription("My test.") ;
	result.setComment("this is a test model...") ;
	result.setCommandLine("testModel <egfr>") ;
	result.setVersion("1") ;
	ComputingPlatform comp = new ComputingPlatform() ;
	comp.setOperatingSystemType("any") ;
	comp.setProcessorArchitecture("any") ;
	comp.setModel(Collections.singleton(result)) ;
	result.setPlatforms(Collections.singleton(comp)) ;
	ProgrammingPlatform prog = new ProgrammingPlatform() ;
	prog.setLanguageType("Java") ;
	prog.setLanguageVersion("1.4.1") ;
	prog.setModel(Collections.singleton(result)) ;
	result.setProgram(prog) ;
	File doc = new File() ;
	doc.setName("modelDoc.txt") ;
	doc.setSource("http://example.org/modelDoc.txt") ;
	doc.setDocumentModel(result) ;
	result.setDocument(doc) ;
	File exec = new File() ;
	exec.setName("executable.jar") ;
	exec.setSource("http://example.org/executable.jar") ;
	exec.setFileModel(result) ;
	result.setFiles(Collections.singleton(exec)) ;
	Parameter param = new Parameter() ;
	param.setName("param1") ;
	param.setDescription("Param 1") ;
	param.setDataType("float") ;
	param.setPrefix("-in") ;
	param.setChoices(Arrays.asList("1.0", "2.0", "3.0")) ;
	param.setDefaultValue("1.0") ;
	//DONOW: support for Boolean attributes...
//	param.setIsOptional(false) ;
//	param.setIsFile(false) ;
	result.setParameters(Collections.singleton(param)) ;
	return result ;
    }
    
    private static ComputationJob newTestJob(ComputationalModel model) {
	ComputationJob result = new ComputationJob() ;
	result.setTitle("My job") ;
	result.setDescription("This is my job.") ;
	result.setComment("A test job.") ;
	//DONOW: support for date and integer attributes...
//	result.setDateSubmitted(new Date()) ;
//	result.setDateCompleted(new Date()) ;
//	result.setJobNumber(1) ;
	result.setJobStatus("finished") ;
	result.setUserId("urn:lsid:org.cvit:user_1") ;
	File result1 = new File() ;
	result1.setName("stdout.txt") ;
	result1.setSource("http://example.org/stdout.txt") ;
	result1.setJob(result) ;
	File result2 = new File() ;
	result2.setName("stderr.txt") ;
	result2.setSource("http://example.org/stderr.txt") ;
	result2.setJob(result) ;
	result.setResultFiles(Arrays.asList(result1, result2)) ;
	Collection<ParameterValue> values = new ArrayList<ParameterValue>() ;
	for (Parameter param : model.getParameters()) {
	    ParameterValue value = new ParameterValue() ;
	    value.setValue("2.0") ;
	    Parameter paramRef = new Parameter() ;
	    paramRef.setId(param.getId()) ;
	    value.setParameter(paramRef) ;
	    values.add(value) ;
	}
	result.setParameterValues(values) ;
	return result ;
    }
}
