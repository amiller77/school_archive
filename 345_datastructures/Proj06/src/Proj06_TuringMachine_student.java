import java.util.HashMap;
import java.util.LinkedList;

/**
 * this approach simulates an actual Turing Machine by traversing a list 
 * representing the tape, rather than by using a graph simulation
 * @author Alexander Miller
 *
 */
public class Proj06_TuringMachine_student implements Proj06_TuringMachine {
	
	private char[] tape;
	private LinkedList<String> acceptStates;
	private String currentState;
	private int statesAdded;
	private HashMap<Character,LinkedList<Transition>> transitionMap;
	private int headPosition;
	private boolean debug;
	private String startState;
	private int iterations;
	
	/**
	 * CONSTRUCTOR
	 */
	public Proj06_TuringMachine_student () {
		acceptStates = new LinkedList<String>();
		statesAdded = 0;
		transitionMap = new HashMap<Character,LinkedList<Transition>>();
	}

	
	/**
	 * ADD STATE
	 * @param accept is true if state is an accept state, false otherwise
	 */
	public void addState(String stateName, boolean accept) {
		// add any accept states
		if (accept) {
			acceptStates.add(stateName);
		}
		// make sure first state added is the initialized start state
		if (statesAdded == 0) {
			statesAdded++;
			startState = stateName;
		}
	}
	
	
	/**
	 * ADD TRANSITION
	 * @param from the name of the start state
	 * @param to the name of the end state
	 * @param read character under the head
	 * @param write the character to write
	 * @param moveDir true if move direction is right, false if left
	 */
	public void addTransition(String from, char read, char write, 
			boolean moveDir, String to) {
		// create transition:
		Transition t = new Transition(from, to, read, write, moveDir);
		// add to transitionMap:
		// see what's at transition map already:
		LinkedList<Transition> currentMapping = transitionMap.get(read);
		if (currentMapping == null) {
			currentMapping = new LinkedList<Transition>();
		}
		currentMapping.add(t);
		// just to be safe, put mapping back in:
		transitionMap.put(read,currentMapping);
	}
	
	
	/**
	 * RUN
	 * @param startString beginning state of the tape
	 * @param debug if debug==false -> print out initial state, ending state, accept/reject
	 */
	public void run (String startString, boolean debug) {
		// initialize current state:
		currentState = startState;
		// initialize debug:
		this.debug = debug;
		// initialize the tape:
		tape = new char[startString.length()];
		for (int i = 0; i < startString.length(); i++) {
			tape[i] = startString.charAt(i);
		}
		// move head back to start
		headPosition = 0;
		// reset iterations
		iterations = 0;
		// run algorithm
		runHelper();
	}
	
	
	/**
	 * RUN HELPER
	 */
	private void runHelper() {
		// base case: if current state accept state:
		if (verifyAcceptState()) {
			// create state of machine print out; return
			generateStatePrintout(true);
			return;
		} 
		
		// read the tape at head position if on tape:
		char read = tape[headPosition];
		
		// find appropriate transition:
		LinkedList<Transition> transitionMapping = transitionMap.get(read);
		Transition t = null;
		for (Transition transition : transitionMapping ) {
			if (transition.startState.equals(currentState)) {
				t = transition;
				break;
			}
		}
		
		// if no entry for that char exists, reject:
		if (t == null) {
			generateStatePrintout(false);
			return;
		}
		// else if debug or first round, generate state printout
		if (debug || iterations == 0) {
			generateStatePrintout(null);
			iterations++;
		}
	
		// write the write char:
		tape[headPosition] = t.writeChar;
		// move L or R (increment head position):
		if (t.moveDir) {
			headPosition++;
		} else {
			headPosition--;
		}
		
		// if head position out of tape bounds, modify the tape accordingly:
		if (headPosition >= tape.length) {
			runOffTheRight();
		} else if (headPosition < 0) {
			runOffTheLeft();
		}
		
		// update current state from transition end state:
		currentState = t.endState;
		
		// recurse:
		runHelper();
	}
	
	/**
	 * VERIFY ACCEPT STATE
	 * @return true if current state accept state, false otherwise
	 */
	private boolean verifyAcceptState() {
		for (String state : acceptStates) {
			if (currentState.equals(state)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * RUN OFF THE RIGHT
	 * updates tape if the head runs off to the right
	 * extends array by 1, adds period to end
	 */
	private void runOffTheRight() {
		char[] newTape = new char[tape.length+1];
		for (int i = 0; i<tape.length; i++) {
			newTape[i] = tape[i];
		}
		newTape[tape.length]='.';
		tape = newTape;
	}
	
	/**
	 * RUN OFF THE LEFT
	 * updates tape if the head runs off to the left
	 * extends array by 1, shifts contents over, adds period to start
	 */
	private void runOffTheLeft() {
		char[] newTape = new char[tape.length+1];
		for (int i = 0; i<tape.length; i++) {
			newTape[i+1] = tape[i];
		}
		newTape[0] = '.';
		tape = newTape;
		// reset head position to 0 on the new array
		headPosition = 0;
	}
	
	
	/**
	 * GENERATE STATE PRINTOUT
	 * @param accept if true: machine accepts, if false: machine rejects, if null: neither
	 * @return
	 */
	private void generateStatePrintout(Boolean accept) {
		// add representation of tape:
		String printString = new String(tape);
		printString = printString + "\n";
		
		// create second line:
		String secondLine = "";
		for (int i = 0; i < headPosition; i++) {
			secondLine = secondLine + " ";
		}
		secondLine = secondLine + "^" + "   " + "state: " + currentState;
		printString = printString + secondLine;
		
		// add accept end line
		if (accept!= null && accept) {
			printString = printString + "\n" + "\n" + "MACHINE ACCEPTS!";
		}
		
		// add reject end line
		if (accept!= null && !accept) {
			printString = printString + "\n" + "\n" + "MACHINE REJECTS!";
		}
		
		System.out.println(printString);
	}
	
	
	
	
}
