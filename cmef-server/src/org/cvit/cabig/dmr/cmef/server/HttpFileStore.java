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
package org.cvit.cabig.dmr.cmef.server;

import java.io.IOException ;
import java.io.InputStream ;
import java.util.Calendar ;
import java.util.Collections ;

import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.domain.File ;
import org.restlet.data.Method ;
import org.restlet.data.Status ;
import org.restlet.data.Tag ;
import org.restlet.representation.InputRepresentation ;
import org.restlet.resource.ClientResource ;

public class HttpFileStore implements FileStore {
    private String storeUrl ;
    private String rootUrl ;
    private String user ;

    public HttpFileStore(String storeUrl, String rootUrl, String user) {
	if (storeUrl == null || "".equals(storeUrl)) {
	    throw new IllegalArgumentException("Must specify HTTP file store URL.") ;
	}
	if (user == null || "".equals(user)) {
	    throw new IllegalArgumentException("Must specify user name.") ;
	}
	if (rootUrl == null || "".equals(rootUrl)) {
	    rootUrl = storeUrl ;
	}
	this.storeUrl = storeUrl ;
	this.rootUrl = rootUrl ;
	this.user = user ;
    }
    
    public File storeFile(String name, InputStream data) throws IOException {
	String localRoot = buildLocalRoot() ;
	String localName = getLocalFileName(name) ;
	String storeRoot = storeUrl + localRoot ;
	String extRoot = rootUrl + localRoot ;
	
	String fileUrl = null ;
	String extUrl = null ;
	boolean stored = false ;
	while (!stored) {
	    String storeName = buildStoreName(storeRoot, localName) ;
	    fileUrl = storeRoot + storeName ;
	    extUrl = extRoot + storeName ;
	    Status status = putFile(data, fileUrl) ;
	    if (!status.isSuccess()) {
		if (!Status.CLIENT_ERROR_PRECONDITION_FAILED.equals(status)) {
		    throw new IOException("Exception communicating with HTTP file store: " + status) ;
		}
	    } else {
		stored = true ;
	    }
	}

	File result = new File() ;
	result.setName(localName) ;
	result.setSource(extUrl) ;
	return result ;
    }
    
    public void deleteFile(String fileUri) throws IOException {
	ClientResource res = new ClientResource(fileUri) ;
	
	res.setMethod(Method.DELETE) ;
	res.handle() ;
	if (res.getStatus().isError()) {
	    throw new IOException("Error deleting " + fileUri + ": " + res.getStatus() + ".") ;
	}
    }

    private String buildLocalRoot() {
	Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        return new StringBuilder(user).append("/").append(year).append("/").append(month).append("/").append(day).append("/").toString() ;
    }
    
    private String getLocalFileName(String fileName) {
	int startIndex = Math.max(fileName.lastIndexOf("/"), fileName.lastIndexOf("\\")) + 1 ;
        return (startIndex < fileName.length()) ? fileName.substring(startIndex) : fileName ;
    }
    
    private String buildStoreName(String storeDir, String fileName) throws IOException {
	String storeName = removeIllegalURIChars(fileName) ;
	int count = 0 ;
	String result = storeName ;
	while (exists(storeDir + result)) {
	    result = count++ + "_" + storeName ;
	}
	return result ;
    }
    
    private boolean exists(String url) throws IOException {
	ClientResource res = new ClientResource(url) ;
	
	res.setMethod(Method.HEAD) ;
	res.handle() ;
	if (res.getStatus().isSuccess()) {
	    return true ;
	} else {
	    if (Status.CLIENT_ERROR_NOT_FOUND.equals(res.getStatus())) {
		return false ;
	    } else {
		throw new IOException("Exception communicating with HTTP file store: " + res.getStatus()) ;
	    }
	}
    }
    
    private Status putFile(InputStream data, String url) {
	ClientResource res = new ClientResource(url) ;
	
	res.setMethod(Method.PUT) ;
	res.getConditions().setNoneMatch(Collections.singletonList(Tag.ALL)) ;
	res.getRequest().setEntity(new InputRepresentation(data)) ;
	res.handle() ;
	return res.getStatus() ;
    }
    
    private static String removeIllegalURIChars(String s) {
        String s2 = removeSpaces(s);

        char[] chars = s2.toCharArray();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\'' || chars[i] == '"')
                b.append("_");
            else
                b.append(chars[i]);
        }

        return b.toString();
    }

    private static String removeSpaces(String s) {
        char[] chars = s.toCharArray();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ')
                b.append("_");
            else
                b.append(chars[i]);
        }
        return b.toString();
    }
}
