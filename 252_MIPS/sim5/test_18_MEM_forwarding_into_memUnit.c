#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

#include "sim5.h"
#include "sim5_test_commonCode.h"


int main()
{
	srand(0xdeadbeef);


	/* set up a common memory, which we'll reuse over and over without
	 * ever wiping it.
	 */
#define TEST_MEM_SIZE_WORDS 4096
	WORD mem[TEST_MEM_SIZE_WORDS];

	int i;
	for (i=0; i<TEST_MEM_SIZE_WORDS; i++)
		mem[i] = rand();


	/* we'll manually construct situations for the various combinations:
	 * is the previous instruction writing to a register (and if so,
	 * where from), where is it writing to (so that it can't be used as
	 * a hardcode field by studetns).
	 *
	 * UPDATE: Test everything twice, just for kicks.
	 */
	int writeMode, aheadReg, thisReg;
	for (int i=0; i<2; i++)
	for (int writeMode=0;  writeMode<3;  writeMode++)
	for (int  aheadReg=17;  aheadReg<20;  aheadReg++)
	for (int   thisReg=17;   thisReg<20;   thisReg++)
	{
		EX_MEM this_exmem;
		MEM_WB ahead_memwb, this_memwb;
		  memset(& this_exmem, 0, sizeof( this_exmem));
		  memset(&ahead_memwb, 0, sizeof(ahead_memwb));
		  memset(& this_memwb, 0, sizeof( this_memwb));

		
		/* initialize the 'this' instruction, which is always a
		 * store into an interesting address.  The address is
		 * randomly generated.
		 */
		this_exmem.rt    = thisReg;
		this_exmem.rtVal = rand();       // write value, if not forwarded

		this_exmem.memWrite = 1;

		this_exmem.writeReg = rand()%32;    // a proper implementation should ignore this
		this_exmem.regWrite = 0;

		this_exmem.aluResult = (rand()%TEST_MEM_SIZE_WORDS)*4;   // write addr


		/* initialize the ahead instruction, which could be just about
		 * anything.
		 */
		ahead_memwb.writeReg  = aheadReg;
		ahead_memwb.aluResult = rand();   // many instructions will have nonzero ALU results.  They might use it, or not.  whatever.

		if (writeMode == 0)
		{
			ahead_memwb.regWrite = 0;
			ahead_memwb.memToReg = 0;     // already zero, but I like to make it clear that it's zero because I *want* it to be
		}
		else
		{
			ahead_memwb.regWrite = 1;

			if (writeMode == 1)
			{
				// write the ALU resul to the register
				ahead_memwb.memToReg = 0;     // already zero, but I like to make it clear that it's zero because I *want* it to be
			}
			else
			{
				// write the memory result to the register
				ahead_memwb.memToReg = 1;
				ahead_memwb.memResult = rand();
			}
		}


		// handy: dump out the two pipeline registers we've filled
		// out.
		printf("+--------- BEGIN ARTIFICIAL TESTCASE ----------\n");
		printf("| Testcase loop variables: *NOT* control wires!!!\n");
		printf("|     writeMode=%d\n", writeMode);
		printf("|     aheadReg =%d\n", aheadReg);
		printf("|     thisReg  =%d\n", thisReg);
		printf("+----------------------------------------------\n");
		printf("  -- Old EX/MEM pipline fields --\n");
		printf("     EX_MEM.rt = %d\n", this_exmem.rt);
		printf("     EX_MEM.rtVal = 0x%04x_%04x\n",
		             (this_exmem.rtVal >> 16) & 0xffff,
		             (this_exmem.rtVal      ) & 0xffff);
		printf("     ---\n");
		printf("     EX_MEM.memRead  = %d\n", this_exmem.memRead);
		printf("     EX_MEM.memWrite = %d\n", this_exmem.memWrite);
		printf("     EX_MEM.memToReg = %d\n", this_exmem.memToReg);
		printf("     ---\n");
		printf("     EX_MEM.writeReg = %d\n", this_exmem.writeReg);
		printf("     EX_MEM.regWrite = %d\n", this_exmem.regWrite);
		printf("     ---\n");
		printf("     EX_MEM.aluResult = 0x%04x_%04x\n",
		             (this_exmem.aluResult >> 16) & 0xffff, this_exmem.aluResult & 0xffff);
		printf("\n");

		printf("  -- Old MEM/WB pipline fields --\n");
		printf("     MEM_WB.memToReg  = %d\n", ahead_memwb.memToReg);
		printf("     MEM_WB.aluResult = 0x%04x_%04x\n",
		             (ahead_memwb.aluResult >> 16) & 0xffff,
		             (ahead_memwb.aluResult      ) & 0xffff);
		printf("     MEM_WB.memResult = 0x%04x_%04x\n",
		             (ahead_memwb.memResult >> 16) & 0xffff,
		             (ahead_memwb.memResult      ) & 0xffff);
		printf("     ---\n");
		printf("     MEM_WB.writeReg = %d\n", ahead_memwb.writeReg);
		printf("     MEM_WB.regWrite = %d\n", ahead_memwb.regWrite);
		printf("\n");



		// now that we've initialized both of the instructions, use
		// standard functions to test the MEM logic which (sometimes)
		// will involve forwarding logic.

		printf("  -- NOW TESTING THE MEM PHASE --\n");
		Test_MEM(&this_exmem, &ahead_memwb,
		          mem, TEST_MEM_SIZE_WORDS,
		         &this_memwb);
	}

	return 0;
}

