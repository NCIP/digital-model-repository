/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef;

public class CmefServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CmefServiceException() {}
	public CmefServiceException(String msg) {
		super(msg);
	}
	public CmefServiceException(String msg, Exception cause) {
		super(msg, cause);
	}
	public CmefServiceException(Exception cause) {
		super(cause);
	}
}
