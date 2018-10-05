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
import java.util.Scanner;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents the copy command. This command takes two arguments -
 * First argument has to be a file which will be copied. Second argument can be
 * a file or a directory. If the arguments is a file, after an interaction with
 * the user about the permission, the file is overwritten by the first one. If
 * the arguments is a directory, then the file is copied to that particular
 * directory.
 * 
 * @author Dinz
 *
 */
public class CopyShellCommand implements ShellCommand {
	/**
	 * Method that executes the copy command.
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
		if(!source.isAbsolute()) {
			source = env.getCurrentDirectory().resolve(source);
		}
		
		if(!dest.isAbsolute()) {
			dest = env.getCurrentDirectory().resolve(dest);
		}
		if (!source.toFile().isFile()) {
			throw new IllegalArgumentException("First argument must be a file.");
		}

		if (dest.toFile().isDirectory()) {
			File newFile = new File(dest.toFile().getAbsolutePath() + "/" + source.getFileName());
			try {
				copy(source.toFile(), newFile);
			} catch (IOException e) {
				env.writeln(e.getMessage());
			}
		} else {
			try {
				if (dest.toFile().exists()) {
					env.writeln("Do you wish to overwrite file: " + dest.getFileName()
							+ "? Type Y for confirmation, N if you want to abort.");
					@SuppressWarnings("resource")
					Scanner sc = new Scanner(System.in);
					String line = sc.nextLine();
					if (line.equals("Y")) {
						copy(source.toFile(), dest.toFile());
					} else if (line.equals("N")) {
						env.writeln("Aborting.");
					} else {
						env.writeln("Uknown answer, aborting.");
					}
				} else {
					copy(source.toFile(), dest.toFile());
				}

			} catch (IOException e) {
				env.writeln(e.getMessage());
			}
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "copy";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"copy\" expects two arguments: source file and");
				add("destination file name. If destination file exists, the command");
				add("asks user is it allowed to overwrite it. First arguments must be");
				add("a file. If the second argument is directory, the command copies the");
				add("original file into that directory using it's original name.");
			}
		};
	}

	/**
	 * Method that copies the content of the file from the source to the
	 * destination.
	 * 
	 * @param source
	 *            File that is being copied.
	 * @param dest
	 *            Destination where the file should be pasted.
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
