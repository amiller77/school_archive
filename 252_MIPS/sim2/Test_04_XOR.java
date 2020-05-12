/* Testcase for Sim2.
 *
 * Author: Russ Lewis
 *
 * This checks that XOR is working properly.
 */

public class Test_04_XOR
{
	public static void main(String[] args)
	{
		Sim2_XOR p = new Sim2_XOR();

		p.a.set(false);
		p.b.set(false);
		p.execute();
		System.out.printf("%5s ^ %5s = %5s\n", p.a, p.b, p.out);

		p = new Sim2_XOR();
		p.a.set(false);
		p.b.set(true);
		p.execute();
		System.out.printf("%5s ^ %5s = %5s\n", p.a, p.b, p.out);

		p = new Sim2_XOR();
		p.a.set(true);
		p.b.set(false);
		p.execute();
		System.out.printf("%5s ^ %5s = %5s\n", p.a, p.b, p.out);

		p = new Sim2_XOR();
		p.a.set(true);
		p.b.set(true);
		p.execute();
		System.out.printf("%5s ^ %5s = %5s\n", p.a, p.b, p.out);
	}
}
