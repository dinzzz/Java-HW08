package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw07.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.CptreeShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.ListDShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.MassrenameShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.RmtreeShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.TreeShellCommand;

/**
 * Class that represents one example of a shell. It is a rendition of a command
 * prompt used in most of the operational systems, only has a fewer amount of
 * commands, and with that a limited usage.
 * 
 * @author Dinz
 *
 */
public class MyShell implements Environment {
	/**
	 * Symbol for the prompt of the shell.
	 */
	private static char promptSymbol = '>';
	/**
	 * Symbol for the multiline occurance in the shell.
	 */
	private static char multiLineSymbol = '|';
	/**
	 * Symbol that represents going into the new line in the shell.
	 */
	private static char moreLinesSymbol = '\\';
	/**
	 * Status of the shell.
	 */
	private static ShellStatus status = ShellStatus.CONTINUE;
	/**
	 * Commands of the shell.
	 */
	private static SortedMap<String, ShellCommand> commands;
	/**
	 * Scanner used for shell implementation.
	 */
	Scanner sc = new Scanner(System.in);
	
	Path currentDirectory;
	Map<String, Object> sharedData;
	

	/**
	 * Method that runs the class and acts as a shell.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MyShell ms = new MyShell();
		ms.write("Welcome to MyShell v1.0\n");

		while (status == ShellStatus.CONTINUE) {
			ms.write(Character.toString(promptSymbol) + " ");
			String input = ms.readLine();
			String command = input.split("\\s+", 2)[0];
			String arguments = "";
			if (input.split("\\s+", 2).length > 1) {
				arguments = input.split("\\s+", 2)[1];
			}
			try {
				if (commands.containsKey(command)) {
					status = commands.get(command).executeCommand(ms, arguments);
				} else {
					ms.writeln("There is an unknown command in the input.");
				}
			} catch (IllegalArgumentException ex) {
				ms.writeln(ex.getMessage());
			}
		}

	}

	/**
	 * Constructs a new shell with its commands.
	 */
	public MyShell() {
		commands = new TreeMap<String, ShellCommand>() {
			private static final long serialVersionUID = 1L;

			{
				put("cat", new CatShellCommand());
				put("charsets", new CharsetsShellCommand());
				put("copy", new CopyShellCommand());
				put("help", new HelpShellCommand());
				put("hexdump", new HexdumpShellCommand());
				put("ls", new LsShellCommand());
				put("mkdir", new MkdirShellCommand());
				put("symbol", new SymbolShellCommand());
				put("tree", new TreeShellCommand());
				put("exit", new ExitShellCommand());
				put("cd", new CdShellCommand());
				put("pwd", new PwdShellCommand());
				put("popd", new PopdShellCommand());
				put("pushd", new PushdShellCommand());
				put("listd", new ListDShellCommand());
				put("dropd", new DropdShellCommand());
				put("rmtree", new RmtreeShellCommand());
				put("cptree", new CptreeShellCommand());
				put("massrename", new MassrenameShellCommand());
			}
		};
		
		sharedData = new HashMap<>();
		this.setCurrentDirectory(Paths.get(".").toAbsolutePath().normalize());
	}

	@Override
	public String readLine() throws ShellIOException {
		StringBuilder sb = new StringBuilder();
		while (true) {
			String input = sc.nextLine().trim();
			if (input.endsWith(Character.toString(moreLinesSymbol))) {
				this.write(Character.toString(multiLineSymbol) + " ");
				sb.append(input.substring(0, input.length() - 1));
			} else {
				sb.append(input);
				break;
			}
		}
		return sb.toString();
	}

	@Override
	public void write(String text) throws ShellIOException {
		System.out.print(text);

	}

	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);

	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return commands;
	}

	@Override
	public Character getMultilineSymbol() {
		return multiLineSymbol;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		multiLineSymbol = symbol;
	}

	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		promptSymbol = symbol;

	}

	@Override
	public Character getMorelinesSymbol() {
		return moreLinesSymbol;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		moreLinesSymbol = symbol;

	}

	@Override
	public Path getCurrentDirectory() {
		return this.currentDirectory.toAbsolutePath().normalize();
	}

	@Override
	public void setCurrentDirectory(Path path) {
		if(!path.toFile().exists()) {
			throw new IllegalArgumentException("Non-existing path.");
		}
		this.currentDirectory = path.toAbsolutePath();
		
	}

	@Override
	public Object getSharedData(String key) {
		Objects.requireNonNull(key);
		return sharedData.get(key);
	}

	@Override
	public void setSharedData(String key, Object value) {
		Objects.requireNonNull(key);
		sharedData.put(key, value);
		
	}
	


}
