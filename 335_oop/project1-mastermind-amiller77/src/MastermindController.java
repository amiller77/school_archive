/**
 * 
 * @author Alexander Miller
 * This is the controller, which interacts with the model based
 * on queries from the mastermind class. 
 * This class allows for checking if the guesses are correct,
 * and calculating which values are correct but in the wrong place, 
 * or correct and in the right place.
 */
import java.util.LinkedList;

public class MastermindController {
	// FIELDS
	private MastermindModel model;
	private int rightColorRightPlace;
	private int rightColorWrongPlace;
	private LinkedList remainingColorList;
	private LinkedList wrongGuessList;

	// CONSTRUCTOR
	public MastermindController(MastermindModel ParamModel) {
		this.model = ParamModel;
		this.rightColorRightPlace = 0;
		this.rightColorWrongPlace = 0;
		this.remainingColorList = new LinkedList();
		this.wrongGuessList = new LinkedList();
	}
 
	// IS CORRECT
	// takes guess and checks it with the model
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
    public void reinitializeGuessData() {
    	this.rightColorRightPlace = 0;
    	this.rightColorWrongPlace = 0;
    	this.remainingColorList.clear();
    	this.wrongGuessList.clear();
    }
    
    // FIND RIGHT COLOR RIGHT PLACE
    // iterate over guess and find how many wrong guesses had right letter:
    public void findRightColorRightPlace(String guess) {
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
	// iterate over wrong guesses and find how many are in right place:
	public void findRightColorWrongPlace() {
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
    // using last guess:
    public int getRightColorRightPlace() { 
    	return this.rightColorRightPlace;
    }
    // using a new guess:
    public int getRightColorRightPlace(String guess) {
    	this.isCorrect(guess);
    	return this.rightColorRightPlace;
    }

    // GET RIGHT COLOR WRONG PLACE
    // using last guess:
    public int getRightColorWrongPlace() {
    	return this.rightColorWrongPlace;
    }
    // using a new guess:
    public int getRightColorWrongPlace(String guess) {
    	this.isCorrect(guess);
    	return this.rightColorWrongPlace;
    }
	// ****************** //
    
}
