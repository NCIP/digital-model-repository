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
	* User profile	**/
public class Person  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* The identifier of the Person.	**/
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
	* The persons DMR user id.	**/
	public String userId;
	/**
	* Retreives the value of userId attribute
	* @return userId
	**/

	public String getUserId(){
		return userId;
	}

	/**
	* Sets the value of userId attribue
	**/

	public void setUserId(String userId){
		this.userId = userId;
	}
	
		/**
	* User's first name	**/
	public String firstName;
	/**
	* Retreives the value of firstName attribute
	* @return firstName
	**/

	public String getFirstName(){
		return firstName;
	}

	/**
	* Sets the value of firstName attribue
	**/

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
		/**
	* User's last name	**/
	public String lastName;
	/**
	* Retreives the value of lastName attribute
	* @return lastName
	**/

	public String getLastName(){
		return lastName;
	}

	/**
	* Sets the value of lastName attribue
	**/

	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
		/**
	* Phone number	**/
	public String phone;
	/**
	* Retreives the value of phone attribute
	* @return phone
	**/

	public String getPhone(){
		return phone;
	}

	/**
	* Sets the value of phone attribue
	**/

	public void setPhone(String phone){
		this.phone = phone;
	}
	
		/**
	* Email address	**/
	public String emailAddress;
	/**
	* Retreives the value of emailAddress attribute
	* @return emailAddress
	**/

	public String getEmailAddress(){
		return emailAddress;
	}

	/**
	* Sets the value of emailAddress attribue
	**/

	public void setEmailAddress(String emailAddress){
		this.emailAddress = emailAddress;
	}
	
		/**
	* User's homepage	**/
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
	* User's icon/picture	**/
	public String depiction;
	/**
	* Retreives the value of depiction attribute
	* @return depiction
	**/

	public String getDepiction(){
		return depiction;
	}

	/**
	* Sets the value of depiction attribue
	**/

	public void setDepiction(String depiction){
		this.depiction = depiction;
	}
	
		/**
	* Type of research being done by user. (used in cvit.org/mashup)	**/
	public String research;
	/**
	* Retreives the value of research attribute
	* @return research
	**/

	public String getResearch(){
		return research;
	}

	/**
	* Sets the value of research attribue
	**/

	public void setResearch(String research){
		this.research = research;
	}
	
		/**
	* Geographical location of person on cvit.org/mashup	**/
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
	* Job title	**/
	public String position;
	/**
	* Retreives the value of position attribute
	* @return position
	**/

	public String getPosition(){
		return position;
	}

	/**
	* Sets the value of position attribue
	**/

	public void setPosition(String position){
		this.position = position;
	}
	
		/**
	* Fax number	**/
	public String fax;
	/**
	* Retreives the value of fax attribute
	* @return fax
	**/

	public String getFax(){
		return fax;
	}

	/**
	* Sets the value of fax attribue
	**/

	public void setFax(String fax){
		this.fax = fax;
	}
	
		/**
	* Current mailing address	**/
	public String address;
	/**
	* Retreives the value of address attribute
	* @return address
	**/

	public String getAddress(){
		return address;
	}

	/**
	* Sets the value of address attribue
	**/

	public void setAddress(String address){
		this.address = address;
	}
	
		/**
	* Several paragraphs describing current research interests	**/
	public String researchInterest;
	/**
	* Retreives the value of researchInterest attribute
	* @return researchInterest
	**/

	public String getResearchInterest(){
		return researchInterest;
	}

	/**
	* Sets the value of researchInterest attribue
	**/

	public void setResearchInterest(String researchInterest){
		this.researchInterest = researchInterest;
	}
	
		/**
	* Suffix titles.  Usually Ph.D. or M.D..	**/
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
	* Tracks a user's expertise level. One of Faculty | Postdoc | GradStudent | Other	**/
	public String seniority;
	/**
	* Retreives the value of seniority attribute
	* @return seniority
	**/

	public String getSeniority(){
		return seniority;
	}

	/**
	* Sets the value of seniority attribue
	**/

	public void setSeniority(String seniority){
		this.seniority = seniority;
	}
	
		/**
	* Tracks groups of cvit users for cvit.org/teampages. One of Main | AdvisoryBoard | NCI | ICBP | Unlisted	**/
	public String group;
	/**
	* Retreives the value of group attribute
	* @return group
	**/

	public String getGroup(){
		return group;
	}

	/**
	* Sets the value of group attribue
	**/

	public void setGroup(String group){
		this.group = group;
	}
	
	/**
	* An associated org.cvit.cabig.dmr.domain.Organization object
	**/
			
	private Organization organization;
	/**
	* Retreives the value of organization attribue
	* @return organization
	**/
	
	public Organization getOrganization(){
		return organization;
	}
	/**
	* Sets the value of organization attribue
	**/

	public void setOrganization(Organization organization){
		this.organization = organization;
	}
			
	/**
	* An associated org.cvit.cabig.dmr.domain.Entry object's collection 
	**/
			
	private Collection<Entry> entries;
	/**
	* Retreives the value of entries attribue
	* @return entries
	**/

	public Collection<Entry> getEntries(){
		return entries;
	}

	/**
	* Sets the value of entries attribue
	**/

	public void setEntries(Collection<Entry> entries){
		this.entries = entries;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof Person) 
		{
			Person c =(Person)obj; 			 
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