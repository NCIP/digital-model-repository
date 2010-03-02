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
package org.cvit.cabig.dmr.cmef.condor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cvit.cabig.dmr.cmef.CmefServiceException;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CondorMessageParser {

	public static void updateStatus(InputStream condorClassAdsXMLStream,
			ComputationJob job) throws IOException {
		updateStatus(createDocument(condorClassAdsXMLStream), job);
	}

	protected static void updateStatus(Document condorClassAdsDoc,
			ComputationJob job) throws IOException {
		Document doc = condorClassAdsDoc;
		Element node = doc.getDocumentElement();
		if (!"classads".equalsIgnoreCase(node.getTagName()))
			throw new IOException("Document is not in Condor ClassAds format.");
		// <classads><c><a></a>...</c></classads>
		// All nodes in classads are in format <a
		// n="name"><'type'>'val'</type></a>
		// <i> integer
		// <s> string
		// <b v="t/f"/> boolean
		// <e> expression
		// <r> double(?)
		Node cNode = node.getFirstChild(); // c-node
		if (cNode != null) {
			NodeList nodeList = cNode.getChildNodes(); // a-nodes
			for (int i = 0; i < nodeList.getLength(); i++) {
				add(nodeList.item(i), job);
			}
		}
	}

	// TODO note - this may not be the most efficient implementation
	protected static void add(Node node, ComputationJob job) {
		
		Node attribute = node.getAttributes().getNamedItem("n");
		String attributeName = attribute.getNodeValue();
		// // dateSubmitted
		if ("QDate".equalsIgnoreCase(attributeName)) { // QDate is consistent
			// across _q and
			// _history;
			// JobStartDate is only
			// in _history
			String nodeValue = nodeValue(node);
			if (nodeValue != null && nodeValue.length() > 1) {
				try {
					Date date = new Date(Long.parseLong(nodeValue) * 1000);
					job.setDateSubmitted(date);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// dataCompleted
		} else if ("CompletionDate".equalsIgnoreCase(attributeName)) {
			String nodeValue = nodeValue(node);
			//nodeValue == 0 means job was removed and not given CompletedDate
			if (nodeValue != null && nodeValue.length() > 1) {
				try {
					Date date = new Date(Long.parseLong(nodeValue) * 1000);
					job.setDateCompleted(date);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		// jobStatus
		else if ("JobStatus".equalsIgnoreCase(attributeName)) {
			String nodeValue = nodeValue(node);
			String jobStatus = jobStatus2String(nodeValue);
			job.setJobStatus(jobStatus);
			// TODO update JobStatus with 'ExitStatus'?
		}
	}

	protected static String nodeValue(Node node) {
		node = node.getFirstChild(); // <a><i>

		String value = node.getTextContent();
		// String value = node.getNodeValue();
		return value;
	}

	// http://pages.cs.wisc.edu/~adesmet/status.html
	/**
	 * Job Status JobStatus in job ClassAds
	 * 
	 * 0 Unexpanded U 1 Idle I 2 Running R 3 Removed X 4 Completed C 5 Held H 6
	 * Submission_err E
	 */
	public static String jobStatus2String(String jobStatusCode) {
		int code = -1;
		String value = jobStatusCode;
		try {
			code = Integer.parseInt(jobStatusCode);
			switch (code) {
			case 0:
				value = "Unexpanded";
				break;
			case 1:
				value = "Idle";
				break;
			case 2:
				value = "Running";
				break;
			case 3:
				value = "Removed";
				break;
			case 4:
				value = "Completed";
				break;
			case 5:
				value = "Held";
				break;
			case 6:
				value = "Submission Error";
				break;
			}
		} catch (NumberFormatException ex) {
			// ignore: try character conversion
			if ("U".equalsIgnoreCase(jobStatusCode))
				value = "Unexpanded";
			else if ("I".equalsIgnoreCase(jobStatusCode))
				value = "Idle";
			else if ("R".equalsIgnoreCase(jobStatusCode))
				value = "Running";
			else if ("X".equalsIgnoreCase(jobStatusCode))
				value = "Removed";
			else if ("C".equalsIgnoreCase(jobStatusCode))
				value = "Completed";
			else if ("H".equalsIgnoreCase(jobStatusCode))
				value = "Held";
			else if ("E".equalsIgnoreCase(jobStatusCode))
				value = "Submission Error";
		}
		return value;
	}

	/* Copied from INFOTECH Soft: com.infotechsoft.eforms.xml.DocumentReader */
	/**
	 * Reads the given InputStream to parse a Document. Wraps all resulting
	 * exceptions as an IOException.
	 * 
	 * @param input
	 *            the InputStream to read the Document from.
	 * @return Document parsed from the input.
	 * @throws IOException
	 *             if the Document cannot be read from the InputStream for any
	 *             reason.
	 */
	public static Document createDocument(InputStream input) throws IOException {
		try {
			// 1. get a Document Builder Factory
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			docBuilderFactory.setValidating(false);
			docBuilderFactory.setCoalescing(true);
			docBuilderFactory.setIgnoringComments(true);
			docBuilderFactory.setIgnoringElementContentWhitespace(true);
			// 2. get a Document Builder
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			// 3. parse the InputStream into a Document
			Document document = docBuilder.parse(input);
			// 4. return the result
			return document;
		} catch (javax.xml.parsers.ParserConfigurationException pcEx) {
			throw new java.io.IOException(pcEx.getMessage());
		} catch (org.xml.sax.SAXException saxEx) {
			throw new java.io.IOException(saxEx.getMessage());
		}
	}

	public static Document createDocumentFromString(String input)
			throws IOException {
		return createDocument(new ByteArrayInputStream(input.getBytes("UTF-8")));
	}

	public static Document createDocument(File filename) throws IOException {
		return createDocument(new FileInputStream(filename));
	}

	public static Integer parseCondorSubmitStdout(InputStream condorSubmitStdout)
			throws IOException, CmefServiceException {
		// Note: Condor Job ID
		// stdout:
		// Submitting job(s).
		// Logging submit event(s).
		// 1 job(s) submitted to cluster ### // note: if multiple Queued jobs
		// are started, all are prefixed with clusterid
		// stdlog (condor log)
		// 000 (###.###.###) <date> Job submitted from host: <IP>
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				condorSubmitStdout));
		String line = null;
		String message = null;
		boolean hasError = false;
		String condorJobId = null;
		while ((line = bReader.readLine()) != null) {
			if (message == null)
				message = line;
			else
				message = message + "\r\n" + line;
			if (line.toLowerCase().contains("error")) {
				hasError = true;
			} else if (line.toLowerCase().contains("cluster")) {
				int index = line.lastIndexOf("cluster");
				condorJobId = line.substring(index + 8).trim();
				index = condorJobId.indexOf('.');
				if (index > 0)
					condorJobId = condorJobId.substring(0, index);
			}
		}
		if (hasError)
			throw new CmefServiceException("Error starting Condor job :"
					+ message);

		Integer condorJobIdInt = null;
		try {
			condorJobIdInt = Integer.parseInt(condorJobId);
		} catch (NumberFormatException ex) {
			throw new CmefServiceException("Error parsing Condor job id '"
					+ condorJobId + "'", ex);
		}
		return condorJobIdInt;
	}

	public static boolean containsEntryForInputStream(InputStream condorClassAdsXMLStream) throws IOException {
		return containsEntryForInputStream(createDocument(condorClassAdsXMLStream));
	}
	
	public static boolean containsEntryForInputStream(Document document) throws IOException {
		Document doc = document;
		Element node = doc.getDocumentElement();
		if (!"classads".equalsIgnoreCase(node.getTagName()))
			throw new IOException("Document is not in Condor ClassAds format.");
		Node cNode = node.getFirstChild(); // c-node
		if (cNode != null) {
			return true;
		}
		return false;
	}
}
