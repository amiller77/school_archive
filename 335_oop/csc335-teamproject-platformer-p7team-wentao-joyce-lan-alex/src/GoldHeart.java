import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;


/**
 * GoldHeart mario collects that increases the max lives
 *
 */
public class GoldHeart extends Item{
	/**
	 * Instantiates instance vairables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width heart width
	 * @param height heart height
	 */
	public GoldHeart(int x, int y, int width, int height) {
		super(x,y, width, height);
	}
	
	/**
	 * Generates imageview and starts sprite animation
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/goldHeartSprite.png"));
		SpriteAnimation sprite = new SpriteAnimation(imageView,2,Animation.INDEFINITE);
		sprite.play();
	}
	
	/**
	 * Increases marios max lives by one
	 * @param object Object interacting with
	 * @time Time placeholder
	 */
	public void interact(MapObjects object, long time) {
		if(object instanceof Mario) {
			Mario mario = (Mario) object;
			mario.addMaxLife();
		}
	}
}