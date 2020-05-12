import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;



/**
 * Implements platform where mario can walk on
 */
public class Platform extends MapObjects{
	private int x;
	private int y;
	
	/**
	 * Instantiates instance vairables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Platform width
	 * @param height Platform height
	 */
	public Platform(int x, int y,int width, int height) {
		super(x,y, width, height);
		passable=false;
	}
	
	/**
	 * Generates imageview
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/platform.png"));
	}
} 
