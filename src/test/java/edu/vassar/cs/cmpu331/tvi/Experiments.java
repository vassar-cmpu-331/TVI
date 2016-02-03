package edu.vassar.cs.cmpu331.tvi;

import org.apache.commons.cli.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.out;

/**
 * @author Keith Suderman
 */
@Ignore
public class Experiments
{
	@Test
	public void threadSafeCollections() {
		List<Integer> list = IntStream.range(1, 1000)
				  .parallel()
				  .mapToObj(Integer::valueOf)
				  .collect(Collectors.toList());

		System.out.println(list.getClass().getName());
		list.forEach(System.out::println);
	}

	@Test
	public void generateOpcodes() throws IOException
	{
		Path path = Paths.get("src/test/resources/opcodes.txt");
		String s = Files.lines(path)
				  .map(String::toUpperCase)
				  .collect(Collectors.joining(", "));
		out.println(s);
	}

	@Test
	public void generateSwitch() throws IOException
	{
		out.println("switch(instruction.getOpcode()) {");
		for (Opcode code : Opcode.values()) {
			out.println("\tcase " + code + ":");
			out.println("\t\tif (true) throw new TVIError(\"This opcode has not implemented.\");");
			out.println("\t\tbreak;");
		}
		out.println("}");
	}

	@Test
	public void testDecode() {
		int i = decode("10", Integer::valueOf);
		assertEquals(10, i);
		float pi = decode("3.14", Float::valueOf);
		assertEquals(3.14, pi, 0.00001);
	}

	@Test
	public void general() throws ParseException
	{
		CommandLineParser parser = new DefaultParser();
		Options options = new Options()
				  .addOption("t", "trace", false, "Enable execution tracing.")
				  .addOption("m", "mem", true, "Sets the CPU memory size")
				  .addOption("v", "version", false, "Prints the TVI version number")
				  .addOption("h", "help", false, "Prints this help message");

//		String[] args = ;
		CommandLine args = parser.parse(options, new String[]{ "-t", "--mem", "256" });
		if (args.hasOption('t')) {
			System.out.println("Tracing enabled");
		}
		else {
			System.out.println("Tracing disabled");
		}
		if (args.hasOption('m')) {
			System.out.println("Memory size: " + args.getOptionValue('m'));
		}
		else {
			System.out.println("Using default memory size");
		}
		String[] files = args.getArgs();
		if (files == null) {
			System.out.println("files is null");
		}
		else if (files.length == 0) {
			System.out.println("No filename provided.");
		}
		else {
			Arrays.stream(files).forEach(System.out::println);
		}
		HelpFormatter out = new HelpFormatter();
		out.printHelp("tvi -tm <size>", options);

	}

	private String identity(String input) { return input; }

	private <T extends Number> T decode(String input, Function<String,T> factory) {
		System.out.println("T is a " + factory.getClass().getName());
		return factory.apply(input);
	}
}
