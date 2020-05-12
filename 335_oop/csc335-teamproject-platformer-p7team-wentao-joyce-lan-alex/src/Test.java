import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;

class Test {

	/**
	 * tests mario and model constructors, and constructors of all map objects
	 */
	@org.junit.jupiter.api.Test
	void modelAndMapObjectConstructorTests() {
		// test mario constructor:
		Mario mario = new Mario(0,0,64,64);
		assertEquals(mario.getX(),0);
		assertEquals(mario.getHeight(),64);
		assertEquals(mario.getWidth(),64);
		assertEquals(mario.getY(),MapObjects.FLOOR - mario.getHeight()-0);
		assertEquals(mario.getAttackPower(),2);
		assertEquals(mario.getLives(),3);
		assertEquals(mario.getCoins(),0);
		assertEquals(mario.getPoints(),0);
		assertFalse(mario.hasWeapon());
		//assertEquals(mario.getMaxLives(),3);
		
		// test model constructor
		Model model = new Model (1,new Mario(0,0,64,64));
		// verify level update:
		assertEquals(model.getLevel(),1);
		// should move mario to new map location
		assertEquals(model.getMario().getX(),550);
		// should rescale mario to 100 x 100
		assertEquals(model.getMario().getHeight(),100);
		assertEquals(model.getMario().getWidth(),100);
		assertEquals(model.getMario().getY(),MapObjects.FLOOR - model.getMario().getHeight()-0);
		
		// test bat, coin, red heart, gold heart, scythe, platform, crawler, hacker constructors:
		MapObjects[] mapObjects = new MapObjects[8];
		Bat bat = new Bat(0,0,64,64);
		Coin coin = new Coin(0,0,64,64);
		RedHeart redHeart = new RedHeart(0,0,64,64);
		GoldHeart goldHeart = new GoldHeart(0,0,64,64);
		Sythe scythe = new Sythe(0,0,64,64);
		Platform platform = new Platform(0,0,64,64);
		Crawler crawler = new Crawler(0,0,64,64);
		Hacker hacker = new Hacker(0,0,64,64);
		mapObjects[0] = bat;
		mapObjects[1] = coin;
		mapObjects[2] = redHeart;
		mapObjects[3] = goldHeart;
		mapObjects[4] = scythe;
		mapObjects[5] = platform;
		mapObjects[6] = crawler;
		mapObjects[7] = hacker;
		// check that position and dimensions correct:
		for (int i = 0; i<mapObjects.length; i++) {
			MapObjects mapObject = mapObjects[i];
			assertEquals(mapObject.getX(),0);
			assertEquals(mapObject.getHeight(),64);
			assertEquals(mapObject.getWidth(),64);
			assertEquals(mapObject.getY(),MapObjects.FLOOR - mapObject.getHeight()-0);
		}
		// verify passability:
		assertFalse(bat.getPassable());
		assertTrue(coin.getPassable());
		assertTrue(redHeart.getPassable());
		assertTrue(goldHeart.getPassable());
		assertTrue(scythe.getPassable());
		assertFalse(platform.getPassable());
		assertFalse(crawler.getPassable());
		assertFalse(hacker.getPassable());
	}
	
	
	/**
	 * launches level 1 setup test [do we set up the model correctly based on map?]
	 */
	@org.junit.jupiter.api.Test
	void levelOneTest() {
		String fileUrl = "map1/Map1";
		int level = 1;
		levelTest(1,fileUrl);
	}
	
	
	/**
	 * launches level 2 setup test [do we set up the model correctly based on map?]
	 */
	@org.junit.jupiter.api.Test
	void levelTwoTest() {
		String fileUrl = "map2/Map2";
		int level = 2;
		levelTest(2,fileUrl);
	}
	
	/**
	 * launches level 3 setup test [do we set up the model correctly based on map?]
	 */
	@org.junit.jupiter.api.Test
	void levelThreeTest() {
		String fileUrl = "map3/Map3";
		int level = 3;
		levelTest(3,fileUrl);
	}
	
	/**
	 * common logic to level tests 1-3
	 */
	void levelTest(int level, String fileUrl) {
		// test model constructor
		Model model = new Model (level,new Mario(0,0,64,64));
		// verify level update:
		assertEquals(model.getLevel(),level);
		// verify proper instantiation of all map objects to match the design:
		List<MapObjects> mapObjects = model.getMapObjects();
		try {
			File file = new File(fileUrl);
			Scanner scanner = new Scanner(file);
			for (MapObjects mapObject : mapObjects) {
				String[] lineArray = scanner.nextLine().split(" ");
				// verify proper location, and existence in mapObjects:
				int fileX = Integer.parseInt(lineArray[1]);
				int fileY = Integer.parseInt(lineArray[2]);
				assertEquals(mapObject.getX(),fileX);
				assertEquals(mapObject.getY(),MapObjects.FLOOR - mapObject.getHeight()-fileY);				
			}
			scanner.close();
		} catch (FileNotFoundException x ) {
			System.out.println("test failed - file not found");
		}
	}
	
	/**
	 * tests any further functionality of mario and controller 
	 * not covered in the constructor tests and not requiring the application to launch
	 */
	@org.junit.jupiter.api.Test
	void marioAndControllerTests() {
		Controller controller = new Controller();
		Mario mario = new Mario(0,0,64,64);
		Model model = new Model(1,mario);
		controller.debug_setModel(model);
		// last hit time
		mario.setLastHitTime(1);
		assertEquals(mario.getLastHitTime(),1);
		// isattacking
		assertFalse(controller.isAttacking());
		// check win, lose
		assertFalse(controller.Win());
		assertFalse(controller.Lose());
		mario.debug_setX(2800);
		assertTrue(controller.Win());
		mario.debug_setLives(0);
		assertTrue(controller.Lose());
	}
	

}
