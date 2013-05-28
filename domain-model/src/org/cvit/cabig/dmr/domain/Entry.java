/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.domain;

import java.util.Collection;
import java.io.Serializable;
	/**
	* An entry in the repository contains relevant, uploaded information regarding a project. Entries can only be created by Principal Investigators (PIs). With a Licensing Officer's (LO's) approval a PI can have his entry published to other users of the repository. Designated contributors can annotate entries with metadata.	**/
public class Entry  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* The identifier of the Entry.	**/
	public String id;
	/**
	* Retreives the value of id attribute
	* @return id
	**/

	public String getId(){
		return id;
	}

	/**
	* Sets the value of id attribue
	**/

	public void setId(String id){
		this.id = id;
	}
	
		/**
	* A name to identify the entry.	**/
	public String title;
	/**
	* Retreives the value of title attribute
	* @return title
	**/

	public String getTitle(){
		return title;
	}

	/**
	* Sets the value of title attribue
	**/

	public void setTitle(String title){
		this.title = title;
	}
	
		/**
	* A paragraph explaining why the entry exists and what project it contains.  This defines the scope for further data uploaded to the entry.	**/
	public String description;
	/**
	* Retreives the value of description attribute
	* @return description
	**/

	public String getDescription(){
		return description;
	}

	/**
	* Sets the value of description attribue
	**/

	public void setDescription(String description){
		this.description = description;
	}
	
		/**
	* A brief summary of the project's description.	**/
	public String abstractText;
	/**
	* Retreives the value of abstractText attribute
	* @return abstractText
	**/

	public String getAbstractText(){
		return abstractText;
	}

	/**
	* Sets the value of abstractText attribue
	**/

	public void setAbstractText(String abstractText){
		this.abstractText = abstractText;
	}
	
		/**
	* Background and basic idea of this project	**/
	public String concept;
	/**
	* Retreives the value of concept attribute
	* @return concept
	**/

	public String getConcept(){
		return concept;
	}

	/**
	* Sets the value of concept attribue
	**/

	public void setConcept(String concept){
		this.concept = concept;
	}
	
		/**
	* What assumption(s) will be proved by this experiment?	**/
	public String hypothesis;
	/**
	* Retreives the value of hypothesis attribute
	* @return hypothesis
	**/

	public String getHypothesis(){
		return hypothesis;
	}

	/**
	* Sets the value of hypothesis attribue
	**/

	public void setHypothesis(String hypothesis){
		this.hypothesis = hypothesis;
	}
	
		/**
	* The outcome and significance of the project	**/
	public String conclusion;
	/**
	* Retreives the value of conclusion attribute
	* @return conclusion
	**/

	public String getConclusion(){
		return conclusion;
	}

	/**
	* Sets the value of conclusion attribue
	**/

	public void setConclusion(String conclusion){
		this.conclusion = conclusion;
	}
	
		/**
	* Space for adding notes	**/
	public String note;
	/**
	* Retreives the value of note attribute
	* @return note
	**/

	public String getNote(){
		return note;
	}

	/**
	* Sets the value of note attribue
	**/

	public void setNote(String note){
		this.note = note;
	}
	
		/**
	* List of 3-5 searchable terms characterizing the entry.	**/
	public Collection<String> keywords;
	/**
	* Retreives the value of keywords attribute
	* @return keywords
	**/

	public Collection<String> getKeywords(){
		return keywords;
	}

	/**
	* Sets the value of keywords attribue
	**/

	public void setKeywords(Collection<String> keywords){
		this.keywords = keywords;
	}
	
	/**
	* An associated org.cvit.cabig.dmr.domain.Organization object
	**/
			
	private Organization fundingOrganization;
	/**
	* Retreives the value of fundingOrganization attribue
	* @return fundingOrganization
	**/
	
	public Organization getFundingOrganization(){
		return fundingOrganization;
	}
	/**
	* Sets the value of fundingOrganization attribue
	**/

	public void setFundingOrganization(Organization fundingOrganization){
		this.fundingOrganization = fundingOrganization;
	}
			
	/**
	* An associated org.cvit.cabig.dmr.domain.EntryCategory object's collection 
	**/
			
	private Collection<EntryCategory> categories;
	/**
	* Retreives the value of categories attribue
	* @return categories
	**/

	public Collection<EntryCategory> getCategories(){
		return categories;
	}

	/**
	* Sets the value of categories attribue
	**/

	public void setCategories(Collection<EntryCategory> categories){
		this.categories = categories;
	}
		
	/**
	* An associated org.cvit.cabig.dmr.domain.EntryType object's collection 
	**/
			
	private Collection<EntryType> types;
	/**
	* Retreives the value of types attribue
	* @return types
	**/

	public Collection<EntryType> getTypes(){
		return types;
	}

	/**
	* Sets the value of types attribue
	**/

	public void setTypes(Collection<EntryType> types){
		this.types = types;
	}
		
	/**
	* An associated org.cvit.cabig.dmr.domain.Reference object's collection 
	**/
			
	private Collection<Reference> references;
	/**
	* Retreives the value of references attribue
	* @return references
	**/

	public Collection<Reference> getReferences(){
		return references;
	}

	/**
	* Sets the value of references attribue
	**/

	public void setReferences(Collection<Reference> references){
		this.references = references;
	}
		
	/**
	* An associated org.cvit.cabig.dmr.domain.Person object's collection 
	**/
			
	private Collection<Person> contributors;
	/**
	* Retreives the value of contributors attribue
	* @return contributors
	**/

	public Collection<Person> getContributors(){
		return contributors;
	}

	/**
	* Sets the value of contributors attribue
	**/

	public void setContributors(Collection<Person> contributors){
		this.contributors = contributors;
	}
		
	/**
	* An associated org.cvit.cabig.dmr.domain.DataClassification object's collection 
	**/
			
	private Collection<DataClassification> data;
	/**
	* Retreives the value of data attribue
	* @return data
	**/

	public Collection<DataClassification> getData(){
		return data;
	}

	/**
	* Sets the value of data attribue
	**/

	public void setData(Collection<DataClassification> data){
		this.data = data;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof Entry) 
		{
			Entry c =(Entry)obj; 			 
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}
		
	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}
	
}