/*
* File: count2.c
* Author: Alexander Miller
* Description: counts input integers, and prints result in asc. order by input
* Input: sequence of integers from stdin
*       The first value, N > 0, is an integer specifying
*           number of ints to be processed
* Output: print out the number of times each distinct integer appears
*       These are printed out in ascending order of the input values
*       Print format: printf("%d %d\n", input_value, count);
* Error cases:
*       * cannot read at least one integer from stdin
*       * N is not positive
*       * number of ints to read is actually fewer than N
*           note: not an error if amount supplied is greater than N, just ignore extras
*/

# include <stdio.h>
# include <stdlib.h>

// LINKEDLIST: NODE
typedef struct node {
    int value;
    struct node *next;
} node;

/*
* SELECTION SORT
* Inputs:
*   N - size of linked list
*   linkedListRoot - points to beginning of linked list
* Pre-conditions: linkedList has at least 1 node, which has an initialized val
* Returns: linkedListRoot now points to beginning of reordered list
*/
node *selectionSort(int N, node *linkedListRoot ) {
    int searchIndex = 0;
    node *searchPointerPrevious = NULL;
    node *searchPointer = linkedListRoot;
    while (searchIndex < N) {
        // initialize minimum information for this round:
        node *minimumNodePrevious = searchPointerPrevious;
        node *minimumPointer = searchPointer;
        int minVal = minimumPointer->value;

        // search unsorted section of list for minimum:
        int i = searchIndex;
        node *ithNode = searchPointer;
        node *ithNodePrevious = searchPointerPrevious;
        while (i<N) {
            i++;
            // compare this value with min value found
            int comparisonValue = ithNode->value;
            // if new minimum found, update min information:
            if (comparisonValue < minVal) {
                minVal = comparisonValue;
                minimumPointer = ithNode;
                minimumNodePrevious = ithNodePrevious;
            }
            // update pointers to ithNode
            ithNode = ithNode->next;
            if (ithNodePrevious != NULL) {
                ithNodePrevious = ithNodePrevious->next;
            } else {
                ithNodePrevious = linkedListRoot;
            }
        }
        // if minVal == searchPointer value, then skip swap:
        if (minVal == searchPointer->value) {
            searchIndex++;
            searchPointerPrevious = searchPointer;
            searchPointer = searchPointer->next;
            continue;
        }
        // else put the minimum pointer where the search pointer was:
        // save where minimumPointer was originally pointing (minAfter)
        node *minimumPointerAfter = minimumPointer->next;

        // minimumPointer will now point to searchPointerNext unless
        // they are directly adjacent, in which case it points to searchPointer
        if (searchPointer->next == minimumPointer) {
            minimumPointer->next = searchPointer;
        } else {
            minimumPointer->next = searchPointer->next;
        }

        // now move searchPointer to where minimumPointer was:
        // update minimumNodePrevious (generally, to point to searchPointer)
        if (minimumNodePrevious != NULL) {
            // the previous node generally points to searchPointer
            if (minimumNodePrevious != searchPointer) {
                minimumNodePrevious->next = searchPointer;
            } // if previous node was searchPointer, points to minAfter instead of itself
            else {
                minimumNodePrevious->next = minimumPointerAfter;
            }
        }
        // point searchPointer to where minimumPointer was pointing
        searchPointer->next = minimumPointerAfter;

        // set searchPointerPrevious to point to the minimum
        if (searchPointerPrevious != NULL) {
            searchPointerPrevious->next = minimumPointer;
        } // if searchPointerPrevious == NULL, assign new root
        else {
            linkedListRoot = minimumPointer;
        }

        // update the search index information for next round:
        searchIndex++;
        searchPointer = minimumPointer->next;
        searchPointerPrevious = minimumPointer;
    }
    return linkedListRoot;
}

/*
* GET Ith ELEMENT
* Description: grabs ith element of a linked list (returns pointer)
* Inputs:
*   i - element to grab
*   inputDataRoot - pointer to beginning of linkedList
* Pre-condition: i is a valid index for the linkedList
* Returns: pointer to ith element
*/
node *getIthElement(int i, node *inputDataRoot) {
    int k = 0;
    node *kthElement = inputDataRoot;
    for (k = 0; k< i; k++) {
        // move k to point at the next node
        kthElement = kthElement->next;
    }
    return kthElement;
}

/*
* COUNT
* Description: iterates over input and makes a count of values
* Inputs: inputData: data to count, countData: stores counts,
*   valuesFound: records which ones we've already seen (prevent multiple counts for same value),
*   n: size of inputData
* Pre-conditions: countDataRoot and valuesFoundRoot exist but contain no interesting data
* Post-conditions: countDataRoot and valuesFoundRoot point to correct lists
*/
void count(node *inputDataRoot, node *countDataRoot, node *valuesFoundRoot, int n) {
    int i;
    int valsFoundSoFar = 0;
    node *inputI = inputDataRoot;
    // iterate over input values
    for (i=0;i<n;i++){
        int inputVal = inputI->value;
        int duplicateFound = 0;
        int v;
        // iterate over values found so far
        for (v=0;v<valsFoundSoFar;v++) {
            // if we've found the value already, increment count
            if (getIthElement(v,valuesFoundRoot)->value == inputVal) {
                (getIthElement(v,countDataRoot)->value)++;
                duplicateFound = 1;
                break;
            }
        }
        // if no duplicate found, add a new entry to valuesFound
        if (duplicateFound == 0) {
            // append to lists:
            if (valsFoundSoFar > 0) {
                // allocate memory:
                node *newValue = malloc(sizeof(node));
                node *newCount = malloc(sizeof(node));
                if (newValue==NULL || newCount == NULL) {
                    fprintf(stderr,"Error: out of memory!\n");
                    exit(1);
                }
                // initialize vals:
                newValue->value = inputVal;
                newValue->next = NULL;
                newCount->value = 1;
                newCount->next = NULL;
                // append:
                node *lastValFound = getIthElement(valsFoundSoFar-1,valuesFoundRoot);
                node *lastCount = getIthElement(valsFoundSoFar-1,countDataRoot);
                lastValFound->next = newValue;
                lastCount->next = newCount;
            } // or just update root vals if necessary
            else {
                countDataRoot->value = 1;
                valuesFoundRoot->value = inputVal;
            }
            valsFoundSoFar++;
        }
        // move to next input value
        inputI = inputI->next;
    }
}

/*
* GENERATE OUTPUT
* prints the values and their counts, separated by a space, to each line
* Inputs: valuesFound: contains unique values we are counting, countData: contains resp. counts
*/
void generateOutput(node *countDataRoot, node *valuesFoundRoot) {
    node *countDataPointer = countDataRoot;
    node *valuesFoundPointer = valuesFoundRoot;
    while (countDataPointer != NULL) {
        printf("%d %d\n",valuesFoundPointer->value,countDataPointer->value);
        // iterate pointers:
        countDataPointer = countDataPointer->next;
        valuesFoundPointer = valuesFoundPointer->next;
    }
}


/*
* MAIN
* gathers N and inputs from user, allocates memory, handles error conditions
* sorts the inputs by calling partitionSort(), counts the values by calling count(),
* generates the output by calling generateOutput()
*/
int main() {
    int terminationCondition = 0;

    // declare linked list:
    // inputDataRoot - pointer to root to our linkedList
    // inputDataEnd - pointer to end of our linkedList
    // N - int size of our input data
    node *inputDataRoot = NULL;
    node *inputDataEnd = NULL;
    int N = 0;

    // main input loop:
    int flag = 1;
    while (flag==1) {
        int val;
        int result = scanf("%d",&val);
        if (result == 0) {
            fprintf(stderr,"Error: please enter integers\n");
            return(1);
        }
        else if (result == -1) {
            break;
        }
        else {
            // create new node:
            node *newNode = malloc(sizeof(node));
            if (newNode == NULL) {
                fprintf(stderr,"Error: out of memory!\n");
                return(1);
            }
            // initialize its values
            newNode->value = val;
            newNode->next = NULL;
            // if first node, initialize list
            if (inputDataRoot == NULL) {
                inputDataRoot = newNode;
                inputDataEnd = newNode;
            } // else, take current list end and attach new node
            else {
                inputDataEnd->next = newNode;
                // move end of list pointer
                inputDataEnd = newNode;
            }
            // increment our list size
            N++;
        }
    }
    // if no input acquired, quit normally
    if (inputDataRoot == NULL) {
        return 0;
    }

    inputDataRoot = selectionSort(N, inputDataRoot);

    // create lists to hold output
    node *countDataRoot = malloc(sizeof(node));
    node *valuesFoundRoot = malloc(sizeof(node));
    if (countDataRoot == NULL || valuesFoundRoot == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        return(1);
    }
    // initialize values
    countDataRoot->value = 0;
    countDataRoot->next = NULL;
    valuesFoundRoot->next = NULL;

    // count the values:
    count(inputDataRoot,countDataRoot,valuesFoundRoot,N);

    // generate output:
    generateOutput(countDataRoot,valuesFoundRoot);

    return terminationCondition;
}
