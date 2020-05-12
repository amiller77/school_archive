import java.io.Serializable;

import javafx.scene.paint.Color;

/**
 * @author Lan Ngo
 * @author Alexander Miller
 * this class represents the model, which contains where pieces have been placed
 * model has 0s for empty spots, 1s for computer moves, and 2s for human moves
 */
public class Connect4Model extends java.util.Observable implements Serializable{
	/**
	 * Number of columns on board
	 */
	public transient static int cols = 7;
	/**
	 * Number of rows on board
	 */
	public transient static int rows = 6;
	/**
	 * 2D Array used to store pieces on board
	 */
	private int[][] board;
	/**
	 * Used to communicate changes with view
	 */
	private transient Connect4MoveMessage message;
	
	static final long serialVersionUID = 123456;
	
	/**
	 * Instantiates instance variables
	 */
	public Connect4Model() {
		board = new int[rows][cols];
	}
	
	public void setBoard(int[][] newBoard) {
		board = newBoard;
	}
	
	/**
	 * GET BOARD
	 * @return 2D array representing board
	 */
	public int[][] getBoard() {
		return board;
	}
	
	public void clearBoard() {
		board = new int[rows][cols];
		message = new Connect4MoveMessage();
		message.setClear();
		setChanged();
		notifyObservers(message);
	}
	/**
	 * Sends View alert
	 */
	public void setAlert(String alerttype, String title, String header, String context) {
		message = new Connect4MoveMessage();
		message.setAlert(alerttype, title, header, context);
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Sends View board changes
	 */
	public void set(int row, int col, Color color) {
		message = new Connect4MoveMessage();
		message.set(row, col, color);
		setChanged();
		notifyObservers(message);
	}
}
