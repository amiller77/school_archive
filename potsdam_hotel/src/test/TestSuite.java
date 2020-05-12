package test;

import model.Model;
import view.TextView;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TEST SUITE
 * series of Junit4 tests for the application
 * essentially tests the model for booking logic, view for input validation
 * @author Alexander Miller
 */
public class TestSuite {
	
	// CLASSES UNDER TEST
	public Model model;

	
	/**
	 * TEST 1
	 * requests outside our planning period are declined, where size = 1
	 * validation handled in view, so we test its hotel size validator
	 */
	@Test
	public void test1() {
		String[] input = {"-4","400","abc"};
		for (int i=0; i<input.length; i++) {
			// user inputs bad input, expect validator to catch it
			boolean exceptionFound = false;
			try {
				TextView.validateDate("-4");
			} catch (NumberFormatException x) {
				exceptionFound = true;
			}
			assertTrue(exceptionFound);
		}
	}
	
	/**
	 * TEST 2
	 * requests are accepted, where size = 3
	 */
	@Test
	public void test2() {
		model = new Model(3);
		int[][] input = {
			{0,5},
			{7,13},
			{3,9},
			{5,7},
			{6,6},
			{0,4}
		};
		// all of these should be assigned a room (not null result)
		for (int i=0; i<input.length; i++) {
			Integer roomAssignment = model.processBooking(input[i]);
			assertNotNull(roomAssignment);
		}
	}
	
	/**
	 * TEST 3
	 * requests are declined, where size = 3
	 */
	@Test
	public void test3() {
		model = new Model(3);
		int[][] input = {
			{1,3},
			{2,5},
			{1,9},
			{0,15}
		};
		// all of these should be assigned a room except last
		for (int i=0; i<input.length; i++) {
			Integer roomAssignment = model.processBooking(input[i]);
			if (i < input.length - 1) {
				assertNotNull(roomAssignment);
			} else {
				assertNull(roomAssignment);
			}
		}
	}
	
	/**
	 * TEST 4
	 * requests are accepted after decline, where size = 3
	 */
	@Test
	public void test4() {
		model = new Model(3);
		int[][] input = {
			{1,3},
			{0,15},
			{1,9},
			{2,5},
			{4,9}
		};
		// all of these should be assigned a room except index 3
		for (int i=0; i<input.length; i++) {
			Integer roomAssignment = model.processBooking(input[i]);
			if (i == 3) {
				assertNull(roomAssignment);
			} else {
				assertNotNull(roomAssignment);
			}
		}
	}
	
	/**
	 * TEST 5
	 * "complex requests", where size = 2
	 */
	@Test
	public void test5() {
		model = new Model(2);
		int[][] input = {
			{1,3},
			{0,4},
			{2,3},
			{5,5},
			{4,10},
			{10,10},
			{6,7},
			{8,10},
			{8,9}
		};
		// all of these should be assigned a room except indices 2,4
		for (int i=0; i<input.length; i++) {
			Integer roomAssignment = model.processBooking(input[i]);
			if (i == 2 || i == 4) {
				assertNull(roomAssignment);
			} else {
				assertNotNull(roomAssignment);
			}
		}
	}
	

}
