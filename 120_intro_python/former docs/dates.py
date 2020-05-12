"""
File: dates.py
Author: Alexander Miller
Purpose:
* use input to read in filename, and perform operation depending on key
* keys:
    * I: add event string to collection of events for specified date
        * duplicates OK; create date object if none exists
    * R: print out event strings for that date, one event per line (see output format)
        * if no Date object exists, do not print anything
* canonical representation: will need to standardize dates to canoncial representations

"""
import sys
VALID_OPERATIONS = ['I','R']
MONTHS_OF_THE_YEAR = {'Jan':1,'Feb':2,'Mar':3,'Apr':4,'May':5,'Jun':6, \
                        'Jul':7,'Aug':8,'Sep':9,'Oct':10,'Nov':11,'Dec':12}

class DateSet:
    """
    Purpose: creates a DateSet, a container for Date objects
    """
    def __init__(self,name):
        """
        Purpose: initializes DateSet instance
        Parameters: name
        """
        self._name = name
        self._tuple = ()
# setters
    def add(self,date_object):
        """
        Purpose: adds Date object to container
        Parameters: date_object
        """
        self._tuple += (date_object,)
# misc
    def perform_program_objectives(self, operation_cue, event, date,date_set):
        """
        Purpose: performs major objectives- operations based upon I or R cue
        Parameters: operation_cue, event, date, date_set
        Pre-Condition: event and date have been prepped accordingly; date_set initialized
        Post-Condition: program objectives for that line complete
        """
        counter = 0
        for i in self._tuple:
            if date == i: # case where matching date was found
                if operation_cue == 'I':
                    i.update(event)
                    counter += 1
                if operation_cue == 'R':
                    if len(i.get_events()) > 0: #control for empty event tuples
                        i.my_sort()
                        for x in i.get_events():
                            print('{}: {}'.format(i.get_date(),x))
        if counter == 0: # case where no other matching dates were found
            if operation_cue == 'I':
                date_object = Date(date, event)
                self.add(date_object)

class Date:
    """
    Purpose: Date objects contain date and event attributes
    """
    def __init__(self, date, event):
        """
        Purpose: initializes Date instance
        Parameters: date, event
        """
        self._name = date
        self._event = [event,]
# getters
    def get_events(self):
        """
        Purpose: returns events
        Returns: self._event
        """
        return self._event
    def get_date(self):
        """
        Purpose: returns date
        Returns: self._name
        """
        return self._name
# setters
    def update(self, event):
        """
        Purpose: adds event to roster 
        Parameters: event
        """
        self._event += [event,]
    def my_sort(self):
        """
        Purpose: sorts the events
        """
        self._event.sort()
# misc
    def __eq__(self, i):
        """
        Purpose: provides manner for comparison: are the dates the same?
        Returns: self._name == i
        """
        return self._name == i
    def __str__(self):
        """
        Purpose: provides print instructions
        Returns: self._event
        """
        return self._event
        

def main():
    """
    Purpose: Mission Control
    Post-Condition: Program finished.
    """
    openfile = validate_input()
    name = 'date_set'
    date_set = DateSet(name)
    # objective: process lines
    for line in openfile:
        line = line.strip()
        line_list = line.split()
        operation_cue, event, date = process_input(line_list, line)
        if operation_cue != None:
            date_set.perform_program_objectives(operation_cue,event,date,date_set)
    openfile.close()


    
def validate_input():
    """
    Purpose: validates file name input
    Returns: openfile
    """
    # objective: validate input
    filename = input()
    try:
        openfile = open(filename)
    except:
        print('ERROR: Could not open file ' + filename)
        sys.exit(1)
    return openfile


###### SUBFUNCTIONS FOR PROCESS_INPUT #####
def split_line(line_list):
    """
    Purpose: finds colon location as landmark to split line
    *may remove colon from date/event and insert into own position if necessary
    Parameters: line_list
    Returns: line_list, index
    """
    # objective: find colon as landmark in splitting line
    index = 0
    while index != len(line_list):
        if line_list[index] == ':':
            return index, line_list
        elif ':' in line_list[index]:
            # case where it's attached to end
            if line_list[index][0] != ':':
                line_list[index] = line_list[index].strip(':')
                line_list.insert(index+1,':')
                index += 1 # updates new colon location to next spot
            # case where it's attached to event
            else:
                line_list[index] = line_list[index].strip(':')
                line_list.insert(index,':')
            return index, line_list
        index += 1
    index = 0 # this means no colon was found
    return index, line_list

def canonicalize_date(date, line):
    """
    Purpose: get dates into approved format
    Parameters: date, line
    Pre-Conditions: date must be in the formats laid out in the specs
    Returns: date
    """
    ### ASSUMPTION: abbreviations in the right format (Jul, Aug, etc.)
    # 3 cases to consider:
    if '-' in date:
        date = date.split('-')
        month = date[1]
        day = date[2]
        year = date[0]
    elif '/' in date:
        date = date.split('/')
        month = date[0]
        day = date[1]
        year = date[2]
    else:
        date = date.split()
        day = date[1]
        year = date[2]
        # fetches corresponding month number out of dictionary above
        try:
            month = MONTHS_OF_THE_YEAR[date[0]]
        except:
            print('ERROR: Illegal date on line '+ line)
            sys.exit(1)
    # objective: validate dates
    try:
        assert int(day) <= 31
        assert int(month) <= 12
    except:
        print('ERROR: Illegal date on line '+ line)
        sys.exit(1)
    date = '{:d}-{:d}-{:d}'.format(int(year),int(month),int(day))
    return date
##### END SUBFUNCTIONS FOR PROCESS_INPUT #####


def process_input(line_list, line):
    """
    Purpose: processes input to get necessary info from each line
    Parameters: line_list, line
    Returns: operation_cue, event, date
    Pre-Conditions: file formatted correctly and opened
    """
    if len(line_list) > 1: # control for whitespace lines:
        operation_cue = line_list[0]
        # objective: validate for operation types
        if operation_cue not in VALID_OPERATIONS:
            print('ERROR: Illegal operation on line ' + line)
            sys.exit(1)
        # objective: determine what ranges are dates and events, and clean
        ### ASSUMPTION: file formatted correctly
        index, line_list = split_line(line_list)
        date = None
        event = None # standardized values
        # objective: handle lines with colon
        if index != 0:
            date = line_list[1:index] #cutting operation cue out of range
            if len(date)>1:
                date = ' '.join(date)
            else:
                date = date[0]
            event = line_list[index+1:] # cutting colon out of range
            event = ' '.join(event)
        # objective: handle lines w/o colon
        else:
            date = line_list[1:]
            if len(date)>1:
                date = ' '.join(date)
            else:
                date = date[0]
        # objective: canonicalize dates
        if date != None:
            date = canonicalize_date(date, line)
        return operation_cue, event, date
    else:
        operation_cue = None
        event = None
        date = None
        return operation_cue, event, date




main()

