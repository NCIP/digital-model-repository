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

package org.cvit.cabig.dmr.anzo;

import static org.hamcrest.Matchers.* ;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cvit.cabig.dmr.AuthenticationException;
import org.cvit.cabig.dmr.AuthorizationException;
import org.cvit.cabig.dmr.AuthorizationService;
import org.cvit.cabig.dmr.DataSourceException;
import org.cvit.cabig.dmr.InvalidUserIdException;
import org.cvit.cabig.dmr.UserAuthorizer;
import org.cvit.cabig.dmr.domain.Algorithm;
import org.cvit.cabig.dmr.domain.DataClassification;
import org.cvit.cabig.dmr.domain.Entry;
import org.cvit.cabig.dmr.domain.Image;
import org.cvit.cabig.dmr.domain.Movie;
import org.cvit.cabig.dmr.domain.Organization;
import org.cvit.cabig.dmr.domain.Paper;
import org.cvit.cabig.dmr.domain.Parameters;
import org.cvit.cabig.dmr.vocabulary.CViT;
import org.cvit.cabig.dmr.vocabulary.DC11;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.Is;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openanzo.model.Constants;

import com.infotechsoft.cagrid.cql.CqlProcessor;
import com.infotechsoft.cagrid.cql2sparql.QueryProcessingException;
import com.infotechsoft.rdf.RdfFactory;
import com.infotechsoft.rdf.Resource;
import com.infotechsoft.rdf.Statement;
import com.infotechsoft.rdf.Uri;
import com.infotechsoft.rdf.Value;
import com.infotechsoft.rdf.mapping.MappingException;
import com.infotechsoft.rdf.mapping.RdfMapper;
import com.infotechsoft.rdf.sesame.SesameRdfFactory;

@RunWith(JMock.class)
public class AnzoDmrServiceTests {
    private static RdfFactory rdfFactory = new SesameRdfFactory(Constants.valueFactory) ;
    
    private static final String USER_ID = "userId" ;
    private static final Uri USER_URI = uri("urn:user#userId") ;
    
    private static final Statement STMT_1 = statement(uri("urn:subj1"), uri("urn:pred1"), uri("urn:obj1")) ;
    private static final Statement STMT_2 = statement(uri("urn:subj2"), uri("urn:pred2"), uri("urn:obj2")) ;
    private static final Set<Statement> STMTS = set(STMT_1, STMT_2) ;
    
    private Mockery mockery ;
    
    //Class Under Test
    private AnzoDMRService dmr ;
    
    //Mocked Collaborators
    private AnzoService anzo ;
    private AuthorizationService authService ;
    private UserAuthorizer authorizer ;
    private RdfMapper mapper ;
    private CqlProcessor queryProcessor ;
    
    @Before
    public void initJmock() throws AuthenticationException, InvalidUserIdException {
	mockery = new JUnit4Mockery() ;
	anzo = mockery.mock(AnzoService.class) ;
	mockery.checking(new Expectations() {{
	    allowing(anzo).getRdfFactory() ; will(returnValue(rdfFactory)) ;
	    oneOf(anzo).setCurrentUser(with(any(String.class))) ;
	    allowing(anzo).getCurrentUser() ; will(returnValue(USER_ID)) ;
	}}) ;
	authService = mockery.mock(AuthorizationService.class) ;
	authorizer = mockery.mock(UserAuthorizer.class) ;
	mockery.checking(new Expectations() {{
	    oneOf(authService).getUserAuthorizer(with(any(String.class))) ; will(returnValue(authorizer)) ;
	    allowing(authorizer).getUri() ; will(returnValue(USER_URI)) ;
	}}) ;
	mapper = mockery.mock(RdfMapper.class) ;
	queryProcessor = mockery.mock(CqlProcessor.class) ;
	dmr = new AnzoDMRService(anzo, authService, mapper, queryProcessor) ;
	dmr.setUser(USER_ID) ;
    }
    
    // [start] addEntry(Entry, Organization) Tests
    
    @Test(expected=AuthorizationException.class)
    public void willNotAddEntryIfUserNotAuthorized() throws AuthorizationException, DataSourceException {
	Entry entry = new Entry() ;
	entry.setId("urn:entryId") ;
	mockery.checking(new Expectations() {{
	    oneOf(authorizer).canAddEntry() ; will(returnValue(false)) ;
	}}) ;
	
	dmr.addEntry(entry, null) ;
    }
    
    @Test
    public void addsEntryToAnzo() throws AuthorizationException, MappingException, DataSourceException {
	final Entry entry = new Entry() ;
	final Uri entryUri = uri("urn:entry#1") ;
	mockery.checking(new Expectations() {{
	    allowing(authorizer).canAddEntry() ; will(returnValue(true)) ;
	    allowing(mapper).toRdf(entry) ; will(returnValue(STMTS)) ;
	    oneOf(anzo).newId() ; will(returnValue(entryUri)) ;
	    oneOf(anzo).updateGraphs(
		with(anyOf(
		    aNull(Map.class),
		    equalTo(Collections.emptyMap()))), 
		with(hasEntry(equalTo(entryUri), setWith(STMT_1, STMT_2)))) ;
	}}) ;
	
	dmr.addEntry(entry, null) ;
    }
    
    @Test
    public void addsFundingOrganizationToEntryIfItIsNotNull() throws AuthorizationException, DataSourceException {
	Entry entry = new Entry() ;
	String orgId = "urn:organization#org1" ;
	Organization org = new Organization() ;
	org.setId(orgId) ;
	allowQueryFor(org) ;
	Uri entryUri = allowToAddEntry(entry) ;
	mockery.checking(addsStatement(
	    statement(entryUri, uri(CViT.ObjectProperties.FUNDING_ORGANIZATION), uri(orgId)))) ;
	
	dmr.addEntry(entry, org) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void willNotAddEntryIfInvalidOrganizationIsSpecified() throws AuthorizationException, DataSourceException {
	Entry entry = new Entry() ;
	String orgId = "urn:invalid" ;
	Organization org = new Organization() ;
	org.setId(orgId) ;
	allowQueryForNonExistingObject(org) ;
	allowToAddEntry(entry) ;
	
	dmr.addEntry(entry, org) ;
    }
    
    @Test
    public void willUseCurrentUserAsCreatorOfNewEntry() throws AuthorizationException, MappingException, DataSourceException {
	Entry entry = new Entry() ;
	Uri entryUri = allowToAddEntry(entry) ;
	mockery.checking(addsStatement(
	    statement(entryUri, uri(DC11.CREATOR), USER_URI))) ;
	
	dmr.addEntry(entry, null) ;
    }
    
    @Test
    public void willUseCurrentUserAsPiOfNewEntry() throws AuthorizationException, MappingException, DataSourceException {
	Entry entry = new Entry() ;
	Uri entryUri = allowToAddEntry(entry) ;
	mockery.checking(addsStatement(
	    statement(entryUri, uri(CViT.ObjectProperties.PRINCIPLE_INVESTIGATOR), USER_URI))) ;
	
	dmr.addEntry(entry, null) ;
    }
    
    private Uri allowToAddEntry(final Entry entry) {
	final Uri entryUri = uri("urn:entry#" + Math.random()) ;
	try {
	    mockery.checking(new Expectations() {{
		allowing(authorizer).canAddEntry() ; will(returnValue(true)) ;
		allowing(mapper).toRdf(entry) ; will(returnValue(new HashSet<Statement>())) ;
		allowing(anzo).newId() ; will(returnValue(entryUri)) ;
	    }}) ;
	} catch (Exception e) {
		throw new RuntimeException("Defect: should never happen.") ;
	}
	return entryUri ;
    }
    
    // [end] addEntry(Entry, Organization) Tests
    
    // [start] addDataToEntry(DataClassification, Entry) Tests
    
    @Test(expected=AuthorizationException.class)
    public void willNotAddDataIfUserNotAuthorizedToEditEntry() throws AuthorizationException, DataSourceException, QueryProcessingException {
	final Entry entry = new Entry() ;
	entry.setId("urn:entryId") ;
	Image image = new Image() ;
	allowQueryFor(entry) ;
	mockery.checking(new Expectations() {{
	    oneOf(authorizer).canEditObject(entry.getId()) ; will(returnValue(false)) ;
	}}) ;
	
	dmr.addDataToEntry(image, entry) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void willNotAddDataIfInvalidEntryIsSpecified() throws AuthorizationException, DataSourceException {
	final Algorithm data = new Algorithm() ;
	Entry entry = new Entry() ;
	entry.setId("urn:invalid") ;
	allowToAddData(data, entry) ;
	allowQueryForNonExistingObject(entry) ;
	
	dmr.addDataToEntry(data, entry) ;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void willNotAddDataIfInvalidIdIsSpecified() throws AuthorizationException, DataSourceException {
	Entry entry = new Entry() ;
	String entryId = "urn:entry#1" ;
	entry.setId(entryId) ;
	Parameters data = new Parameters() ;
	data.setId("urn:data#invalidId") ;
	allowQueryFor(entry) ;
	allowQueryForNonExistingObject(data, DataClassification.class) ;
	allowToAddData(data, entry) ;
	
	dmr.addDataToEntry(data, entry) ;
    }
    
    @Test
    public void alwaysAddsObjectPropertyFromEntryToData() throws DataSourceException, AuthorizationException {
	Entry entry = new Entry() ;
	final String entryId = "urn:entry#1" ;
	entry.setId(entryId) ;
	Movie data1 = new Movie() ;
	final String data1Id = "urn:data#1" ;
	final Statement data1Stmt = statement(uri(entryId), uri(CViT.ObjectProperties.DATA), uri(data1Id)) ;
	data1.setId(data1Id) ;
	Image data2 = new Image() ;
	allowQueryFor(entry) ;
	allowQueryFor(data1, DataClassification.class) ;
	final Uri data2Uri = allowToAddData(data2, entry) ;
	final Statement data2Stmt = statement(uri(entryId), uri(CViT.ObjectProperties.DATA), data2Uri) ;
	mockery.checking(new Expectations() {{
	    allowing(authService).updateObjectsAclFromEntry(
		with(anyOf(equalTo(data1Id), equalTo(data2Uri.toString()))),
		with(equalTo(entryId))) ;
	    oneOf(anzo).updateGraphs(
		with(emptyMapOrNull()), 
		with(hasEntry(equalTo(uri(entryId)), setWith(data1Stmt)))) ;
	    oneOf(anzo).updateGraphs(
		with(emptyMapOrNull()), 
		with(hasEntry(equalTo(uri(entryId)), setWith(data2Stmt)))) ;
	}}) ;

	dmr.addDataToEntry(data1, entry) ;
	dmr.addDataToEntry(data2, entry) ;
    }
    
    @Test
    public void addsDataToAnzoIfIdIsNull() throws AuthorizationException, MappingException, DataSourceException {
	Entry entry = new Entry() ;
	final String entryId = "urn:entry#1" ;
	entry.setId(entryId) ;
	final Algorithm data = new Algorithm() ;
	final Uri dataUri = uri("urn:data#1") ;
	allowQueryFor(entry) ;
	mockery.checking(new Expectations() {{
	    allowing(authorizer).canEditObject(entryId) ; will(returnValue(true)) ;
	    oneOf(anzo).newId() ; will(returnValue(dataUri)) ;
	    allowing(mapper).toRdf(data) ; will(returnValue(STMTS)) ;
	    oneOf(authService).updateObjectsAclFromEntry(dataUri.toString(), entryId) ;
	    oneOf(anzo).updateGraphs(
		with(emptyMapOrNull()), 
		with(hasEntry(equalTo(dataUri), setWith(STMT_1, STMT_2)))) ;
	}}) ;
	
	dmr.addDataToEntry(data, entry) ;
    }
    
    @Test
    public void willNotAddDataToAnzoIfAlreadyExists() throws AuthorizationException, DataSourceException {
	Entry entry = new Entry() ;
	final String entryId = "urn:entry#1" ;
	entry.setId(entryId) ;
	DataClassification data = new Image() ;
	final String dataId = "urn:data#1" ;
	data.setId(dataId) ;
	allowQueryFor(entry) ;
	allowQueryFor(data, DataClassification.class) ;
	mockery.checking(new Expectations() {{
	    allowing(authorizer).canEditObject(entryId) ; will(returnValue(true)) ;
	    oneOf(authService).updateObjectsAclFromEntry(dataId, entryId) ;
	    oneOf(anzo).updateGraphs(
		with(emptyMapOrNull()), 
		with(anyOf(
		    outKey(uri(dataId)),
		    hasEntry(equalTo(uri(dataId)), equalTo(Collections.<Statement>emptySet()))))) ;
	}}) ;
	
	dmr.addDataToEntry(data, entry) ;
    }
    
    private Uri allowToAddData(final DataClassification data, final Entry entry) {
	final Uri dataUri = uri("urn:data#" + Math.random()) ;
	try {
	    mockery.checking(new Expectations() {{
		allowing(authorizer).canEditObject(entry.getId()) ; will(returnValue(true)) ;
		allowing(mapper).toRdf(data) ; will(returnValue(new HashSet<Statement>())) ;
		allowing(anzo).newId(); will(returnValue(dataUri)) ;
	    }}) ;
	} catch (Exception e) {
	    throw new RuntimeException("Defect: should never happen.") ; 
	}
	return dataUri ;
    }
    
    // [end] addDataToEntry(DataClassification, Entry) Tests
    
    // [start] addReferenceToEntry(Reference, Entry) Tests
    
    @Test(expected=AuthorizationException.class)
    public void willNotAddReferenceIfUserNotAuthorizedToEditEntry() throws AuthorizationException, DataSourceException {
	final Entry entry = new Entry() ;
	entry.setId("urn:entryId") ;
	Paper paper = new Paper() ;
	allowQueryFor(entry) ;
	mockery.checking(new Expectations() {{
	    oneOf(authorizer).canEditObject(entry.getId()) ; will(returnValue(false)) ;
	}}) ;
	
	dmr.addReferenceToEntry(paper, entry) ;
    }
    
    // [end] addReferenceToEntry(Reference, Entry) Tests
    
    // [start] updateEntry(Entry) Tests
    
    @Test(expected=AuthorizationException.class)
    public void willNotUpdateEntryIfUserNotAuthorizedToEditEntry() throws AuthorizationException, DataSourceException {
	final Entry entry = new Entry() ;
	entry.setId("urn:entryId") ;
	allowQueryFor(entry) ;
	mockery.checking(new Expectations() {{
	    oneOf(authorizer).canEditObject(entry.getId()) ; will(returnValue(false)) ;
	}}) ;
	
	dmr.updateEntry(entry) ;
    }
    
    // [end] updateEntry(Entry) Tests

    // [start] updateData(DataClassification) Tests
    
    @Test(expected=AuthorizationException.class)
    public void willNotUpdateDataIfUserNotAuthorizedToEditData() throws AuthorizationException, DataSourceException {
	final Image image = new Image() ;
	image.setId("urn:imageId") ;
	allowQueryFor(image) ;
	mockery.checking(new Expectations() {{
	    oneOf(authorizer).canEditObject(image.getId()) ; will(returnValue(false)) ;
	}}) ;
	
	dmr.updateData(image) ;
    }
    
    // [end] updateData(DataClassification) Tests
    
    // [start] updateReference(Reference) Tests
    
    @Test(expected=AuthorizationException.class) 
    public void willNotUpdateReferenceIfUserNotAuthorizedToEditReference() throws AuthorizationException, DataSourceException {
	final Paper paper = new Paper() ;
	paper.setId("urn:paperId") ;
	allowQueryFor(paper) ;
	mockery.checking(new Expectations() {{
	    oneOf(authorizer).canEditObject(paper.getId()) ; will(returnValue(false)) ;
	}}) ;
	
	dmr.updateReference(paper) ;
    }
    
    // [end] updateReference(Reference) Tests
    
    // [start] setUser(String) Tests
    
    @Test
    public void canBeSetToUseSpecificUserCredentials() throws AuthenticationException, InvalidUserIdException {
	final String userId = "myUserId" ;
	mockery.checking(new Expectations() {{
	    oneOf(authService).getUserAuthorizer(userId) ; will(returnValue(authorizer)) ;
	    oneOf(anzo).setCurrentUser(userId) ;
	}}) ;
	
	dmr.setUser(userId) ;
    }
    
    @Test(expected=AuthenticationException.class)
    public void throwsAuthenticationExceptionForInvalidId() throws InvalidUserIdException, AuthenticationException {
	mockery.checking(new Expectations() {{
	    oneOf(authService).getUserAuthorizer(with(any(String.class))) ; will(throwException(new InvalidUserIdException(""))) ;
	}}) ;
	
	dmr.setUser("anInvalidId") ;
    }
    
    // [end] setUser(String) Tests
    
    private void allowQueryFor(final Object obj) {
	allowQueryFor(obj, obj.getClass()) ;
    }
    
    private <T> void allowQueryFor(final Object obj, final Class<T> clazz) {
	try {
	    mockery.checking(new Expectations() {{
		allowing(queryProcessor).processObjectQuery(with(any(CQLQuery.class)), with(typeCompatibleWith(clazz))) ;
		will(returnValue(Collections.singletonList(obj))) ;
		allowing(queryProcessor).processObjectQuery(with(any(CQLQuery.class))) ;
		will(returnValue(Collections.singletonList(obj))) ;
	    }}) ;
	} catch (Exception ex) {
	    throw new RuntimeException("Defect: should never happen.") ;
	}
    }
    
    private void allowQueryForNonExistingObject(final Object obj) {
	allowQueryForNonExistingObject(obj, obj.getClass()) ;
    }
    
    private <T> void allowQueryForNonExistingObject(final Object obj, final Class<T> clazz) {
	try {
	    mockery.checking(new Expectations() {{
		allowing(queryProcessor).processObjectQuery(with(any(CQLQuery.class)), with(typeCompatibleWith(clazz))) ;
		will(returnValue(Collections.emptyList())) ;
		allowing(queryProcessor).processObjectQuery(with(any(CQLQuery.class))) ;
		will(returnValue(Collections.emptyList())) ;
	    }}) ;
	} catch (Exception e) {
	    throw new RuntimeException("Defect: should never happen.") ;
	}
    }
    
    private Expectations addsStatement(final Statement statement) {
	try {
	    return new Expectations() {{
		oneOf(anzo).updateGraphs(
		    with(any(Map.class)),
		    with(value(setWith(statement)))) ;
	    }} ;
	} catch (Exception e) {
	    throw new RuntimeException("Defect: should never happen.") ;
	}
    }
    
    private static Uri uri(String uri) {
	return rdfFactory.createUri(uri) ;
    }
    
    private static Statement statement(Resource subject, Uri predicate, Value object) {
	return rdfFactory.createStatement(subject, predicate, object) ;
    }
    
    private static <T> Set<T> set(T... objs) {
	Set<T> result = new HashSet<T>() ;
	for (T obj : objs) {
	    result.add(obj) ;
	}
	return result ;
    }
    
    private Matcher<Map> emptyMapOrNull() {
	return anyOf(
	    nullValue(Map.class), 
	    equalTo(Collections.emptyMap())) ;
    }
    
    private Matcher<Set<Statement>> setWith(final Statement... stmts) {
	return new BaseMatcher<Set<Statement>>() {
	    public boolean matches(Object obj) {
		if (!(obj instanceof Set)) {
		    return false ;
		}
		Set<?> set = (Set<?>) obj ;
	        for (Statement stmt : stmts) {
	            if (!set.contains(stmt)) {
	        	return false ;
	            }
	        }
	        return true ;
	    }
	    
	    public void describeTo(Description desc) {
	        desc.appendText("set with ")
	        	.appendValueList("[", ",", "]", stmts) ;
	    }
	} ;
    }
    
    private Matcher<Map<Uri, Set<Statement>>> value(Matcher<Set<Statement>> value) {
	return IsMapContaining.hasValue(value) ;
    }
    
    private Matcher<Map<Uri, Set<Statement>>> outKey(Uri key) {
	return not(IsMapContaining.<Uri, Set<Statement>>hasKey(key)) ;
    }
}
