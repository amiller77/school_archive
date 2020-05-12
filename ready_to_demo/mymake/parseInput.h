/*
* File: parseInput.h
* Author: Alexander Miller
* Description: header file for parseInput.c
*
*/

#ifndef PARSEINPUT_H
#define PARSEINPUT_H

#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include "./graph.h"

void extractArguments(int argc, char* argv[], char** fileNamePtr, char** targetPtr);
Node* processMakefile(char* argv_1);




#endif
