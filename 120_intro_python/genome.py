"""
File: genome.py
Author: Alexander Miller
Purpose:
* This program contains the GenomeData class for use in phylo.py
* note: instances contained within Tree class instances
    * this provides for easy and direct access to all relevant information
* note: GenomeData class also contains respective similarity dictionaries

"""

class GenomeData:
    """
    Description: Instances find ngram set after being given sequence and
    have a function for establishing similarity between species
    """
    def __init__(self,name,sequence):
        """
        Description: initializes GenomeData instance
        Parameters: self,name, sequence
        """
        self._name = name
        self._sequence = sequence #type: str
        self._ngrams = None # ultimately, type: set
        self._similarity_dictionary_numerical = {} #type: dictionary
        self._similarity_dictionary_nominal = {} #type: dictionary
        self._max_similarity = None # not yet intialized
    # getters:
    def name(self):
        """
        Description:returns name
        Returns: self._name
        """
        return self._name
    def sequence(self):
        """
        Description: returns genome sequence
        Returns: self._sequence
        """
        return self._sequence
    def ngrams(self):
        """
        Description: returns ngram set
        Returns: self._ngrams
        """
        return self._ngrams
    def similarity_dictionary_numerical(self):
        """
        Description: returns numerical similarity dictionary (BRANCH)
        Returns: self._similarity_dictionary_numerical
        """
        return self._similarity_dictionary_numerical
    def similarity_dictionary_nominal(self):
        """
        Description: returns nominal similarity dictionary (BRANCH)
        Returns: self._similarity_dictionary_nominal
        """
        return self._similarity_dictionary_nominal
    def max_similarity(self):
        """
        Description: returns max_similarity (BRANCH)
        Returns: self._max_similarity
        """
        return self._max_similarity
    # setters:
    def set_ngrams(self,n):
        """
        Description: establishes ngram set for GenomeData instance
        * makes use of short problem solution
        Parameters: self,n
        """
        char_list = [i for i in self._sequence]
        ngram_list = [''.join(char_list[z:][0:n]) for z in range (len(char_list) - (n-1))]
        self._ngrams = set(ngram_list)
    def establish_similarity(self,other):
        """
        Description: comparing 2 ngram sets of genomes
        * makes use of short problem solution
        Parameters: OTHER IS A TREE INSTANCE, NOT A GENOMEDATA INSTANCE
        * this is because it was passed down from the hierarchically superior
        Tree object
        Note: current build has values as keys -> compare keys, find highest\
        value, return species name with that highest value
        Parameters: self, other
        """
        intersection = [] # AND elements
        union = [] # OR elements
        for i in self.ngrams():
            union.append(i)
            if i in other.data().ngrams(): # item occurring in both
                intersection.append(i)
        for i in other.data().ngrams():
            union.append(i)
            if i in self.ngrams():
                intersection.append(i)
        intersection = set(intersection)
        union = set(union)
        jaccard_index = float(len(intersection))/float(len(union))
        self.similarity_dictionary_nominal()[other.name()] = jaccard_index
        # no duplicates for nominal so don't have to worry
        # numerical, however:
        if jaccard_index not in self.similarity_dictionary_numerical():
            self.similarity_dictionary_numerical()[jaccard_index] = other.name()
        else:
            value = self.similarity_dictionary_numerical()[jaccard_index]
            if type(value) == list:
                self.similarity_dictionary_numerical()\
                    [jaccard_index].append(other.name())
            else:
                self.similarity_dictionary_numerical()\
                    [jaccard_index] = [value]
                self.similarity_dictionary_numerical()\
                    [jaccard_index].append(other.name())
                
    def establish_max_similarity(self):
        """
        Description: updates highest similarity value for this leaf
        """
        value = 0
        for i in self.similarity_dictionary_numerical():
            if i > value:
                value = i
        self._max_similarity = value
        
    # misc:
    def __str__(self):
        """
        Description: gives print instructions (debugging)
        Returns: self.name()
        """
        return self.name()
    def __eq__(self,other):
        """
        Description: establishes equality (nominal)
        Returns: self.name() == other.name()
        """
        return self.name() == other.name()
