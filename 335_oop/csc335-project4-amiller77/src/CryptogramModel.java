import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * CRYPTOGRAM MODEL
 * contains the encryption mappings for use by Cryptogram Controller to query
 * @author ale
 *
 */
public class CryptogramModel {
	// CLASS VARIABLES
	public static final Character[] ALPHABET = {'A','B','C','D','E','F','G',
			'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	
	// INSTANCE VARIABLES
	private Map<Character,Character> encryptionMap;
	private Map<Character,Character> guessMap;

	/**
	 * CONSTRUCTOR
	 * initializes the encryption mappings
	 * fills any guess values with empty spaces as placeholders
	 */
	public CryptogramModel() {
		// instantiate instance variables:
		this.encryptionMap = new HashMap<Character,Character>();
		this.guessMap = new HashMap<Character,Character>();
		// create a randomized key array:
		List<Character> keys = new LinkedList<Character>();
		for (int i = 0; i<ALPHABET.length;i++) {
			keys.add(ALPHABET[i]);
		}
		Collections.shuffle(keys);
		// create encryption Map and guess Map
		// guess map vals default to space character as placeholder
		for (int i = 0; i < ALPHABET.length; i++) {
			encryptionMap.put(ALPHABET[i], keys.get(i));
			guessMap.put(ALPHABET[i],' ');
		}
	}
	
	// **** EXTERNAL INTERFACE ***** 
	// SETTERS
	/**
	 * GUESS 
	 * passes a guess along from the view to the model
	 * @param letterToReplace , letter to replace
	 * @param replacementLetter , letter to replace with
	 */
	public void guess(Character letterToReplace,Character replacementLetter) {
		System.out.println("Switching "+letterToReplace+" with "+replacementLetter);
		this.guessMap.put(letterToReplace, replacementLetter);
	}
	
	// GETTERS
	/**
	 * GET ENCRYPTION MAP
	 * @return encryption map, the encryption map
	 */
	public Map<Character,Character> getEncryptionMap(){
		return this.encryptionMap;
	}

	/**
	 * GET GUESS MAP
	 * @return guess map, the guess map
	 */
	public Map<Character,Character> getGuessMap() {
		return this.guessMap;
	}
	
	
}

