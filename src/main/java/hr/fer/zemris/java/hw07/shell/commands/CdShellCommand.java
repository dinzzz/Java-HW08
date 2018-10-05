package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents the cd command. This command takes one directory path
 * as an argument and makes that directory a current directory for the shell.
 * 
 * @author Dinz
 *
 */
public class CdShellCommand implements ShellCommand {
	/**
	 * Method that executes the cd command.
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

		if (!path.toFile().isDirectory()) {
			throw new IllegalArgumentException("Argument has to be a directory.");
		}

		if (!path.isAbsolute()) {
			path = env.getCurrentDirectory().resolve(path);
		}
		env.setCurrentDirectory(path);
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "cd";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"cd\" changes the current directory");
				add("of the shell.");
			}
		};
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
