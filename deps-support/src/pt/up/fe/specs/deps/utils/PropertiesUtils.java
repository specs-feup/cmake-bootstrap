/**
 * Copyright 2011 Joao Bispo.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package pt.up.fe.specs.deps.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Utility methods related to Java Properties objects.
 * 
 * @author Joao Bispo
 */
public class PropertiesUtils {

	/**
	 * Given a File, loads the contents of the file into a Java Properties
	 * object.
	 *
	 * <p>
	 * Throws an Exception if an error occurs (ex.: the File argument does not
	 * represent a file, could not load the Properties object).
	 * 
	 * @param file
	 *            a File object representing a file.
	 * @return If successful, a Properties objects with the contents of the
	 *         file. Throws an exception otherwise.
	 */
	public static Properties load(File file) {
		assert file != null : "Input file must not be null";

		try (InputStream inputStream = new FileInputStream(file)) {
			return load(inputStream);
		} catch (Exception ex) {
			throw new RuntimeException("Could not load properties file '" + file + "'", ex);
		}

	}

	/**
	 * Given a File object, loads the contents of the file into a Java
	 * Properties object.
	 *
	 * <p>
	 * If an error occurs (ex.: the File argument does not represent a file,
	 * could not load the Properties object) returns null and logs the cause.
	 * 
	 * @param file
	 *            a File object representing a file.
	 * @return If successfull, a Properties objects with the contents of the
	 *         file. Null otherwise.
	 */
	public static Properties load(InputStream inputStream) {

		Properties props = new Properties();

		try {
			// Properties props = new Properties();
			// props.load(new java.io.FileReader(file));
			props.load(inputStream);
			inputStream.close();
		} catch (IOException ex) {
			System.err.println("IOException: " + ex.getMessage());
			return null;
		}

		return props;
		// return null;
	}

	/**
	 * Saves a properties object to a file.
	 * 
	 * @param outputfile
	 * @param props
	 * @return true if there was no problems. False otherwise
	 */
	public static boolean store(File outputfile, Properties props) {

		try (OutputStream outStream = new BufferedOutputStream(new FileOutputStream(outputfile))) {

			props.store(outStream, "");
			outStream.close();

		} catch (IOException ex) {
			System.err.println("Could not save properties object to file '" + outputfile + "': " + ex.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Returns the value associated to the string representation of the given
	 * enum.
	 * 
	 * @param props
	 * @param anEnum
	 * @return the value associated to the given enum
	 */
	public static String get(Properties props, Enum<?> anEnum) {
		String propertyName = anEnum.toString();
		String value = props.getProperty(propertyName);
		return value;
	}

	/**
	 * Warns user if a value for the given key is not found.
	 * 
	 * @param props
	 * @param anEnum
	 * @return
	 */
	public static String getExisting(Properties props, Enum<?> anEnum) {
		String value = get(props, anEnum);
		if (value == null) {
			System.out.println("Key '" + anEnum.toString() + "' not found in properties.");
		}

		return value;
	}

	/**
	 * Builds a folder from a property.
	 * 
	 */
	public static File getFolder(Properties props, Enum<?> anEnum) {
		String folderName = PropertiesUtils.getExisting(props, anEnum);
		File folder;
		if (folderName.equals("")) {
			folder = null;
		} else {
			folder = IoUtils.safeFolder(folderName);
		}

		return folder;
	}
}
