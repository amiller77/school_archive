
public class Test_00_checkFields
{
	public static void main(String[] args)
	{
		Sim2_XOR xor = new Sim2_XOR();

		Sim2_HalfAdder ha  = new Sim2_HalfAdder();
		Sim2_FullAdder fa  = new Sim2_FullAdder();
		Sim2_AdderX   a32 = new Sim2_AdderX(32);

		/* we just just check the various fields - just to see
		 * if they exist.  This is *NOT* a logical test of any
		 * of the functionality!
		 */

		xor.a.set(false);
		xor.b.set(false);
		xor.execute();
		System.out.printf("XOR: %s %s -> %s\n",
		                  xor.a.get(), xor.b.get(), xor.out.get());

		ha.a.set(false);
		ha.b.set(false);
		ha.execute();
		System.out.printf("HALF ADDER: %s %s -> %s %s\n",
		                  ha.a.get(), ha.b.get(), ha.sum.get(), ha.carry.get());

		fa.a      .set(false);
		fa.b      .set(false);
		fa.carryIn.set(false);
		fa.execute();
		System.out.printf("FULL ADDER: %s %s %s -> %s %s\n",
		                  fa.a.get(), fa.b.get(), fa.carryIn.get(),
		                  fa.sum.get(), fa.carryOut.get());

		for (int i=0; i<32; i++)
		{
			a32.a[i].set(true);
			a32.b[i].set(true);
		}
		a32.execute();
		System.out.printf("ADD32 (too much to print): %s %s\n",
		                  a32.overflow.get(), a32.carryOut.get());
	}
}