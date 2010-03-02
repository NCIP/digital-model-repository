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
import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.util.Properties ;
import java.util.concurrent.TimeUnit ;
import java.util.concurrent.atomic.AtomicReference ;

import org.cvit.cabig.dmr.cmef.BasicCmefUriGenerator ;
import org.cvit.cabig.dmr.cmef.CmefProvider ;
import org.cvit.cabig.dmr.cmef.CmefRepository ;
import org.cvit.cabig.dmr.cmef.CmefService ;
import org.cvit.cabig.dmr.cmef.FileStore ;
import org.cvit.cabig.dmr.cmef.InMemoryCmefRepository ;
import org.cvit.cabig.dmr.cmef.UserCredentials ;
import org.cvit.cabig.dmr.cmef.anzo.AnzoCmefRepository ;
import org.cvit.cabig.dmr.cmef.condor.CondorService ;
import org.cvit.cabig.dmr.cmef.monitoring.JobMonitoringService ;
import org.cvit.cabig.dmr.cmef.monitoring.JobStatusUpdater ;
import org.cvit.cabig.dmr.cmef.monitoring.PollingJobMonitor ;
import org.cvit.cabig.dmr.cmef.server.CmefServer ;
import org.cvit.cabig.dmr.cmef.server.CvitIdResolver ;
import org.cvit.cabig.dmr.cmef.server.providers.AnzoRepositoryProvider ;
import org.cvit.cabig.dmr.cmef.server.providers.CvitFileStoreProvider ;
import org.cvit.cabig.dmr.cmef.server.providers.DelegatingCmefProvider ;
import org.cvit.cabig.dmr.cmef.server.providers.GenePatternServiceProvider ;
import org.cvit.cabig.dmr.cmef.server.providers.HttpFileStoreProvider ;
import org.cvit.cabig.dmr.cmef.util.Provider ;
import org.cvit.cabig.dmr.cmef.util.ProviderException ;
import org.cvit.cabig.dmr.cmef.util.SingletonProvider ;
import org.restlet.Component ;
import org.restlet.data.ChallengeScheme ;
import org.restlet.data.Protocol ;
import org.restlet.resource.Directory ;
import org.restlet.security.ChallengeAuthenticator ;
import org.restlet.security.SecretVerifier ;
import org.restlet.security.Verifier ;

import freemarker.template.Configuration ;

//TODO: clean this up
//...generic providers based on class names given as property values, or just move to Guice (better handling of Singletons and the like)...
//...try constructor taking Properties param, then default constructor...
public class StartCmefServer {
    public static final String CMEF_SERVICE_IMPL_KEY = "org.cvit.cmef.service" ; // genepattern || condor
    public static final String GENEPATTERN_SERVICE = "genepattern" ;
    public static final String CONDOR_SERVICE = "condor" ;
    
    public static final String GP_USER_KEY = "org.cvit.cmef.genepattern.user" ;
    public static final String GP_PW_KEY = "org.cvit.cmef.genepattern.password" ;
    public static final String GP_URL_KEY = "org.cvit.cmef.genepattern.url" ;
    public static final String GP_WORKING_DIR_KEY = "org.cvit.cmef.genepattern.workingDir" ;
    public static final String GP_USE_GRID_KEY = "org.cvit.cmef.genepattern.useGrid" ;
    
    public static final String FS_TYPE_KEY = "org.cvit.cmef.filestore.type" ; // file || http
    public static final String FS_ROOT_DIR_KEY = "org.cvit.cmef.filestore.file.dir" ;
    public static final String FS_ROOT_URL_KEY = "org.cvit.cmef.filestore.rootUrl" ;
    public static final String FS_HTTP_URL_KEY = "org.cvit.cmef.filestore.http.url" ;
    
    public static final String CMEF_PORT_KEY = "org.cvit.cmef.server.port" ;
    public static final String CMEF_TEMPLATES_KEY = "org.cvit.cmef.server.templates" ;
    public static final String CMEF_FILES_KEY = "org.cvit.cmef.server.files" ;
    
    public static final String ANZO_ADMIN_USER = "org.cvit.cmef.anzo.adminUser" ;
    public static final String ANZO_ADMIN_PW = "org.cvit.cmef.anzo.adminPassword" ;
    
    /**
     * @param args
     * @throws DatatypeConfigurationException 
     */
    public static void main(String[] args) {
	Properties props = new Properties() ;
	if (args.length == 0) {
	    System.out.println("ERROR: Must specify properties file name.") ;
	    System.exit(1) ;
	} else {
	    File propsFile = new File(args[0]) ;
	    try {
		props.load(new FileInputStream(propsFile)) ;
	    } catch (Exception e) {
		System.out.printf("ERROR: unable to load properties file: %s.", propsFile) ;
		System.exit(1) ;
	    }
	}
	
	Provider<CmefService> serviceProvider = null ;
	String serviceType = props.getProperty(CMEF_SERVICE_IMPL_KEY, "genepattern").toLowerCase() ;
	if (GENEPATTERN_SERVICE.equals(serviceType)) {
	    String gpUserName = props.getProperty(GP_USER_KEY, "") ;
	    if (gpUserName.length() == 0) {
		System.out.printf("ERROR: No GenePattern user specified, use key: %s.\n", GP_USER_KEY) ;
		System.exit(1) ;
	    }
	    UserCredentials gpUser = new UserCredentials(gpUserName, props.getProperty(GP_PW_KEY, "")) ;
	    String gpUrl = props.getProperty(GP_URL_KEY, "") ;
	    if (gpUrl.length() == 0) {
		System.out.printf("ERROR: No GenePattern URL specified, use key: %s.\n", GP_URL_KEY) ;
		System.exit(1) ;
	    }
	    String workingDir = props.getProperty(GP_WORKING_DIR_KEY, ".") ;
	    String useGrid = props.getProperty(GP_USE_GRID_KEY, "false") ;
	    if (!"true".equals(useGrid) && !"false".equals(useGrid)) {
		System.out.println("ERROR: Value of " + GP_USE_GRID_KEY + " must be either 'true' or 'false'.") ;
		System.exit(1) ;
	    }
	    serviceProvider = new GenePatternServiceProvider(gpUser, gpUrl, workingDir, Boolean.parseBoolean(useGrid)) ;
	} else if (CONDOR_SERVICE.equals(serviceType)) {
	    CmefService cmefSvc = new CondorService() ;
	    serviceProvider = new SingletonProvider<CmefService>(cmefSvc) ;
	} else {
	    System.out.printf("ERROR: Invalid CmefService type specified. Must be one of 'genepattern' or 'condor', was: %s.", serviceType) ;
	    System.exit(1) ;
	}
	
//	Provider<CmefRepository> repoProvider = new SingletonProvider<CmefRepository>(
//		new InMemoryCmefRepository(new BasicCmefUriGenerator("urn:lsid:cvit.org:cmef:"))) ;
//	Provider<CmefRepository> repoProvider = AnzoRepositoryProvider.newInMemoryProvider() ;
	String anzoAdmin = props.getProperty(ANZO_ADMIN_USER, "") ;
	if (anzoAdmin.length() == 0) {
	    System.out.printf("ERROR: No Anzo administrator specified, use key: %s.\n", ANZO_ADMIN_USER) ;
	    System.exit(1) ;
	}
	String anzoPw = props.getProperty(ANZO_ADMIN_PW, "") ;
	Provider<CmefRepository> repoProvider = new AnzoRepositoryProvider(props, new UserCredentials(anzoAdmin, anzoPw)) ;
	
	String storeType = props.getProperty(FS_TYPE_KEY, "file") ;
	String storeRootUrl = props.getProperty(FS_ROOT_URL_KEY, "") ;
	if (storeRootUrl.length() == 0) {
	    System.out.printf("ERROR: No File Store root URL specified, use key: %s.\n", FS_ROOT_URL_KEY) ;
	    System.exit(1) ;
	}
	Provider<FileStore> storeProvider = null ;
	if ("file".equals(storeType)) {
	    String storeRoot = props.getProperty(FS_ROOT_DIR_KEY, "") ;
	    if (storeRoot.length() == 0) {
		System.out.printf("ERROR: No File Store root directory specified, use key: %s.\n", FS_ROOT_DIR_KEY) ;
		System.exit(1) ;
	    }
	    storeProvider = new CvitFileStoreProvider(new File(storeRoot),  storeRootUrl) ;
	} else if ("http".equals(storeType)) {
	    String storeUrl = props.getProperty(FS_HTTP_URL_KEY, "") ;
	    if (storeUrl.length() == 0) {
		System.out.printf("ERROR: HTTP file store selected but URL not specified, use key: %s.\n", FS_HTTP_URL_KEY) ;
		System.exit(1) ;
	    }
	    storeProvider = new HttpFileStoreProvider(storeUrl, storeRootUrl) ;
	} else {
	    System.out.printf("ERROR: Invalid file store type specified. Must be one of 'file' or 'http', was: %s.", storeType) ;
	    System.exit(1) ;
	}
	
	final Provider<CmefService> finalSvcProvider = serviceProvider ;
	Provider<JobMonitoringService> monitorProvider = new Provider<JobMonitoringService>() {
	    private AtomicReference<JobMonitoringService> service = new AtomicReference<JobMonitoringService>() ;
	    
	    public JobMonitoringService provide(UserCredentials credentials) throws ProviderException {
		if (service.get() == null) {
		    JobMonitoringService svc = new PollingJobMonitor(new JobStatusUpdater(finalSvcProvider.provide(credentials)), 30, TimeUnit.SECONDS) ;
		    if (service.compareAndSet(null, svc)) {
			svc.start() ;
		    }
		}
	        return service.get() ;
	    }
	}; 
	
	CmefProvider provider = new DelegatingCmefProvider(serviceProvider, repoProvider, storeProvider, monitorProvider) ;
	
	String tplDir = props.getProperty(CMEF_TEMPLATES_KEY, "") ;
	if (tplDir.length() == 0) {
	    System.out.printf("ERROR: No templates directory specified, use key: %s.\n", CMEF_TEMPLATES_KEY) ;
	    System.exit(1) ;
	}
	Configuration configuration = new Configuration() ;
	try {
	    configuration.setDirectoryForTemplateLoading(new File(tplDir)) ;
	} catch (IOException e) {
	    System.out.printf("ERROR: Exception setting template directory: %s.", tplDir) ;
	    System.exit(1) ;
	}
	
	String port = props.getProperty(CMEF_PORT_KEY, "8182") ;
	Component component = new Component() ;
	component.getServers().add(Protocol.HTTP, Integer.parseInt(port)) ;
	component.getClients().add(Protocol.FILE) ;
	if ("http".equals(storeType)) {
	    component.getClients().add(Protocol.HTTP) ;
	}
	
	String cmefFiles = props.getProperty(CMEF_FILES_KEY, "") ;
	if (cmefFiles.length() == 0) {
	    System.out.printf("ERROR: No files directory specified, use key: %s.\n", CMEF_FILES_KEY) ;
	    System.exit(1) ;
	}
	
	//CmefServerResource handles actual authentication/authorization...
	Verifier verifier = new SecretVerifier() {
	    @Override
	    public boolean verify(String arg0, char[] arg1) {
		return true ;
	    }
	};
	ChallengeAuthenticator cmefGuard = new ChallengeAuthenticator(component.getContext().createChildContext(), ChallengeScheme.HTTP_BASIC, "cmef") ;
	cmefGuard.setNext(new CmefServer(
	    provider,
	    configuration,
	    new CvitIdResolver("urn:lsid:telar.cambridgesemantics.com:", "urn:lsid:cvit.org:cmef:"))) ;
	cmefGuard.setVerifier(verifier) ;

	component.getDefaultHost().attach("/cmef/files", new Directory(component.getContext().createChildContext(), cmefFiles)) ;
	component.getDefaultHost().attach("/cmef", cmefGuard) ;
	
	try {
	    component.start() ;
	} catch (Throwable e) {
	    e.printStackTrace();
	}
    }

}
