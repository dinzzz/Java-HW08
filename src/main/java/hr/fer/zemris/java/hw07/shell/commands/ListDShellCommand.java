package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents the listd command. This command expects no arguments
 * and lists all the paths that are currently stored on the "change directory
 * stack" in the shared data.
 * 
 * @author Dinz
 *
 */
public class ListDShellCommand implements ShellCommand {
	/**
	 * Method that executes the listd command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (env.getSharedData("cdstack") == null) {
			throw new IllegalArgumentException("Stack is empty.");
		} else {
			@SuppressWarnings("unchecked")
			Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");

			if (stack.isEmpty()) {
				throw new IllegalArgumentException("Stack is empty.");
			}

			@SuppressWarnings("unchecked")
			Stack<Path> copy = (Stack<Path>) stack.clone();
			while (!copy.isEmpty()) {
				env.writeln(copy.pop().toString());
			}
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "listd";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"listd\" lists all the available paths");
				add("on the \"change directory stack\" to the output.");
			}
		};
	}

}
