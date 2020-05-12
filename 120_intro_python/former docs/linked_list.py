"""
File: linked_list.py
Author: Alexander Miller
Purpose:
* LinkedList Class skeleton for friends.py

"""
class Node:
    def __init__(self,name):
        """
        Purpose: initialize Node instance
        * contains a LinkedList attribute to store friends
        Parameters: name
        """
        self._name = name
        self._next = None
        self._friends = LinkedList()
# getters
    def name(self):
        """
        Purpose: gets name
        Returns: self._name
        """
        return self._name
    def next(self):
        """
        Purpose: gets next node
        Returns: self._next
        """
        return self._next
    def friends(self):
        """
        Purpose: gets friends of node
        Returns: self._friends
        """
        return self._friends
# setters
    def set_next(self, node):
        """
        Purpose: sets next node
        Parameters: node
        """
        self._next = node
    def add_friend(self, friend):
        """
        Purpose: adds a friend to linkedlist friend attribute
        Parameters: friend
        """
        self._friends.add(friend)
    def pop(self,friend):
        """
        Purpose: pops a friend
        Parameters: friend
        """
        self._friends.pop(friend)
# misc
    def __str__(self):
        """
        Purpose: gives print instructions
        * primarily for use debugging
        Returns: self._name + ': node'
        """
        return self._name + ': node'
    def __eq__(self, word):
        """
        Purpose: establishes conditions under which a string\
        represents the node
        Parameters: word
        Returns: self._name == word
        """
        return self._name == word


class LinkedList:
    def __init__(self):
        """
        Purpose: initializes linkedlist instance
        """
        self._head = None
# getters
    def get_person(self,person):
        """
        Purpose: gets a node (person) from the linkedlist
        Parameters: person
        Returns: node
        """
        node = self._head
        while node.next() != None:
            if node == person:
                return node
            node = node.next()
        if node == person:
            return node
# setters
    def add(self, node):
        """
        Purpose: adds a node to a linkedlist
        *adds to *head* of list
        Parameters: node
        """
        node.set_next(self._head)
        self._head = node
    def pop(self, friend):
        """
        Purpose: pops a friend
        Parameters: friend
        Returns: node (that was popped)
        """
        if self._head == None:
            return
        node = self._head
        node_previous = self._head
        while node.next() != None:
            if node == friend:
                node_previous.set_next(node.next())
                node.set_next(None)
                return node
            node = node.next()
        if node == friend:
            node_previous.set_next(node.next())
            node.set_next(None)
            return node
# misc
    def __contains__(self,word):
        """
        Purpose: establishes conditions under which a linkedlist\
        instance "contains" a node
        Parameters: word
        Returns: T/F
        """
        node = self._head
        if node == None:
            return False
        while node.next() != None:
            if node == word:
                return True
            node = node.next()
        if node == word:
            return True
        else:
            return False
        
    def __str__(self):
        """
        Purpose: gives print instructions for linkedlist instance
        * primarily for use debugging
        * prints an extra space at the end
        Returns: '' (last print line)
        """
        node = self._head
        if node == None:
            return ''
        while node.next() != None:
            if node == None:
                return ''
            print(node)
            node = node.next()
        if node == None:
            return ''
        print(node)
        return ''

    def compare_persons(self,name1,name2):
        """
        Purpose: goes through two nodes and compares their
        friends lists, then prints any friends in common
        as per specs
        Parameters: name1, name2
        Post-Conditions: program goals completed
        """
        # different 'tracks' for different friends lists
        track_1_node = name1.friends()._head
        track_2_node = name2.friends()._head
        # make sure friends lists aren't empty
        try:
            assert track_1_node != None
            assert track_2_node != None
        except:
            return
        # objective: iterate through friends list, make comparisons, and print
        first_go = 0 #trigger to print introductory statement
        while track_1_node != None:
            track_2_node = name2.friends()._head #reinitialize each round
            while track_2_node != None:
                if track_1_node.name() == track_2_node.name():
                    if first_go == 0: # have to print introductory statement as well
                        print('Friends in common:')
                        print(track_2_node.name())
                        first_go += 1
                    else:
                        print(track_2_node.name())
                track_2_node = track_2_node.next()
            track_1_node = track_1_node.next()


