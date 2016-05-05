/**
 *  Copyright 2016 SPeCS.
 * 
 */

package pt.up.fe.specs.deps.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class IoUtils {

	private final static char DEFAULT_FOLDER_SEPARATOR = '/';

	/**
	 * Helper method which accepts a parent File and a child String as input.
	 * 
	 * @param parentFolder
	 * @param child
	 * @return
	 */
	public static File safeFolder(File parentFolder, String child) {
		return safeFolder(new File(parentFolder, child));
	}

	/**
	 * Helper method which accepts a File as input.
	 * 
	 * @param folder
	 * @return
	 */
	public static File safeFolder(File folder) {
		return safeFolder(folder.getPath());
	}

	/**
	 * Given a string representing a filepath to a folder, returns a File object
	 * representing the folder.
	 * 
	 * <p>
	 * If the folder doesn't exist, the method will try to create the folder and
	 * necessary sub-folders. If an error occurs (ex.: the folder could not be
	 * created, the given path does not represent a folder), returns null and
	 * logs the cause.
	 * 
	 * *
	 * <p>
	 * If the given folderpath is an empty string, returns the current working
	 * folder.
	 * 
	 * <p>
	 * If an object different than null is returned it is guaranteed that the
	 * folder exists.
	 * 
	 * @param folderpath
	 *            String representing a folder.
	 * @return a File object representing a folder, or null if unsuccessful.
	 */
	public static File safeFolder(String folderpath) {
		// Check null argument. If null, it would raise and exception and stop
		// the program when used to create the File object.
		if (folderpath == null) {
			throw new RuntimeException("Input 'folderpath' is null");
		}

		// Check if folderpath is empty
		if (folderpath.trim().isEmpty()) {
			return new File("./");
		}

		// Create File object
		final File folder = new File(folderpath);

		// The following checks where done in that sequence to avoid having
		// more than one level of if-nesting.

		// Check if File is a folder
		final boolean isFolder = folder.isDirectory();
		if (isFolder) {
			return folder;
		}

		// Check if File exists. If true, is not a folder.
		final boolean folderExists = folder.exists();
		if (folderExists) {
			// "doesn''t represent a folder.");
			throw new RuntimeException("Path '" + folderpath + "' exists, but " + "doesn't represent a folder");
		}

		// Try to create folder.
		final boolean folderCreated = folder.mkdirs();
		if (folderCreated) {
			return folder;
		}

		// Check if folder exists
		if (folder.exists()) {
			System.err.println("Folder created (" + folder.getAbsolutePath() + ") but 'mkdirs' returned false.");
			return folder;
		}

		// Couldn't create folder
		throw new RuntimeException("Path '" + folderpath + "' does not exist and " + "could not be created");

	}

	public static File existingFile(File parent, String filePath) {
		File completeFilepath = new File(parent, filePath);

		return existingFile(completeFilepath.getPath());
	}

	/**
	 * Method to create a File object for a file which should exist.
	 * 
	 * <p>
	 * The method does some common checks (ex.: if the file given by filepath
	 * exists, if it is a file). If any of the checks fail, throws an exception.
	 * 
	 * @param filepath
	 *            String representing an existing file.
	 * @return a File object representing a file, or null if unsuccessful.
	 */
	public static File existingFile(String filepath) {
		// Check null argument. If null, it would raise and exception and stop
		// the program when used to create the File object.
		if (filepath == null) {
			throw new RuntimeException("Input 'filepath' is null");
		}

		// Create File object
		final File file = new File(filepath);

		// Check if File is a file
		final boolean isFile = file.isFile();
		if (isFile) {
			return file;
		}

		// Check if File exists. If true, is not a file.
		final boolean fileExists = file.exists();
		if (fileExists) {
			throw new RuntimeException("Path '" + filepath + "' exists, but doesn't " + "represent a file");
		}

		// File doesn't exist, return null.
		throw new RuntimeException("Path '" + filepath + "' does not exist");
	}

	/**
	 * 
	 * @param folder
	 * @return true in case the operation was successful (could delete all
	 *         files, or the folder does not exit)
	 */
	public static boolean deleteFolderContents(File folder) {
		if (!folder.exists()) {
			return true;
		}

		if (!folder.isDirectory()) {
			System.err.println("Not a folder");
			return false;
		}

		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				deleteFolderContents(file);
			}
			boolean deleted = file.delete();
			if (!deleted) {
				System.out.println("Could not delete '" + file + "'");
			}
		}

		return true;
	}

	public static boolean download(String urlString, File outputFolder) {
		URL url = null;

		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			System.out.println("Could not create URL from '" + urlString + "'.");
			return false;
		}

		return download(url, outputFolder);
	}

	/**
	 * This function downloads the file specified in the URL.
	 * 
	 * @param url
	 *            The URL of the file to be downloaded.
	 * @return true if the file could be downloaded, false otherwise
	 * @throws IOException
	 */
	public static boolean download(URL url, File outputFolder) {
		URLConnection con;
		// UID uid = new UID();

		try {
			con = url.openConnection();
			con.connect();

			String type = con.getContentType();

			if (type == null) {
				System.out.println("Could not get the content type of the URL '" + url + "'");
				return false;

			}

			// Get filename
			String path = url.getPath();
			String filename = path.substring(path.lastIndexOf('/') + 1, path.length());

			if (filename.isEmpty()) {
				System.out.println("Could not get a filename for the url '" + url + "'");
				return false;
			}

			byte[] buffer = new byte[4 * 1024];
			int read;

			// String[] split = type.split("/");
			// String filename = Integer.toHexString(uid.hashCode()) + "_" +
			// split[split.length - 1];
			File outputFile = new File(outputFolder, filename);

			try (FileOutputStream os = new FileOutputStream(outputFile); InputStream in = con.getInputStream()) {

				while ((read = in.read(buffer)) > 0) {
					os.write(buffer, 0, read);
				}

			}

			return true;
		} catch (IOException e) {
			String urlString = url.toString();
			if (urlString.length() > 512) {
				urlString = urlString.substring(0, 512) + " ... (url truncated)";
			}
			// System.out.println("IOException while reading URL '" + urlString
			// + "'");
			return false;
		}

	}

	/**
	 * Returns the canonical path of a file
	 * 
	 * @param file
	 * @return
	 */
	public static String getCanonicalPath(File file) {
		return getCanonicalFile(file).getPath();
	}

	/**
	 * Returns the canonical file.
	 * 
	 * <p>
	 * Calls getAbsoluteFile(), to avoid problems when using paths such as
	 * 'Desktop' in Windows, and then transforms to a canonical path.
	 * 
	 * <p>
	 * Throws a RuntimeException if it could not obtain the canonical file.
	 * 
	 * @param file
	 * @return
	 */
	public static File getCanonicalFile(File file) {

		try {
			file = file.getAbsoluteFile().getCanonicalFile();
			// return new File(file.getAbsolutePath().replace('\\', '/'));
			return new File(normalizePath(file.getAbsolutePath()));
		} catch (IOException e) {
			throw new RuntimeException("Could not get canonical file for " + file.getPath());
		}
	}

	/**
	 * Converts all '\' to '/'
	 * 
	 * @param path
	 * @return
	 */
	public static String normalizePath(String path) {
		return path.replace('\\', IoUtils.DEFAULT_FOLDER_SEPARATOR);
	}

}
