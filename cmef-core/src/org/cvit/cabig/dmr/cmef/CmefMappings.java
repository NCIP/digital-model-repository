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

import static com.infotechsoft.rdf.mapping.mappers.OldAttributeMapper.attribute ;

import java.net.URI ;
import java.net.URISyntaxException ;
import java.util.Arrays ;
import java.util.Collections ;
import java.util.LinkedHashSet ;
import java.util.Set ;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform ;
import org.cvit.cabig.dmr.cmef.vocabulary.CMEF ;
import org.cvit.cabig.dmr.cmef.vocabulary.DC11 ;
import org.cvit.cabig.dmr.domain.DataClassification ;
import org.cvit.cabig.dmr.domain.Entry ;
import org.cvit.cabig.dmr.cmef.vocabulary.CViT ;

import com.infotechsoft.rdf.RdfFactory ;
import com.infotechsoft.rdf.basic.BasicRdfFactory ;
import com.infotechsoft.rdf.mapping.BasicMappers ;
import com.infotechsoft.rdf.mapping.BooleanDatatypeTranslator ;
import com.infotechsoft.rdf.mapping.DatatypeTranslator ;
import com.infotechsoft.rdf.mapping.DateDatatypeTranslator ;
import com.infotechsoft.rdf.mapping.IntegerDatatypeTranslator ;
import com.infotechsoft.rdf.mapping.Mappers ;
import com.infotechsoft.rdf.mapping.mappers.AttributeMapper ;
import com.infotechsoft.rdf.mapping.mappers.BasicAssociationMapper ;
import com.infotechsoft.rdf.mapping.mappers.BasicClassMapper ;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type ;

public class CmefMappings {
    private static RdfFactory factory = new BasicRdfFactory() ;
    
    public static Mappers getMappers() {
	BasicMappers result = new BasicMappers() ;
	addClassMappings(result) ;
	addAssociationMappings(result) ;
	return result ;
    }
    
    private static void addClassMappings(BasicMappers mappers) {
	BasicClassMapper dataClassification = new BasicClassMapper(
	    DataClassification.class.getName(), 
	    createURI(CViT.Types.DATA), 
	    "id", 
	    getDataClassificationAttributes()) ;
	mappers.addClassMapping(dataClassification) ;

	mappers.addClassMapping(new BasicClassMapper(
	    Entry.class.getName(),
	    createURI(CViT.Types.ENTRY),
	    "id",
	    Collections.<AttributeMapper>emptySet())) ;
	
	mappers.addClassMapping(new BasicClassMapper(
	    dataClassification, 
	    ComputationalModel.class.getName(), 
	    createURI(CMEF.Types.MODEL), 
	    "id", 
	    getModelAttributes())) ;
	
	mappers.addClassMapping(new BasicClassMapper(
	    dataClassification,
	    ComputationJob.class.getName(),
	    createURI(CMEF.Types.JOB),
	    "id",
	    getJobAttributes()));
	
	Set<URI> types = new LinkedHashSet<URI>() ;
	types.add(createURI(CMEF.Types.FILE)) ;
	types.add(createURI(CViT.Types.FILE)) ;
	mappers.addClassMapping(new BasicClassMapper(
	    File.class.getName(), 
	    createURI(CMEF.Types.FILE),
	    types, 
	    "id", 
	    getFileAttributes())) ;
	
	mappers.addClassMapping(new BasicClassMapper(
	    ComputingPlatform.class.getName(), 
	    createURI(CMEF.Types.COMPUTITNG_PLATFORM), 
	    "id", 
	    getComputingPlatformAttributes())) ;
	
	mappers.addClassMapping(new BasicClassMapper(
	    ProgrammingPlatform.class.getName(), 
	    createURI(CMEF.Types.PROGRAMMING_PLATFORM), 
	    "id", 
	    getProgrammingPlatformAttributes())) ;
	
	mappers.addClassMapping(new BasicClassMapper(
	    Parameter.class.getName(), 
	    createURI(CMEF.Types.PARAMETER), 
	    "id", 
	    getParameterAttributes())) ;
	
	mappers.addClassMapping(new BasicClassMapper(
	    ParameterValue.class.getName(), 
	    createURI(CMEF.Types.PARAMETER_VALUE), 
	    "id", 
	    getParameterValueAttributes())) ;
    }

    private static void addAssociationMappings(BasicMappers mappings) {
	mappings.putAssociationMapping(new BasicAssociationMapper(
	    mappings.getClassMapper(Entry.class.getName()),
	    "data", 
	    mappings.getClassMapper(ComputationalModel.class.getName()), 
	    createURI(CMEF.ObjectProperties.MODELS))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
	    mappings.getClassMapper(ComputationalModel.class.getName()), 
	    "entry", 
	    mappings.getClassMapper(Entry.class.getName()), 
	    createURI(CMEF.ObjectProperties.MODELS), 
	    true)) ;
	    
	mappings.putAssociationMapping(new BasicAssociationMapper(
	    mappings.getClassMapper(Entry.class.getName()),
	    "data", 
	    mappings.getClassMapper(ComputationJob.class.getName()), 
	    createURI(CMEF.ObjectProperties.JOBS))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
	    mappings.getClassMapper(ComputationJob.class.getName()), 
	    "entry", 
	    mappings.getClassMapper(Entry.class.getName()), 
	    createURI(CMEF.ObjectProperties.JOBS), 
	    true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationalModel.class.getName()),
		"job", 
		mappings.getClassMapper(ComputationJob.class.getName()), 
		createURI(CMEF.ObjectProperties.JOB))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationJob.class.getName()),
		"model", 
		mappings.getClassMapper(ComputationalModel.class.getName()), 
		createURI(CMEF.ObjectProperties.JOB),
		true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationalModel.class.getName()),
		"platforms", 
		mappings.getClassMapper(ComputingPlatform.class.getName()), 
		createURI(CMEF.ObjectProperties.COMPUTING_PLATFORM))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputingPlatform.class.getName()),
		"model", 
		mappings.getClassMapper(ComputationalModel.class.getName()), 
		createURI(CMEF.ObjectProperties.COMPUTING_PLATFORM),
		true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationalModel.class.getName()),
		"program", 
		mappings.getClassMapper(ProgrammingPlatform.class.getName()), 
		createURI(CMEF.ObjectProperties.PROGRAMMING_PLATFORM))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ProgrammingPlatform.class.getName()),
		"model", 
		mappings.getClassMapper(ComputationalModel.class.getName()), 
		createURI(CMEF.ObjectProperties.PROGRAMMING_PLATFORM),
		true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationalModel.class.getName()),
		"parameters", 
		mappings.getClassMapper(Parameter.class.getName()), 
		createURI(CMEF.ObjectProperties.PARAMETER))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Parameter.class.getName()),
		"model", 
		mappings.getClassMapper(ComputationalModel.class.getName()), 
		createURI(CMEF.ObjectProperties.PARAMETER),
		true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationalModel.class.getName()),
		"document", 
		mappings.getClassMapper(File.class.getName()), 
		createURI(CMEF.ObjectProperties.DOCUMENT))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(File.class.getName()),
		"documentModel", 
		mappings.getClassMapper(ComputationalModel.class.getName()), 
		createURI(CMEF.ObjectProperties.DOCUMENT),
		true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationalModel.class.getName()),
		"files", 
		mappings.getClassMapper(File.class.getName()), 
		createURI(CMEF.ObjectProperties.FILE))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(File.class.getName()),
		"fileModel", 
		mappings.getClassMapper(ComputationalModel.class.getName()), 
		createURI(CMEF.ObjectProperties.FILE),
		true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationJob.class.getName()),
		"resultFiles", 
		mappings.getClassMapper(File.class.getName()), 
		createURI(CMEF.ObjectProperties.RESULT))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(File.class.getName()),
		"job", 
		mappings.getClassMapper(ComputationJob.class.getName()), 
		createURI(CMEF.ObjectProperties.RESULT),
		true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ComputationJob.class.getName()),
		"parameterValues", 
		mappings.getClassMapper(ParameterValue.class.getName()), 
		createURI(CMEF.ObjectProperties.PARAMETER_VALUE))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ParameterValue.class.getName()),
		"job", 
		mappings.getClassMapper(ComputationJob.class.getName()), 
		createURI(CMEF.ObjectProperties.PARAMETER_VALUE),
		true)) ;
	
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(ParameterValue.class.getName()),
		"parameter", 
		mappings.getClassMapper(Parameter.class.getName()), 
		createURI(CMEF.ObjectProperties.PARAMETER))) ;
	mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Parameter.class.getName()),
		"value", 
		mappings.getClassMapper(ParameterValue.class.getName()), 
		createURI(CMEF.ObjectProperties.PARAMETER),
		true)) ;
    }
    
    private static Set<AttributeMapper> getDataClassificationAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>() ;
	result.add(attribute("title", createURI(DC11.TITLE), false, false)) ;
	result.add(attribute("description", createURI(DC11.DESCRIPTION), true, false)) ;
	result.add(attribute("source", createURI(DC11.SOURCE), true, false)) ;
	result.add(attribute("comment", createURI(CViT.DatatypeProperties.COMMENT), true, false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getModelAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>(2) ;
	result.add(attribute("commandLine", createURI(CMEF.DatatypeProperties.COMMAND_LINE), false, false)) ;
	result.add(attribute("version", createURI(CMEF.DatatypeProperties.VERSION), true, false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getJobAttributes() {
	DatatypeTranslator<Object> dateTranslator = (DatatypeTranslator) new DateDatatypeTranslator(factory) ;
	DatatypeTranslator<Object> intTranslator = (DatatypeTranslator) new IntegerDatatypeTranslator(factory) ;
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>(5) ;
	result.add(attribute("dateSubmitted", createURI(CMEF.DatatypeProperties.DATE_SUBMITTED), dateTranslator, true, false)) ;
	result.add(attribute("dateCompleted", createURI(CMEF.DatatypeProperties.DATE_COMPLETED), dateTranslator, true, false)) ;
	result.add(attribute("jobNumber", createURI(CMEF.DatatypeProperties.JOB_NUMBER), intTranslator, true, false)) ;
	result.add(attribute("jobStatus", createURI(CMEF.DatatypeProperties.JOB_STATUS), true, false)) ;
	result.add(attribute("userId", createURI(CMEF.DatatypeProperties.USER_ID), true, false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getFileAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>(2) ;
	result.add(attribute("name", createURI(DC11.TITLE), false, false)) ;
	result.add(attribute("source", createURI(DC11.SOURCE), false, false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getComputingPlatformAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>(2) ;
	result.add(attribute("operatingSystemType", createURI(CMEF.DatatypeProperties.OPSYS_TYPE), false, false)) ;
	result.add(attribute("processorArchitecture", createURI(CMEF.DatatypeProperties.PROC_TYPE), false, false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getProgrammingPlatformAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>(2) ;
	result.add(attribute("languageType", createURI(CMEF.DatatypeProperties.LANG_TYPE), false, false)) ;
	result.add(attribute("languageVersion", createURI(CMEF.DatatypeProperties.LANG_VER), true, false)) ;
	return result ;
    }

    private static Set<AttributeMapper> getParameterAttributes() {
	DatatypeTranslator<Object> boolTranslator = (DatatypeTranslator) new BooleanDatatypeTranslator(factory) ;
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>(8) ;
	result.add(attribute("name", createURI(DC11.TITLE), false, false)) ;
	result.add(attribute("description", createURI(DC11.DESCRIPTION), true, false)) ;
	result.add(attribute("dataType", createURI(CMEF.DatatypeProperties.DATA_TYPE), false, false)) ;
	result.add(attribute("prefix", createURI(CMEF.DatatypeProperties.PREFIX), true, false)) ;
	result.add(attribute("choices", createURI(CMEF.DatatypeProperties.CHOICE), true, true)) ;
	result.add(attribute("defaultValue", createURI(CMEF.DatatypeProperties.DEFAULT_VALUE), true, false)) ;
	result.add(attribute("isOptional", createURI(CMEF.DatatypeProperties.OPTIONAL), boolTranslator, true, false)) ;
	result.add(attribute("isFile", createURI(CMEF.DatatypeProperties.IS_FILE), boolTranslator, true, false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getParameterValueAttributes() {
	return Collections.<AttributeMapper>singleton(attribute("value", createURI(CMEF.DatatypeProperties.PARAM_VALUE), false, false)) ;
    }
    
    private static URI createURI(String uri) {
	try {
	    return new URI(uri) ;
	} catch (URISyntaxException e) {
	    throw new RuntimeException("URI syntax exception: " + uri + ".", e) ;
	}
    }
}
