package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a pwd command. This command takes no arguments and
 * prints out to the console current directory the shell is in.
 * 
 * @author Dinz
 *
 */
public class PwdShellCommand implements ShellCommand {
	/**
	 * Method that executes the pwd command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!arguments.trim().isEmpty()) {
			throw new IllegalArgumentException("THe command \"pwd\" does not accept any arguments.");
		}
		env.writeln(env.getCurrentDirectory().toString());
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "pwd";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"pwd\" prints the current directory position");
				add("to the user.");
			}
		};
	}

}
