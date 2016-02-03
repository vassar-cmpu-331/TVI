package edu.vassar.cs.cmpu331.tvi.memory;

import edu.vassar.cs.cmpu331.tvi.CPU;

/**
 * @author Keith Suderman
 */
public class IntegerDecoder implements Decoder<Integer>
{
	private CPU cpu;

	public IntegerDecoder(CPU cpu)
	{
		this.cpu = cpu;
	}

	@Override
	public Integer read(String offset)
	{
		int index = Integer.parseInt(offset);
		cpu.readInt(index);
		return null;
	}

	@Override
	public Integer literal(String input)
	{
		return Integer.parseInt(input);
	}
}
