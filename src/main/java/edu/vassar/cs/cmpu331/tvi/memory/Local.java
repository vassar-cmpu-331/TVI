package edu.vassar.cs.cmpu331.tvi.memory;

import edu.vassar.cs.cmpu331.tvi.CPU;

/**
 *
 * @author Keith Suderman
 */
public class Local extends Offset
{
	public Local(int offset)
	{
		super(offset);
	}

	public float readFloat(CPU cpu)
	{
//		int actual = offset + cpu.fp();
		return 0;
	}

	public int readInt(CPU cpu)
	{
		return 0;
	}

	public void writeInt(CPU cpu, int value)
	{

	}

	public void writeFloat(CPU cpu, float value)
	{

	}
}
