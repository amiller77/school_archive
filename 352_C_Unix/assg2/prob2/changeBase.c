/*
* File: changeBase.c
* Author: Alexander Miller
* Description: changes from base b in [2,32] to base 10
*
* Input: read in a base, followed by series of WS-delineated strings that
*   represent numbers in that base
* Strings consist of alpha-numeric characters (non-case sensitive) s.t. a=10 ... z=35
* Legal bases: 2-36, given as an integer (base 'a' not allowed)
* If non-legal base entered, show error and exit (b is int in [2,32])
* use scanf to read strings one at a time; each of these strings is limited to 6 characters of input
* if string is not a legal b-base number, print error, then continue
*
* Output: For each string, convert to unsigned long and print as follows:
*   printf("%lu\n"); // %lu -> code for unsigned long
*
* Mapping ASCII Values:
* Map lowercase: 65 <= 'x' <= 90 by subtracting 55 and casting as int : a-z -> 10-35
* Map digits: 48 <= 'x' <= 57 by subtracting 48 and casting as int : 0-9 -> 0-9
* Map uppercase: 97 <= x <= 122 by subtracting 87 and casting as int : A-Z -> 10-35
*/

#include <stdio.h>


/*
* POWER
* calculates and returns base ^ exponent
*/
int power(int base, int exponent) {
    int i;
    int product = 1;
    for (i = 0; i<exponent; i++) {
        product = product * base;
    }
    return product;
}

/*
* FIND STRING LENGTH
* takes string as parameter, returns its length based on location of '\0'
*/
int findStringLength(char str[]) {
    // find string length:
    // actual string length is equal to index of '\0'
    int stringLength;
    for (stringLength = 0; stringLength<7; stringLength++) {
        if (str[stringLength] == '\0') {
            break;
        }
    }
    if (stringLength == 0) {
        return -1;
    }
    return stringLength;
}

/*
* MAP CHAR TO VALUE
* takes in a string, and maps it to the integer equivalent for our purposes
*   ASCII numbers mapped to int values, alphabeticals to {a=10,z=35}
* Returns: integer equivalent, or:
* -1 -> non-alphanumeric character
* -2 -> not a base b number
*/
int mapCharToValue(char c, int base) {
    int cValue;
    // map char to int value
    // check digit inputs:
    if (c >= 48 && c <= 57) {
        cValue = c - 48;
    } // check lowercase alphabetical:
    else if (c >= 65 && c <= 90) {
        cValue = c - 55;
    } // check uppercase alphabetical:
    else if (c >= 97 && c <= 122 ) {
        cValue = c - 87;
    } // otherwise, not a valid character
    else {
        return -1;
    }

    // check that the alphanumeric character is in the proper range for the base
    if (cValue >= base) {
        return -2;
    }
    return cValue;
}

/*
* MAIN
* gathers and validates base, provides feedback loop for successive numbers
* validates base and numbers, iterates over number and performs calculations
* prints sum, and any error messages
*/
int main() {

    // GATHER AND VALIDATE BASE:
    int base = 0;
    int scanResult = scanf("%d",&base);
    if (scanResult <= 0 || base<2 || base>36) {
        fprintf(stderr,"Error: illegal base\n");
        return (1);
    }

    // INPUT LOOP
    char number[7];
    int terminationCondition = 0;
    int result = 0;
    while ( (result=scanf("%6s",number)) != -1 ) {
        // check if we couldn't read input
        if (result == 0) {
            terminationCondition = 1;
            fprintf(stderr,"Error: Not a base 10 number\n");
            continue;
        }
        // get input string length
        int stringLength = findStringLength(number);
        // make sure it's not length 0
        if (stringLength == -1) {
            continue;
        }

        // iterate over chars in input number in reverse order
        unsigned long int sum = 0;
        int i;
        int cValue;
        // start at last meaningful index to avoid accessing '\0' at end of string
        for (i = stringLength - 1; i>=0; i--) {
            char c = number[i];
            // get our numeric value from the character:
            cValue = mapCharToValue(c, base);
            // if input char was bad, quit processing on this word
            if (cValue == -1) {
                terminationCondition = 1;
                fprintf(stderr,"Error: Please only use alphanumeric characters\n");
                break;
            } else if (cValue == -2) {
                terminationCondition = 1;
                fprintf(stderr,"Error: Not a base %d number\n",base);
                break;
            }
            // iterate the sum by the weight for that index
            // since we're iterating backwards, len - 1 - i = weight
            sum = sum + (power(base,stringLength-1-i)*cValue);
        }
        // if input was valid, print the sum
        if (cValue >= 0) {
            printf("%lu\n",sum);
        }
    }

    return terminationCondition;

}
