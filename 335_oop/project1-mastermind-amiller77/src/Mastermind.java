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
 */
import java.util.Scanner;

//This class represents the view, it should be how users play the game
public class Mastermind {
	// FIELDS
	private int round;
	private boolean victory;
	
	// MAIN
	public static void main(String[] args) {
		// create a new game and keybd scanner:
		Mastermind game = new Mastermind();
		Scanner scanner = new Scanner(System.in);
		// Construct the model (whose constructor builds the secret answer)
		MastermindModel model = new MastermindModel();
		// Construct the controller, passing in the model
		MastermindController controller = new MastermindController(model);
		// While the user wants to play:
		while (userWantsToPlay(scanner)) {
			// reinitialize round to 0
			game.resetGame();
			//  While fewer than 10 rounds (first round is 0):
			while (game.getRound() < 10) {
				game.addRound(); 
				String guess = gatherGuess(scanner,game);
				// validate guess
				if (!validateGuess(guess)) {
					game.addRound();
					continue;
				}
				// Check whether or not the guess is correct
				if (controller.isCorrect(guess)) {
					System.out.println("You win! ");
					game.setVictory();
					break;
				} else {
					System.out.println("Colors in the correct place: "+controller.getRightColorRightPlace());
					System.out.println("Colors correct but in wrong position: "+controller.getRightColorWrongPlace());
				}
			}
			if (!game.getVictory()) {
				System.out.println("You lose! ");
				System.out.println("Correct answer was: ");
				model.getGuess();
			}
		}
	}
	
	// ***** PROCEDURAL FUNCTIONS *****
	
	// USER WANTS TO PLAY
	// determines if user wants to play
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
	
	// GATHER GUESS
	// collect guess from user
	private static String gatherGuess(Scanner scanner, Mastermind game) {
		// skip line, then gather:
		System.out.println("");
		System.out.println("Enter guess number "+game.getRound()+": ");
		String guess = scanner.next();
		return guess;
	}
	
	// VALIDATE GUESS
	private static boolean validateGuess(String guess) {
		if (guess.length() != 4) {
			System.out.println("Invalid guess length. ");
			return false;
		}
		return true;
	}
	
	// ***** SETTERS *****
	
	// ADD ROUND
	public void addRound() {
		this.round++;
	}
	
	//GAME WON
	public void setVictory() {
		this.victory = true;
	}
	
	// RESET GAME
	public void resetGame() {
		this.round = 0;
		this.victory = false;
	}
	
	// ***** GETTERS *****
	
	// GET ROUND
	public int getRound() {
		return this.round;
	}
	
	// GET VICTORY
	public boolean getVictory() {
		return this.victory;
	}
	
}
