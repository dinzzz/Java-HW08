package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents the pushd command. This command takes one directory
 * path as an argument which is then set as a current directory, while the
 * previous current directory is pushed to the "current directory stack".
 * 
 * @author Dinz
 *
 */
public class PushdShellCommand implements ShellCommand {
	/**
	 * Stack used as a current directory stack.
	 */
	private Stack<Path> stack = new Stack<>();
	/**
	 * Class that executes the pushd command.
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

		if (env.getSharedData("cdstack") == null) {
			stack.push(env.getCurrentDirectory());
			env.setSharedData("cdstack", stack);
		} else {
			@SuppressWarnings("unchecked")
			Stack<Path> oldStack = (Stack<Path>) env.getSharedData("cdstack");
			oldStack.push(env.getCurrentDirectory());
		}
		env.setCurrentDirectory(path);
		return ShellStatus.CONTINUE;

	}

	@Override
	public String getCommandName() {
		return "pushd";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"pushd\" takes a directory as an argument,");
				add("pushes the current directory to the stack and changes the current");
				add("directory to the argument one.");
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
