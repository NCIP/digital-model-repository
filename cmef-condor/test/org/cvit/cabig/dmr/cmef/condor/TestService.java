package org.cvit.cabig.dmr.cmef.condor;

import java.util.Enumeration;
import java.util.Iterator;

import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.cvit.cabig.dmr.cmef.condor.CmefServiceTest;

public class TestService {
	public static void main(String[] args) {
		
		TestSuite suite= new TestSuite(CmefServiceTest.class);		
		TestResult results = new TestResult();
		
		try {
			suite.run(results);
		} finally {
			
			int numOfTests = results.runCount();
			int errors = results.errorCount();
			int failures = results.failureCount();
			
			System.err.println("Tests executed:\t\t\t" + numOfTests);
			System.err.println("Tests succeeded:\t\t" + (numOfTests - (errors + failures)));
			
			System.err.println("Tests resulting in error:\t" + errors);
			Enumeration<TestFailure> errorTests = results.errors();
			System.err.println("Tests with errors:");
			while (errorTests.hasMoreElements()) {
				TestFailure fail = errorTests.nextElement();
				System.err.println(fail.failedTest());
			}
			
			System.err.println("Tests resulting in failure:\t" + failures);
			errorTests = results.failures();
			System.err.println("Tests with failures:");
			while (errorTests.hasMoreElements()) {
				TestFailure fail = errorTests.nextElement();
				System.err.println(fail.failedTest());
			}
		}
	}
}
