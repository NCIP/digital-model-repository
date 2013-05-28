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
	* Data files can be included in an entry by way of upload, http reference, or internal repository reference.	**/
public class DataClassification  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* The identifier of the DataClassification.	**/
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
	* Name of the upload	**/
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
	* A brief description of the file's contents	**/
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
	* Link to the file itself	**/
	public String source;
	/**
	* Retreives the value of source attribute
	* @return source
	**/

	public String getSource(){
		return source;
	}

	/**
	* Sets the value of source attribue
	**/

	public void setSource(String source){
		this.source = source;
	}
	
		/**
	* Any additional user input	**/
	public String comment;
	/**
	* Retreives the value of comment attribute
	* @return comment
	**/

	public String getComment(){
		return comment;
	}

	/**
	* Sets the value of comment attribue
	**/

	public void setComment(String comment){
		this.comment = comment;
	}
	
	/**
	* An associated org.cvit.cabig.dmr.domain.Entry object's collection 
	**/
			
	private Collection<Entry> entry;
	/**
	* Retreives the value of entry attribue
	* @return entry
	**/

	public Collection<Entry> getEntry(){
		return entry;
	}

	/**
	* Sets the value of entry attribue
	**/

	public void setEntry(Collection<Entry> entry){
		this.entry = entry;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof DataClassification) 
		{
			DataClassification c =(DataClassification)obj; 			 
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