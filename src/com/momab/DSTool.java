package com.momab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.momab.dstool.DSOperation;
import com.momab.dstool.DSOperationBuilder;

/**
 * @author Jonas Andreasson
 * 
 *         This code is copyrighted under the MIT license. Please see
 *         LICENSE.TXT.
 * 
 */
public class DSTool {

	final static String TOOLNAME = "dstool";
	final static String CMDLINE_SYNTAX = TOOLNAME + " appurl entitykind";

	static String getVersionString() {
		Properties prop = new Properties();
		InputStream in = DSTool.class.getResourceAsStream("version.properties");
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			return "Could not read version.properties";
		}
		return prop.getProperty("version");
	}

	static Options commandLineOptions() {

		Option help = new Option("h", "help", false, "print this message");
		Option username = new Option("u", "username", true,
				"Username to use when sending requests to the remote app");
		Option password = new Option("p", "password", true,
				"Password to use when sending requests to the remote app");
		Option port = new Option(
				"P",
				"port",
				true,
				"Network port used when connecting to the app, if omitted the default port 80 wil be used");

		Option read = new Option("r", "read", false,
				"download all entites of the specified kind from the"
						+ "remote app datastore");
		Option write = new Option("w", "write", false,
				"upload entites of the specified kind to the remote"
						+ " app datastore");
		Option delete = new Option("d", "delete", false,
				"delete all entities of the specified kind from the remote"
						+ " app datastore");
		Option file = new Option(
				"f",
				"file",
				true,
				"specify a file name to read/write local entity data from/to"
						+ ", stdin/stdout will be used of the filename is omitted");

		OptionGroup operations = new OptionGroup();
		operations.addOption(read);
		operations.addOption(write);
		operations.addOption(delete);

		Options options = new Options();

		options.addOptionGroup(operations);
		options.addOption(file);
		options.addOption(help);
		options.addOption(username);
		options.addOption(password);
		options.addOption(port);

		return options;
	}

	static void printUsage(Options options, OutputStream out) {

		PrintWriter writer = new PrintWriter(out);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printUsage(writer, 80, CMDLINE_SYNTAX, options);
		writer.flush();
	}

	static void printHelp(Options options, OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		writer.println(
				String.format("%s version %s, Copyright Jonas Andreasson under the MIT license.",
				TOOLNAME, getVersionString()));
		writer.println();
		writer.flush();
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(CMDLINE_SYNTAX, options, true);
		writer.flush();
	}

	static void printErrorMessage(String message, OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		writer.println(String.format("Error: %s", message));
		writer.flush();
	}

	public static void main(String[] args) throws ParseException, IOException,
			ClassNotFoundException {

		final CommandLineParser parser = new BasicParser();
		final Options options = commandLineOptions();
		final CommandLine line = parser.parse(options, args);

		if (args.length == 0) {
			printUsage(options, System.out);
			System.exit(0);
		}

		if (args.length == 1 && line.hasOption("h")) {
			printHelp(options, System.out);
			System.exit(0);
		}

		if (!line.hasOption("r") && !line.hasOption("w")
				&& !line.hasOption("d")) {
			printErrorMessage(
					"One of the options -r -w or -d must be specified.",
					System.err);
			System.exit(-1);
		}

		if (line.getArgs().length != 2) {
			printErrorMessage("To few arguments.", System.err);
			System.exit(-1);
		}

		DSOperation op = null;

		// Required operands/arguments
		DSOperationBuilder opBuilder = new DSOperationBuilder(
				line.getArgs()[0], line.getArgs()[1]);

		// Username and password
		if (line.hasOption("u") || line.hasOption("p")) {
			if (!line.hasOption("u") || !line.hasOption("p")) {
				printErrorMessage("Missing username or password", System.err);
				System.exit(-1);
			}

			opBuilder
					.asUser(line.getOptionValue("u"), line.getOptionValue("p"));
		}

		// Port
		if (line.hasOption("P")) {
			opBuilder = opBuilder.usingPort(Integer.valueOf(line
					.getOptionValue("P")));
		}

		// File
		File file = null;
		OutputStream out = null;
		InputStream in = null;
		if (line.hasOption("f")) {
			if (line.hasOption("d")) {
				printErrorMessage(
						"Option -f is invalid for a -d delete operation",
						System.err);
				System.exit(-1);
			}

			file = new File(line.getOptionValue("f"));
		}

		// Read (download) operation
		if (line.hasOption("r")) {

			if (file != null) {
				out = new FileOutputStream(file);
			} else {
				out = System.out;
			}

			opBuilder = opBuilder.writeTo(out);

			op = opBuilder.buildDownload();
		}

		// Write (upload) operation
		if (line.hasOption("w")) {

			if (file != null) {
				in = new FileInputStream(file);
			} else {
				in = System.in;
			}

			opBuilder = opBuilder.readFrom(in);

			op = opBuilder.buildUpload();
		}

		// Delete (upload) operation
		if (line.hasOption("d")) {
			op = opBuilder.buildDelete();
		}

		if (file != null || line.hasOption("d")) {
			boolean result = op.Run(new DSOperation.ProgressCallback() {

				@Override
				public void Tick(String message) {
					System.out.print(message);
				}
			});

			if (in != null)
				in.close();
			if (out != null)
				out.close();

			System.exit(result ? 0 : -1);

		} else {
			System.exit(op.Run(null) ? 0 : -1);
		}

	}

}