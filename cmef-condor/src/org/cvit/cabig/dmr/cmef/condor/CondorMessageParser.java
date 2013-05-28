/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


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
