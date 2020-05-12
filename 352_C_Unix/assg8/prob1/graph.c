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
* CMD CONSTRUCTOR
* returns a CMD pointer
* nextCmd set to NULL; cmdString deep copied on heap
*/
Cmd* cmdConstructor(char* cmdString) {
    Cmd* newCmd = malloc(sizeof(Cmd));
    if (newCmd == NULL) {
        fprintf(stderr,"Error: out of memory! Abort.\n");
        exit(1);
    }
    newCmd->cmdString = strdup(cmdString);
    if (newCmd->cmdString == NULL) {
        fprintf(stderr,"Error: out of memory! Abort.\n");
        exit(1);
    }
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
    if (newEdge == NULL) {
        fprintf(stderr,"Error: out of memory! Abort.\n");
        exit(1);
    }
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
* NODE CONSTRUCTOR
* makes a deep copy of name on the heap
* sets if target or not; sets mark to 0
* sets edges, cmds, and nextSequentialNode to NULL
*/
Node* nodeConstructor(char* name, int isTarget) {
    Node* newNode = malloc(sizeof(Node));
    if (newNode == NULL) {
        fprintf(stderr,"Error: out of memory! Abort.\n");
        exit(1);
    }
    newNode->name = strdup(name);
    if (newNode->name == NULL) {
        fprintf(stderr,"Error: out of memory! Abort.\n");
        exit(1);
    }
    newNode->isTarget = isTarget;
    newNode->mark = 0;
    newNode->edges = NULL;
    newNode->cmds = NULL;
    newNode->nextSequentialNode= NULL;
    return newNode;
}

/*
* POST ORDER TRAVERSAL
* prints out a graph of nodes in post-order
*/
void postOrderTraversal(Node* root) {
    // since it's not a tree, prevent multiple visitation
    if (root->mark == 1) {
        return;
    } else {
        root->mark = 1;
    }
    // visit all children recursively:
    Edge* thisEdge = root->edges;
    while (thisEdge != NULL) {
        postOrderTraversal(thisEdge->to);
        thisEdge = thisEdge->nextEdge;
    }
    // print after visiting children: name then cmds
    printf("%s\n",root->name);
    Cmd* thisCmd = root->cmds;
    while (thisCmd != NULL) {
        printf("  %s\n",thisCmd->cmdString);
        thisCmd = thisCmd->nextCmd;
    }
}
