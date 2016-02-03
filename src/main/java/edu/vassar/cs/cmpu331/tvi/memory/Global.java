package edu.vassar.cs.cmpu331.tvi.memory;

import edu.vassar.cs.cmpu331.tvi.CPU;

/**
 * @author Keith Suderman
 */
public class Global extends Offset
{
	public Global(int offset)
	{
		super(offset);
	}

	public float readFloat(CPU cpu)
	{
		return cpu.readFloat(offset);
	}

	public int readInt(CPU cpu)
	{
		return cpu.readInt(offset);
	}

	public void writeInt(CPU cpu, int value)
	{

	}

	public void writeFloat(CPU cpu, float value)
	{

	}
}
