/* Testcase for Sim2.
 *
 * Author: Russ Lewis
 *
 * This checks that the 32-bit adder is working properly.  (At least, it
 * tests one possible addition problem.)
 */

public class Test_10_Adder32
{
	public static void main(String[] args)
	{
		Sim2_AdderX p = new Sim2_AdderX(32);

		// These are the same bits as we testing in Project 1,
		// testcase 3.
		p.a[ 0].set(true);
		p.a[ 1].set(true);
		p.a[ 2].set(true);
		p.a[ 3].set(true);
		p.a[ 4].set(      false);
		p.a[ 5].set(      false);
		p.a[ 6].set(true);
		p.a[ 7].set(true);
		p.a[ 8].set(true);
		p.a[ 9].set(      false);
		p.a[10].set(true);
		p.a[11].set(      false);
		p.a[12].set(      false);
		p.a[13].set(      false);
		p.a[14].set(      false);
		p.a[15].set(      false);
		p.a[16].set(true);
		p.a[17].set(      false);
		p.a[18].set(      false);
		p.a[19].set(true);
		p.a[20].set(      false);
		p.a[21].set(      false);
		p.a[22].set(      false);
		p.a[23].set(      false);
		p.a[24].set(      false);
		p.a[25].set(      false);
		p.a[26].set(      false);
		p.a[27].set(      false);
		p.a[28].set(      false);
		p.a[29].set(      false);
		p.a[30].set(      false);
		p.a[31].set(true);

		p.b[ 0].set(      false);
		p.b[ 1].set(      false);
		p.b[ 2].set(true);
		p.b[ 3].set(true);
		p.b[ 4].set(      false);
		p.b[ 5].set(true);
		p.b[ 6].set(      false);
		p.b[ 7].set(true);
		p.b[ 8].set(      false);
		p.b[ 9].set(      false);
		p.b[10].set(      false);
		p.b[11].set(true);
		p.b[12].set(      false);
		p.b[13].set(true);
		p.b[14].set(      false);
		p.b[15].set(      false);
		p.b[16].set(      false);
		p.b[17].set(true);
		p.b[18].set(      false);
		p.b[19].set(true);
		p.b[20].set(      false);
		p.b[21].set(      false);
		p.b[22].set(      false);
		p.b[23].set(true);
		p.b[24].set(      false);
		p.b[25].set(      false);
		p.b[26].set(      false);
		p.b[27].set(      false);
		p.b[28].set(      false);
		p.b[29].set(true);
		p.b[30].set(      false);
		p.b[31].set(true);

		p.execute();

		System.out.printf("Binary:\n");
		printf_binary    ("    ", p.a);
		printf_binary    ("  + ", p.b);
		System.out.printf(" -------------------------------------------\n");
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
