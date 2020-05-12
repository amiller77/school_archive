"""
File: street.py
Author: Alexander Miller
Description:
* prompts user for one-line specification of a city street\
and prints a simple ASCII rendering
* input descriptions:
    * elements separated by whitespace
    * Building: 'b:width,height,brick' #ie b:5,7,x
        * assume w, h > 0 and brick not whitespace
    * Parks: 'p:width,foliage' #ie. p:19,*
        * has exactly 1 tree, centered in the park
        * width of the park is given by 'width'
        * width must be odd and >= 5
        * tree appears to have two |s as a stem and 9 stars as leaves
        * resulting height is 5
    * Empty lots: 'e:width,trash'
        * any underscores in 'trash' to be interpreted as blank spaces
        * repeat trash graphic as necessary to fill width
        * if 'trash' is longer than width, only include those \
        within appropriate range
        * height is always 1
* frame:
    * border with '+' at corners, '|' along vertical sides, and \
    '-' along horizontal sides
    * height is one more than the tallest element drawn (park \
    or building of height greater than 5)

"""
import copy

##### START: CLASSES AND INPUT PROCESSING FUNCTIONS #####
class Feature:
    """
    Description: Instances represent parks, buildings, and empty_lots
    """
    def __init__(self, width, brick, height, street_pos, typ):
        """
        Purpose: Initialize Feature Instance
        * note: brick also stands in for foliage and trash (same concept)
        Parameters: self,width,brick,height,street_pos,typ
        """
        self._width = width
        self._brick = brick
        self._height = height
        self._street_pos = street_pos
        self._typ = typ
        self._width_range = []
    # getters
    def width(self):
        """
        Purpose: returns width of feature
        Returns: self._width
        """
        return self._width
    def height(self):
        """
        Purpose: returns height of feature
        Returns: self._height
        """
        return self._height
    def brick(self):
        """
        Purpose: returns brick, foliage, or trash
        Returns: self._brick
        """
        return self._brick
    def street_pos(self):
        """
        Purpose: relative position of feature on street
        Returns: self._street_pos
        """
        return self._street_pos
    def width_range(self):
        """
        Purpose: returns feature width range (for use establishing\
        coordinates later)
        Returns: self._width_range
        """
        return self._width_range
    def typ(self):
        """
        Purpose: returns feature type
        Returns: self._typ
        """
        return self._typ
    # setters
    def set_width_range(self, start_width, end_width):
        """
        Purpose: sets width range of Feature instance
        Parameters: start_width, end_width
        """
        # gives range of x values where it can be found
        self._width_range = [start_width,end_width-1]
    # misc
    def __str__(self):
        """
        Purpose: gives print instructions
        * note: primarily for use debugging
        Returns: self._typ
        """
        return self._typ

class Street:
    """
    Description: Instances are mission control for accessing all necessary
    information such as the features on the street, dimensions of street, etc.
    """
    def __init__(self):
        """
        Purpose: initializes Street instance
        """
        self._items = []
        self._height = 6
        self._width = 1
        # starting width is 1 to account for left frame edge
    # getters
    def height(self):
        """
        Purpose: returns height of street
        Returns: self._height
        """
        return self._height
    def width(self):
        """
        Purpose: returns street width
        Returns: self._width
        """
        return self._width
    def street(self):
        """
        Purpose: returns list of features on street
        Returns: self._items
        """
        return self._items
    # setters
    def append(self, argument):
        """
        Purpose: adds in a feature to street
        Parameters: argument (feature)
        """
        self._items.append(argument)
    def set_height(self,height):
        """
        Purpose: sets height of street
        Parameters: height
        """
        self._height = height + 1
    def increment_width(self,width):
        """
        Purpose: increments width as each feature is added
        Parameters: width (of feature)
        """
        self._width += width
    # misc
    def __str__(self):
        """
        Purpose: provides print instructions
        note: primarily for use debugigng
        Returns: str(self._items)
        """
        return str(self._items)

def process_input_list(input_list, street,street_pos):
    """
    Description: fills Street instance out with objects representing buildings,
    parks, and empty lots
    * also acquires frame height and width for the street while we're at it
    * also notes position and type of each Feature instance
    Parameters: input_list, street, street_pos
    Returns: street, street_pos
    Post-Condition: street instance created and updated with all relevant information
    about the image
    """
    # base case:
    if len(input_list) == 0:
        return street, street_pos
    
    # general case:
    street_pos += 1
    # objective: separate types into tracks to handle the common things\
    # together and the unique things separately
    if input_list[0][0] == 'b':#building
        typ = 'b'
    elif input_list[0][0] == 'e':#empty_lot 
        typ = 'e'
    else: # input_list[0][0] == 'p': # park
        typ = 'p'
    phrase = input_list[0][2:] #slices off intro stuff
    phrase = phrase.split(',')
    
    # objective: perform type-specific operations
    if typ == 'b': #building
        height = int(phrase[1])
        if height > street.height():
            #method automatically builds 1-space cushion
            street.set_height(height) 
        brick = phrase[2]
    elif typ == 'e': #empty_lot
        # note: trash is represented by 'brick', the general idea
        brick = phrase[1]
        # objective: replace trash _ with whitespace
        brick = brick.split('_')
        brick = ' '.join(brick)
        height = 1 #constant
    else: #park
        # note: foliage is represented by 'brick', the general idea
        brick = phrase[1]
        height = 5 #constant

    #objective: perform common operations
    width = int(phrase[0])
    street.increment_width(width)
    end_width = street.width()
    start_width = street.width()-width
    f = Feature(width,brick,height,street_pos,typ)
    f.set_width_range(start_width,end_width)
    street.append(f)
        
    street, street_pos = process_input_list(input_list[1:], street,street_pos)
    return street, street_pos
##### END: CLASSES AND INPUT PROCESSING FUNCTIONS #####



##### START: GENERIC GRID BUILDING AND EDITING FUNCTIONS #####
def grid_skeleton(old_grid,count, filler):
    """
    Description: create a 2D list representing a grid
    * note: implemented by grid_constructor
    Parameters: old_grid,count,filler
    Returns: old_grid
    """
    # general case:
    old_grid += [filler]
    count -= 1
    # base case: # want to quit after performing duties
    if count == 0:
        return old_grid
    grid_skeleton(old_grid,count,filler)
    return old_grid

def grid_expander(grid,accum, filler_list):
    """
    Purpose: expands grid_skeleton
    * note: implemented by grid_constructor
    Parameters: grid,accum,filler_list
    Returns: grid
    """
    # base case
    if accum == 0:
        return grid
    # general case
    accum -= 1
    # have to deep copy or it shallow copies
    grid[accum] = copy.deepcopy(filler_list)
    grid_expander(grid,accum,filler_list)
    return grid

def grid_constructor(street):
    """
    Purpose: constructs grid via grid_skeleton and grid_expander functions
    Parameters: street
    Returns: new_grid
    Post-Condition: grid has been constructed and is ready for updates
    """
    row_length = street.width()
    number_of_rows = street.height()
    filler = [] # the inside list
    old_grid = []
    grid = grid_skeleton(old_grid, number_of_rows, filler)
    filler = 0
    filler_list = []
    filler_list = grid_skeleton(filler_list,row_length,filler)
    new_grid = grid_expander(grid,number_of_rows,filler_list)
    return new_grid

def install_vertical_sides(grid,accum):
    """
    Description: adds in side frames to existing grid
    Parameters: grid,accum
    Returns: grid
    """
    # base case:
    if accum == -1:
        return grid
    grid[accum][0] = '|'
    grid[accum][-1] = '|'
    accum -= 1
    grid = install_vertical_sides(grid,accum)
    return grid

def horizontal_border_builder(width):
    """
    Description: also includes the edge tacks '+' if used as subfunction
    for horizontal_border_attacher
    Parameters: width
    Returns: border of '-' characters 
    """
    if width == 0:
        return []
    width -= 1
    return ['-'] + horizontal_border_builder(width)

def horizontal_border_attacher(street,grid):
    """
    Purpose: attaches horizontal borders onto frame
    Parameters: street, grid
    Returns: grid
    
    """
    # construct our frame
    width = street.width()-2
    horizontal_border = ['+'] + horizontal_border_builder(width) + ['+']
    grid.insert(0,horizontal_border)
    grid.append(horizontal_border)
    # note: our vertical border will be contained within each grid element
    return grid

def whitewash_filler(row,street,accum2):
    """
    Purpose: removes filler spaces from grid 
    Parameters: row, street, accum2
    Returns: row, accum2
    """
    # base case:
    if accum2 == street.width():
        return row,accum2
    if row[accum2] == 0:
        row[accum2] = ' '
    accum2 += 1
    row,accum2 = whitewash_filler(row,street,accum2)
    return row,accum2

def traverse_grid_and_print(grid,street,accum):
    """
    Purpose: traverses grid, clears filler with whitewash_filler
    and prints grid
    Parameters: grid, street, accum
    Returns: grid
    """
    # base case:
    if accum == street.height()+2:#accounts for tacked on end/start lines
        return grid
    accum2=0
    grid[accum],accum2 = whitewash_filler(grid[accum],street,accum2)
    line = ''.join(grid[accum])
    print(line)
    accum += 1
    grid = traverse_grid_and_print(grid,street,accum)
    return grid
    
    
##### END: GRID EDITOR FUNCTIONS ######



##### START: STREET FEATURE INTEGRATING FUNCTIONS #####
def x_direction_implementation(x_range,y_range,grid,value):
    """
    Description: iterates through x coordinate range and inserts bricks
    * stand-alone function or can be use as\
    subfunction for y_direction_implementation
    Parameters: x_range, y_range, grid, value
    Returns: grid
    """
    x = x_range[0]
    y = y_range[0]
    if len(value) == 1: #may have multi-character bricks
        grid[y][x] = value
    else:
        grid[y][x] = value[0]
    # base case: want the above to implement before quitting
    if x_range[1]-x_range[0] == 0:
        return grid
    # have to deep copy to avoid changes in recursion
    new_x_range = copy.deepcopy(x_range)
    new_y_range = copy.deepcopy(y_range)
    new_value = copy.deepcopy(value)
    new_x_range[0] += 1
    if len(new_value) > 1: #slicing to next character in list
        new_value = new_value[1:]
    x_direction_implementation(new_x_range,new_y_range,grid,new_value)
    return grid

def y_direction_implementation(x_range,y_range,grid,value,typ):
    """
    Description: iterates through y range values and calls\
    x_direction_implementation to fill out with bricks
    *note: this performs high to low y iteration, in other words,
    from the bottom of the picture to the top
    * as designed only works if y =[higher_val,lower_val]
    * requires x_direction_implementation
    Parameters: x_range, y_range, grid, value, typ
    Returns: grid
    """
    x_direction_implementation(x_range,y_range,grid,value)
    # base case: want the above to implement before quitting
    if y_range[1]-y_range[0] == 0:
        return grid
    # have to deep copy to avoid changes in recursion
    new_y_range = copy.deepcopy(y_range)
    new_y_range[0]-= 1
    new_x_range = copy.deepcopy(x_range)
    if typ == 'p': #have to cut down xrange with each implementation
        new_x_range[0] += 1
        new_x_range[1] -= 1
    y_direction_implementation(new_x_range,new_y_range,grid,value,typ)
    return grid

def configure_feature_location(street,grid,accum):
    """
    Description: establishes coordinate break-down of parks, typ == 'p'
    * performs main objectives via x_direction_implementation\
    and y_direction_implementation 
    Parameters: street, grid, accum
    Returns: grid
    """
    # base case (last case was accum == 0)
    if accum == -1:
        return grid
    feature = street.street()[accum]
    last_row = street.height()-1
    typ = feature.typ()
    
    # type: parks
    if typ == 'p':
        # stump location:
        x = feature.width()//2 + feature.width_range()[0]
        y = last_row
        value = '|'
        # traditional y coordinates are given by row number
        # which is the first index
        grid[y][x] = value
        # trunk location:
        y = last_row-1
        grid[y][x] = value
        # leaves:
        x_range = [x-2,x+2]
        y_range = [last_row-2,last_row-4]
        
    elif typ == 'e': #lots
        x_range = feature.width_range()
        y_range = [last_row,last_row]
        
    else: # buildings
        x_range = feature.width_range()
        y_range = [last_row,street.height()-feature.height()]
        
    # common operations:
    value = feature.brick()
    new_value = copy.deepcopy(value)
    if len(value) > 1: #in event of multi-character values
        # need (len of value)*k = width so we can slice it away
        x_width = x_range[1]-x_range[0]
        new_value = new_value*x_width
    grid = y_direction_implementation(x_range,y_range,grid,new_value,typ)
    accum -= 1
    configure_feature_location(street,grid,accum)
    return grid
##### END: STREET FEATURE INTEGRATING FUNCTIONS #####

    
def main():
    """
    Description: mission control
    """
    input_string = input("Street: ")
    input_list = input_string.split(' ')
    street = Street()
    street_pos = 0
    # input processing:
    street, street_pos = process_input_list(input_list, street, street_pos)
    street.increment_width(1) # adds right frame edge cushion 
    # grid constructor:
    grid = grid_constructor(street)
    # configure feature locations:
    accum = len(street.street()) - 1
    grid = configure_feature_location(street,grid,accum)
    # install vertical sides:
    accum = street.height()-1
    grid = install_vertical_sides(grid,accum)
    # build and attach horizontal borders:
    grid = horizontal_border_attacher(street,grid)
    # whitewash filler slots and print grid:
    accum = 0
    grid = traverse_grid_and_print(grid,street,accum)



main()


