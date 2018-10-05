package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a help command. This command can be called without any
 * arguments. If so, it gives the user a list of all commands that shell can
 * execute. The help command can be given an argument which has to be the name
 * of the command that is contained in the shell. Then, the command will give
 * the user the description of the wanted command.
 * 
 * @author Dinz
 *
 */
public class HelpShellCommand implements ShellCommand {
	/**
	 * Method that executes the help command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments.split("\\s+").length > 1) {
			throw new IllegalArgumentException("Wrong number of arguments for the \"help\" command.");
		}
		if (arguments.trim().isEmpty()) {
			for (String cmd : env.commands().keySet()) {
				env.writeln(cmd);
			}
		} else {
			if (env.commands().containsKey(arguments.trim())) {
				for (String string : env.commands().get(arguments.trim()).getCommandDescription()) {
					env.writeln(string);
				}

			} else {
				throw new IllegalArgumentException("There's no such command in the shell.");
			}
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The help command lists names of all supported commands if");
				add("no arguments are input. If started with a single argument,");
				add("It prints the neme and the description of the selected command.");
			}
		};
	}

}
