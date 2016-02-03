package edu.vassar.cs.cmpu331.tvi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Keith Suderman
 */
public class Renumber
{
	public Renumber()
	{

	}

	public static void renumber(int n, Instruction i) {
		i.setLine(n);
	}

	public static void main(String[] args) throws IOException
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a filename: ");
		String filename = in.nextLine();
		if (!filename.endsWith(".tvi")) {
			filename = filename + ".tvi";
		}
		Path file = Paths.get("src/test/resources/tvi", filename);
		if (Files.notExists(file)) {
			System.out.println("File not found: " + filename);
			return;
		}
		Function<String,String> trim = s -> {
			int i = s.indexOf(':');
			if (i > 0) {
				return s.substring(i+1);
			}
			return s.trim();
		};
		List<String> list = Files.lines(file)
				  .filter(line -> !line.startsWith("CODE"))
				  .map(line -> trim.apply(line))
				  .collect(Collectors.toList());
		int size = list.size();
		PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file));
//		PrintStream writer = System.out;
		writer.println("CODE");
		IntStream.range(0, size).forEach(index -> {
			String line = list.get(index);

			writer.println(index + ": " + line);
		});
		writer.close();
	}
}
