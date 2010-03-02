/*******************************************************************************
 * caBIG™ Open Source Software License
 * Computational Model Execution Framework (CMEF) v1.0
 * 
 * Copyright Notice.
 * Copyright 2010 Massachusetts General Hospital (“caBIG™ Participant”).  Computational Model Execution Framework (CMEF) was created with NCI funding and is part of the caBIG™ initiative. The software subject to this notice and license includes both human readable source code form and machine readable, binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant and You.  “You (or “Your”) shall mean a person or an entity, and all other entities that control, are controlled by, or are under common control with the entity.  “Control” for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.  
 * 
 * License.  
 * Provided that You agree to the conditions described below, caBIG™ Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights in the caBIG™ Software, including any copyright or patent rights therein, to (i) use, install, disclose, access, operate, execute, reproduce, copy, modify, translate, market, publicly display, publicly perform, and prepare derivative works of the caBIG™ Software in any manner and for any purpose, and to have or permit others to do so; (ii) make, have made, use, practice, sell, and offer for sale, import, and/or otherwise dispose of caBIG™ Software (or portions thereof); (iii) distribute and have distributed to and by third parties the caBIG™ Software and any modifications and derivative works thereof; and (iv) sublicense the foregoing rights set out in (i), (ii) and (iii) to third parties, including the right to license such rights to further third parties.  For sake of clarity, and not by way of limitation, caBIG™ Participant shall have no right of accounting or right of payment from You or Your sublicensees for the rights granted under this License.  This License is granted at no charge to You.  Your downloading, copying, modifying, displaying, distributing or use of caBIG™ Software constitutes acceptance of all of the terms and conditions of this Agreement.  If you do not agree to such terms and conditions, you have no right to download, copy, modify, display, distribute or use the caBIG™ Software.  
 * 
 * 1.	Your redistributions of the source code for the caBIG™ Software must retain the above copyright notice, this list of conditions and the disclaimer and limitation of liability of Article 6 below.  Your redistributions in object code form must reproduce the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials provided with the distribution, if any.
 * 
 * 2.	Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: “This product includes software developed by Massachusetts General Hospital.”  If You do not include such end-user documentation, You shall include this acknowledgment in the caBIG™ Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3.	You may not use the names  ”Massachusetts General Hospital”, “MGH”, "INFOTECH Soft", “The National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™” to endorse or promote products derived from this caBIG™ Software.  This License does not authorize You to use any trademarks, service marks, trade names, logos or product names of either caBIG™ Participant, NCI or caBIG™, except as required to comply with the terms of this License.
 * 
 * 4.	For sake of clarity, and not by way of limitation, You may incorporate this caBIG™ Software into Your proprietary programs and into any third party proprietary programs.  However, if You incorporate the caBIG™ Software into third party proprietary programs, You agree that You are solely responsible for obtaining any permission from such third parties required to incorporate the caBIG™ Software into such third party proprietary programs and for informing Your sublicensees, including without limitation Your end-users, of their obligation to secure any required permissions from such third parties before incorporating the caBIG™ Software into such third party proprietary software programs.  In the event that You fail to obtain such permissions, You agree to indemnify caBIG™ Participant for any claims against caBIG™ Participant by such third parties, except to the extent prohibited by law, resulting from Your failure to obtain such permissions.
 * 
 * 5.	For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses of modifications of the caBIG™ Software, or any derivative works of the caBIG™ Software as a whole, provided Your use, reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.
 * 
 * 6.	THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED.  IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU)
 * Contributors: INFOTECH Soft, Inc.
 ******************************************************************************/
package org.cvit.cabig.dmr.cmef.json;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.cvit.cabig.dmr.cmef.domain.File;
import org.cvit.cabig.dmr.cmef.domain.Parameter;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONComputationJob implements JSONInterface{

	public Object fromJSON(JSONObject jsonObject) {
		ComputationJob job = new ComputationJob();
		try {
			JSONObject theModelObject = jsonObject.optJSONObject("computationJob");
			
			//All String Variables
			job.setId(theModelObject.optString("id"));
			job.setTitle(theModelObject.optString("title"));
			job.setDescription(theModelObject.optString("description"));
			job.setSource(theModelObject.optString("source"));
			job.setComment(theModelObject.optString("comment"));
			
			String dateSubmitted = theModelObject.optString("dateSubmitted");
			String dateCompleted = theModelObject.optString("dateCompleted");
			SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'EST' yyyy");			
			if (!"".equals(dateSubmitted)) {
				job.setDateSubmitted(formatter.parse(dateSubmitted));
			}
			if (!"".equals(dateCompleted)) {
				job.setDateCompleted(formatter.parse(dateCompleted));
			}
			job.setJobNumber(theModelObject.optInt("jobNumber"));
			job.setJobStatus(theModelObject.optString("jobStatus"));
			job.setUserId(theModelObject.optString("userId"));
			
			//"resultFiles" variable which is a Collection<File>
			JSONArray resultFileArray = theModelObject.optJSONArray("resultFiles");
			if (resultFileArray != null) {
				Collection<File> files = new ArrayList<File>(); 
				for (int i = 0; i < resultFileArray.length(); i++) {
					//Create new File and JSONObject representation
					File file = new File();
					JSONObject fileJSON = resultFileArray.optJSONObject(i).optJSONObject("file");
					
					//Fill file with JSON values
					file.setId(fileJSON.optString("id"));
					file.setName(fileJSON.optString("name"));
					file.setSource(fileJSON.optString("source"));
					file.setJob(job);
					
					//Add completed platform to collection
					files.add(file);
				}
				job.setResultFiles(files);
			}
			
			//"parameterValues" variable which is a Collection<ParameterValue>
			JSONArray parameterArray = theModelObject.optJSONArray("parameterValues");
			if (parameterArray != null) {
				Collection<ParameterValue> parameters = new ArrayList<ParameterValue>(); 
				for (int i = 0; i < parameterArray.length(); i++) {
					//Create new Parameter and JSONObject representation
					ParameterValue parameterValue = new ParameterValue();
					JSONObject parameterJSON = parameterArray.optJSONObject(i).optJSONObject("parameterValue");
					//Fill Parameter with JSON values
					parameterValue.setId(parameterJSON.optString("id"));
					parameterValue.setValue(parameterJSON.optString("value"));
					
					JSONObject parameterVariable = parameterJSON.optJSONObject("parameter");
					
					Parameter parameter = new Parameter();
					if (parameterVariable != null) {
						parameter.setName(parameterVariable.optString("name"));
						parameter.setDescription(parameterVariable.optString("description"));
						parameter.setDataType(parameterVariable.optString("dataType"));
						parameter.setPrefix(parameterVariable.optString("prefix"));
						parameter.setDefaultValue(parameterVariable.optString("defaultValue"));
						parameter.setIsOptional(parameterVariable.optBoolean("isOptional"));
						parameter.setIsFile(parameterVariable.optBoolean("isFile"));
						parameter.setId(parameterVariable.optString("id"));
						
						//choices	
						JSONArray choicesArray = parameterVariable.optJSONArray("choices");
						if (choicesArray != null) {
							Collection<String> choices = new ArrayList<String>();
						
							for (int j = 0; j < choicesArray.length(); j++) {
								choices.add(choicesArray.getString(j));
							}
							parameter.setChoices(choices);
						}
						parameter.setValue(parameterValue);
						parameterValue.setParameter(parameter);
					}
					//Add completed parameter to collection
					parameters.add(parameterValue);
					parameterValue.setJob(job);
				}
				job.setParameterValues(parameters);
			}		
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return job;
	}
	
	public JSONObject toJSON(Object object) {
		ComputationJob job = (ComputationJob)object;
		JSONObject returnJSON = null;
		try {
			returnJSON = new JSONObject();
			JSONObject jsonValues = new JSONObject(job, new String[]{"id","title","description","source","comment",
					"dateSubmitted","dateCompleted","jobNumber","jobStatus","userId"});

			if (job.getResultFiles() != null) {
				jsonValues.put("resultFiles", getJSONArray("file",new String[]{"name","source","id"},job.getResultFiles()));
			}
			if (job.getParameterValues() != null) {
				jsonValues.put("parameterValues", getJSONArray("parameterValue",new String[]{"value","id","parameter"},job.getParameterValues()));
			}
			returnJSON.put("computationJob", jsonValues);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		
		return returnJSON;
	}
	
	private JSONArray getJSONArray(String key, String[] fields, Collection<?> collection) throws JSONException {
		JSONArray array = new JSONArray();
		Iterator<?> itr = collection.iterator();
		
		//Collection of primitive type
		while (itr.hasNext()) { 
			Object object = itr.next();
			if (fields == null) {
				array.put(object);
			}
			//Collection of Objects
			else {							
				JSONObject values = new JSONObject(object,fields);
				if (object instanceof ParameterValue && ((ParameterValue)object).getParameter() != null) {
					values.put("parameter", toJSON(((ParameterValue)object).getParameter(), new String[]{"name","description","dataType","prefix",
								"defaultValue","isOptional","isFile","id"}));
				}
				
				JSONObject returnObject = new JSONObject();
				returnObject.put(key, values);
				array.put(returnObject);
			}
		}
		return array;
	}	

	private JSONObject toJSON(Object object, String[] fields) throws JSONException {
		JSONObject objectValue = new JSONObject(object, fields);	
		//getChoices
		if (object instanceof Parameter && ((Parameter)object).getChoices() != null) {					
			objectValue.put("choices", getJSONArray("choice",null,((Parameter)object).getChoices()));
		}
		return objectValue;
	}	
}
