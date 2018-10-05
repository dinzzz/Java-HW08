package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents the rmtree command. This command takes one directory as
 * an argument and removes the content of that directory recursively.
 * 
 * @author Dinz
 *
 */
public class RmtreeShellCommand implements ShellCommand {
	/**
	 * Method that executes the rmtree command.
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

		if (!path.isAbsolute()) {
			path = env.getCurrentDirectory().resolve(path);
		}

		if (!path.toFile().isDirectory()) {
			throw new IllegalArgumentException("Argument has to be a directory.");
		}

		recursive(path.toFile());

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "rmtree";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"rmtree\" recursively removes the content of a directory");
				add("given as an argument.");
			}
		};
	}

	/**
	 * Method that runs recursively through the file content of the directory and
	 * removes files it run into.
	 * 
	 * @param d
	 *            Directory to erase.
	 */
	private static void recursive(File d) {
		if (!d.isDirectory()) {
			throw new IllegalArgumentException("Must be a directory.");
		}
		File[] children = d.listFiles();
		if (children != null) {
			for (File child : children) {
				if (child.isDirectory()) {
					recursive(child);
				} else {
					child.delete();
				}
			}
		}
		d.delete();
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
