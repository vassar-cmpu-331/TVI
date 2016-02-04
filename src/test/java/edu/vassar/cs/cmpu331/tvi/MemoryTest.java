package edu.vassar.cs.cmpu331.tvi;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author Keith Suderman
 */
public class MemoryTest
{
	Memory memory;

	@Before
	public void setup() {
		memory = new Memory();
	}

	@After
	public void cleanup() {
		memory = null;
	}

	@Test
	public void copyFloats() {
		memory.write(3.14f);
		int t = memory.readInt();
		Memory temp = new Memory();
		temp.write(t);
		Float f = temp.readFloat();

		assertEquals(3.14f, f, 0.00001);
	}

	@Test
	public void testInts() {
		test(10);
		test(-1);
		test(0);
		test(Integer.MAX_VALUE);
		test(Integer.MIN_VALUE);
	}

	@Test
	public void testFloats() {
		test(3.14f);
		test(-3.14f);
		test(0f);
		test(1f);
		test(-1f);
		test(Float.MAX_VALUE);
		test(Float.MIN_VALUE);
	}

	private void test(int expected) {
		memory.write(expected);
		int actual = memory.readInt();
		assertEquals(expected, actual);
	}

	private void test(float expected) {
		memory.write(expected);
		float actual = memory.readFloat();
		assertEquals(expected, actual, 0.0001);
	}
}
