/*
* File: bacon.c
* Author: Alexander Miller
* Description: source code for program to computer bacon score
* Invocations: -l optional and can appear arbitrarily many times; must have exactly 1 infile
*   bacon inFile
*   bacon -l inFile
*   bacon inFile -l
*   bacon -l -l inFile
*   bacon -l inFile -l -l ....
* Input File Format: may contain blank lines as well
*   Movie: ...  [single space after "Movie:"; movie name rest of line, excluding '/n']
*   actor1...   [name is entire line except '/n']
*   ...
* Processing Input:
*   Case sensitive, don't worry about trimming lines except for '\n'
*   Can assume file in proper format [no actors without movie, movie lines contain names]
* Behavior:
*   After processing input file, Loop:
*       read line from stdin with actors name
*       Compute Bacon score using BFS
*       Print Bacon score to stdout, or the connections as well if -l
* Output:
*   printf("Score: %d\n",score);
*   printf("Score: No Bacon!");
*   printf("
*       <actor>
*       was in <movie> with
*       <actor>
*       was in <movie> with
*       ...
*       was in <movie> with
*       Kevin Bacon
*    ");
* Non-fatal Errors:
*   User queries actor not in the graph
* Fatal Errors:
*   Bad program invocation
*/

# include "graph.h"

# include <stdio.h>
# include <stdlib.h>
# include <string.h>
# include <ctype.h>



// ****************** DECLARATION OF FUNCTIONS ********************
Actor* buildGraph(char* fileName);
int checkForWS(char* line);
int findBacon(char* name, Actor* actors);
void processActors(Queue* queue, char* movieName);
void trimNewline(char* str);
char* validateProgramArguments(int argc, char* argv[], int* L);


// MAIN
int main(int argc, char* argv[]) {
    int L; // holds -l flag value
    char* fileName = validateProgramArguments(argc, argv, &L);
    Actor* root = buildGraph(fileName);
    // get queries from user
    char* lineBuff = NULL;
    size_t buffCapac = 0;
    int terminationCondition = 0;
    while (1) {
        int scanResult = getline(&lineBuff, &buffCapac, stdin);
        if (scanResult == -1 || scanResult == 0) {
            break;
        }
        // if line is just whitespace, or null line, skip
        if (checkForWS(lineBuff) == 1) {
            continue;
        }
        // let's trim the newline off the end
        trimNewline(lineBuff);
        // try to find bacon score
        int dfsResult = findBacon(lineBuff,root);
        if (dfsResult == 1) {
            terminationCondition = 1;
        }
        // clear off the marks from our last traversal
        clearMarks(root);
    }
    // free memory
    free(fileName);
    free(lineBuff);
    freeGraph(root);
    return terminationCondition;
}

// BUILD GRAPH
Actor* buildGraph(char* fileName) {
    // open and validate file
    FILE* file = fopen(fileName,"r");
    if (file == NULL) {
        fprintf(stderr,"Error: could not open provided file.\n");
        exit(1);
    }

    // scan lines from file
    char* lineBuff = NULL; // holds line contents
    size_t buffCapac = 0; // size of lineBuff
    char* movieName = NULL; // name of current movie, whose actors are being parsed
    Queue* movieActors = queueConstructor(); // holds actors for current movie, so we can connect them later
    Actor* root = NULL;
    while (1) {
        int scanResult = getline(&lineBuff, &buffCapac, file);
        if (scanResult == -1 || scanResult == 0) {
            break;
        }

        // if line is just whitespace, or null line, skip
        if (checkForWS(lineBuff) == 1) {
            continue;
        }
        // let's trim the newline off the end
        trimNewline(lineBuff);

        // if first word of line is "Movie:", movie line
        char wordBuff[7];
        sscanf(lineBuff,"%6s",wordBuff);
        if (strcmp(wordBuff,"Movie:")==0) {

            // we hit another movie, so process actor connections for old movie
            // (add an edge for each actor to every other actor in the movie)
            processActors(movieActors,movieName);
            // new movie name starts after "Movie: " [at pos 7]
            free(movieName);
            movieName = strdup(lineBuff+7);
            memoryCheck(movieName);
            continue;
        }

        // if actor line, get existing actor by that name, or create new one:
        Actor* actor = getActor(lineBuff, root);
        if (actor == NULL) {
            actor = actorConstructor(lineBuff);
            // if first actor, set root to it
            if (root == NULL) {
                root = actor;
            } // else, add to graph
            else {
                addActorToGraph(root,actor);
            }
        }
        // add to processing queue
        queuePush(movieActors,actor);
    }
    // if there's still stuff in queue, go ahead and process the rest
    if (movieActors->actors != NULL) {
        processActors(movieActors,movieName);
    }
    free(lineBuff);
    free(movieActors);
    free(movieName);
    fclose(file);
    return root;
}

/*
* CHECK FOR WS
* returns 1 if line only consists of whitespace (or is simply '\0'), 0 else
*/
int checkForWS(char* line) {
    // check trivial case, line == '\0'
    if (*line == '\0') {
        return 1;
    }
    // check general case
    char* thisChar = line;
    while (*thisChar != '\0') {
        // if a single char is not whitespace, not a WS line
        if (isspace(*thisChar) == 0) {
            return 0;
        }
        thisChar++;
    }
    return 1;
}

/*
* TRIM NEW LINE
* pops the newline off the end of a string if necessary
* note: assumes that the first newline encountered is the end of the string
* note: edits the passed string
*/
void trimNewline(char* str) {
    char* thisChar = str;
    while (*thisChar != '\0') {
        if (*thisChar == '\n') {
            *thisChar = '\0';
            return;
        }
        thisChar++;
    }
}

/*
* PROCESS ACTORS
* builds connections for all actors in a certain movie
* Post Condition: queue is empty
*/
void processActors(Queue* queue, char* movieName) {
    if (movieName == NULL || queue == NULL) {
        return;
    }
    // pop all actors from queue, one at a time
    while (1) {
        Actor* thisActor = queuePop(queue);
        if (thisActor == NULL) {
            break;
        }
        // build edges with every other actor in queue, without removing them yet
        Actor* otherActor = queue->actors;
        while (otherActor != NULL) {
            addEdges(thisActor,otherActor,movieName);
            otherActor = otherActor->nextQueueActor;
        }
    }
}

/*
* VALIDATE PROGRAM ARGUMENTS
* Params: program args [argc, argv], int* L
*   function modifies L to hold 0 if no -l, 1 if -l
* Returns:
*   char* fileName -> malloc'd copy of fileName provided as program parameter
* Invocations: -l optional and can appear arbitrarily many times; must have exactly 1 infile
*   bacon inFile
*   bacon -l inFile
*   bacon inFile -l
*   bacon -l -l inFile
*   bacon -l inFile -l -l ....
*/
char* validateProgramArguments(int argc, char* argv[], int* L) {
    // if no args, no bueno
    if (argc == 1) {
        fprintf(stderr,"Error. Correct Invocation: bacon -l inFile\n");
        exit(1);
    }
    // otherwise, iterate thru args [starting at index 1]
    int i;
    *L = 0;
    int fileFound = 0;
    char* fileName;
    for (i=1;i<argc;i++) {
        // if arg is -l, turn flag on
        if (strcmp(argv[i],"-l")==0) {
            *L = 1;
        }
        else {
            // gather file name if not already provided
            if (fileFound == 0) {
                fileFound = 1;
                fileName = strdup(argv[i]);
            } // if file already, provided, fatal error
            else {
                fprintf(stderr,"Error. Correct Invocation: bacon -l inFile\n");
                free(fileName);
                exit(1);
            }
        }
    }
    return fileName;
}



/*
* FIND BACON
* find bacon score for actor, name passed as parameter
* returns 1 if non-fatal error, 0 else
*/
int findBacon(char* name, Actor* actors) {
    // get target actor, validate in graph
    Actor* target = getActor(name,actors);
    if (target == NULL) {
        fprintf(stderr,"Error: No actor named %s entered.\n",name);
        return 1;
    }

    // get KB, validate exists
    Actor* KB = getActor("Kevin Bacon",actors);
    if (KB == NULL) {
        printf("Score: No Bacon!");
        return 0;
    }

    // if target is Kevin Bacon, answer is trivially 0
    if (strcmp(name,"Kevin Bacon")==0) {
        printf("Score: 0\n");
        return 0;
    }

    // BFS on graph
    Queue* queue = queueConstructor();
    queuePush(queue,KB);
    while (queue->actors != NULL) {
        Actor* thisActor = queuePop(queue);
        thisActor->mark=2; // mark as visited

        // see if we reached target
        if (strcmp(thisActor->name,name)==0) {
            printf("Score: %d\n",thisActor->baconScore);
            free(queue);
            return 0;
        }

        // look at our neighbors
        Edge* thisEdge = thisActor->edges;
        Queue* visitedNeighbors = queueConstructor();
        while (thisEdge != NULL) {
            // if neighbor's already visited or in queue, skip
            if (thisEdge->to->mark == 0) {
                // update the neighbor's bacon score, but only if we haven't done so
                // if in more than one movie with an actor, will have multiple edges bwn them
                Actor* duplicate = getSecondaryQueueElement(visitedNeighbors,thisEdge->to->name);
                if (duplicate == NULL) {
                    thisEdge->to->baconScore = thisActor->baconScore + 1;
                    thisEdge->to->mark = 1;
                    secondaryQueuePush(visitedNeighbors,thisEdge->to);
                    // add neighbor to main processing queue
                    queuePush(queue,thisEdge->to);
                }
            }
            thisEdge = thisEdge->nextSequentialEdge;
        }
        freeSecondaryQueue(visitedNeighbors);

    }
    // if we haven't found the actor yet, then there's no connection
    printf("Score: No Bacon!\n");
    free(queue);
    return 0;
}
