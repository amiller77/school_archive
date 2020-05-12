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

void validateNumArguments(int argc);
Node* processMakefile(char* argv_1);




#endif
