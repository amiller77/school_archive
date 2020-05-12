

/**
 * 
 * @author Alexander Miller
 * This is the model for mastermind, which auto-generates a 
 * randomized color series, allows for a user-set series for 
 * debugging, and allows the query of one of the letters, as 
 * well as the entire answer, so the user can see what they 
 * were trying to guess once they lose.
 */
public class MastermindModel {
	// VARIABLES
	private char[] charArray;

	/**
	 * Constructor
	 * selects color based on number range, pseudo-random generated
	 */
    public MastermindModel() { 
    	this.charArray = new char[4];
    	for (int i = 0; i < 4; i++) {
        	double randomVal = Math.random();
        	if (randomVal < (1.0/6)) {
        		this.charArray[i] = 'r';
        	} else if (randomVal < (1.0/3)) {
        		this.charArray[i] = 'o';
        	} else if (randomVal < .5) {
        		this.charArray[i] = 'y';
        	} else if (randomVal < (2.0/3)) {
        		this.charArray[i] = 'g';
        	} else if (randomVal < (5.0/6)) {
        		this.charArray[i] = 'b';
        	} else {
        		this.charArray[i] = 'p';
        	}
    	}
    }
    
    // TEST CONSTRUCTOR
    // fills data from input, rather than randomly
    /**
     * constructor used for testing; allows preset answer
     * @param answer , takes an answer as input
     */
    public MastermindModel(String answer) {
    	// convert to lower case
    	answer = answer.toLowerCase();
    	// instantiate and fill array
    	this.charArray = new char[4];
    	for (int i = 0; i < 4; i++) {
    		this.charArray[i] = answer.charAt(i);
    	}
    }

    // GET COLOR AT
    /**
     * Gets the color at a certain index
     * @param index
     * @return char, the color at the index
     */
    public char getColorAt(int index) {
    	return this.charArray[index];
    }
    
    /*
    // GET GUESS (ANSWER REVEAL AT END)
    public void getGuess() {
    	for (int i = 0; i<4; i++) {
    		System.out.print(this.charArray[i]);
    	}
    	System.out.println("");
    }
    */

}