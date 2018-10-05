package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents the dropd command. This command expects no arguments
 * from the user and removes the directory at the top of the "change directory
 * stack" without performing any changes considering the current directory of
 * the shell.
 * 
 * @author Dinz
 *
 */
public class DropdShellCommand implements ShellCommand {
	/**
	 * Method that executes the dropd command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (!arguments.trim().isEmpty()) {
			throw new IllegalArgumentException("THe command \"popd\" does not accept any arguments.");
		}

		if (env.getSharedData("cdstack") == null) {
			throw new IllegalArgumentException("Stack is empty.");
		} else {
			@SuppressWarnings("unchecked")
			Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");

			if (stack.isEmpty()) {
				throw new IllegalArgumentException("Stack is empty.");
			}
			stack.pop();

		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "dropd";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"dropd\" drops one path from the \"change directory stack\"");
			}
		};
	}

}
