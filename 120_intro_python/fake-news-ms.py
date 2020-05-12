"""
File: fake-news-ms.py
Author: Alexander Miller
Description:
* new iteration of the fake news program
* contains Word class with specific methods
* python list of Word instances will be sorted using a merge sort alogrithm

"""

import csv
import string
import sys

class Word:
    """
    Description: instances save a word and its counts
    """
    def __init__(self, word):
        """
        Purpose: initializes Word class instance
        Parameters: self,word
        """
        self._word = word
        self._count = 1
# getters
    def word(self):
        """
        Purpose: returns the actual word
        Returns: self._word
        """
        return self._word
    def count(self):
        """
        Purpose: returns the word's count
        Returns: self._count
        """
        return self._count
# setters
    def incr(self):
        """
        Purpose: increment's word's count
        """
        self._count += 1
# misc
    def __str__(self):
        """
        Purpose: gives print instructions
        Returns: self._word
        """
        return self._word


def read_file():
    ### SOURCE: FAKE-NEWS.PY ###
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
    ### SOURCE: FAKE-NEWS.PY ###
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
    ### SOURCE: FAKE-NEWS.PY ###
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


def create_word_instances(cleaned_list):
    """
    Purpose: creates Word class instances
    Parameters: cleaned_list
    Returns: word_list
    """
    word_list = []
    accum = 0
    while accum != len(cleaned_list):
        i = cleaned_list[accum]
        # first word case:
        if len(word_list) == 0:
            i = Word(i)
            word_list.append(i)
            continue
        # otherwise:
        accum2 = 0
        while accum2 != len(word_list):
            if i == word_list[accum2].word():
                # word found- increment
                word_list[accum2].incr()
                break
            accum2 += 1
        if accum2 == len(word_list): # no instance found
            i = Word(i)
            word_list.append(i)
        accum += 1
    return word_list

### START: SUBFUNCTIONS FOR SORT_WORD_INSTANCES ###
def merge(L1,L2,merged):
    """
    Purpose: merges 2 lists, both alphabetically and by hierarchy of count
    Parameters: L1,L2,merged
    Returns: merged+L1+L2
    """
    ### SOURCE (MODIFIED): CLASS NOTES ###
    if L1 == [] or L2 == []:
        return merged + L1 + L2
    else:
        if L1[0].count() > L2[0].count():
            new_merged = merged + [L1[0]]
            new_L1 = L1[1:]
            new_L2 = L2
        # case where counts are the same- now compare alphabetically
        elif L1[0].count() == L2[0].count():
            # have to switch direction of comparison (increasing order)
            if L1[0].word() < L2[0].word():
                new_merged = merged + [L1[0]]
                new_L1 = L1[1:]
                new_L2 = L2
            else:
                new_merged = merged + [L2[0]]
                new_L1 = L1
                new_L2 = L2[1:]
        else:
            new_merged = merged + [L2[0]]
            new_L1 = L1
            new_L2 = L2[1:]
        return merge(new_L1,new_L2,new_merged)
def msort(L):
    """
    Purpose: sorts a list via binary splitting and the merge function
    Returns: L
    """
    ### SOURCE: CLASS NOTES ###
    if len(L) <= 1:
        return L
    else:
        split_pt = len(L)//2
        L1 = L[:split_pt]
        L2 = L[split_pt:]
        sortedL1 = msort(L1)
        sortedL2 = msort(L2)
        return merge(sortedL1,sortedL2,[])

### END: SUBFUNCTIONS FOR SORT_WORD_INSTANCES ###



def sort_word_instances(word_list):
    """
    Purpose: implements msort to sort Word class instances,\
    gathers n, validates n, performs print instructions
    Parameters: word_list
    Post-Condition: program objectives complete
    """
    word_list = msort(word_list)
    try:
        n = int(input('N: '))
    except:
        print('ERROR: Could not read N')
        sys.exit(1)
    assert n >= 0
    word_of_interest = word_list[n]
    k = word_of_interest.count()
    for i in word_list:
        if i.count() >= k:
            print('{} : {:d}'.format(i.word(),i.count()))
            
def main():
    """
    Description: Mission Control
    """
    sys.setrecursionlimit(2500)
    greater_title = read_file()
    cleaned_list = process_titles(greater_title)
    word_list = create_word_instances(cleaned_list)
    sort_word_instances(word_list)
    
main()
