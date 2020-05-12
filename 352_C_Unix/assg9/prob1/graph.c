/*
* File: graph.c
* Author: Alexander Miller
* Description: utility functions for graph in mymake.c
* Note: Functions ordered alphabetically, for semantic relationship, see graph.h
*/

#include "./graph.h"

/*
* ADD CMD
* adds a command to a node
*/
void addCmd(Node* node, Cmd* newCmd) {
    // base case: first cmd
    if (node->cmds == NULL) {
        node->cmds = newCmd;
        return;
    }
    // general case: add to end of cmds
    Cmd* thisCmd = node->cmds;
    while (thisCmd != NULL) {
        if (thisCmd->nextCmd == NULL) {
            thisCmd->nextCmd = newCmd;
            return;
        }
        thisCmd = thisCmd->nextCmd;
    }
}

/*
* ADD EDGE
* adds an edge to a node
*/
void addEdge(Node* node, Edge* newEdge) {
    // base case: first edge
    if (node->edges == NULL) {
        node->edges = newEdge;
        return;
    }
    // general case: add to end of edges:
    Edge* thisEdge = node->edges;
    while (thisEdge != NULL) {
        if (thisEdge->nextEdge == NULL) {
            thisEdge->nextEdge = newEdge;
            return;
        }
        thisEdge = thisEdge->nextEdge;
    }
}

/*
* ADD NODE
* adds a node to the graph (adds to list of nodes so we can recall that it exists)
* doesn't add the node in a meaningful sense (doesn't set appropriate dependencies etc.)
* NOTE: assumes that the root exists; don't use addNode to add the very first node
*/
void addNode(Node* root, Node* newNode) {
    Node* thisNode = root;
    while (thisNode != NULL) {
        if (thisNode->nextSequentialNode == NULL) {
            thisNode->nextSequentialNode = newNode;
            return;
        }
        thisNode = thisNode->nextSequentialNode;
    }
}

/*
* COMPARE TIMES
* compares modify time of a node with the stat struct of the target
* apparently at time of this check, if node doesn't exist, need to update target
*   note that at this time, we've already validated the node for not existing
*       and not being target simultaneously
* Pre-Conditions:
*   rootFile actually exists and rootFileInfo has valid information
* Returns:
*   1 -> need to build target, 0 -> no need to build target at this time
*/
int compareTimes(struct stat* rootFileInfo, Node* node) {
    struct stat* nodeFileInfo = malloc(sizeof(struct stat));
    memoryCheck(nodeFileInfo);
    int fileCheckResult = stat(node->name,nodeFileInfo);
    // if node doesn't exist, update target
    if (fileCheckResult != 0) {
        free(nodeFileInfo);
        return 1;
    }
    // perform time comparison
    // if node1 has more seconds/ns than node2, then more time has elapsed since its creation,
    //      so it is younger
    time_t rootFileTimeS = rootFileInfo->st_mtim.tv_sec;
    time_t nodeFileTimeS = nodeFileInfo->st_mtim.tv_sec;
    if (rootFileTimeS == nodeFileTimeS) {
        long rootFileTimeNS = rootFileInfo->st_mtim.tv_nsec;
        long nodeFileTimeNS = nodeFileInfo->st_mtim.tv_nsec;
        if (nodeFileTimeNS > rootFileTimeNS) {
            free(nodeFileInfo);
            return 1;
        } else {
            free(nodeFileInfo);
            return 0;
        }
    } else if (nodeFileTimeS > rootFileTimeS) {
        free(nodeFileInfo);
        return 1;
    } else {
        free(nodeFileInfo);
        return 0;
    }
}

/*
* CMD CONSTRUCTOR
* returns a CMD pointer
* nextCmd set to NULL; cmdString deep copied on heap
*/
Cmd* cmdConstructor(char* cmdString) {
    Cmd* newCmd = malloc(sizeof(Cmd));
    memoryCheck(newCmd);
    newCmd->cmdString = strdup(cmdString);
    memoryCheck(newCmd->cmdString);
    newCmd->nextCmd = NULL;
    return newCmd;
}

/*
* EDGE CONSTRUCTOR
* returns an Edge pointer
* nextEdge set to NULL
*/
Edge* edgeConstructor(Node* from, Node* to) {
    Edge* newEdge = malloc(sizeof(Edge));
    memoryCheck(newEdge);
    newEdge->from = from;
    newEdge->to = to;
    newEdge->nextEdge = NULL;
    return newEdge;
}

/*
* FREE GRAPH
* frees your malloc'd graph
*/
void freeGraph(Node* root) {
    Node* previousNode = NULL;
    Node* thisNode = root;
    while (thisNode != NULL) {
        // free name:
        free(thisNode->name);
        // free the edges:
        Edge* previousEdge = NULL;
        Edge* thisEdge = thisNode->edges;
        while (thisEdge != NULL) {
            previousEdge = thisEdge;
            thisEdge = thisEdge->nextEdge;
            free(previousEdge);
        }
        // free the commands:
        Cmd* previousCmd = NULL;
        Cmd* thisCmd = thisNode->cmds;
        while (thisCmd != NULL) {
            free(thisCmd->cmdString);
            previousCmd = thisCmd;
            thisCmd = thisCmd->nextCmd;
            free(previousCmd);
        }
        // free the node itself, and iterate:
        previousNode = thisNode;
        thisNode = thisNode->nextSequentialNode;
        free(previousNode);
    }
}

/*
* GET EDGE
* returns the edge from node A to node B, or NULL if none exists
* Pre-conditions: nodes A,B exist
*/
Edge* getEdge(Node* fromNode, Node* toNode) {
    Edge* thisEdge = fromNode->edges;
    while (thisEdge != NULL) {
        if (strcmp(thisEdge->to->name,toNode->name ) == 0) {
            return thisEdge;
        }
        thisEdge = thisEdge->nextEdge;
    }
    return NULL;

}

/*
* GET LAST NODE
* returns last added node to graph, or NULL if graph empty
*/
Node* getLastNode(Node* root) {
    Node* thisNode = root;
    while (thisNode != NULL) {
        if (thisNode->nextSequentialNode == NULL) {
            return thisNode;
        }
        thisNode = thisNode->nextSequentialNode;
    }
    return NULL;
}


/*
* GET NODE
* checks the graph for an existing node of the same name
* returns pointer to node if exists, or null
*/
Node* getNode(Node* root, char* nodeName) {
    Node* thisNode = root;
    while (thisNode != NULL) {
        if ( strcmp(nodeName,thisNode->name) == 0) {
            return thisNode;
        }
        thisNode = thisNode->nextSequentialNode;
    }
    return NULL;
}

/*
* MEMORY CHECK
* checks if a pointer is null, if so, prints error message & does a hard program exit
*/
void memoryCheck(void* ptr) {
    if (ptr == NULL) {
        fprintf(stderr,"Error: out of memory!\n");
        exit(1);
    }
}

/*
* NODE CONSTRUCTOR
* makes a deep copy of name on the heap
* sets if target or not; sets mark to 0
* sets edges, cmds, and nextSequentialNode to NULL
*/
Node* nodeConstructor(char* name, int isTarget) {
    Node* newNode = malloc(sizeof(Node));
    memoryCheck(newNode);
    newNode->name = strdup(name);
    memoryCheck(newNode->name);
    newNode->isTarget = isTarget;
    newNode->mark = 0;
    newNode->build = 0;
    newNode->edges = NULL;
    newNode->cmds = NULL;
    newNode->nextSequentialNode= NULL;
    return newNode;
}

/*
* POST ORDER TRAVERSAL
* prints out a graph of nodes in post-order
* updates buildPerformed to equal 1 by reaching thru pointer if any commands
*    are run at any point [helps track trivial "target updated" case]
* Returns:
*    1 for fatal error, 0 for normal operation, 2 circular dependency
*       if circular dependency with child, don't perform the file comparison on that round
*/
int postOrderTraversal(Node* root, int* buildPerformedPtr) {
    // perform cycle detection and prevent infinite recursion
    // mark == 0 -> not visited; mark == 1 -> in process; mark == 2 -> visited
    if (root->mark == 0) {
        root->mark = 1;
    } else if (root->mark == 1) {
        fprintf(stderr,"Ignoring circular dependency\n");
        return 2;
    } else if (root->mark == 2) {
        return 0;
    }

    // grab the modify date for this node
    struct stat* rootFileInfo = malloc(sizeof(struct stat));
    memoryCheck(rootFileInfo);
    int fileCheckResult = stat(root->name,rootFileInfo);
    // if root does not exist:
    if (fileCheckResult != 0) {
        // if not a target, then exit with an error
        if (root->isTarget == 0) {
            fprintf(stderr,"Error: Dependency does not exist and has no build instructions.\n");
            free(rootFileInfo);
            return 1;
        } // if is a target, need to build
        else {
            root->build = 1;
        }
    }

    // traverse all edges
    Edge* thisEdge = root->edges;
    while (thisEdge != NULL) {
        // recurse down this node
        int recursionResult = postOrderTraversal(thisEdge->to,buildPerformedPtr);
        // if error down the line, exit back up immediately
        if (recursionResult == 1) {
            free(rootFileInfo);
            return 1;
        }
        // now that we have traversed this node, if target exists, compare times
        // if circular dependency, skip the time comparison
        if (fileCheckResult == 0 && recursionResult != 2) {
            // if build required, compareTimes returns 1
            if (compareTimes(rootFileInfo,thisEdge->to)==1) {
                root->build = 1;
            }
        }
        thisEdge = thisEdge->nextEdge;
    }

    // if we need to build this node, print and run the commands:
    if (root->build == 1) {
        Cmd* thisCmd = root->cmds;
        while (thisCmd != NULL) {
            *buildPerformedPtr = 1;
            printf("%s\n",thisCmd->cmdString);
            int sysResult = system(thisCmd->cmdString);
            if (sysResult != 0) {
                fprintf(stderr,"Error: command failed.\n");
                free(rootFileInfo);
                return 1;
            }
            thisCmd = thisCmd->nextCmd;
        }
    }

    // mark node as complete
    root->mark = 2;
    // free memory
    free(rootFileInfo);
    return 0;
}
