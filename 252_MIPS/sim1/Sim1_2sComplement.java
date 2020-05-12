/* Simulates a physical device that performs 2's complement on a 32-bit input.
 *
 * Author: Alexander Miller
 */

public class Sim1_2sComplement {
	// INPUT, OUTPUT
	public RussWire[] in;
	public RussWire[] out;
	
	// INTERNAL HARDWARE
	private Sim1_NOT[] notGates;
	private Sim1_ADD addGate;
	
	// CONSTRUCTOR
	public Sim1_2sComplement() {
		// initialize inputs, outputs, other hardware
		in = new RussWire[32];
		out = new RussWire[32];
		notGates = new Sim1_NOT[32];
		addGate = new Sim1_ADD();
		for (int i = 0; i < 32; i++) {
			in[i] = new RussWire();
			out[i] = new RussWire();
			notGates[i] = new Sim1_NOT();
		}
	}
	
	// EXECUTE
	public void execute() {
		// take inputs, plug them into the notGates, and execute each one, map output to addGate input slot
		for (int i = 0; i<32; i++) {
			Sim1_NOT notGate = notGates[i];
			// plug input into notGate
			RussWire notGateInputWire = this.in[i];
			notGate.in.set(notGateInputWire.get());
			// execute notGate
			notGate.execute();
			// plug output into addGate input
			addGate.a[i].set(notGate.out.get());
			// initialize other operand to be 1:
			if (i == 0) {
				addGate.b[i].set(true);
			} else {
				addGate.b[i].set(false);
			}
		}
		// call execute on the add gate
		addGate.execute();
		// plug output from adder to output for 2sComplement
		for (int i = 0; i<32; i++) {
			this.out[i].set(addGate.sum[i].get());
		}
	}



}
