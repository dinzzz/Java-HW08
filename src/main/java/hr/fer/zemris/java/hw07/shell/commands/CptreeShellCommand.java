package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a cptree command. The command takes the source and
 * destination directories as an arguments and performs recursive copying of the
 * content in source directory to the destination.
 * 
 * @author Dinz
 *
 */
public class CptreeShellCommand implements ShellCommand {
	/**
	 * Method that executes the cptree command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path source;
		Path dest;
		try {
			String[] argumentsSplited = parsing(arguments);
			if (argumentsSplited.length != 2) {
				throw new IllegalArgumentException("Wrong argument input.");
			}

			source = Paths.get(argumentsSplited[0]);
			dest = Paths.get(argumentsSplited[1]);

		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
		if (!source.isAbsolute()) {
			source = env.getCurrentDirectory().resolve(source);
		}

		if (!dest.isAbsolute()) {
			dest = env.getCurrentDirectory().resolve(dest);
		}

		try {
			copyRecursively(source.toFile(), dest.toFile());
		} catch (IOException e) {
			env.writeln(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "cptree";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"cptree\" recursively copies the directory given");
				add("as an argument and all of its content inside to another given");
				add("directory.");
			}
		};
	}

	/**
	 * Method that recursively copies the content of the directory to the
	 * destination.
	 * 
	 * @param src
	 *            Source directory that is being copied.
	 * @param dest
	 *            Destination.
	 * @throws IOException
	 */
	public void copyRecursively(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}

			String destFileString = dest.getAbsolutePath() + "/" + src.getName();
			File destFile = new File(destFileString);
			destFile.mkdir();
			dest = destFile;

			for (String file : src.list()) {
				File srcdir = new File(src, file);
				File destdir = new File(dest, file);
				copyRecursively(srcdir, destdir);
			}

		} else {
			copy(src, dest);
		}
	}

	/**
	 * Method that copies the file to another location.
	 * 
	 * @param source
	 *            File to be copied.
	 * @param dest
	 *            Destination.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void copy(File source, File dest) throws IOException, FileNotFoundException {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		inputStream = new FileInputStream(source);
		outputStream = new FileOutputStream(dest);
		byte[] buffer = new byte[4096];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);

		}
		inputStream.close();
		outputStream.close();

	}

	/**
	 * Method that is used to parse the arguments for the command.
	 * 
	 * @param arguments
	 *            String line that the shell gives to the command.
	 * @return Proper format of the arguments.
	 * @throws IllegalArgumentException
	 *             If the arguments are invalid.
	 */
	private static String[] parsing(String arguments) {
		String[] result = null;
		List<String> list = new ArrayList<>();
		if (arguments.contains("\"")) {
			int firstIndex = arguments.indexOf("\"");
			int secondIndex = arguments.indexOf('\"', arguments.indexOf('\"') + 1);
			if (secondIndex == -1) {
				throw new IllegalArgumentException("Wrong use of quotation marks.");
			}

			if (firstIndex == 0) {
				list.add(arguments.substring(firstIndex, secondIndex).replaceAll("\"", ""));
				String secondPart = arguments.substring(secondIndex + 1);
				if (!Character.isWhitespace(secondPart.charAt(0))) {
					throw new IllegalArgumentException("Wrong argument input.");
				}
				if (secondPart.contains("\"")) {
					int firstIndexQ = secondPart.indexOf("\"");
					int secondIndexQ = secondPart.indexOf('\"', secondPart.indexOf('\"') + 1);
					if (firstIndexQ == -1 || secondIndexQ == -1
							|| !secondPart.substring(secondIndexQ + 1).trim().isEmpty()) {
						throw new IllegalArgumentException("Wrong use of quotation marks.");
					}
					list.add(secondPart.trim().replaceAll("\"", ""));
				} else {
					list.add(secondPart.trim());
				}

			} else {
				if (!Character.isWhitespace(arguments.charAt(firstIndex - 1))) {
					throw new IllegalArgumentException("Wrong argument input.");
				}
				if (!arguments.substring(secondIndex + 1).trim().isEmpty()) {
					throw new IllegalArgumentException("Wrong use of quotation marks.");
				}
				list.add(arguments.substring(0, firstIndex - 1).trim());
				list.add(arguments.substring(firstIndex, secondIndex).replaceAll("\"", ""));
			}
			result = new String[list.size()];
			result = list.toArray(result);
		} else {
			result = arguments.split("\\s+");

		}
		return result;
	}

}
