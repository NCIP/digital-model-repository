/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


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
