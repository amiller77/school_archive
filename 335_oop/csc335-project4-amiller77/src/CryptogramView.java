import java.util.Scanner;

/**
 * CRYPTOGRAM VIEW
 * handles the main control flow of the program, processes user requests, queries the
 * CryptogramController
 */
public class CryptogramView {
	// CLASS VARIABLES
	public static CryptogramController controller = new CryptogramController();
	
	/**
	 * MAIN
	 * main control flow of the program, loops until user wins, gathers input
	 * and feeds it to the controller
	 * @param args
	 */
	public static void main(String[] args) {
		// initial setup:
		Scanner input = new Scanner(System.in);
		printGreeting();
		// feedback loop:
		while (!controller.checkForWinner()){
			System.out.println(controller.getGuessQuotation());
			System.out.println(controller.getEncodedQuotation());
			Character letterToReplace = parseInput(input,"Enter the letter to replace: ");
			Character replacementLetter = parseInput(input,"Enter its replacement: ");
			controller.guess(letterToReplace,replacementLetter);
		}
		System.out.println("You win!");
		
	}
	
	/**
	 * PRINT GREETING
	 * prints basic program greeting
	 */
	public static void printGreeting() {
		System.out.println("Greetings! Welcome to Cryptogram!");
		System.out.println("Win the game by decrypting the message.");
		System.out.println("");
	}
	
	/**
	 * PARSE INPUT
	 * validation loops the user until the input can be parsed to a valid character
	 * @param input, the scanner that takes input
	 * @param message, the request message to send to user
	 * @return Character, parsed and ready to go
	 */
	public static Character parseInput(Scanner input, String message) {
		Character letter = null;
		while (!controller.checkLetter(letter)) {
			System.out.println(message);
			String inputString = input.nextLine().trim().toUpperCase();
			if (inputString.equals("") || inputString==null) {
				continue;
			}
			letter = inputString.charAt(0);
		}
		return letter;
	}
}
