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

import java.util.Arrays;
import java.util.StringTokenizer;

/**
 *
 */
public class Instruction
{
	private int line;
	private Opcode opcode;
	private String[] operands = { null, null, null };

	private Instruction() {

	}

	public static Instruction nop(int line) {
		Instruction nop = new Instruction();
		nop.line = line;
		nop.opcode = Opcode.NOP;
		return nop;
	}

	public static Instruction invalid(int line) {
		Instruction invalid = new Instruction();
		invalid.line = line;
		invalid.opcode = Opcode.INVALID;
		return invalid;
	}

//	public Instruction(String line, String opcode)
//	{
//		this.line = Integer.parseInt(line);
//		this.opcode = translate(opcode);
//	}
//
//	public Instruction(String line, String opcode, String... operands)
//	{
//		this(line, opcode);
//		for (int i = 0; i < operands.length; ++i) {
//			this.operands[i] = operands[i];
//		}
//	}

	public Instruction(String input)
	{
		int semicolon = input.indexOf(';');
		if (semicolon > 0) {
			input = input.substring(0, semicolon);
		}
		StringTokenizer tokenizer = new StringTokenizer(input, " \t:,");
		line = Integer.parseInt(tokenizer.nextToken());
		opcode = translate(tokenizer.nextToken());
		if (Opcode.PRINT == opcode) {
			int start = input.indexOf('"');
			if (start < 0) {
				throw TVIError.MISSING_QUOTE(input);
			}
			int end = input.indexOf('"', start+1);
			if (end < start) {
				throw TVIError.UNTERMINATED_STRING();
			}
			operands[0] = input.substring(start, end);
		}
		else
		{
			int i = 0;
			while (tokenizer.hasMoreTokens())
			{
				if (i >= operands.length)
				{
					throw TVIError.TOO_MANY_OPERANDS(input);
				}
				operands[i] = tokenizer.nextToken().trim();
				++i;
			}
		}
	}

	public int getLine() { return line; }
	public void setLine(int line) { this.line = line; }
	public Opcode getOpcode() { return opcode; }
	public String getOperand(int i) {
		if (i < 0 || i >= operands.length) {
			throw new IndexOutOfBoundsException("Invalid operand index: " + i);
		}
		return operands[i];
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.valueOf(line));
		builder.append(": ");
		builder.append(opcode);
		if (operands[0] != null) {
			builder.append(" ");
			builder.append(operands[0]);
		}
		for (int i = 1; i < operands.length; ++i) {
			if (operands[i] == null) {
				break;
			}
			builder.append(", ");
			builder.append(operands[i]);
		}
		return builder.toString();
	}

//	private Operand parse(String operand, int start, int end) {
//		int offset;
//		switch (operand.charAt(start)) {
//			case '^':
//				break;
//			case '@':
//				break;
//			case '%':
//				offset = Integer.parseInt(operand.substring(1));
//				break;
//			case '_':
//				break;
//			case '"':
//				break;
//			default:
//
//
//		}
//		return null;
//	}

	private Opcode translate(String opcode)
	{
		try
		{
			return Opcode.valueOf(opcode.toUpperCase());
		}
		catch (IllegalArgumentException e) {
//			return Opcode.INVALID;
//			throw new TVIError("Invalid opcode " + opcode);
			throw TVIError.UNKNOWN_OPCODE(opcode);
		}
	}
}
