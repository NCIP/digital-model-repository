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
