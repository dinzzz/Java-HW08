package hr.fer.zemris.java.hw07.shell.nameparsing;

/**
 * NameBuilder represents an interface whose implementations perform a certain
 * name building action over the information given
 * 
 * @author Dinz
 *
 */
public interface NameBuilder {
	/**
	 * Method that executes the action over the NameBuilderInfo instance.
	 * 
	 * @param info
	 *            NameBuilderInfo instance.
	 */
	void execute(NameBuilderInfo info);
}
