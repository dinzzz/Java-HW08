package hr.fer.zemris.java.hw07.shell.nameparsing;

import java.util.regex.Matcher;

/**
 * Class that represents a NameBuilderInfo implementation which holds the basic
 * information for renaming files.
 * 
 * @author Dinz
 *
 */
public class NameBuilderInfoImpl implements NameBuilderInfo {
	/**
	 * Matcher instance.
	 */
	private Matcher matcher;
	/**
	 * StringBuilder instance.
	 */
	private StringBuilder sb = new StringBuilder();
	/**
	 * Creates a new NameBuilderInfoImpl instance.
	 * @param matcher Matcher instance.
	 */
	public NameBuilderInfoImpl(Matcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public String getGroup(int index) {
		return matcher.group(index);
	}

	@Override
	public StringBuilder getStringBuilder() {
		return sb;
	}

}
