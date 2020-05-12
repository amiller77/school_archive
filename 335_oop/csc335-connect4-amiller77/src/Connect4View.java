import java.util.Scanner;
/**
 * 
 * @author Alexander Miller
 * Connect4View, which takes and validates user input, and contains and queries a 
 * controller, which in turn interacts with the model
 */
public class Connect4View {
	
	// VARIABLES
	private Connect4Controller controller;
	private Scanner scanner;

	// CONSTRUCTOR
	/**
	 * CONNECT 4 VIEW
	 * constructor ~ creates a scanner to take input and a controller to query
	 */
	public Connect4View() {
		this.controller = new Connect4Controller();
		this.scanner = new Scanner(System.in);
	}
	
	/**
	 * PLAY GAME
	 * controls the main control flow of the program, and loops until victory or tie
	 */
	public void playGame() {
		greeting();
		boolean loop = true;
		while (true) {
			userQuery();
			printBoard();
			if (this.controller.checkForWinner()==2) {
				System.out.println("You win!");
				break;
			}
			controller.computerTurn(null);
			printBoard();
			if (this.controller.checkForWinner()==1) {
				System.out.println("You lose!");
				break;
			}
			if (controller.checkForTie()) {
				System.out.println("Tie game!");
				break;
			}
		}
	}
	
	/**
	 * GREETING
	 * prints out a greeting for the user
	 */
	public void greeting() {
		System.out.println("Welcome to Connect 4");
		System.out.println("");
		printBoard();
		System.out.println("");
		System.out.println("You are X");
		System.out.println("");
	}
	
	/**
	 * PRINT BOARD
	 * iterates over the possible board locations, requests the tile at the location from the
	 * controller, then paints it
	 * then, paints a last row of digits for user column reference
	 * insulates the drawing with 2 blank lines
	 */
	public void printBoard() {
		System.out.println("");
		for (int row = 0; row<6; row++) {
			String printString="";
			for (int col = 0; col<7; col++) {
				int tile = this.controller.getTile(row, col);
				if (tile == 0) {
					printString+="_ ";
				} else if (tile ==1) {
					printString+="O ";
				} else if (tile==2) {
					printString+="X ";
				} else {
					System.out.println("CRITICAL MODEL ERROR: INVALID TOKEN: "+tile);
				}
			}
			System.out.println(printString);
		}
		System.out.println("0 1 2 3 4 5 6");
		System.out.println("");
	}
	
	/**
	 * USER QUERY
	 * traps the user in a loop that continues until they give a valid column to move
	 */
	public void userQuery() {
		boolean successfulMove = false;
		while (!successfulMove) {
			System.out.println("What column would you like to place your token in?");
			boolean validInput = false;
			int col = 0;
			while (!validInput) {
				String input = this.scanner.nextLine();
				try {
					col = Integer.parseInt(input);
					validInput = true;
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter an integer. ");
				}
			}
			successfulMove = this.controller.humanTurn(col);
			if (successfulMove == false) {
				System.out.println("Move invalid. Try again.");
			}
		}
	}
	
	
}
