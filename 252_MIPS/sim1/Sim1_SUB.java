/* Simulates a physical device that performs (signed) subtraction on
 * a 32-bit input.
 *
 * Author: Alexander Miller
 */

public class Sim1_SUB {
	
	// INPUTS
	public RussWire[] a;
	public RussWire[] b;
	
	// OUTPUT
	public RussWire[] sum;
	
	// INTERNAL MECHANICS
	private Sim1_2sComplement twosComplement;
	private Sim1_ADD adder;
	
	// CONSTRUCTOR
	public Sim1_SUB() {
		a = new RussWire[32];
		b = new RussWire[32];
		sum = new RussWire[32];
		for (int i = 0; i<32; i++) {
			a[i] = new RussWire();
			b[i]= new RussWire();
			sum[i] = new RussWire();
		}
		twosComplement = new Sim1_2sComplement();
		adder = new Sim1_ADD();
	}
	
	// EXECUTE
	public void execute() {
		// flip the second operand using twosComplement:
		// plug in b to twosComplement input
		for (int i = 0; i<32; i++) {
			twosComplement.in[i].set(b[i].get());
		}
		// execute twosComplement
		twosComplement.execute();
		// add a and b complement using adder:
		// plug a into adder's a, and twosComplement out into adder's b:
		for (int i = 0; i<32; i++) {
			adder.a[i].set(this.a[i].get());
			adder.b[i].set(twosComplement.out[i].get());
		}
		adder.execute();
		// pull adder's sum into our sum
		for (int i = 0; i<32; i++) {
			this.sum[i].set(adder.sum[i].get());
		}
		
	}


}
