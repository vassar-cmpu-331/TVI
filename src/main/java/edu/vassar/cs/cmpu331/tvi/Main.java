/*
 * Copyright (c) 2016 Vassar College
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package edu.vassar.cs.cmpu331.tvi;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Keith Suderman
 */
public class Main
{
	private static String COPYRIGHT = "Copyright 2016 Vassar College";

	private static int memory = -1;
	private static boolean tracing = false;
	private static boolean debugging = false;
	private static String[] files;

	private CPU cpu;

	public Main()
	{
	}

	private void parse(String line) throws TVIError
	{
		if (line.startsWith("CODE")) {
			return;
		}
		cpu.add(new Instruction(line));
	}

	private boolean include(String line) {
		if (line.startsWith("CODE")) {
			return false;
		}
		if (line.startsWith(";")) {
			return false;
		}
		if (line.trim().length() == 0) {
			return false;
		}
		return true;
	}

	private static void renumber(String filename) throws IOException
	{
		Path file = Paths.get(filename);
		if (Files.notExists(file)) {
			System.out.println("File not found: " + filename);
			return;
		}
		System.out.println("Renumbering " + filename);
		Function<String,String> trim = s -> {
			int i = s.indexOf(':');
			if (i > 0) {
				return s.substring(i + 1).trim();
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
		System.out.println("Done.");
	}

	private void run(String filename)
	{
		if (memory > 0) {
			cpu = new CPU(memory);
		}
		else {
			cpu = new CPU();
		}
		if (tracing) {
			System.out.println("Tracing enabled.");
			cpu.enableTracing();
		}
		if (debugging) {
			System.out.println("Debug output enabled.");
			cpu.enableDebug();
		}

		Path file = Paths.get(filename);
		if (Files.notExists(file)) {
			System.out.println("ERROR: File not found: " + filename);
			return;
		}

		System.out.println("Running " + filename);
		try
		{
			if (debugging) {
				System.out.println("Loading opcodes.");
			}
			List<String> lines = Files.readAllLines(file);
			for (String line : lines) {
				if (include(line))
				{
					cpu.add(new Instruction(line));
				}
			}
			if (debugging) {
				System.out.println("Execution starting.");
			}
			cpu.run();
		}
		catch(IOException|TVIError e)
		{
			System.out.println(e.getMessage());
		}
	}

	private static void help(Options options) {
		System.out.println();
		System.out.println("TVI v" + Version.getVersion());
		System.out.println(COPYRIGHT);
		System.out.println();
		HelpFormatter out = new HelpFormatter();
		out.printHelp("java -jar tvi-" + Version.getVersion() + ".jar [options] <file> [<file>...]", options);
		System.out.println();
	}
	public static void main(String[] args) {
		Options options = new Options()
				  .addOption("t", "trace", false, "Enable tracing.")
				  .addOption("d", "debug", false, "Enable debug output.")
				  .addOption("s", "size", true, "Sets amount of memory available.")
				  .addOption("v", "version", false, "Prints the TVI version number.")
				  .addOption("r", "renumber", true, "Renumbers the lines in a files.")
				  .addOption("h", "help", false, "Prints this help message");
		CommandLineParser parser = new DefaultParser();
		try
		{
			CommandLine opts = parser.parse(options, args);
			if (opts.hasOption('h')) {
				help(options);
				return;
			}
			if (opts.hasOption('v')) {
				System.out.println();
				System.out.println("The Vassar Interpreter v" + Version.getVersion());
				System.out.println(COPYRIGHT);
				System.out.println();
				return;
			}
			if (opts.hasOption('r')) {
				int returnCode = 0;
				try
				{
					renumber(opts.getOptionValue('r'));
				}
				catch (IOException e)
				{
					e.printStackTrace();
					returnCode = 1;
				}
				System.exit(returnCode);
			}
			files = opts.getArgs();
			if (files.length == 0) {
				System.out.println("ERROR: No file names given.");
				help(options);
				System.exit(1);
			}
			if (opts.hasOption('s')) {
				try {
					memory = Integer.parseInt(opts.getOptionValue('s'));
				}
				catch (NumberFormatException e) {
					System.out.println("ERROR: Invalid --size parameter.");
					help(options);
					System.exit(1);
				}
			}
			if (opts.hasOption('t')) {
				tracing = true;
			}
			if (opts.hasOption('d')) {
				debugging = true;
			}
		}
		catch(ParseException e) {
			System.out.println(e.getMessage());
			help(options);
			System.exit(1);
		}
		Main app = new Main();
		Arrays.stream(files).forEach(app::run);
	}
//	public static void main(String[] args) {
//		Scanner in = new Scanner(System.in);
//		System.out.print("Enter a filename: ");
//		String filename = in.nextLine();
//		if (!filename.endsWith(".tvi")) {
//			filename = filename + ".tvi";
//		}
//		Path file = Paths.get("src/test/resources/tvi", filename);
//		if (!Files.exists(file)) {
//			System.out.println("File not found: " + file.getFileName());
//			return;
//		}
//		new Main();
//	}
}
