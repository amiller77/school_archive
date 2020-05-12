/* Simulates a physical OR gate.
 *
 * Author: Alexander Miller
 */

public class Sim1_OR {
	
	// INPUTS
	public RussWire a;
	public RussWire b;
	
	// OUTPUT
	public RussWire out;
	
	// CONSTRUCTOR
	public Sim1_OR() {
		a = new RussWire();
		b = new RussWire();
		out = new RussWire();
	}
	
	// EXECUTE
	public void execute() {
		// set output to OR of input values
		out.set(a.get()||b.get());
	}



}
