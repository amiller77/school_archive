/**
 * 
 * @author Alexander Miller
 * this program operates almost entirely through a view. this function calls the view and its
 * playGame method
 */
public class Connect4 {

	/**
	 *  MAIN
	 *  instantiates a view, and then runs playGame
	 *  @param args, the control-line argument
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connect4View view = new Connect4View();
		view.playGame();
	}

}
