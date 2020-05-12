/*
* File: sim4.c
* Author: Alexander Miller
* Description: simulates a CPUs varies functionalities in respective functions
* ALU Control Bits:
*   0 - AND
*   1 - OR
*   2 - ADD
*   3 - LESS
*   4 - XOR
*
*/

#include "sim4.h"


/*
* GET INSTRUCTION
* Inputs:
*   curPC: address of current instruction in bytes
*   instructionMemory: array of WORDs
*/
WORD getInstruction(WORD curPC, WORD *instructionMemory) {
    int pcWords = curPC/4;
    return *(instructionMemory+pcWords);
}


/*
* EXTRACT INSTRUCTION FIELDS
* takes a 32 bit instruction, then extracts the fields
* and places them in the InstructionFields struct
* opcode:   26:31   [6 bits]
* rs:       21:25   [5 bits]
* rt:       16:20   [5 bits]
* rd:       11:15   [5 bits]
* shamt:    6:10    [5 bits]
* funct:    0:5     [6 bits]
* imm16:    0:15    [16 bits]
* imm32:    (sign extended version of imm16)
* address:	0:25 [J-format]  [26 bits]
*/
void extract_instructionFields(WORD in, InstructionFields *out) {
    out->opcode = (in >> 26) & 0x3f;
    out->rs = (in >> 21) & 0x1f;
    out->rt = (in >> 16) & 0x1f;
    out->rd = (in >> 11) & 0x1f;
    out->shamt = (in >> 6) & 0x1f;
    out->funct = in & 0x3f;
    out->imm16 = in & 0xffff;
    out->imm32 = signExtend16to32(out->imm16);
    out->address = in & 0x3ffffff;
}

/*
* FILL CPU CONTROL
* Returns: if opcode is invalid, or if it indicates R-format
*   but funct field is invalid, then returns 0, else returns 1
*
* CPUControl:
* int ALUsrc;
* struct {
*    int op;
*    int bNegate;
* } ALU;
* int memRead;
* int memWrite;
* int memToReg;
* int regDst;
* int regWrite;
* int branch;
* int jump;
*
* Supported Instructions:
*   Note: any u operation, just treat like normal signed counterpart
* R Format Instructions:
*   add, addu, sub, subu, and, or, xor, slt
* I Format Instructions:
*   addi, addiu, slti, lw, sw, beq
* J Format Instructions:
*   j
*/
int fill_CPUControl(InstructionFields* in, CPUControl* out) {
    // set all to 0 by default.
    out->ALUsrc = 0;
    out->ALU.op = 0;
    out->ALU.bNegate = 0;
    out->memRead = 0;
    out->memWrite = 0;
    out->memToReg = 0;
    out->regDst = 0;
    out->regWrite = 0;
    out->branch = 0;
    out->jump = 0;
    out->extra1 = 0;
    out->extra2 = 0;
    out->extra3 = 0;
    // R FORMAT
    if (in->opcode == 0) {
        // add, addu
        if (in->funct==32 || in->funct==33) {
            out->ALU.op = 2;
            out->regDst = 1;
            out->regWrite = 1;
        } // sub, subu
        else if (in->funct==34 || in->funct==35) {
            out->ALU.op = 2;
            out->ALU.bNegate = 1;
            out->regDst = 1;
            out->regWrite = 1;
        } // slt
        else if (in->funct==42) {
            out->ALU.op = 3;
            out->ALU.bNegate = 1;
            out->regDst = 1;
            out->regWrite = 1;
        } // and
        else if (in->funct==36) {
            out->regDst = 1;
            out->regWrite = 1;
        } // or
        else if (in->funct==37) {
            out->ALU.op = 1;
            out->regDst = 1;
            out->regWrite = 1;
        } // xor
        else if (in->funct==38) {
            out->ALU.op = 4;
            out->regDst = 1;
            out->regWrite = 1;
        } // unrecognized R instruction
        else {
            return 0;
        }
    } // J FORMAT
    else if (in->opcode == 2) {
        out->jump = 1;
    } // I FORMAT
    else {
        // addi, addiu
        if (in->opcode == 8 || in->opcode == 9) {
            out->ALUsrc = 1;
            out->regWrite = 1;
            out->ALU.op = 2;
        } // slti
        else if (in->opcode == 10) {
            out->ALUsrc = 1;
            out->regWrite = 1;
            out->ALU.op = 3;
            out->ALU.bNegate = 1;
        } // lw
        else if (in->opcode == 35) {
            out->ALUsrc = 1;
            out->regWrite = 1;
            out->ALU.op = 2;
            out->memToReg = 1;
            out->memRead = 1;
        } // sw
        else if (in->opcode == 43) {
            out->ALUsrc = 1;
            out->ALU.op = 2;
            out->memWrite = 1;
        } // beq
        else if (in->opcode == 4) {
            out->ALU.op = 2;
            out->ALU.bNegate = 1;
            out->branch = 1;
        } // bne
        else if (in->opcode == 5) {
            out->ALU.op = 2;
            out->ALU.bNegate = 1;
            out->branch = 1;
            out->extra1 = 1;
        } // andi
        else if(in->opcode == 12) {
            out->ALUsrc = 1;
            out->regWrite = 1;
            out->extra3 = 1;
        } // lb
        else if(in->opcode == 32) {
            out->ALUsrc = 1;
            out->regWrite = 1;
            out->ALU.op = 2;
            out->memToReg = 1;
            out->memRead = 1;
            out->extra2 = 1;
        } // not recognized
        else {
            return 0;
        }
    }
    return 1;
}

/*
* GET ALU INPUT 1
*/
WORD getALUinput1(CPUControl *controlIn, InstructionFields *fieldsIn,
    WORD rsVal, WORD rtVal, WORD reg32, WORD reg33, WORD oldPC) {
    return rsVal;
}

/*
* GET ALU INPUT 2
*/
WORD getALUinput2(CPUControl *controlIn, InstructionFields *fieldsIn,
    WORD rsVal, WORD rtVal, WORD reg32, WORD reg33, WORD oldPC) {

    // handle andi by not using the sign-extended value:
    if (controlIn->extra3 == 1 && controlIn->ALUsrc == 1) {
        return fieldsIn->imm16;
    }
    // general case, if we need immediate field, take it
    if (controlIn->ALUsrc == 1) {
        return fieldsIn->imm32;
    }
    else {
        return rtVal;
    }
}

/*
* EXECUTE ALU
* ALU Control Bits:
*   0 - AND
*   1 - OR
*   2 - ADD
*   3 - LESS
*   4 - XOR
*/
void execute_ALU(CPUControl *controlIn, WORD input1, WORD input2,
    ALUResult  *aluResultOut) {
    // process bNegate:
    if (controlIn->ALU.bNegate == 1) {
        input2 = -input2;
    }
    // AND
    if (controlIn->ALU.op==0) {
        aluResultOut->result = input1 & input2;
    } // OR
    else if (controlIn->ALU.op==1) {
        aluResultOut->result = input1 | input2;
    } // ADD
    else if (controlIn->ALU.op==2) {
        aluResultOut->result = input1 + input2;
    } // LESS
    else if (controlIn->ALU.op==3){
        int tempSum = input1 + input2;
        if (tempSum < 0) {
            aluResultOut->result = 1;
        } else {
            aluResultOut->result = 0;
        }
    }// XOR
    else if (controlIn->ALU.op==4) {
        aluResultOut->result = input1 ^ input2;
    }
    // set aluResultOut->zero
    if (aluResultOut->result == 0) {
        aluResultOut->zero = 1;
    } else {
        aluResultOut->zero = 0;
    }
    // add branchNot functionality
    if (controlIn->extra1 == 1) {
        aluResultOut->zero = !aluResultOut->zero;
    }
}

/*
* EXECUTE MEM
*/
void execute_MEM(CPUControl *controlIn, ALUResult  *aluResultIn,
    WORD rsVal, WORD rtVal, WORD *memory, MemResult  *resultOut) {
    // set resultOut to 0 by default, change if we read
    resultOut->readVal = 0;

    // calculate address in words
    int wordAddress;
    int byteOffset;
    // general case:
    if (controlIn->extra2 != 1) {
        wordAddress = aluResultIn->result / 4;
    } // lb case:
    else {
        // need to remove the remainder above the interval of 4 to load begin. of word
        // address = ( byteAddress - (byteAddress % 4) )/4;
        byteOffset = aluResultIn->result % 4;
        wordAddress = (aluResultIn->result - byteOffset)/4;
    }

    // READ
    if (controlIn->memRead == 1) {
        resultOut->readVal = *(memory+wordAddress);
    } // WRITE
    else if (controlIn->memWrite == 1) {
        *(memory+wordAddress) = rtVal;
    }

    // if lb instruction, reduce data to 1 byte
    if (controlIn->extra2 == 1) {
        // if signBit == 1, neg; else pos
        int signBit = (resultOut->readVal >> 31) & 0x1;
        int mask = 0xff;
        if (byteOffset == 0) {
            resultOut->readVal = resultOut->readVal & mask;
        } else if (byteOffset == 1) {
            resultOut->readVal = (resultOut->readVal>>8) & mask;
        } else if (byteOffset == 2) {
            resultOut->readVal = (resultOut->readVal>>16) & mask;
        } else if (byteOffset == 3) {
            resultOut->readVal = (resultOut->readVal>>24) & mask;
        }
        // sign extend if neg sign bit:
        if (signBit == 1) {
            resultOut->readVal = resultOut->readVal | 0xffffff00;
        }
    }
}

/*
* GET NEXT PC
*/
WORD getNextPC(InstructionFields *fields, CPUControl *controlIn,
    int aluZero, WORD rsVal, WORD rtVal, WORD oldPC) {
    // jump case:
    if (controlIn->jump == 1) {
        // get top 4 bits from pc
        int newAddress = (oldPC >> 28) & 0xf;
        newAddress = newAddress << 28;
        // merge with a shifted left 26-bit address
        newAddress = newAddress + (fields->address << 2);
        return newAddress;
    }

    // branch case:
    if (controlIn->branch == 1 && aluZero == 1) {
        return oldPC + 4 + (fields->imm32 << 2);
    }

    // basic case: increment by 4
    return oldPC + 4;
}

/*
* EXECUTE UPDATE REGS
*/
void execute_updateRegs(InstructionFields *fields,
    CPUControl *controlIn, ALUResult *aluResultIn,
    MemResult *memResultIn, WORD *regs) {

    // validate op:
    if (controlIn->regWrite == 0) {
        return;
    }

    // determine the write register: rt or rd
    int writeRegister;
    if (controlIn->regDst == 0) {
        writeRegister = fields->rt;
    }
    else {
        writeRegister = fields->rd;
    }

    // if dealing w/ mem result:
    if (controlIn->memToReg == 1) {
        *(regs+writeRegister) = memResultIn->readVal;
    } // alu result:
    else {
        *(regs+writeRegister) = aluResultIn->result;
    }

}
