
public class Player {
	int player;
	Connect4Model model;
	public static int cols = 7;
	public static int rows = 6;
	
	public void move() {};
	/**
	 * MAKE MOVE
	 * @param col
	 * @param value
	 * @return true if move successful, else false
	 * note: public for testing purposes
	 */
	protected void makeMove(int col) {
		int[][] board = model.getBoard();
		// find first position to place the piece
		for (int r = 1; r <board.length; r++) {
			if (board[r][col]!= 0) {
				board[r-1][col]=player;
				// alert model of successful move:
				model.set(r-1, col, player);
				return;
			}
		}
		// if they're all zero, then put it in the last row
		board[board.length-1][col]=player;
		// alert model of successful move:
		model.set(board.length-1, col, player);
		return;
	}
}
