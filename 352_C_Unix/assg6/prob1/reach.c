/*
* File: reach.c
* Author: Alexander Miller
* Program Arguments: (optional) name of a file
*   If file specified, take input from file; else from stdin
*   If too many args given, ignore extras, print error, report out error
*   If given file doesn't exist or is non-readable, fatal error
* Input: sequence of directives, one per line, of the form "op args"
*   Args: 0+ op arguments; number depends on operation
*   Vertex Names: at most 64 chars, alphanumeric, case-sensitive
*   Edges are directed
*   Ops: @n, @e, @q
*       @n vName    -> declare vertex for graph
*       @e vName1 vName2    -> declare edge for prev. declared nodes
*           if edge already exists, ignore; not an error
*       @q vName1 vName2    -> ask if path exists from v1 to v2
*   Invalid input: print error, ignore, continue; indicate error occurred
*
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// VALIDATE STRING
// validates a string for alphanumeric characters
// returns 0 if invalid, else returns 1
int validateString(char *str) {
    int len = strlen(str);
    int i;
    for (i=0;i<len;i++){
        char thisChar = *(str+i);
        if (thisChar <= 47 || (thisChar>=58 && thisChar <=64) ||
            (thisChar >= 91 && thisChar <= 96) || thisChar >= 123 ) {
                return 0;
        }
    }
    return 1;
}

// NODE
typedef struct Node {
    // unique identifier for a node:
    char *name;
    // allows us to see if we have been here before in graph traversal:
    int mark;
    // stores the "edges":
    struct Edge *edges;
    // allows us to easily traverse all nodes to reset marks, or find a particular node:
    struct Node *nextSequentialNode;
} Node;

// EDGE
typedef struct Edge {
    Node *destination;
    struct Edge *nextEdge;
} Edge;

// NODE CONSTRUCTOR
// mallocs a node pointer, initializes its values, returns the pointer
Node *nodeConstructor(char *name) {
    Node *node = malloc(sizeof(Node));
    if (node == NULL) {
        fprintf(stderr,"Out of memory! Goodbye.\n");
        exit(1);
    }
    node->name = strdup(name);
    node->mark = 0;
    node->edges = NULL;
    node->nextSequentialNode = NULL;
    return node;
}

// EDGE CONSTRUCTOR
// mallocs an edge, intializes values, returns the pointer
Edge *edgeConstructor(Node *node) {
    Edge *edge = malloc(sizeof(Edge));
    if (edge == NULL) {
        fprintf(stderr,"Out of memory! Goodbye.\n");
        exit(1);
    }
    edge->destination = node;
    edge->nextEdge = NULL;
    return edge;
}

// FIND NODE
// returns the pointer of a node, given its name as input
// returns NULL if no node of that name found
Node *findNode(char *name, Node *root) {
    Node *thisNode = root;
    while (thisNode != NULL) {
        // if thisNode's name == name, we found the node
        if (strcmp(name,thisNode->name)==0) {
            return thisNode;
        }
        thisNode = thisNode->nextSequentialNode;
    }
    return NULL;
}

// ADD EDGE
// add an edge from node1 to node2
// if an edge already exists between these two nodes, then just ignores
void addEdge(Node *node1, Node *node2) {
    // construct the edge:
    Edge *newEdge = edgeConstructor(node2);
    // add the edge to node1:
    // if no edges, just add it:
    if (node1->edges == NULL) {
        node1->edges = newEdge;
        return;
    }
    // go over edges, check for duplicate edge, add to end if appl.
    Edge *thisEdge = node1->edges;
    while (thisEdge != NULL) {
        // if edge already exists to node2, essentially a no-op
        if (thisEdge->destination == node2) {
            free(newEdge);
            return;
        }
        // if next edge is null, add to end
        if (thisEdge->nextEdge == NULL) {
            thisEdge->nextEdge = newEdge;
            return;
        }

    }
}

// REINITIALIZE MARKS
// resets marks of all nodes to 0
void reinitializeMarks(Node *root) {
    Node *thisNode = root;
    while (thisNode != NULL) {
        thisNode->mark = 0;
        thisNode = thisNode->nextSequentialNode;
    }
}

// TRAVERSE GRAPH
// returns 1 if path exists, else returns 0
// note: will leave graph altered by "marking" nodes
int traverseGraph(Node *root, Node *target) {
    // if root has already been visited, return
    if (root->mark == 1) {
        return 0;
    } else {
        // mark this node as visited
        root->mark = 1;
    }
    // is this the node we're searching for?
    if (strcmp(root->name,target->name)==0) {
        return 1;
    }
    // base case: leaf node
    if (root->edges == NULL) {
        return 0;
    }
    // else check children:
    Edge *thisEdge = root->edges;
    while (thisEdge != NULL) {
        int result = traverseGraph(thisEdge->destination,target);
        if (result == 1) {
            return (1);
        }
        thisEdge = thisEdge->nextEdge;
    }
    // if we couldn't find thru our children, can't find it
    return 0;
}

// MAIN
int main(int argc, char *argv[]) {
    int terminationCondition = 0;
    /*
    * validate number of arguments:
    * if num_args = 1 -> no args were given [arg = name of exec]
    * if num_args = 2 -> attempt to read from file
    * if num_args > 2 -> error: too many inputs; attempt to read from file
    */
    FILE *SRC;
    if (argc > 2) {
        fprintf(stderr,"Error: too many arguments given.\n");
        terminationCondition = 1;
    }
    if (argc == 1) {
        SRC = stdin;
    } else {
        SRC = fopen(argv[1],"r");
        if (SRC == NULL) {
            fprintf(stderr,"Error: failed to open file.\n");
            return(1);
        }
    }
    // gather input:
    Node *root = NULL;
    Node *tail = NULL;
    char *lineBuffer = NULL;
    unsigned long storageCapacity = 0;
    char *N = "@n";
    char *E = "@e";
    char *Q = "@q";
    char op[4];
    char arg1[65];
    char arg2[65];
    int opNo;
    while (1) {
        int result = getline(&lineBuffer,&storageCapacity,SRC);
        if (result == -1) {
            break;
        }
        int scanResult = sscanf(lineBuffer,"%3s%64s%64s",op,arg1,arg2);
        // make sure we have at least an op provided and an arg1
        if (scanResult <= 1) {
            fprintf(stderr,"Error: Too few values provided.\n");
            continue;
        }
        opNo = 0;
        // op = @n
        if (strcmp(op,N)==0) {
            opNo = 1;
        } // op = @e
        else if (strcmp(op,E)==0) {
            opNo = 2;
        } // op = @q
        else if (strcmp(op,Q)==0) {
            opNo = 3;
        } // op not recognized
        else {
            fprintf(stderr,"Operation not recognized.\n");
            terminationCondition = 1;
            continue;
        }
        // validate arg 1 (common to all ops)
        if (validateString(arg1)==0) {
            fprintf(stderr,"First argument is invalid. Use alphanumeric characters.\n");
            continue;
        }
        // process @n
        if (opNo == 1) {
            // if we read too many arguments, report an error
            if (scanResult > 2) {
                fprintf(stderr,"Too many arguments for @n operation.\n");
                terminationCondition = 1;
                continue;
            }
            // make sure we if we haven't already added this node:
            Node *newNode = findNode(arg1,root);
            if (newNode != NULL) {
                fprintf(stderr,"This node already exists!\n");
                terminationCondition = 1;
                continue;
            }
            // otherwise, add a node
            newNode = nodeConstructor(arg1);
            if (root == NULL ) {
                root = newNode;
                tail = newNode;
            } else {
                tail->nextSequentialNode = newNode;
                tail = newNode;
            }
        } // process @e and @q
        else if (opNo == 2 || opNo == 3) {
            // if we read too many arguments, report an error
            if (scanResult > 3) {
                fprintf(stderr,"Error: too many arguments for the operation.\n");
                terminationCondition = 1;
                continue;
            }
            // validate arg 2
            if (validateString(arg2)==0) {
                fprintf(stderr,"Error: second argument is invalid. Use alphanumeric characters.\n");
                terminationCondition = 1;
                continue;
            }
            // validate that node names exist as nodes already
            Node *node1 = findNode(arg1, root);
            Node *node2 = findNode(arg2, root);
            if (node1 == NULL || node2 == NULL) {
                fprintf(stderr,"Error: at least one given node does not exist.\n");
                terminationCondition = 1;
                continue;
            }
            // @e -> add an edge from node1 to node2
            if (opNo == 2) {
                addEdge(node1,node2);
            } // @q -> traverse from node1 to node2
            else if (opNo == 3) {
                int pathFound = traverseGraph(node1,node2);
                reinitializeMarks(root);
                printf("%d\n",pathFound);
            }

        }

    }

    return terminationCondition;
}
