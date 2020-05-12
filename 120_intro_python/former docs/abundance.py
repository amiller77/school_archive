"""
File: abundance.py
Author: Alexander Miller
Purpose:
* examine the distribution of species across different
national parks and prints out the most widely distributed species
* read in the name of a file (sinfo) using input
    * for each line in sinfo use "scientific name" and park name to count
    the total number of national parks where species occurs
    * other data entries may be discarded
    * any line beginning with # should be discarded
    *CASE INSENSTIVE
    * close file
    * only reads the file once
* print out species that are found in the largest number of parks
* use a dictionary
* using the following output statement:
    print("{} -- {:d} parks".format(species_name, number_of_parks))
* asserts placed at beginning of fn to check any pre-conditions
* asserts at beginning of loops that compute value or transforms data which:
    * reflects compution of loop and is not directly tied to iteration condition (?)
        * asserts should check invariants within loop
    * think type(x) == b
    * where asserts are impossible/difficult can use comment instead:
        ### INVARIANT/ASSUMPTION: invariant/assumption
"""

def main():
    file_list = init_fn()
    file_dictionary = data_organization(file_list)
    assert type(file_dictionary) == dict
    success_list, max_val = data_processing(file_dictionary)
    assert type(success_list) == list
    print_fn(success_list, max_val)

def init_fn():
    """
    Description: gathers information from file
    Parameters: none
    Returns: file_list
    Pre-condition: file is CSV and exists
    Post-Condition: file_list is list
    """
    filename = input()
    ### ASSUMPTION: input file is a CSV; file exists
    openfile = open(filename,'r')
    file_list = []
    for line in openfile:
        # objective: remove any white space from ends and make lower case
        line = line.strip()
        line = line.lower()
        # objective: remove empty lines
        if len(line) > 0:
            file_list.append(line.split(','))
    openfile.close()
    return file_list

def data_organization(file_list):
    """
    Description: organizes information into dictionary
    Parameters: file_list
    Returns: file_dictionary
    Pre-condition: file_list is a list
    Post-Condition: file_dictionary is a dictionary
    """
    assert type(file_list) == list
    file_dictionary = {}
    accum = 0
    while accum != len(file_list):
        ### ASSUMPTION: lines are organized with the information in the proper location
        # objective: only adjoin non-comment lines
        if file_list[accum][0] != '#' and '#' not in file_list[accum][0]:
            # handling first occurrences
            if file_list[accum][2] not in file_dictionary:
                # logging the key inside of itself for expedient recovery
                file_dictionary[file_list[accum][2]] = [file_list[accum][2]] \
                                                       + [file_list[accum][0]]
            # handling multiple occurrences
            else:
                file_dictionary[file_list[accum][2]] = \
                file_dictionary[file_list[accum][2]] + [file_list[accum][0]] 
        accum = accum + 1
    return file_dictionary


def data_processing(file_dictionary):
    """
    Description: processes dictionary to find most widely\
    distributed species
    Parameters: file_dictionary
    Returns: success_list, max_val
    Pre-condition: file_dictionary is a dictionary
    Post-Condition: success_list is a list; max_val is an integer
    """
    assert type(file_dictionary) == dict
    success_list = []
    max_val = 0
    for i in file_dictionary:
        ### ASSUMPTION: none (this will still operate even if dict is empty)
        if len(file_dictionary[i]) > max_val:
            max_val = len(file_dictionary[i])
    for x in file_dictionary:
        ### ASSUMPTION: none (this will still operate even if dict is empty)
        if len(file_dictionary[x]) == max_val:
            success_list = success_list + [file_dictionary[x][0]]
    return success_list, max_val

def print_fn(success_list, max_val):
    """
    Description: prints out successful entries in appropriate format
    Parameters: success_list, max_val
    Returns: none
    Pre-condition: success_list is a list; max_val is an integer
    Post-Condition: none
    """
    assert type(success_list) == list
    assert type(max_val) == int
    accum = 0
    while accum < len(success_list):
        print("{} -- {:d} parks".format(success_list[accum], max_val - 1))
        accum = accum + 1
    
main()



