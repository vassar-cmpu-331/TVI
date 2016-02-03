package edu.vassar.cs.cmpu331.tvi.memory;

/**
 * @author Keith Suderman
 */
public interface Decoder<T>
{
	T read(String offset);
	T literal(String input);
}
