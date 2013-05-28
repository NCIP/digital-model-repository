/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.anzo;

import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.common.InvalidCredentialException;
import gov.nih.nci.cagrid.authentication.common.SubjectProvider;
import gov.nih.nci.security.authentication.principal.EmailIdPrincipal;
import gov.nih.nci.security.authentication.principal.FirstNamePrincipal;
import gov.nih.nci.security.authentication.principal.LastNamePrincipal;
import gov.nih.nci.security.authentication.principal.LoginIdPrincipal;

import java.security.Principal;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import org.cvit.cabig.dmr.vocabulary.CViT;
import org.cvit.cabig.dmr.vocabulary.FOAF;
import org.openanzo.common.exceptions.AnzoException;
import org.openanzo.model.Constants;
import org.openanzo.services.IAuthenticationService;
import org.openanzo.services.IModelService;
import org.openrdf.model.URI;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CvitSubjectProvider implements SubjectProvider {
    private static final String SYSADMIN_ROLE = "http://openanzo.org/Role/sysAdmin" ;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    private IAuthenticationService authService ;
    private IModelService modelService ;
    
    public CvitSubjectProvider(IAuthenticationService authService, IModelService modelService) {
	if (authService == null) {
	    throw new IllegalArgumentException("Authentication service may not be null.") ;
	}
	if (modelService == null) {
	    throw new IllegalArgumentException("Model service may not be null.") ;
	}
	this.authService = authService ;
	this.modelService = modelService ;
    }
    
    public Subject getSubject(Credential credential) throws InvalidCredentialException {
	URI user ;
	try {
	    user = authService.authenticateUser(
	        credential.getBasicAuthenticationCredential().getUserId(), 
	        credential.getBasicAuthenticationCredential().getPassword()) ;
	} catch (Exception e) {
	    throw new InvalidCredentialException("Unable to validate user credentials.", e) ;
	}
	try {
        	if (!isSysadmin(user)) {
        	    if (!isCvitUser(user)) {
        		throw new InvalidCredentialException("Account has not been approved by licensing officer.") ;
        	    }
        	    if (isDisabled(user)) {
        		throw new InvalidCredentialException("Account is disabled.") ;
        	    }
        	}
	} catch (AnzoException e) {
	    throw new InvalidCredentialException("Exception while validating user credentials.", e) ;
	} catch (QueryEvaluationException e) {
	    throw new InvalidCredentialException("Exception while validating user credentials.", e) ;
	}
	Subject result = new Subject() ;
	result.getPrincipals().add(new LoginIdPrincipal(credential.getBasicAuthenticationCredential().getUserId())) ;
	try {
	    addPrincipals(user, result.getPrincipals()) ;
	} catch (Exception e) {
	    logger.warn("Exception while retrieving principals for: {}.", user) ;
	}
	return result ;
    }

    private static final String FIRST_NAME = "firstName" ;
    private static final String LAST_NAME = "lastName" ;
    private static final String EMAIL = "email" ;
    private void addPrincipals(URI user, Set<Principal> set) throws QueryEvaluationException, AnzoException {
	BindingSet principals = getPrincipals(user) ;
	set.add(new FirstNamePrincipal(
	    (principals.hasBinding(FIRST_NAME)) ? principals.getValue(FIRST_NAME).toString() : "Unknown")) ;
	set.add(new LastNamePrincipal(
	    (principals.hasBinding(LAST_NAME)) ? principals.getValue(LAST_NAME).toString() : "Unknown")) ;
	set.add(new EmailIdPrincipal(
	    (principals.hasBinding(EMAIL)) ? principals.getValue(EMAIL).toString() : "unknown@example.org")) ;
    }
    
    private static final String PRINCIPALS_QUERY = "SELECT ?" + FIRST_NAME + " ?" + LAST_NAME + " ?" + EMAIL + " WHERE {" +
    		"OPTIONAL { <%1$s> <" + FOAF.FIRST_NAME + "> ?" + FIRST_NAME + ".} " +
    		"OPTIONAL { <%1$s> <" + FOAF.SURNAME + "> ?" + LAST_NAME + ".} " +
    		"OPTIONAL { <%1$s> <" + FOAF.MBOX + "> ?" + EMAIL + ".} " +
    	"}" ;
    private BindingSet getPrincipals(URI user) throws AnzoException, QueryEvaluationException {
	String query = new Formatter().format(PRINCIPALS_QUERY, user).toString() ;
	TupleQueryResult selectResult = modelService.executeQuery(
	    Set(Constants.allNamedGraphsUriURI),
	    Set(new URI[0]),
	    query).getSelectResult() ;
	if (selectResult.hasNext()) {
	    return selectResult.next() ;
	}
	return null ;
    }
    
    private boolean isSysadmin(URI user) throws AnzoException {
	Set<URI> roles = authService.getRolesForUser(user) ;
	return roles.contains(Constants.valueFactory.createURI(SYSADMIN_ROLE)) ;
    }
    
    private boolean isCvitUser(URI user) throws AnzoException {
	Set<URI> roles = authService.getRolesForUser(user) ;
	return roles.contains(Constants.valueFactory.createURI(CViT.ROLE)) ;
    }
    
    private static String DISABLED_QUERY = "SELECT ?disabled WHERE {" +
    		"<%s> <" + CViT.DatatypeProperties.LOGIN_DISABLED + "> ?disabled." +
    	"}" ;
    private boolean isDisabled(URI user) throws AnzoException, QueryEvaluationException {
	String query = new Formatter().format(DISABLED_QUERY, user).toString() ;
	TupleQueryResult selectResult = modelService.executeQuery(
	    Set(Constants.allNamedGraphsUriURI),
	    Set(new URI[0]),
	    query).getSelectResult() ;
	if (selectResult.hasNext()) {
	    return Boolean.parseBoolean(selectResult.next().getValue("disabled").toString()) ;
	}
	return false ;
    }
    
    private <T> Set<T> Set(T... objs) {
	Set<T> result = new HashSet<T>() ;
	for (T obj : objs) {
	    result.add(obj) ;
	}
	return result ;
    }
}
