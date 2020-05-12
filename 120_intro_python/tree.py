"""
File: tree.py
Author: Alexander Miller
Purpose:
* This file contains the Tree class for implementation in phylo.py
* note: instances contain GenomeData class instances
    * this provides for easy and direct access to all relevant information
"""
import copy

class Tree:
    """
    Description: instances represent tree elements and may be leaves, branches\
    (or roots)
    """
    def __init__(self,name,data):
        """
        Description: initializes Tree class instance
        Parameters: name, data
        """
        self._name = name
        self._left = None
        self._right = None
        self._data = data #Note: this MUST be a GenomeData instance

        # these update after becoming a branch:
        self._similarity_dictionary_nominal = {}
        self._similarity_dictionary_numerical = {} 
        self._max_similarity = None 
        self._leaf_list = []
        self._leaf_list_names = []
        
    #getters:
    def name(self):
        """
        Description: returns name
        Returns: self._name
        """
        return self._name
    def left(self):
        """
        Description: returns left child
        Returns: self._left
        """
        return self._left
    def right(self):
        """
        Description: returns right child
        Returns: self._right 
        """
        return self._right
    def data(self): #note: GenomeData instance
        """
        Description: access GenomeData instance (leaf)
        Returns: self._data
        """
        return self._data
    def leaf_list(self):
        """
        Description: collection of sub-leaves/branches (objects) for reference
        Returns: self._leaf_list
        """
        return self._leaf_list
    def leaf_list_names(self):
        """
        Description: collection of sub-leaf/branch (names) for reference
        Returns: self._leaf_list_names
        """
        return self._leaf_list_names
    def max_similarity(self):
        """
        Description: returns max_similarity in similarity dictionary
        Returns: self._max_similarity
        """
        return self._max_similarity
    def similarity_dictionary_nominal(self):
        """
        Description: fetches nominal similarity dictionary
        Returns: self._similarity_dictionary_nominal
        """
        return self._similarity_dictionary_nominal
    def similarity_dictionary_numerical(self):
        """
        Description: fetches numerical similarity dictionary
        Returns: self._similarity_dictionary_numerical
        """
        return self._similarity_dictionary_numerical
                
    # setters:
    def update_leaf_list(self,leaf):
        """
        Description: updates leaf_list and leaf_list_names\
        with newly added branches/leaves
        Parameters: self,leaf
        """
        self._leaf_list += [leaf]
        leaf_list = copy.deepcopy(leaf.leaf_list())
        self._leaf_list += leaf_list
        for i in self.leaf_list():
            self._leaf_list_names += [i.name()]
        
    def update_dictionaries(self,nominal_dictionary):
        """
        Description: updates branch with passed-in nominal dictionary and \
        creates numerical dictionary from that
        Parameters: self, nominal_dictionary
        """
        # assign nominal dictionary
        self._similarity_dictionary_nominal = nominal_dictionary
        # create numerical dictionary
        for x in nominal_dictionary:
            string = x
            number = nominal_dictionary[x]
            self._similarity_dictionary_numerical[number] = string
        num_list = []
        for i in self.similarity_dictionary_numerical():
            num_list += [i]
        self._max_similarity = max(num_list)
        return
    
    def set_right(self,tree):
        """
        Description: sets right child
        Parameters: tree (child)
        """
        self._right = tree
    def set_left(self,tree):
        """
        Description: sets left child
        Parameters: tree (child)
        """
        self._left = tree
    def establish_similarity(self,other):
        """
        Description: initializes similarity relationships
        note: passes the operation down to the GenomeClass
        note: ONLY FOR USE WHEN LEAF
        Parameters: other (Tree)
        """
        try:
            self.data().establish_similarity(other)
        except:
            pass
            
    # misc:
    def is_leaf(self):
        """
        Description: returns T if leaf and F otherwise
        Returns: T/F
        """
        if (self._left == None) and (self._right == None):
            return True
        else:
            return False
    def __str__(self):
        """
        SOURCE: SPECS
        Description: print instructions a la specs
        Returns: self._name(), format print instructions
        """
        if self.is_leaf():
            return self.name()
        else:
            return '({}, {})'.format(str(self.left()),str(self.right()))
