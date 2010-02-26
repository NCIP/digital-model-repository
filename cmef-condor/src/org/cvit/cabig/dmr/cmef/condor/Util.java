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
