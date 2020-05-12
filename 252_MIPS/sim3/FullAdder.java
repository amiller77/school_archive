public class FullAdder
{
	public FullAdder()
	{
		a       = new RussWire();
		b       = new RussWire();
		carryIn = new RussWire();

		sum      = new RussWire();
		carryOut = new RussWire();

		add1 = new HalfAdder();
		add2 = new HalfAdder();
		or   = new OR();
	}


	// inputs
	public RussWire a,b, carryIn;
	// outputs
	public RussWire sum, carryOut;

	// internal components
	public HalfAdder add1,add2;
	public OR        or;

	void execute()
	{
		add1.a.set(a.get());
		add1.b.set(b.get());
		add1.execute();

		add2.a.set(add1.sum.get());
		add2.b.set(carryIn.get());
		add2.execute();

		or.a.set(add1.carry.get());
		or.b.set(add2.carry.get());
		or.execute();

		sum     .set(add2.sum.get());
		carryOut.set(or  .out.get());
	}
}
