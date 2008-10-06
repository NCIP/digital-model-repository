/*
 * caBIG� Open Source Software License v.2 Digital Model Repository (DMR)
 * 
 * Copyright Notice. Copyright 2008 Massachusetts General Hospital (�caBIG�
 * Participant�). Digital Model Repository (DMR) was created with NCI funding
 * and is part of the caBIG� initiative. The software subject to this notice and
 * license includes both human readable source code form and machine readable,
 * binary, object code form (the �caBIG� Software�).
 * 
 * This caBIG� Software License (the �License�) is between caBIG� Participant
 * and You. �You (or �Your�) shall mean a person or an entity, and all other
 * entities that control, are controlled by, or are under common control with
 * the entity. �Control� for purposes of this definition means (i) the direct or
 * indirect power to cause the direction or management of such entity, whether
 * by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of
 * the outstanding shares, or (iii) beneficial ownership of such entity.
 * 
 * License. Provided that You agree to the conditions described below, caBIG�
 * Participant grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caBIG� Software, including any copyright or patent rights
 * therein, to (i) use, install, disclose, access, operate, execute, reproduce,
 * copy, modify, translate, market, publicly display, publicly perform, and
 * prepare derivative works of the caBIG� Software in any manner and for any
 * purpose, and to have or permit others to do so; (ii) make, have made, use,
 * practice, sell, and offer for sale, import, and/or otherwise dispose of
 * caBIG� Software (or portions thereof); (iii) distribute and have distributed
 * to and by third parties the caBIG� Software and any modifications and
 * derivative works thereof; and (iv) sublicense the foregoing rights set out in
 * (i), (ii) and (iii) to third parties, including the right to license such
 * rights to further third parties. For sake of clarity, and not by way of
 * limitation, caBIG� Participant shall have no right of accounting or right of
 * payment from You or Your sublicensees for the rights granted under this
 * License. This License is granted at no charge to You. Your downloading,
 * copying, modifying, displaying, distributing or use of caBIG� Software
 * constitutes acceptance of all of the terms and conditions of this Agreement.
 * If you do not agree to such terms and conditions, you have no right to
 * download, copy, modify, display, distribute or use the caBIG� Software.
 * 
 * 1. Your redistributions of the source code for the caBIG� Software must
 * retain the above copyright notice, this list of conditions and the disclaimer
 * and limitation of liability of Article 6 below. Your redistributions in
 * object code form must reproduce the above copyright notice, this list of
 * conditions and the disclaimer of Article 6 in the documentation and/or other
 * materials provided with the distribution, if any.
 * 
 * 2. Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: �This product includes software
 * developed by Massachusetts General Hospital.� If You do not include such
 * end-user documentation, You shall include this acknowledgment in the caBIG�
 * Software itself, wherever such third-party acknowledgments normally appear.
 * 
 * 3. You may not use the names �Massachusetts General Hospital�, �MGH�, �The
 * National Cancer Institute�, �NCI�, �Cancer Bioinformatics Grid� or �caBIG��
 * to endorse or promote products derived from this caBIG� Software. This
 * License does not authorize You to use any trademarks, service marks, trade
 * names, logos or product names of either caBIG� Participant, NCI or caBIG�,
 * except as required to comply with the terms of this License.
 * 
 * 4. For sake of clarity, and not by way of limitation, You may incorporate
 * this caBIG� Software into Your proprietary programs and into any third party
 * proprietary programs. However, if You incorporate the caBIG� Software into
 * third party proprietary programs, You agree that You are solely responsible
 * for obtaining any permission from such third parties required to incorporate
 * the caBIG� Software into such third party proprietary programs and for
 * informing Your sublicensees, including without limitation Your end-users, of
 * their obligation to secure any required permissions from such third parties
 * before incorporating the caBIG� Software into such third party proprietary
 * software programs. In the event that You fail to obtain such permissions, You
 * agree to indemnify caBIG� Participant for any claims against caBIG�
 * Participant by such third parties, except to the extent prohibited by law,
 * resulting from Your failure to obtain such permissions.
 * 
 * 5. For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the caBIG� Software, or any derivative works
 * of the caBIG� Software as a whole, provided Your use, reproduction, and
 * distribution of the Work otherwise complies with the conditions stated in
 * this License.
 * 
 * 6. THIS caBIG� SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 * DISCLAIMED. IN NO EVENT SHALL THE MASSACHUSETTS GENERAL HOSPITAL OR ITS
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS caBIG� SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contact: Thomas Deisboeck (DEISBOEC@HELIX.MGH.HARVARD.EDU) Contributors:
 * INFOTECH Soft, Inc.
 */

package org.cvit.cabig.dmr.integration.queryprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import org.cvit.cabig.dmr.anzo.AnzoIo;
import org.cvit.cabig.dmr.anzo.AnzoIoException;
import org.cvit.cabig.dmr.integration.InMemoryDmrFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotechsoft.cagrid.cql.SerializationException;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.common.DataServiceI;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata;

public class InMemoryCvitDataServiceTests extends CvitDataServiceTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    
    private InMemoryDmrFactory factory = new InMemoryDmrFactory() ;
    
    public InMemoryCvitDataServiceTests() throws AnzoIoException {
	AnzoIo anzoIo = new AnzoIo() ;
	anzoIo.resetAnzo(
	    factory.getDatasetService(), 
	    getDataFile("init.ttl"), 
	    getDataFile("manifest.ttl")) ;
    }
    
    @Override
    protected DataServiceI getClient() {
	return new DataServiceI() {
	    public CQLQueryResults query(CQLQuery query) throws RemoteException, MalformedQueryExceptionType,
	        QueryProcessingExceptionType {
	        try {
		    return factory.getQueryProcessor().processQuery(query) ;
		} catch (MalformedQueryException e) {
		    throw new MalformedQueryExceptionType() ;
		} catch (QueryProcessingException e) {
		    throw new QueryProcessingExceptionType() ;
		}
	    }
	   
	    public ServiceSecurityMetadata getServiceSecurityMetadata() throws RemoteException {
	        throw new UnsupportedOperationException() ;
	    }
	} ;
    }
    
    private File getDataFile(String... path) {
	StringBuilder relativePath = new StringBuilder() ;
	for (String part : path) {
	    relativePath.append(part + File.separatorChar) ;
	}
	relativePath.deleteCharAt(relativePath.length() - 1) ;
	return new File(getDataDirectory(), relativePath.toString()) ;
    }
    
    private File getDataDirectory() {
	return new File("./test-data/query-tests") ;
    }
    
    protected void validateQuery(File queryFile, File resultsFile) {
	validateQuery(queryFile, resultsFile, false) ;
    }
    
    protected void validateQueryUsingIdAttribute(File queryFile, File resultsFile) {
	validateQuery(queryFile, resultsFile, true) ;
    }
    
    private void validateQuery(File queryFile, File resultsFile, boolean attribute) {
	InputStream query = null ;
	InputStream result = null ;
	try {
	    query = new FileInputStream(queryFile) ;
	    result = new FileInputStream(resultsFile) ;
	    logger.debug(
		"Running query {} with expected results {}.",
		queryFile.getCanonicalPath(), 
		resultsFile.getCanonicalPath()) ;
	    if (attribute) {
		validateQueryUsingIdAttribute(query, result) ;
	    } else {
		validateQuery(query, result) ;
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Exception loading file.", e) ;
	} finally {
	    close(query) ;
	    close(result) ;
	}
    }
    
    /**
     * Tests an Object Query for Entries
     * @throws SerializationException
     * @throws IOException
     * @throws QueryProcessingException 
     * @throws MalformedQueryException 
     */
    @Test
    public void basicEntryQuery() {
	validateQuery(
	    getDataFile("object-queries", "Entry_query.cql"), 
	    getDataFile("object-queries", "Entry_query.results")) ;
    }

    /**
     * Tests an Object Query for Organizations
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicOrganizationQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Organization_query.cql"), 
	    getDataFile("object-queries", "Organization_query.results")) ;
    }

    /**
     * Tests an Object Query for Paper
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicPaperQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Paper_query.cql"), 
	    getDataFile("object-queries", "Paper_query.results")) ;
    }

    /**
     * Tests an Object Query for Book
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicBookQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Book_query.cql"), 
	    getDataFile("object-queries", "Book_query.results")) ;
    }

    /**
     * Tests an Object Query for Review
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicReviewQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Review_query.cql"), 
	    getDataFile("object-queries", "Review_query.results")) ;
    }

    /**
     * Tests an Object Query for Persons
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicPersonQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Person_query.cql"), 
	    getDataFile("object-queries", "Person_query.results")) ;
    }

    /**
     * Tests an Object Query for Algorithm
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicAlgorithmQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Algorithm_query.cql"), 
	    getDataFile("object-queries", "Algorithm_query.results")) ;

    }

    /**
     * Tests an Object Query for SourceCode
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicSourceCodeQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "SourceCode_query.cql"), 
	    getDataFile("object-queries", "SourceCode_query.results")) ;

    }

    /**
     * Tests an Object Query for ExperimentalData
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicExperimentalDataQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "ExperimentalData_query.cql"), 
	    getDataFile("object-queries", "ExperimentalData_query.results")) ;

    }

    /**
     * Tests an Object Query for Image
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicImageQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Image_query.cql"), 
	    getDataFile("object-queries", "Image_query.results")) ;

    }

    /**
     * Tests an Object Query for Parameters
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicParametersQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Parameters_query.cql"),
	    getDataFile("object-queries", "Parameters_query.results")) ;

    }

    /**
     * Tests an Object Query for Movie
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicMovieQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "Movie_query.cql"), 
	    getDataFile("object-queries", "Movie_query.results")) ;

    }

    /**
     * Tests an Object Query for Entry Category
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicEntryCategoryQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "EntryCategory_query.cql"), 
	    getDataFile("object-queries", "EntryCategory_query.results")) ;
    }

    /**
     * Tests an Object Query for Entry Type
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void basicEntryTypeQuery() throws Exception {
	validateQuery(
	    getDataFile("object-queries", "EntryType_query.cql"), 
	    getDataFile("object-queries", "EntryType_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "id"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryIdAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_id_query.cql"), 
	    getDataFile("attribute_query", "Entry_id_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryTitleAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_title_query.cql"), 
	    getDataFile("attribute_query", "Entry_title_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "description"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryDescriptionAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_description_query.cql"), 
	    getDataFile("attribute_query", "Entry_description_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "abstractText"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryAbstractTextAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_abstractText_query.cql"), 
	    getDataFile("attribute_query", "Entry_abstractText_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "concept"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryConceptAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_concept_query.cql"), 
	    getDataFile("attribute_query", "Entry_concept_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "hypothesis"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryHypothesisAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_hypothesis_query.cql"), 
	    getDataFile("attribute_query", "Entry_hypothesis_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "conclusion"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryConclusionAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_conclusion_query.cql"), 
	    getDataFile("attribute_query", "Entry_conclusion_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "note"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryNoteAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_note_query.cql"), 
	    getDataFile("attribute_query", "Entry_note_query.results")) ;
    }

    /**
     * Tests an attribute query for the Entry attribute "keywords"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryKeywordsAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_keywords_query.cql"), 
	    getDataFile("attribute_query", "Entry_keywords_query.results")) ;
    }

    /**
     * Tests an attribute query for the EntryCategory attribute "id"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryCategoryIdAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "EntryCategory_id_query.cql"), 
	    getDataFile("attribute_query", "EntryCategory_id_query.results")) ;
    }

    /**
     * Tests an attribute query for the EntryCategory attribute "name"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryCategoryNameAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "EntryCategory_name_query.cql"), 
	    getDataFile("attribute_query", "EntryCategory_name_query.results")) ;
    }

    /**
     * Tests an attribute query for the EntryType attribute "id"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryTypeIdAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "EntryType_id_query.cql"), 
	    getDataFile("attribute_query", "EntryType_id_query.results")) ;
    }

    /**
     * Tests an attribute query for the EntryType attribute "name"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryTypeNameAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "EntryType_name_query.cql"), 
	    getDataFile("attribute_query", "EntryType_name_query.results")) ;
    }

    /**
     * Tests an attribute query for the Reference attribute "id"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void referenceIdAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Reference_id_query.cql"), 
	    getDataFile("attribute_query", "Reference_id_query.results")) ;
    }

    /**
     * Tests an attribute query for the Reference attribute "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void referenceTitleAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Reference_title_query.cql"), 
	    getDataFile("attribute_query", "Reference_title_query.results")) ;
    }

    /**
     * Tests an attribute query for the Reference attribute "description"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void referenceDescriptionAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Reference_description_query.cql"), 
	    getDataFile("attribute_query", "Reference_description_query.results")) ;
    }

    /**
     * Tests an attribute query for the Reference attribute "source"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void referenceSourceAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Reference_source_query.cql"), 
	    getDataFile("attribute_query", "Reference_source_query.results")) ;
    }

    /**
     * Tests an attribute query for the Reference attribute "comment"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void referenceCommentAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Reference_comment_query.cql"), 
	    getDataFile("attribute_query", "Reference_comment_query.results")) ;
    }

    /**
     * Tests an attribute query for the Organization attribute "id"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void organizationIdAttributeQuery() throws Exception {
	validateQueryUsingIdAttribute(
	    getDataFile("attribute_query", "Organization_id_query.cql"), 
	    getDataFile("attribute_query", "Organization_id_query.results")) ;
    }

    /**
     * Tests an attribute query for the Organization attribute "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void organizationTitleAttributeQuery() throws Exception {
	validateQueryUsingIdAttribute(
	    getDataFile("attribute_query", "Organization_name_query.cql"), 
	    getDataFile("attribute_query", "Organization_name_query.results")) ;
    }

    /**
     * Tests an attribute query for the Organization attribute "description"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void organizationDescriptionAttributeQuery() throws Exception {
	validateQueryUsingIdAttribute(
	    getDataFile("attribute_query", "Organization_description_query.cql"),
	    getDataFile("attribute_query", "Organization_description_query.results")) ;
    }

    /**
     * Tests an attribute query for the Organization attribute "source"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void organizationSourceAttributeQuery() throws Exception {
	validateQueryUsingIdAttribute(
	    getDataFile("attribute_query", "Organization_website_query.cql"), 
	    getDataFile("attribute_query", "Organization_website_query.results")) ;
    }

    /**
     * Tests an attribute query for the Organization attribute "geoCode"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void organizationGeoCodeAttributeQuery() throws Exception {
	validateQueryUsingIdAttribute(
	    getDataFile("attribute_query", "Organization_geoCode_query.cql"), 
	    getDataFile("attribute_query", "Organization_geoCode_query.results")) ;
    }

    /**
     * Tests an attribute query for the DataClassification attribute "id"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void dataClassificationIdAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "DataClassification_id_query.cql"), 
	    getDataFile("attribute_query", "DataClassification_id_query.results")) ;
    }

    /**
     * Tests an attribute query for the DataClassification attribute "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void dataClassificationTitleAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "DataClassification_title_query.cql"), 
	    getDataFile("attribute_query", "DataClassification_title_query.results")) ;
    }

    /**
     * Tests an attribute query for the DataClassification attribute "description"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void dataClassificationDescriptionAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "DataClassification_description_query.cql"), 
	    getDataFile("attribute_query", "DataClassification_description_query.results")) ;
    }

    /**
     * Tests an attribute query for the DataClassification attribute "source"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void dataClassificationSourceAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "DataClassification_source_query.cql"), 
	    getDataFile("attribute_query", "DataClassification_source_query.results")) ;
    }

    /**
     * Tests an attribute query for the DataClassification attribute "comment"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void dataClassificationCommentAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "DataClassification_comment_query.cql"), 
	    getDataFile("attribute_query", "DataClassification_comment_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "id"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personIdAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_id_query.cql"), 
	    getDataFile("attribute_query", "Person_id_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "userId"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personUserIdAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_userId_query.cql"), 
	    getDataFile("attribute_query", "Person_userId_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "firstName"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personFirstNameAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_firstName_query.cql"), 
	    getDataFile("attribute_query", "Person_firstName_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "surname"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personSurnameAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_lastName_query.cql"), 
	    getDataFile("attribute_query", "Person_lastName_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "phone"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personPhoneAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_phone_query.cql"), 
	    getDataFile("attribute_query", "Person_phone_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "mailbox"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personMailboxAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_emailAddress_query.cql"), 
	    getDataFile("attribute_query", "Person_emailAddress_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "source"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personSourceAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_website_query.cql"), 
	    getDataFile("attribute_query", "Person_website_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "depiction"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personDepictionAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_depiction_query.cql"), 
	    getDataFile("attribute_query", "Person_depiction_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "research"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personResearchAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_research_query.cql"), 
	    getDataFile("attribute_query", "Person_research_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "geoCode"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personGeoCodeAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_geoCode_query.cql"), 
	    getDataFile("attribute_query", "Person_geoCode_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "position"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personPositionAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_position_query.cql"), 
	    getDataFile("attribute_query", "Person_position_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "fax"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personFaxAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_fax_query.cql"), 
	    getDataFile("attribute_query", "Person_fax_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "address"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personAddressAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_address_query.cql"), 
	    getDataFile("attribute_query", "Person_address_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "researchInterest"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personResearchInterestAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_researchInterest_query.cql"), 
	    getDataFile("attribute_query", "Person_researchInterest_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personTitleAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_title_query.cql"), 
	    getDataFile("attribute_query", "Person_title_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "seniority"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personSeniorityAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_seniority_query.cql"), 
	    getDataFile("attribute_query", "Person_seniority_query.results")) ;
    }

    /**
     * Tests an attribute query for the Person attribute "group"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personGroupAttributeQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_group_query.cql"), 
	    getDataFile("attribute_query", "Person_group_query.results")) ;
    }

    /**
     * Tests multiple attribute query for the Entry attributes "id" and "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryIdAndTitleQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Entry_id_and_title_query.cql"), 
	    getDataFile("attribute_query", "Entry_id_and_title_query.results")) ;
    }

    /**
     * Tests multiple attribute query for the Reference attributes "id" and "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void referenceIdAndTitleQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Reference_id_and_title_query.cql"), 
	    getDataFile("attribute_query", "Reference_id_and_title_query.results")) ;
    }

    /**
     * Tests multiple attribute query for the Organization attributes "id" and "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void organizationIdAndTitleQuery() throws Exception {
	validateQueryUsingIdAttribute(
	    getDataFile("attribute_query", "Organization_id_and_name_query.cql"),
	    getDataFile("attribute_query", "Organization_id_and_name_query.results")) ;
    }

    /**
     * Tests multiple attribute query for the DataClassification attributes "id" and "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void dataClassificationIdAndTitleQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "DataClassification_id_and_title_query.cql"), 
	    getDataFile("attribute_query", "DataClassification_id_and_title_query.results")) ;
    }

    /**
     * Tests multiple attribute query for the Person attributes "id" and "title"
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personIdAndTitleQuery() throws Exception {
	validateQuery(
	    getDataFile("attribute_query", "Person_id_and_title_query.cql"), 
	    getDataFile("attribute_query", "Person_id_and_title_query.results")) ;
    }

    /**
     * Tests the "allOf" group query.
     */
    @Test
    public void groupAllOfQuery1() throws Exception {
	validateQuery(
	    getDataFile("group_all", "All_of_with_one_result.cql"), 
	    getDataFile("group_all", "All_of_with_one_result.results")) ;
    }

    /**
     * Tests the "allOf" group query. 
     */
    //This query should return no Entries.
    @Test
    public void groupAllOfQuery2() throws Exception {
	validateQuery(
	    getDataFile("group_all", "All_of_with_no_result.cql"), 
	    getDataFile("group_all", "All_of_with_no_result.results")) ;
    }

    /**
     * Tests the "anyOf" group query. 
     */
    //This query should return one Entry: "org.cvit.cabig.dmr.domain.Entry@ed2ff832".
    @Test
    public void groupAnyOfQuery1() throws Exception {
	validateQuery(
	    getDataFile("group_any", "Any_of_with_one_result_1.cql"), 
	    getDataFile("group_any", "Any_of_with_one_result_1.results")) ;
    }

    /**
     * Tests the "anyOf" group query. 
     */
    //This query should return one Entry: "org.cvit.cabig.dmr.domain.Entry@8961a082".
    @Test
    public void groupAnyOfQuery2() throws Exception {
	validateQuery(
	    getDataFile("group_any", "Any_of_with_one_result_2.cql"), 
	    getDataFile("group_any", "Any_of_with_one_result_2.results")) ;
    }

    /**
     * Tests the "anyOf" group query.
     */
    //This query should return no Entries.
    @Test
    public void groupAnyOfQuery3() throws Exception {
	validateQuery(
	    getDataFile("group_any", "Any_of_with_no_result.cql"), 
	    getDataFile("group_any", "Any_of_with_no_result.results")) ;
    }

    /**
     * Tests the wild card symbol '%'. 
     * @throws SerializationException
     * @throws IOException
     */
    //This should return the Entry with id "org.cvit.cabig.dmr.domain.Entry@69a665ad".
    @Test
    public void wildCardQuery1() throws Exception {
	validateQuery(
	    getDataFile("wild_card", "No_wild_card.cql"), 
	    getDataFile("wild_card", "No_wild_card.results")) ;
    }

    /**
     * Tests the wild card symbol '%'.
     * @throws SerializationException
     * @throws IOException
     */
    //This should return all Entries.
    @Test
    public void wildCardQuery2() throws Exception {
	validateQuery(
	    getDataFile("wild_card", "Only_a_wild_card.cql"), 
	    getDataFile("wild_card", "Only_a_wild_card.results")) ;
    }

    /**
     * Tests the wild card symbol '%'. 
     * @throws SerializationException
     * @throws IOException
     */
    //This should return the Entry with id "org.cvit.cabig.dmr.domain.Entry@69a665ad".
    @Test
    public void wildCardQuery3() throws Exception {
	validateQuery(
	    getDataFile("wild_card", "Wild_card_with_text.cql"), 
	    getDataFile("wild_card", "Wild_card_with_text.results")) ;
    }

    /**
     * Tests the association between Entry and Image. 
     * @throws SerializationException
     * @throws IOException
     */
    /*
     * Using the title "model2d_egfr-1.0_frame-001.JPG" of an image, it should return the 
     * following Entries: "org.cvit.cabig.dmr.domain.Entry@9bf1a82" and "org.cvit.cabig.dmr.domain.Entry@8961a082"
     */
    @Test
    public void entryImageAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Image_association.cql"), 
	    getDataFile("association", "Entry_Image_association.results")) ;
    }

    /**
     * Tests the association between Entry and Parameters. 
     * @throws SerializationException
     * @throws IOException
     */
    //Using the wild card for the title of the parameter, it should not return 
    //any entries.
    @Test
    public void entryParametersAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Parameter_association.cql"), 
	    getDataFile("association", "Entry_Parameter_association.results")) ;
    }

    /**
     * Tests the association between Entry and Movie. 
     * @throws SerializationException
     * @throws IOException
     */
    /*
     * Using the title "2D Model Simulation Movie for EGF Concentration 1.0." of 
     * a movie, it should return the following Entries: "org.cvit.cabig.dmr.domain.Entry@8961a082"
     */
    @Test
    public void entryMovieAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Movie_association.cql"), 
	    getDataFile("association", "Entry_Movie_association.results")) ;
    }

    /**
     * Tests the association between Entry and ExperimentalData. 
     * @throws SerializationException
     * @throws IOException
     */
    /*
     * Using the title "Experimental Data Title HTML" of 
     * an ExperimentalData, it should return the following Entries: "org.cvit.cabig.dmr.domain.Entry@9bf1a82",
     * "org.cvit.cabig.dmr.domain.Entry@ed2ff832", and "org.cvit.cabig.dmr.domain.Entry@8961a082".
     */
    @Test
    public void entryExperimentalDataAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_ExperimentalData_association.cql"), 
	    getDataFile("association", "Entry_ExperimentalData_association.results")) ;
    }

    /**
     * Tests the association between Entry and SourceCode. 
     * @throws SerializationException
     * @throws IOException
     */
    /*
     * Using the wild card for the title of a SourceCode, it should return 
     * no Entries.
     */
    @Test
    public void entrySourceCodeAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_SourceCode_association.cql"), 
	    getDataFile("association", "Entry_SourceCode_association.results")) ;
    }

    /**
     * Tests the association between Entry and Algorithm. 
     * @throws SerializationException
     * @throws IOException
     */
    /*
     * Using the title "My Data" for an Algorithm, 
     * it should return the following Entries: "org.cvit.cabig.dmr.domain.Entry@9bf1a82"
     */
    @Test
    public void entryAlgorithmAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Algorithm_association.cql"), 
	    getDataFile("association", "Entry_Algorithm_association.results")) ;
    }

    /**
     * Tests the association between Entry and Paper. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryPaperAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Paper_association.cql"), 
	    getDataFile("association", "Entry_Paper_association.results")) ;
    }

    /**
     * Tests the association between Entry and Book. 
     * @throws SerializationException
     * @throws IOException
     */
    /*
     * Using the wild card for the title of the book, it should not return 
     * any entries.
     */
    @Test
    public void entryBookAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Book_association.cql"), 
	    getDataFile("association", "Entry_Book_association.results")) ;
    }

    /**
     * Tests the association between Entry and Review. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryReviewAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Review_association.cql"), 
	    getDataFile("association", "Entry_Review_association.results")) ;
    }

    /**
     * Tests the association between Entry and Organization.
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryOrganizationAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Organization_association.cql"), 
	    getDataFile("association", "Entry_Organization_association.results")) ;
    }

    /**
     * Tests the association between Entry and Person.
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryPersonAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_Person_association.cql"), 
	    getDataFile("association", "Entry_Person_association.results")) ;
    }

    /**
     * Tests the association between Entry and EntryCategory.
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryEntryCategoryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_EntryCategory_association.cql"), 
	    getDataFile("association", "Entry_EntryCategory_association.results")) ;
    }

    /**
     * Tests the association between Entry and EntryType.
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryEntryTypeAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Entry_EntryType_association.cql"), 
	    getDataFile("association", "Entry_EntryType_association.results")) ;
    }

    /**
     * Tests the association between Image and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void imageEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Image_Entry_association.cql"), 
	    getDataFile("association", "Image_Entry_association.results")) ;
    }

    /**
     * Tests the association between Paramaters and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void parametersEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Parameter_Entry_association.cql"), 
	    getDataFile("association", "Parameter_Entry_association.results")) ;
    }

    /**
     * Tests the association between Movie and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void movieEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Movie_Entry_association.cql"), 
	    getDataFile("association", "Movie_Entry_association.results")) ;
    }

    /**
     * Tests the association between ExperimentalData and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void experimentalDataEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "ExperimentalData_Entry_association.cql"), 
	    getDataFile("association", "ExperimentalData_Entry_association.results")) ;
    }

    /**
     * Tests the association between SourceCode and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void sourceCodeEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "SourceCode_Entry_association.cql"),
	    getDataFile("association", "SourceCode_Entry_association.results")) ;
    }

    /**
     * Tests the association between Algorithm and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void algorithmEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Algorithm_Entry_association.cql"), 
	    getDataFile("association", "Algorithm_Entry_association.results")) ;
    }

    /**
     * Tests the association between Paper and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void paperEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Paper_Entry_association.cql"), 
	    getDataFile("association", "Paper_Entry_association.results")) ;
    }

    /**
     * Tests the association between Book and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void bookEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Book_Entry_association.cql"), 
	    getDataFile("association", "Book_Entry_association.results")) ;
    }

    /**
     * Tests the association between Review and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void reviewEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Review_Entry_association.cql"), 
	    getDataFile("association", "Review_Entry_association.results")) ;
    }

    /**
     * Tests the association between Organization and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void organizationEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Organization_Entry_association.cql"), 
	    getDataFile("association", "Organization_Entry_association.results")) ;
    }

    /**
     * Tests the association between Person and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Person_Entry_association.cql"), 
	    getDataFile("association", "Person_Entry_association.results")) ;
    }

    /**
     * Tests the association between EntryCategory and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryCategoryEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "EntryCategory_Entry_association.cql"), 
	    getDataFile("association", "EntryCategory_Entry_association.results")) ;
    }

    /**
     * Tests the association between EntryType and Entry. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void entryTypeEntryAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "EntryType_Entry_association.cql"), 
	    getDataFile("association", "EntryType_Entry_association.results")) ;
    }

    /**
     * Tests the association between Person and Organization. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personOrganizationAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Person_Organization_association.cql"), 
	    getDataFile("association", "Person_Organization_association.results")) ;
    }

    /**
     * Tests the association between Organization and Person. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void organizationPersonAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Organization_Person_association.cql"), 
	    getDataFile("association", "Organization_Person_association.results")) ;
    }

    /**
     * Tests the association between Paper, Entry and Image. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void paperEntryImageAssociationQuery() throws Exception {
	validateQuery(getDataFile("association", "Paper_Entry_Image_association.cql"), 
	    getDataFile("association", "Paper_Entry_Image_association.results")) ;
    }

    /**
     * Tests the association between Person, Organization, Entry and Review. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void personOrganizationEntryReviewAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Person_Org_Entry_Review.cql"), 
	    getDataFile("association", "Person_Org_Entry_Review.results")) ;
    }

    /**
     * Tests the association between Movie, Entry, Organization, Person, Entry and Book. 
     * @throws SerializationException
     * @throws IOException
     */
    @Test
    public void movieEntryOrganizationPersonEntryBookAssociationQuery() throws Exception {
	validateQuery(
	    getDataFile("association", "Mov_Ent_Org_Person_Ent_Book.cql"), 
	    getDataFile("association", "Mov_Ent_Org_Person_Ent_Book.results")) ;
    }
}
