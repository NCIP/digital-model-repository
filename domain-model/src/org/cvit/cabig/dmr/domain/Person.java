/*
 * caBIG™ Open Source Software License v.2 Digital Model Repository (DMR)
 * 
 * Copyright Notice. Copyright 2008 Massachusetts General Hospital (“caBIG™
 * Participant”). Digital Model Repository (DMR) was created with NCI funding
 * and is part of the caBIG™ initiative. The software subject to this notice and
 * license includes both human readable source code form and machine readable,
 * binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant
 * and You. “You (or “Your”) shall mean a person or an entity, and all other
 * entities that control, are controlled by, or are under common control with
 * the entity. “Control” for purposes of this definition means (i) the direct or
 * indirect power to cause the direction or management of such entity, whether
 * by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of
 * the outstanding shares, or (iii) beneficial ownership of such entity.
 * 
 * License. Provided that You agree to the conditions described below, caBIG™
 * Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caBIG™ Software, including any copyright or patent rights
 * therein, to (i) use, install, disclose, access, operate, execute, reproduce,
 * copy, modify, translate, market, publicly display, publicly perform, and
 * prepare derivative works of the caBIG™ Software in any manner and for any
 * purpose, and to have or permit others to do so; (ii) make, have made, use,
 * practice, sell, and offer for sale, import, and/or otherwise dispose of
 * caBIG™ Software (or portions thereof); (iii) distribute and have distributed
 * to and by third parties the caBIG™ Software and any modifications and
 * derivative works thereof; and (iv) sublicense the foregoing rights set out in
 * (i), (ii) and (iii) to third parties, including the right to license such
 * rights to further third parties. For sake of clarity, and not by way of
 * limitation, caBIG™ Participant shall have no right of accounting or right of
 * payment from You or Your sublicensees for the rights granted under this
 * License. This License is granted at no charge to You. Your downloading,
 * copying, modifying, displaying, distributing or use of caBIG™ Software
 * constitutes acceptance of all of the terms and conditions of this Agreement.
 * If you do not agree to such terms and conditions, you have no right to
 * download, copy, modify, display, distribute or use the caBIG™ Software.
 * 
 * 1. Your redistributions of the source code for the caBIG™ Software must
 * retain the above copyright notice, this list of conditions and the disclaimer
 * and limitation of liability of Article 6 below. Your redistributions in
 * object code form must reproduce the above copyright notice, this list of
 * conditions and the disclaimer of Article 6 in the documentation and/or other
 * materials provided with the distribution, if any.
 * 
 * 2. Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: “This product includes software
 * developed by Massachusetts General Hospital.” If You do not include such
 * end-user documentation, You shall include this acknowledgment in the caBIG™
 * Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3. You may not use the names ”Massachusetts General Hospital”, “MGH”, “The
 * National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™”
 * to endorse or promote products derived from this caBIG™ Software. This
 * License does not authorize You to use any trademarks, service marks, trade
 * names, logos or product names of either caBIG™ Participant, NCI or caBIG™,
 * except as required to comply with the terms of this License.
 * 
 * 4. For sake of clarity, and not by way of limitation, You may incorporate
 * this caBIG™ Software into Your proprietary programs and into any third party
 * proprietary programs. However, if You incorporate the caBIG™ Software into
 * third party proprietary programs, You agree that You are solely responsible
 * for obtaining any permission from such third parties required to incorporate
 * the caBIG™ Software into such third party proprietary programs and for
 * informing Your sublicensees, including without limitation Your end-users, of
 * their obligation to secure any required permissions from such third parties
 * before incorporating the caBIG™ Software into such third party proprietary
 * software programs. In the event that You fail to obtain such permissions, You
 * agree to indemnify caBIG™ Participant for any claims against caBIG™
 * Participant by such third parties, except to the extent prohibited by law,
 * resulting from Your failure to obtain such permissions.
 * 
 * 5. For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the caBIG™ Software, or any derivative works
 * of the caBIG™ Software as a whole, provided Your use, reproduction, and
 * distribution of the Work otherwise complies with the conditions stated in
 * this License.
 * 
 * 6. THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 * DISCLAIMED. IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU) Contributors:
 * INFOTECH Soft, Inc.
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