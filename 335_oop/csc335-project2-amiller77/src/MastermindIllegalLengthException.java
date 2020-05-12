
/**
 * 
 * @author ale
 * Creates a new exception class for when an invalid length is guessed
 */
public class MastermindIllegalLengthException extends Exception{

	/**
	 * string to print as error
	 */
	private String message;
	
	/**
	 * constructor for the exception
	 * @param guessLength , the length that was wrong
	 */
	public MastermindIllegalLengthException (int guessLength) {
		super("The guess should be of length four. Your guess was of length "+guessLength+". Please try again. ");
		this.message ="The guess should be of length four. Your guess was of length "+guessLength+". Please try again. ";
	}
	
	/**
	 * allows user to get error message w/o throwing the error
	 * @return string to print if you just want the error message
	 */
	public String message() {
		return this.message;
	}
}
