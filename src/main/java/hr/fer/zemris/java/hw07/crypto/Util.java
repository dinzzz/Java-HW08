package hr.fer.zemris.java.hw07.crypto;

/**
 * Class that represents utilisation with byte elements. Includes operations and
 * transformations with byte arrays and byte elements.
 * 
 * @author Dinz
 *
 */
public class Util {
	/**
	 * Transforms a given HEX value to the byte array.
	 * 
	 * @param keyText
	 *            Hex value to be transformed.
	 * @return Byte array.
	 * @throws IllegalArgumentException
	 *             If the given HEX is in invalid format or contains illegal
	 *             characters.
	 */
	public static byte[] hextobyte(String keyText) {
		if (keyText.length() % 2 == 1) {
			throw new IllegalArgumentException("Invalid string format --> invalid size");
		}

		byte[] byteArray = new byte[keyText.length() / 2];
		char[] charArray = keyText.toCharArray();

		for (int i = 0; i < charArray.length;) {
			char c1 = charArray[i++];
			char c2 = charArray[i];

			if (checkChar(c1) && checkChar(c2)) {
				int value = Integer.parseInt(new StringBuilder().append(c1).append(c2).toString(), 16);
				byteArray[i / 2] = (byte) value;
			} else {
				throw new IllegalArgumentException("Invalid string format --> invalid character");
			}
			i++;
		}

		return byteArray;
	}

	/**
	 * Transforms a given byte array to the HEX value in string format.
	 * 
	 * @param byteArray
	 *            Byte array to be transformed.
	 * @return Hex value in string format.
	 */
	public static String bytetohex(byte[] byteArray) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			sb.append(String.format("%02x", 0xFF & (int) byteArray[i]));
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * Method that checks if the character is valid to use in an operation.
	 * 
	 * @param c
	 *            Character to be checked.
	 * @return True if the character is valid, false otherwise.
	 */
	private static boolean checkChar(char c) {
		if (Character.isDigit(c)) {
			return true;
		} else if (Character.isLetter(c)) {
			if (Character.toUpperCase(c) >= 'A' && Character.toUpperCase(c) <= 'F') {
				return true;
			}
		}

		return false;
	}
}
