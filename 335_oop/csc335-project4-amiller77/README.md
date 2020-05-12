# Cryptograms

A cryptogram is a word puzzle based upon a simple substitution cypher. The letters of a quotation
are replaced with different letters such that:
1. All occurrences of the same letter are replaced with the same one
2. No replacement letter is ever used twice
3. All letters are uppercase
4. Punctuation is not altered
5. Spaces remain where they originally were
6. No letter stands for itself (We will ignore this one)

So if we had the phrase:

```
HELLO WORLD
```

We might generate a random encryption key that says all H should be replaced by T, E by R, L by Z, O
by X, etc., yielding a cryptogram of:

```
TRZZX AXPZE
```

The puzzle is for a user, presented with the encrypted version above, to reproduce the original
text by guessing the substitutions that were made. Knowledge of how words work in English usually
gives the first attempts. The most common letter in English is 'E', the most common three-letter
word is 'THE', the sequence of letters 'QQ' never occurs, but 'OO', 'LL', or 'SS' might.

You can play some online for yourself to see: <https://www.cryptograms.org/play.php>

The purpose of this project is to create a new Collection in the java.util Framework, an
ArrayMap, and use this ArrayMap to create cryptograms, and let a human user solve them.

## Example Run

```
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN 
Enter the letter to replace: J
Enter its replacement: T

T            .         T       . -       T        
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN 
Enter the letter to replace: R
Enter its replacement: H

T        H   .  H      TH      . -       T        
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN 
Enter the letter to replace: B
Enter its replacement: E

T        HE  .  H    E THE    E. -       T        
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN 

...

TALK IS CHEAP. SHOW ME THE CODE. - LINUS TORVALDS 
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN

You got it!
```

## Writing the Cryptogram Program

We have provided a file, `quotes.txt` that contains some programming-related quotations.
Select a random quote (one line in the file) to be the puzzle. Generate a random key for
the substitution cypher by creating a List of the letters A-Z and using ``Collections.shuffle()`` to permute them. Create a Map that maps Characters to Characters
and for each letter, associate it with one element from the randomly shuffled List.

Encode the chosen quotation by replacing the letters of the quote with the shuffled version using
your Map.

Have an additional Map for the user's attempt to solve the puzzle. As the user enters their guesses,
add the mapping to their Map and show them their progress by using the Map to replace the 
encrypted characters with their guess ones.

A user is always free to change a letter to something else. 
 
The game is over when the user's guesses turn the encrypted quote back into the original quotation.

## ArrayMap

For the first version of your cryptogram program, the Map you use should be Java's HashMap. However,
you will replace that with a map that you create according to the rules of the java.util Framework.

An ArrayMap will be a map (dictionary) that is implemented using two arrays: one of keys and one of
values. Both arrays will be of type Object.

Your ArrayMap will be a templated (generic type) that will allow maps of any type to any other type.
It will be declared as:

```Java
public class ArrayMap<K, V> extends AbstractMap<K,V> 
```

You are required to provide the implementations of several methods to make your map work.

```Java
@Override
public V put(K key, V value)
```
	
This method adds key and value to your map. If key already exists, the new
value replaces the old one, and the old one is returned.

```Java
@Override
public int size()
```

This method returns the number of mappings that the object contains.

```Java
@Override
public Set<Entry<K, V>> entrySet()
```

Returns a ``Set`` of key, value pairs contained in an ``Entry`` object. The ``AbstractMap`` class provides a concrete ``SimpleEntry`` class that we can use to hold them.

You will need to provide a concrete set, which you will do via a private inner class:

```Java
private class ArrayMapSet extends AbstractSet<Entry<K,V>>
```

You will need to implement these methods:

```Java
@Override
public int size()
```

This method returns the size of the set (and of the Map).

```Java
@Override
public boolean contains(Object o)
```

This method should return true if the Set contains an Entry equal to the the one represented by the
parameter. If ``o`` is not an Entry, this is trivially false. If it is, validate that the key
and the value are actually part of the Map.

```Java		
@Override
public Iterator<Entry<K,V>> iterator() 
```

This returns an iterator that walks over the Set of Entries in the Map. This iterator will
also be implemented as a private inner class:

```Java
private class ArrayMapIterator<T> implements Iterator<T>
```

And must provide implementations of:

```Java
@Override
public boolean hasNext()
```

Returns true if there are more items in the Set of Entries being iterated over.


```Java
@Override
public T next() 
```

Returns an Entry (an ``AbstractMap.SimpleEntry<K,V>`` for us) that represents the next 
mapping in our Map.

We will not support removing items from our Map for this project.

## Using ArrayMap for Cyptograms

Now that you've built and tested your ArrayMap class, you should replace HashMap in your Cryptogram
project with your ArrayMap. If you've done everything using generic Map references, this should require minimal code changes.

Do not assume your ArrayMap will only be tested against the Cryptogram program. Make sure it works 
for any reasonable application.

This means that you will need to do the ArrayList-style growth on your key and value arrays. You
will need to have an initial capacity and when ``put()`` is unable to find any space in the 
arrays, you will need to double their sizes and copy the old elements over.

Make sure you have a test suite that exercises your code to convince yourself that it works
independently of the Cryptogram programs.

## Requirements

- You need a working implementation of the cryptogram puzzle game that uses your ArrayMap class to 
    1. Store the random encryption key
    2. Encrypt the quotation
    3. Store the player's decryption mappings
    4. Decrypt only those letters you have entered a guess for 
	
- The ArrayMap class needs all of the methods and inner classes as explained above
- A reasonable Test Suite that convinces you that your ArrayMap works (we will grade with our own test suite for correctness, but yours should exist and do what we want our test suite to do).
- Documentation using javadoc.
- A reasonable design for the Cryptogram game's objects
 
## Submission
 
 As always, the last pushed commit prior to the due date will be graded.
 
 

