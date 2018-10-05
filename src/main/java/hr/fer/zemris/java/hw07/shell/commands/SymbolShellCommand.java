package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a symbol command. This command allows user to check the
 * current prompt, multiline and morelines symbol and also allows him to change
 * them.
 * 
 * @author Dinz
 *
 */
public class SymbolShellCommand implements ShellCommand {
	/**
	 * Method that executes the symbol command.
	 * 
	 * @throws IllegalArgumentException
	 *             If the arguments are invalid.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments.split("\\s+").length == 1) {
			if (arguments.trim().equals("PROMPT")) {
				env.writeln("Symbol for PROMPT is " + env.getPromptSymbol());
			} else if (arguments.trim().equals("MULTILINE")) {
				env.writeln("Symbol for MULTILINE is " + env.getMultilineSymbol());
			} else if (arguments.trim().equals("MORELINES")) {
				env.writeln("Symbol for MORELINES is " + env.getMorelinesSymbol());
			} else {
				throw new IllegalArgumentException("Unknown symbol type.");
			}
		} else if (arguments.split("\\s+").length == 2) {
			if (arguments.split("\\s+")[1].length() != 1) {
				throw new IllegalArgumentException("Second argument must be a single symbol!");
			}

			if (Character.isAlphabetic(arguments.split("\\s+")[1].charAt(0))) {
				throw new IllegalArgumentException(
						"Implementator of this shell forbids usage of letters and digits for prompt symbols.");
			}

			if (arguments.split("\\s+")[0].equals("PROMPT")) {
				env.writeln("Symbol for PROMPT changed from " + env.getPromptSymbol() + " to "
						+ arguments.split("\\s+")[1]);
				env.setPromptSymbol(arguments.split("\\s+")[1].charAt(0));
			} else if (arguments.split("\\s+")[0].equals("MULTILINE")) {
				env.writeln("Symbol for MULTILINE changed from " + env.getMultilineSymbol() + " to "
						+ arguments.split("\\s+")[1]);
				env.setMultilineSymbol(arguments.split("\\s+")[1].charAt(0));
			} else if (arguments.split("\\s+")[0].equals("MORELINES")) {
				env.writeln("Symbol for MORELINES changed from " + env.getMorelinesSymbol() + " to "
						+ arguments.split("\\s+")[1]);
				env.setMorelinesSymbol(arguments.split("\\s+")[1].charAt(0));
			} else {
				throw new IllegalArgumentException("Unknown symbol type.");
			}
		} else {
			throw new IllegalArgumentException("Wrong arguments in the \"symbol\" command.");
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "symbol";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("Command \"symbol\" takes one or two arguments. If there is a");
				add("single argument in the command, it prints out the current symbol");
				add("for the input prompt symbol. If there are two arguments, the command");
				add("changes the symbol for the input prompt symbol to the second argument");
			}
		};
	}

}
