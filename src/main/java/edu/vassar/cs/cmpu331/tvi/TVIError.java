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

import java.security.PrivilegedActionException;

public class TVIError extends RuntimeException
{
	/**
	 * Constructs a new exception with {@code null} as its detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 */
	public TVIError()
	{
		super("Unknown error encountered.");
	}

	/**
	 * Constructs a new exception with the specified detail message.  The
	 * cause is not initialized, and may subsequently be initialized by
	 * a call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 *                later retrieval by the {@link #getMessage()} method.
	 */
	private TVIError(String message)
	{
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and
	 * cause.  <p>Note that the detail message associated with
	 * {@code cause} is <i>not</i> automatically incorporated in
	 * this exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval
	 *                by the {@link #getMessage()} method).
	 * @param cause   the cause (which is saved for later retrieval by the
	 *                {@link #getCause()} method).  (A <tt>null</tt> value is
	 *                permitted, and indicates that the cause is nonexistent or
	 *                unknown.)
	 */
	private TVIError(String message, Throwable cause)
	{
		super(message, cause);
	}

	public static TVIError INVALID_OPCODE() {
		return new TVIError("The 'invalid' opcode has been encountered.");
	}
	public static TVIError UNKNOWN_OPCODE(String opcode) {
		return new TVIError("Unknown opcode: " + opcode);
	}
	public static TVIError EMPTY_STACK() {
		return new TVIError("PROCEND encountered but call stack is empty.");
	}
	public static TVIError ADDRESS_NOT_WRITABLE(String address) {
		return new TVIError(address + " is not a writable address.");
	}
	public static TVIError INVALID_MODE(String mode) {
		return new TVIError("Invalid addressing mode: " + mode);
	}
	public static TVIError LINE_DEFINED(int line) {
		return new TVIError("Line " + line + " has already been defined.");
	}
	public static TVIError GENERIC(String message) {
		return new TVIError(message);
	}
	//TODO missing quote, unterminated string, too many operands
	public static TVIError UNTERMINATED_STRING() {
		return new TVIError("Unterminated string.");
	}
	public static TVIError MISSING_QUOTE(String line) {
		return new TVIError("Missing quote character: " + line);
	}
	public static TVIError TOO_MANY_OPERANDS(String input) {
		return new TVIError("Too many operands: " + input);
	}

	public static TVIError LABEL_REDEFINED(String name)
	{
		return new TVIError("Label has already been defined: " + name);
	}

	public static TVIError UNKNOWN_LABEL(String name)
	{
		return new TVIError("Unknown label: " + name);
	}
	public static TVIError INT_READ(Throwable e) {
		return new TVIError("Unable to read integer value.", e);
	}
	public static TVIError INT_WRITE(Throwable e) {
		return new TVIError("Unable to write integer value.", e);
	}
	public static TVIError FLOAT_READ(Throwable e) {
		return new TVIError("Unable to read float value.", e);
	}
	public static TVIError FLOAT_WRITE(Throwable e) {
		return new TVIError("Unable to write float value", e);
	}
	public static TVIError BUS_ERROR() {
		return new TVIError("Bus error");
	}

	public static TVIError EXECUTION_FLOW()
	{
		return new TVIError("Execution ran past the last instruction.");
	}
}
