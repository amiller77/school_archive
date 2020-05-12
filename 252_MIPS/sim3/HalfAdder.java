public class HalfAdder
{
	public HalfAdder()
	{
		a = new RussWire();
		b = new RussWire();

		sum   = new RussWire();
		carry = new RussWire();

		xor = new XOR();
		and = new AND();
	}

	// inputs
	public RussWire a,b;
	// outputs
	public RussWire sum,carry;

	// internal components
	public XOR xor;
	public AND and;

	void execute()
	{
		xor.a.set(a.get());
		xor.b.set(b.get());
		xor.execute();

		and.a.set(a.get());
		and.b.set(b.get());
		and.execute();

		sum  .set(xor.out.get());
		carry.set(and.out.get());
	}
}
