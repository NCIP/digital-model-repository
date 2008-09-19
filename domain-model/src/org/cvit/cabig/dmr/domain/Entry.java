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