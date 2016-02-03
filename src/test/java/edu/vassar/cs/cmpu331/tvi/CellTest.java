package edu.vassar.cs.cmpu331.tvi;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author Keith Suderman
 */
public class CellTest
{
	Cell cell;

	@Before
	public void setup() {
		cell = new Cell();
	}

	@After
	public void cleanup() {
		cell = null;
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
		cell.write(expected);
		int actual = cell.readInt();
		assertEquals(expected, actual);
	}

	private void test(float expected) {
		cell.write(expected);
		float actual = cell.readFloat();
		assertEquals(expected, actual, 0.0001);
	}
}
