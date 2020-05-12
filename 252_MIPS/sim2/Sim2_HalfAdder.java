/*
 * Author: Alexander Miller
 * File: Sim2_HalfAdder.java
 * Description: simulates 1-bit half adder
 * Inputs: Russwires a,b
 * Output: Russwire sum, carry
 */
public class Sim2_HalfAdder {
	// INPUTS
	public RussWire a;
	public RussWire b;

	// OUTPUTS
	public RussWire sum;
	public RussWire carry;

	// INTERNALS
	private Sim2_XOR xor;
	private AND and;

	// CONSTRUCTOR
	// build "hardware"
	public Sim2_HalfAdder() {
		a = new RussWire();
		b = new RussWire();
		sum = new RussWire();
		carry = new RussWire();
		xor = new Sim2_XOR();
		and = new AND();
	}

	// EXECUTE
	// run an XOR and AND gate in parallel
	// they independently determine sum and carry resp.
	public void execute() {
		// XOR GATE OP
		xor.a.set(a.get());
		xor.b.set(b.get());
		xor.execute();
		sum.set(xor.out.get());

		// AND GATE OP
		and.a.set(a.get());
		and.b.set(b.get());
		and.execute();
		carry.set(and.out.get());

	}


}
