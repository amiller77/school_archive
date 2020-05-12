/*
* File: sumReverse.c
* Author: Alexander Miller
* Behavior: program to read in input numbers and, for each number read,
* calculate the "sum of reverse" and print it out to its own line
* Sum of Reverse: reverse the order of the decimal digits, then add to original
*/

#include <stdio.h>
#include <math.h>

/*
* COMPUTER POWER OF 10
* Behavior: small function to find 10^k
* Input: integer, k, power of 10 to compute
* Output: the kth power of 10
*/
int computePowerOf10(int k) {
    int runningProduct = 1;
    int i;
    for (i = 0; i<k; i++) {
        runningProduct = runningProduct*10;
    }
    return runningProduct;
}

/*
* FIND REVERSE
* Behavior: finds the reverse of a given positive integer
* Input: a positive integer
* Output: the reverse of the input integer
*/
int findReverse(int x) {
    // numColumns = smallest power of 10 > x [even if x is a power of 10]
    int numColumns = floor(log10((double)x)) + 1;
    // initialize variables needed in coming loop:
    int runningColumnTotal = 0;
    int reverseInteger = 0;
    int columnValue = 0;
    int newIndexWeight = 0;
    int firstIColSum = 0;
    // iterate over columns to extract the column value:
    int i;
    for (i = 1; i <= numColumns; i++) {
        // if you mod by 10^i, you get sum of columns up to 10^(i-1) column
        // modding by 10^i breaks down at i==11 b/c it overflows, so use the following optout:
        if (i<numColumns) {
            firstIColSum = x % computePowerOf10(i);
        } else {
            firstIColSum = x;
        }
        // we can split this column sum: largestColumn*itsWeight + rest
        // -> firstIColSum = colVal(10^(i-1)) + runningColTotal
        // -> colVal = (firstIColSum - runningColTotal) / 10^(i-1)
        columnValue = (firstIColSum - runningColumnTotal) / computePowerOf10(i-1);
        // the new weight of the extracted digit is:
        // newIndexWeight = maxIndexWeight - oldIndexWeight
        // -> newIndexWeight = (numColumns - 1) - (i - 1) = numColumns - i
        newIndexWeight = numColumns - i;
        // add in the columnValue at the reversed weight:
        reverseInteger = reverseInteger + columnValue * computePowerOf10(newIndexWeight);
        // iterate runningColumnTotal
        runningColumnTotal = runningColumnTotal + columnValue * computePowerOf10(i-1);
    }
    return reverseInteger;
}

/*
* MAIN
* gathers input from user, prints error messages to stderr, output to stdout
* prints sum of input with its reverse, found by findReverse()
*/
int main() {
    int flag = 1;
    int terminationSignal = 0;
    while (flag == 1) {
        int x = 0;
        int readResult = scanf("%d",&x);
        // if we couldn't read a value, error & irregular quit
        if (readResult == 0) {
            fprintf(stderr,"Error: Non-integer value in input\n");
            return 1;
        } // if EOF, normal quit
        else if (readResult == -1) {
            return(terminationSignal);
        }  // if non-positive x, error & continue
        else if (x <= 0) {
            fprintf(stderr,"Error: input value %d is not positive\n",x);
            terminationSignal = 1;
        } // if normal value, find reverse of x, print sum, continue
        else {
            int y = findReverse(x);
            int sum = x + y;
            printf("%d\n",sum);
        }
    }
    // should be unreachable, but to avoid compiler warning
    return(terminationSignal);
}
