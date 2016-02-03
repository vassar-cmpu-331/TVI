package edu.vassar.cs.cmpu331.tvi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Keith Suderman
 */
public class FindAll
{
	public FindAll()
	{

	}

	public static void main(String[] args) throws IOException
	{
		String input = null;
		boolean prompt = true;
		Scanner in = new Scanner(System.in);
		while (prompt) {
			System.out.print("Enter an opcode: ");
			input = in.nextLine();
			try {
				Opcode test = Opcode.valueOf(input.toUpperCase());
				prompt = false;
			}
			catch (IllegalArgumentException ignored) {
				System.out.println("Invalid opcode: " + input);
			}
		}
		String opcode = input;
		Path directory = Paths.get("src/test/resources/tvi");
		Consumer<Path> print = file -> {
			try
			{
				Files.lines(file)
						  .filter(line -> line.contains(opcode))
						  .forEach(System.out::println);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		};
		Files.list(directory).forEach(print);

	}
}
