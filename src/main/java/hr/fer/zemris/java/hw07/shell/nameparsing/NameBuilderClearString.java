package hr.fer.zemris.java.hw07.shell.nameparsing;

/**
 * Class that represents a NameBuilder instance which performs a name building
 * actions with pure string values.
 * 
 * @author Dinz
 *
 */
public class NameBuilderClearString implements NameBuilder {
	/**
	 * String value.
	 */
	private String clearString;

	/**
	 * Constructs a new NameBuilderClearString instance.
	 * 
	 * @param clearString
	 *            String value.
	 */
	public NameBuilderClearString(String clearString) {
		this.clearString = clearString;
	}

	@Override
	public void execute(NameBuilderInfo info) {
		info.getStringBuilder().append(clearString);

	}

}
