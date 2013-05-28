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
	* An institution.  Most of these will be colleges, universities, and research institutes.  Each user in the repository is affiliated with one and only one organization.  Each organization has one or more licensing officers to approve of user's licensing requests.	**/
public class Organization  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* The identifier of the Organization.	**/
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
	* Name of the organization	**/
	public String name;
	/**
	* Retreives the value of name attribute
	* @return name
	**/

	public String getName(){
		return name;
	}

	/**
	* Sets the value of name attribue
	**/

	public void setName(String name){
		this.name = name;
	}
	
		/**
	* Brief description of the organization	**/
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
	* Organization's homepage	**/
	public String website;
	/**
	* Retreives the value of website attribute
	* @return website
	**/

	public String getWebsite(){
		return website;
	}

	/**
	* Sets the value of website attribue
	**/

	public void setWebsite(String website){
		this.website = website;
	}
	
		/**
	* Geographical location of organization used in cvit.org/mashup	**/
	public String geoCode;
	/**
	* Retreives the value of geoCode attribute
	* @return geoCode
	**/

	public String getGeoCode(){
		return geoCode;
	}

	/**
	* Sets the value of geoCode attribue
	**/

	public void setGeoCode(String geoCode){
		this.geoCode = geoCode;
	}
	
	/**
	* An associated org.cvit.cabig.dmr.domain.Entry object's collection 
	**/
			
	private Collection<Entry> fundedEntries;
	/**
	* Retreives the value of fundedEntries attribue
	* @return fundedEntries
	**/

	public Collection<Entry> getFundedEntries(){
		return fundedEntries;
	}

	/**
	* Sets the value of fundedEntries attribue
	**/

	public void setFundedEntries(Collection<Entry> fundedEntries){
		this.fundedEntries = fundedEntries;
	}
		
	/**
	* An associated org.cvit.cabig.dmr.domain.Person object's collection 
	**/
			
	private Collection<Person> members;
	/**
	* Retreives the value of members attribue
	* @return members
	**/

	public Collection<Person> getMembers(){
		return members;
	}

	/**
	* Sets the value of members attribue
	**/

	public void setMembers(Collection<Person> members){
		this.members = members;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof Organization) 
		{
			Organization c =(Organization)obj; 			 
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