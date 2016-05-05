/**
 * Copyright 2016 SPeCS.
 * 
 */

package pt.up.fe.specs.deps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pt.up.fe.specs.deps.resolve.DepsResolve;

public class DepsMain {

	private static final Map<String, Function<List<String>, ExitCode>> MODES;
	static {
		MODES = new HashMap<>();
		MODES.put("resolve", DepsResolve::execute);
		MODES.put("deploy", DepsMain::deploy);
	}

	private static ExitCode deploy(List<String> args) {
		throw new UnsupportedOperationException("Mode 'deploy' is not yet implemented");
	}

	public static void main(String[] args) {

		// First argument is the mode
		if (args.length < 1) {
			System.out.println("Needs at least one argument, the operation mode. Current modes: " + MODES.keySet());
			System.exit(ExitCode.FAILURE.getCode());
		}

		Function<List<String>, ExitCode> mode = MODES.get(args[0]);
		if (mode == null) {
			System.out.println("Mode '" + args[0] + "' not available.  Current modes: " + MODES.keySet());
			System.exit(ExitCode.FAILURE.getCode());
		}

		// Create list with remaining arguments
		List<String> modeArgs = IntStream.range(1, args.length).mapToObj(i -> args[i]).collect(Collectors.toList());

		ExitCode exitCode = mode.apply(modeArgs);

		System.exit(exitCode.getCode());

	}

}
