/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr;

public class Defect extends RuntimeException {

    public Defect(String msg, Throwable cause) {
	super(msg, cause) ;
    }
}
