/*
* File: shortestPaths.c
* Author: Alexander Miller
* Description: computes shortest distance between 2 nodes
*   in a weighted, undirected graph
* Program Parameters: inFile, the name of a file containing
*   a description of the graph
* Behavior:
*   read graph from file
*   read queries from stdin, compute shortest distance, print to std out
*   repeat query processing until no further input can be read
* Input File Format:
*   series of lines of the form: name1 name2 dist
*   strings are whitespace delineated
* Query Format:
*   each query on its own line, names separated by whitespace
* More on Inputs:
*   all names read in (query and file) are max length 64
*   Input graph connected and undirected, edges are non-negatively weighted
*   city names handled case-sensitively
* Output Format:
*   printf("%d\n",val);
* Fatal Errors:
*   not enough command line arguments
*   input file cannot be opened
* Non-fatal Errors:
*   anything else that doesn't meet specs, including:
*       too many command-line arguments
*       input line not in correct format
*       distance between same pair of cities specified multiple times
*       queries on cities that don't exist
*
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
* EDGE
* edges belong to a node, so 2 edges are required to connect
*   2 nodes, one for each node
*/
typedef struct Edge {
    int weight;
    struct Node *from;
    struct Node *to;
    // allows us to traverse all edges at a node
    struct Edge *nextEdge;
} Edge;

// NODE
typedef struct Node {
    // shortest path found to this node so far:
    int shortestPath;
    // indicate if we've been here before:
    int mark;
    // name of node:
    char *name;
    // edges at this node:
    Edge *edges;
    // allows us to "quickly" iterate over all nodes:
    struct Node *nextSequentialNode;
    // allows us to traverse nodes contained in a stack
    struct Node *nextStackNode;
} Node;

/*
* STACK
* our stack is used as a priority queue to process shortest paths non-traveled
*/
typedef struct Stack {
    Node *nodes;
} Stack;

/*
* EDGE CONSTRUCTOR
* mallocs an edge, returns its pointer
* initializes weights and nodes from parameters
* initializes nextEdge to NULL
*/
Edge *edgeConstructor(int weight, Node *from, Node *to) {
    Edge *thisEdge = malloc(sizeof(Edge));
    if (thisEdge == NULL) {
        fprintf(stderr,"Error: out of memory! Program aborting.\n");
        exit(1);
    }
    thisEdge->weight = weight;
    thisEdge->from = from;
    thisEdge->to = to;
    thisEdge->nextEdge = NULL;
    return thisEdge;
}

/*
* NODE CONSTRUCTOR
* mallocs an node, returns its pointer
* initializes paths to -1 (indicating we haven't found it)
* initializes all pointers to NULL, except name
*   makes a deep copy of name via a malloc
*/
Node *nodeConstructor(char *name) {
    Node *thisNode = malloc(sizeof(Node));
    char *nameCopy = strdup(name);
    if (thisNode == NULL || nameCopy == NULL) {
        fprintf(stderr,"Error: out of memory! Program aborting.\n");
        exit(1);
    }
    thisNode->shortestPath = -1;
    thisNode->name = nameCopy;
    thisNode->edges = NULL;
    thisNode->nextSequentialNode = NULL;
    thisNode->nextStackNode = NULL;
    thisNode->mark = 0;
    return thisNode;
}

/*
* STACK CONSTRUCTOR
* mallocs a stack, sets nodes pointer to NULL, returns stack pointer
*/
Stack *stackConstructor() {
    Stack *newStack = malloc(sizeof(Stack));
    if (newStack == NULL) {
        fprintf(stderr,"Error: out of memory! Program aborting.\n");
        exit(1);
    }
    newStack->nodes = NULL;
    return newStack;
}

/*
* PUSH
* used to push nodes onto the stack
* the stack sorts the nodes s.t. the node with the lowest
*   path length is on top
* pre-condition: requires that the path on the node be initialized
*   since non-initialized paths are -1, they will immediately go to the top of the stack
*/
void push(Stack *stack, Node *node) {
    // if stack empty, just push on top
    if (stack->nodes == NULL) {
        stack->nodes = node;
        return;
    }
    // otherwise insert in ascending order
    Node *thisNode = stack->nodes;
    Node *previousNode = NULL;
    while (thisNode != NULL) {
        // if insertion node has smaller path len, insert before thisNode
        if (node->shortestPath < thisNode->shortestPath) {
            if (previousNode != NULL) {
                previousNode->nextStackNode = node;
            } // if there is no previous node, then we insert at the head
            else {
                stack->nodes = node;
            }
            node->nextStackNode = thisNode;
            return;
        }
        previousNode = thisNode;
        thisNode = thisNode->nextStackNode;
    }
    // if we haven't inserted it yet, add it now
    previousNode->nextStackNode = node;
}

/*
* POP
* pops a node off the stack (this will be the minimum pathlength node)
* returns NULL if stack empty
*/
Node *pop(Stack *stack) {
    if (stack->nodes == NULL) {
        return NULL;
    }
    Node *returnNode = stack->nodes;
    stack->nodes = returnNode->nextStackNode;
    returnNode->nextStackNode = NULL;
    return returnNode;
}


/*
* REINITIALIZE NODES
* sets all the path lengths, marks back to default values, -1 and 0 resp.
*/
void reinitializeNodes(Node *root) {
    Node *thisNode = root;
    while (thisNode != NULL) {
        thisNode->shortestPath = -1;
        thisNode->mark = 0;
        thisNode = thisNode->nextSequentialNode;
    }
}


/*
* SOLVE GRAPH
* starting at source, traverses the graph to find shortest paths
*/
void solveGraph(Node *source) {
    // path to itself is trivially 0
    source->shortestPath = 0;
    source->mark = 1;
    // create a stack, sorted ascending, to retrieve minimum path nodes
    Stack *stack = stackConstructor();
    Node *thisNode = source;
    // keep going until we have visited all nodes
    while (thisNode != NULL) {
        // update path lengths based on what we can see from this node:
        Edge *thisEdge = thisNode->edges;
        while (thisEdge != NULL) {
            Node *targetNode = thisEdge->to;
            // if path length is uninitialized, or this path is shorter, update
            if (targetNode->shortestPath == -1 ||
                (targetNode->shortestPath > thisNode->shortestPath + thisEdge->weight) ) {
                    targetNode->shortestPath = thisNode->shortestPath + thisEdge->weight;
            }
            // if we haven't queued that node already, then we'll put it in the processing stack
            if (targetNode->mark == 0) {
                // mark the node as queued
                targetNode->mark = 1;
                push(stack,targetNode);
            }
            thisEdge = thisEdge->nextEdge;
        }
        // pop the next node to process off the stack, and repeat
        thisNode = pop(stack);
    }
    free(stack);
}

/*
* FIND NODE
* returns a pointer to a node if it exists, or null
*/
Node *findNode(Node *root, char *nodeName) {
    Node *thisNode = root;
    while (thisNode != NULL) {
        // if nodeName == node's name, we found it
        if ( strcmp(nodeName,thisNode->name)==0 ) {
            return thisNode;
        }
        thisNode = thisNode->nextSequentialNode;
    }
    return NULL;
}

/*
* ADD NODE
* adds a new node to the graph
* takes root node and node to add
* returns the root (so that if root is changed in the function, it can be reassign in caller)
*/
Node *addNode(Node *root, Node *newNode) {
    if (root == NULL) {
        root = newNode;
        return root;
    }
    Node *thisNode = root;
    while (thisNode != NULL) {
        if (thisNode->nextSequentialNode == NULL) {
            thisNode->nextSequentialNode = newNode;
            return root;
        }
        thisNode = thisNode->nextSequentialNode;
    }
    return root;
}

/*
* FIND EDGE
* returns 1 if edge to target exists, 0 else
*/
int findEdge(Node *fromNode, Node *toNode) {
    Edge *thisEdge = fromNode->edges;
    while (thisEdge != NULL) {
        // if the names match, edge exists
        if ( strcmp(thisEdge->to->name,toNode->name ) == 0) {
            return 1;
        }
        thisEdge = thisEdge->nextEdge;
    }
    return 0;
}

/*
* ADD EDGE
* adds an edge to a node
*/
void addEdge(Node *node, Edge *edge) {
    if (node->edges == NULL) {
        node->edges = edge;
        return;
    }
    Edge *thisEdge = node->edges;
    while (thisEdge != NULL) {
        if (thisEdge->nextEdge == NULL) {
            thisEdge->nextEdge=edge;
            return;
        }
        thisEdge = thisEdge->nextEdge;
    }
}

/*
* VALIDATE NAME
* returns 1 if name is valid, 0 else
* valid name consists of only alphabetical characters
*/
int validateName(char *name) {
    int len = strlen(name);
    int i;
    for (i=0;i<len;i++) {
        char thisChar = *(name+i);
        if ( thisChar <= 64 || (thisChar >= 91 && thisChar <= 96) ||
            thisChar >= 123) {
                return 0;
        }
    }
    return 1;
}

/*
* MAIN
*/
int main(int argc, char* argv[]) {
    int terminationCondition = 0;
    // validate number of parameters:
    if (argc < 2) {
        fprintf(stderr,"Error: Please provide a filename for the graph.\n");
        return(1);
    } else if (argc > 2) {
        fprintf(stderr,"Error: Too many program parameters provided. Those after the first ignored.\n");
        terminationCondition = 1;
    }
    char *fileName = argv[1];
    FILE *inputFile = fopen(fileName,"r");
    // validate that file is opened:
    if (inputFile == NULL) {
        fprintf(stderr,"Error: Input file could not be opened.\n");
        terminationCondition = 1;
        return(1);
    }

    // get data from file
    char *lineBuffer = NULL;
    unsigned long bufferSize = 0;
    char name1[65];
    char name2[65];
    char extraLineContents[2];
    Node *root = NULL;
    int scanResult = 0;
    while ( getline(&lineBuffer,&bufferSize,inputFile) != -1) {
        int distance = -1;
        // extract data:
        scanResult = sscanf(lineBuffer,"%64s%64s%d%1s",name1,name2,&distance,extraLineContents);
        // ensure that 3 fields are read in:
        if (scanResult != 3) {
            fprintf(stderr,"Error: poorly formed graph line.\n");
            terminationCondition = 1;
            continue;
        }
        // ensure names are legit
        if ( validateName(name1) == 0 || validateName(name2) == 0 ) {
            fprintf(stderr,"Error: non-alphabetical characters in name. Ignoring.\n");
            terminationCondition = 1;
            continue;
        }
        // ensure distance is non-negative and ensure that it was read in
        if (distance < 0) {
            fprintf(stderr,"Error: invalid distance given. Ignoring.\n");
            terminationCondition = 1;
            continue;
        }
        // otherwise, create nodes / edge if applicable
        Node *node1 = findNode(root,name1);
        Node *node2 = findNode(root,name2);
        if (node1 == NULL) {
            node1 = nodeConstructor(name1);
            root = addNode(root, node1);
        }
        if (node2 == NULL) {
            node2 = nodeConstructor(name2);
            root = addNode(root, node2);
        }
        // make sure edge doesn't already exist
        int edgeExists = findEdge(node1,node2);
        if (edgeExists) {
            fprintf(stderr,"Error: this edge already exists.\n");
            terminationCondition = 1;
            continue;
        }
        // add edges
        Edge *newEdge1 = edgeConstructor(distance,node1,node2);
        Edge *newEdge2 = edgeConstructor(distance,node2,node1);
        addEdge(node1,newEdge1);
        addEdge(node2,newEdge2);
    }
    fclose(inputFile);

    // now process user input from standard in:
    Node *lastNodeQueried = NULL;
    while ( getline(&lineBuffer,&bufferSize,stdin) != -1) {
        scanResult = sscanf(lineBuffer,"%64s%64s%1s",name1,name2,extraLineContents);
        if (scanResult < 2 || scanResult > 2) {
            fprintf(stderr,"Error: improperly formatted line. Ignoring.\n");
            terminationCondition = 1;
            continue;
        }
        // find nodes:
        Node *node1 = findNode(root,name1);
        Node *node2 = findNode(root,name2);
        // make sure nodes exist
        if (node1 == NULL || node2 == NULL) {
            fprintf(stderr,"Error: node does not exist.\n");
            terminationCondition = 1;
            continue;
        }
        // if last node queried is the same, just use previous results
        if (lastNodeQueried != NULL && strcmp(lastNodeQueried->name,node1->name) == 0) {
            printf("%d\n",node2->shortestPath);
        } // otherwise, clear the graph, solve it again, then print solution
        else {
            reinitializeNodes(root);
            solveGraph(node1);
            printf("%d\n",node2->shortestPath);
        }
        lastNodeQueried = node1;

    }

    // free memory:
    free(lineBuffer);
    Node *thisNode = root;
    while (thisNode != NULL) {
        // free the name string:
        free(thisNode->name);
        // free the edges for the node:
        Edge *thisEdge = thisNode->edges;
        while (thisEdge != NULL) {
            Edge *nextEdge = thisEdge->nextEdge;
            free(thisEdge);
            thisEdge = nextEdge;
        }
        // free this node, move to next node
        Node *previousNode = thisNode;
        thisNode = thisNode->nextSequentialNode;
        free(previousNode);
    }

    return terminationCondition;
}
