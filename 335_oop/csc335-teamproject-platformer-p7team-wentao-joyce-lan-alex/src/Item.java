import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

/**
 * Represents items that mario collects
 */
public class Item extends MapObjects{
	/**
	 * Instantiates instance vairables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Item width
	 * @param height Item height
	 */
	public Item(int x, int y,int width, int height) {
		super(x,y,width, height);
		passable = true;
	}
}