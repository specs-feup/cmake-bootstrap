/**
 *  Copyright 2016 SPeCS.
 * 
 */

package pt.up.fe.specs.deps;

public enum DepsProperty {

	HOSTS("deps.hosts");

	private final String key;

	private DepsProperty(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}
}
