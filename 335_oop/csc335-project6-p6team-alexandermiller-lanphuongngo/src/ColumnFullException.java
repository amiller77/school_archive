/**
 * @author Lan Ngo
 * ColumnFullException is used to identify user inputs that are
 * not integers.
 *
 */
public class ColumnFullException extends Exception{
	/**
	 * Initializes the exception and stores a custom message
	 * @param message A string that contains a message that is displayed when the error is printed
	 */
	public ColumnFullException(String message) {
		super(message);
	}
	
	/**
	 * When called, returns a string describing the error.
	 * @return A string describing the error.
	 */
	public String toString() {
		return "Column " + getMessage() + " is already filled.";
	}
}
