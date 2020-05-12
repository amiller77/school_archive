import java.time.Duration;

import com.sun.javafx.geom.Rectangle;

import javafx.scene.image.Image;
/**
 * Overall class used to represent all enemies
 */
public class Enemy extends Character{
	
	/**
	 * Move that enemy will take
	 */
	protected int move;
	
	/**
	 * Points enemy is worth
	 */
	protected int points;
	
	/**
	 * Initializes instance variables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Enemy width
	 * @param height Enemy height
	 */
	public Enemy(int x, int y, int width, int height) {
		super(x,y, width, height);
		passable=false;
	}
	
	/**
	 * Generates imageViews
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
	}
	
	/**
	 * PlaceHolder for enemy to hurt mario if he runs into them
	 * @param object Object enemy is interacting with
	 * @param time Animation time used to set mario buffer
	 */
	public void interact(MapObjects object, long time) {
	}
	
	/**
	 * Getter method for enemy next move
	 * @return int next move (Horizontally)
	 */
	public int getMove() {return 0;	}
	
	/**
	 * Getter method for points
	 * @return int points
	 */
	public int getPoints() {
		return points;
	}
}
