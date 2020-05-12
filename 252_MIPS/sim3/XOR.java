public class XOR
{
	public XOR()
	{
		a = new RussWire();
		b = new RussWire();

		out = new RussWire();

		not1 = new NOT();
		not2 = new NOT();
		and1 = new AND();
		and2 = new AND();
		or   = new OR();
	}

	// inputs
	public RussWire a,b;
	// outputs
	public RussWire out;

	// internal components
	public NOT not1,not2;
	public AND and1,and2;
	public OR  or;

	void execute()
	{
		// negate B
		not1.in.set(b.get());
		not1.execute();

		// calculate (A AND NOT B)
		and1.a.set(a.get());
		and1.b.set(not1.out.get());
		and1.execute();

		// negate A
		not2.in.set(a.get());
		not2.execute();

		// calculate (NOT A AND B)
		and2.a.set(not2.out.get());
		and2.b.set(b.get());
		and2.execute();

		// fill the two ANDs into the output OR
		or.a.set(and1.out.get());
		or.b.set(and2.out.get());
		or.execute();

		// copy the output bit into place
		out.set(or.out.get());
	}
}
