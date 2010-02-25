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
