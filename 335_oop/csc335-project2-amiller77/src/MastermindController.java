
import java.util.LinkedList;

/**
 * 
 * @author Alexander Miller
 * This is the controller, which interacts with the model based
 * on queries from the mastermind class. 
 * This class allows for checking if the guesses are correct,
 * and calculating which values are correct but in the wrong place, 
 * or correct and in the right place.
 */
public class MastermindController {
	// FIELDS
	private MastermindModel model;
	private int rightColorRightPlace;
	private int rightColorWrongPlace;
	private LinkedList remainingColorList;
	private LinkedList wrongGuessList;

	// CONSTRUCTOR
	/**
	 * Constructor for Controller, sets default values
	 * @param paramModel , takes MastermindModel as argument and saves
	 */
	public MastermindController(MastermindModel paramModel) {
		this.model = paramModel;
		this.rightColorRightPlace = 0;
		this.rightColorWrongPlace = 0;
		this.remainingColorList = new LinkedList();
		this.wrongGuessList = new LinkedList();
	}
 
	// IS CORRECT
	/**
	 * Determines if guess is correct, calls helper methods to find right guess
	 * in right place, right guess in wrong place
	 * @param guess
	 * @return boolean, if guess is correct or not
	 */
    public boolean isCorrect(String guess) {
    	guess = guess.toLowerCase();
    	reinitializeGuessData();
    	// iterate over guess and find how many wrong guesses had right letter:
    	findRightColorRightPlace(guess);
    	// iterate over wrong guesses and find how many were in right place
    	findRightColorWrongPlace();
    	if (this.rightColorRightPlace ==4) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    // ***** SUBFUNCTIONS FOR "IS CORRECT" *****
    
    // REINITIALIZE GUESS DATA
    /**
     * resets previous guess data prior to initializing new round
     */
    private void reinitializeGuessData() {
    	this.rightColorRightPlace = 0;
    	this.rightColorWrongPlace = 0;
    	this.remainingColorList.clear();
    	this.wrongGuessList.clear();
    }
    
    // FIND RIGHT COLOR RIGHT PLACE
    /**
     * iterate over guess and find how many wrong guesses had right letter:
     * @param guess
     */
    private void findRightColorRightPlace(String guess) {
    	for (int i = 0; i < guess.length(); i++) {
    		// if the guess is right at position, iterate RCRP:
    		if (guess.charAt(i) == this.model.getColorAt(i)) {
    			this.rightColorRightPlace++;
    		} else { // guess was wrong -> save wrong guess and unguessed color
    			this.remainingColorList.add(this.model.getColorAt(i));
    			this.wrongGuessList.add(guess.charAt(i));
    		}
    	}
    }
    
    // FIND RIGHT COLOR WRONG PLACE
    /**
     * iterate over wrong guesses and find how many are in right place:
     */
	private void findRightColorWrongPlace() {
		while (wrongGuessList.size() > 0) {
			char wrongGuess = (char) wrongGuessList.remove();
			// cross-check with remaining letter list
			for (int i = 0; i < this.remainingColorList.size(); i++) {
				char remainingColor = (char) this.remainingColorList.get(i);
				// if there's a match, iterate RCWP and remove from remaining colors:
				if (remainingColor == wrongGuess) {
					this.rightColorWrongPlace++;
					// nonsense value to avoid duplicate registering and variable list length
					this.remainingColorList.set(i,';');
					break;
				}
			}
		}
	}
	// ****************** //
    
	// ***** METHODS FOR USE BY MASTERMIND *****
	
    // GET RIGHT COLOR RIGHT PLACE
	/**
	 * version that uses the last valid guess
	 * @return int, the number of right colors in right place
	 */
    public int getRightColorRightPlace() { 
    	return this.rightColorRightPlace;
    }
    // using a new guess:
    /**
     * version that takes a new guess
     * @param guess
     * @return int, the number of right colors in right place
     */
    public int getRightColorRightPlace(String guess) {
    	this.isCorrect(guess);
    	return this.rightColorRightPlace;
    }

    // GET RIGHT COLOR WRONG PLACE
    // using last guess:
    /**
     * version that uses the last valid guess
     * @return int, the number of right colors in the wrong place
     */
    public int getRightColorWrongPlace() {
    	return this.rightColorWrongPlace;
    }
    // using a new guess:
    /**
     * version that takes a new guess
     * @param guess
     * @return int, the number of right colors in the wrong place
     */
    public int getRightColorWrongPlace(String guess) {
    	this.isCorrect(guess);
    	return this.rightColorWrongPlace;
    }
	// ****************** //
    
}