package edu.vassar.cs.cmpu331.tvi;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * @author Keith Suderman
 */
public class Scan
{
	public Scan()
	{

	}

	public void run() throws IOException
	{
		Path directory = Paths.get("src/test/resources/tvi");
		if (!Files.exists(directory)) {
			throw new FileNotFoundException();
		}
		Function<Path,List<String>> getAllLines = (p) -> {
			try {
				return Files.readAllLines(p, Charset.forName("UTF-8"));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		};
		String codes = Files.list(directory)
				  .map(getAllLines)
				  .flatMap(List::stream)
				  .filter(line -> !line.startsWith("CODE"))
				  .map(line -> line.split("\\s+")[1])
				  .distinct()
				  .sorted()
				  .map(String::toUpperCase)
				  .collect(Collectors.joining(", "));
		System.out.println(codes);
	}

	public void printSwitch() throws IOException
	{
		Path directory = Paths.get("src/test/resources/tvi");
		if (!Files.exists(directory)) {
			throw new FileNotFoundException();
		}
		Function<Path,List<String>> getAllLines = (p) -> {
			try {
				return Files.readAllLines(p, Charset.forName("UTF-8"));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		};
		List<String> codes = Files.list(directory)
				  .map(getAllLines)
				  .flatMap(List::stream)
				  .filter(line -> !line.startsWith("CODE"))
				  .map(line -> line.split("\\s+")[1])
				  .distinct()
				  .sorted()
				  .map(String::toUpperCase)
				  .collect(Collectors.toList());

		out.println("switch(instruction.getOpcode()) {");
		codes.forEach(code -> {
			out.println("\tcase " + code + ":");
			out.println("\t\tif (true) throw new TVIError(\"This opcode has not been implemented.\");");
			out.println("\t\tbreak;");

		});
		out.println("\tdefault:");
		out.println("\t\tthrow new TVIError(\"Unknown opcode\" + instruction.getOpcode());");
		out.println("}");
	}

	public void gatherDestinations() throws IOException
	{
		Path source = Paths.get("src/test/resources/tvi/ult.tvi");
		Files.lines(source)
				  .map( s -> s.split("\\s+"))
				  .filter(a -> a.length == 5)
				  .map( a -> Arrays.copyOfRange(a, 2, 5))
				  .flatMap(Arrays::stream)
				  .distinct()
				  .sorted()
				  .forEach(System.out::println);
	}

	public static void main(String[] args) {
		try
		{
			new Scan().gatherDestinations();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
