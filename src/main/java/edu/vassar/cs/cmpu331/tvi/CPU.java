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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static edu.vassar.cs.cmpu331.tvi.Opcode.*;

public class CPU
{
	public static final int DEFAULT_MEMORY_SIZE = 1024*1024;

	private List<Instruction> instructions;
	private JumpTable jump;
	private Stack<StackFrame> callStack;
	private boolean tracing = false;
	private boolean debugging = false;

	/** A pointer to the next instruction to be executed. */
	private int ip;

	/** The frame pointer. Local references are relative to the
	 *  frame pointer.
	 */
	private int fp;

	/** Pointer to the top of the current frame. */
	private int tof;

	/** Pointer the the next memory location used by param/push. */
	private int top;

	private Memory[] memory;

	public CPU() {
		this(DEFAULT_MEMORY_SIZE);
	}

	public CPU(int size)
	{
		this.instructions = new ArrayList<>();
		this.ip = this.fp = this.top = 0;
		this.memory = Stream.generate(Memory::new)
				  .limit(size)
				  .toArray(Memory[]::new);
		jump = new JumpTable();
		callStack = new Stack<>();
	}

	public void add(Instruction instruction) {
		int line = instruction.getLine();
		if (line < instructions.size()) {
			throw TVIError.LINE_DEFINED(line);
		}
		while (instructions.size() < line) {
			instructions.add(Instruction.nop(instructions.size()));
		}
		instructions.add(instruction);
	}

	public void enableTracing() { tracing = true; }
	public void disableTracing() { tracing = false; }
	public void setTracing(boolean tracing) { this.tracing = tracing; }

	public void enableDebug() { debugging = true; }
	public void disableDebug() { debugging = false; }
	public void setDebug(boolean value) { debugging = value; }

	public float readFloat(int offset) {
		return memory[offset].readFloat();
	}

	public int readInt(int offset) {
		return memory[offset].readInt();
	}

	public void run() {
		makeJumpTable();
		ip = 0;
		// The exit opcode sets the ip to -1.
		while (ip >= 0) {
			execute();
		}
		System.out.println("Execution complete.");
	}

	protected void makeJumpTable() {
		instructions.stream()
				  .filter( i -> PROCBEGIN == i.getOpcode())
				  .forEach( i -> {
					  String label = i.getOperand(0);
					  jump.set(label, i.getLine());
				  });
	}

	/**
	 * Execute the instruction pointed to by the ip (instruction pointer).
	 */
	protected void execute()
	{
		String op1, op2, op3;
		int i1, i2, i3;
		float f1, f2;
		Scanner scanner;

		if (ip >= instructions.size()) {
			throw TVIError.EXECUTION_FLOW();
		}
		Instruction instruction = instructions.get(ip);
		++ip;
		switch(instruction.getOpcode()) {
			case ADD:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				writeTo(i1 + i2, instruction.getOperand(2));
				break;
			case ALLOC:
				trace(instruction);
				String operand = instruction.getOperand(0);
				notNull(operand, "alloc: size is missing.");
				int size = Integer.parseInt(operand);
				debug("Allocating %d memory locations", size);
				tof += size;
				top = tof;
				debug("[ALLOC] top:%d tof:%d", top, tof);
				break;
			case ASSERT:
				trace(instruction);
				break;
			case BEQ:
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				i3 = decodeInt(instruction.getOperand(2));
				if (i1 == i2) ip = i3;
				trace(instruction);
				break;
			case BGE:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				i3 = decodeInt(instruction.getOperand(2));
				if (i1 >= i2) ip = i3;
				break;
			case BGT:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				i3 = decodeInt(instruction.getOperand(2));
				if (i1 > i2) ip = i3;
				break;
			case BLE:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				i3 = decodeInt(instruction.getOperand(2));
				if (i1 <= i2) ip = i3;
				break;
			case BLT:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				i3 = decodeInt(instruction.getOperand(2));
				if (i1 < i2) ip = i3;
				break;
			case BNE:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				i3 = decodeInt(instruction.getOperand(2));
				if (i1 != i2) ip = i3;
				break;
			case CALL:
				trace(instruction);
				String name = instruction.getOperand(0);
				notNull(name, "No name provided for call opcode");
				debug("Pushing %d on to the call stack", ip);
				callStack.push(new StackFrame(ip, fp));
				ip = jump.get(name) + 1;
				fp = tof;
				tof = top;
				debug("[CALL] IP:%d FP:%d TOF:%d", ip, fp, tof);
				break;
			case DIV:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				writeTo(i1 / i2, instruction.getOperand(2));
				break;
			case DUMP:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				System.out.println(String.format("IP:%d FP:%d TOF:%d TOP:%d", ip, fp, tof, top));
				IntStream.range(0, i1)
						  .mapToObj(i -> i + ": " + memory[i].toString())
						  .forEach(System.out::println);
				break;
			case EXIT:
				trace(instruction);
				ip = -1;
				break;
			case FADD:
				trace(instruction);
				f1 = decodeFloat(instruction.getOperand(0));
				f2 = decodeFloat(instruction.getOperand(1));
				writeTo(f1 + f2, instruction.getOperand(2));
				break;
			case FDIV:
				trace(instruction);
				f1 = decodeFloat(instruction.getOperand(0));
				f2 = decodeFloat(instruction.getOperand(1));
				writeTo(f1 / f2, instruction.getOperand(2));
				break;
			case FINP:
				trace(instruction);
				scanner = new Scanner(System.in);
				f1 = scanner.nextFloat();
				writeTo(f1, instruction.getOperand(0));
				break;
			case FMUL:
				trace(instruction);
				f1 = decodeFloat(instruction.getOperand(0));
				f2 = decodeFloat(instruction.getOperand(1));
				writeTo(f1 * f2, instruction.getOperand(2));
				break;
			case FOUTP:
				trace(instruction);
				f1 = decodeFloat(instruction.getOperand(0));
				System.out.print(f1);
				break;
			case FREE:
				trace(instruction);
				i1 = Integer.parseInt(instruction.getOperand(0));
				top -= i1;
				break;
			case FSUB:
				trace(instruction);
				f1 = decodeFloat(instruction.getOperand(0));
				f2 = decodeFloat(instruction.getOperand(1));
				writeTo(f1 - f2, instruction.getOperand(2));
				break;
			case FTOL:
				trace(instruction);
				break;
			case FUMINUS:
				trace(instruction);
				f1 = decodeFloat(instruction.getOperand(0));
				writeTo(-f1, instruction.getOperand(1));
				break;
			case GOTO:
				trace(instruction);
				op1 = instruction.getOperand(0);
				notNull(op1, "No destination provided for goto");
				ip = Integer.parseInt(op1);
				break;
			case INP:
				trace(instruction);
				scanner = new Scanner(System.in);
				i1 = scanner.nextInt();
				writeTo(i1, instruction.getOperand(0));
				break;
			case INVALID:
				throw TVIError.INVALID_OPCODE();
				//break;
			// load src, off, dest      ;  move from src+offset into dest
			case LOAD:
				trace(instruction);
				//source
				i1 = decodeAddress(instruction.getOperand(0));
				//offset
				i2 = decodeInt(instruction.getOperand(1));
				i3 = readInt(i1 + i2);
				debug("src:%d off:%d value:%d dest:%s", i1, i2, i3);
				writeTo(i3, instruction.getOperand(2));
				break;
			case LTOF:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				writeTo((float) i1, instruction.getOperand(1));
				break;
			case MOVE:
				trace(instruction);
				op1 = instruction.getOperand(0);
				if (op1.startsWith("@")) {
					i1 = decodeInt(op1);
					writeTo(i1, instruction.getOperand(1));
				}
				else if (isAddress(op1)) {
					i1 = decodeInt(op1);
					writeTo(i1, instruction.getOperand(1));
				}
				else if (looksLikeAFloat(op1)) {
					f1 = decodeFloat(op1);
					writeTo(f1, instruction.getOperand(1));
				}
				else {
					i1 = Integer.parseInt(op1);
					writeTo(i1, instruction.getOperand(1));
				}
				break;
			case MUL:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				writeTo(i1 * i2, instruction.getOperand(2));
				break;
			case NEWL:
				System.out.println();
				break;
			case OUTP:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				System.out.print(i1);
				break;
			case PUSH:
			case PARAM:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				debug("Writing %d to location %d", i1, top);
				memory[top].write(i1);
				++top;
				break;
			case PRINT:
				trace(instruction);
				String message = instruction.getOperand(0);
				int end = message.length();
				System.out.print(message.substring(1, end));
				break;
			case PROCEND:
				trace(instruction);
				if (callStack.isEmpty()) {
					throw TVIError.EMPTY_STACK();
				}
				tof = top = fp;
				StackFrame frame = callStack.pop();
				ip = frame.ip();
				fp = frame.fp();
				debug("Returning to %d", ip);
				break;
			// store src, offset, dest  ; move from src into dest + offset
			case STOR:
				trace(instruction);
				// src
				i1 = decodeInt(instruction.getOperand(0));
				// offset
				i2 = decodeInt(instruction.getOperand(1));
				// base
				i3 = decodeAddress(instruction.getOperand(2));
				debug("value: %d, offset: %d, base: %d", i1, i2, i3);
				memory[i2 + i3].write(i1);
				break;
			case SUB:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				i2 = decodeInt(instruction.getOperand(1));
				writeTo(i1 - i2, instruction.getOperand(2));
				break;
			case UMINUS:
				trace(instruction);
				i1 = decodeInt(instruction.getOperand(0));
				writeTo(-i1, instruction.getOperand(1));
				break;
		}
	}

//	private int parse(String input) {
//		return parse(input, 0);
//	}
//	private int parse(String input, int offset) {
//		return Integer.parseInt(input.substring(offset));
//	}

//	private void move(Instruction instruction) {
//		// Temporary buffer so we can easily read/write bytes.
//		Memory cell = new Memory();
//		String source = instruction.getOperand(0);
//		String destination = instruction.getOperand(1);
//		if (source.startsWith("@_")) {
//			// Address of global memory is the address given.
//			cell.write(parse(source, 2));
//		}
//		else if (source.startsWith("@%")) {
//			// Address of local memory is the provided address + the FP.
//			cell.write(parse(source, 2) + fp);
//		}
//		else if (source.startsWith("_")) {
//			// Global memory value
//			int address = parse(source, 1);
//
//		}
//		else if (source.startsWith("%")) {
//			// Local memory value
//		}
//		else if (source.startsWith("^_")) {
//			// Dereference a global memory location.
//		}
//		else if (source.startsWith("^%")) {
//			// Dereference a local memory location.
//
//		}
//		else if (source.startsWith("^_")) {
//			// Dereference a local memory location.
//
//		}
//		else if (looksLikeAFloat(source)) {
//			// Floating point literal value.
//		}
//		else {
//			// Integer literal.
//		}
//	}

	private boolean looksLikeAFloat(String s) {
		return s.contains(".") || s.contains("e");
	}
	private boolean isAddress(String s) {
		char c = s.charAt(0);
		boolean result = false;
		switch (c) {
			case '%':
			case '^':
			case '_':
				result = true;
		}
		return result;
	}

	private void writeTo(int value, String address) {
		int index = decodeAddress(address);
		memory[index].write(value);
	}

	private void writeTo(float value, String address) {
		int index = decodeAddress(address);
		memory[index].write(value);
	}

	private int decodeAddress(String address) {
		int index = -1;
		if (address.startsWith("^%")) {
			index = Integer.parseInt(address.substring(2)) + fp;
			index = memory[index].readInt();
		}
		else if (address.startsWith("^_")) {
			index = Integer.parseInt(address.substring(2));
			index = memory[index].readInt();
		}
		else if (address.startsWith("%")) {
			index = Integer.parseInt(address.substring(1)) + fp;
		}
		else if (address.startsWith("_")) {
			index = Integer.parseInt(address.substring(1));
		}
		else {
			throw TVIError.ADDRESS_NOT_WRITABLE(address);
		}
		return index;
	}

	private int decodeInt(String input) {
		if (input.startsWith("@")) {
			return addressOf(input);
		}

		return decode(input, Integer::parseInt, i -> memory[i].readInt());
	}

	private float decodeFloat(String input) {
		if (input.startsWith("@")) {
			throw TVIError.INVALID_MODE(input);
		}
		return decode(input, Float::parseFloat, i -> memory[i].readFloat());
	}

	private int addressOf(String input) {

		if (input.startsWith("@_")) {
			return Integer.parseInt(input.substring(2));
		}
		else if (input.startsWith("@%")) {
			int address = Integer.parseInt(input.substring(2));
			debug("[@%] FP:%d OFF:%d", fp, address);
			return Integer.parseInt(input.substring(2)) + fp;
		}
		else {
			throw TVIError.INVALID_MODE(input);
		}
	}

	private <T extends Number> T decode(String input, Function<String,T> literal, Function<Integer, T> read) {
		T result;
		int offset;
		if (input.startsWith("^%")) {
			// Dereference local memory.
			offset =  Integer.parseInt(input.substring(2)) + fp;
			offset = memory[offset].readInt();
			result = read.apply(offset);
		}
		else if (input.startsWith("^_")) {
			// Dereference global memory.
			offset =  Integer.parseInt(input.substring(2));
			offset = memory[offset].readInt();
			result = read.apply(offset);
		}
		else if (input.startsWith("%")) {
			// Read local memory location.
			offset =  Integer.parseInt(input.substring(1)) + fp;
			result = read.apply(offset);
		}
		else if (input.startsWith("_")) {
			// Read global memory location.
			offset =  Integer.parseInt(input.substring(1));
			result = read.apply(offset);
		}
		else {
			// Assume a literal value.
			result = literal.apply(input);
		}
		return result;
	}

//	private <T extends Number> T decode(String input, Function<String,T> parse, Function<String,T> generate) {
//		int result = 0;
//		int offset;
//		if (input.startsWith("@_")) {
//			result = Integer.valueOf(input.substring(2));
//		}
//		else if (input.startsWith("@%")) {
//			result = Integer.valueOf(input.substring(2)) + fp;
//		}
//		else if (input.startsWith("^%")) {
//			offset =  Integer.parseInt(input.substring(2)) + fp;
//			offset = memory[offset].readInt();
//			result = memory[offset].readInt();
//		}
//		else if (input.startsWith("^_")) {
//			offset =  Integer.parseInt(input.substring(2));
//			offset = memory[offset].readInt();
//			result = memory[offset].readInt();
//		}
//		else if (input.startsWith("%")) {
//			offset =  Integer.parseInt(input.substring(1)) + fp;
//			result = memory[offset].readInt();
//		}
//		else if (input.startsWith("_")) {
//			offset =  Integer.parseInt(input.substring(1));
//			result = memory[offset].readInt();
//		}
//		else {
////			throw new TVIError("Invalid addressing mode: " + input);
//			result = Integer.valueOf(input);
//		}
//		return result;
//	}

//	private int decodeInt(String input, int start) {
//		int result = 0;
//		int index;
//		char c = input.charAt(start);
//		switch(c) {
//			case '@':
//				result = decodeInt(input, start + 1);
//				break;
//			case '^':
//				index = decodeInt(input, start + 1);
//				result = memory[index].readInt();
//				break;
//			case '%':
//				index = Integer.parseInt(input.substring(start+1));
//				result = memory[index + fp].readInt();
//				break;
//			case '_':
//				index = Integer.parseInt(input.substring(start+1));
//				result = memory[index].readInt();
//				break;
//			default:
//				result = Integer.parseInt(input.substring(start));
//				break;
//		}
//		return result;
//	}

	private void notNull(Object value, String message) {
		if (value == null) {
			throw TVIError.GENERIC(message);
		}
	}

	private void trace(Instruction instruction) {
		if (!tracing) {
			return;
		}
		System.out.println(instruction);
	}

	private void debug(String message, Object... args) {
		if (!debugging) {
			return;
		}
		if (args.length > 0) {
			System.out.println(String.format(message, args));
		}
		else {
			System.out.println(message);
		}
	}
}
