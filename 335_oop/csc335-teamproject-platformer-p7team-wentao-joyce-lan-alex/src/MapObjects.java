import java.io.Serializable;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


/**
 * MapObjects serves as a parent class for all interactable objects on
 * Map (ex: items (coins/shrooms), blocks, platforms, enemies)
 */
public class MapObjects implements Serializable{
	/**
	 * Y coordinate of the floor
	 */
	protected static final int FLOOR = 470;
	
	/**
	 * Width of the object
	 */
	protected int width;
	
	/**
	 * Height of object
	 */
	protected int height;
	
	/**
	 * Starting X coordinate
	 */
	protected int startingX;
	
	/**
	 * starting y coordinate
	 */
	protected int startingY;
	
	/**
	 * Current x coordinate
	 */
	protected int x;
	
	/**
	 * Current y coordinate
	 */
	protected int y;
	
	/**
	 * If object is passable or not (passable = true, impassable = false)
	 */
	protected boolean passable;
	
	/**
	 * Current x translation of imageview
	 */
	protected int imageViewTranslateX;
	
	/**
	 * Current y translation of imageview
	 */
	protected int imageViewTranslateY;
	
	/**
	 * Visual generated
	 */
	protected transient ImageView imageView;
	
	/**
	 * Sprite Animator
	 */
	protected transient SpriteAnimation sprite;
	
	/**
	 * Version number for serialization
	 */
	static final long serialVersionUID = 1;

	/**
	 * Instantiates class variables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Object width
	 * @param height Object height
	 */
	public MapObjects(int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = FLOOR - y - height;
		this.startingX = x;
		this.startingY = FLOOR - y - height;
		imageViewTranslateX = 0;
		imageViewTranslateY = 0;
	}
	
	/**
	 * Generates imageview
	 */
	public void setUpVisuals() {
		imageView = new ImageView();
		imageView.relocate(this.startingX, this.startingY);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		
		imageView.setTranslateX(imageViewTranslateX);
		imageView.setTranslateY(imageViewTranslateY);
		
	}
	
	/**
	 * Getter for image of object
	 * @return imageview
	 */
	public ImageView getImageView() {
		return imageView;
	}
	/**
	 * Getter method for passable
	 * @return true if object is passable, false otherwise
	 */
	public boolean getPassable() {
		return passable;
	}
	
	/**
	 * Get X position of current object
	 * @param int x coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get Y position of current object
	 * @param int y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Shifts X position of current object
	 * @param dx Horizontal shift
	 */
	public void changeX(int dx) {
		imageView.setTranslateX(imageView.getTranslateX() + dx);
		imageViewTranslateX += dx;
		x += dx;
	}
	
	/**
	 * Shifts Y position of current object
	 * @param dy Vertical shift
	 */
	public void changeY(int dy) {
		imageView.setTranslateY(imageView.getTranslateY() + dy);
		imageViewTranslateY += dy;
		y += dy;
	}
	
	/**
	 * Gettter for width
	 * @return int width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gettter for Height
	 * @return int height
	 */
	public int getHeight() {
		return height;
	}
	
	
	/**
	 * placeholders for methods that allow mario to interact with objects
	 * @param object Object to be interacted with
	 * @param time Current animation time
	 */
	public void interact(MapObjects object, long time) {}
}