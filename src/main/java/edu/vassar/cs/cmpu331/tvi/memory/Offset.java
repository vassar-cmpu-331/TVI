package edu.vassar.cs.cmpu331.tvi.memory;

/**
 * @author Keith Suderman
 */
public abstract class Offset extends Operand
{
	protected int offset;

	public Offset(int offset)
	{
		this.offset = offset;
	}
}
