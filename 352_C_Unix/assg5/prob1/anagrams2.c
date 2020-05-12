/*
* File: anagrams2.c
* Author: Alexander Miller
* Description:
*   groups (case-insensitive) anagrams into "families"
* Input:
*   sequence of strings separated by whitespace, at most 64 in length
* Error conditions:
*   non-alphabetical characters -> error message, ignore string
* Output:
*   print each anagram family on its own line
*   families in order of occurrence in input
*   words in each family also in order of occurrence in input
*
*/

# include <stdio.h>
# include <stdlib.h>

// WORD: linked list of strings
typedef struct word {
    char *str;
    struct word *next;
} word;

// FAMILY: linked list of words
typedef struct family {
    word *wordsHead;
    word *wordsTail;
    struct family *next;
} family;

/*
* FIND STRING LENGTH
* returns length of a string
*/
int findStringLength(char *str) {
    char *thisChar = str;
    int count = 0;
    while (*thisChar != '\0') {
        count++;
        thisChar++;
    }
    return count;
}

/*
* COPY STRING
* returns a malloced copy of a string (array reduced to minimum required length)
*/
char* copyString(char* str, int len) {
    // including space for '\0'
    char* newString = malloc((len+1)*sizeof(char));
    if (newString == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        exit(1);
    }
    int i;
    // including len index s.t. '\0' is included
    for (i=0;i<=len;i++){
        *(newString+i) = *(str+i);
    }
    return newString;
}

/*
* WORD CONSTRUCTOR
* mallocs a word, sets str to deep copy of parameter string, next to NULL
* returns pointer to the word
*/
word* wordConstructor(char *str, int strLen) {
    word* newWord = malloc(sizeof(word));
    if (newWord == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        exit(1);
    }
    newWord->str = copyString(str,strLen);
    newWord->next = NULL;
    return newWord;
}

/*
* FAMILY CONSTRUCTOR
* mallocs a family, sets head/tail to firstWord, sets next to null
* returns pointer to the family
*/
family* familyConstructor (word *firstWord) {
    family *newFamily = malloc(sizeof(family));
    if (newFamily == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        exit(1);
    }
    newFamily->wordsHead = firstWord;
    newFamily->wordsTail = firstWord;
    newFamily->next = NULL;
    return newFamily;
}

/*
* TO LOWER CASE
* Parameters: string and string length
* Returns: a newly-allocated string which is the lower case version
*   returns NULL if there is a non-alphabetic character inside
*/
char* toLowerCase(char *str, int strLen) {
    char *newStr = NULL;
    newStr = malloc(sizeof(char)*(strLen+1));
    if (newStr == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        exit(1);
    }
    int i;
    for (i=0; i<strLen; i++) {
        char thisChar = *(str+i);
        char *destination = newStr+i;
        if (thisChar >= 65 && thisChar <= 90) {
            *destination = thisChar + 32;
        } else if (thisChar >= 97 && thisChar <= 122) {
            *destination = thisChar;
        } else {
            free(newStr);
            return NULL;
        }
    }
    *(newStr+strLen)='\0';
    return newStr;
}

/*
* SWAP
* Description: swaps array elements at positions a, b
*/
void swap(char data[], int a, int b) {
    char buffer = data[a];
    data[a] = data[b];
    data[b] = buffer;
}

/*
* PARTITION SORT
* Description: sorts an array "in place" by using the QuickSort algorithm
* Inputs: a: starting index, z: last index of array, data: array to sort
*/
void partitionSort(char data[], int a, int z) {
    int lenArray = z+1-a;
    // base case: last index = first index
    if (lenArray <= 1) {
        return;
    }
    char firstVal = data[a];
    // middle of array is at starting point + length/2
    char medVal = data[a+lenArray/2];
    char lastVal = data[z];
    // find median value and location
    char median;
    int medianIndex;
    if ( (lastVal >= firstVal && lastVal <= medVal)
    || (lastVal <= firstVal && lastVal >= medVal) ) {
        median = lastVal;
        medianIndex = z;
    } else if ( (medVal >= firstVal && medVal <= lastVal)
    || (medVal <= firstVal && medVal >= lastVal) ) {
        median = medVal;
        medianIndex = a+lenArray/2;
    } else if ( (firstVal >= medVal && firstVal <= lastVal)
    || (firstVal <= medVal && firstVal >= lastVal) ) {
        median = firstVal;
        medianIndex = a;
    } // this block isn't possible, but it makes the compiler happy
    else {
        median = medVal;
        medianIndex = a+lenArray/2;
    }
    // move median value to first position:
    swap(data,a,medianIndex);
    /*
    * scan up from the left, creating a "less than" partition
    * scan down from the right, creating a "greater than" partition
    */
    int tail = z;
    int head = a+1;
    while (head < tail) {
        if (data[head] <= median) {
            head++;
            continue;
        }
        // case where we can swap
        if (data[tail] <= median) {
            swap(data,head,tail);
            head++;
            tail--;
            continue;
        }
        // case where we can't swap, move tail down
        tail--;
    }
    // perform some adjustments:
    if (data[head] > median) {
        head--;
    }

    // now head == tail, so
    // now that the data is partitioned, swap head, median (now in a)
    swap(data,a,head);
    // now head holds the median value, which is in its final location
    // now call parition sort on each partition
    partitionSort(data,a,head-1);
    partitionSort(data,head+1,z);
}

/*
* CHECK IDENTICAL
* Parameters: 2 strings and their mutual length
* Returns: 1 if they are identical, 0 else
*/
int checkIdentical(char *str1, char *str2, int length) {
    int i;
    for (i=0;i<length;i++) {
        if ( *(str1+i) != *(str2+i) ) {
            return 0;
        }
    }
    return 1;
}

/*
* FIND ANAGRAM
* Parameters: str1, a valid string; str2, an unvalidated string
* Returns 1 if they are anagrams, 0 if not anagrams
*/
int findAnagram(char *str1, char *str2) {
    // get array lengths:
    int l1 = findStringLength(str1);
    int l2 = findStringLength(str2);
    // create lower-case copies
    char *lowerStr1 = toLowerCase(str1, l1);
    char *lowerStr2 = toLowerCase(str2, l2);
    // if not the same length, then not anagrams
    if (l1 != l2) {
        free(lowerStr1);
        free(lowerStr2);
        return 0;
    }
    // sort the lower case copies
    partitionSort(lowerStr1,0,l1-1);
    partitionSort(lowerStr2,0,l2-1);
    // check to see if they are the same:
    int same = checkIdentical(lowerStr1,lowerStr2,l1);
    free(lowerStr1);
    free(lowerStr2);
    return same;
}

/*
* FIND LAST FAMILY
* gets us the last family in the sequence
* returns the pointer to the family
*/
family* findLastFamily(family *familyRoot) {
    family *thisFamily = familyRoot;
    while (thisFamily->next != NULL) {
        thisFamily=thisFamily->next;
    }
    return thisFamily;
}

/*
* GENERATE OUTPUT
* prints families to their own lines, space separated
*/
void generateOutput(family *familyRoot) {
    family *thisFamily = familyRoot;
    // iterate over families
    while (thisFamily != NULL) {
        word *thisWord = thisFamily->wordsHead;
        // iterate over words in family
        while (thisWord != NULL) {
            printf("%s",thisWord->str);
            if (thisWord->next != NULL) {
                printf(" ");
            } else {
                printf("\n");
            }
            thisWord = thisWord->next;
        }
        thisFamily = thisFamily->next;
    }
}

/*
* MAIN
*/
int main() {
    // intialize variables:
    int terminationCondition = 0;
    int flag = 1;
    char inputBuffer[65];
    family *familyRoot = familyConstructor(NULL);
    // input loop
    while (flag) {
        int result = scanf("%64s",inputBuffer);
        // if we hit EOF, end loop
        if (result == -1) {
            break;
        }
        // validate the string as valid input:
        int strLen = findStringLength(inputBuffer);
        char *validatedLowerCase = toLowerCase(inputBuffer,strLen);
        if (validatedLowerCase == NULL) {
            free(validatedLowerCase);
            terminationCondition = 1;
            fprintf(stderr,"Error: invalid characters in string.\n");
            continue;
        } else {
            free(validatedLowerCase);
        }
        // string is valid, so create a word
        word *newWord = wordConstructor(inputBuffer,strLen);
        // find anagram family:
        int familyFound = 0;
        family *thisFamily = familyRoot;
        while (thisFamily != NULL) {
            // check for an empty family (i.e. when first string from input)
            if (thisFamily->wordsHead == NULL) {
                familyFound = 1;
                thisFamily->wordsHead = newWord;
                thisFamily->wordsTail = newWord;
                break;
            } // general case:
            else {
                // check if belongs to this family:
                // add to anagram family if necessary:
                int anagram = findAnagram(thisFamily->wordsHead->str,inputBuffer);
                if (anagram) {
                    // append to family:
                    thisFamily->wordsTail->next = newWord;
                    thisFamily->wordsTail = newWord;
                    familyFound = 1;
                    break;
                }
            }
            thisFamily = thisFamily->next;
        }
        // if no family found, create new anagram family:
        if (!familyFound) {
            family *lastFamily = findLastFamily(familyRoot);
            // pass to family constructor
            family *newFamily = familyConstructor(newWord);
            lastFamily->next = newFamily;
        }
    }
    generateOutput(familyRoot);
    return terminationCondition;
}
