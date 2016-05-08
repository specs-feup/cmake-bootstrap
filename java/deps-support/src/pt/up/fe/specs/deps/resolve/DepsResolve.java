/**
 * Copyright 2016 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.deps.resolve;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.suikasoft.SharedLibrary.IoUtils;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.utils.Props;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import pt.up.fe.specs.deps.ExitCode;

public class DepsResolve {

    public static ExitCode execute(List<String> args) {
	if (args.size() < 5) {
	    LoggingUtils.msgInfo(
		    "'resolve' needs 5 arguments: <DEPS_PROPERTIES> <LIB> <SYSTEM> <COMPILER> <ARTIFACTS_DIR>");
	    return ExitCode.FAILURE;
	}

	// Load properties and extract hosts
	Props properties = Props.newInstance(IoUtils.existingFile(args.get(0)));

	List<String> hosts = getHosts(properties);

	String lib = args.get(1);
	String system = args.get(2);
	String compiler = args.get(3);
	File artifactFolder = IoUtils.safeFolder(args.get(4));

	ExitCode mainReturn = ExitCode.SUCCESS;
	try {
	    mainReturn = resolve(hosts, lib, system, compiler, artifactFolder);
	} catch (Exception e) {
	    LoggingUtils.msgInfo(e.getMessage());
	    mainReturn = ExitCode.FAILURE;
	} finally {
	    File tempFolder = new File(getTempFolderPath());
	    if (tempFolder.isDirectory()) {
		IoUtils.deleteFolderContents(tempFolder);
		tempFolder.delete();
	    }
	}

	return mainReturn;
    }

    private static List<String> getHosts(Props properties) {
	String hostsRaw = properties.get(ResolveProperty.HOSTS);

	if (hostsRaw == null) {
	    LoggingUtils.msgInfo("Empty hosts list, check if property '" + ResolveProperty.HOSTS.getKey() + "' is set");
	    return Collections.emptyList();
	}

	// Use | as separator
	String[] hostsArray = hostsRaw.split("\\|");

	if (hostsArray.length == 0) {
	    throw new RuntimeException("Could not find hosts file in value '" + hostsRaw + "'");
	}

	return IntStream.range(0, hostsArray.length).mapToObj(i -> hostsArray[i].trim()).collect(Collectors.toList());
    }

    private static final String getTempFolderPath() {
	return "./temp";
    }

    private static ExitCode resolve(List<String> hosts, String lib, String system, String compiler, File parentFolder) {
	// Build the link to retrieve the zip and save to temporary folder
	String filename = lib + "-" + system + "-" + compiler + ".zip";

	File tempFolder = downloadLib(filename, hosts);

	// Unzip to correct folder

	File zipFilename = IoUtils.existingFile(tempFolder, filename);
	File libFolder = IoUtils.safeFolder(parentFolder, lib + "-" + system + "-" + compiler);
	LoggingUtils.msgInfo("Unzipping to '" + libFolder + "'... ");

	// String source = "some/compressed/file.zip";
	// String destination = "some/destination/folder";
	// String password = "password";

	try {
	    ZipFile zipFile = new ZipFile(zipFilename);
	    zipFile.extractAll(IoUtils.getCanonicalPath(libFolder));
	} catch (ZipException e) {
	    throw new RuntimeException("Could not unzip file '" + zipFilename + "'", e);
	}
	LoggingUtils.msgInfo("Done");

	// Delete temp folder
	IoUtils.deleteFolderContents(tempFolder);
	tempFolder.delete();

	return ExitCode.SUCCESS;
    }

    private static File downloadLib(String filename, List<String> hosts) {
	// Try all hosts until link is found
	for (String host : hosts) {
	    String link = host + filename;

	    File tempFolder = IoUtils.safeFolder(getTempFolderPath());
	    if (IoUtils.download(link, tempFolder)) {
		return tempFolder;
	    }

	}

	throw new RuntimeException("Did not find library '" + filename + "' for download");
    }

}
