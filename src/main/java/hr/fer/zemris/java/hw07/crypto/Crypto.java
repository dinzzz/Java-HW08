package hr.fer.zemris.java.hw07.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class that allows the user to toy with cryptation of his file. The user can
 * do the sha-256 digest for his file by inputing the appropriate command and
 * giving the program one file. Also user can encrypt the file with the
 * specified key and IV parameter. In that case the program produces
 * encrypted/decrypted file and stores it into a file which program expects to
 * be the second argument.
 * 
 * @author Dinz
 *
 */
public class Crypto {
	/**
	 * Main method that runs the class.
	 * 
	 * @param args
	 *            Arguments from the command line.
	 */
	public static void main(String[] args) {
		if (args.length != 2 && args.length != 3) {
			System.out.println("Invalid number of arguments.");
			return;
		}

		Path p = Paths.get(args[1]);

		if (args[0].equals("checksha")) {
			String fileDigest = checkSha(p.toFile());
			System.out.println("Please provide expected sha-256 digest for " + args[1] + ":\n>");

			Scanner sc = new Scanner(System.in);
			String inputDigest = sc.nextLine();
			sc.close();

			if (fileDigest.equals(inputDigest)) {
				System.out.println("Digesting completed. Digest of " + args[1] + " matches expected digest.");
			} else {
				System.out.println("Digesting completed. Digest of " + args[1]
						+ " does not match the expected digest. Digest was: " + fileDigest);
			}

		} else if (args[0].equals("encrypt")) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n>");
			String keyText = sc.nextLine();
			System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):\n>");
			String ivText = sc.nextLine();
			sc.close();

			Path d = Paths.get(args[2]);

			crypt(p.toFile(), keyText, ivText, d.toFile(), Cipher.ENCRYPT_MODE);

		} else if (args[0].equals("decrypt")) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n>");
			String keyText = sc.nextLine();
			System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):\n>");
			String ivText = sc.nextLine();
			sc.close();

			Path d = Paths.get(args[2]);

			crypt(p.toFile(), keyText, ivText, d.toFile(), Cipher.DECRYPT_MODE);

		} else {
			System.out.println("Invalid command.");
			return;
		}
	}

	/**
	 * Method that checks the sha-256 digest of the file.
	 * 
	 * @param file
	 *            File to be checked.
	 * @return SHA-256 digest for the given file.
	 */
	private static String checkSha(File file) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			InputStream inputStream = new FileInputStream(file);
			byte[] input = inputStream.readAllBytes();
			inputStream.close();

			messageDigest.update(input);
			byte[] digested = messageDigest.digest();

			StringBuffer stringBuffer = new StringBuffer();

			for (byte bytes : digested) {
				stringBuffer.append(String.format("%02x", bytes & 0xff)); // util.bytetohex ??
			}

			return stringBuffer.toString();

		} catch (NoSuchAlgorithmException e) {
			System.out.println("Wrong algorithm.");

		} catch (FileNotFoundException e) {
			System.out.println("No such file.");

		} catch (IOException e) {
			System.out.println("Can't read bytes from the stream.");
		}
		return null; // Unreachable
	}

	/**
	 * Method that encrypts or decrpyts the given file and stores it to the desired
	 * destination.
	 * 
	 * @param file
	 *            File to be crypted.
	 * @param keyText
	 *            Key for crypting.
	 * @param ivText
	 *            IV parameter for crypting.
	 * @param destination
	 *            Destination of the produced file.
	 * @param cipherMode
	 *            Mode of crypting - Encryption or Decryption.
	 */
	private static void crypt(File file, String keyText, String ivText, File destination, int cipherMode) {
		try {
			InputStream inputStream = new FileInputStream(file);
			OutputStream outputStream = new FileOutputStream(destination);

			SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(cipherMode, keySpec, paramSpec);

			byte[] buffer = new byte[16];

			while (true) {
				int r = inputStream.read(buffer);
				if (r < 1)
					break;
				outputStream.write(cipher.update(buffer, 0, r));
			
			}

			outputStream.write(cipher.doFinal());
			inputStream.close(); // finally
			outputStream.close();

		} catch (FileNotFoundException e) {
			System.out.println("No such file.");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm.");
		} catch (NoSuchPaddingException e) {
			System.out.println("No such padding.");
		} catch (InvalidKeyException e) {
			System.out.println("Invalid key.");
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Invalid algorithm.");
		} catch (IOException e) {
			System.out.println("IO Exception.");
		} catch (IllegalBlockSizeException e) {
			System.out.println("Illegal block size.");
		} catch (BadPaddingException e) {
			System.out.println("Invalid padding.");
		}
	}

}
