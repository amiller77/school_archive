#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <unistd.h>    // for exit()

#include "sim5.h"
#include "sim5_test_commonCode.h"


int main()
{
	int i,j;

#define COUNT 15
	WORD  pcs[COUNT], instructions[COUNT];
	ID_EX out[COUNT];

	instructions[ 0] =  ADD(S_REG(0),  S_REG(1), S_REG(2));
	instructions[ 1] =  SUB(S_REG(3),  S_REG(4), S_REG(5));
	instructions[ 2] =  ADDI(T_REG(2), T_REG(3), -1);
	instructions[ 3] =  ADDI(T_REG(4), T_REG(5), 16);
	instructions[ 4] =  ADDU( 1,  0, 2);
	instructions[ 5] =  SUBU( 3,  4, 5);
	instructions[ 6] = ADDIU( 6,  7,0x1234);
	instructions[ 7] =   AND( 9, 10,11);
	instructions[ 8] =    OR(12, 13,14);
	instructions[ 9] =  ANDI(15, 16,0x5678);
	instructions[10] =   ORI(18, 19,0x9abc);
	instructions[11] =   SLT(21, 22,23);
	instructions[12] =  SLTI(24, 25,-1);
	instructions[13] =    LW(26,  0,104);
	instructions[14] =    SW(29,  0,108);
	instructions[15] =   NOP();


	/* for every *PAIR* of instruction in the list, (including
	 * duplicates), run them through the simulation, as if starting in an
	 * empty processor.  (We will *NOT* print out debug data for these
	 * steps.)
	 *
	 * Then with those two instructions sitting in the ID/EX and
	 * EX/MEM pipeline registers, set up a SW instruction (as if it were
	 * the third instruction in the sequence).  That is, the 'a'
	 * instruction is currently executing MEM, the 'b' instruction is
	 * currently executing EX, and the SW instruction is currently
	 * executing ID.  We need to know if the SW should stall or not.
	 *
	 * Try three different SW instructions, actually:
	 *     - where rt==writeReg of the instruction in ID/EX
	 *     - where rt==writeReg of the instruction in EX/MEM
	 *     - where rt==ZERO
	 *
	 * (Note that, since we allow duplicates, this handles several
	 * special cases, implicitly.)
	 */
	for (i=0; i<16; i++)
	for (j=0; j<16; j++)
	{
		printf("+-------------------------------------------+\n");
		printf("| STARTING TEST i=%d j=%d\n", i,j);
		printf("+-------------------------------------------+\n");

		WORD a,b;
		a = instructions[i];
		b = instructions[j];
		printf("  instruction A: 0x%08x\n", a);
		printf("  instruction B: 0x%08x\n", b);
		printf("\n");

		InstructionFields aFields, bFields;
		ID_EX             aIDEX,   bIDEX;
		EX_MEM            aEXMEM;
		  memset(&aFields, 0, sizeof(aFields));
		  memset(&bFields, 0, sizeof(bFields));
		  memset(&aIDEX,   0, sizeof(aIDEX));
		  memset(&bIDEX,   0, sizeof(bIDEX));
		  memset(&aEXMEM,  0, sizeof(aEXMEM));

		extract_instructionFields(a, &aFields);
		extract_instructionFields(b, &bFields);

		/* for simplicity, we won't even check for stalls or
		 * branches, except for stalling B.  Also note that we
		 * falsify the PC (it's useless for this testcase) and
		 * rs,rt values.
		 */
		execute_ID(0, &aFields, 
		           rand() & 0xfffffffc,   // PC
		           rand(),rand(),         // rsVal, rtVal
		           &aIDEX);

		/* the only stall that is possible, at this point in the
		 * code, is a LW stall - and the only LW we have above
		 * (at least, in the current code) writes to a register
		 * that is not read by any other instruction on the list.
		 * So this should always be zero.
		 */
		if (IDtoIF_get_stall(&bFields,
		                     &aIDEX,      // full
		                     &aEXMEM      // stall all zeroes
		                     ) != 0)
		{
			printf("**** TESTCASE ERROR ****\n");
			printf("This instruction pair stalled in the B instruction!!!\n");
			exit(1);
		}

		execute_ID(0, &bFields,
		           rand() & 0xfffffffc,
		           rand(),rand(),
		           &bIDEX);


		/* also, advance the A instruction through the EX phase
		 * Note that the ALU inputs I pass have *nothing* to do
		 * with the random rsVal,rtVal given earlier - except
		 * for the NOP instruciton, where I will hardcode 0/0
		 * just in case the student code cares.
		 */
		int alu1,alu2;
		if (a == NOP())
			alu1 = alu2 = 0;
		else
		{
			alu1 = rand();
			alu2 = rand();
		}

		execute_EX(&aIDEX, alu1,alu2, &aEXMEM);


		printf("Key pipeline registers when A is in the EX/MEM register, and B is in ID/EX:\n");
		printf("  ID_EX.regWrite = %d\n", bIDEX.regWrite);
		printf("  ID_EX.regDst   = %d\n", bIDEX.regDst);
		printf("  ID_EX.rt       = %d\n", bIDEX.rt);
		printf("  ID_EX.rd       = %d\n", bIDEX.rd);
		printf("  --\n");
		printf("  EX_MEM.regWrite = %d\n", aEXMEM.regWrite);
		printf("  EX_MEM.writeReg = %d\n", aEXMEM.writeReg);
		printf("\n");


		printf("The testcase will perform 3 stall checks with those two instructions: match-A, match-B, and a SW-stores-zero-register test.\n");
		printf("\n");

		int  aWriteReg,bWriteReg;
		WORD sw1,sw2,sw3;

		aWriteReg = aEXMEM.writeReg;

		if (bIDEX.regDst == 0)
			bWriteReg = bIDEX.rt;
		else
			bWriteReg = bIDEX.rd;

		sw1 = SW(aWriteReg,   0,112);
		sw2 = SW(bWriteReg,   0,116);
		sw3 = SW(0,           0,120);

		InstructionFields sw1_fields, sw2_fields, sw3_fields;
		  memset(&sw1_fields, 0, sizeof(sw1_fields));
		  memset(&sw2_fields, 0, sizeof(sw2_fields));
		  memset(&sw3_fields, 0, sizeof(sw3_fields));

		extract_instructionFields(sw1, &sw1_fields);
		extract_instructionFields(sw2, &sw2_fields);
		extract_instructionFields(sw3, &sw3_fields);


		if (aWriteReg == 0)
		{
			printf("Skipping Test 1 for this instruction combination, because A appears to \"write\" to register zero.\n");
		}
		else
		{
			printf("Test 1 (match A): a=0x%08x b=0x%08x sw1=0x%08x\n", a,b,sw1);
			printf("        stall? %d\n",
			       IDtoIF_get_stall(&sw1_fields,
			                        &bIDEX, &aEXMEM));
		}


		if (bWriteReg == 0)
		{
			printf("Skipping Test 2 for this instruction combination, because B appears to \"write\" to register zero.\n");
		}
		else
		{
			printf("Test 2 (match B): a=0x%08x b=0x%08x sw2=0x%08x\n", a,b,sw2);
			printf("        stall? %d\n",
			       IDtoIF_get_stall(&sw2_fields,
			                        &bIDEX, &aEXMEM));
		}


		if (aWriteReg == 0 || bWriteReg == 0)
		{
			printf("Skipping Test 3 for this instruction combination, because either A or B appear to \"write\" to register zero.\n");
		}
		else
		{
			printf("Test 3 (SW zero): a=0x%08x b=0x%08x sw3=0x%08x\n", a,b,sw3);
			printf("        stall? %d\n",
			       IDtoIF_get_stall(&sw3_fields,
			                        &bIDEX, &aEXMEM));
		}

		printf("\n");
	}

	return 0;
}


