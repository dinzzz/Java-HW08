package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.nameparsing.NameBuilder;
import hr.fer.zemris.java.hw07.shell.nameparsing.NameBuilderInfoImpl;
import hr.fer.zemris.java.hw07.shell.nameparsing.NameBuilderParser;

/**
 * Class that represents massrename command. This command takes two directories,
 * command type, mask and expression as arguments. Then after filtering the
 * files of the source directory through the mask and the expression, it
 * executes the file renaming then moving to the destination directory.
 * 
 * @author Dinz
 *
 */
public class MassrenameShellCommand implements ShellCommand {
	/**
	 * Method that executes the massrename command.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] result = parsing(arguments);
		if (result.length != 4 && result.length != 5) {
			throw new IllegalArgumentException("Wrong arguments in the \"massrename\" command");
		}

		Path dir1 = Paths.get(result[0]);
		Path dir2 = Paths.get(result[1]);

		if (!dir1.isAbsolute()) {
			dir1 = env.getCurrentDirectory().resolve(dir1);
		}

		if (!dir2.isAbsolute()) {
			dir2 = env.getCurrentDirectory().resolve(dir2);
		}

		if (!dir1.toFile().isDirectory() || !dir2.toFile().isDirectory()) {
			throw new IllegalArgumentException("First two arguments must be directories.");
		}

		String cmd = result[2];
		String mask = result[3];

		if (cmd.equals("filter")) {
			if (result.length == 5) {
				throw new IllegalArgumentException("Filter does not accept five arguments.");
			}
			filter(env, dir1, mask);
		} else if (cmd.equals("groups")) {
			if (result.length == 5) {
				throw new IllegalArgumentException("Groups does not accept five arguments.");
			}
			group(env, dir1, mask);
		} else if (cmd.equals("show")) {
			if (result.length == 4) {
				throw new IllegalArgumentException("Show expects 5 arguments.");
			}
			String izraz = result[4];
			show(env, dir1, mask, izraz);
		} else if (cmd.equals("execute")) {
			if (result.length == 4) {
				throw new IllegalArgumentException("Execute expects 5 arguments.");
			}
			String izraz = result[4];
			try {
				execute(env, dir1, dir2, mask, izraz);
			} catch (IOException e) {
				throw new IllegalArgumentException("Can't move the files.");
			}
		} else {
			throw new IllegalArgumentException("Wrong cmd argument.");
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "massrename";
	}

	@Override
	public List<String> getCommandDescription() {
		return new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("The command \"massrename\" takes all the files matching");
				add("the pattern given as an argument, renames them and moves");
				add("them from the source directory to appropriate destination.");
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
	public String[] parsing(String arguments) {
		arguments = arguments.trim();
		List<String> lista = new ArrayList<>();

		if (arguments.contains("\"")) {
			String[] split = arguments.split("\"");

			for (int i = 0; i < split.length; i++) {
				if (i % 2 == 1) {
					lista.add(split[i]);
				} else {
					String[] newSplit = split[i].trim().split("\\s+");
					for (String newString : newSplit) {
						if (!newString.isEmpty()) {
							lista.add(newString);
						}
					}
				}
			}

			String[] result = new String[lista.size()];
			result = lista.toArray(result);
			return result;
		}

		else {
			return arguments.split("\\s+");
		}
	}

	/**
	 * Method that is run upon the wanted files if the "filter" command type is
	 * called. Filters the files based on the given mask.
	 * 
	 * @param env
	 *            Shell instance.
	 * @param dir1
	 *            Directory where the files are contained.
	 * @param mask
	 *            Mask
	 */
	private void filter(Environment env, Path dir1, String mask) {

		Pattern p = Pattern.compile(mask);

		for (File f : dir1.toFile().listFiles()) {
			if (f.isFile()) {
				Matcher m = p.matcher(f.getName());
				if (m.matches()) {
					env.writeln(f.getName());
				}
			}
		}

	}

	/**
	 * Method that is run upon the wanted files if the "group" command type is
	 * called. It groups the elements according to the mask and writes them to the
	 * console.
	 * 
	 * @param env
	 *            Shell instance.
	 * @param dir1
	 *            Directory where the files are contained.
	 * @param mask
	 *            Mask
	 */
	private void group(Environment env, Path dir1, String mask) {
		Pattern p = Pattern.compile(mask);

		for (File f : dir1.toFile().listFiles()) {
			if (f.isFile()) {
				Matcher m = p.matcher(f.getName());
				int groupCount = m.groupCount();
				while (m.find()) {
					StringBuilder sb = new StringBuilder();
					sb.append(f.getName());
					for (int i = 0; i <= groupCount; i++) {
						sb.append(" " + i + ": " + m.group(i));
					}
					env.writeln(sb.toString());
				}
			}
		}
	}

	/**
	 * Method that prints the preview how the files would be renamed if the execute
	 * method is called after filtering it through the given mask and expression.
	 * 
	 * @param env
	 *            Shell instance.
	 * @param dir1
	 *            Directory where the files are contained.
	 * @param mask
	 *            Mask
	 * @param izraz
	 *            Expression
	 */
	private void show(Environment env, Path dir1, String mask, String izraz) {
		Pattern p = Pattern.compile(mask);
		NameBuilderParser parser = new NameBuilderParser(izraz);
		NameBuilder builder = parser.getNameBuilder();

		for (File f : dir1.toFile().listFiles()) {
			Matcher m = p.matcher(f.getName());
			if (m.matches()) {
				NameBuilderInfoImpl info = new NameBuilderInfoImpl(m);
				builder.execute(info);
				String newName = info.getStringBuilder().toString();
				env.writeln(newName);
			}
		}

	}

	/**
	 * Method that executes the renaming and moving of the files after being
	 * filtered through the given mask and expression.
	 * 
	 * @param env
	 *            Shell instance.
	 * @param dir1
	 *            Directory where the files are contained.
	 * @param mask
	 *            Mask
	 * @param izraz
	 *            Expression
	 */
	private void execute(Environment env, Path dir1, Path dir2, String mask, String izraz) throws IOException {
		Pattern p = Pattern.compile(mask);
		NameBuilderParser parser = new NameBuilderParser(izraz);
		NameBuilder builder = parser.getNameBuilder();

		for (File f : dir1.toFile().listFiles()) {
			Matcher m = p.matcher(f.getName());
			if (m.matches()) {
				NameBuilderInfoImpl info = new NameBuilderInfoImpl(m);
				builder.execute(info);
				String newName = info.getStringBuilder().toString();
				if (dir1.equals(dir2)) {
					f.renameTo(Paths.get(dir2 + "/" + newName).toFile());
				} else {
					Files.move(f.toPath(), Paths.get(dir2 + "/" + newName));
				}

			}
		}
	}

}
