/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */



package org.cvit.cabig.dmr.integration.security;

import static org.junit.Assert.*;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.common.InvalidCredentialException;
import gov.nih.nci.security.authentication.principal.EmailIdPrincipal;
import gov.nih.nci.security.authentication.principal.FirstNamePrincipal;
import gov.nih.nci.security.authentication.principal.LastNamePrincipal;
import gov.nih.nci.security.authentication.principal.LoginIdPrincipal;

import java.util.Set;

import javax.security.auth.Subject;

import org.cvit.cabig.dmr.anzo.CvitSubjectProvider;
import org.cvit.cabig.dmr.integration.AbstractAnzoTests;
import org.cvit.cabig.dmr.vocabulary.AnzoPredicates;
import org.cvit.cabig.dmr.vocabulary.CViT;
import org.cvit.cabig.dmr.vocabulary.FOAF;
import org.cvit.cabig.dmr.vocabulary.RBAC;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;

import com.infotechsoft.rdf.vocabulary.Rdf;


public class CvitSubjectProviderTests extends AbstractAnzoTests {
    private static final String MYADMIN = "http://example.org/users/MyAdmin" ;
    private static final String MYADMIN_ID = "myadmin" ;
    private static final String MYADMIN_PW = "myadminpw" ;
    private static final String MYADMIN_FN = "My" ;
    private static final String MYADMIN_LN = "Administrator" ;
    private static final String MYADMIN_EMAIL = "myadmin@example.org" ;
    
    private static final String USER = "http://example.org/users/User" ;
    private static final String USER_ID = "user" ;
    private static final String USER_PW = "userpw" ;
    
    private static final String NONCVIT = "http://example.org/users/NonCvit" ;
    private static final String NONCVIT_ID = "noncvit" ;
    private static final String NONCVIT_PW = "noncvitpw" ;
    
    private CvitSubjectProvider subjectProvider ;
    @Before
    public void initCvitSubjectProvider() {
	subjectProvider = new CvitSubjectProvider(
	    getAuthenticationService(), 
	    getDatasetService().getModelService()) ;
    }
    
    @Override
    protected Set<Statement> getInitialStatements(Set<Statement> defaults) {
	//Add CViT role...
	defaults.add(statement(uri(CViT.ROLE), uri(Rdf.TYPE), uri(RBAC.ROLE))) ;
        //Add an administrator user...
	defaults.add(statement(uri(MYADMIN), uri(Rdf.TYPE), uri(RBAC.USER))) ;
	defaults.add(statement(uri(MYADMIN), uri(AnzoPredicates.USER_ID), literal(MYADMIN_ID))) ;
	defaults.add(statement(uri(MYADMIN), uri(AnzoPredicates.PASSWORD), literal(MYADMIN_PW))) ;
	defaults.add(statement(uri(MYADMIN), uri(RBAC.IN_ROLE), uri(SYSADMIN_ROLE))) ;
        //Add a CViT user...
	defaults.add(statement(uri(USER), uri(Rdf.TYPE), uri(RBAC.USER))) ;
	defaults.add(statement(uri(USER), uri(AnzoPredicates.USER_ID), literal(USER_ID))) ;
	defaults.add(statement(uri(USER), uri(AnzoPredicates.PASSWORD), literal(USER_PW))) ;
	defaults.add(statement(uri(USER), uri(RBAC.IN_ROLE), uri(CViT.ROLE))) ;
        //Add non-CViT user...
	defaults.add(statement(uri(NONCVIT), uri(Rdf.TYPE), uri(RBAC.USER))) ;
	defaults.add(statement(uri(NONCVIT), uri(AnzoPredicates.USER_ID), literal(NONCVIT_ID))) ;
	defaults.add(statement(uri(NONCVIT), uri(AnzoPredicates.PASSWORD), literal(NONCVIT_PW))) ;
        return defaults ;
    }
    
    @Test(expected=InvalidCredentialException.class)
    public void doesNotAuthenticateInvalidCredentials() throws Exception {
	subjectProvider.getSubject(Credentials("Invalid", "Invalid")) ;
    }
    
    @Test
    public void authenticatesNonCvitAdministrator() throws Exception {
	Subject subject = subjectProvider.getSubject(Credentials(MYADMIN_ID, MYADMIN_PW)) ;
	assertNotNull(subject) ;
    }
    
    @Test
    public void authenticatesDisabledAdministrator() throws Exception {
	addNamedGraph(uri(MYADMIN),
	    statement(uri(MYADMIN), uri(CViT.DatatypeProperties.LOGIN_DISABLED), literal("true"))) ;
	
	assertNotNull(subjectProvider.getSubject(Credentials(MYADMIN_ID, MYADMIN_PW))) ;
    }
    
    @Test
    public void authenticatesNonDisabledCvitUser() throws Exception {
	assertNotNull(subjectProvider.getSubject(Credentials(USER_ID, USER_PW))) ;
    }
    
    @Test(expected=InvalidCredentialException.class)
    public void doesNotAthenticateNonCvitUser() throws Exception {
	subjectProvider.getSubject(Credentials(NONCVIT_ID, NONCVIT_PW)) ;
    }
    
    @Test(expected=InvalidCredentialException.class)
    public void doesNotAuthenticateDisabledCvitUser() throws Exception {
	addNamedGraph(uri(USER),
	    statement(uri(USER), uri(CViT.DatatypeProperties.LOGIN_DISABLED), literal("true"))) ;
	
	subjectProvider.getSubject(Credentials(USER_ID, USER_PW)) ;
    }
    
    //TODO: doesNotAuthenticateCvitUser...expired institution check...
    
    @Test
    public void addsAllPrincipalsToSubject() throws Exception {
	addNamedGraph(uri(MYADMIN),
	    statement(uri(MYADMIN), uri(FOAF.FIRST_NAME), literal(MYADMIN_FN)),
	    statement(uri(MYADMIN), uri(FOAF.SURNAME), literal(MYADMIN_LN)),
	    statement(uri(MYADMIN), uri(FOAF.MBOX), literal(MYADMIN_EMAIL))) ;
	
	Subject subject = subjectProvider.getSubject(Credentials(MYADMIN_ID, MYADMIN_PW)) ;
	
	assertEquals(4, subject.getPrincipals().size()) ;
	assertTrue(subject.getPrincipals().containsAll(set(
	    new LoginIdPrincipal(MYADMIN_ID), 
	    new FirstNamePrincipal(MYADMIN_FN), 
	    new LastNamePrincipal(MYADMIN_LN), 
	    new EmailIdPrincipal(MYADMIN_EMAIL)))) ;
    }
    
    private Credential Credentials(String userId, String password) {
	Credential result = new Credential() ;
	BasicAuthenticationCredential basicCredentials = new BasicAuthenticationCredential() ;
	basicCredentials.setUserId(userId) ;
	basicCredentials.setPassword(password) ;
	result.setBasicAuthenticationCredential(basicCredentials) ;
	return result ;
    }
}
