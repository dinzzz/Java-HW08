package hr.fer.zemris.java.hw07.shell.nameparsing;

/**
 * Interface whose implementations hold the information needed for the
 * generation of new file names.
 * 
 * @author Dinz
 *
 */
public interface NameBuilderInfo {
	/**
	 * Returns the string value which is a part of the name that belongs to the
	 * group at the given index.
	 * 
	 * @param index
	 *            Index of the group.
	 * @return String value.
	 */
	String getGroup(int index);
	/**
	 * Returns the StringBuilder instance.
	 * @return The StringBuilder instance.
	 */
	StringBuilder getStringBuilder();
}
