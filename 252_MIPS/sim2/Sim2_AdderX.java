/*
 * Author: Alexander Miller
 * File: Sim2_AdderX.java
 * Description: simulates X-bit adder by composing 1-bit adders
 * Inputs: Russwires[X] a,b
 * Output: Russwire[X] sum, RussWire carryOut, RussWire overflow
 */
public class Sim2_AdderX {

	// INPUTS
	public RussWire[] a;
	public RussWire[] b;

	// OUTPUTS
	public RussWire[] sum;
	public RussWire carryOut;
	public RussWire overflow;

	// INTERNALS
	int X;
	private Sim2_FullAdder[] adderArray;
	private RussWire[] carryWires;
	private AND and;
	private OR or1;
	private NOT not;
	private OR or2;
	private Sim2_XOR xor;

	// CONSTRUCTOR
	public Sim2_AdderX(int X) {
		this.X = X;
		a = new RussWire[X];
		b = new RussWire[X];
		sum = new RussWire[X];
		adderArray = new Sim2_FullAdder[X];
		carryWires = new RussWire[X];
		for (int i = 0; i<X; i++) {
			a[i] = new RussWire();
			b[i] = new RussWire();
			sum[i] = new RussWire();
			adderArray[i] = new Sim2_FullAdder();
			carryWires[i] = new RussWire();
		}
		carryOut = new RussWire();
		overflow = new RussWire();
		and = new AND();
		or1 = new OR();
		not = new NOT();
		or2 = new OR();
		xor = new Sim2_XOR();
	}

	// EXECUTE
	// find sum and carryout through sequence of full adders
	// let An and Bn be the most significant bits of input
	// let Sn be the most significant bit of the sum
	// then overflow = [NOT(An OR Bn) OR (An AND Bn)] XOR Sn
	public void execute() {
		// PERFORM ADD
		// do step 1 outside of loop b/c we can't condition on irregular 1st carry situation
		// plug A0, B0, carry0 into adder0
		adderArray[0].a.set(a[0].get());
		adderArray[0].b.set(b[0].get());
		adderArray[0].carryIn.set(false);
		// run adder
		adderArray[0].execute();
		// plug sum to sum array, plug carry into carrywires
		sum[0].set(adderArray[0].sum.get());
		carryWires[0].set(adderArray[0].carryOut.get());
		// do rest of loop
		for (int i=1; i<X; i++) {
			// plug Ai, Bi, carryi into adderi
			adderArray[i].a.set(a[i].get());
			adderArray[i].b.set(b[i].get());
			adderArray[i].carryIn.set(carryWires[i-1].get());
			// run adder
			adderArray[i].execute();
			// plug sum to sum array, plug carry into carrywires
			sum[i].set(adderArray[i].sum.get());
			carryWires[i].set(adderArray[i].carryOut.get());
		}
		// set carryOut
		carryOut.set(adderArray[X-1].carryOut.get());

		// PERFORM OVERFLOW CHECK (looking at last bits in a, b, sum)
		// C = AND An, Bn // both are positive operands
		and.a.set(a[X-1].get());
		and.b.set(b[X-1].get());
		and.execute();
		or2.a.set(and.out.get());

		// D = NOT (OR An, Bn) // both are negative operands
		or1.a.set(a[X-1].get());
		or1.b.set(b[X-1].get());
		or1.execute();
		not.in.set(or1.out.get());
		not.execute();
		or2.b.set(not.out.get());

		// E = OR C, D
		or2.execute();

		// overflow = XOR E, Sn // check for where Sn contradicts
		xor.a.set(or2.out.get());
		xor.b.set(sum[X-1].get());
		xor.execute();
		overflow.set(xor.out.get());
	}

}
