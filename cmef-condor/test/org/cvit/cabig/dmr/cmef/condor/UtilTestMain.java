package org.cvit.cabig.dmr.cmef.condor;

import java.io.File;

public class UtilTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Util.copyFile(new File("resources/ExecutableWrapper.jar"), new File(
				"jobs"));

	}

}
