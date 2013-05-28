/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections ;
import java.util.Iterator;

import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform;
import org.cvit.cabig.dmr.cmef.domain.File;
import org.cvit.cabig.dmr.cmef.domain.Parameter;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONComputationalModel implements JSONInterface {

	public Object fromJSON(JSONObject jsonObject) {
		ComputationalModel model = new ComputationalModel();
		try {
			JSONObject theModelObject = jsonObject.optJSONObject("computationalModel");
			
			//All String Variables
			model.setId(theModelObject.optString("id"));
			model.setTitle(theModelObject.optString("title"));
			model.setDescription(theModelObject.optString("description"));
			model.setSource(theModelObject.optString("source"));
			model.setComment(theModelObject.optString("comment"));
			model.setCommandLine(theModelObject.optString("commandLine"));
			model.setVersion(theModelObject.optString("version"));
			
			//"document" variable which is a File object
			JSONObject document = (JSONObject)theModelObject.optJSONObject("document");
			if (document != null) {
				File file = new File();
				file.setName(document.optString("name"));
				file.setSource(document.optString("source"));
				file.setId(document.optString("id"));
				file.setDocumentModel(model);
				model.setDocument(file);
			}

			//"program" variable which is a Programming Platform object
			JSONObject program = theModelObject.optJSONObject("program");
			if (program != null) {
				ProgrammingPlatform progPlatform = new ProgrammingPlatform();
				progPlatform.setLanguageType(program.optString("languageType"));
				progPlatform.setLanguageVersion(program.optString("languageVersion"));
				progPlatform.setId(program.optString("id"));
				progPlatform.setModel(Collections.singleton(model));
				model.setProgram(progPlatform);
			}
			
			//"platforms" variable which is a Collection<ProgrammingPlatforms>
			JSONArray platformArray = theModelObject.optJSONArray("platforms");
			if (platformArray != null) {
				Collection<ComputingPlatform> platforms = new ArrayList<ComputingPlatform>(); 
				for (int i = 0; i < platformArray.length(); i++) {
					//Create new ComputingPlatform and JSONObject representation
					ComputingPlatform platform = new ComputingPlatform();
					JSONObject platformJSON = platformArray.optJSONObject(i).optJSONObject("platform");
					
					//Fill CompPlatform with JSON values
					platform.setId(platformJSON.optString("id"));
					platform.setOperatingSystemType(platformJSON.optString("operatingSystemType"));
					platform.setProcessorArchitecture(platformJSON.optString("processorArchitecture"));
					platform.setModel(Collections.singleton(model));
					
					//Add completed platform to collection
					platforms.add(platform);
				}
				model.setPlatforms(platforms);
			}
			
			//"parameters" variable which is a Collection<Parameter>
			JSONArray parameterArray = theModelObject.optJSONArray("parameters");
			if (parameterArray != null) {
				Collection<Parameter> parameters = new ArrayList<Parameter>(); 
				for (int i = 0; i < parameterArray.length(); i++) {
					//Create new Parameter and JSONObject representation
					Parameter parameter = new Parameter();
					JSONObject parameterJSON = parameterArray.optJSONObject(i).optJSONObject("parameter");
					
					//Fill Parameter with JSON values
					parameter.setId(parameterJSON.optString("id"));
					parameter.setDescription(parameterJSON.optString("description"));
					parameter.setDataType(parameterJSON.optString("dataType"));
					parameter.setPrefix(parameterJSON.optString("prefix"));
					parameter.setDefaultValue(parameterJSON.optString("defaultValue"));
					parameter.setName(parameterJSON.optString("name"));	
					parameter.setIsFile(parameterJSON.optBoolean("isFile"));
					parameter.setIsOptional(parameterJSON.optBoolean("isOptional"));
					parameter.setModel(model);
					//choices
					JSONArray choiceArray = parameterJSON.optJSONArray("choices");
					if (choiceArray != null) {
						Collection<String> choices = new ArrayList<String>();
						for (int j = 0; j < choiceArray.length(); j++) {
							choices.add(choiceArray.getString(j));
						}
						parameter.setChoices(choices);
					}
					//Add completed parameter to collection
					parameters.add(parameter);
				}
				model.setParameters(parameters);
			}
			
			//"files" variable which is a Collection<File>
			JSONArray fileArray = theModelObject.optJSONArray("files");
			if (fileArray != null) {
				Collection<File> files = new ArrayList<File>(); 
				for (int i = 0; i < fileArray.length(); i++) {
					//Create new File and JSONObject representation
					File file = new File();
					JSONObject fileJSON = fileArray.optJSONObject(i).optJSONObject("file");
					
					//Fill File with JSON values
					file.setId(fileJSON.optString("id"));
					file.setSource(fileJSON.optString("source"));
					file.setName(fileJSON.optString("name"));
					file.setFileModel(model);
					
					//Add completed file to collection
					files.add(file);
				}
				model.setFiles(files);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
	public JSONObject toJSON(Object object) {
		ComputationalModel model = (ComputationalModel)object;
		JSONObject returnJSON = null;
		try {
			returnJSON = new JSONObject();
			JSONObject jsonValues = new JSONObject(model, new String[]{"id","title","description","source","comment",
					"commandLine","version"});

			if (model.getDocument() != null) {
				jsonValues.put("document", new JSONObject(model.getDocument(), new String[]{"name","source","id"}));
			}
			if (model.getProgram() != null) {
				jsonValues.put("program", new JSONObject(model.getProgram(), new String[]{"languageType","languageVersion","id"}));
			}
			
			if (model.getFiles() != null) {
				jsonValues.put("files", getJSONArray("file",new String[]{"name","source","id"},model.getFiles()));
			}
			if (model.getPlatforms() != null) {
				jsonValues.put("platforms", getJSONArray("platform",new String[]{"operatingSystemType","processorArchitecture","id"},model.getPlatforms()));
			}
			if (model.getParameters() != null) {
				jsonValues.put("parameters", getJSONArray("parameter",new String[]{"name","description","dataType","prefix",
													"defaultValue","isOptional","isFile","id","choices"},model.getParameters()));
			}
			
			returnJSON.put("computationalModel", jsonValues);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return returnJSON;
	}
	
	private JSONArray getJSONArray(String key, String[] fields, Collection<?> collection) throws JSONException {
		JSONArray array = new JSONArray();
		Iterator<?> itr = collection.iterator();
		while (itr.hasNext()) {
			JSONObject values = new JSONObject(itr.next(),fields);
			JSONObject returnObject = new JSONObject();
			returnObject.put(key, values);
			array.put(returnObject);
		}
		return array;
	}
}
