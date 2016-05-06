/**
 *  Copyright 2016 SPeCS.
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

package pt.up.fe.specs.deps.resolve;

import org.suikasoft.SharedLibrary.Interfaces.KeyProvider;

public enum ResolveProperty implements KeyProvider<String> {

	HOSTS("deps.hosts");

	private final String key;

	private ResolveProperty(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}

	@Override
	public String getKey() {
		return key;
	}

}
