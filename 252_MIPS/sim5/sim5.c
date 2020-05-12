/*
* File: sim5.c
* Author: Alexander Miller
*
*
*
*/

#include "sim5.h"

// ****** HELPER FUNCTIONS FOR ID **************

// VALIDATE OP
// returns 1 if good, 0 if invalid opcode/funct pair
int validateOp (InstructionFields* fields) {
    int validOpcodes[13] = {0,2,4,5,8,9,10,11,12,13,15,35,43};
    int validFuncts[11] = {0,32,33,34,35,36,37,38,39,42,43};
    if (fields->opcode == 0) {
        int i;
        for (i=0; i<11; i++) {
            if (fields->funct==validFuncts[i]) {
                return 1;
            }
        }
        return 0;
    } else {
        int k;
        for (k=0; k<13; k++) {
            if (fields->opcode==validOpcodes[k]) {
                return 1;
            }
        }
        return 0;
    }
}

// ********************************* ID *********************************

// EXTRACT INSTRUCTION FIELDS
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

// IDtoIF GET STALL
// Returns: 1 -> stall ID, 0-> else [including invalid opCode or funct]
// Params: fields of current instruction, ID/EX, EX/MEM
//      Do not edit any params
int IDtoIF_get_stall(InstructionFields *fields, ID_EX *old_idex, EX_MEM *old_exmem) {
    // validate opcode/funct
    if (validateOp(fields)==0) {
        return 0;
    }
    // case 1: LW directly ahead (check ID/EX)
    if (old_idex->memRead==1) {
        // if load is writing to our read, problem
        if (fields->rs == old_idex->rt) {
            return 1;
        }
        // if this instruction is R format, also have to check for conflict with rt
        if (fields->opcode == 0 && (fields->rs == old_idex->rt || fields->rt == old_idex->rt )) {
            return 1;
        }
    }

    /*
    // case 2: LW 2 steps ahead (check EX/MEM)
    if (old_exmem->memRead==1) {
        if (fields->rs == old_exmem.rt) {
            return 1;
        }
        // if this instruction is R format, also have to check for conflict with rt
        if (opcode == 0 && (fields->rs == old_exmem->rt || fields->rt == old_exmem->rt) {
            return 1;
        }
    }
    */

    // case 3: if we're a SW and an instruction 2 steps ahead writes to rs
    if (fields->opcode==43 && old_exmem->regWrite==1) {
        if (old_exmem->writeReg==fields->rs ) {
            return 1;
        }
    }

    return 0;
}

// ID to IF GET BRANCH CONTROL
// Returns: 0 -> PC advance as normal or stall, 1 -> PC jump to relative location,
//      2 -> PC jump to absolute location
int IDtoIF_get_branchControl(InstructionFields *fields, WORD rsVal, WORD rtVal) {

    int difference = rsVal - rtVal;
    // BEQ
    if (fields->opcode == 4) {
        if (difference == 0) {
            return 1;
        } else {
            return 0;
        }
    } // BNE
    else if (fields->opcode == 5) {
        if (difference != 0) {
            return 1;
        } else {
            return 0;
        }
    } // J
    else if (fields->opcode == 2) {
        return 2;
    } else {
        return 0;
    }
}

// CALC BRANCH ADDR
// calculate address that would be jumped to if conditional branch taken
WORD calc_branchAddr(WORD pcPlus4, InstructionFields *fields) {
    // add pc+4 , imm32 << 2
    return (pcPlus4 + (fields->imm32 << 2));
}

// CALC JUMP ADDR
// calculate branch would jump to if unconditional branch taken
WORD calc_jumpAddr (WORD pcPlus4, InstructionFields *fields) {
    // get top 4 bits from pc
    int result = pcPlus4 & 0xf0000000;
    // add in the address << 2
    result = result | (fields->address << 2);
    return result;
}

// EXECUTE ID
// Param IDstall : return from IDtoIF_get_stall
// Behavior: decode opcode, funct; set fields of ID_EX
// Returns: 1 -> all good, 0-> invalid opcode/funct
int execute_ID(int IDstall, InstructionFields *in, WORD pcPlus4,
    WORD rsVal, WORD rtVal, ID_EX *out) {
    // validate opcode/funct
    if (validateOp(in)==0) {
        return 0;
    }

    // set control bits to 0 by default
    out->ALUsrc=0;
    out->ALU.bNegate=0;
    out->ALU.op=0;
    out->memRead=0;
    out->memWrite=0;
    out->memToReg=0;
    out->regDst=0;
    out->regWrite=0;
    out->extra1=0;
    out->extra2=0;
    out->extra3=0;

    // handle stall, fill non-control fields
    if (IDstall==1) {
        out->rs=0;
        out->rt=0;
        out->rd=0;
        out->rsVal=0;
        out->rtVal=0;
        out->imm16=0;
        out->imm32=0;
        return 1;
    } // pass vals from fieldsIn and params
    else {
        out->rs=in->rs;
        out->rt=in->rt;
        out->rd=in->rd;
        out->rsVal=rsVal;
        out->rtVal=rtVal;
        out->imm16=in->imm16;
        out->imm32=in->imm32;
    }

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
        } // nor
        else if (in->funct==39) {
            out->regDst = 1;
            out->regWrite = 1;
            out->extra1 = 1;
        }
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
        } // lui
        else if (in->opcode == 15) {
            out->ALUsrc = 1;
            out->regWrite = 1;
            out->ALU.op = 2;
            out->memToReg = 1;
            out->memRead = 1;
            out->extra2 = 1;
        } // beq
        else if (in->opcode == 4) {
            out->ALU.op = 2;
            out->ALU.bNegate = 1;
        } // bne
        else if (in->opcode == 5) {
            out->ALU.op = 2;
            out->ALU.bNegate = 1;
            out->extra1 = 1;
        } // sw
        else if (in->opcode == 43) {
            out->ALUsrc = 1;
            out->ALU.op = 2;
            out->memWrite = 1;
        }  // andi
        else if(in->opcode == 12) {
            out->ALUsrc = 2;
            out->regWrite = 1;
            out->extra3 = 1;
        } // ori
        else if (in->opcode == 13) {
            out->ALU.op =1;
            out->regWrite = 1;
            out->ALUsrc = 2;
            out->extra3 = 1;
        }
    }
    return 1;
}


// ********************************* EX *********************************

// EX GET ALU INPUT 1
// returns 1st input to ALU
WORD EX_getALUinput1(ID_EX* in, EX_MEM* old_exMem, MEM_WB* old_memWb) {
    WORD returnVal = 0;
    int rvFound = 0;
    // note that for MEM_WB,
    // check MEM_WB to see if we need to pull rs from there (mem case)
    if (old_memWb->regWrite == 1 && old_memWb->writeReg == in->rs) {
        // if mem read:
        if (old_memWb->memToReg==1) {
            returnVal = old_memWb->memResult;
            rvFound = 1;
        } // if aluResult:
        else {
            returnVal = old_memWb->aluResult;
            rvFound = 1;
        }
    }

    // do this second to ensure we're getting the most current info
    // check EX_MEM to see if we need to pull rs from there
    if (old_exMem->regWrite == 1 && old_exMem->writeReg == in->rs && old_exMem->memToReg == 0) {
        returnVal = old_exMem->aluResult;
        rvFound = 1;
    }

    if (rvFound) {
        return returnVal;
    } else {
        return in->rs;
    }
}

// EX GET ALU INPUT 2
// returns 2nd input to ALU
WORD EX_getALUinput2(ID_EX* in, EX_MEM* old_exMem, MEM_WB* old_memWb) {

    // handle andi, ori by not using the sign-extended value:
    if (in->extra3 == 1 && in->ALUsrc == 1) {
        return in->imm16;
    }
    // general immediate case, if we need immediate field, take it
    if (in->ALUsrc == 1) {
        return in->imm32;
    }

    // otherwise, need rt; check for conflicts
    WORD returnVal = 0;
    int rvFound = 0;
    // note that for MEM_WB,
    // check MEM_WB to see if we need to pull rs from there (mem case)
    if (old_memWb->regWrite == 1 && old_memWb->writeReg == in->rt) {
        // if mem read:
        if (old_memWb->memToReg==1) {
            returnVal = old_memWb->memResult;
            rvFound = 1;
        } // if aluResult:
        else {
            returnVal = old_memWb->aluResult;
            rvFound = 1;
        }
    }

    // do this second to ensure we're getting the most current info
    // check EX_MEM to see if we need to pull rs from there
    if (old_exMem->regWrite == 1 && old_exMem->writeReg == in->rt && old_exMem->memToReg == 0) {
        returnVal = old_exMem->aluResult;
        rvFound = 1;
    }

    if (rvFound) {
        return returnVal;
    } else {
        return in->rt;
    }
}

// EXECUTE EX
/*
* ALU Control Bits:
*   0 - AND
*   1 - OR
*   2 - ADD
*   3 - LESS
*   4 - XOR
*/
void execute_EX(ID_EX* in, WORD input1, WORD input2, EX_MEM* new_exMem) {

    // pass over control bits
    new_exMem->rtVal = in->rtVal;
    new_exMem->rt = in->rt;
    new_exMem->memRead = in->memRead;
    new_exMem->memWrite = in->memWrite;
    new_exMem->memToReg = in->memToReg;
    new_exMem->regWrite = in->regWrite;
    new_exMem->extra1 = in->extra1;
    new_exMem->extra2 = in->extra2;
    new_exMem->extra3 = in->extra3;
    if (in->regDst == 0) {
        new_exMem->writeReg = in->rt;
    } else {
        new_exMem->writeReg = in->rd;
    }


    // process bNegate:
    if (in->ALU.bNegate == 1) {
        input2 = -input2;
    }
    // AND
    if (in->ALU.op==0) {
        new_exMem->aluResult = input1 & input2;
    } // OR
    else if (in->ALU.op==1) {
        new_exMem->aluResult = input1 | input2;
    } // ADD
    else if (in->ALU.op==2) {
        new_exMem->aluResult = input1 + input2;
    } // LESS
    else if (in->ALU.op==3){
        int tempSum = input1 + input2;
        if (tempSum < 0) {
            new_exMem->aluResult = 1;
        } else {
            new_exMem->aluResult = 0;
        }
    }// XOR
    else if (in->ALU.op==4) {
        new_exMem->aluResult = input1 ^ input2;
    }

    // add nor functionality
    if (in->extra1 == 1) {
        new_exMem->aluResult = ! new_exMem->aluResult;
    }

    /*
    // add branchNot functionality
    if (controlIn->extra1 == 1) {
        aluResultOut->zero = !aluResultOut->zero;
    }
    */

}

// ********************************* MEM *********************************
void execute_MEM(EX_MEM* in, MEM_WB* old_memWb, WORD* mem, MEM_WB* new_memWb) {

    // pass over results from in
    new_memWb->memToReg = in->memToReg;
    new_memWb->aluResult = in->aluResult;
    new_memWb->writeReg = in->writeReg;
    new_memWb->regWrite = in->regWrite;
    new_memWb->extra1 = in->extra1;
    new_memWb->extra2 = in->extra2;
    new_memWb->extra3 = in->extra3;

    // see if we need to forward value to write [SW]
    WORD writeValue = in->rtVal;
    if (old_memWb->regWrite == 1 && old_memWb->writeReg == in->rt) {
        writeValue = old_memWb->aluResult;
    }

    // calculate address in words
    int wordAddress = in->aluResult / 4;

    // READ
    if (in->memRead == 1) {
        new_memWb->memResult = *(mem+wordAddress);
    } // WRITE
    else if (in->memWrite == 1) {
        *(mem+wordAddress) = writeValue;
    }

    // if lui instruction, mask out the highest 16 bits, shift 'em over
    if (in->extra2 == 1) {
        new_memWb->memResult = new_memWb->memResult & 0x0000ffff;
        new_memWb->memResult = new_memWb->memResult << 16;
    }
}

// ********************************* WB *********************************
void execute_WB (MEM_WB* in, WORD* regs) {
    // validate op:
    if (in->regWrite == 0) {
        return;
    }

    // if dealing w/ mem result:
    if (in->memToReg == 1) {
        *(regs+in->writeReg) = in->memResult;
    } // alu result:
    else {
        *(regs+in->writeReg) = in->aluResult;
    }
}

//
