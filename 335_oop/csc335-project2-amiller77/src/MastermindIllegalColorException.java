/**
 * 
 * @author ale
 * Creates a new exception class for when an invalid color is guessed
 */
public class MastermindIllegalColorException extends Exception{

	/**
	 * string to print as error
	 */
	private String message;
	
	/**
	 * constructor for the exception
	 * @param illegalColor , the color that was wrong
	 */
	public MastermindIllegalColorException (char illegalColor) {
		super("Valid colors include R, O, Y, G, B, P. You selected "+illegalColor+". Please try again. ");
		this.message="Valid colors include R, O, Y, G, B, P. You selected "+illegalColor+". Please try again. ";
	}
	
	/**
	 * allows user to get error message w/o throwing the error
	 * @return string to print if you just want the error message
	 */
	public String message() {
		return this.message;
	}
}

