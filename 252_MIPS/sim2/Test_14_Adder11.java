/* Testcase for Sim2.
 *
 * Author: Russ Lewis
 *
 * This checks that the X-bit adder is working properly.  (At least, it
 * tests one possible addition problem.)
 */

public class Test_14_Adder11
{
	public static void main(String[] args)
	{
		Sim2_AdderX p = new Sim2_AdderX(11);

		// These are the same bits as we testing in Project 1,
		// testcase 3.
		p.a[ 0].set(      false);
		p.a[ 1].set(      false);
		p.a[ 2].set(      false);
		p.a[ 3].set(true);
		p.a[ 4].set(true);
		p.a[ 5].set(true);
		p.a[ 6].set(      false);
		p.a[ 7].set(      false);
		p.a[ 8].set(true);
		p.a[ 9].set(true);
		p.a[10].set(      false);

		p.b[ 0].set(true);
		p.b[ 1].set(true);
		p.b[ 2].set(true);
		p.b[ 3].set(      false);
		p.b[ 4].set(true);
		p.b[ 5].set(      false);
		p.b[ 6].set(true);
		p.b[ 7].set(true);
		p.b[ 8].set(true);
		p.b[ 9].set(true);
		p.b[10].set(true);

		p.execute();

		System.out.printf("Binary:\n");
		printf_binary    ("    ", p.a);
		printf_binary    ("  + ", p.b);
		System.out.printf(" -----------------\n");
		printf_binary    ("    ", p.sum);

		System.out.printf("\n");
		System.out.printf("  carryOut = %5s\n", p.carryOut);
		System.out.printf("  overflow = %5s\n", p.overflow);
	}

	public static void printf_binary(String prefix, RussWire[] arr)
	{
		int i;

		System.out.printf("%s", prefix);

		for (i=arr.length-1; i>=0; i--)
		{
			System.out.printf("%d", (arr[i].get()?1:0));

			if (i%4 == 0 && i != 0)
				System.out.printf(" ");
		}

		System.out.printf("\n");
	}
}
