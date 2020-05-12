import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
/**
 * Model creates map of a level by creating game layers, reading in textfile of 
 * chosen map and instantiates mario.
 * 
 */
public class Model implements Serializable{
	
	/**
	 * Version
	 */
	static final long serialVersionUID = 1;
	
	/**
	 * Map width
	 */
	private static int WIDTH = 1200;
	
	/**
	 * Map height
	 */
	private static int HEIGHT = 600;
	
	/**
	 * List of all interactable objects
	 */
	private List<MapObjects> mapObjects;
	
	/**
	 * List of all enemy objects
	 */
	private transient List<Enemy> enemies;
	
	/**
	 * user character
	 */
	private Mario mario;
	
	/**
	 * X coordinate of end of map
	 */
	private int endPosition;
	
	/**
	 * Level number
	 */
	private int level; 
	
	/**
	 * name of background image
	 */
	private String backgroundImageFileName;
	
	/**
	 * name of cloud image
	 */
	private String cloudImageFileName;
	
	/**
	 * name of floor image
	 */
	private String floorImageFileName;
	
	/**
	 * name of front image
	 */
	private String frontImageFileName;
	
	/**
	 * Current translation of cloud layer
	 */
	private double cloudTranslateX;
	
	/**
	 * Current translation of floor layer
	 */
	private double floorTranslateX;
	
	/**
	 * Current translation of front layer
	 */
	private double frontTranslateX;
	
	
	/**
	 * Background layer
	 */
	private transient Rectangle background;
	
	/**
	 * Cloud layer
	 */
	private transient Rectangle clouds;
	
	/**
	 * Floor layer, which includes all mapobjects' imageviews
	 */
	private transient Pane floorLayer;
	
	/**
	 * Front layer
	 */
	private transient Rectangle front;
	
	/**
	 * Instantiates instance variables, reads from specified files of level
	 * and generates a map. Adds in old mario if given, otherwise generates
	 * new one
	 * @param level Which level 1-3
	 * @param oldMario A Mario from a previous game, null if new mario is to 
	 * be created
	 */
	public Model(int level, Mario oldMario) {
		// pull the appropriate filenames based on the level
		this.level = level;
		String filename;
		if (level == 1) {
			filename = System.getProperty("user.dir") + "/map1/Map1";
			backgroundImageFileName = "file:map1/background.png";
			cloudImageFileName = "file:map1/clouds.png";
			floorImageFileName = "file:map1/floor.png";
			frontImageFileName = "file:map1/front.png";
		} else if (level == 2) {
			filename = System.getProperty("user.dir") + "/map2/Map2";
			backgroundImageFileName = "file:map2/background.png";
			cloudImageFileName = "file:map2/clouds.png";
			floorImageFileName = "file:map2/floor.png";
			frontImageFileName = "file:map2/front.png";
		} else { //level == 3
			filename = System.getProperty("user.dir") + "/map3/Map3";
			backgroundImageFileName = "file:map3/background.png";
			cloudImageFileName = "file:map3/clouds.png";
			floorImageFileName = "file:map3/floor.png";
			frontImageFileName = "file:map3/front.png";
		}
		
		cloudTranslateX = 0;
		floorTranslateX = 0;
		frontTranslateX = 0;
			
		// Parse text file to create all map objects
		Scanner scan = null;
		try {
			File file = new File(filename);
			scan = new Scanner(file);
		}catch (FileNotFoundException e) {
			System.out.println(filename + " not found.");
			System.exit(1);
		} 
		String line;
		MapObjects newOb;
		mapObjects = new ArrayList<>();
		enemies = new ArrayList<>();
		while (scan.hasNextLine()){
			line = scan.nextLine();
			String[] parts = line.split(" ");
			if(parts[0].equals("Coin")) {
				newOb = new Coin(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),30,30);
			}else if(parts[0].equals("Gold")) {
				newOb = new GoldHeart(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),30,30);
			}else if(parts[0].equals("Red")) {
				newOb = new RedHeart(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),30,30);
			}else if(parts[0].equals("Platform")) {
				newOb = new Platform(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),100,20);
			}else if(parts[0].equals("Block")) {
				newOb = new Block(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),50,50);
			}else if(parts[0].equals("Sythe")) {
				newOb = new Sythe(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]), 100, 100);
			}else if (parts[0].equals("Mario")) {
				if(oldMario != null) {
					oldMario.resetVisuals(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),100,100);
					mario = oldMario;
				}else {
					mario = new Mario(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),100,100);
				}
				mapObjects.add(mario);
				continue;
			}else if (parts[0].equals("Enemy")){
				newOb = new Enemy(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),100,100);
				enemies.add((Enemy)newOb);
			}else if (parts[0].equals("Bat")){
				newOb = new Bat(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),30,30);
				enemies.add((Enemy)newOb);
			}else if (parts[0].equals("Crawler")){
				newOb = new Crawler(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),60,60);
				enemies.add((Enemy)newOb);
			}else if (parts[0].equals("Hacker")){
				newOb = new Hacker(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),130,130);
				enemies.add((Enemy)newOb);
			}else if(parts[0].equals("Sythe")) {
				newOb = new Sythe(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]), 50, 50);
			}else{
				endPosition = Integer.parseInt(parts[1]);
				continue;
			}
			mapObjects.add(newOb);
		}
	}
	
	/**
	 * Generates all map visuals
	 */
	public void setUpVisuals() {
		// BACKGROUND
		Image backImage = new Image(backgroundImageFileName);
		background = new Rectangle(0,0,WIDTH,HEIGHT);
		background.setFill(new ImagePattern(backImage));
		// CLOUDS
		Image cloudImage = new Image(cloudImageFileName);
		clouds = new Rectangle(0,0,cloudImage.getWidth(),HEIGHT);
		clouds.setFill(new ImagePattern(cloudImage));
		clouds.setTranslateX(cloudTranslateX);
		
		// FLOOR LAYER
		Image floorImage = new Image(floorImageFileName);
		Rectangle floor = new Rectangle(0,0,floorImage.getWidth(),HEIGHT);
		floor.setFill(new ImagePattern(floorImage));		
		// Adds mapobject image views to floor Layer
		floorLayer = new Pane(floor);
		for(MapObjects ob: mapObjects) {
			ob.setUpVisuals();
			floorLayer.getChildren().add(ob.getImageView());
		}
		floorLayer.setTranslateX(floorTranslateX);
		
		// FRONT LAYER
		Image frontImage = new Image(frontImageFileName);
		front = new Rectangle(0,0,frontImage.getWidth(),HEIGHT);
		front.setFill(new ImagePattern(frontImage));
		front.setTranslateX(frontTranslateX);
	}
	
	/**
	 * Getter methods for background
	 * @return rectangle containing image of background
	 */
	public Rectangle getBackground() {
		return background;
	}
	
	/**
	 * Getter methods for clouds
	 * @return rectangle containing image of clouds
	 */
	public Rectangle getClouds() {
		return clouds;
	}
	
	/**
	 * Getter methods for floorLayer
	 * @return pane containing images for floorLayer
	 */
	public Pane getFloorLayer() {
		return floorLayer;
	}
	
	/**
	 * Getter methods for front
	 * @return rectangle containing image of front
	 */
	public Rectangle getFront() {
		return front;
	}
	
	/**
	 * Translates map visuals
	 * @param shift Integer shift of map
	 */
	public void setVisualTranslate(int shift) {
		clouds.setTranslateX(clouds.getTranslateX() - shift*.2);
		cloudTranslateX -= shift*.2;
		
		floorLayer.setTranslateX(floorLayer.getTranslateX() - shift);
		floorTranslateX -= shift;
		
		front.setTranslateX(front.getTranslateX() - shift*1.2);
		frontTranslateX -= shift*1.2;
	}
	
	/**
	 * Getter method for map objects
	 * @return list of all map objects
	 */
	public List<MapObjects> getMapObjects(){
		return mapObjects;
	}
	
	/**
	 * Places all enemy objects in enemies list
	 */
	public void setUpEnemies() {
		enemies = new ArrayList<>();
		for(MapObjects ob: mapObjects) {
			if(ob instanceof Enemy) {
				enemies.add((Enemy) ob);
			}
		}
	}
	
	/**
	 * Getter method for all enemy objects
	 * @return list of all enemy objects
	 */
	public List<Enemy> getEnemies(){
		return enemies;
	}
	
	/**
	 * Getter method for mario object
	 * @return mario object
	 */
	public Mario getMario() {
		return mario;
	}
	
	/**
	 * Get the current level
	 * @return an int representing the level
	 */
	
	public int getLevel() {
		return this.level;
	}
	
	/**
	 * Getter method for the end position
	 */
	public int endPosition() {
		return endPosition;
	}
	
	
	// *******************************************************************
	// ************************ DEBUG METHODS ****************************
	
	/**
	 * allows querying current level for debug purposes
	 * @return
	 */
	public int debug_getLevel() {
		return this.level;
	}
	
	
}