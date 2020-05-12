#include <stdio.h>
#include <memory.h>

#include "sim5.h"
#include "sim5_test_commonCode.h"



int main()
{
#define CODE_SIZE (16*1024)
#define DATA_SIZE (16*1024)
	WORD regs[34];
	WORD instMemory[CODE_SIZE];
	WORD dataMemory[DATA_SIZE];

	// fill in the registers and data memory with some good default values
	int i;
	for (i=0; i<34; i++)
		regs[i] = 0x01010101 * i;
	for (i=0; i<sizeof(dataMemory); i+=4)
		dataMemory[i/4] = 0xffff0000 + i;


	//	addi	$t0, $zero, 1234    # t0 = 1234
	//	addi	$t1, $zero, 5678    # t1 = 5678
	//	addi	$t2, $zero, 1111    # t2 = 1111
	//	addi	$t3, $zero, 2222    # t3 = 2222
	//
	//	lui	$s0, $zero, 1       # s0 = (   1 << 16)
	//	lui	$s1, $zero, 17      # s0 = (  17 << 16)
	//	lui	$s2, $zero, 1000    # s0 = (1000 << 16)
	//	lui	$s3, $zero, 1234    # s0 = (1234 << 16)

	
	WORD codeOffset = 0x12340000;

	// init
	instMemory[ 0] = ADDI(T_REG(0), REG_ZERO,1234);
	instMemory[ 1] = ADDI(T_REG(1), REG_ZERO,5678);
	instMemory[ 2] = ADDI(T_REG(2), REG_ZERO,1111);
	instMemory[ 3] = ADDI(T_REG(3), REG_ZERO,2222);

	// instruction test
	instMemory[ 4] = LUI(S_REG(0), 1);
	instMemory[ 5] = LUI(S_REG(1), 17);
	instMemory[ 6] = LUI(S_REG(2), 1000);
	instMemory[ 7] = LUI(S_REG(3), 1234);

	// print_int(t0)
	instMemory[ 8] = ADDI(V_REG(0), REG_ZERO,1);
	instMemory[ 9] = ADD (A_REG(0), T_REG(0), REG_ZERO);
	instMemory[10] = NOP();
	instMemory[11] = NOP();
	instMemory[12] = SYSCALL();

	// print_char('\n')
	instMemory[13] = ADDI(V_REG(0), REG_ZERO,11);
	instMemory[14] = ADDI(A_REG(0), REG_ZERO,0xa);
	instMemory[15] = NOP();
	instMemory[16] = NOP();
	instMemory[17] = SYSCALL();

	// print_int(t1)
	instMemory[18] = ADDI(V_REG(0), REG_ZERO,1);
	instMemory[19] = ADD (A_REG(0), T_REG(1), REG_ZERO);
	instMemory[20] = NOP();
	instMemory[21] = NOP();
	instMemory[22] = SYSCALL();

	// print_char('\n')
	instMemory[23] = ADDI(V_REG(0), REG_ZERO,11);
	instMemory[24] = ADDI(A_REG(0), REG_ZERO,0xa);
	instMemory[25] = NOP();
	instMemory[26] = NOP();
	instMemory[27] = SYSCALL();

	// print_int(t2)
	instMemory[28] = ADDI(V_REG(0), REG_ZERO,1);
	instMemory[29] = ADD (A_REG(0), T_REG(2), REG_ZERO);
	instMemory[30] = NOP();
	instMemory[31] = NOP();
	instMemory[32] = SYSCALL();

	// print_char('\n')
	instMemory[33] = ADDI(V_REG(0), REG_ZERO,11);
	instMemory[34] = ADDI(A_REG(0), REG_ZERO,0xa);
	instMemory[35] = NOP();
	instMemory[36] = NOP();
	instMemory[37] = SYSCALL();

	// print_int(t3)
	instMemory[38] = ADDI(V_REG(0), REG_ZERO,1);
	instMemory[39] = ADD (A_REG(0), T_REG(3), REG_ZERO);
	instMemory[40] = NOP();
	instMemory[41] = NOP();
	instMemory[42] = SYSCALL();

	// print_char('\n')
	instMemory[43] = ADDI(V_REG(0), REG_ZERO,11);
	instMemory[44] = ADDI(A_REG(0), REG_ZERO,0xa);
	instMemory[45] = NOP();
	instMemory[46] = NOP();
	instMemory[47] = SYSCALL();

	// print_int(s0)
	instMemory[48] = ADDI(V_REG(0), REG_ZERO,1);
	instMemory[49] = ADD (A_REG(0), S_REG(0), REG_ZERO);
	instMemory[50] = NOP();
	instMemory[51] = NOP();
	instMemory[52] = SYSCALL();

	// print_char('\n')
	instMemory[53] = ADDI(V_REG(0), REG_ZERO,11);
	instMemory[54] = ADDI(A_REG(0), REG_ZERO,0xa);
	instMemory[55] = NOP();
	instMemory[56] = NOP();
	instMemory[57] = SYSCALL();

	// print_int(s1)
	instMemory[58] = ADDI(V_REG(0), REG_ZERO,1);
	instMemory[59] = ADD (A_REG(0), S_REG(1), REG_ZERO);
	instMemory[60] = NOP();
	instMemory[61] = NOP();
	instMemory[62] = SYSCALL();

	// print_char('\n')
	instMemory[63] = ADDI(V_REG(0), REG_ZERO,11);
	instMemory[64] = ADDI(A_REG(0), REG_ZERO,0xa);
	instMemory[65] = NOP();
	instMemory[66] = NOP();
	instMemory[67] = SYSCALL();

	// print_int(s2)
	instMemory[68] = ADDI(V_REG(0), REG_ZERO,1);
	instMemory[69] = ADD (A_REG(0), S_REG(2), REG_ZERO);
	instMemory[70] = NOP();
	instMemory[71] = NOP();
	instMemory[72] = SYSCALL();

	// print_char('\n')
	instMemory[73] = ADDI(V_REG(0), REG_ZERO,11);
	instMemory[74] = ADDI(A_REG(0), REG_ZERO,0xa);
	instMemory[75] = NOP();
	instMemory[76] = NOP();
	instMemory[77] = SYSCALL();

	// print_int(s3)
	instMemory[78] = ADDI(V_REG(0), REG_ZERO,1);
	instMemory[79] = ADD (A_REG(0), S_REG(3), REG_ZERO);
	instMemory[80] = NOP();
	instMemory[81] = NOP();
	instMemory[82] = SYSCALL();

	// print_char('\n')
	instMemory[83] = ADDI(V_REG(0), REG_ZERO,11);
	instMemory[84] = ADDI(A_REG(0), REG_ZERO,0xa);
	instMemory[85] = NOP();
	instMemory[86] = NOP();
	instMemory[87] = SYSCALL();


	// exit()
	instMemory[88] = ADDI(V_REG(0), REG_ZERO,10);
	instMemory[89] = NOP();
	instMemory[90] = NOP();
	instMemory[91] = SYSCALL();



	ExecProcessor(instMemory, CODE_SIZE,
	              regs,
	              dataMemory, DATA_SIZE,
	              codeOffset);

	return 0;
}


