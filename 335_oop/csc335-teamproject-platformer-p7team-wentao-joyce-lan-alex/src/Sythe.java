import javafx.animation.Animation;
import javafx.scene.image.Image;
/**
 * Implements sythe item mario can collect as a weapon
 */
 public class Sythe extends Item{
	/**
	 * Instantiates instance vairables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width syth width
	 * @param height sythe height
	 */	 
 	public Sythe(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
 	/**
 	 * Generates imageview and start sprite animation
 	 */
 	public void setUpVisuals() {
 		super.setUpVisuals();
 		imageView.setImage(new Image("file:Sprites/sytheSprite.png"));
 		sprite = new SpriteAnimation(imageView,2,Animation.INDEFINITE);
 		sprite.play();
 	}
 	
 	/**
 	 * Sets mario's weapon attribute to true
 	 * @param object Object sythe interacting with
 	 * @param time Time placeholder
 	 */
	public void interact(MapObjects object, long time) {
		if(object instanceof Item) {
			Mario mario = (Mario) object;
			mario.addMaxLife();
		}
	}
	
}