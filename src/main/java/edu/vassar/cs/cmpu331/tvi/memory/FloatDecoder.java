package edu.vassar.cs.cmpu331.tvi.memory;

import edu.vassar.cs.cmpu331.tvi.CPU;

/**
 * @author Keith Suderman
 */
public class FloatDecoder implements Decoder<Float>
{
	private CPU cpu;
	public FloatDecoder(CPU cpu)
	{
		this.cpu = cpu;
	}

	@Override
	public Float read(String offset)
	{
		int index = Integer.parseInt(offset);
		return cpu.readFloat(index);
	}

	@Override
	public Float literal(String input)
	{
		return Float.parseFloat(input);
	}
}
