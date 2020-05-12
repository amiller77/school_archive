import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
/**
 * CRYPTOGRAM CONTROLLER
 * validates user input, file input, etc.
 * Keeps strings of quotes and their various encodings based upon the mappings provided in 
 * the model.
 * Queries model.
 * @author ale
 *
 */
public class CryptogramController {
	
	//INSTANCE VARIABLES
	private CryptogramModel model;
	private String quotation;
	private String encodedQuotation;
	private String guessQuotation;

	/**
	 * CONSTRUCTOR
	 * Gets random line from quotes.txt, creates CryptogramModel, initializes instance variables
	 * 
	 */
	public CryptogramController() {
		// construct model
		this.model = new CryptogramModel();
		// open file, create scanner, generate random line number:
		File file = new File("quotes.txt");
		Integer fileLength = findFileLength(file);
		if (fileLength == null) {
			System.out.println("ERROR: Null length file. Abort.");
			System.exit(1);
		}
		Integer randomLine = (int) Math.floor(Math.random()*fileLength);
		//iterate to random line
		String line = findLine(file,randomLine);
		if (line == null) {
			System.out.println("ERROR: Null line string. Abort.");
			System.exit(1);
		}
		// initialize String variables:
		this.quotation = line.toUpperCase();
		this.encodedQuotation = encrypt(quotation,model.getEncryptionMap());
		this.guessQuotation=encrypt(quotation,model.getGuessMap());
	}
	
	// **** EXTERNAL INTERFACE **** 
	// GETTERS
	/**
	 * CHECK FOR WINNER
	 * sees if the user won the game
	 * @return boolean, if they won or not
	 */
	public boolean checkForWinner() {
		if (this.guessQuotation.equals(this.quotation)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * GET GUESS QUOTATION
	 * returns current guess string to view
	 * @return current guess string to view
	 */
	public String getGuessQuotation() {
		return this.guessQuotation;
	}

	/**
	 * GET ENCODED QUOTATION
	 * returns the encoded quotation [not influenced by guesses] to view
	 * @return the encoded quotation [not influenced by guesses] to view
	 */
	public String getEncodedQuotation() {
		return this.encodedQuotation;
	}
	
	/**
	 * CHECK LETTER
	 * checks if input letter is valid or not
	 * @param letter, letter to verify
	 * @return boolean, if letter was ok
	 */
	public boolean checkLetter(Character letter) {
		Character[] alphabet = CryptogramModel.ALPHABET;
		boolean validator = false;
		for (int i = 0; i < alphabet.length; i++) {
			if (alphabet[i].equals(letter)) {
				validator=true;
			}
		}
		return validator;
	}
	
	// SETTERS
	/**
	 * GUESS
	 * takes a guess from the view, and makes it to the model
	 * @param letterToReplace, letter to replace
	 * @param replacementLetter, letter to replace with
	 */
	public void guess(Character letterToReplace, Character replacementLetter) {
		this.model.guess(letterToReplace, replacementLetter);
		this.guessQuotation = encrypt(this.encodedQuotation,this.model.getGuessMap());
	}
	
	// ***** INTERNAL MECHANISMS *****
	/**
	 * ENCRYPT
	 * takes a string and a map and returns the encoded version
	 * @param writeFrom, string to encode
	 * @param encoder, map that has encoding pairs
	 * @return writeTo, the encoded version of the string
	 */
	private static String encrypt (String writeFrom, Map<Character,Character> encoder) {
		String writeTo = "";
		for (int i = 0; i < writeFrom.length(); i++) {
			Character encodedLetter = encoder.get(writeFrom.charAt(i));
			// if key not in dictionary: just add the original
			if (encodedLetter == null) {
				writeTo+=writeFrom.charAt(i);
			} else {
				writeTo+=encodedLetter;
			}
		}
		return writeTo;
	}
	
	// ***** READ FILE METHODS ******
	/**
	 * FIND FILE LENGTH
	 * returns length of input file
	 * @param file, file to verify
	 * @return Integer length, the length of the input file
	 */
	private static Integer findFileLength(File file) {
		try {
			Scanner scanner = new Scanner(file);
			int iterator = 0;
			while (scanner.hasNext()) {
				scanner.nextLine();
				iterator++;
			}
			scanner.close();
			return iterator;
		} catch (IOException x) {
			System.out.println("File doesn\'t exist. Abort.");
			System.exit(1);
		}
		return null;
	}
	
	/**
	 * FIND LINE
	 * returns the desired line from file
	 * @param file, file to operate on
	 * @param randomLine, line number to find
	 * @return line, the desired line
	 */
	private static String findLine(File file, Integer randomLine) {
		try {
			Scanner scanner = new Scanner(file);
			int iterator = 0;
			while (iterator != randomLine) {
				scanner.nextLine();
				iterator++;
			}
			String line = scanner.nextLine().trim();
			scanner.close();
			return line;
		} catch (IOException f) {
			System.out.println("File doesn\'t exist. Abort.");
			System.exit(1);
		}
		return null;
	}
}
