/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef;

import org.cvit.cabig.dmr.cmef.domain.ComputationJob ;
import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.cvit.cabig.dmr.cmef.domain.ComputingPlatform ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.cvit.cabig.dmr.cmef.domain.Parameter ;
import org.cvit.cabig.dmr.cmef.domain.ParameterValue ;
import org.cvit.cabig.dmr.cmef.domain.ProgrammingPlatform ;
import org.cvit.cabig.dmr.domain.Entry ;

public interface CmefUriGenerator {

    String generateModelUri(Entry entry, ComputationalModel model) ;

    String generateDocumentUri(Entry entry, ComputationalModel model, File document) ;

    String generateModelFileUri(Entry entry, ComputationalModel model, File file) ;

    String generatePlatformUri(Entry entry, ComputationalModel model, ComputingPlatform platform) ;

    String generateProgramUri(Entry entry, ComputationalModel model, ProgrammingPlatform programmingPlatform) ;

    String generateParameterUri(Entry entry, ComputationalModel model, Parameter parameter) ;

    //TODO: should Job generate methods include entry???
    String generateJobUri(ComputationalModel model, ComputationJob job) ;
    
    String generateResultFileUri(ComputationalModel model, ComputationJob job, File file) ;

    String generateParameterValueUri(ComputationalModel model, ComputationJob job, ParameterValue value) ;

}
