import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.io.File;
import java.util.AbstractMap;
import java.util.Set;

import org.junit.jupiter.api.Test;
/**
 * ARRAY MAP TEST
 * test suite for the array map class
 * @author ale
 *
 */
class ArrayMapTest {

	/**
	 * BASIC FUNCTIONALITY TEST
	 * tests basic functionality
	 */
	@Test
	void basicFunctionalityTest() {
		// see if it can instantiate multiple types of maps:
		Map<String,String> map1 = new ArrayMap<String,String>();
		Map<String, Integer> map2 = new ArrayMap<String,Integer>();
		Map<Object,int[]> map3 = new ArrayMap<Object,int[]>();
		// check initial sizes
		assertEquals(map1.size(),0);
		assertEquals(map2.size(),0);
		assertEquals(map3.size(),0);
		// check basic insertion
		assertTrue(map1.put("a","b") instanceof String);
		assertTrue(map2.put("a", 1) instanceof Integer);
		String[] s = {"a,","b","c"};
		int[] i = {1,2,3};
		assertTrue(map3.put(s,i) instanceof int[]);
		// check that sizes now equal one
		assertEquals(map1.size(),1);
		assertEquals(map2.size(),1);
		assertEquals(map3.size(),1);
		// check that a set is returned
		assertTrue(map1.entrySet() instanceof Set<?>);
		assertTrue(map2.entrySet() instanceof Set<?>);
		assertTrue(map3.entrySet() instanceof Set<?>);
		// check that the entry sets are size 1
		assertEquals(map1.entrySet().size(),1);
		assertEquals(map2.entrySet().size(),1);
		assertEquals(map3.entrySet().size(),1);
		// check our getter returns the values correctly
		assertEquals(map1.get("a"),"b");
		assertTrue(map2.get("a")==1);
		assertEquals(map3.get(s),i); // should work because using the same pointers
	}
	
	/**
	 * LARGE MAP TEST
	 * tests some more specific functionality
	 */
	@Test
	void largeMapTest() {
		Map<Character,Integer> map = new ArrayMap<Character,Integer>();
		Character[] alphabet = {'A','B','C','D','E','F','G',
				'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		Integer[] indices = new Integer[26];
		for (int i = 0; i<indices.length; i++) {
			// check that put returns the correct value after inserting
			assertEquals(map.put(alphabet[i],indices[i]),indices[i]);
		}
		// check that entry set and map are the correct size
		assertEquals(map.size(),26);
		assertEquals(map.entrySet().size(),26);
		/*
		 * CHECK THAT ENTRY SET HAS CORRECT ENTRIES
		 * ~ REFERENCE THE ENTRIES/SET APIS 
		 */
		// check that the getter returns the right thing for all inputs
		for (int k = 0; k < map.size();k++) {
			assertEquals(map.get(alphabet[k]),indices[k]);
		}
		
	}

}
