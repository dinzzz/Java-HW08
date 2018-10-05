package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a popd command. This command pops one directory path
 * from the "current directory stack" and makes that same directory as current
 * for the certain shell.
 * 
 * @author Dinz
 *
 */
public class PopdShellCommand implements ShellCommand {
	/**
	 * Method that executes the popd command.
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
			Path path = stack.pop();

			if (path.toFile().isDirectory() && path.toFile().exists()) {
				env.setCurrentDirectory(path);
			}

		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "popd";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"popd\" pops one path from the \"change directory stack\"");
				add("and if this is the valid path, makes it as a current directory.");
			}
		};
	}

}
