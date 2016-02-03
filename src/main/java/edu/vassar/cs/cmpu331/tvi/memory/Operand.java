package edu.vassar.cs.cmpu331.tvi.memory;

import edu.vassar.cs.cmpu331.tvi.CPU;

/**
 * @author Keith Suderman
 */
public abstract class Operand<T extends Number>
{
//	public abstract float readFloat(CPU cpu);
//	public abstract int readInt(CPU cpu);
//	public abstract void writeInt(CPU cpu, int value);
//	public abstract void writeFloat(CPU cpu, float value);
	public T read(int offset) {
		throw new UnsupportedOperationException();
	}
	public void write(T value, int offset) {
		throw new UnsupportedOperationException();
	}
}
