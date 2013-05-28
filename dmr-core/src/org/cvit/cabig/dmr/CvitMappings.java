/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr;

import static com.infotechsoft.rdf.mapping.mappers.OldAttributeMapper.attribute;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.cvit.cabig.dmr.domain.Algorithm;
import org.cvit.cabig.dmr.domain.Book;
import org.cvit.cabig.dmr.domain.DataClassification;
import org.cvit.cabig.dmr.domain.Entry;
import org.cvit.cabig.dmr.domain.EntryCategory;
import org.cvit.cabig.dmr.domain.EntryType;
import org.cvit.cabig.dmr.domain.ExperimentalData;
import org.cvit.cabig.dmr.domain.Image;
import org.cvit.cabig.dmr.domain.Movie;
import org.cvit.cabig.dmr.domain.Organization;
import org.cvit.cabig.dmr.domain.Paper;
import org.cvit.cabig.dmr.domain.Parameters;
import org.cvit.cabig.dmr.domain.Person;
import org.cvit.cabig.dmr.domain.Reference;
import org.cvit.cabig.dmr.domain.Review;
import org.cvit.cabig.dmr.domain.SourceCode;
import org.cvit.cabig.dmr.vocabulary.AnzoPredicates;
import org.cvit.cabig.dmr.vocabulary.CViT;
import org.cvit.cabig.dmr.vocabulary.DC11;
import org.cvit.cabig.dmr.vocabulary.FOAF;
import org.cvit.cabig.dmr.vocabulary.RBAC;

import com.infotechsoft.rdf.mapping.BasicMappers;
import com.infotechsoft.rdf.mapping.Mappers;
import com.infotechsoft.rdf.mapping.mappers.AttributeMapper;
import com.infotechsoft.rdf.mapping.mappers.BasicAssociationMapper;
import com.infotechsoft.rdf.mapping.mappers.BasicClassMapper;

public class CvitMappings {
    
    public static Mappers getMappers() {
	BasicMappers result = new BasicMappers() ;
	addClassMappings(result) ;
	addAssociationMappings(result) ;
	return result ;
    }
    
    private static void addClassMappings(BasicMappers mappers) {
	try {
	    mappers.addClassMapping(new BasicClassMapper(
		Entry.class.getName(), 
		new URI(CViT.Types.ENTRY),
		"id", 
		getEntryAttributes())) ;
	    mappers.addClassMapping(new BasicClassMapper(
		EntryCategory.class.getName(),
		new URI(CViT.Types.ENTRY_CATEGORY),
		"id",
		getEntryCategoryEntryTypeAttributes())) ;
	    mappers.addClassMapping(new BasicClassMapper(
		EntryType.class.getName(),
		new URI(CViT.Types.ENTRY_TYPE),
		"id",
		getEntryCategoryEntryTypeAttributes())) ;
	    Set<URI> organizationTypes = new HashSet<URI>() ;
	    organizationTypes.add(new URI(CViT.Types.ORGANIZATION)) ;
	    organizationTypes.add(new URI(CViT.Types.FUNDING_SOURCE)) ;
	    mappers.addClassMapping(new BasicClassMapper(
		Organization.class.getName(),
		new URI(CViT.Types.ORGANIZATION),
		organizationTypes,
		"id",
		getOrganizationAttributes())) ;
	    BasicClassMapper reference = new BasicClassMapper(
	                Reference.class.getName(), 
	                new URI(CViT.Types.REFERENCE),
	                "id",
	                getReferenceAttributes()) ;
	    mappers.addClassMapping(reference) ;
	    mappers.addClassMapping(new BasicClassMapper(
		reference,
	        Paper.class.getName(), 
		new URI(CViT.Types.PAPER),
		"id",
		emptyAttrSet())) ;
	    mappers.addClassMapping(new BasicClassMapper(
		reference,
	        Book.class.getName(), 
		new URI(CViT.Types.BOOK),
		"id",
		emptyAttrSet())) ;
	    mappers.addClassMapping(new BasicClassMapper(
	        reference,
		Review.class.getName(), 
		new URI(CViT.Types.REVIEW),
		"id",
		emptyAttrSet())) ;
	    BasicClassMapper dataClassification = new BasicClassMapper(
		DataClassification.class.getName(),
		new URI(CViT.Types.DATA),
		"id",
		getDataClassificationAttributes()) ;
	    mappers.addClassMapping(dataClassification) ;
	    mappers.addClassMapping(new BasicClassMapper(
	        dataClassification,
		Algorithm.class.getName(), 
		new URI(CViT.Types.ALGORITHM),
		"id",
		emptyAttrSet())) ;
	    mappers.addClassMapping(new BasicClassMapper(
	        dataClassification,
		SourceCode.class.getName(),
		new URI(CViT.Types.CODE), 
		"id",
		emptyAttrSet())) ;
	    mappers.addClassMapping(new BasicClassMapper(
	        dataClassification,
		ExperimentalData.class.getName(), 
		new URI(CViT.Types.EXPERIMENTAL_DATA), 
		"id",
		emptyAttrSet())) ;
	    mappers.addClassMapping(new BasicClassMapper(
	        dataClassification,
		Image.class.getName(), 
		new URI(CViT.Types.IMAGE),
		"id",
		emptyAttrSet())) ;
	    mappers.addClassMapping(new BasicClassMapper(
	        dataClassification,
		Parameters.class.getName(), 
		new URI(CViT.Types.PARAMETERS),
		"id",
		emptyAttrSet())) ;
	    mappers.addClassMapping(new BasicClassMapper(
	        dataClassification,
		Movie.class.getName(), 
		new URI(CViT.Types.MOVIE),
		"id",
		emptyAttrSet())) ;
	    Set<URI> personTypes = new HashSet<URI>() ;
	    personTypes.add(new URI(CViT.Types.PERSON)) ;
	    personTypes.add(new URI(RBAC.USER)) ;
	    mappers.addClassMapping(new BasicClassMapper(
		Person.class.getName(), 
		new URI(CViT.Types.PERSON),
		personTypes,
		"id",
		getPersonAttributes())) ;
	} catch (URISyntaxException e) {
	    throw new Defect("Check syntax of URIs.", e) ;
	}
    }
    
    private static void addAssociationMappings(BasicMappers mappings) {
	try {
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Entry.class.getName()),
		"fundingOrganization",
		mappings.getClassMapper(Organization.class.getName()),
		new URI(CViT.ObjectProperties.FUNDING_ORGANIZATION))) ;
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Organization.class.getName()),
		"fundedEntries",
		mappings.getClassMapper(Entry.class.getName()),
		new URI(CViT.ObjectProperties.FUNDING_ORGANIZATION),
		true)) ;
	    
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Entry.class.getName()),
		"references",
		mappings.getClassMapper(Reference.class.getName()),
		new URI(CViT.ObjectProperties.REFERENCES))) ;
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Reference.class.getName()),
		"entries",
		mappings.getClassMapper(Entry.class.getName()),
		new URI(CViT.ObjectProperties.REFERENCES),
		true)) ;
	    
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Entry.class.getName()),
		"data",
		mappings.getClassMapper(DataClassification.class.getName()),
		new URI(CViT.ObjectProperties.DATA))) ;
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(DataClassification.class.getName()),
		"entry",
		mappings.getClassMapper(Entry.class.getName()),
		new URI(CViT.ObjectProperties.DATA),
		true)) ;
	    
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Entry.class.getName()),
		"contributors",
		mappings.getClassMapper(Person.class.getName()),
		createURI(DC11.CONTRIBUTOR))) ;
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Person.class.getName()),
		"entries",
		mappings.getClassMapper(Entry.class.getName()),
		createURI(DC11.CONTRIBUTOR),
		true)) ;
	    
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Person.class.getName()),
		"organization",
		mappings.getClassMapper(Organization.class.getName()),
		new URI(CViT.ObjectProperties.ORGANIZATION))) ;
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Organization.class.getName()),
		"members",
		mappings.getClassMapper(Person.class.getName()),
		new URI(CViT.ObjectProperties.ORGANIZATION),
		true)) ;
	    
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Entry.class.getName()),
		"categories",
		mappings.getClassMapper(EntryCategory.class.getName()),
		new URI(CViT.ObjectProperties.ENTRY_CLASSIFICATION))) ;
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(EntryCategory.class.getName()),
		"entries",
		mappings.getClassMapper(Entry.class.getName()),
		new URI(CViT.ObjectProperties.ENTRY_CLASSIFICATION),
		true)) ;
	    
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(Entry.class.getName()),
		"types",
		mappings.getClassMapper(EntryType.class.getName()),
		new URI(CViT.ObjectProperties.ENTRY_TYPE))) ;
	    mappings.putAssociationMapping(new BasicAssociationMapper(
		mappings.getClassMapper(EntryType.class.getName()),
		"entries",
		mappings.getClassMapper(Entry.class.getName()),
		new URI(CViT.ObjectProperties.ENTRY_TYPE),
		true)) ;
	} catch (URISyntaxException e) {
	    throw new Defect("Check syntax of URIs.", e) ;
	}
    }
    
    private static Set<AttributeMapper> getEntryAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>() ;
	result.add(attribute(
	    "title",
	    createURI(DC11.TITLE),
	    false,
            false)) ;
	result.add(attribute(
	    "description",
	    createURI(DC11.DESCRIPTION),
	    true,
            false)) ;
	result.add(attribute(
	    "abstractText",
	    createURI(CViT.DatatypeProperties.ABSTRACT),
	    true,
            false)) ;
	result.add(attribute(
	    "concept",
	    createURI(CViT.DatatypeProperties.CONCEPT),
	    true,
            false)) ;
	result.add(attribute(
	    "hypothesis",
	    createURI(CViT.DatatypeProperties.HYPOTHESIS),
	    true,
            false)) ; 
	result.add(attribute(
	    "conclusion",
	    createURI(CViT.DatatypeProperties.CONCLUSION),
	    true,
            false)) ;
	result.add(attribute(
	    "note",
	    createURI(CViT.DatatypeProperties.NOTE),
	    true,
            false)) ;
	result.add(attribute(
	    "keywords",
	    createURI(CViT.DatatypeProperties.KEYWORD),
	    true,
	    true)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getOrganizationAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>() ;
	result.add(attribute(
	    "name",
	    createURI(DC11.TITLE),
	    false,
            false)) ;
	result.add(attribute(
	    "description",
	    createURI(DC11.DESCRIPTION),
	    true,
            false)) ;
	result.add(attribute(
	    "website",
	    createURI(DC11.SOURCE),
	    true,
            false)) ;
	result.add(attribute(
	    "geoCode",
	    createURI(CViT.DatatypeProperties.GEO_CODE),
	    true,
            false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getPersonAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>() ;
	result.add(attribute(
	    "userId",
	    createURI(AnzoPredicates.USER_ID),
	    true,
	    false)) ;
	result.add(attribute(
	    "firstName",
	    createURI(FOAF.FIRST_NAME),
	    true,
	    false)) ;
	result.add(attribute(
	    "lastName",
	    createURI(FOAF.SURNAME),
	    true,
	    false)) ;
	result.add(attribute(
	    "phone",
	    createURI(FOAF.PHONE),
	    true,
	    false)) ;
	result.add(attribute(
	    "emailAddress",
	    createURI(FOAF.MBOX),
	    true,
	    false)) ;
	result.add(attribute(
	    "website",
	    createURI(DC11.SOURCE),
	    true,
	    false)) ;
	result.add(attribute(
	    "depiction",
	    createURI(FOAF.DEPICTION),
	    true,
	    false)) ;
	result.add(attribute(
	    "research",
	    createURI(CViT.DatatypeProperties.RESEARCH),
	    true,
	    false)) ;
	result.add(attribute(
	    "geoCode",
	    createURI(CViT.DatatypeProperties.GEO_CODE),
	    true,
	    false)) ;
	result.add(attribute(
	    "position",
	    createURI(CViT.DatatypeProperties.POSITION),
	    true,
	    false)) ;
	result.add(attribute(
	    "fax",
	    createURI(CViT.DatatypeProperties.FAX),
	    true,
	    false)) ;
	result.add(attribute(
	    "address",
	    createURI(CViT.DatatypeProperties.ADDRESS),
	    true,
	    false)) ;
	result.add(attribute(
	    "researchInterest",
	    createURI(CViT.DatatypeProperties.RESEARCH_INTEREST),
	    true,
	    false)) ;
	result.add(attribute(
	    "title",
	    createURI(FOAF.TITLE),
	    true,
	    false)) ;
	result.add(attribute(
	    "seniority",
	    createURI(CViT.DatatypeProperties.SENIORITY),
	    true,
	    false)) ;
	result.add(attribute(
	    "group",
	    createURI(CViT.DatatypeProperties.GROUP),
	    true,
	    false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getReferenceAttributes() {
	return getReferenceDataClassificationAttributes() ;
    }
    
    private static Set<AttributeMapper> getDataClassificationAttributes() {
	return getReferenceDataClassificationAttributes() ;
    }
    
    private static Set<AttributeMapper> getReferenceDataClassificationAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>() ;
	result.add(attribute(
	    "title",
	    createURI(DC11.TITLE),
	    false,
	    false)) ;
	result.add(attribute(
	    "description",
	    createURI(DC11.DESCRIPTION),
	    true,
	    false)) ;
	result.add(attribute(
	    "source",
	    createURI(DC11.SOURCE),
	    true,
	    false)) ;
	result.add(attribute(
	    "comment",
	    createURI(CViT.DatatypeProperties.COMMENT),
	    true,
	    false)) ;
	return result ;
    }
    
    private static Set<AttributeMapper> getEntryCategoryEntryTypeAttributes() {
	Set<AttributeMapper> result = new LinkedHashSet<AttributeMapper>() ;
	result.add(attribute(
	    "name",
	    createURI(DC11.TITLE),
	    true,
	    false)) ;
	return result;
    }
    
    private static URI createURI(String uri) {
	try {
	    return new URI(uri) ;
	} catch (URISyntaxException e) {
	    throw new Defect("Check syntax of URIs.", e) ;
	}
    }
    
    @SuppressWarnings("unchecked")
    private static Set<AttributeMapper> emptyAttrSet() {
        return Collections.emptySet() ;
    }
}
