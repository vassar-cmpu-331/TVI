package edu.vassar.cs.cmpu331.tvi;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Keith Suderman
 */
public class StringTable
{
	private List<String> table = new ArrayList<>();

	public StringTable()
	{

	}

	public int add(String s) {
		int index = table.size();
		table.add(s);
		return index;
	}

	public String get(int index) {
		if (index < 0 || index >= table.size()) {
			throw new IndexOutOfBoundsException();
		}
		return table.get(index);
	}
}
