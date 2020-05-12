package model;

/**
 * 
 * @author Alexander Miller
 * this class represents the model, which contains where pieces have been placed
 * model has 0s for empty spots, 1s for computer moves, and 2s for human moves
 */
public class Connect4Model extends java.util.Observable{

	//VARIABLES
	private int[][] board;
	
	/**
	 * CONNECT 4 MODEL
	 * instantiates a blank board
	 */
	public Connect4Model() {
		this.board = new int[6][7];
	}
	
	/**
	 * GET BOARD
	 * @return this.board, the board.
	 */
	public int[][] getBoard() {
		return this.board;
	}
	
	/**
	 * NOTIFY VIEW
	 * lets the controller tell the model to update the view
	 * @param situation: 0 -> tie, 1 -> computer win, 2 -> player win, 3 -> full stack
	 */
	public void notifyView(int situation) {
		this.setChanged();
		this.notifyObservers((Integer) situation);
	}
	/**
	 * NOTIFY VIEW (overload)
	 * @param row: row of move
	 * @param col: column of move
	 * @param val: value of move
	 */
	public void notifyView(int row, int col, int val) {
		this.setChanged();
		int[] i = {row,col,val};
		this.notifyObservers(i);
	}

	
	
}
