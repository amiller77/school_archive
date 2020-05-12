/*
* File: getPrime.c
* Author: Alexander Miller
* Behavior: program reads in positive integer from stdin and prints out the
* smallest prime number that is larger than n.
*/
#include <stdio.h>
#include <math.h>

/*
* SOLVE PRIME
* Behavior: for a given int x, finds the smallest prime larger
* Inputs: a positive integer
* Returns: the smallest integer larger than the input that is also prime
* or 0 if we couldn't find one
*/
int solvePrime(int x) {
    // check all possible ints larger than x
    int i;
    for (i = x+1; i<=2147483647; i++) {
        int iWorks = 1;
        // check all possible factors up to sqrt(i)
        int k;
        for (k = 2; k<=ceil(sqrt((double)i)); k++) {
            // if i is divisible by k, then it's not prime
            if (i%k==0) {
                iWorks = 0;
                break;
            }
        }
        // if we found an i s.t. for all k in [2,sqrt(i)], i%k!=0, then i is prime
        if (iWorks == 1) {
            return (i);
        }
    }
    return 0;
}

/*
* MAIN
* terminates irregularly if input can't be read, isn't a positive number,
* or if a larger prime can't be found
* prints the prime and returns 0 otherwise
*/
int main() {
    int x = 0;
    int readResult = scanf("%d",&x);
    // if we couldn't read input, quit
    if (readResult == 0 || readResult == -1) {
        fprintf(stderr,"Error reading input\n");
        return(1);
    }
    else if (x <= 0) {
        fprintf(stderr,"Error: %d is not a positive number\n",x);
        return(1);
    } else {
        int result = solvePrime(x);
        if (result==0) {
            fprintf(stderr,"Error: Couldn't find a prime...\n");
            return(1);
        } else {
            printf("%d\n",result);
            return(0);
        }
    }

}
