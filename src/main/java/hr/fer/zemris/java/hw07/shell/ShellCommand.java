package hr.fer.zemris.java.hw07.shell;

import java.util.List;

/**
 * Represents a command in a shell. The main attributes of the command are its
 * execution, name and description.
 * 
 * @author Dinz
 *
 */
public interface ShellCommand {
	/**
	 * Method that executes the command.
	 * 
	 * @param env
	 *            Environment where the command executes.
	 * @param arguments
	 *            Arguments of the command.
	 * @return Status of the shell.
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * Gets the name of the command.
	 * 
	 * @return Name of the command.
	 */
	String getCommandName();

	/**
	 * Gets the description of the command.
	 * 
	 * @return Description of the command.
	 */
	List<String> getCommandDescription();
}
