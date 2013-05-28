/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.server;


import org.restlet.data.Status ;
import org.restlet.ext.json.JsonRepresentation ;
import org.restlet.representation.Representation ;
import org.restlet.resource.Get ;


public class JobResource extends AbstractJobResource {
    
    @Get("json")
    public Representation getJson() {
	setStatus(Status.SUCCESS_OK) ;
	return new JsonRepresentation(jsonJob(job)) ;
    }
    
    @Get("htm|html")
    public Representation getHtml() {
	return buildHtmlJob() ;
    }
}
