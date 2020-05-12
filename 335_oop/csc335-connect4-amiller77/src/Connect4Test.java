import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * CONNECT 4 TEST
 * testing suite for Connect4 game; tests all possible types of outcomes 
 * @author ale
 *
 */
class Connect4Test {

	/**
	 * MODEL TEST
	 * tests the model
	 */
	@Test
	void modelTest() {
		Connect4Model model = new Connect4Model();
		assertEquals(model.getBoard().length,6);
		assertEquals(model.getBoard()[0].length,7);
	}
	
	/**
	 * BASE FUNCTIONALITY TEST
	 * tests base functionality
	 */
	@Test
	void baseFunctionalityTest() {
		Connect4Controller controller = new Connect4Controller();
		// invalid moves:
		assertEquals(controller.humanTurn(-1),false);
		assertEquals(controller.humanTurn(7),false);
		// valid move:
		assertEquals(controller.humanTurn(0),true);
		// computer move:
		controller.computerTurn(null);
		// get tile
		assertEquals(controller.getTile(5, 0),2);
		// invalid check for winner
		assertEquals(controller.checkForWinner(),0);
		// invalid check for tie
		assertEquals(controller.checkForTie(),false);
	}
	
	/**
	 * FULL STACK TEST
	 * tests adding to full stack
	 */
	@Test
	void fullStackTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(0);
		controller.computerTurn(0);
		assertFalse(controller.humanTurn(0));
	}
	
	
	/**
	 * HUMAN ROW TEST
	 * tests human row victory
	 */
	@Test
	void humanRowTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(1);
		controller.computerTurn(1);
		controller.humanTurn(2);
		controller.computerTurn(2);
		controller.humanTurn(3);
		assertTrue(controller.checkForWinner()==2);	
	}
	
	/**
	 * COMPUTER ROW TEST
	 * tests computer row victory
	 */
	@Test
	void computerRowTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(0);
		controller.computerTurn(1);
		controller.humanTurn(0);
		controller.computerTurn(2);
		controller.humanTurn(1);
		controller.computerTurn(3);
		controller.humanTurn(2);
		controller.computerTurn(4);
		assertEquals(controller.checkForWinner(),1);
	}
	
	/**
	 * HUMAN COLUMN TEST
	 * tests human column victory
	 */
	@Test void humanColumnTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(0);
		controller.computerTurn(1);
		controller.humanTurn(0);
		controller.computerTurn(2);
		controller.humanTurn(0);
		controller.computerTurn(3);
		controller.humanTurn(0);
		assertEquals(controller.checkForWinner(),2);
	}

	/**
	 * COMPUTER COLUMN TEST
	 * tests computer column victory
	 */
	@Test
	void computerColumnTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(1);
		controller.computerTurn(0);
		controller.humanTurn(2);
		controller.computerTurn(0);
		controller.humanTurn(1);
		controller.computerTurn(0);
		controller.humanTurn(2);
		controller.computerTurn(0);
		assertEquals(controller.checkForWinner(),1);
	}
	
	/**
	 * DIAGONAL EDGE CASES TESTS
	 * tests diagonal edge cases
	 */
	@Test
	void diagonalEdgeCasesTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(6);
		controller.humanTurn(5);
		controller.humanTurn(5);
		controller.humanTurn(4);
		controller.humanTurn(4);
		controller.humanTurn(4);
		assertEquals(controller.checkForWinner(),0);
		controller.computerTurn(0);
		controller.computerTurn(0);
		controller.computerTurn(0);
		controller.computerTurn(1);
		controller.computerTurn(1);
		controller.computerTurn(2);
		assertEquals(controller.checkForWinner(),0);
		controller.humanTurn(6);
		controller.humanTurn(6);
		assertEquals(controller.checkForWinner(),0);
	}
	
	/**
	 * UPWARDS DIAGONAL TEST
	 * tests upwards diagonal edge case
	 */
	@Test
	void upwardsDiagonalTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(6);
		controller.humanTurn(5);
		controller.humanTurn(4);
		controller.humanTurn(5);
		controller.humanTurn(6);
		assertEquals(controller.checkForWinner(),0);
		
	}
	
	/**
	 * HUMAN UPWARDS DIAGONAL TEST
	 * tests upwards human diagonal victory
	 */
	@Test
	void humanUpwardsDiagonalTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(0);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(2);
		controller.humanTurn(3);
		controller.computerTurn(2);
		controller.humanTurn(2);
		controller.computerTurn(3);
		controller.humanTurn(3);
		controller.computerTurn(4);
		controller.humanTurn(3);
		assertEquals(controller.checkForWinner(),2);
	}
	
	/**
	 * HUMAN UPWARDS DIAGONAL TEST 2
	 * tests different type of human upwards diagonal victory
	 */
	@Test
	void humanUpwardsDiagonalTest2() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(3);
		controller.computerTurn(4);
		controller.humanTurn(4);
		controller.computerTurn(5);
		controller.humanTurn(5);
		controller.computerTurn(6);
		controller.humanTurn(5);
		controller.computerTurn(6);
		controller.humanTurn(2);
		controller.computerTurn(6);
		controller.humanTurn(6);
		assertEquals(controller.checkForWinner(),2);
	}
	
	/**
	 * COMPUTER UPWARDS DIAGONAL TEST
	 * tests computer upward diagonal victory
	 */
	@Test
	void computerUpwardsDiagonalTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(1);
		controller.computerTurn(0);
		controller.humanTurn(2);
		controller.computerTurn(1);
		controller.humanTurn(2);
		controller.computerTurn(2);
		controller.humanTurn(3);
		controller.computerTurn(3);
		controller.humanTurn(3);
		controller.computerTurn(3);
		assertEquals(controller.checkForWinner(),1);
	}
	
	/**
	 * COMPUTER UPWARDS DIAGONAL TEST 2
	 * tests another type of computer upwards diagonal victory
	 */
	@Test
	void computerUpwardsDiagonalTest2() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(4);
		controller.computerTurn(5);
		controller.humanTurn(4);
		controller.computerTurn(4);
		controller.humanTurn(5);
		controller.computerTurn(6);
		controller.humanTurn(5);
		controller.computerTurn(5);
		controller.humanTurn(6);
		controller.computerTurn(6);
		controller.humanTurn(6);
		controller.computerTurn(6);
		controller.humanTurn(2);
		controller.computerTurn(3);
		controller.humanTurn(2);
		controller.computerTurn(3);
		assertEquals(controller.checkForWinner(),1);
	}
	
	/**
	 * HUMAN DOWNWARDS DIAGONAL TEST
	 * tests human downwards diagonal victory
	 */
	@Test
	void humanDownwardsDiagonalTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(1);
		controller.computerTurn(0);
		controller.humanTurn(0);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(0);
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(3);
		controller.computerTurn(2);
		controller.humanTurn(2);
		assertEquals(controller.checkForWinner(),2);
	}
	
	/**
	 * HUMAN DOWNWARDS DIAGONAL TEST 2
	 * tests different type of human downwards diagonal victory
	 */
	@Test
	void humanDownwardsDiagonalTest2() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(6);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(2);
		controller.humanTurn(2);
		controller.computerTurn(2);
		controller.humanTurn(4);
		controller.computerTurn(2);
		controller.humanTurn(2);
		controller.computerTurn(3);
		controller.humanTurn(4);
		controller.computerTurn(3);
		controller.humanTurn(3);
		controller.computerTurn(5);
		controller.humanTurn(3);
		controller.computerTurn(6);
		controller.humanTurn(4);
		assertEquals(controller.checkForWinner(),2);
	}
	
	/**
	 * COMPUTER DOWNWARDS DIAGONAL TEST
	 * tests computer downwards diagonal victory
	 */
	@Test
	void computerDownwardsDiagonalTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(0);
		controller.computerTurn(3);
		controller.humanTurn(1);
		controller.computerTurn(1);
		controller.humanTurn(2);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(2);
		assertEquals(controller.checkForWinner(),1);
	}
	
	/**
	 * COMPUTER DOWNWARDS DIAGONAL TEST 2
	 * tests different type of computer downwards diagonal victory
	 */
	@Test
	void computerDownwardsDiagonalTest2() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(1);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(1);
		controller.humanTurn(2);
		controller.computerTurn(2);
		controller.humanTurn(2);
		controller.computerTurn(4);
		controller.humanTurn(2);
		controller.computerTurn(2);
		controller.humanTurn(3);
		controller.computerTurn(4);
		controller.humanTurn(3);
		controller.computerTurn(3);
		controller.humanTurn(5);
		controller.computerTurn(3);
		controller.humanTurn(6);
		controller.computerTurn(4);
		assertEquals(controller.checkForWinner(),1);
	}
	
	/**
	 * TIE GAME TEST
	 * tests successful tie game
	 */
	@Test
	void tieGameTest() {
		Connect4Controller controller = new Connect4Controller();
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(0);
		controller.computerTurn(0);
		controller.humanTurn(2);
		controller.computerTurn(1);
		controller.humanTurn(1);
		controller.computerTurn(2);
		controller.humanTurn(1);
		controller.computerTurn(2);
		controller.humanTurn(1);
		controller.computerTurn(1);
		controller.humanTurn(2);
		controller.computerTurn(0);
		controller.humanTurn(1);
		controller.computerTurn(2);
		controller.humanTurn(2);
		controller.computerTurn(6);
		controller.humanTurn(3);
		controller.computerTurn(6);
		controller.humanTurn(5);
		controller.computerTurn(3);
		controller.humanTurn(3);
		controller.computerTurn(3);
		controller.humanTurn(3);
		controller.computerTurn(3);
		controller.humanTurn(6);
		controller.computerTurn(4);
		controller.humanTurn(6);
		controller.computerTurn(4);
		controller.humanTurn(5);
		controller.computerTurn(5);
		controller.humanTurn(4);
		controller.computerTurn(4);
		controller.humanTurn(4);
		controller.computerTurn(4);
		controller.humanTurn(6);
		controller.computerTurn(6);
		controller.humanTurn(5);
		controller.computerTurn(5);
		controller.humanTurn(5);
		assertEquals(controller.checkForWinner(),0);
		assertTrue(controller.checkForTie());
	}
	
}
