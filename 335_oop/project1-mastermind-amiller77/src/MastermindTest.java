import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class collects all of the test methods for our controller.
 * 
 * @author Alexander Miller
 *
 */
class MastermindTest {

	/**
	 * Test method for {@link MastermindController#isCorrect(java.lang.String)}.
	 */
	@Test
	void testIsCorrect() {
		//Build a model with a known answer, using our special testing constructor
		MastermindModel answer = new MastermindModel("rrrr");
		//Build the controller from the model
		MastermindController controllerUnderTest = new MastermindController(answer);
		
		//For a properly working controller, this should return true
		assertTrue(controllerUnderTest.isCorrect("rrrr"));
		//For a properly working controller, this should be false
		assertFalse(controllerUnderTest.isCorrect("oooo"));
		// combinations of 3:
		assertFalse(controllerUnderTest.isCorrect("orrr"));
		assertFalse(controllerUnderTest.isCorrect("rrro"));
		assertFalse(controllerUnderTest.isCorrect("orrr"));
		// combinations of 2:
		assertFalse(controllerUnderTest.isCorrect("orro"));
		assertFalse(controllerUnderTest.isCorrect("roor"));
		assertFalse(controllerUnderTest.isCorrect("rroo"));
		assertFalse(controllerUnderTest.isCorrect("oorr"));
		// combinations of 1:
		assertFalse(controllerUnderTest.isCorrect("oroo"));
		assertFalse(controllerUnderTest.isCorrect("ooro"));
		assertFalse(controllerUnderTest.isCorrect("rooo"));
		assertFalse(controllerUnderTest.isCorrect("ooor"));
	}

	/**
	 * Test method for {@link MastermindController#getRightColorRightPlace(java.lang.String)}.
	 */
	@Test
	void testGetRightColorRightPlace() {
		//Build a model with a known answer, using our special testing constructor
		MastermindModel answer = new MastermindModel("rrrr");
		//Build the controller from the model
		MastermindController controllerUnderTest = new MastermindController(answer);
		//For a properly working controller, should return:
		//4
		assertEquals(controllerUnderTest.getRightColorRightPlace("rrrr"), 4);
		//0
		assertEquals(controllerUnderTest.getRightColorRightPlace("oooo"), 0);
		//3
		assertEquals(controllerUnderTest.getRightColorRightPlace("orrr"),3);
		assertEquals(controllerUnderTest.getRightColorRightPlace("rrro"),3);
		assertEquals(controllerUnderTest.getRightColorRightPlace("orrr"),3);
		//2
		assertEquals(controllerUnderTest.getRightColorRightPlace("roor"),2);
		assertEquals(controllerUnderTest.getRightColorRightPlace("orro"),2);
		assertEquals(controllerUnderTest.getRightColorRightPlace("rroo"),2);
		assertEquals(controllerUnderTest.getRightColorRightPlace("oorr"),2);
		//1
		assertEquals(controllerUnderTest.getRightColorRightPlace("oroo"),1);
		assertEquals(controllerUnderTest.getRightColorRightPlace("ooro"),1);
		assertEquals(controllerUnderTest.getRightColorRightPlace("rooo"),1);
		assertEquals(controllerUnderTest.getRightColorRightPlace("ooor"),1);
	}
	
	/**
	 * Test method for {@link MastermindController#getRightColorRightPlace(java.lang.String)}.
	 */
	@Test
	void testGetRightColorRightPlace2() {
		//Build a model with a known answer, using our special testing constructor
		MastermindModel answer = new MastermindModel("ogyr");
		//Build the controller from the model
		MastermindController controllerUnderTest = new MastermindController(answer);
		//For a properly working controller, should return:
		//4
		assertEquals(controllerUnderTest.getRightColorRightPlace("ogyr"), 4);
		//0
		assertEquals(controllerUnderTest.getRightColorRightPlace("rygo"), 0);
		//3
		assertEquals(controllerUnderTest.getRightColorRightPlace("ogyp"),3);
		assertEquals(controllerUnderTest.getRightColorRightPlace("ogyo"),3);
		assertEquals(controllerUnderTest.getRightColorRightPlace("rgyr"),3);
		//2
		assertEquals(controllerUnderTest.getRightColorRightPlace("ogry"),2);
		assertEquals(controllerUnderTest.getRightColorRightPlace("oppr"),2);
		assertEquals(controllerUnderTest.getRightColorRightPlace("goyr"),2);
		//1
		assertEquals(controllerUnderTest.getRightColorRightPlace("oyrg"),1);
		assertEquals(controllerUnderTest.getRightColorRightPlace("goyg"),1);
		assertEquals(controllerUnderTest.getRightColorRightPlace("rgoy"),1);
		assertEquals(controllerUnderTest.getRightColorRightPlace("yogr"),1);
	}

	/**
	 * Test method for {@link MastermindController#getRightColorWrongPlace(java.lang.String)}.
	 */
	@Test
	void testGetRightColorWrongPlace() {
		//Build a model with a known answer, using our special testing constructor
		MastermindModel answer = new MastermindModel("rrrr");
		//Build the controller from the model
		MastermindController controllerUnderTest = new MastermindController(answer);
		//For a properly working controller, should return:
		//0
		assertEquals(controllerUnderTest.getRightColorWrongPlace("rrrr"), 0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("oooo"), 0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("orrr"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("rrro"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("orrr"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("roor"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("orro"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("rroo"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("oorr"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("oroo"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("ooro"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("rooo"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("ooor"),0);
	}
	
	/**
	 * Test method for {@link MastermindController#getRightColorWrongPlace(java.lang.String)}.
	 */
	@Test
	void testGetRightColorWrongPlace2() {
		//Build a model with a known answer, using our special testing constructor
		MastermindModel answer = new MastermindModel("oryg");
		//Build the controller from the model
		MastermindController controllerUnderTest = new MastermindController(answer);
		// For a properly working controller, should return:
		//0
		assertEquals(controllerUnderTest.getRightColorWrongPlace("oryg"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("orpp"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("oppg"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("opyg"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("pppp"),0);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("oryp"),0);
		//4
		assertEquals(controllerUnderTest.getRightColorWrongPlace("rogy"),4);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("gyro"),4);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("ygor"),4);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("gyor"),4);
		//3
		assertEquals(controllerUnderTest.getRightColorWrongPlace("pgry"),3);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("gyrp"),3);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("rygp"),3);
		//2
		assertEquals(controllerUnderTest.getRightColorWrongPlace("goyp"),2);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("orgy"),2);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("royg"),2);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("ogyr"),2);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("rooo"),2);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("gypp"),2);
		//1
		assertEquals(controllerUnderTest.getRightColorWrongPlace("ooor"),1);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("ooro"),1);
		assertEquals(controllerUnderTest.getRightColorWrongPlace("opgp"),1);
	}
}
