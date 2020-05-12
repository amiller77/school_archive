import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.time.Duration;

/**
 * Controller communicates with model to move images on screen and allow
 * characters to interact with each other and other objects.
 * 
 * @author Alexander Miller
 * @author Lan Ngo
 * @author Joyce Wang
 * @author Wentao Zhou
 */
public class Controller {
	/**
	 * Model that stores game data 
	 */
	private Model model;
	
	/**
	 * Displays Live, Coin, Point and Level bar
	 */
	private Pane currDisplay;
	
	/**
	 * Layers of map visual
	 */
	private StackPane allLayers;
	
	/**
	 * The y coordinate where the floor is
	 */
	private static int FLOOR = 470;
	
	/**
	 * Time when last fireBall was fired
	 */
	private long lastFireBall;
	
	/**
	 * Generates a new model for a new game
	 * @param level Level chosen for map
	 */
	public void newGame(int level) {
		model = new Model(level, null);
		model.setUpVisuals();
	}
	
	/**
	 * Generates a new model but maintains the same Mario
	 * object
	 * @param level Level chosen for map
	 */
	public void differentLevel(int level) {
		Mario oldMario = model.getMario();
		model = new Model(level, oldMario);
		model.setUpVisuals();
		model.setUpEnemies();
	}
	
	/**
	 * Saves current model to save_game.dat
	 */
	public void saveGame() {
		FileOutputStream out;
		try {
			out = new FileOutputStream("save_game.dat");
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(this.model);
			oos.close();
			out.close();
		} catch (Exception e) {
			System.out.println("SAVE ERROR");
			e.printStackTrace();
			return;
		}
		System.out.println("Save successful");
	}
	
	/**
	 * Generates model from saved data. If there is no saved data, it returns 0.
	 * Otherwise it returns the int of the level the saved data is at
	 * @return level of model
	 */
	public int oldGame() {
		int level = 0;
		try {
			FileInputStream fis = new FileInputStream("save_game.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			model = (Model) ois.readObject();
			model.setUpVisuals();
			model.setUpEnemies();
			level = model.getLevel();
			}
		catch (IOException e){
			System.out.println("There is no saved game");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR WITH MODEL CLASS DESERIALIZATION");
		}
		return level;
	}
	
	/**
	 * Getter method returns all visual aspects of game in a stackpane
	 * (all characters, items, background layers, info display)
	 * @return StackPane of all map visuals
	 */
	public StackPane getAllLayers() {
		currDisplay = updateDisplay();
		Rectangle r = new Rectangle(0,450,20,20);
		r.setFill(Color.BLUE);
		Pane backdrop = new Pane(model.getBackground(),model.getClouds());
		Pane foreground = new Pane(model.getFront());
		allLayers = new StackPane(
				backdrop,
				model.getFloorLayer(),
				foreground, 
				currDisplay				
				);
		
		return allLayers;
	}
	
	/**
	 * This method updates the Pane containing coin, life, point, level counter
	 * and returns it
	 * @return new Pane containing updated display info
	 */
	public Pane updateDisplay() {
		Color textColor = Color.BLACK;
		if(model.getLevel() == 3) {
			textColor = Color.WHITE;
		}
		Pane display;
		Text lives = new Text(50, 25, "LIVES");
		lives.setFont(Font.font("Krungthep", 30));
		lives.setFill(textColor);
		
		Text level = new Text(350, 25, "LEVEL " + model.getLevel());
		level.setFont(Font.font("Krungthep", 30));
		level.setFill(textColor);
		
		Text coins = new Text(550, 25, "COINS " + model.getMario().getCoins());
		coins.setFont(Font.font("Krungthep", 30));
		coins.setFill(textColor);
		
		Text points = new Text(750, 25, "POINTS " + model.getMario().getPoints());
		points.setFont(Font.font("Krungthep", 30));
		points.setFill(textColor);
		int life = model.getMario().getLives();
		display = new Pane(lives);
		Image heartImage = new Image("file:Sprites/redHeartSprite.png");
		ImageView heart;
		for(int i = 0; i < life; i++) {
			heart  = new ImageView(heartImage);
			heart.setFitHeight(35);
			heart.setFitWidth(35);
			heart.setViewport(new Rectangle2D(0,0,300,300));
			heart.setX(140 + 40*(i%5));
			heart.setY(0 + 40*Math.floor(i/5));
			display.getChildren().add(heart);
		}
		display.getChildren().addAll(level,coins,points);
		
		return display; 
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// GAME PLAY /////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Translates Mario and allows him to interact with map objects
	 * @param dx Horizontal shift
	 * @param dy Vertical shift
	 */
	public void move(int dx, int dy) {
		if(dy != 0) {
			moveVertical(model.getMario(),dy);
		}
		if(dx != 0) {
			model.getMario().setRun();
			moveHorizontal(model.getMario(),dx);
		}
	}
	
	/**
	 * Stops running animation
	 */
	public void endMove() {
		model.getMario().endRun();
	}
	
	/**
	 * Determines if user has lost game
	 * @return true if lost, false otherwise
	 */
	public boolean Lose() {
		return model.getMario().getLives() <= 0;
	}
	
	/**
	 * Determines if user has won
	 * @return true if won, false otherwise
	 */
	public boolean Win() {
		return model.getMario().getX() >= model.endPosition();
	}	
	
	/**
	 * Allows mario to attack
	 * @return true if mario has a weapon, false otherwise
	 */
	public void attack() {
		if(!model.getMario().hasWeapon()) {
			return;
		}
		MapObjects victim;
		if(model.getMario().getFaceDirection()) {
			victim = getRight(model.getMario(),model.getMario().getRange());
		}else {
			victim = getLeft(model.getMario(),model.getMario().getRange());
		}
		model.getMario().attack(victim);
		if(victim != null && victim instanceof Enemy) {
			Enemy e = (Enemy) victim;
			if(e.getLives() == 0) {
				model.getMapObjects().remove(victim);
				model.getEnemies().remove((Enemy) victim);
				model.getFloorLayer().getChildren().remove(victim.getImageView());
				model.getMario().addReducePoints(e.getPoints());
			}
		}
	}
	
	/**
	 * Notifies user if mario is in the proccess of attacking
	 * @return true if he is attacking, false otherwise
	 */
	public boolean isAttacking() {
		return model.getMario().attacking();
	}
	
	/**
	 * Allows mario to shoot a fireball
	 */
	public void fireBall(long time) {
		if(lastFireBall == 0 || time - lastFireBall >= Duration.ofSeconds(1).toNanos()) {
			lastFireBall = time;
			int direction;
			MapObjects fireBall;
			if(model.getMario().getFaceDirection()) {
				direction = 1;
				fireBall = new FireBall(model.getMario().getX() + model.getMario().getWidth(), model.getMario().getY(),20,20);
			}else {
				direction = -1;
				fireBall = new FireBall(model.getMario().getX(), model.getMario().getY(),20,20);
			}
			fireBall.setUpVisuals();
			MapObjects victim;
			 
			model.getFloorLayer().getChildren().add(fireBall.getImageView());
			AnimationTimer fireBallAnimation = new AnimationTimer() {
				private int tick = 0;
				@Override
				public void handle(long time) {
					// Duration of fireball = 50 ticks
					if(tick == 50) {
						model.getFloorLayer().getChildren().remove(fireBall.getImageView());
						this.stop();
					}
					// Getting fireBall collisions
					MapObjects victim;
					if(direction <0) {
						victim = getLeft(fireBall,10);
					}else {
						victim = getRight(fireBall, 10);
					}
					// If fireball hits impassable object, terminates fireball
					if(victim != null && !victim.getPassable()) {
						model.getFloorLayer().getChildren().remove(fireBall.getImageView());
						this.stop();
						// If enemy, fireball reduces enemy's life by 1
						if(victim instanceof Enemy) {
							Enemy victimOb = (Enemy) victim;
							fireBall.interact(victim, time);
							// Remove enemy from view if 0 lives
							if(victimOb.getLives() == 0) {
								model.getMapObjects().remove(victim);
								model.getEnemies().remove((Enemy) victim);
								model.getFloorLayer().getChildren().remove(victim.getImageView());
								model.getMario().addReducePoints(victimOb.getPoints());
							}
						}
						return;
					}
					fireBall.changeX(direction*10);
					tick ++;
				}
			};
			fireBallAnimation.start();
		}
	}
	
	
	/**
	 * Shifts an objects x coordinate by dx, if there is an object in that
	 * path and is impassable, it will shift objects 1px next to that object.
	 * If objects is center of the screen and moving to the right, it shifts the
	 * screen accordingly to create depth.
	 * @param object Character we are moving
	 * @param dx Horizontal shift
	 */
	private void moveHorizontal(Character object, int dx) {
		MapObjects col;
		int shift = dx;
		// Adjusting shift if theere is an object in the path
		if(dx < 0) {
			object.setFaceDirection(false);
			col = getLeft(object,-dx);
			if(col != null && !col.getPassable()) {
				shift = (col.getX() + col.getWidth()) - object.getX()+1;
			}
		}
		else {
			object.setFaceDirection(true);
			col = getRight(object,dx);
			if(col != null && !col.getPassable()) {
				shift = col.getX() -object.getWidth() - object.getX()-1;
			}
		}
		// Shift background if mario is center
		if(object instanceof Mario && shift > 0 
				&&( model.getFloorLayer().getTranslateX() == - object.getImageView().getTranslateX()
				||model.getFloorLayer().getTranslateX() +5 >= - object.getImageView().getTranslateX())) {
			model.setVisualTranslate(shift);
		}
		// Shifting Character if he is not at the edge of the map
		if((shift < 0 && object.getImageView().getTranslateX() > -(model.getFloorLayer().getTranslateX() + 600)) ||
			(shift > 0 && object.getX() <= model.endPosition())) {
			object.changeX(shift);
		}
	}
	
	/**
	 * Shifts an objects y coordinate by dy, if there is an object in that
	 * path and is impassable, it will shift objects 1px next to that object.
	 * @param object Character we are moving
	 * @param dy Vertical shift
	 */
	private void moveVertical(Character object, int dy) {
		// It does not allow mario to pass below the floor
		if(object.getY() + object.getHeight()+ dy >= FLOOR) {
			object.changeY(FLOOR - object.getY()-object.getHeight());
			object.getImageView().setTranslateY(0);
			return;
		}
		
		// Adjusting shift if theere is an object in the path
		MapObjects col;
		int shift = dy;
		if(dy < 0) {
			col = getTop(object,-dy);
			if(col != null && !col.getPassable()) {
				shift = (col.getY() + col.getHeight()) - object.getY()+1;
			}
		}
		else {
			col = getBottom(object,dy);
			if(col != null && !col.getPassable()) {
				shift = col.getY() - object.getHeight() - object.getY()-1;
			}
		}
		object.changeY(shift);
	}
	
	
	/**
	 * Moves all enemies and handles their collisions
	 * @param nanoTime Current time in animation used to give mario a buffer time
	 */
	public void moveEnemies(long nanoTime) {
		int move;
		MapObjects col;
		for(Enemy e: model.getEnemies()) {
			move =e.getMove();
			if(move < 0) {
				e.setFaceDirection(false);
				col = getLeft(e,e.getRange());
			}else {
				e.setFaceDirection(true);
				col = getRight(e,e.getRange());
			}
			if(e instanceof Hacker) {
				if(col != null && col instanceof Mario && !e.attacking()) {
					System.out.println("HERE");
					e.attack(col);
				}else {
					moveHorizontal(e,move);
				}
			}else {
				moveHorizontal(e,move);
			}
		}
	}
	
	/**
	 * Handles collisions from the right side. If there is an object to the right in
	 * range of 1px, mario will interact with it then it will return if the object is
	 * passable or not so that the user can either move through it or not.
	 * @param time Current time in animation used to give mario a buffer time
	 * @return true if mario can pass through, false otherwise
	 */
	public boolean rightCollision(long time) {
		MapObjects col = getRight(model.getMario(),1);
		if(col == null) {
			return true;
		}
		interact(col,time);
		allLayers.getChildren().remove(currDisplay);
		currDisplay = updateDisplay();
		allLayers.getChildren().add(currDisplay);
		return col.getPassable();
	}
	
	/**
	 * Handles collisions from the left side. If there is an object to the left in
	 * range of 1px, mario will interact with it then it will return if the object is
	 * passable or not so that the user can either move through it or not.
	 * @param time Current time in animation used to give mario a buffer time
	 * @return true if mario can pass through, false otherwise
	 */
	public boolean leftCollision(long time) {
		MapObjects col = getLeft(model.getMario(),1);
		if(col == null) {
			return true;
		}
		interact(col,time);
		allLayers.getChildren().remove(currDisplay);
		currDisplay = updateDisplay();
		allLayers.getChildren().add(currDisplay);
		return col.getPassable();
	}
	
	/**
	 * Handles collisions from the bottom side. If there is an object to the bottom in
	 * range of 1px, mario will interact with it then it will return if the object is
	 * passable or not so that the user can either move through it or not.
	 * @param time Current time in animation used to give mario a buffer time
	 * @return true if mario can pass through, false otherwise
	 */
	public boolean bottomCollision(long time) {
		// If mario reaches the floor it counts as an impassable collision
		if(model.getMario().getY() + model.getMario().getHeight() >= FLOOR) {
			return false;
		}
		MapObjects col = getBottom(model.getMario(),1);
		if(col == null) {
			return true;
		}
		interact(col,time);
		allLayers.getChildren().remove(currDisplay);
		currDisplay = updateDisplay();
		allLayers.getChildren().add(currDisplay);
		return col.getPassable();
	}
	
	/**
	 * Handles collisions from the top side. If there is an object to the top in
	 * range of 1px, mario will interact with it then it will return if the object is
	 * passable or not so that the user can either move through it or not.
	 * @param time Current time in animation used to give mario a buffer time
	 * @return true if mario can pass through, false otherwise
	 */
	public boolean topCollision(long time) {
		MapObjects col = getTop(model.getMario(),1);
		if(col == null) {
			return true;
		}
		interact(col,time);
		allLayers.getChildren().remove(currDisplay);
		currDisplay = updateDisplay();
		allLayers.getChildren().add(currDisplay);
		return col.getPassable();
	}
	
	/**
	 * Returns a MapObject to the bottom of the object within the specified distance
	 * if any
	 * @param object Object we are determining collisions for
	 * @param distance Distance from source counted as a collision
	 * @return MapObject that is causing a collision, null if none exist
	 */
	private MapObjects getBottom(MapObjects object, int distance) {		
		int minX = object.getX();
		int maxX = object.getX() + object.getWidth();
		int minY = object.getY();
		int maxY = object.getY() + object.getHeight() + distance;
		
		for(MapObjects ob: model.getMapObjects()) {
			if(ob.equals(object)) {
				continue;
			}
			if(ob.getY()>= minY && ob.getY()  <= maxY) {
				if((ob.getX() > minX && ob.getX() < maxX)
					||(ob.getX() + ob.getWidth() > minX && ob.getX() + ob.getWidth() < maxX)
					||(object.getX() > ob.getX() && object.getX() < ob.getX() + ob.getWidth())
					||(object.getX() + object.getWidth() > ob.getX() && object.getX() + object.getWidth() < ob.getX() + ob.getWidth())) {
						return ob;
					}
			}
		}
		return null;
	}
	
	/**
	 * Returns a MapObject to the top of the object within the specified distance
	 * if any
	 * @param object Object we are determining collisions for
	 * @param distance Distance from source counted as a collision
	 * @return MapObject that is causing a collision, null if none exist
	 */
	private MapObjects getTop(MapObjects object, int distance) {		
		int minX = object.getX();
		int maxX = object.getX() + object.getWidth();
		int minY = object.getY() - distance;
		int maxY = object.getY() + object.getHeight();
		
		for(MapObjects ob: model.getMapObjects()) {
			if(ob.equals(object)) {
				continue;
			}
			if(ob.getY() + ob.getHeight()>= minY && ob.getY() + ob.getHeight()<= maxY) {
				if((ob.getX() > minX && ob.getX() < maxX)
					||(ob.getX() + ob.getWidth() > minX && ob.getX() + ob.getWidth() < maxX)
					||(object.getX() > ob.getX() && object.getX() < ob.getX() + ob.getWidth())
					||(object.getX() + object.getWidth() > ob.getX() && object.getX() + object.getWidth() < ob.getX() + ob.getWidth())) {
						return ob;
					}
			}
		}
		return null;
	}
	
	
	
	/**
	 * Returns a MapObject to the right of the object within the specified distance
	 * if any
	 * @param object Object we are determining collisions for
	 * @param distance Distance from source counted as a collision
	 * @return MapObject that is causing a collision, null if none exist
	 */
	private MapObjects getRight(MapObjects object,int distance) {
		int minX = object.getX();
		int maxX = object.getX() + object.getWidth() + distance;
		int minY = object.getY();
		int maxY = object.getY() + object.getHeight();
		
		for(MapObjects ob: model.getMapObjects()) {
			if(ob.equals(object)) {
				continue;
			}
			if(ob.getX() >= minX && ob.getX() <= maxX) {
				if((ob.getY() > minY && ob.getY() < maxY)
					||(ob.getY() + ob.getHeight() < minY && ob.getY() + ob.getHeight() > maxY)
					||(object.getY() > ob.getY() && object.getY() < ob.getY() + ob.getHeight())
					||(object.getY() + object.getHeight() > ob.getY() && object.getY() + object.getHeight() < ob.getY() + ob.getHeight())) {
					return ob;
					}
			}
		}
		return null;
	}
	
	/**
	 * Returns a MapObject to the left of the object within the specified distance
	 * if any
	 * @param object Object we are determining collisions for
	 * @param distance Distance from source counted as a collision
	 * @return MapObject that is causing a collision, null if none exist
	 */
	private MapObjects getLeft(MapObjects object, int distance) {
		int minX = object.getX() - distance;
		int maxX = object.getX() + object.getWidth();
		int minY = object.getY();
		int maxY = object.getY() + object.getHeight();
		
		for(MapObjects ob: model.getMapObjects()) {
			if(ob.equals(object)) {
				continue;
			}
			if(ob.getX() + ob.getWidth() >= minX && ob.getX() + ob.getWidth()<= maxX) {
				if((ob.getY() > minY && ob.getY() < maxY)
					||(ob.getY() + ob.getHeight() < minY && ob.getY() + ob.getHeight() > maxY)
					||(object.getY() > ob.getY() && object.getY() < ob.getY() + ob.getHeight())
					||(object.getY() + object.getHeight() > ob.getY() && object.getY() + object.getHeight() < ob.getY() + ob.getHeight())) {
						return ob;
					}
			}
		}
		return null;
	}
	
	
	/**
	 * Helper function prompts object to interact with mario and removes object from view
	 * if the object is an item mario is collecting
	 * @param col object to interact with mario
	 * @param time Animation time used to allow mario to have a buffer after being attacked
	 */
	private void interact(MapObjects col,long time) {
		if(col instanceof Item) {
			model.getMapObjects().remove(col);
			model.getFloorLayer().getChildren().remove(col.getImageView());
		}
		if (col instanceof Sythe) {
			model.getMario().setWeapon();
			model.getMario().addMaxLife();
		}
		col.interact(model.getMario(), time);
	}
	
	/**
	 * allows external configuration of model for testing purposes
	 * @param m
	 */
	public void debug_setModel(Model m) {
		this.model = m;
	}
	
}