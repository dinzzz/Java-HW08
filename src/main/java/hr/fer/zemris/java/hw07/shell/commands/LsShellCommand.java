package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a ls command. This command takes only one directory
 * from the shell input and prints out its whole content with its atributes.
 * Atributes are date and time of creation, is the file readable, writeable,
 * executable, is the file a directory and the size of the file.
 * 
 * @author Dinz
 *
 */
public class LsShellCommand implements ShellCommand {
	/**
	 * Method that executes the ls command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path;

		try {
			String argumentsParsed = parsing(arguments);

			path = Paths.get(argumentsParsed);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
		if(!path.isAbsolute()) {
			path = env.getCurrentDirectory().resolve(path);
		}
		
		if (!path.toFile().isDirectory()) {
			throw new IllegalArgumentException("Argument in \"ls\" command must be a directory.");
		}

		for (File child : path.toFile().listFiles()) {
			try {
				formatAndPrint(child);
			} catch (IOException e) {
				env.writeln(e.getMessage());
			}
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "ls";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"ls\" takes a single argument which has");
				add("to be a directory and writes a directory listing.");
			}
		};
	}

	/**
	 * Method that formats and prints the appropriate output for the ls command.
	 * 
	 * @param file
	 *            File to be printed.
	 * @throws IOException
	 */
	private void formatAndPrint(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		if (file.isDirectory()) {
			sb.append("d");
		} else {
			sb.append("-");
		}

		if (file.canRead()) {
			sb.append("r");
		} else {
			sb.append("-");
		}

		if (file.canWrite()) {
			sb.append("w");
		} else {
			sb.append("-");
		}

		if (file.canExecute()) {
			sb.append("x");
		} else {
			sb.append("-");
		}

		sb.append(String.format("%10d", file.length()));
		sb.append(" " + dateAndTime(file) + " ");
		sb.append(file.getName());

		System.out.println(sb.toString());
	}

	/**
	 * Method that determines the date and time of creation of the given file and
	 * returns it in a string format.
	 * 
	 * @param file
	 *            File whose date and time of creation we have to determine.
	 * @return Date and time of file creation if the string format.
	 * @throws IOException
	 */
	private static String dateAndTime(File file) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Path path = file.toPath();
		BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes = faView.readAttributes();
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

		return formattedDateTime;
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
	private static String parsing(String arguments) {
		if (arguments.contains("\"")) {
			int firstIndex = arguments.indexOf("\"");
			int secondIndex = arguments.indexOf('\"', arguments.indexOf('\"') + 1);
			if (firstIndex != 0 || secondIndex == -1 || !arguments.substring(secondIndex + 1).trim().isEmpty()) {
				throw new IllegalArgumentException("Wrong use of quotation marks.");
			}
			return arguments.replaceAll("\"", "");
		} else {
			if (arguments.split("\\s+").length != 1) {
				throw new IllegalArgumentException("Wrong number of arguments in the \"hexdump\" command.");
			}
			return arguments.trim();
		}
	}

}
