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

package pt.up.fe.specs.deps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.suikasoft.SharedLibrary.LoggingUtils;
import org.suikasoft.SharedLibrary.ProcessUtils;

import pt.up.fe.specs.deps.deploy.DepsDeploy;
import pt.up.fe.specs.deps.resolve.DepsResolve;

public class DepsMain {

    private static final Map<String, Function<List<String>, ExitCode>> MODES;
    static {
        MODES = new HashMap<>();
        MODES.put("resolve", DepsResolve::execute);
        MODES.put("deploy", DepsDeploy::execute);
    }

    public static void main(String[] args) {
        ProcessUtils.programStandardInit();

        // First argument is the mode
        if (args.length < 1) {
            LoggingUtils.msgInfo("Needs at least one argument, the operation mode. Current modes: " + MODES.keySet());
            System.exit(ExitCode.FAILURE.getCode());
        }

        Function<List<String>, ExitCode> mode = MODES.get(args[0]);
        if (mode == null) {
            LoggingUtils.msgInfo("Mode '" + args[0] + "' not available.  Current modes: " + MODES.keySet());
            System.exit(ExitCode.FAILURE.getCode());
            return; // To avoid warning in Eclipse, cannot detect program ends after System.exit
        }

        // Create list with remaining arguments
        List<String> modeArgs = IntStream.range(1, args.length).mapToObj(i -> args[i]).collect(Collectors.toList());

        try {
            ExitCode exitCode = mode.apply(modeArgs);
            System.exit(exitCode.getCode());
        } catch (Exception e) {
            // Convert exception into a message to the user, return FAILURE
            LoggingUtils.msgInfo(e.getMessage());
            System.exit(ExitCode.FAILURE.getCode());
        }

    }

}
