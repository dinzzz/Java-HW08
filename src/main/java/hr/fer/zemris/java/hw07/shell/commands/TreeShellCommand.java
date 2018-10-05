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
 * Class that demonstrates a tree command. It is a command that takes one
 * directory, recursively runs through the content of that directory while
 * forming a correct output in the form of a tree.
 * 
 * @author Dinz
 *
 */
public class TreeShellCommand implements ShellCommand {
	/**
	 * Method that executes a tree command.
	 * 
	 * @throws IllegalArgumentException
	 *             If the arguments in the command are invalid.
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

		if (!path.toFile().isDirectory()) {
			throw new IllegalArgumentException("Argument has to be a directory.");
		}		
		

		env.write(recursive(path.toFile(), 0));

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "tree";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"tree\" expects a directory as a single argument");
				add("and prints out the directory content recursively");
			}
		};
	}

	/**
	 * Method that recursively runs through the given directory forming a tree
	 * output.
	 * 
	 * @param d
	 *            Root directory of the tree.
	 * @param level
	 *            Level of the directory in a tree.
	 * @return Tree in the string format.
	 */
	private static String recursive(File d, int level) {
		if (!d.isDirectory()) {
			throw new IllegalArgumentException("Must be a directory.");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append(" ");
		}
		sb.append("Directory name: " + d.getName() + "\n");
		File[] children = d.listFiles();
		if (children != null) {
			for (File child : children) {
				if (child.isDirectory()) {
					sb.append(recursive(child, level + 2));
				} else {
					for (int i = 0; i < level; i++) {
						sb.append(" ");
					}
					sb.append("-" + child.getName() + "\n");
				}
			}
		}
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
