
import java.util.Scanner;

/**
 * 
 * @author Alexander Miller
 *
 *Description: this program performs a game of mastermind.
 *User will enter guesses of 4 characters from {r,o,y,g,p,b}.
 *i.e. rrrr
 *The player will get to guess 10 times prior to losing.
 *After each guess they will receive feedback on guesses in the
 *right place and correct and guesses that are correct, but in the
 *wrong place.
 *
 *This class represents the view, it should be how users play the game
 */
public class Mastermind {
	/**
	 * Stores valid colors, for validation
	 */
	private static char[] VALID_COLORS = {'r','o','y','g','b','p'};
	
	/**
	 * Handles the main procedural elements of the program, interfaces between
	 * the user and the controller
	 * @param args
	 */
	public static void main(String[] args) {
		// create a new game and keybd scanner:
		Scanner scanner = new Scanner(System.in);
		// Construct the model (whose constructor builds the secret answer)
		MastermindModel model = new MastermindModel();
		// Construct the controller, passing in the model
		MastermindController controller = new MastermindController(model);
		// While the user wants to play:
		while (userWantsToPlay(scanner)) {
			// reset game
			int round = 0;
			boolean victory = false;
			//  While fewer than 10 rounds (first round is 0):
			while (round < 10) {
				round++;
				String guess = gatherGuess(scanner,round);
				// validate length
				try {
					validateLength(guess);
				} catch (MastermindIllegalLengthException l) {
					System.out.println(l.message());
					round--; //subtract, then it adds, keeping net change at 0
					continue;
				}
				// validate color
				try {
					validateColor(guess);
				} catch (MastermindIllegalColorException c) {
					System.out.println(c.message());
					round--;
					continue;
				}
				// Check whether or not the guess is correct
				if (controller.isCorrect(guess)) {
					System.out.println("You win! ");
					victory=true;
					break;
				} else {
					System.out.println("Colors in the correct place: "+controller.getRightColorRightPlace());
					System.out.println("Colors correct but in wrong position: "+controller.getRightColorWrongPlace());
				}
			}
			if (!victory) {
				System.out.println("You lose! ");
				//System.out.println("Correct answer was: ");
				//model.getGuess();
			}
		}
	}
	
	// ***** PROCEDURAL FUNCTIONS *****
	
	/**
	 * USER WANTS TO PLAY
	 * determines if user wants to play
	 * @param scanner
	 * @return boolean, if the player wants to play or not
	 */
	private static boolean userWantsToPlay(Scanner scanner) {
		System.out.println("Welcome to Mastermind!");
		System.out.println("Would you like to play? ");
		String keepPlaying = scanner.next().toLowerCase();
		if (keepPlaying.equals("y")||keepPlaying.equals("yes")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * GATHER GUESS
	 * collect guess from user
	 * @param scanner
	 * @param round
	 * @return guess, after the user gives it
	 */
	private static String gatherGuess(Scanner scanner, int round) {
		// skip line, then gather:
		System.out.println("");
		System.out.println("Enter guess number "+round+": ");
		String guess = scanner.next();
		return guess;
	}
	
	/**
	 * Validates Length, throws exception if != 4
	 * @param guess
	 * @throws MastermindIllegalLengthException
	 */
	public static void validateLength(String guess) throws MastermindIllegalLengthException {
		if (guess.length() != 4) {
			throw new MastermindIllegalLengthException(guess.length());
		}
	}
	

	/**
	 * Validates color, throws exception if not in VALID_COLORS
	 * @param guess
	 * @throws MastermindIllegalColorException
	 */
	public static void validateColor(String guess) throws MastermindIllegalColorException {
		for (int i = 0; i < 4; i++) {
			boolean matchFound = false;
			for (int j = 0; j<6; j++) {
				if (guess.charAt(i) == VALID_COLORS[j]) {
					matchFound=true;
				}
			}
			if (matchFound==false) {
				throw new MastermindIllegalColorException(guess.charAt(i));
			}
		}
	}
	
}
