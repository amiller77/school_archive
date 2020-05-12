/*
* File: rotate.c
* Author: Alexander Miller
* Description: "rotates" a vector of integers as specified, and prints out
*       the rotated vector
* Input: N A1 .... An R
*       N: positive integer specifying vector size
*       Ai: integers, vector elements
*       R:  integer (pos,neg,zero) denotes magnitude and direction of rotation
*           * positive -> to the right, negative -> to the left
* Output: print out elements of rotated vector on one line, space delineated
*       * note that the last int printed is followed by space as well, prior to newline
* Error Cases:
*       * Cannot read at least one int from stdin
*       * 1st int is not positive
*       * fewer ints available to read than indicated
*           Note: if too many ints, ignore extras
*       * input not parseable as int
*/

# include <stdio.h>
# include <stdlib.h>

/*
* PRINT ARRAY
* Description: prints a space-delineated vector, where last element is also
*   followed by a space, and an additional new-line character
*/
void printArray(int data[], int n) {
    int i;
    for (i=0; i<n; i++) {
        printf("%d ",data[i]);
    }
    printf("\n");
}

/*
* ROTATE
* Description: rotates a vector by R
* Inputs: inputData (vector to rotate); outputData (vector to write to)
*       N, size of the vectors; R, amount to rotate by
* Pre-conditions: R has been reduced to range where R in [-N,N]
* Post-conditions: outputData is rotated version of inputData
*/
void rotate(int inputData[], int outputData[], int N, int R) {
    int i;
    for (i=0;i<N;i++) {
        // based on rotate, find the target index of solution:
        int target = i + R;
        if (target >= N) {
            target = target - N;
        }
        if (target < 0) {
            target = target + N;
        }
        // write the value to the target location:
        outputData[target] = inputData[i];
    }
}


/*
* MAIN
* controls main input loop for user, rotates array by calling rotate(),
* prints output by calling printArray()
*/
int main() {
    int N = 0;
    int terminationCondition = 0;

    // gather N
    int result = scanf("%d",&N);
    if (result == 0 || result == -1) {
        fprintf(stderr,"Error: couldn't read in a value for N\n");
        return(1);
    }

    // validate N
    if (N <= 0) {
        fprintf(stderr,"Error: size is not positive number\n");
        return(1);
    }

    // create an array the size of N
    int *inputData;
    int *outputData;
    inputData = malloc(N*sizeof(int));
    outputData = malloc(N*sizeof(int));
    if (inputData == NULL || outputData == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        return(1);
    }

    // main input loop:
    int i = 0;
    for (i = 0; i<N; i++) {
        int val;
        result = scanf("%d",&val);
        if (result == 0) {
            fprintf(stderr,"Error: please enter integers\n");
            return(1);
        }
        if (result == -1) {
            fprintf(stderr,"Error: fewer integers provided than indicated\n");
            return(1);
        }
        inputData[i] = val;
    }

    // gather R
    int R = 0;
    result = scanf("%d",&R);
    if (result == 0 || result == -1) {
        fprintf(stderr,"Error: can't read in value for R");
        return(1);
    }
    // reduce R to range of the array:
    // i.e. rotating by 10 or -10 on a 5-element array is like rotating 0
    if (R > N) {
        R = R%N;
    } else if (R<0 && -1*R > N) {
        R = -( (-1*R) % N);
    }

    // turn outputData into rotated vector:
    rotate(inputData,outputData,N,R);
    // print output
    printArray(outputData,N);
    // end program
    return (terminationCondition);
}
