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
