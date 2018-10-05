package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a charsets command. Charsets command is called without
 * an argument and gives the user a list of all available charsets to use in a
 * shell.
 * 
 * @author Dinz
 *
 */
public class CharsetsShellCommand implements ShellCommand {
	/**
	 * Method that executes the charsets command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!arguments.trim().isEmpty()) {
			throw new IllegalArgumentException("Command \"charsets\" does not accept arguments.");
		}
		for (String charset : Charset.availableCharsets().keySet()) {
			env.writeln(charset);
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "charsets";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("Command \"charsets\" takes no arguments and lists names of");
				add("supported charsets for your Java platform. A single charset");
				add("name is written per line.");
			}
		};
	}

}
