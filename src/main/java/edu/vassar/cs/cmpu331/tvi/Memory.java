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

import java.io.*;
import java.util.Arrays;

/**
 */
public class Memory
{
	public static final int SIZE = 4;

	private byte[] storage = new byte[SIZE];

	public Memory()
	{

	}

	public int readInt() {
		ByteArrayInputStream in = new ByteArrayInputStream(storage);
		DataInputStream stream = new DataInputStream(in);
		try
		{
			return stream.readInt();
		}
		catch (IOException e)
		{
			throw TVIError.INT_READ(e);
		}
	}

	public float readFloat() {
		ByteArrayInputStream in = new ByteArrayInputStream(storage);
		DataInputStream stream = new DataInputStream(in);
		try
		{
			return stream.readFloat();
		}
		catch (IOException e)
		{
			throw TVIError.FLOAT_READ(e);
		}
	}

	public void write(int value) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(SIZE);
		DataOutputStream data = new DataOutputStream(out);
		try
		{
			data.writeInt(value);
		}
		catch (IOException e)
		{
			throw TVIError.INT_WRITE(e);
		}
		write(out.toByteArray());
	}

	public void write(float value) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(SIZE);
		DataOutputStream data = new DataOutputStream(out);
		try
		{
			data.writeFloat(value);
		}
		catch (IOException e)
		{
			throw TVIError.FLOAT_WRITE(e);
		}
		write(out.toByteArray());
	}

	public void write(byte[] buffer) {
		if (buffer.length > SIZE) {
			throw TVIError.BUS_ERROR();
		}
		int i = 0;
		for ( ; i < buffer.length; ++i) {
			storage[i] = buffer[i];
		}
		for ( ; i < SIZE; ++i) {
			storage[i] = 0;
		}
	}

	public String toString() {
		return readInt() + " : " + readFloat();
	}
}
