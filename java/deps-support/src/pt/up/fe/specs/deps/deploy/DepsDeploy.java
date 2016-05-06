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

package pt.up.fe.specs.deps.deploy;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.suikasoft.EclipseDevelopment.Utilities.DeployUtils;
import org.suikasoft.SharedLibrary.IoUtils;
import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.PropertiesUtils;

import pt.up.fe.specs.deps.DepsResource;
import pt.up.fe.specs.deps.ExitCode;

public class DepsDeploy {

    public static ExitCode execute(List<String> args) {
	// First argument is the filename to transfer, second argument is the id of the host
	if (args.size() < 2) {
	    LoggingUtils.msgInfo(
		    "'deploy' needs 2 arguments: <FILE_TO_DEPLOY> <HOST_ID>");
	    return ExitCode.FAILURE;
	}

	File fileToDeploy = IoUtils.existingFile(args.get(0));
	String hostId = args.get(1);

	// Load properties file with same name as the host id
	File hostPropertiesFile = IoUtils.existingFile(hostId + ".properties");

	// Load properties
	Properties properties = PropertiesUtils.load(hostPropertiesFile);

	// Get deploy type
	String deployType = PropertiesUtils.getK(properties, DeployProperty.TYPE);

	if (DeployType.SFTP.equals(deployType)) {
	    return sftpDeploy(fileToDeploy, hostId, properties);
	}

	throw new RuntimeException("Deploy type '" + deployType + "' not defined");

    }

    private static ExitCode sftpDeploy(File fileToDeploy, String hostId, Properties properties) {

	String message = "Using a secure connection over SSH. Make sure you are inside a network that permits the communication! (e.g., by using a VPN).";
	LoggingUtils.msgInfo(message);

	// Get deploy type
	String host = PropertiesUtils.getK(properties, DeployProperty.HOST);
	String location = PropertiesUtils.getK(properties, DeployProperty.LOCATION);

	LoggingUtils.msgInfo("Transfering '" + fileToDeploy + "' to " + host + ":" + location);

	// Get ANT script
	String antSftp = buildSftpScript(fileToDeploy, hostId, host, location);

	// Save script
	File sftpScript = new File(DeployUtils.getTempFolder(), "sftp.xml");

	IoUtils.write(sftpScript, antSftp);

	DeployUtils.runAnt(sftpScript);

	return ExitCode.SUCCESS;
    }

    /**
     * @param sftpData
     * @return
     */
    private static String buildSftpScript(File fileToSend, String hostId, String host, String location) {
	String template = IoUtils.getResource(DepsResource.SFTP_TEMPLATE);

	// Load login and pass
	Properties hostCreds = PropertiesUtils.load(IoUtils.existingFile(hostId + ".creds"));
	String login = PropertiesUtils.getK(hostCreds, DeployProperty.LOGIN);
	String pass = PropertiesUtils.getK(hostCreds, DeployProperty.PASS);

	template = template.replace("<LOGIN>", login);
	template = template.replace("<PASS>", pass);
	template = template.replace("<HOST>", host);
	template = template.replace("<DESTINATION_FOLDER>", location);

	template = template.replace("<FILE>", fileToSend.getAbsolutePath());

	return template;
    }

}
