package hr.fer.zemris.java.hw07.shell.nameparsing;

/**
 * Class that represents a NameBuilder instance which handles the name building
 * actions when grouping of the file name occurs.
 * 
 * @author Dinz
 *
 */
public class NameBuilderGroups implements NameBuilder {
	/**
	 * Index of the group.
	 */
	private int index;
	/**
	 * Number of spaces wanted.
	 */
	private int spacing;
	/**
	 * Type of spacing.
	 */
	private SpacingType type;
	/**
	 * Constructs a new NameBuilderGroups instance.
	 * @param index Index of the group-
	 * @param spacing Number of spaces.
	 * @param type Type of spacing.
	 */
	public NameBuilderGroups(int index, int spacing, SpacingType type) {
		this.index = index;
		this.spacing = spacing;
		this.type = type;
	}

	@Override
	public void execute(NameBuilderInfo info) {

		if (type == SpacingType.NOTHING) {
			info.getStringBuilder().append(info.getGroup(index));
		} else if (type == SpacingType.SPACING) {
			info.getStringBuilder().append(String.format("%1$" + spacing + "s", info.getGroup(index)));
		} else if (type == SpacingType.ZEROING) {
			info.getStringBuilder()
					.append(String.format("%1$" + spacing + "s", info.getGroup(index)).replace(' ', '0'));
		}

	}

}
