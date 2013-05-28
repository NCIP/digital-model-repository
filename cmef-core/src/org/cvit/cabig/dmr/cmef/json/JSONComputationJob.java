/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


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
