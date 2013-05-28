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
	* Model entry classification denotes the origin of the data. (Computation | Invitro | Invivo | Clinical)	**/
public class EntryType  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* The identifier of the EntryType.	**/
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
	* The name of the Entry Type. One of In silico, In vitro, In vivo, Clinical.	**/
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
		if(obj instanceof EntryType) 
		{
			EntryType c =(EntryType)obj; 			 
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