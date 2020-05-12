/* Implementation of a 32-bit adder in C.
 *
 * Author: Alexander Miller
 *
 * Description:
 * one function that performs add unless a flag is turned on,
 * in which case it performs sub instead
 * must do this using bit operations
 *
 * Inputs: struct passed by pointer, Sim1Data, defined in sim1.h
 * a,b: 32-bit signed ints
 * flag: isSubraction -> tells us if we are performing subtraction
 * Outputs: same struct that was input
 * flag: aNonNeg -> indicates sign of input a
 * flag: bNonNeg -> indicates sign of input b [independent of add vs sub]
 * flag: sumNonNeg -> indicates sign of sum
 */


#include "sim1.h"

// NEGATIVITY CHECK (helper function)
// returns 1 if positive, 0 if negative
int negativityCheck(int x) {
    // x non-neg if its last bit is 0
    int msbX = (x>>31)&1;
    // ^ will be 1 if negative, 0 if positive
    if (msbX == 1) {
        return 0;
    } else {
        return 1;
    }
}

// EXECUTE ADD
void execute_add(Sim1Data *obj) {
    // copy in inputs:
    int a = obj->a;
    int b = obj->b;

    // configure input-related output flags:
    obj->aNonNeg = negativityCheck(a);
    obj->bNonNeg = negativityCheck(b);

    int carryBit;
    // determine add or subtraction:
    if (obj->isSubtraction == 1) {
        // do subtraction: flip carry bit on, take complement of b
        carryBit = 1;
        b = ~b;
    } else {
        // do addition: keep carry bit off, don't complement b
        carryBit = 0;
    }

    // perform addition:
    int sum = 0;
    for (int i = 0; i<32; i++) {
        // extract ith input bits:
        int aBit = (a>>i)&1;
        int bBit = (b>>i)&1;

        // if carry.. !STOPPED HERE!
        // carry, first, second
        // 1, 1, 1 -> carry bit 1, sum 1
        if (carryBit==1 && aBit==1 && bBit==1) {
            carryBit = 1;
            sum = sum | (1<<i);
        } // 0, 0, 0 -> carry bit 0, sum 0
        else if (carryBit==0 && aBit==0 && bBit==0) {
            carryBit = 0;
        } // 1, 1, 0 // 1, 0, 1 // 0, 1, 1 -> carry bit 1, sum 0
        else if ( (carryBit==1 && aBit==1 && bBit==0) ||
                (carryBit==1 && aBit==0 && bBit==1) ||
                (carryBit==0 && aBit==1 && bBit==1) ) {
            carryBit = 1;
        } // 1, 0, 0 // 0, 1, 0 // 0, 0, 1 -> carry bit 0, sum 1
        else if ( (carryBit==1 && aBit==0 && bBit==0) ||
                (carryBit==0 && aBit==1 && bBit==0) ||
                (carryBit==0 && aBit==0 && bBit==1) ) {
            carryBit = 0;
            sum = sum | (1<<i);
        }

    }

    // write-out the results
    obj->sum = sum;
    obj->sumNonNeg = negativityCheck(sum);
    obj->carryOut = carryBit;
    //obj->overflow = overflowCheck(&obj);

    // perform overflow check:
    // case 1: addition
    if (obj->isSubtraction == 0) {
        // if a, b negative and sum positive -> overflow
        if (obj->aNonNeg == 0 && obj->bNonNeg == 0 && obj->sumNonNeg == 1) {
            obj->overflow = 1;
        } // if a,b positive and sum negative -> overflow
        else if (obj->aNonNeg == 1 && obj->bNonNeg == 1 && obj->sumNonNeg == 0) {
            obj->overflow = 1;
        }
    } // case 2: subtraction
    else {
        // if a pos, b neg, and sum negative -> overflow
        if (obj->aNonNeg == 1 && obj->bNonNeg == 0 && obj->sumNonNeg == 0) {
            obj->overflow = 1;
        } // if a neg, b pos, and sum positive -> overflow
        else if (obj->aNonNeg == 0 && obj->bNonNeg == 1 && obj->sumNonNeg == 1) {
            obj->overflow = 1;
        }
    }
    obj->overflow = 0;

}
