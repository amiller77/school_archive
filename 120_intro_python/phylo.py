"""
File: phylo.py
Author: Alexander Miller
Purpose:
* This is the main body of the phylogenetic tree program, which makes use of\
the Tree and GenomeData classes, contained in tree.py and genome.py resp.
* read in input parameters, name of input file and n
* read in input file, extract species ID, sequence
    * species ID occurs after '>' and ends at whitespace
    * sequence may occur over many lines, but should be concatenated into \
    single string
* compute similarity of ngram sets between species
* construct a phylogenetic tree based upon similarities
* print phylogenetic tree

"""
from genome import *
from tree import *
import string
import sys


def gather_input():
    """
    Description: creates species_list of GenomeData instances and tree_list
    of Tree instances after gathering input from file
    * validates input for file and n values
    Returns: tree_list, n
    Post-Condition: creates tree_list, which will be used throughout \
    the program
    """
    filename = input('FASTA file: ')
    n = input('n-gram size: ')
    # objective: validate inputs
    try:
        n = int(n)
    except:
        print('ERROR: Bad value for N')
        sys.exit(1)
    try:
        openfile = open(filename)
    except:
        print('ERROR: could not open file '+filename)
        sys.exit(1)
    # objective: gather species, create GenomeData instances, and store in list
    tree_list = []
    # default variable settings:
    name = ''
    sequence = ''
    for line in openfile:
        if line in string.whitespace:
            # this means it is time to store our information and\
            # clean the slate for new species
            name = name.strip()
            sequence = sequence.strip() #must remove newlines
            data = GenomeData(name,sequence)
            k = Tree(name,data)
            tree_list.append(k)
            name = ''
            sequence = ''
        elif line[0] == '>':
            # objective: process 'name' of species
            accum = 1
            while line[accum] not in string.whitespace:
                name += line[accum]
                accum += 1
        else: #genome sequence line
            sequence += line.strip()
    if len(tree_list) == 0: # catch for empty file
        sys.exit(1)
    return tree_list,n


def ngram_processing(tree_list,n):
    """
    Description: gets ngrams for each species, fills out similarity\
    dictionary for each species (leaf), stores max similarity for each leaf,
    and creates a similarity hierarchy for first iteration searching
    Parameters: tree_list, n
    Returns: tree_list, similarity_hierarchy
    """
    # objective: get ngrams for each species
    for x in tree_list:#accesses Tree object
        y = x.data()#accesses resp. GenomeData object
        y.set_ngrams(n)
    # objective: fill out similarity_dictionary for each species
    # note: similarity_dictionary contained within GenomeData object\
    # (which in turn is in Tree object)
    for i in tree_list: # iterate thru tree objects
        for x in tree_list:
            if i != x:
                i.establish_similarity(x)#updates similarity_dictionary\
                # for x resp. to i
    # storing max similarity for each leaf:
    for i in tree_list:
        i.data().establish_max_similarity()
    # objective: create a similarity hierarchy for more expedited searching
    similarity_set = []
    for i in tree_list:
        for x in i.data().similarity_dictionary_numerical():
            similarity_set.append(x)
    similarity_set = set(similarity_set)
    similarity_hierarchy = []
    for z in similarity_set:
        similarity_hierarchy.append(z)
    similarity_hierarchy.sort()
    return tree_list,similarity_hierarchy
    

##### SUBFUNCTIONS FOR TREE_BUILDER #####
def find_right_leaf(node):
    """
    Description: finds terminal right leaf on branch
    Parameters: node
    Returns: node
    """
    # node == branch root
    while node.right() != None:
        node = node.right()
    return node
def find_left_leaf(node):
    """
    Description: finds terminal left leaf on branch
    Parameters: node
    Returns: node
    """
    # node == branch root
    while node.left() != None:
        node = node.left()
    return node

def max_branch_similarity(tree_list):
    """
    Description: finds max similarity relationship among branches \
    remaining in tree_list
    Parameters: tree_list
    Returns: max_val
    """
    # objective: find highest similarity relationship among leaves
    val_list = []
    for x in tree_list:
        if x.similarity_dictionary_nominal() == {}:#leaf
            val_list += [x.data().max_similarity()]
        else: #branch
                val_list += [x.max_similarity()]
    max_val = max(val_list)
    return max_val

def update_branch_similarity(node,leaf_1,leaf_2):
    """
    Description: subfunction to update branch similarity dictionaries
    * makes use of update_dictionaries method
    * node is the branch,leaf_1 name of leaf on right, leaf_2\
    name of leaf on left
        * note: may be branches, not leaves
    Parameters: node, leaf_1, leaf_2
    """
    # gets composite dictionaries from different locations depending\
    # on if adding together branches or leaves
    if leaf_1.similarity_dictionary_nominal() == {}:
        dictionary1 = leaf_1.data().similarity_dictionary_nominal()
    else:
        dictionary1 = leaf_1.similarity_dictionary_nominal()
    if leaf_2.similarity_dictionary_nominal() == {}:
        dictionary2 = leaf_2.data().similarity_dictionary_nominal()
    else:
        dictionary2 = leaf_2.similarity_dictionary_nominal()
    # combine the dictionaries:
    combined_dictionary = {} #nominal
    for i in dictionary1:
        if i != leaf_2.name(): #dont want to include endogenous leaves
            if i not in leaf_2.leaf_list_names():
                combined_dictionary[i] = dictionary1[i]
    for x in dictionary2:
        if x != leaf_1.name(): #don't add leaves that are contained within self
            if x not in leaf_1.leaf_list_names():
                if x not in combined_dictionary:# add if not present
                    combined_dictionary[x] = dictionary2[x]
                else: #overwrite with greater value
                    if combined_dictionary[x] < dictionary2[x]:
                        combined_dictionary[x] = dictionary2[x]
                    else:
                        pass
    node.update_dictionaries(combined_dictionary)
    return
                
def branch_builder(tree_list,max_val):
    """
    Description: creates branches from leaves and/or branches, \
    updates similarity dictionaries for new branches, removes old\
    twigs and inserts new branch into tree_list
    Parameters: tree_list, max_val
    Returns: tree_list
    """
    for i in tree_list:
        if i.name() == None: #branch
            if i.max_similarity() == max_val:
                leaf_of_interest = i
                y = leaf_of_interest.similarity_dictionary_numerical()
                break
        else: #leaf
            if i.data().max_similarity() == max_val:
                leaf_of_interest = i
                y = leaf_of_interest.data().similarity_dictionary_numerical()
                break
    for x in y: # y determined above depending on leaf or branch
        if x == max_val:#get leaf name
            comparison_leaf = y[x]
            if type(comparison_leaf) == list:
                comparison_leaf = comparison_leaf[0]
            break
    for z in tree_list: #get the actual leaf
        if comparison_leaf == z.name(): #leaf
            comparison_leaf = z
            break
        elif z.leaf_list() != None:#branch
            for a in z.leaf_list_names():
                if comparison_leaf == a:
                    comparison_leaf = z
                    break
        else:
            pass
    #note: comparison leaf might actually be a branch at this point    
    name = None
    data = None
    branch = Tree(name,data)
    # objective: correct leaf assignment (L/R)
    if str(leaf_of_interest.name()) > str(comparison_leaf.name()):
        branch.set_right(leaf_of_interest)
        branch.set_left(comparison_leaf)
    else:
        branch.set_left(leaf_of_interest)
        branch.set_right(comparison_leaf)
    # objective: update branch similarity dictionary and leaf list
    leaf_1 = branch.right()
    leaf_2 = branch.left()
    branch.update_leaf_list(leaf_1)
    branch.update_leaf_list(leaf_2)
    update_branch_similarity(branch,leaf_1,leaf_2)
    # objective: pop the old leaves
    accum = 0
    while accum != len(tree_list):
        if tree_list[accum] == leaf_of_interest:
            break
        accum += 1 
    tree_list.pop(accum)
    accum=0
    while accum != len(tree_list):
        if tree_list[accum] == comparison_leaf:
            break
        accum += 1
    tree_list.pop(accum)
    # append branch
    tree_list.append(branch)
    return tree_list
##### END SUBFUNCTIONS #####

    
def tree_builder(tree_list,similarity_hierarchy):                
    """
    Description: makes use of subfunctions in order to build the tree
    Parameters: tree_list, similarity_hierarchy
    Post-Condition: program objectives complete
    """
    max_val = similarity_hierarchy[-1] # true for first iteration
    while len(tree_list) > 2:
        tree_list = branch_builder(tree_list,max_val)
        max_val = max_branch_similarity(tree_list)
    # last case:
    name = None
    data = None
    root = Tree(name,data)
    if str(tree_list[0]) > str(tree_list[1]):
        root.set_left(tree_list[1])
        root.set_right(tree_list[0])
    else:
        root.set_left(tree_list[0])
        root.set_right(tree_list[1])
    print(root)

                 
def main():
    """
    Description: mission control
    """
    tree_list,n = gather_input()
    tree_list,similarity_hierarchy = ngram_processing(tree_list,n)
    tree_builder(tree_list,similarity_hierarchy)




main()

