import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;


/**
 * Implements coin that the main character collects
 * @author Lan, Joyce, Wentao, Alex
 */
public class Coin extends Item{    
    /**
     * Instantiates instance variables
     */
	public Coin(int x, int y,int width, int height) {
		super(x,y, width, height);
	}
	
	/**
	 * Generates the imageview and starts the sprite animation
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/coinSprite.png"));
		SpriteAnimation sprite = new SpriteAnimation(imageView,6,Animation.INDEFINITE);
		sprite.play();
	}
	
	/**
	 * Increases marios coin count when he collects it
	 */
	public void interact(MapObjects object, long time) {
		if(object instanceof Mario) {
			Mario mario = (Mario) object;
			mario.addCoins();
		}
	}
}