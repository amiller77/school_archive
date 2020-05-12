/*
* File: parseInput.c
* Author: Alexander Miller
* Description: provides input-parsing utility functions for mymake.c
* Input File Specs not in Spec:
*   leading whitespace before target name ok, but not if it's \t because that identifies a cmd line
*       trailing whitespace after target ^ before colon also ok; trims whitespace around target
*   need to validate for cmd line with no owning target
*   /t uniquely identifies a cmd line
*   must have exactly 1 colon in target line
*       (not allowed to have additional colon anywhere in line)
*       (not allowed to have 0 colons)
*   can't have space in target name, or any other node name
* ~Note: consider freeing memory in event of errors in this file...
*/

// ************************** EXTERN FUNCTIONS ***************************

#include "./parseInput.h"

// DECLARATION OF PRIVATE FUNCTIONS:
static int isWSLine(char* line);
static int processCmdLine(char* lineBuffer, Node* thisTarget);
static int countColons(char* str);
static int searchForColon(char* str);
static char* extractString(char** startPtrPtr);
static char* buildTargetString(char* lineBuffer, int lastIndex);
static int processTargetLine(char* lineBuffer, Node** thisTargetPtr, Node** rootPtr);

// ************************** PUBLIC FUNCTIONS ***************************

/*
* VALIDATE NUM ARGUMENTS
* checks if num arguments correct
*/
void validateNumArguments(int argc) {
    if (argc != 3) {
        fprintf(stderr,"Usage: mymake makeFileName targetName\n");
        exit(1);
    }
}

/*
* PROCESS MAKE FILE
* creates graph from makefile, returns pointer to first target
* validates makefile; exit(1) if invalid format
*/
Node* processMakefile(char* argv_1) {
    // create root for graph:
    Node* root = NULL;
    // get input file:
    FILE* makefile = fopen(argv_1,"r");
    if (makefile == NULL) {
        fprintf(stderr,"Makefile could not be opened.\n");
        exit(1);
    }
    // iterate over file and build graph
    char *lineBuffer = NULL;
    unsigned long lineBufferSize = 0;
    Node* thisTarget = NULL;
    while (getline(&lineBuffer,&lineBufferSize,makefile) != -1) {
        int processResult;
        // use leading tab to uniquely identify cmd lines
        if (*lineBuffer=='\t') {
            processResult = processCmdLine(lineBuffer,thisTarget);
        } // otherwise treat it as target line
        else {
            // don't process lines that are all whitespace
            if (isWSLine(lineBuffer)==1) {
                continue;
            }
            // (updates thisTarget &, potentially, root)
            processResult = processTargetLine(lineBuffer, &thisTarget, &root);
        }
        // validate that the lines were parsed correctly:
        if (processResult == 1) {
            freeGraph(root);
            free(lineBuffer);
            fclose(makefile);
            exit(1);
        }
    }
    free(lineBuffer);
    // close makefile
    fclose(makefile);
    // return graph root
    return root;
}


// ************************** PRIVATE FUNCTIONS ***************************

/*
* IS WS LINE
* checks to see if line only consists of whitespace
* note: returns true if string is an empty string (just '\0')
* Returns: 1 if whitespace line, 0 else
*/
static int isWSLine(char* line) {
    char* thisChar = line;
    while (isspace(*thisChar)!=0) {
        thisChar++;
    }
    if (*thisChar=='\0') {
        return 1;
    } else {
        return 0;
    }
}

/*
* PROCESS CMD LINE
* processes command line for a given target
* adds cmd to target
* if target is NULL, fatal program error [cmd without associated target]
* Returns:
*   0 -> normal
*   1 -> fatal error
*/
static int processCmdLine(char* lineBuffer, Node* thisTarget) {
    if (thisTarget == NULL) {
        fprintf(stderr,"Error in makefile: command without target given.\n");
        return 1;
    }
    // modify string slightly: trim leading /t and ending /n, if there
    char* trimmedStr = lineBuffer + 1; // trims the /t
    int lineBuffSz = strlen(lineBuffer);
    int trimmedStrLen;
    // if last char == /n, cut off last char, else don't
    if (*(lineBuffer+lineBuffSz-1) == '\n') {
        trimmedStrLen = strlen(trimmedStr) - 1;
    } else {
        trimmedStrLen = strlen(trimmedStr);
    }
    // copy the word, skipping the first tab, up to end, or end-1 if \n is last char
    char* wordBuff = strndup(trimmedStr,trimmedStrLen);
    if (wordBuff == NULL) {
        fprintf(stderr,"Error: out of memory.\n");
        exit(1);
    }

    // create a new cmd
    Cmd* newCmd = cmdConstructor(wordBuff);
    addCmd(thisTarget,newCmd);
    free(wordBuff);
    return 0;
}

/*
* *************** HELPER FUNCTIONS FOR PROCESS TARGET LINE ***********
*/

/*
* COUNT COLONS
*/
static int countColons(char* str) {
    int colonCount = 0;
    char* thisChar = str;
    while (*thisChar != '\0') {
        if (*thisChar == ':') {
            colonCount++;
        }
        thisChar++;
    }
    return colonCount;
}

/*
* SEARCH FOR COLON
* returns index of 1st colon if str contains colon, -1 if no colon
*/
static int searchForColon(char* str) {
    char* thisChar = str;
    int k = 0;
    while (*thisChar != '\0') {
        if (*thisChar == ':') {
            return k;
        }
        k++;
        thisChar = str+k;
    }
    return -1;
}

/*
* BUILD TARGET STRING
* looks at "target" section of string, removes leading and trailing whitespace
* verifies that there is no internal whitespace
* target string put on heap
* returns target string or NULL if invalid target string
* Input:
*   lastIndex: last index of the "target" portion of the string
*/
static char* buildTargetString(char* lineBuffer, int lastIndex) {
    char* strStart = lineBuffer;
    char* strEnd = lineBuffer + lastIndex;
    // trim whitespace:
    while ( isspace(*strStart) != 0) {
        if (strStart == strEnd) {
            fprintf(stderr,"Error: invalid target given\n");
            return NULL;
        }
        strStart++;
    }
    while ( isspace(*strEnd) != 0) {
        if (strStart == strEnd) {
            fprintf(stderr,"Error: invalid target given\n");
            return NULL;
        }
        strEnd--;
    }
    // ensure there is no interior whitespace
    char* thisChar = strStart;
    while (thisChar <= strEnd) {
        if ( isspace(*thisChar) != 0) {
            fprintf(stderr, "Error: invalid target given\n");
            return NULL;
        }
        thisChar++;
    }
    // copy the trimmed, validated string
    int strlen = strEnd - strStart + 1;
    char* newStr = strndup(strStart,strlen);
    if (newStr == NULL) {
        fprintf(stderr,"Error: out of memory.\n");
        exit(1);
    }
    return newStr;
}

/*
* EXTRACT STRING
* scans from start ptr to first whitespace encountered
*   updates start ptr to point to next char to read for next time
* Returns:
*   malloc'd substring, or NULL if nothing left to copy (end of source string)
*/
static char* extractString(char** startPtrPtr) {
    // iterator
    char* thisChar = *startPtrPtr;
    // skip initial whitespace:
    while (isspace(*thisChar) != 0) {
        thisChar++;
    }
    // if we hit end of string while checking initial whitespace, return null
    if (*thisChar == '\0') {
        return NULL;
    }
    // mark start of new string:
    char* startOfNewString = thisChar;
    // iterate until whitespace or EOF
    while (isspace(*thisChar) == 0 && *thisChar != '\0') {
        thisChar++;
    }
    // mark end of new string:
    char* endOfNewString = thisChar-1;
    // update scan head for next time
    *startPtrPtr = thisChar;
    // create the new string:
    int strSize = endOfNewString - startOfNewString + 1;
    char* newStr = malloc(sizeof(char)*(strSize+1));
    // set last char to '\0'
    *(newStr + strSize) = '\0';
    // copy over the values
    char* newStrChar = newStr;
    thisChar = startOfNewString;
    while (thisChar <= endOfNewString) {
        *newStrChar = *thisChar;
        newStrChar++;
        thisChar++;
    }
    return newStr;
}


/*
* *************** END HELPER FUNCTIONS FOR PROCESS TARGET LINE ***********
*/

/*
* PROCESS TARGET LINE
* processes target line
* may reassign thisTarget and root through use of their addresses
* Returns:
*   0 -> normal
*   1 -> fatal error
*/
static int processTargetLine(char* lineBuffer, Node** thisTargetPtr, Node** rootPtr) {
    // validate num colons in input line:
    int colonCount = countColons(lineBuffer);
    if (colonCount != 1) {
        fprintf(stderr,"Error in makefile: bad target line. Should contain exactly one colon.\n");
        return 1;
    }

    // extract, trim, and validate the target:
    // find single colon in line:
    int colonIndex = searchForColon(lineBuffer);
    char* targetString = buildTargetString(lineBuffer,colonIndex-1);
    if (targetString == NULL) {
        return 1;
    }

    // check graph for existing node:
    Node* previousNode = getLastNode(*rootPtr);
    Node* targetNode = getNode(*rootPtr,targetString);
    if (targetNode != NULL) {
        // see if it's already a target
        if (targetNode->isTarget==1) {
            // target already exists:
            fprintf(stderr, "Error: target provided more than once.\n");
            free(targetString);
            return 1;
        } // if not, make it a target:
        else {
            targetNode->isTarget = 1;
        }
    } // no node already exists; create new one, and add to graph
    else {
        targetNode = nodeConstructor(targetString,1);
        if (previousNode != NULL) {
            previousNode->nextSequentialNode = targetNode;
            previousNode = targetNode;
        } // assign target to be graph root if necessary
        else {
            *rootPtr = targetNode;
            previousNode = targetNode;
        }
    }
    // update current target pointer
    *thisTargetPtr = targetNode;

    // process the rest of the line:
    char* restLinePtr = lineBuffer + colonIndex + 1;
    char* scanHead = restLinePtr;
    while (1) {
        // extract a malloc'd whitespace-delineated substring until end of line:
        // (scanHead updates on its own to point to next part of string to extract)
        char* nodeName = extractString(&scanHead);
        if (nodeName == NULL) {
            break;
        }
        // we have already parsed on whitespace and checked for colons,
        //      so remaining strings should be good
        // see if node exists already, otherwise create one:
        Node* node = getNode(*rootPtr,nodeName);
        if (node == NULL) {
            node = nodeConstructor(nodeName,0);
            // add the new node to this graph section, since it doesn't already exist
            // note: invariant: graph contains at least the target of this line, so root nonnull
            previousNode->nextSequentialNode = node;
            previousNode = node;
        }
        // add an edge from target to node, if that edge doesn't already exist
        Edge* thisEdge = getEdge(targetNode,node);
        if (thisEdge == NULL) {
            thisEdge = edgeConstructor(targetNode,node);
            addEdge(targetNode,thisEdge);
        }
        free(nodeName);
    }
    free(targetString);
    return 0;
}
