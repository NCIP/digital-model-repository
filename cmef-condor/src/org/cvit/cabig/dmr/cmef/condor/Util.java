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
package org.cvit.cabig.dmr.cmef.condor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.cvit.cabig.dmr.cmef.domain.ComputationalModel;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class Util {
	// TODO XXX Don't do this!!!
	private static final String AUTHENTICATION_STRING = "sysadmin:bymi4zg3";

	protected static File zipFile(ComputationalModel model,
			SubmitFile submitFile, String directory) throws IOException {
		Collection<org.cvit.cabig.dmr.cmef.domain.File> dmrFileCollection = model
				.getFiles();

		// HashSet chosen because it does not allow duplicates
		Collection<String> collectionOfFileNames = new HashSet<String>();

		File destinationDir = new File(directory
				+ System.getProperty("file.separator") + getRandomNumber(100));
		if (!destinationDir.exists()) {
			destinationDir.mkdir();
		}
		String destinationPath = destinationDir.getAbsolutePath();

		if (dmrFileCollection != null) {
			for (org.cvit.cabig.dmr.cmef.domain.File file : dmrFileCollection) {
				if (file != null) {
					String tempFileName = downloadFile(file.getSource(), file
							.getName(), destinationPath);
					collectionOfFileNames.addAll(getFileNames(destinationPath,
							tempFileName));
				}
			}
		}
		submitFile.outputFile(destinationPath);
		collectionOfFileNames.add(submitFile.getFileLocation().getName());

		InputStream is = Util.class
				.getResourceAsStream("/ExecutableWrapper.jar");
		OutputStream os = new FileOutputStream(destinationPath
				+ System.getProperty("file.separator")
				+ "ExecutableWrapper.jar");
		transferData(is, os);
		is.close();
		os.close();
		collectionOfFileNames.add("ExecutableWrapper.jar");

		String[] zipFileStringNameArray = collectionOfFileNames
				.toArray(new String[0]);

		String outFilename = model.getTitle() + ".zip";
		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					directory + System.getProperty("file.separator")
							+ outFilename));

			// Compress the files
			for (int i = 0; i < zipFileStringNameArray.length; i++) {
				FileInputStream in = new FileInputStream(destinationDir
						.getAbsolutePath()
						+ System.getProperty("file.separator")
						+ zipFileStringNameArray[i]);
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(zipFileStringNameArray[i]));

				transferData(in, out);

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Delete all files in temp directory
			File[] fileNames = destinationDir.listFiles();
			for (File f : fileNames) {
				f.delete();
			}

			destinationDir.delete();
			// Complete the ZIP file
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new File(directory + System.getProperty("file.separator")
				+ outFilename);
	}

	public static <T extends Object> boolean collectionsAreEqual(
			Collection<T> firstSet, Collection<T> secondSet) {
		if (firstSet == null && secondSet == null) {
			return true;
		} else if (firstSet == null || secondSet == null
				|| firstSet.size() != secondSet.size()) {
			return false;
		}
		return firstSet.containsAll(secondSet);
	}

	private static void transferData(InputStream in, OutputStream out)
			throws IOException {
		byte[] buf = new byte[1024];

		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
	}

	protected static String copyFilesToLocal(String destination, String source) {
		try {
			File f1 = new File(source);
			File f2 = new File(destination, f1.getName());

			InputStream in = new FileInputStream(f1);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return f2.getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	final static int size = 1024;

	protected static String downloadFile(String fAddress, String localFileName,
			String destinationDir) {
		OutputStream outStream = null;
		URLConnection uCon = null;
		String fileDestination = null;

		InputStream is = null;
		try {
			URL Url;
			byte[] buf;
			int ByteRead, ByteWritten = 0;
			Url = new URL(fAddress);
			fileDestination = ("".equals(destinationDir) || destinationDir == null) ? localFileName
					: destinationDir + System.getProperty("file.separator")
							+ localFileName;
			outStream = new BufferedOutputStream(new FileOutputStream(
					fileDestination));

			uCon = Url.openConnection();

			String authString = "Basic "
					+ Base64.encode(AUTHENTICATION_STRING.getBytes());
			uCon.setRequestProperty("Authorization", authString);

			is = uCon.getInputStream();
			buf = new byte[size];
			while ((ByteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, ByteRead);
				ByteWritten += ByteRead;
			}
			System.err.println("Downloaded Successfully: " + localFileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return localFileName;
	}

	protected static String downloadFile(String fAddress, String destinationDir) {

		int forwardSlashIndex = fAddress.lastIndexOf('/');
		int periodIndex = fAddress.lastIndexOf('.');

		String fileName = fAddress.substring(forwardSlashIndex + 1);

		// URL
		if (periodIndex >= 1 && forwardSlashIndex >= 0
				&& forwardSlashIndex < fAddress.length() - 1) {
			downloadFile(fAddress, fileName, destinationDir);
		}
		// Local File
		else {
			fileName = copyFilesToLocal(destinationDir, fileName);
		}
		return fileName;
	}

	@SuppressWarnings("unchecked")
	protected static Collection<String> getFileNames(String directory,
			String file) {
		if (isArchive(file)) {
			try {
				ZipFile zip = new ZipFile(new File(directory
						+ System.getProperty("file.separator") + file));
				Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip
						.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();
					if (isArchive(entry.getName())) {
						return unzipZipFile(zip, directory);
					}
				}
				zip.close();
			} catch (IOException e) {
				System.err.println("Tried to open " + file);
				e.printStackTrace();
			}
		}
		return Collections.singleton(file);
	}

	protected static Collection<org.cvit.cabig.dmr.cmef.domain.File> addFileToCollection(
			ComputationalModel model, org.cvit.cabig.dmr.cmef.domain.File file) {
		Collection<org.cvit.cabig.dmr.cmef.domain.File> listOfFiles = model
				.getFiles();
		listOfFiles.add(file);
		return listOfFiles;
	}

	@SuppressWarnings("unchecked")
	private static Collection<String> unzipZipFile(ZipFile zip, String directory)
			throws FileNotFoundException, IOException {
		Collection<String> returnCollection = new ArrayList<String>();
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			copyInputStream(zip.getInputStream(entry),
					new BufferedOutputStream(new FileOutputStream(directory
							+ System.getProperty("file.separator")
							+ entry.getName())));
			returnCollection.add(entry.getName());
		}
		zip.close();
		return returnCollection;
	}

	protected static boolean isArchive(String file) {
		if (file == null) {
			return false;
		}
		int periodIndex = file.lastIndexOf(".");
		return (file != null && periodIndex != -1 && (".zip".equals(file
				.substring(periodIndex)) || ".jar".equals(file
				.substring(periodIndex))));
	}

	protected static int getRandomNumber(int range) {
		Random random = new Random();
		return random.nextInt(1000);
	}

	private static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	/**
	 * Finds the "Main-Class" entry in the Jar file's manifest.
	 * 
	 * @return null If no Main-Class is found.
	 */
	public static String getMainClass(ComputationalModel model, String string)
			throws IOException {
		String mainClass = null;
		Collection<org.cvit.cabig.dmr.cmef.domain.File> files = model
				.getFiles();
		for (org.cvit.cabig.dmr.cmef.domain.File file : files) {
			if (string != null && string.contains(file.getName())) {
				String fileName = Util.downloadFile(file.getSource(), "");
				JarFile jar = new JarFile(new File(fileName));
				mainClass = jar.getManifest().getMainAttributes().getValue(
						"Main-Class");
			}
		}
		return mainClass;
	}

	/**
	 * Copies src File to dest directory (preserve's src filename:
	 * src.getName()), using Input/Output streams.
	 * 
	 * @param src
	 *            the source file
	 * @param dest
	 * 
	 *            the destination directory
	 * @throws IOException
	 */
	public static void copyFile(File src, File dest) throws IOException {
		FileInputStream in = new FileInputStream(src);
		if (dest.isDirectory())
			dest = new File(dest, src.getName());
		FileOutputStream out = new FileOutputStream(dest);
		copyInputStream(in, out);
		in.close();
		out.close();
	}
}
