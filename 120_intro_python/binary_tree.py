# working with binary tree construction using oop methods

class binary_tree:
    def __init__(self,value):
        self._value = value
        self._lchild = None
        self._rchild = None
    def insert(self, tree):
        # note: tree must be binary_tree instance
        # base case:
        if (self._lchild == None) and (self._rchild == None):
            if tree._value > self._value:
                self._rchild = tree
                return
            elif tree._value < self._value:
                self._lchild = tree
                return
            else:
                # what do you do if they are equal?
                # what if they are non-comparable entities?
                print('unexpected outcome- potential equality?')
                return
        # recursive case:
        if tree._value > self._value:
            return insert(self._rchild,tree): #will this cause an argument error?
        elif tree._value < self._value:
            return insert(self._lchild,tree): # same question ^
        else:
            print('unexpected outcome- potential equality?')
            return


my_list = [1,3,5,7,9,2,4,6,8,10]
def recursive_merge_sort(my_list):
    pass




recursive_merge_sort(my_list)
