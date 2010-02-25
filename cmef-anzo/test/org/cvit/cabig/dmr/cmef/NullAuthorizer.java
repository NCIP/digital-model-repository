package org.cvit.cabig.dmr.cmef;

import java.util.Set ;

import com.infotechsoft.rdf.Uri ;
import com.infotechsoft.rdf.basic.BasicUri ;

public class NullAuthorizer implements CmefAuthorizer {
    private Uri userUri ;

    public NullAuthorizer(Uri userUri) {
	if (userUri == null) {
	    userUri = new BasicUri("urn:its:null_authorizer") ;
	}
	this.userUri = userUri ;
    }
    
    @Override
    public boolean canEditResource(Uri id) {
	return true ;
    }

    @Override
    public boolean canViewResource(Uri id) {
	return true ;
    }

    @Override
    public Uri getUserUri() {
	return userUri ;
    }
    
    @Override
    public void updateObjectsAclFromEntry(Set<String> modelIds, String id) throws AuthorizationException {
        // TODO Auto-generated method stub
    }
}
