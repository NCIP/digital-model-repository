/*
 * caBIG™ Open Source Software License v.2 Digital Model Repository (DMR)
 * 
 * Copyright Notice. Copyright 2008 Massachusetts General Hospital (“caBIG™
 * Participant”). Digital Model Repository (DMR) was created with NCI funding
 * and is part of the caBIG™ initiative. The software subject to this notice and
 * license includes both human readable source code form and machine readable,
 * binary, object code form (the “caBIG™ Software”).
 * 
 * This caBIG™ Software License (the “License”) is between caBIG™ Participant
 * and You. “You (or “Your”) shall mean a person or an entity, and all other
 * entities that control, are controlled by, or are under common control with
 * the entity. “Control” for purposes of this definition means (i) the direct or
 * indirect power to cause the direction or management of such entity, whether
 * by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of
 * the outstanding shares, or (iii) beneficial ownership of such entity.
 * 
 * License. Provided that You agree to the conditions described below, caBIG™
 * Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caBIG™ Software, including any copyright or patent rights
 * therein, to (i) use, install, disclose, access, operate, execute, reproduce,
 * copy, modify, translate, market, publicly display, publicly perform, and
 * prepare derivative works of the caBIG™ Software in any manner and for any
 * purpose, and to have or permit others to do so; (ii) make, have made, use,
 * practice, sell, and offer for sale, import, and/or otherwise dispose of
 * caBIG™ Software (or portions thereof); (iii) distribute and have distributed
 * to and by third parties the caBIG™ Software and any modifications and
 * derivative works thereof; and (iv) sublicense the foregoing rights set out in
 * (i), (ii) and (iii) to third parties, including the right to license such
 * rights to further third parties. For sake of clarity, and not by way of
 * limitation, caBIG™ Participant shall have no right of accounting or right of
 * payment from You or Your sublicensees for the rights granted under this
 * License. This License is granted at no charge to You. Your downloading,
 * copying, modifying, displaying, distributing or use of caBIG™ Software
 * constitutes acceptance of all of the terms and conditions of this Agreement.
 * If you do not agree to such terms and conditions, you have no right to
 * download, copy, modify, display, distribute or use the caBIG™ Software.
 * 
 * 1. Your redistributions of the source code for the caBIG™ Software must
 * retain the above copyright notice, this list of conditions and the disclaimer
 * and limitation of liability of Article 6 below. Your redistributions in
 * object code form must reproduce the above copyright notice, this list of
 * conditions and the disclaimer of Article 6 in the documentation and/or other
 * materials provided with the distribution, if any.
 * 
 * 2. Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: “This product includes software
 * developed by Massachusetts General Hospital.” If You do not include such
 * end-user documentation, You shall include this acknowledgment in the caBIG™
 * Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3. You may not use the names ”Massachusetts General Hospital”, “MGH”, “The
 * National Cancer Institute”, “NCI”, “Cancer Bioinformatics Grid” or “caBIG™”
 * to endorse or promote products derived from this caBIG™ Software. This
 * License does not authorize You to use any trademarks, service marks, trade
 * names, logos or product names of either caBIG™ Participant, NCI or caBIG™,
 * except as required to comply with the terms of this License.
 * 
 * 4. For sake of clarity, and not by way of limitation, You may incorporate
 * this caBIG™ Software into Your proprietary programs and into any third party
 * proprietary programs. However, if You incorporate the caBIG™ Software into
 * third party proprietary programs, You agree that You are solely responsible
 * for obtaining any permission from such third parties required to incorporate
 * the caBIG™ Software into such third party proprietary programs and for
 * informing Your sublicensees, including without limitation Your end-users, of
 * their obligation to secure any required permissions from such third parties
 * before incorporating the caBIG™ Software into such third party proprietary
 * software programs. In the event that You fail to obtain such permissions, You
 * agree to indemnify caBIG™ Participant for any claims against caBIG™
 * Participant by such third parties, except to the extent prohibited by law,
 * resulting from Your failure to obtain such permissions.
 * 
 * 5. For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the caBIG™ Software, or any derivative works
 * of the caBIG™ Software as a whole, provided Your use, reproduction, and
 * distribution of the Work otherwise complies with the conditions stated in
 * this License.
 * 
 * 6. THIS caBIG™ SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 * DISCLAIMED. IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG™ SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU) Contributors:
 * INFOTECH Soft, Inc.
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
