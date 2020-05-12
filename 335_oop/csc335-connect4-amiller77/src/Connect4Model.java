/**
 * 
 * @author Alexander Miller
 * this class represents the model, which contains where pieces have been placed
 * model has 0s for empty spots, 1s for computer moves, and 2s for human moves
 */
public class Connect4Model {

	//VARIABLES
	private int[][] board;
	
	//CONSTRUCTOR
	/**
	 * CONNECT 4 MODEL
	 * instantiates a blank board
	 */
	public Connect4Model() {
		this.board = new int[6][7];
	}
	
	// GET BOARD
	/**
	 * GET BOARD
	 * @return this.board, the board.
	 */
	public int[][] getBoard() {
		return this.board;
	}
	
	
}
