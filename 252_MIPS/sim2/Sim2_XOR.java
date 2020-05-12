/*
 * Author: Alexander Miller
 * File: Sim2_XOR.java
 * Description: simulates XOR by composing AND, OR, and NOT gates
 * Inputs: Russwires a,b
 * Output: Russwire (?)
 */
public class Sim2_XOR {

	// INPUTS
	public RussWire a;
	public RussWire b;

	// OUTPUTS
	public RussWire out;

	// INTERNAL COMPONENTS
	private AND and1;
	private AND and2;
	private OR or;
	private NOT not;


	// CONSTRUCTOR
	// instantiate "hardware"
	public Sim2_XOR() {
		a = new RussWire();
		b = new RussWire();
		out = new RussWire();
		and1 = new AND();
		and2 = new AND();
		or = new OR();
		not = new NOT();
	}

	// EXECUTE
	public void execute() {
		// RUN OR_GATE
		// plug in A,B into OR gate
		or.a.set(a.get());
		or.b.set(b.get());
		// run or
		or.execute();
		// plug or's out into andB
		and2.a.set(or.out.get());

		// RUN AND_GATE (1), AND NEGATE
		// plug A,B into AND gate
		and1.a.set(a.get());
		and1.b.set(b.get());
		// run and1
		and1.execute();
		// plug and1's out into NOT gate
		not.in.set(and1.out.get());
		// run NOT gate
		not.execute();
		// plug NOT out into and2
		and2.b.set(not.out.get());

		// RUN AND_GATE (2), AND SET OUT
		and2.execute();
		out.set(and2.out.get());


	}


}
