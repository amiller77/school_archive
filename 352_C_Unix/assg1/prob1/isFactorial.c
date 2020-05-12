/*
* File: isFactorial.c
* Author: Alexander Miller
* Purpose: determine if input numbers can be expressed as factorial
* Input: reads a sequence of 0+ positive integers from stdin until
* no more can be read
* Output: prints "N = b!" or "N not factorial" for any N read from input
*/

#include <stdio.h>


/*
* CALCULATE FACTORIAL
* Behavior: finds factorial representation, if one exists
* Parameters: x, a positive integer
* Returns: 0 if no factorial exists, or the factorial integer
*   i.e. if answer is 4!, returns 4
*/
int calculateFactorial(int x) {
    int runningTotal = 1;
    // iterate over integer values up to x
    int i;
    for (i = 1; i<=x; i++) {
        // if x % i == 0, then i is a factor of x [since x is nonzero]
        if (x%i==0) {
            runningTotal = runningTotal * i;
            // if the product is too large, factorial failed
            if (runningTotal > x) {
                return 0;
            } // if product is x, we have found the factorial
            else if (runningTotal == x) {
                return i;
            }
        } // if i not a factor, then factorial not possible
        else {
            return 0;
        }
    }
    // invariant: we were unable to find a factorial representation
    return 0;
}


/*
* MAIN
* Behavior: control loop to gather input, call calculateFactorial, and
* terminate program
*/
int main() {
    int x = 0;
    int flag = 1;
    int programTermination = 0;
    while (flag == 1) {
        int result = scanf("%d",&x);
        // if we couldn't read anything, abnormal exit:
        if (result == 0) {
            fprintf(stderr,"Error: input is not a number\n");
            return(1);
        } // if EOF, end program
        else if (result == -1) {
            return(programTermination);
        }
        // if x is non-positive, print to stderr; then continue
        if (x <= 0) {
            programTermination = 1;
            fprintf(stderr,"Error: input value %d is not positive\n",x);
            continue;
        }
        // otherwise, we are ready to rumble
        int factorial = calculateFactorial(x);
        if (factorial == 0) {
            printf("%d not factorial\n",x);
        } else {
            printf("%d = %d!\n",x,factorial);
        }
    }
    // should be unreachable, but prevents compiler warning:
    return(programTermination);
}
