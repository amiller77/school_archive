/*
 * Author: Alexander Miller
 * File: Sim2_FullAdder.java
 * Description: simulates 1-bit adder by composing 2 half adders
 * Inputs: Russwires a,b, carryIn
 * Output: Russwire sum, carryOut
 */
public class Sim2_FullAdder {
	// INPUTS
	public RussWire a;
	public RussWire b;
	public RussWire carryIn;

	// OUTPUTS
	public RussWire sum;
	public RussWire carryOut;

	// INTERNALS
	private Sim2_HalfAdder adder1;
	private Sim2_HalfAdder adder2;
	private OR or;

	// CONSTRUCTOR
	public Sim2_FullAdder() {
		a = new RussWire();
		b = new RussWire();
		carryIn = new RussWire();
		sum = new RussWire();
		carryOut = new RussWire();
		adder1 = new Sim2_HalfAdder();
		adder2 = new Sim2_HalfAdder();
		or = new OR();
	}

	// EXECUTE
	// if you map out the outcome space of 2 successive adds, there are no cases
	// where both carries are set to 1, and their OR determines the ultimate carry value
	// you can also just add A,B,carryIn in two successive adds to get sum
	public void execute() {
		// ADD A,B
		// plug a, b into adder1
		adder1.a.set(a.get());
		adder1.b.set(b.get());
		adder1.execute();
		// result: s1 = adder1.sum = add A,B; c1 = adder1.carry
		// ADD s1, carryIn
		adder2.a.set(adder1.sum.get());
		adder2.b.set(carryIn.get());
		adder2.execute();
		// result: sum, c2 = adder2.carry
		sum.set(adder2.sum.get());

		// OR c2, c1
		or.a.set(adder1.carry.get());
		or.b.set(adder2.carry.get());
		or.execute();
		// result: carryOut
		carryOut.set(or.out.get());
	}

}
