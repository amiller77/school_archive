"""
# working with basic binary merge sort
my_list_a = [2,4,6,8,10]
my_list_b = [1,3,5,7,9]
def merge_sort(my_list_a,my_list_b):
    # sorts highest to lowest
    # base case
    if (len(my_list_a) == 0) or (len(my_list_b)==0):
        return my_list_a+my_list_b
    if my_list_a[0] >= my_list_b[0]:
        return [my_list_a[0]] + merge_sort(my_list_a[1:],my_list_b)
    elif my_list_a[0] <= my_list_b[0]:
        return [my_list_b[0]
"""



"""
below:
this works combining slide solutions
idk man..
"""

def merge(L1,L2,merged):
    if L1 == [] or L2 == []:
        return merged + L1 + L2
    else:
        if L1[0] > L2[0]:
            new_merged = merged + [L1[0]]
            new_L1 = L1[1:]
            new_L2 = L2
        else:
            new_merged = merged + [L2[0]]
            new_L1 = L1
            new_L2 = L2[1:]
        return merge(new_L1,new_L2,new_merged)
def msort(L):
    if len(L) <= 1:
        return L
    else:
        split_pt = len(L)//2
        L1 = L[:split_pt]
        L2 = L[split_pt:]
        sortedL1 = msort(L1)
        sortedL2 = msort(L2)
        return merge(sortedL1,sortedL2,[])
print(msort([33,22,11,25,20,15,10,5]))
