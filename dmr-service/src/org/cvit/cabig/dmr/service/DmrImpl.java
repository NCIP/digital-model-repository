package org.cvit.cabig.dmr.service ;

import gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils ;

import java.io.FileInputStream ;
import java.rmi.RemoteException ;
import java.util.Properties ;

import org.cvit.cabig.dmr.AuthenticationException ;
import org.cvit.cabig.dmr.AuthorizationException ;
import org.cvit.cabig.dmr.CvitMappings ;
import org.cvit.cabig.dmr.DMRService ;
import org.cvit.cabig.dmr.DataSourceException ;
import org.cvit.cabig.dmr.anzo.Anzo ;
import org.cvit.cabig.dmr.anzo.AnzoAuthorizationService ;
import org.cvit.cabig.dmr.anzo.AnzoDMRService;
import org.cvit.cabig.dmr.anzo.DmrServiceAuthenticationProvider ;
import org.openanzo.client.DatasetService ;
import org.openanzo.model.embedded.EmbeddedAuthenticationService ;
import org.openanzo.server.repository.RepositoryProperties ;
import org.openanzo.services.IAuthenticationService ;

import com.infotechsoft.cagrid.cql2sparql.Cql2SparqlProcessor ;
import com.infotechsoft.rdf.mapping.ObjectConverter;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DmrImpl extends DmrImplBase {

    public DmrImpl() throws RemoteException {
	super() ;
    }

    public org.cvit.cabig.dmr.domain.Entry addEntry(org.cvit.cabig.dmr.domain.Entry newEntry,
						    org.cvit.cabig.dmr.domain.Organization fundingOrganization)
	throws RemoteException {
	DMRService svc = getDmrService() ;
	try {
	    return svc.addEntry(newEntry, fundingOrganization) ;
	} catch (AuthorizationException e) {
	    throw new RemoteException("Authorization exception...", e) ;
	} catch (DataSourceException e) {
	    throw new RemoteException("Data source exception...", e) ;
	}
    }

    public org.cvit.cabig.dmr.domain.DataClassification addDataToEntry(
								       org.cvit.cabig.dmr.domain.DataClassification data,
								       org.cvit.cabig.dmr.domain.Entry sourceEntry)
	throws RemoteException {
	DMRService svc = getDmrService() ;
	try {
	    return svc.addDataToEntry(data, sourceEntry) ;
	} catch (AuthorizationException e) {
	    throw new RemoteException("Authorization exception...", e) ;
	} catch (DataSourceException e) {
	    throw new RemoteException("Data source exception...", e) ;
	}
    }

    public org.cvit.cabig.dmr.domain.Reference addReferenceToEntry(org.cvit.cabig.dmr.domain.Reference reference,
								   org.cvit.cabig.dmr.domain.Entry sourceEntry)
	throws RemoteException {
	DMRService svc = getDmrService() ;
	try {
	    return svc.addReferenceToEntry(reference, sourceEntry) ;
	} catch (AuthorizationException e) {
	    throw new RemoteException("Authorization exception...", e) ;
	} catch (DataSourceException e) {
	    throw new RemoteException("Data source exception...", e) ;
	}
    }

    public void updateEntry(org.cvit.cabig.dmr.domain.Entry entry) throws RemoteException {
	DMRService svc = getDmrService() ;
	try {
	    svc.updateEntry(entry) ;
	} catch (AuthorizationException e) {
	    throw new RemoteException("Authorization exception...", e) ;
	} catch (DataSourceException e) {
	    throw new RemoteException("Data source exception...", e) ;
	}
    }

    public void updateData(org.cvit.cabig.dmr.domain.DataClassification data) throws RemoteException {
	DMRService svc = getDmrService() ;
	try {
	    svc.updateData(data) ;
	} catch (AuthorizationException e) {
	    throw new RemoteException("Authorization exception...", e) ;
	} catch (DataSourceException e) {
	    throw new RemoteException("Data source exception...", e) ;
	}
    }

    public void updateReference(org.cvit.cabig.dmr.domain.Reference reference) throws RemoteException {
	DMRService svc = getDmrService() ;
	try {
	    svc.updateReference(reference) ;
	} catch (AuthorizationException e) {
	    throw new RemoteException("Authorization exception...", e) ;
	} catch (DataSourceException e) {
	    throw new RemoteException("Data source exception...", e) ;
	}
    }

    private void setCurrentUser(DMRService service) throws RemoteException {
	String userDn ;
	try {
	    userDn = SecurityUtils.getCallerIdentity() ;
	} catch (Exception e) {
	    throw new RemoteException("Unable to retrieve callers identity.", e) ;
	}
	try {
	    service.setUser(getUserId(userDn)) ;
	} catch (AuthenticationException e) {
	    throw new RemoteException("Unable to authenticate user.", e) ;
	}
    }

    private String getUserId(String userDn) {
	return userDn.substring(userDn.lastIndexOf('=') + 1) ;
    }

    private DMRService dmrService ;
    private DMRService getDmrService() throws RemoteException {
	if (dmrService == null) {
	    Properties props = new Properties() ;
	    try {
		props.load(new FileInputStream(getConfiguration().getAnzo_properties())) ;
	    } catch (Exception e) {
		throw new RemoteException("Error loading anzo properties.", e) ;
	    }
	    RepositoryProperties
		    .setAuthenticationProviderClass(props, DmrServiceAuthenticationProvider.class.getName()) ;

	    DatasetService systemSvc = new DatasetService(props) ;
	    IAuthenticationService authenticationSvc = EmbeddedAuthenticationService.getInstance(systemSvc, props) ;

	    DatasetService userSvc = new DatasetService(props) ;
	    Anzo anzo = new Anzo(userSvc) ;

	    dmrService = new AnzoDMRService(
		anzo, 
		new AnzoAuthorizationService(authenticationSvc, systemSvc),
		new ObjectConverter(CvitMappings.getMappers(), anzo.getRdfFactory()), 
		new Cql2SparqlProcessor(anzo, CvitMappings.getMappers())) ;
	}
	setCurrentUser(dmrService) ;
	return dmrService ;
    }

}
