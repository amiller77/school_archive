package controller;

import view.Connect4View;
import model.Connect4Model;

/**
 * CONNECT 4 CONTROLLER
 * Connect4Controller class, which contains a model and operates on it
 * @author Alexander Miller
 */
public class Connect4Controller {

	// VARIABLES
	private Connect4Model model;
	
	// ******** EXTERNAL INTERFACE ********
	/**
	 * CONNECT 4 CONTROLLER
	 * default constructor 
	 */
	public Connect4Controller() {
		this.model=null;
	}
	
	/**
	 * INSTANTIATE MODEL
	 * separated from constructor so that the observer view can be returned and used
	 * @return
	 */
	public Connect4View instantiateModel() {
		Connect4View view = new Connect4View();
		this.model = new Connect4Model();
		this.model.addObserver(view);
		return view;
	}
	
	/**
	 * MOVE
	 * consolidates the human and computer turns 
	 * @param col
	 */
	public void move(int col) {
		boolean success = humanTurn(col);
		// if valid move and human doesn't win:
		if (success && checkForWinner()!=2) {
			// make computer move:
			computerTurn(null);
			// if computer doesn't win:
			if (checkForWinner()!=1) {
				// check for tie:
				checkForTie();
			}
		}
	}

	
	// ******** MECHANICS ***********
	/**
	 * HUMAN TURN
	 * @param col the column number to move
	 * @return boolean if the move was successful or not
	 * note: public for testing purposes
	 */
	private boolean humanTurn(int col) {
		if (col > 6 || col < 0) {
			return false;
		}
		return makeMove(col,2);
	}
	
	/**
	 * COMPUTER TURN
	 * repeatedly generates a random column to move, then makes the move, until one is successful
	 * @param debug, allows for input of move for testing, insert null for normal operation
	 * note that if you do debug, it is up to the tester to validate correct input
	 * note: public for testing purposes
	 */
	private void computerTurn(Integer debug) {
		if (debug != null) {
			makeMove(debug,1);
			return;
		}
		boolean success = false;
		while (!success) {
			int randomCol = (int) Math.floor(Math.random()*7);
			success = makeMove(randomCol,1);
		}
	}
	
	/**
	 * CHECK FOR WINNER
	 * checks the columns, rows, and diagonals for a winner
	 * @return returns 1 if the computer wins, 2 if the player wins, 0 if neither wins
	 * note: public for testing purposes
	 */
	private int checkForWinner() {
		int r = checkRows();
		int c = checkColumns();
		int d1 = checkUpwardsDiagonal();
		int d2 = checkDownwardsDiagonal();
		if (r==2||c==2||d1==2||d2==2) {
			this.model.notifyView(2);
			return 2;
		} else if (r==1||c==1||d1==1||d2==1) {
			this.model.notifyView(1);
			return 1;
		} else {
			return 0;
		}
		
	}
	
	/**
	 * CHECK FOR TIE
	 * checks for a tie game. Note: only works correctly, if the view already checked for a winner
	 * @return true if a tie, or false otherwise
	 * note: public for testing purposes
	 */
	private void checkForTie() {
		int[][] board = this.model.getBoard();
		for (int r = 0; r<6;r++) {
			for (int c = 0; c<7; c++) {
				// if there are any "empty" slots, return false
				if (board[r][c]==0) {
					// no tie:
					return;
				}
			}
		}
		// notify view of tie:
		this.model.notifyView(0);
	}
	
	/**
	 * MAKE MOVE
	 * @param col
	 * @param value
	 * @return true if move successful, else false
	 * note: public for testing purposes
	 */
	private boolean makeMove(int col, int value) {
		int[][] board = this.model.getBoard();
		// base case: full stack
		if (board[0][col] != 0) {
			// have model notify the view
			this.model.notifyView(3);
			return false;
		}
		// find first position to place the piece
		for (int r = 1; r <board.length; r++) {
			if (board[r][col]!= 0) {
				board[r-1][col]=value;
				// alert model of successful move:
				this.model.notifyView(r-1,col,value);
				return true;
			}
		}
		// if they're all zero, then put it in the last row
		board[board.length-1][col]=value;
		// alert model of successful move:
		this.model.notifyView(board.length-1,col,value);
		return true;
	}
	
	/**
	 * CHECK ROWS
	 * checks the rows for a winner
	 * @return returns 1 if the computer wins, 2 if the player wins, 0 if neither wins
	 */
	private int checkRows() {
		int[][] board = this.model.getBoard();
		for (int r = 0; r<6; r++) {
			int humanCounter = 0;
			int computerCounter = 0;
			for (int c = 0; c<7; c++) {
				if (board[r][c]==1) {
					computerCounter++;
					humanCounter=0;
				} else if (board[r][c]==2) {
					humanCounter++;
					computerCounter=0;
				} else {
					humanCounter=0;
					computerCounter=0;
				}
				if (humanCounter == 4) {
					return 2;
				} else if (computerCounter == 4) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	/**
	 * CHECK COLUMNS
	 * checks the columns for a winner
	 * @return returns 1 if the computer wins, 2 if the player wins, 0 if neither wins
	 */
	private int checkColumns() {
		int[][] board = this.model.getBoard();
		for (int c = 0; c<7; c++) {
			int humanCounter = 0;
			int computerCounter = 0;
			for (int r = 0; r<6; r++) {
				if (board[r][c]==1) {
					computerCounter++;
					humanCounter=0;
				} else if (board[r][c]==2) {
					humanCounter++;
					computerCounter=0;
				} else {
					humanCounter=0;
					computerCounter=0;
				}
				if (humanCounter == 4) {
					return 2;
				} else if (computerCounter == 4) {
					return 1;
				}
			}
		}
		return 0;
		
	}
	
	/**
	 * CHECK UPWARDS DIAGONAL
	 * checks the diagonals upwards for a winner
	 * @return returns 1 if the computer wins, 2 if the player wins, 0 if neither wins
	 */
	private int checkUpwardsDiagonal() {
		int[][] board = this.model.getBoard();
		int humanScore;
		int computerScore;
		// check down the left side of the board
		// lowest row is index 3, move down the rows
		for (int r = 3; r<6; r++) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move up across the diagonal
			int j = 0;
			for (int k = r; k>=0; k--) {
				if (board[k][j]==1) {
					computerScore++;
					humanScore=0;
				} else if (board[k][j]==2) {
					humanScore++;
					computerScore=0;
				} else {
					humanScore=0;
					computerScore=0;
				}
				if (humanScore == 4) {
					return 2;
				} else if (computerScore == 4) {
					return 1;
				}
				j++;
				// max cols: 6
				if (j>6) {
					break;
				}
			}
		}
		// now check across the bottom of the board
		// lowest column index is 1, max is 3
		for (int c = 1; c<4; c++) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move up across the diagonal
			int i = c;
			for (int m = 5; m>=0; m--) {
				if (board[m][i]==1) {
					computerScore++;
					humanScore=0;
				} else if (board[m][i]==2) {
					humanScore++;
					computerScore=0;
				} else {
					humanScore=0;
					computerScore=0;
				}
				if (humanScore == 4) {
					return 2;
				} else if (computerScore == 4) {
					return 1;
				}
				i++;
				// max cols: 6
				if (i>6) {
					break;
				}
			}
		}
		return 0;	
	}
	
	/**
	 * CHECK DOWNWARDS DIAGONAL
	 * checks the diagonals downwards for a winner
	 * @return returns 1 if the computer wins, 2 if the player wins, 0 if neither wins
	 */
	private int checkDownwardsDiagonal() {
		int[][] board = this.model.getBoard();
		int humanScore;
		int computerScore;
		// move up left side; max row is 2
		for (int r = 2; r>=0; r--) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move down across the diagonal
			int j = 0;
			for (int k=r; k<6;k++) {
				if (board[k][j] == 1) {
					computerScore++;
					humanScore=0;
				} else if (board[k][j]==2) {
					humanScore++;
					computerScore=0;
				} else {
					humanScore=0;
					computerScore=0;
				}
				if (humanScore == 4) {
					return 2;
				} else if (computerScore == 4) {
					return 1;
				}	
				j++;
				// max cols: 6
				if (j>6) {
					break;
				}
			}
		}
		// now check across the top of the board
		// lowest column index is 1, max is 3
		for (int c = 1; c<4; c++) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move down across the diagonal
			int i = c;
			for (int m = 0; m<6; m++) {
				if (board[m][i]==1) {
					computerScore++;
					humanScore=0;
				} else if (board[m][i]==2) {
					humanScore++;
					computerScore=0;
				} else {
					humanScore=0;
					computerScore=0;
				}
				if (humanScore == 4) {
					return 2;
				} else if (computerScore == 4) {
					return 1;
				}
				i++;
				// max cols: 6
				if (i>6) {
					break;
				}
			}
		}
		return 0;	
	}
	
	
	
}