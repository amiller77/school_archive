"""
File: rhymes-oo.py
Author: Alexander Miller 
Purpose:
* use input() to read in pronunciation dictionary and 'word'
* collect all words that rhyme with 'word'
    * given multiple pronunciations, find all words that rhyme \
    with each pronunciation
* program is CASE INSENSITIVE
* print out each word one per line in any order 
* error handling:
    * pronunciation dictionary cannot be read: give error message and quit
        * "ERROR: Could not open file '' + filename
        * USE TRY
    * input word is not in pronunciation dictionary: give \
    error message and quit
        * "ERROR: the word input by the user is not in\
        the pronunciation dictionary'' + word
        * USE ASSERT
        
"""
import sys

class Word:
    """
    Description: instances have names (the word itself) as well as phonemes\
    saved to them
    """
    def __init__(self, line_list, p_stress_location):
        """
        Purpose: initializes instance of Word
        Parameters: line_list, p_stress_location
        Pre-Condition: line has been cleaned and p_stress_location found
        Post-Condition: Word instance will have name (word itself)\
        in upper case, and its corresponding phonemes
        """
        self._name = line_list[0].upper()
        self._harmonizing_phonemes = [line_list[p_stress_location:]]
        self._discordant_phoneme = [line_list[p_stress_location-1]]
        self._status = 'single'
# getters
    def get_name(self):
        """
        Purpose: returns name
        Returns: self._name
        """
        return self._name
    def harmonizing_phonemes(self):
        """
        Purpose: returns the harmonizing phonemes
        Returns: self._harmonizing_phonemes
        """
        return self._harmonizing_phonemes
    def discordant_phoneme(self):
        """
        Purpose: returns the discordant phoneme
        Returns: self._discordant_phoneme
        """
        return self._discordant_phoneme
    def status(self):
        """
        Purpose: reports status (multi vs single pronunciation)
        Returns: self._status
        """
        return self._status
# setters
    def convert_to_multi_pronunciation(self):
        """
        Purpose: converts status from single to multi (pronunciation)
        Returns: none
        """
        self._status = 'multi'
        
    def add_pronunciation(self, line_list, p_stress_location):
        """
        Purpose: adds new phonemes (multiple pronunciations)
        Parameters: line_list, p_stress_location
        """
        self._harmonizing_phonemes += [line_list[p_stress_location:]]
        self._discordant_phoneme += [line_list[p_stress_location-1]]
#misc
    def __str__(self):
        """
        Purpose: gives print instructions (name)
        Returns: self._name
        """
        return self._name
    def __eq__(self, other):
        """
        Purpose: compares two Word instances by comparing their phonemes \
        and returns if they rhyme (perfectly) or not
        Parameters: other
        Returns: True or None
        Post-Condition: established: words either rhyme perfectly or they don't 
        """
        if other.get_name() != self.get_name(): # eliminate rhyming with itself
            self_iteration = 0
            other_iteration = 0
            while other_iteration != len(other.discordant_phoneme()):
                while self_iteration != len(self.discordant_phoneme()):
                    if (self.harmonizing_phonemes()[self_iteration] == \
                        other.harmonizing_phonemes()[other_iteration]) and\
                        (self.discordant_phoneme()[self_iteration] != \
                        other.discordant_phoneme()[other_iteration]):
                        return True
                    self_iteration += 1
                other_iteration += 1


class WordMap:
    """
    Description: instances read in files from input and initialize Word\
    instances and make comparisons between words based on user input
    """
    def __init__(self,name):
        """
        Purpose: initalizes WordMap instance
        Parameters: name
        """
        self._name = name
        self._word_tuple = ()
# getters
    def get_word_tuple(self):
        """
        Purpose: calls collection of Word instances
        Returns: self._word_tuple
        """
        return self._word_tuple
# setters
    def add(self,word_name):
        """
        Purpose: adds a Word instance to the word collection
        Parameters: word_name
        """
        self._word_tuple += (word_name,)
# misc
    def read_file(self):
        """
        Purpose: reads, cleans, and validates phoneme dicitonary,\
        finds primary stress, creates Word instances, adds multiple\
        pronunciation
        """
        filename = input()
        # objective: input file validation
        try:
            openfile = open(filename)
        except:
            print('ERROR: Could not open file' + filename)
            sys.exit(1)
        # objective: read in data from file
        ### ASSUMPTION: file is formatted correctly with good data
        for line in openfile:
            line = line.strip()
            # get rid of empty lines
            if line == '':
                continue 
            line_list = line.split()
            # handling comment lines
            if line_list[0] == '#':
                continue
            line_list = line.split()
            # find and count primary stresses
            p_stress_counter = 0
            index = -1
            for i in line_list:
                index += 1
                if '1' in i:
                    p_stress_counter += 1
                    p_stress_location = index
            # objective: only create words w/ singular primary stress
            if p_stress_counter == 1:
                word_name = line_list[0]
                word_name = word_name.upper()
                # handle single pronunciation
                count = 0
                for x in self.get_word_tuple():
                    if x.get_name() == word_name:
                        count += 1
                if count == 0:
                    word_name = Word(line_list, p_stress_location)
                    self.add(word_name)
                else: # handle multi
                    for x in self.get_word_tuple():
                        if word_name == x.get_name():
                            x.convert_to_multi_pronunciation()
                            x.add_pronunciation(line_list, p_stress_location)
        openfile.close()
    def find_rhyming_words(self,x):
        """
        Purpose: finds rhyming words
        Parameters: x
        Post-Condition: rhyming words have been found and printed;\
        program terminates
        """
        # objective: find rhyming words
        success_list = []
        for i in self.get_word_tuple():
            if x == i:
                success_list += [i]
        for i in success_list:
            print(i)

def main():
    """
    Description: mission control
    """
    word_map = initialize_word_map_and_read_file()
    process_word_input(word_map)



def initialize_word_map_and_read_file():
    """
    Purpose: initializes WordMap instance and reads file (via WordMap instances),\
    initializes and adds Word instances to WordMap instance
    Returns: word_map
    Post-Condition: WordMap instance is fully initialized, file is read and\
    processed, words have been added to WordMap
    """
    name = 'word_map'
    word_map = WordMap(name)
    word_map.read_file()
    return word_map

def process_word_input(word_map):
    """
    Purpose: staging center for taking in user input, and comparing it with\
    WordMap instance's collection of Word objects and printing results
    Parameters: word_map
    Pre-Condition: WordMap and its elements fully initialized
    Post-Condition: program is finished
    """
    input_word = input()
    input_word = input_word.upper()
    # objective: verify that input word is in database
    for x in word_map.get_word_tuple():
        if input_word == x.get_name():
            break
    # assert trips if no object of the same name was found in word tuple
    assert input_word == x.get_name(), \
           'ERROR: the word input by the user is not in the dictionary ' + input_word
    word_map.find_rhyming_words(x)



main()
