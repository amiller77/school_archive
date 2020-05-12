/**
 * this class simply provides easier concept than an array of strings would
 * transitions are stored in a map and reached by their read character
 * @author Alexander
 *
 */
public class Transition {

	public String startState;
	public String endState;
	public char readChar;
	public char writeChar;
	public boolean moveDir; //true if move direction is right, false if left
	
	public Transition(String from, String to, char read,
			char write, boolean move) {
		startState = from;
		endState = to;
		readChar = read;
		writeChar = write;
		moveDir = move;
	}
	
}
