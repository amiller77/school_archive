/* Simulates a physical AND gate.
 *
 * Author: Alexander Miller
 */

public class Sim1_AND {
	
	// INPUTS
	public RussWire a;
	public RussWire b;
	
	// OUTPUTS
	public RussWire out;
	
	// MECHANICS
	//private RussWire in
	
	// CONSTRUCTOR
	public Sim1_AND() {
		a = new RussWire();
		b = new RussWire();
		out = new RussWire();
		
	}
	
	// EXECUTE
	public void execute() {
		// if both true -> true, else false
		if (a.get() && b.get()) {
			out.set(true);
		} else {
			out.set(false);
		}
	}


}
