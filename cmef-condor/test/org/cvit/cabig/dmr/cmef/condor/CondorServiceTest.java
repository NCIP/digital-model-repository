/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */

/**
 * 
 */
package org.cvit.cabig.dmr.cmef.condor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import org.cvit.cabig.dmr.cmef.condor.CondorMessageParser;
import org.cvit.cabig.dmr.cmef.domain.ComputationJob;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author Thomas
 * 
 */
public class CondorServiceTest extends TestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testJobStatus2String() {
		assertEquals(CondorMessageParser.jobStatus2String("0"), "Unexpanded");
		assertEquals(CondorMessageParser.jobStatus2String("1"), "Idle");
		assertEquals(CondorMessageParser.jobStatus2String("2"), "Running");
		assertEquals(CondorMessageParser.jobStatus2String("3"), "Removed");
		assertEquals(CondorMessageParser.jobStatus2String("4"), "Completed");
		assertEquals(CondorMessageParser.jobStatus2String("5"), "Held");
		assertEquals(CondorMessageParser.jobStatus2String("6"),
				"Submission Error");
		//
		assertEquals(CondorMessageParser.jobStatus2String("U"), "Unexpanded");
		assertEquals(CondorMessageParser.jobStatus2String("I"), "Idle");
		assertEquals(CondorMessageParser.jobStatus2String("R"), "Running");
		assertEquals(CondorMessageParser.jobStatus2String("X"), "Removed");
		assertEquals(CondorMessageParser.jobStatus2String("C"), "Completed");
		assertEquals(CondorMessageParser.jobStatus2String("H"), "Held");
		assertEquals(CondorMessageParser.jobStatus2String("E"),
				"Submission Error");
		//
		assertEquals(CondorMessageParser.jobStatus2String("-1"), "-1");
		assertEquals(CondorMessageParser.jobStatus2String("7"), "7");
		assertEquals(CondorMessageParser.jobStatus2String("NaN"), "NaN");
		assertEquals(CondorMessageParser.jobStatus2String("Status"), "Status");
		assertEquals(CondorMessageParser.jobStatus2String("4.5"), "4.5");
	}

	@Test
	public void testUpdateStatus_q() throws IOException {
		org.w3c.dom.Document document = CondorMessageParser
				.createDocument(new java.io.File("test/condor_q_13.xml")); // unfinished
																			// job
		ComputationJob job = new ComputationJob();
		CondorMessageParser.updateStatus(document, job);
		System.out.println("JobStatus," + "Idle," + job.getJobStatus());
		System.out.println("DateSubmitted," + new Date(1265521623L * 1000)
				+ "," + job.getDateSubmitted());
		System.out.println("DateCompleted," + null + ","
				+ job.getDateCompleted());

		assertEquals("JobStatus", "Idle", job.getJobStatus());
		assertEquals("DateSubmitted", new Date(1265521623L * 1000), job
				.getDateSubmitted());
		assertNull("DateCompleted", job.getDateCompleted());
	}

	@Test
	public void testUpdateStatus_history() throws IOException {
		org.w3c.dom.Document document = CondorMessageParser
				.createDocument(new java.io.File("test/condor_history_13.xml")); // finished
																					// job
		ComputationJob job = new ComputationJob();
		CondorMessageParser.updateStatus(document, job);
		System.out.println("JobStatus," + "Completed," + job.getJobStatus());
		System.out.println("DateSubmitted," + new Date(1265521623L * 1000)
				+ "," + job.getDateSubmitted());
		System.out.println("DateCompleted," + new Date(1265521683L * 1000)
				+ "," + job.getDateCompleted());

		assertEquals("JobStatus", "Completed", job.getJobStatus());
		assertEquals("DateSubmitted", new Date(1265521623L * 1000), job
				.getDateSubmitted());
		assertEquals("DateCompleted", new Date(1265521683L * 1000), job
				.getDateCompleted());
	}

	@Test
	public void testParseCommandLine_submit() {
		String[] commandLine = {
				"Submitting job(s).\r\n" + "Logging submit event(s).\r\n"
						+ "1 job(s) submitted to cluster 0.\r\n",

				"Submitting job(s).\r\n" + "Logging submit event(s).\r\n"
						+ "15 job(s) submitted to cluster 1.\r\n",

				"Submitting job(s).\r\n" + "Logging submit event(s).\r\n"
						+ "1 job(s) submitted to cluster 98.\r\n",

				"Submitting job(s).\r\n" + "Logging submit event(s).\r\n"
						+ "1 job(s) submitted to cluster 1024.\r\n",

				"Submitting job(s).\r\n" + "Logging submit event(s).\r\n"
						+ "1 job(s) submitted to cluster 1024.0.\r\n",

				"Error submitting job.",

				"ERROR submitting job.",

				"Cannot run job, ERROR",

				"Run job error." };
		Integer[] expectedJobIds = { Integer.valueOf(0), Integer.valueOf(1),
				Integer.valueOf(98), Integer.valueOf(1024),
				Integer.valueOf(1024), null, null, null, null };
		for (int i = 0; i < commandLine.length; i++) {
			System.out.println("Parsing...\r\n" + commandLine[i]);

			BufferedReader bReader = new BufferedReader(new StringReader(
					commandLine[i]));
			String line = null;
			String message = null;
			boolean hasError = false;
			String condorJobId = null;
			try {
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
						Integer intValue = Integer.valueOf(condorJobId);

						if (!intValue.equals(expectedJobIds[i]))
							System.err
									.println("Values are not equal. Expected "
											+ expectedJobIds[i] + " Actual "
											+ intValue);
					}
				}
				if (hasError)
					System.out.println("Error starting Condor job :" + message);
				else
					System.out.println("Condor Job Id: " + condorJobId);

			} catch (Exception ex) {
				System.err.println(commandLine[i]);
				ex.printStackTrace();
			}
		}
	}

}
