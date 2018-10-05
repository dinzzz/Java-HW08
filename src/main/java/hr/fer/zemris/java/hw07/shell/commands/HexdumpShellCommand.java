package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a hexdump command. Hexdump command takes one file from
 * the user and formats the hexdump representation of that file. It is a format
 * used by many file stream tools such as programs which monitor network and
 * protocol activities. It transforms the file into a series of byte elements.
 * 
 * @author Dinz
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	/**
	 * Method that executes the hexdump command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		Path path;

		try {
			String argumentsParsed = parsing(arguments);

			path = Paths.get(argumentsParsed);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
		if(!path.isAbsolute()) {
			path = env.getCurrentDirectory().resolve(path);
		}
		
		if (!path.toFile().isFile()) {
			throw new IllegalArgumentException("Input must be a file!");
		}

		try {
			env.write(fileToHex(path.toFile()));
		} catch (ShellIOException e) {
			env.writeln(e.getMessage());
		} catch (IOException e) {
			env.writeln(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "hexdump";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"hexdump\" expects a file name as an argument");
				add("and produces hex-output of the given file");
			}
		};
	}

	/**
	 * Method that transforms the file into a hexdump friendly output with parsing
	 * the file content to byte elements.
	 * 
	 * @param file
	 *            File to be transformed.
	 * @return Appropriate output for the hexdump command.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static String fileToHex(File file) throws IOException, FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		int rowCount = 0;

		InputStream inputStream = new FileInputStream(file);

		while (true) {
			byte[] buffer = new byte[16];
			int r = inputStream.read(buffer);
			if (r < 1) {
				break;
			}
			sb.append(String.format("%08x0: ", rowCount));
			for (int i = 0; i < 8; i++) {
				if (r <= i) {
					for (int j = 0; j < 8 - r && j < 8; j++) {
						sb.append("  ");
						if (j != 8 - r - 1) {
							sb.append(" ");
						}
					}
					break;
				} else {
					sb.append(String.format("%02x", buffer[i]));
					if (i != 7) {
						sb.append(" ");
					}
				}
			}
			sb.append("|");
			for (int i = 8; i < 16; i++) {
				if (r <= i) {
					for (int j = 0; j < 16 - r && j < 8; j++) {
						sb.append("   ");
					}
					break;
				} else {
					sb.append(String.format("%02x ", buffer[i]));
				}

			}
			sb.append("|");
			for (int i = 0; i < r; i++) {
				if (buffer[i] < 32 || buffer[i] > 127) {
					sb.append(".");
				} else {
					sb.append((char) buffer[i]);
				}
			}
			sb.append("\n");
			rowCount++;
		}

		inputStream.close();
		return sb.toString();

	}

	/**
	 * Method that is used to parse the arguments for the command.
	 * 
	 * @param arguments
	 *            String line that the shell gives to the command.
	 * @return Proper format of the arguments.
	 * @throws IllegalArgumentException
	 *             If the arguments are invalid.
	 */
	private static String parsing(String arguments) {
		if (arguments.contains("\"")) {
			int firstIndex = arguments.indexOf("\"");
			int secondIndex = arguments.indexOf('\"', arguments.indexOf('\"') + 1);
			if (firstIndex != 0 || secondIndex == -1 || !arguments.substring(secondIndex + 1).trim().isEmpty()) {
				throw new IllegalArgumentException("Wrong use of quotation marks.");
			}
			return arguments.replaceAll("\"", "");
		} else {
			if (arguments.split("\\s+").length != 1) {
				throw new IllegalArgumentException("Wrong number of arguments in the \"hexdump\" command.");
			}
			return arguments.trim();
		}
	}

}
