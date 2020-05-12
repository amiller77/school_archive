import java.time.Duration;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Implements a fireball mario can shoot
 */
public class FireBall extends MapObjects {
	
	/**
	 * Initializes instance variables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Firebal width
	 * @param height Fireball height
	 */
	public FireBall(int x, int y, int width, int height) {
		super(x,FLOOR - height - y, width, height);
	}
	
	/**
	 * Generates imageviews
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/fireBall.png"));
	}
	
	/**
	 * Reduces an enemy's life by one
	 * @param object Object interacting with
	 * @time Time placeholder
	 */
	public void interact(MapObjects object, long time) {
		if(object instanceof Enemy) {
			Enemy e = (Enemy) object;
			e.reduceLife(1);
		}
	}
}
