"""
File: friends.py
Author: Alexander Miller
Purpose: 
* makes of a linked list class from linked_list.py
* read in name of input file: 'Input file: '
* establish friendships which are reciprocal
* read in name1 and name2 from input: 'Name x: '
* IF there are friends in common, print:
Friends in common:
mutual friends (one per line)
* Else print nothing after the prompts

"""
import sys
import string
from linked_list import *


def whitespace_stripper(word):
    """
    Purpose: strips whitespace from word
    Parameters: word
    Returns: word
    """
    while word[-1] in string.whitespace:
        word = word.strip()
    while word[0] in string.whitespace:
        word = word.strip()
    return word


def read_file_and_create_linked_lists():
    """
    Purpose: read file and create linked lists from nodes
    Returns: L
    Post-Conditions: L will be a linkedlist full of nodes
    """
    filename = input('Input file: ')
    try:
        openfile = open(filename)
    except:
        print('ERROR: Could not open file ' + filename)
        sys.exit(1)
    # initialize LinkedList
    L = LinkedList()
    for line in openfile:
        # objective: clean line and get it into a format to process
        line = line.strip()
        line_list = line.split()
        for i in line_list:
            whitespace_stripper(i)
        # objective: make nodes of persons and add to person list
        for person in line_list:
            if person not in L:
                person = Node(person)
                L.add(person)
        # objective: add friends to person_of_interest if applicable                      
        accum = 0
        while accum != len(line_list):
            person_of_interest = line_list[accum]
            # get node version of person:
            person_of_interest = L.get_person(person_of_interest)
            other_persons = line_list[:accum]+line_list[accum+1:]
            for friend in other_persons:
                if friend not in person_of_interest.friends():
                    friend = Node(friend)
                    # unfortunately have to create new nodes here\
                    # otherwise it removes the node from L
                    person_of_interest.add_friend(friend)
            accum += 1
    openfile.close
    return L


def process_requests(L):
    """
    Purpose: processes user-input names and prints out\
    common friends in holding with the specs
    Parameters: L
    Pre-Conditions: L is a linkedlist full of node objects
    Post-Conditions: program objectives complete
    """
    # objective: take in names, pull them from L, and validate
    name1 = input('Name 1: ')
    try:
        assert name1 in L
    except:
        print('ERROR: Unknown person '+name1)
        sys.exit(1)
    name2 = input('Name 2: ')
    try:
        assert name2 in L
    except:
        print('ERROR: Unknown person ' + name2)
        sys.exit(1)
    name1 = L.get_person(name1)
    name2 = L.get_person(name2)
    L.compare_persons(name1,name2)
    

def main():
    """
    Description: Mission Control
    """
    L = read_file_and_create_linked_lists()
    process_requests(L)


main()

