/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */

package org.cvit.cabig.dmr.integration;

import org.cvit.cabig.dmr.DMRService;
import org.cvit.cabig.dmr.DmrQueryProcessor;

public interface DmrFactory {

    public DmrQueryProcessor getQueryProcessor() ;
    
    public DMRService getService() ;
}
