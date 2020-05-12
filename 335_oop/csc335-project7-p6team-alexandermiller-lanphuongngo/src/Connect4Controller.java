import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

/**
 * @author Alexander Miller
 * @author Lan Ngo
 * Validates player moves, communicates with model to make them.
 */
public abstract class Connect4Controller {
	/**
	 * Number of columns on board
	 */
	public static int cols = 7;
	/**
	 * Number of rows on board
	 */
	public static int rows = 6;
	/**
	 * Model used to store pieces on board
	 */
	protected Connect4Model model;

	/**
	 * Instantiates instance variables
	 */
	public Connect4Controller() {
		model= new Connect4Model();
	}
	
	/**
	 * Instantiates instance variables
	 */
	public Connect4Controller(Connect4Model model) {
		this.model = model;
	}
	
	/**
	 * Getter method for model used only for adding view as
	 * an observer
	 * @return model of game
	 */
	public Connect4Model getModel() {
		return model;
	}
	
	public void move() {}
	
	public void otherMove(int row,int col,int player) {}
	
	// **************************************************************
	// ******************* FILE/SAVE MANAGEMENT METHODS ****************
	
	
	public void save(File saveFile) {
		try {
			ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(saveFile));
			objectOut.writeObject(model);
			objectOut.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void open(File openFile) {
		Connect4Model newModel;
		try {
			ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(openFile));
			newModel = (Connect4Model) objectIn.readObject();
			objectIn.close();
			
		} catch (Exception e) {
			System.out.println(e);
			return;
		}
		newGame();
		model.setBoard(newModel.getBoard());
	}
	
	public Connect4Model newGame() {
		model.clearBoard();
		return model;
	}
	
	// **************************************************************
	// ******************* BOARD CHECKING METHODS ****************
	
	/**
	 * CHECK FOR WINNER
	 * checks the columns, rows, and diagonals for a winner
	 * @return returns 1 if the computer wins, 2 if the player wins, 0 if neither wins
	 * note: public for testing purposes
	 */
	public int checkForWinner() {
		int r = checkRows();
		int c = checkColumns();
		int d1 = checkUpwardsDiagonal();
		int d2 = checkDownwardsDiagonal();
		if (r==2||c==2||d1==2||d2==2) {
			return 2;
		} else if (r==1||c==1||d1==1||d2==1) {
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
	public boolean checkForTie() {
		int[][] board = model.getBoard();
		for (int r = 0; r<rows;r++) {
			for (int c = 0; c<cols; c++) {
				// if there are any "empty" slots, return false
				if (board[r][c]==0) {
					// no tie:
					return false;
				}
			}
		}
		return true;
	}
	
	
	/**
	 * CHECK ROWS
	 * checks the rows for a winner
	 * @return returns 1 if the computer wins, 2 if the player wins, 0 if neither wins
	 */
	protected int checkRows() {
		int[][] board = model.getBoard();
		for (int r = 0; r<rows; r++) {
			int humanCounter = 0;
			int computerCounter = 0;
			for (int c = 0; c<cols; c++) {
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
	protected int checkColumns() {
		int[][] board = model.getBoard();
		for (int c = 0; c<cols; c++) {
			int humanCounter = 0;
			int computerCounter = 0;
			for (int r = 0; r<rows; r++) {
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
	protected int checkUpwardsDiagonal() {
		int[][] board = model.getBoard();
		int humanScore;
		int computerScore;
		// check down the left side of the board
		// lowest row is index 3, move down the rows
		for (int r = 3; r<rows; r++) {
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
				if (j>cols) {
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
	protected int checkDownwardsDiagonal() {
		int[][] board = model.getBoard();
		int humanScore;
		int computerScore;
		// move up left side; max row is 2
		for (int r = 2; r>=0; r--) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move down across the diagonal
			int j = 0;
			for (int k=r; k<rows;k++) {
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
				// max cols: 7
				if (j>cols-1) {
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
			for (int m = 0; m<rows; m++) {
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
				if (i>cols -1) {
					break;
				}
			}
		}
		return 0;	
	}	
}
