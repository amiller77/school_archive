/*
* File: strmath.c
* Author: Alexander Miller
* Input:
*   4 strings, one per line from stdin
*   op: add OR sub
*   str1, str2:
*       ^ sequence of decimal digits (arbitrarily long, arbit. many leading 0s)
*       ^ rightmost digits the least significant
*       ^ will be non-negative (b/c seq. of dec. digits)
* Output: printf("%s\n", output);
*   no extraneous leading 0s or whitespace
* Error conditions:
*   first string not in {"add","sub"}
*   not enough input strings
*   lines containing only whitespace
*   non-numeric chars in strings 2, 3
*/

#include <stdio.h>
#include <stdlib.h>

/*
* VALIDATE INPUT
* Inputs:
*   inputType: 0->"op", 1->"a" or "b"
*   storage: pointer to array to validate
*   operation: pointer to integer (val == 0) -> add, (val == 1) -> sub
* Post-Conditions: if inputType == 0, operation holds correct encoding
*   Any invalid input causes the program to exit irregularly
* Returns: true size of "a" or "b" (amount that actually contains data) or 0 if "op" input type
*/
int validateInput(int inputType, char *storage, int *operation) {
    int returnValue = 0;
    // validate "op"
    if (inputType == 0) {
        // check for 'add\n'
        if (*storage=='a' &&
            *(storage+1)=='d' && *(storage+2)=='d' &&
            *(storage+3)=='\n' && *(storage+4)=='\0' )
            {
            *operation = 0;
        }
        // check for 'sub\n'
        if (*storage=='s' &&
            *(storage+1)=='u' && *(storage+2)=='b' &&
            *(storage+3)=='\n' && *(storage+4)=='\0' )
            {
            *operation = 1;
        }
        // validate the op:
        if (*operation!=0 && *operation!=1) {
            fprintf(stderr,"Error: invalid operation given\n");
            exit(1);
        }
    } // validate "a" or "b"
    else {
        // first check for blank line
        if ( (*storage == '\n') && (*(storage+1)=='\0') ) {
            fprintf(stderr,"Error: blank line given\n");
        }
        int i = 0;
        int flag = 1;
        while (flag == 1) {
            char val = *(storage+i);
            // if null char, mark end of string
            if (val == '\0') {
                returnValue = i+1;
                break;
            }
            // ensure only ASCII string digits or newline occur
            if ( (val <= 47 || val >=58) && (val != 10) ){
                fprintf(stderr,"Error: non-integer chars input\n");
                exit(1);
            }
            i++;
        }
    }
    return returnValue;
}

/*
* MAP CHAR TO INT
* returns the int val of an ASCII int or -1 if char is not an ASCII int
*/
int mapCharToInt(char x) {
    if (x>=48 && x<=57) {
        return (x-48);
    } else {
        return -1;
    }
}

/*
* FIND LARGER INPUT
* returns 0 if first param is greater than or equal, 1 if second param is larger
* does this by finding first non-zero char in each array, and compares their values
* and positions relative to the end of the string
*/
int findLargerInput(char *a, char *b, int sizeA, int sizeB) {
    int smallerArraySize;
    if (sizeA <= sizeB) {
        smallerArraySize = sizeA;
    } else {
        smallerArraySize = sizeB;
    }
    int i;
    int aVal = 0;
    int aValWeight = 0;
    int bVal = 0;
    int bValWeight = 0;
    for (i=0;i<smallerArraySize;i++) {
        // ith elements in A and B
        int newAVal = mapCharToInt( *(a+i) );
        int newBVal = mapCharToInt( *(b+i) );

        // gather first non-zero inputs of a,b:
        if (aVal == 0 && newAVal != 0) {
            aVal = newAVal;
            aValWeight = sizeA - i;
        }
        if (bVal == 0 && newBVal != 0) {
            bVal = newBVal;
            bValWeight = sizeB - i;
        }
        // if we have 2 non-zero vals:
        if (aVal != 0 && bVal !=0) {
            // if equal weights compare vals:
            if (aValWeight == bValWeight) {
                // if equal vals as well, discard and continue
                if (aVal == bVal) {
                    aVal = 0;
                    bVal = 0;
                    aValWeight = 0;
                    bValWeight = 0;
                } // if non-equal vals, one is larger:
                else if (aVal >= bVal) {
                    return 0;
                } else {
                    return 1;
                }
            } // if non-equal weights, the choice is simple:
            else if (aValWeight > bValWeight) {
                return 0;
            } // aValWeight < bValWeight
            else {
                return 1;
            }
        }
    }
    // in this case alg failed, maybe one input is zero
    // return the one which has a valid value
    if (aVal == 0 && bVal != 0) {
        return 1;
    } else {
        return 0;
    }
}

/*
* PERFORM OP
* iterate backwards over inputs, write to output
*/
void performOp(int operationType, char *a, char *b, int *c,
    int aTrueSize, int bTrueSize, int cSize, int invert) {
    // find shorter input
    int longerArrayLength;
    char *longerArray = NULL;
    if (aTrueSize <= bTrueSize) {
        longerArrayLength = bTrueSize;
        longerArray = b;
    } else {
        longerArrayLength = aTrueSize;
        longerArray = a;
    }
    // iterate over input arrays backwards
    int i;
    int carry = 0;
    for (i=0; i<longerArrayLength; i++){
        char *aReverseOffset = a + (aTrueSize - 1) - i;
        char *bReverseOffset = b + (bTrueSize - 1) - i;
        int *cReverseOffset = c + (cSize - 1) - i;
        // if we ran off the end of the shorter array, just give it a zero
        int aArg;
        int bArg;
        if (longerArray == b && aReverseOffset < a) {
            aArg = 0;
            bArg = mapCharToInt(*bReverseOffset);
        } else if (longerArray == a && bReverseOffset < b) {
            aArg = mapCharToInt(*aReverseOffset);
            bArg = 0;
        } else {
            aArg = mapCharToInt(*aReverseOffset);
            bArg = mapCharToInt(*bReverseOffset);
        }

        // if non-int args, use -1 flag in C to denote non-output val
        if (aArg == -1 || bArg == -1) {
            *cReverseOffset = -1;
            carry = 0;
            continue;
        }

        // otherwise, calculate sum
        int sum;
        if (operationType == 0) {
            sum = aArg + bArg + carry;
        } // or calculate difference
        else {
            sum = carry + aArg - bArg;
        }

        // determine column sum and carry value
        if (sum < 10 && sum >= 0) {
            *cReverseOffset = sum;
            carry = 0;
        } else if (sum>=10){
            *cReverseOffset = sum - 10;
            carry = 1;
        } else { // else sum<0
            *cReverseOffset = sum + 10;
            carry = -1;
        }
    }
    // since C is one longer than the longest value,
    // now we just set C[0] to carry if A didn't invert
    //  if it did invert, then set to -1 to indicate negative result
    if (invert==1) {
        *c = -1;
    } else {
        *c = carry;
    }
}

/*
* GENERATE OUTPUT
* prints output (stored in c)
*/
void generateOutput(int *c, int cSize) {
    // first char of C is an indicator of positive or negative
    // if the first val is not the indicator, could be a valid print integer or 0
    int iInitial = 0;
    if (*c == -1) {
        printf("-");
        iInitial = 1;
    }
    int i;
    int firstCharFound = 0;
    for (i=iInitial;i<cSize;i++) {
        int *cOffset = c+i;
        // skip initial zeros:
        if (firstCharFound == 0 && *cOffset ==0) {
            continue;
        } // hit end of non-zero number:
        if (*cOffset < 0 && firstCharFound == 1) {
            printf("\n");
            return;
        } // turns out C is 0:
        else if (*cOffset<0 && firstCharFound == 0) {
            printf("0\n");
            return;
        } // print out normal digit:
        else {
            firstCharFound = 1;
            printf("%d",*cOffset);
        }
    }
}

/*
* MAIN
* main control flow of program: gathers ops, validates them with validateInput(),
*   allocates output buffer, determines if operands need to be inverted
*   by calling findLargerInput(), runs ops by calling performOp(), generates
*   output by calling generateOutput()
*/
int main() {
    int terminationCondition = 0;

    // INITIALIZE VARIABLES:
    // storage for strings:
    char *op = NULL;
    char *a = NULL;
    char *b = NULL;
    unsigned long opSize = 0;
    unsigned long aSize = 0;
    unsigned long bSize = 0;
    // storage for operation type (0->add, 1->sub)
    int operation = -1;
    // storage for true arg sizes (size of string rather than its containing buffer)
    int aTrueSize;
    int bTrueSize;

    // GATHER, VALIDATE OP:
    int result = getline(&op,&opSize,stdin);
    if (result == 0 || result == -1) {
        fprintf(stderr,"Error: not enough inputs\n");
        exit(1);
    }
    validateInput(0,op,&operation);

    // GATHER, VALIDATE A:
    result = getline(&a,&aSize,stdin);
    if (result == 0 || result == -1) {
        fprintf(stderr,"Error: not enough inputs\n");
        exit(1);
    }
    aTrueSize = validateInput(1,a,&operation);

    // GATHER, VALIDATE B:
    result = getline(&b,&bSize,stdin);
    if (result == 0 || result == -1) {
        fprintf(stderr,"Error: not enough inputs\n");
        exit(1);
    }
    bTrueSize = validateInput(2,b,&operation);

    // INITIALIZE OUTPUT
    // largest possible sum is one digit larger than operands
    // if dif, no biggy, will just ignore leading 0s
    // to protect against shorter length of C, calloc instead of malloc
    int cSize = 0;
    if (aTrueSize <= bTrueSize) {
        cSize = bTrueSize + 1;
    } else {
        cSize = aTrueSize + 1;
    }
    int *c = calloc(cSize,sizeof(int));
    if (c==NULL) {
        fprintf(stderr,"Error: out of memory!");
        return(1);
    }

    // INVERT OPERANDS IF NECESSARY
    // if subtracting, make larger operand first input
    // if we switch the order, set invert to 1
    int invert = 0;
    if (operation == 1) {
        int larger = findLargerInput(a,b,aTrueSize,bTrueSize);
        if (larger == 0) {
            invert = 0;
        } else {
            invert = 1;
        }
    }

    // PERFORM OP
    if (invert == 1) {
        performOp(operation, b, a, c, bTrueSize, aTrueSize, cSize, invert);
    } else {
        performOp(operation, a, b, c, aTrueSize, bTrueSize, cSize, invert);
    }

    // GENERATE OUTPUT
    generateOutput(c, cSize);


    return terminationCondition;
}
