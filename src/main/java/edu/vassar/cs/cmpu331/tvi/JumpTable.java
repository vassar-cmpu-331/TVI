/*
 * Copyright (c) 2016 Vassar College
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package edu.vassar.cs.cmpu331.tvi;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Keith Suderman
 */
public class JumpTable
{
	private Map<String, Integer> table = new HashMap<>();

	public JumpTable()
	{

	}

	public void set(String name, int offset) {
		if (table.get(name) != null) {
//			throw new TVIError(name + " has already been defined.");
			throw TVIError.LABEL_REDEFINED(name);
		}
		table.put(name, offset);
	}

	public int get(String name) {
		Integer offset = table.get(name);
		if (offset == null) {
			throw TVIError.UNKNOWN_LABEL(name);
		}
		return offset;
	}
}
