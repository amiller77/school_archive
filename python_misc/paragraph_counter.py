"""
Title: Paragraph Counter
Description: keeps track of how many times \
you hit enter before you enter an input
*For use keeping track, tallying, and counting things\
without messing up
"""

def introduction():
    print('Instructions: ')
    print('Press enter without entering any input for as many \
times as you want to tally.')
    print('Then hit enter after giving an input to terminate the loop.')
    return

def counter():
    eingabe = ''
    accum = -1
    while eingabe == '':
        accum+=1
        eingabe = input()
    return accum
        
def print_fn(accum):
    print(accum)
    quit_var = input('quit? y/n ')
    return quit_var

def main():
    quit_var = 'n'
    while quit_var == 'n':
        introduction()
        accum = counter()
        quit_var = print_fn(accum)
    return


main()
