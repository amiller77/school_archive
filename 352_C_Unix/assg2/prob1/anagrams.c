/*
* File: anagrams.c
* Author: Alexander Miller
* Description: prints out anagrams of a string, if supplied
*
* Input:
* program reads in sequence of strings from stdin until no more can be read
* input string contains alphabetical characters
*   otherwise, give error msg, ignore, continue
* if first string contains non-alphabetical characters, give error and exit
* input strings are at most 64 characters long
*
* Process:
* for each string read, if it is an anagram of the first string in sequence
*   then it is printed to stdout (first string always gets printed out)
* comparisons are non-case sensitive, but printing should maintain original case
* note: anagrams must be of the same length, won't find anagrams
*   which are substrings of the inputs
*
* Output:
* anagrams should be printed as follows: printf("%s\n",S);
*
* Lower case ASCII range: 97 <= x <= 122 (convert to upper by subtracting 32)
* Upper case ASCII range: 65 <= x <= 90 (convert to upper by adding 32)
*
*/


#include <stdio.h>

/*
* FIND STRING LENGTH
* takes a string as a param and returns its length
*   returns -1 if length is 0
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
* VALIDATE STRING
* checks a string for being alphabetic
* Returns: 0-> valid string, 1-> invalid string
*/
int validateString(char str[], int stringLength) {
    int i;
    for (i=0; i<stringLength; i++) {
        char c = str[i];
        if (c < 65 || (c>90&&c<97) || c>122) {
            return 1;
        }
    }
    return 0;
}

/*
* CHECK CHAR EQUALITY
* checks if a,b are same letter (case insensitive)
* Preconditions: need to have already validated a and b to ensure alphabetical
* Returns: 0-> no match, 1-> match
*/
int checkCharEquality(char a, char b) {
    // same char, same case
    if (a==b) {
        return 1;
    } // a is capitalized form of b
    else if (a>b && a-32==b) {
        return 1;
    } // a is lower case form of b
    else if (a<b && a+32==b) {
        return 1;
    }
    // no match found
    return 0;
}

/*
* VALIDATE INDEX
* used to iterate over invalidIndices, to ensure we haven't used
*   the character at position l before
* Returns: 0 -> valid index, 1 -> invalid index
*/
int validateIndex(int arrayLength, int invalidIndices[], int l) {
    // validate the index to ensure we haven't already used this char
    int i;
    for (i=0;i<arrayLength;i++) {
        if (invalidIndices[i]==l) {
            return 1;
        }
    }
    return 0;
}

/*
* GET UPDATE INDEX
* finds last non-filled location in invalidIndices to store new value
* Returns: index available for storage, or -1 if none found
*/
int getUpdateIndex(int invalidIndices[], int index, int arrayLength) {
    int i;
    for (i = 0; i<arrayLength; i++) {
        if (invalidIndices[i] == -1) {
            return i;
        }
    }
    return -1;
}

/*
* CHECK ANAGRAM
* iterate over chars of comparisonString to find matches in thisString
* if for every char in a, char exists in b, then b is anagram of a
*   note: need to "sample without replacement"
* Returns: 0 -> not an anagram, 1 -> anagram
*/
int checkAnagram(char comparisonString[], char thisString[], int comparisonStringLength) {
    // store indices we've checked, so that we don't use them for multiple comparisons
    int invalidIndices[comparisonStringLength];
    // initialize this to nonsense indices
    int k;
    for (k=0; k<comparisonStringLength;k++) {
        invalidIndices[k]=-1;
    }
    // iterate over chars of comparisonString
    for (k = 0; k<comparisonStringLength;k++) {
        char a = comparisonString[k];
        int l;
        int matchFound = 0;
        // iterate over chars of thisString
        for (l=0; l<comparisonStringLength;l++) {
            // validate the index (to prevent multiple comparisons)
            int invalidIndex = validateIndex(comparisonStringLength,invalidIndices,l);
            if (invalidIndex) {
                continue;
            }
            char b = thisString[l];
            // see if a==b (case insensitive)
            if ( checkCharEquality(a,b) ) {
                matchFound = 1;
                // update invalidIndices to prevent multiple comparisons
                int index = getUpdateIndex(invalidIndices,l,comparisonStringLength);
                invalidIndices[index] = l;
                break;
            }
        }
        // if no match for a was found in thisString, then thisString is no anagram
        if (matchFound==0) {
            return 0;
        }
    }
    // invariant: we were able to find a unique match for all chars in a
    // so thisString is an anagram
    return 1;
}

/*
* MAIN
* gathers and validates first string (and prints it if appl.)
* prints error messages, returns error or normal values
* provides feedback loop for user to enter words
* checks anagrams by calling checkAnagram
*/
int main() {
    // variables used throughout:
    char comparisonString[65];
    int terminationCondition = 0;
    int comparisonStringLength = 0;

    // GATHER AND VALIDATE FIRST STRING:
    // get the comparison string from input:
    int result = scanf("%64s",comparisonString);
    if (result == 0) {
        fprintf(stderr,"Error: Can't read input string\n");
        return -1;
    } else if (result == -1) {
        return 0;
    }
    // find comparison string length
    comparisonStringLength = findStringLength(comparisonString);
    if (comparisonStringLength == 0) {
        return 0;
    }
    // validate comparison string:
    int invalidString = validateString(comparisonString, comparisonStringLength);
    if (invalidString == 1) {
        fprintf(stderr,"Error: invalid first string\n");
        return 1;
    }

    // print comparison string, as it is an anagram
    printf("%s\n",comparisonString);

    // MAIN INPUT LOOP (iterate over input strings to compare)
    char thisString[65];
    while ( (result = scanf("%64s",thisString)) != -1 ) {
        int thisStringLength = findStringLength(thisString);
        // first check: string must be valid
        invalidString = validateString(thisString,thisStringLength);
        if (invalidString == 1) {
            fprintf(stderr,"Error: invalid character\n");
            terminationCondition = 1;
            continue;
        }
        // second check: string must be same length to be anagram
        if (thisStringLength != comparisonStringLength) {
            continue;
        }
        // perform other checks:
        int anagram = checkAnagram(comparisonString,thisString,comparisonStringLength);
        // if anagram, print it:
        if (anagram == 1) {
            printf("%s\n",thisString);
        }
    }

    return terminationCondition;
}
