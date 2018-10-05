package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Represents a basic environment for shell creation. Contains all the basic
 * needed methods that one shell must have and use.
 * 
 * @author Dinz
 *
 */
public interface Environment {
	/**
	 * Reads the input from the user.
	 * 
	 * @return Input user in form of a string.
	 * @throws ShellIOException
	 *             If something go wrong while reading the input.
	 */
	String readLine() throws ShellIOException;

	/**
	 * Writes the content to the shell output.
	 * 
	 * @param text
	 *            Text that is to be written.
	 * @throws ShellIOException
	 *             If something go wrong while writing.
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Writes the content to the shell output and goes to the new line.
	 * 
	 * @param text
	 *            Text that is to be written.
	 * @throws ShellIOException
	 *             If something go wrong while writing.
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * Gets the sorted map of commands that shell supports.
	 * 
	 * @return Commands that shell supports.
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * Gets the symbol that represents the usage of multilines in the user input.
	 * 
	 * @return Multilines symbol.
	 */
	Character getMultilineSymbol();

	/**
	 * Sets the symbol that represents multilines in the input.
	 * 
	 * @param symbol
	 *            New Multiline symbol.
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Gets the shell prompt symbol.
	 * 
	 * @return Shell prompt symbol.
	 */
	Character getPromptSymbol();

	/**
	 * Sets the shell prompt symbol.
	 * 
	 * @param symbol
	 *            New prompt symbol.
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Gets the symbol that represents going into the new line for the user input.
	 * 
	 * @return Morelines symbol.
	 */
	Character getMorelinesSymbol();

	/**
	 * Sets the Morelines symbol.
	 * 
	 * @param symbol
	 *            New Morelines symbol.
	 */
	void setMorelinesSymbol(Character symbol);
	/**
	 * Gets the current directory.
	 * @return Current directory.
	 */
	Path getCurrentDirectory();
	/**
	 * Sets the current directory of the environment.
	 * @param path Directory to be set as current.
	 */
	void setCurrentDirectory(Path path);
	/**
	 * Gets the shared data for the given key.
	 * @param key Key of the data.
	 * @return Data that belongs to the given key.
	 */
	Object getSharedData(String key);
	/**
	 * Sets shared data.
	 * @param key Key of the data.
	 * @param value Value of the data.
	 */
	void setSharedData(String key, Object value);


}
