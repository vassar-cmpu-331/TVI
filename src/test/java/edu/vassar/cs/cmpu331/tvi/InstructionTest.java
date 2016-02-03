package edu.vassar.cs.cmpu331.tvi;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author Keith Suderman
 */
public class InstructionTest
{
	public InstructionTest()
	{

	}

	@Test(expected = TVIError.class)
	public void invalidOpcode() throws TVIError
	{
		Instruction i = new Instruction("1:	code	1, _2");
		assertEquals(1, i.getLine());
		assertEquals(Opcode.INVALID, i.getOpcode());
		assertEquals("1", i.getOperand(0));
		assertEquals("_2", i.getOperand(1));
	}

	@Test
	public void simpleTest() throws TVIError
	{
		Instruction i = new Instruction("1:	add	1, _2, %3");
		assertEquals(1, i.getLine());
		assertEquals(Opcode.ADD, i.getOpcode());
		assertEquals("1", i.getOperand(0));
		assertEquals("_2", i.getOperand(1));
		assertEquals("%3", i.getOperand(2));
	}
}
