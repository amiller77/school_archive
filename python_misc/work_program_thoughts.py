"""
File: work_program.py
Author: Alexander Miller
Description:
* ideas on how to use python to read and process written notes \
for Egyptian Corporations research

Important Note about Encoding:
* program by default decodes using ascii
* [BEST OPTION] if you strip from .docx directly to .txt file without changing encoding:
    * MUST STRIP FRENCH CHARACTERS BEFORE RUNNING, as well as any
    apostrophes and quotations 
    * need to do this while still in word for more powerful search tool
* alternatively, can convert from .docx to .txt and switch to ascii encoding
    * will then need to strip the leftover question marks at a later time (not recommended)
* alternatively, can switch encoding to unicode utf-8
    * then you will need to process it as unicode instead of bytes (not recommended)

* Misc:
    note: don't print the entire file at once.
    
"""

import string

class EgyptianCorporations:
    def __init__(self):
        self._list = []
        self._year = 0
        self._number = 0
    # getters
    def year(self):
        return self._year
    # setters
    def set_year(self,year):
        self._year = year
    def add(self, line, integer_string):
        line = SocieteAnonyme(line, integer_string)
        self._list.append(line)
    # misc
    def __str__(self):
        return str(self._list)

class SocieteAnonyme:
    def __init__(self,name, number):
        self._name = name
        self._number = number
    def __str__(self):
        return self._name

def main():
    readfile()

##### UTILITY FUNCTIONS #####
def whitespace_stripper(item):
    # strips whitespace: intended for string arguments
    item = item.strip()
    if item in string.whitespace:
        return ''
    while item[0] in string.whitespace or item[-1] in string.whitespace:
        item = item.strip()
    return item
##### END UTILITY FUNCTIONS #####

##### READFILE SUBFUNCTIONS #####
def title_line_processing(line,egyptian_corporations):
    integer_string = ''
    word_of_interest = line[0]
    for i in word_of_interest:
        try:
            b = int(i) #placeholder to check if its a number
            integer_string += i
        except:
            break #breaks at first non-integer character
    line = line.strip(integer_string)
    egyptian_corporations.add(line, integer_string)
    return egyptian_corporations

##### END READFILE SUBFUNCTIONS #####

def readfile():
    filename = '1904_2.txt'
    openfile = open(filename)
    egyptian_corporations = EgyptianCorporations()
    line_number = 0
    for line in openfile:
        title_line = False
        line = whitespace_stripper(line)
        if line == '':
            continue
        # objective: handle header and title lines
        if line_number == 0:
            # assumes year is first line in file
            egyptian_corporations.set_year(int(line))
            line_number += 1
            continue
        first_char = line[0][0]
        try:
            first_char = int(first_char)
            title_line = True
        except:
            pass
        if title_line == True:
            title_line_processing(line, egyptian_corporations)
            continue
    print(egyptian_corporations)
    openfile.close()


# STATUS: appears to work to gather list of corporations- not sure


main()

"""
notes to myself:
* ultimately change filename to input instead of hardcoding

"""
"""
log:
5:40-8:40

"""
