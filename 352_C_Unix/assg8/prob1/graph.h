/*
* File: graph.h
* Author: Alexander Miller
* Description: header file for graph.c
*
*/
#ifndef GRAPH_H
#define GRAPH_H

// ***************************** INCLUSIONS *************************
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// ***************************** STRUCTS *****************************
// CMD
typedef struct Cmd {
    char* cmdString;
    struct Cmd* nextCmd;
} Cmd;

// NODE
typedef struct Node {
    char* name;
    int isTarget;
    int mark;
    struct Edge* edges;
    Cmd* cmds;
    struct Node* nextSequentialNode;
} Node;

// EDGE
typedef struct Edge {
    Node* from;
    Node* to;
    struct Edge* nextEdge;
} Edge;

// **************************** FUNCTIONS *******************************
// CMD FUNCTIONS:
Cmd* cmdConstructor(char* cmdString);

// NODE FUNCTIONS:
Node* nodeConstructor(char* name, int isTarget);
void addCmd(Node* node, Cmd* newCmd);
void addEdge(Node* node, Edge* newEdge);
Edge* getEdge(Node* fromNode, Node* toNode);

// EDGE FUNCTIONS:
Edge* edgeConstructor(Node* from, Node* to);

// GRAPH FUNCTIONS:
void postOrderTraversal(Node* root);
void addNode(Node* root, Node* newNode);
Node* getNode(Node* root, char* nodeName);
Node* getLastNode(Node* root);
void freeGraph(Node* root);


#endif
