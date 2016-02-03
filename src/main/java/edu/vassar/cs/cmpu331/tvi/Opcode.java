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

/**
 * @author Keith Suderman
 */
public enum Opcode
{
	ADD, ALLOC, ASSERT, BEQ, BGE, BGT, BLE, BLT, BNE,
	CALL, DIV, DUMP, EXIT, FADD, FDIV, FINP, FMUL, FOUTP,
	FREE, FSUB, FTOL, GOTO, INP, INVALID, LOAD, LTOF, MOVE, MUL,
	NEWL, NOP, OUTP, PARAM, PRINT, PROCBEGIN, PROCEND, PUSH, STOR, SUB
}
