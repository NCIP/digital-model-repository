/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.cmef.vocabulary;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CViT {
    private CViT() {}

    public static final String NAMESPACE = "urn:lsid:telar.cambridgesemantics.com:cvit#" ;
    
    private static String uri(String local) {
	return NAMESPACE + local ;
    }
    
    public static final String ROLE = uri("role") ;
    public static final String PRINCIPAL_INVESTIGATOR_ROLE = uri("principal_investigator_role") ;
    public static final String LICENSING_OFFICER_ROLE = uri("licensing_officer_role") ;
    
    public static class Types {
	private Types() {}
	public static final String ENTRY = uri("Entry") ;
	public static final String ENTRY_CATEGORY = uri("EntryClassification") ;
	public static final String ENTRY_TYPE = uri("EntryType") ;
	public static final String ORGANIZATION = uri("Organization") ;
	public static final String FUNDING_SOURCE = uri("FundingSource") ;
	public static final String REFERENCE = uri("Reference") ;
	public static final String PAPER = uri("Paper") ;
	public static final String BOOK = uri("Book") ;
	public static final String REVIEW = uri("Review") ;
	public static final String DATA = uri("Data") ;
	public static final String ALGORITHM = uri("Algorithm") ;
	public static final String CODE = uri("Code") ;
	public static final String EXPERIMENTAL_DATA = uri("ExperimentalData") ;
	public static final String IMAGE = uri("Image") ;
	public static final String PARAMETERS = uri("Parameters") ;
	public static final String MOVIE = uri("Movie") ;
	public static final String PERSON = uri("Person") ;
	public static final String FILE = uri("File") ;
    }
    
    public static class ObjectProperties {
	private ObjectProperties() {}
	public static final String FUNDING_ORGANIZATION = uri("fundingOrganization") ;
	public static final String REFERENCES = uri("reference") ;
	public static final String DATA = uri("data") ;
	public static final String PRINCIPLE_INVESTIGATOR = uri("PrincipalInvestigator") ;
	public static final String ORGANIZATION = uri("Organization") ;
	public static final String ENTRY_TYPE = uri("entryType") ;
	public static final String ENTRY_CLASSIFICATION = uri("entryClassification") ;
    }
    
    public static class DatatypeProperties {
	private DatatypeProperties() {}
	public static final String ABSTRACT = uri("abstract") ;
	public static final String CONCEPT = uri("concept") ;
	public static final String HYPOTHESIS = uri("hypothesis") ;
	public static final String CONCLUSION = uri("conclusion") ;
	public static final String NOTE = uri("note") ;
	public static final String KEYWORD = uri("keyword") ;
	public static final String GEO_CODE = uri("geoCode") ;
	public static final String RESEARCH = uri("research") ;
	public static final String POSITION = uri("position") ;
	public static final String FAX = uri("fax") ;
	public static final String ADDRESS = uri("address") ;
	public static final String SENIORITY = uri("seniority") ;
	public static final String GROUP = uri("group") ;
	public static final String COMMENT = uri("comment") ;
	public static final String RESEARCH_INTEREST = uri("researchInterest") ;
	public static final String LOGIN_DISABLED = uri("loginDisabled") ;
    }
    
    public static class EntryTypes {
	private EntryTypes() {}
	public static final String EXPERIMENT = uri("CVITExperiment") ;
	public static final String COMPUTATION = uri("Computation") ;
	public static final String INVITRO = uri("Invitro") ;
	public static final String INVIVO = uri("Invivo") ;
	public static final String CLINICAL = uri("Clinical") ;
	public static final Set<String> ALL_ENTRY_TYPES ;
	static {
	    Set<String> result = new HashSet<String>() ;
	    result.add(EXPERIMENT) ;
	    result.add(COMPUTATION) ;
	    result.add(INVITRO) ;
	    result.add(INVIVO) ;
	    result.add(CLINICAL) ;
	    ALL_ENTRY_TYPES = Collections.unmodifiableSet(result) ;
	}
    }
    
    public static class Classifications {
	private Classifications() {} 
	public static final String GENETICS = uri("Genetics") ;
	public static final String CELL_CYCLE = uri("CellCycle") ;
	public static final String SIGNALING_PATHWAYS = uri("SignalingPathways") ;
	public static final String MOTILITY = uri("Motility") ;
	public static final String PROLIFERATION = uri("Proliferation") ;
	public static final String METASTASIS = uri("Metastasis") ;
	public static final String IMMUNOLOGY = uri("Immunology") ;
	public static final String ANGIOGENESIS = uri("Angiogenesis") ;
	public static final String STATISTICS = uri("Statistics") ;
	public static final String CONTROL = uri("Control") ;
	public static final String MICROENVIRONMENT = uri("Microenvironment") ;
	public static final String DISCRETE_MODELING = uri("Discrete") ;
	public static final String CONTINUUM_MODELING = uri("Continuum") ;
	public static final String HYBRID_MODELING = uri("Hybrid") ;
	public static final String TREATMENT = uri("Treatment") ;
	public static final String CHEMOTHERAPY = uri("Chemotherapy") ;
	public static final String RADIOTHERAPY = uri("Radiotherapy") ;
	public static final String OTHER = uri("Other") ;
	public static final Set<String> ALL_CLASSIFICATIONS ;
	static {
	    Set<String> result = new HashSet<String>() ;
	    result.add(GENETICS) ;
	    result.add(CELL_CYCLE) ;
	    result.add(SIGNALING_PATHWAYS) ;
	    result.add(MOTILITY) ;
	    result.add(PROLIFERATION) ;
	    result.add(METASTASIS) ;
	    result.add(IMMUNOLOGY) ;
	    result.add(ANGIOGENESIS) ;
	    result.add(STATISTICS) ;
	    result.add(CONTROL) ;
	    result.add(MICROENVIRONMENT) ;
	    result.add(DISCRETE_MODELING) ;
	    result.add(CONTINUUM_MODELING) ;
	    result.add(HYBRID_MODELING) ;
	    result.add(TREATMENT) ;
	    result.add(CHEMOTHERAPY) ;
	    result.add(RADIOTHERAPY) ;
	    result.add(OTHER) ;
	    ALL_CLASSIFICATIONS = Collections.unmodifiableSet(result) ;
	}
    }
}
