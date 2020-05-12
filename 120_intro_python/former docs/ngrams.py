"""
File: ngrams.py
Author: Alexander Miller
Purpose:
* read file and n from input without prompt
    * create list of words, split at whitespace
    * strip punctuation from ends of words; then discard empty strings
* construct n-grams and count occurences (case insensitive)
    * find maximum occurring n-grams
* print one per line using format statement
    * will need to separate words in n-gram by whitespace
* assertions:
    * any preconditions for all methods
    * one assert per substantive loop (compute value; transform data)
* use class system detailed in specs

"""

import string

class Input:
    def __init__(self):
        """
        Description: initializes file object
        Parameters: none
        Returns: a file object
        Pre-condition: file exists
        Post-Condition: file object will be object
        """
        ### ASSUMPTION: file is readable
        filename = str(input())
        self._openfile = open(filename)
        
    def close(self):
        """
        Description: closes file object
        Parameters: none
        Returns: none
        Pre-condition: file object is open
        Post-Condition: closes file object
        """
        self._openfile.close()
        
    def wordlist(self):
        """
        Description: processes document to acquire data
        Parameters: none
        Returns: none
        Pre-condition: file is readable
        Post-Condition: self._list will be a list containing necessary data
        """
        ### ASSUMPTION: file is readable
        self._openfile
        file_list = []
        for line in self._openfile:
            line_list = line.split()
            accum = 0
            while accum != len(line_list):
                try:
                    while line_list[accum][-1] in string.punctuation:
                        line_list[accum] = \
                                         line_list[accum].rstrip(line_list[accum][-1])
                    while line_list[accum][0] in string.punctuation:
                        line_list[accum] = \
                                         line_list[accum].lstrip(line_list[accum][0])
                    # assert to check that strips worked correctly
                    assert line_list[accum][-1] and \
                           line_list[accum][0] not in string.punctuation
                except IndexError:
                    pass
                line_list[accum] = line_list[accum].lower()
                if line_list[accum] != '':
                    file_list = file_list + [line_list[accum]]
                # assert to check that no list elements are blankspace
                for i in file_list:
                    assert i != ''
                accum = accum + 1
        self.close()
        self._list = file_list # new list : file._list
        assert len(self._list) == len(file_list)

                
class Ngrams:
    def __init__(self):
        """
        Description: initializes ngrams object
        Parameters: none
        Returns: none
        Pre-condition: file._list exists
        Post-Condition: ngrams object will be initialized
        """
        n = int(input())
        assert type(n)==int
        self._count = 0
        self._n = n
        self._dict = {}
        self._winnerslist = []
    def update(self,i):
        """
        Description: updates count 
        Parameters: i, element of self._dict
        Returns: none
        Pre-condition: i can be indexed
        Post-Condition: count will be updated
        """
        self._count = self._dict[i][1] 
    def __str__(self):
        """
        Description: gives print instructions
        Parameters: none
        Returns: self._dict
        Pre-condition: none
        Post-Condition: none
        """
        return self._dict
    def process_wordlist(self, other):
        """
        Description: processes list of data to count ngrams 
        Parameters: other, an object 
        Returns: none
        Pre-condition: other contains a list with necessary data
        Post-Condition: will have printed most numerous ngrams
        """
        assert type(other._list) == list
        accum = 0
        while accum != len(other._list):
            cgram = other._list[accum:accum + self._n]
            assert type(cgram) == list
            cgram_phrase = ' '.join(cgram)
            if len(cgram) == self._n: #removes shorter cgrams from very end of file
                if cgram_phrase not in self._dict:
                    self._dict[cgram_phrase] = [cgram_phrase,1]
                else:
                    self._dict[cgram_phrase][1] += 1
            assert len(self._dict) <= len(other._list)
            accum = accum + 1
        # establishing max. occurrences
        for i in self._dict:
            assert type(i) == str
            if self._dict[i][1] > self._count:
                self.update(i)
        for x in self._dict:
            assert self._count >= self._dict[x][1]
            if self._dict[x][1] == self._count:
                self._winnerslist = self._winnerslist + [self._dict[x][0]]
    def print_max_ngrams(self):
        if self._n != 0:
            for i in self._winnerslist:
                print("{:d} -- {}".format(self._count, i))
        else:
            pass
                

def main():
    file = Input()
    file.wordlist()
    ngram = Ngrams()
    ngram.process_wordlist(file)
    ngram.print_max_ngrams()
    
    
main()


