"""
File: word-grid
Author: Alexander Miller
Purpose: this program reads 2 integer values from input, grid size and random number seed,
creates an nxn grid of randomly generated lower-case letters, and prints out the grid of
letters one row at a time
"""
import random

def main():    
    n = init()
    g = make_grid(n)
    print_grid(g,n)

def init():
    """
    Description: gathers input; sets random seed; returns n
    Parameters: none
    Returns: returns n, the grid size
    Pre-condition: random must have been imported
    Post-Condition: n will be an integer, seed will be established
    """
    n = int(input())
    s = int(input())
    random.seed(s)
    return n


def make_grid(n):
    """
    Description: makes a grid out of n
    Parameters: n, the grid size
    Returns: g, the grid
    Pre-condition: n must be integer; random must be imported; randomseed created
    Post-Condition: grid will be list of lists, nxn in dimension, consisting of randomly-generated letters
    """
    g = []
    alpha = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']
    accum2 = 0
    for x in range(n): #need number of entries in g to match n
        g = g + ['%']
    while accum2 != n: #number of sublists
        accum = 0
        while accum != n: #number of entries in each sublist
            a_number = random.randint(0,25)
            g[accum2] = g[accum2] +alpha[a_number]
            accum = accum + 1
        g[accum2] = g[accum2].strip('%') #now i have to strip that '%' from earlier
        accum2 = accum2 + 1
    return g

def print_grid(g,n):
    """
    Description: prints the grid formed in the last function line by line
    Parameters: g, grid; n, gridsize
    Returns: none
    Pre-condition: grid is indeed a grid; n is an integer
    Post-Condition: will have printed out the grid line by line
    """
    accum = 0
    while accum != n:
        a = ','.join(g[accum])
        print(a +'\n')
        accum = accum + 1
    

main()

