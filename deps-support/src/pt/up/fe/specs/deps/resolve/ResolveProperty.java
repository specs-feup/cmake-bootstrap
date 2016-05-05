/**
 *  Copyright 2016 SPeCS.
 * 
 */

package pt.up.fe.specs.deps.resolve;

public enum ResolveProperty {

	HOSTS("deps.hosts");

	private final String key;

	private ResolveProperty(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}
}
