import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;


/**
 * Red heard mario collects that increases lives
 *
 */
public class RedHeart extends Item{
	/**
	 * Instantiates instance vairables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width heart width
	 * @param height heart height
	 */
	public RedHeart(int x, int y, int width, int height) {
		super(x,y, width, height);
	}
	
	/**
	 * Generates imageview and starts sprite animation
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/redHeartSprite.png"));
		sprite = new SpriteAnimation(imageView,2,Animation.INDEFINITE);
		sprite.play();
	}
	
	/**
	 * Increases Marios life
	 */
	public void interact(MapObjects object, long time) {
		if(object instanceof Mario) {
			Mario mario = (Mario) object;
			mario.addLife();
		}
	}
}