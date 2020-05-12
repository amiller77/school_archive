/* Simulates a physical NOT gate.
 *
 * Author: Alexander Miller
 */

public class Sim1_NOT {
	
	// INPUT
	public RussWire in;
	// OUTPUT
	public RussWire out;
	
	// CONSTRUCTOR
	public Sim1_NOT() {
		in = new RussWire();
		out = new RussWire();
	}
	
	// EXECUTE
	public void execute() {
		// set out to opposite of in
		out.set(!in.get());
	}

}
