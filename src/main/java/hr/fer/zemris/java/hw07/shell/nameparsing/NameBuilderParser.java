package hr.fer.zemris.java.hw07.shell.nameparsing;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a name building parser. This parser takes the
 * expression given by the user, and parses it so it forms a unique setup for
 * file renaming.
 * 
 * @author Dinz
 *
 */
public class NameBuilderParser {
	/**
	 * Given expression.
	 */
	String expression;
	/**
	 * Final NameBuilder instance.
	 */
	NameBuilder nameBuilder;

	/**
	 * Constructs a new NameBuilderParser.
	 * 
	 * @param expression
	 */
	public NameBuilderParser(String expression) {
		this.expression = expression;
		this.nameBuilder = parsing();
	}

	/**
	 * returns the final NameBuilder instance.
	 * 
	 * @return final NameBuilder instance.
	 */
	public NameBuilder getNameBuilder() {
		return nameBuilder;
	}

	/**
	 * Method that parses the expression and returns the final NameBuilder instance
	 * which holds all the other needed NameBuilder instances used in process.
	 * 
	 * @return NameBuilder instance.
	 */
	private NameBuilder parsing() {
		List<NameBuilder> list = new ArrayList<>();
		char[] chars = expression.toCharArray();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < chars.length; i++) {

			if (chars[i] == '$') {
				if (i > chars.length - 4) {
					throw new IllegalArgumentException("Illegal expression.");
				}

				list.add(new NameBuilderClearString(sb.toString()));
				sb = new StringBuilder();

				if (chars[++i] != '{') {
					throw new IllegalArgumentException("Illegal expression.");
				}
				char checker = chars[++i];

				while (checker != '}') {
					if (i == chars.length - 1) {
						throw new IllegalArgumentException("Illegal expression");
					}

					sb.append(checker);
					checker = chars[++i];
				}

				String subCMD = sb.toString();
				if (subCMD.contains(",")) {
					String[] split = subCMD.split(",");
					if (split.length != 2) {
						throw new IllegalArgumentException("Illegal expression.");
					}

					try {
						int index = Integer.parseInt(split[0]);
						int spacing = Integer.parseInt(split[1]);

						if (index < 0 || spacing < 0) {
							throw new IllegalArgumentException("Illegal expression.");
						}

						if (split[1].startsWith("0")) {
							list.add(new NameBuilderGroups(index, spacing, SpacingType.ZEROING));
						} else {
							list.add(new NameBuilderGroups(index, spacing, SpacingType.SPACING));
						}

					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Substitutional command must be a positive whole number.");
					}
				} else {
					int num;
					try {
						num = Integer.parseInt(subCMD);
						if (num < 0) {
							throw new IllegalArgumentException(
									"Substitutional command must be a positive whole number.");
						}

						list.add(new NameBuilderGroups(num, 0, SpacingType.NOTHING));
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Substitutional command must be a positive whole number.");
					}
				}
				sb = new StringBuilder();

			} else {
				sb.append(chars[i]);
			}

			if (i == chars.length - 1) {
				list.add(new NameBuilderClearString(sb.toString()));
			}

		}
		return new NameBuilderFinal(list);

	}

}
