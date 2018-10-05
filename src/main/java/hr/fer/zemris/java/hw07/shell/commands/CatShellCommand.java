package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that represents a cat command. This command, if called with only one
 * arguments which has to be a file, prints out the content of that file to the
 * shell. Second argument can be a charset instance which is then used by a
 * command to form the output for that file.
 * 
 * @author Dinz
 *
 */
public class CatShellCommand implements ShellCommand {
	/**
	 * Method that executes the cat command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path;
		Charset charset = Charset.defaultCharset();

		try {
			String[] argumentsSplited = parsing(arguments);
			if (argumentsSplited.length == 1) {
				path = Paths.get(argumentsSplited[0]);
			} else if (argumentsSplited.length == 2) {
				path = Paths.get(argumentsSplited[0]);
				String charsettest = argumentsSplited[1];
				if (!path.toFile().exists()) {
					throw new IllegalArgumentException("No such file!");
				}
				if (!Charset.availableCharsets().containsKey(charsettest)) {
					throw new IllegalArgumentException("No such charset.");
				}
				charset = Charset.forName(argumentsSplited[1]);
			} else {
				throw new IllegalArgumentException("Wrong number of arguments in \"cat\" command.");
			}
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
		
		if(!path.isAbsolute()) {
			path = env.getCurrentDirectory().resolve(path);
		}

		if (!path.toFile().isFile()) {
			throw new IllegalArgumentException("Input must be a file!");
		}
		
		

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), charset));

			String s;
			while ((s = br.readLine()) != null) {
				env.writeln(s);
			}

			br.close();
		} catch (FileNotFoundException ex) {
			env.writeln(ex.getMessage());
		} catch (ShellIOException e) {
			env.writeln(e.getMessage());
		} catch (IOException e) {
			env.writeln(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "cat";
	}

	@Override
	public List<String> getCommandDescription() {

		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("Command \"cat\" takes one or two arguments. The first argument");
				add("is path to some file and is mandatory. The second argument");
				add("is charset name that shoud be used to interpret chars from bytes.");
				add("If not provided, a default platform charset is used. The command opens");
				add("given file and writes its content to console.");

			}
		};
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
			if (secondIndex == -1 || arguments.substring(secondIndex + 1).contains("\"")) {
				throw new IllegalArgumentException("Wrong use of quotation marks.");
			}
			list.add(arguments.substring(firstIndex, secondIndex).replaceAll("\"", ""));
			String rest = arguments.substring(secondIndex + 1);
			if (!rest.trim().isEmpty()) {
				if (!Character.isWhitespace(rest.charAt(0))) {
					throw new IllegalArgumentException("Wrong arguments input.");
				}
				list.add(rest.trim());
			}
			result = new String[list.size()];
			result = list.toArray(result);
		} else {
			result = arguments.split("\\s+");

		}
		return result;
	}

}
