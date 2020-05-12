"""
File: fake-news.py
Author: Alexander Miller
Purpose:
* read in filename from input:  input('File: ')
* validate input file
* lines beginning with '#' should be ignored
* target field of line is 4, the title of the story
* extract the title from each input line
* divide title into list of words by spliting at whitespace\
 AND at punctuation, then stripping punctuation and whitespace (weird)
* use string.punctuation and string.whitespace as references\
 (import string first)
* discard words with len <= 2
* convert all words to lower case
***
* use a linked list to maintain a count of the number of times\
 each word occurs across all of the input data
    * if w occurs in linked list, update its count
    * otherwise, add w to linked list and initialize count
***
* sort linked list in descending order by *count*
***
* read in an integer N using: input('N: ')
* validate N as integer-compatible (make integer) and N should be >= 0
* N is the *position* of the desired word in the sorted list\
(not its count)
* if k is the count at that position, print out all words with count\
>= k

"""

import csv
import string
import sys

##### START: CLASSES #####
class Node:
    """
    Description: instances are nodes in LinkedList
    """
    def __init__(self, word):
        """
        Purpose: initialize Node instance
        Parameters: word
        """
        self._word = word
        self._count = 1
        self._next = None
# getters
    def word(self):
        """
        Purpose: gets word
        Returns: self._word
        """
        return self._word
    def count(self):
        """
        Purpose: gets count
        Returns: self._count
        """
        return self._count
    def next(self):
        """
        Purpose: gets next node
        Returns: self._next
        """
        return self._next
# setters
    def set_next(self, target):
        """
        Purpose: sets next node
        Parameters: target
        """
        if target != None:
            self._next = target
    def incr(self):
        """
        Purpose: increments count
        """
        self._count += 1
# misc
    def __str__(self):
        """
        Purpose: gives print instructions
        Returns: self._word
        """
        return self._word
    

class LinkedList:
    """
    Description: instances are LinkedLists
    """
    def __init__(self):
        """
        Purpose: initializes LinkedList instance
        Returns: self._head
        """
        self._head = None
# getters
    def head(self):
        """
        Purpose: gets head
        Returns: self._head
        """
        return self._head
# setters
    def update_count(self, word):
        """
        Purpose: adds words as Node objects and increments counts
        Parameters: word
        Post-Condition: either adds the Node object or increments the count
        """
        node = self.head()
        # first case
        if node == None:
            word = Node(word)
            self.add(word)
            return
        # normal case
        while node.next() != None:
            if word == node.word():
                node.incr()
                return
            node = node.next()
        # check last case
        if word == node.word():
            node.incr()
            return
        else:
            word = Node(word)
            self.add(word)

    ### SOURCE: ll_sort - short problems ###
    def add(self, node):
        """
        Purpose: adds node to front of LinkedList instance
        Parameters: node
        """
        node._next = self._head
        self._head = node
    
    ### SOURCE: ll_sort - short problems ###
    def insert_after(self,node1,node2):
        """
        Purpose: inserts node 2 after node 1
        """
        assert node1 != None
        node2._next = node1._next
        node1._next = node2
    
    ### SOURCE: ll_sort - short problems ###
    def rm_from_hd(self):
        """
        Purpose: removes a node from the head of LinkedList object
        Returns: _node
        """
        assert self._head != None
        _node = self._head
        self._head = _node._next
        _node._next = None
        return _node
    
    # misc
    def __contains__(self, word):
        """
        Purpose: sets criteria for whether a Node object is "in" LinkedList
        Returns: False if Node object not in LinkedList, True otherwise
        """
        node = self.head()
        if node == None:
            return False
        while node.next() != None:
            if node.word() == word:
                return True
        # last case
        if node.word() == word:
            return True
        
    def is_empty(self):
        """
        Purpose: establishes if LinkedList instance is empty
        Returns: self._head == None
        """
        return self._head == None
    def __str__(self):
        """
        Description: prints nodes and counts over 1
        *primarily for use in debugging
        """
        node = self._head
        while node.next() != None:
            print(node)
            if node.count() > 1:
                print(str(node.count()))
            node = node.next()
        if node != None:
            print(node)
            if node.count() > 1:
                print(str(node.count()))
        return ' '
        

    def sort(self):
        """
        Purpose: takes a LinkedList instance and sorts it by counts
        Post-Condition: LinkedList instance now sorted by counts
        """
        new_list = LinkedList()
        while self._head != None:
            # initialize values
            node = self._head
            min_val = node.count()
            # get lowest value in list
            while node.next() != None:
                if node.count() <= min_val:
                    min_val = node.count()
                node = node.next()
            # check last case
            if node.count() <= min_val:
                min_val = node.count()
            # checkpoint: min_val updated
            node = self._head # reinitialize node
            node_previous = self._head
            while node.next() != None:
                if node.count() == min_val:
                    # case: not the first node
                    if node_previous != node:
                        node_previous.set_next(node.next())
                        node.set_next(None)
                        new_list.add(node)
                        node = node_previous.next()
                        continue
                    else: # case: first node
                        new_list.add(self.rm_from_hd())
                        node_previous = self._head
                        node = self._head
                        continue
                node_previous = node
                node = node.next()
            # check last case
            if node.count() == min_val:
                node_previous._next = node.next()
                new_list.add(node)
                # very last node in original list case
                if node_previous == self._head:
                    self._head = None
                    break
        self._head = new_list._head
        new_list._head = None

    def get_nth_highest_count(self,N):
        """
        Purpose: gets nth highest count
        Parameters: N
        Returns: count
        """
        # objective: get target count
        accum = 0
        node = self._head
        while accum!= N:
            node = node.next()
            accum += 1
        count = node.count()
        return count
        
    def print_upto_count(self,count):
        """
        Purpose: prints up to count
        Parameters: count
        Post-Condition: objective of program complete
        """
        # objective: print nodes w/ target count
        node = self._head
        while node.next() != None:
            if node.count() >= count:
                print("{} : {:d}".format(node.word(), node.count()))
            node = node.next()
        if node.count() >= count:
            print("{} : {:d}".format(node.word(), node.count()))
##### END: CLASSES #####

            
def read_file():
    """
    Purpose: reads input file, validates it for existing files,
    removes any comment lines or empty lines,
    uses greater_title string to store titles
    Returns: greater_title
    """
    filename = input('File: ')
    greater_title = ''
    # objective: validate for existing file
    try:
        openfile = open(filename)
    except:
        print('ERROR: Could not open file '+ filename)
        sys.exit(1)
    csvreader = csv.reader(openfile)
    for itemlist in csvreader:
        if itemlist != []: #controls for empty lines
            if itemlist[0][0] != '#':
                greater_title = greater_title + ' ' + itemlist[4]
    openfile.close()
    return greater_title

### START: SUBFUNCTION FOR PROCESS_TITLES ###
def split_processing(z,y,i):
    """
    * Note: subfunction of process_titles()
    Purpose: template for splitting on whitespace and punctuation
    Parameters: z, y, i
    * where z = target list
    * y = string.punctuation or string.whitespace,\
    * and i = element of list
    Returns: z
    """
    if y == string.whitespace:
        c = '-'
    else:
        c = ' '
    for x in y:
        if x in i:
            i = i.split(x) #removes resp. puncutation
            i = c.join(i) #joins the parts pack for another iteration
    i = i.lower()
    i = i.split(c)
    for q in i:
        if len(q) > 2: # control for very short words
            z.append(q)
    return z
### END: SUBFUNCTION FOR PROCESS_TITLES ###


def process_titles(greater_title):
    """
    Purpose: takes greater_title string and splits it via\
    split_processing() subfunction, validates for empty file
    Parameters: greater_title
    Returns: cleaned_list
    """
    title_list = greater_title.split()
    interim_list = []
    cleaned_list = []
    # objective: strip punctuation and whitespace even from inside of words
    for i in title_list:
        interim_list = split_processing(interim_list, string.punctuation, i)
    for i in interim_list:
        cleaned_list = split_processing(cleaned_list, string.whitespace, i)
    # objective: validates for empty file
    if cleaned_list == []:
        sys.exit(1)
    return cleaned_list
            
def linked_list_staging(cleaned_list):
    """
    Purpose: builds LinkedList, sorts LinkedList, validates N,\
    and prints out Node objects
    Post-Condition: program objectives terminated.
    """
    # objective: build list and sort
    L = LinkedList()
    for i in cleaned_list:
        L.update_count(i)
    L.sort()
    # objective: validate N
    try:
        N = int(input('N: '))
    except:
        print('ERROR: Could not read N')
        sys.exit(1)
    assert N >= 0
    # objective: print
    count = L.get_nth_highest_count(N)
    L.print_upto_count(count)

        
def main():
    """
    Description: Mission Control
    """
    greater_title = read_file()
    cleaned_list = process_titles(greater_title)
    linked_list_staging(cleaned_list)

main()

