package hr.fer.zemris.java.hw07.shell;

/**
 * Represents an exception that happens in a shell while reading or writing.
 * 
 * @author Dinz
 *
 */
public class ShellIOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new ShellIOException.
	 */
	public ShellIOException() {
		super();
	}

	/**
	 * Constructs a new ShellIOException with the proper message.
	 * 
	 * @param message
	 *            Message of the exception.
	 */
	public ShellIOException(String message) {
		super(message);
	}

}
