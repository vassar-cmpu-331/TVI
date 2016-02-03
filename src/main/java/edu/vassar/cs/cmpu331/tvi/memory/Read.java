package edu.vassar.cs.cmpu331.tvi.memory;

import edu.vassar.cs.cmpu331.tvi.CPU;

/**
 * @author Keith Suderman
 */
public class Read<T extends Number> extends Operand<T>
{
	private CPU cpu;

	public Read(CPU cpu)
	{
		this.cpu = cpu;
	}
}
