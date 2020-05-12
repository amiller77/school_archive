import java.time.Duration;

import javafx.animation.Animation;
import javafx.scene.image.Image;

/**
 * Bat class implements bat enemy
 * @author Lan, Joyce, Wentao, Alex
 */
public class Bat extends Enemy{

	/**
	 * Initializes instance variables
	 * @param x X coordinate of bat
	 * @param y Y coordinate of Bat
	 * @param width Width of bat
	 * @param height Height of Bat
	 */
	public Bat(int x, int y, int width, int height) {
		super(x, y, width, height);
		points = 10;
		range = 0;
		lives = 1;
		passable=false;
		
	}
	
	/**
	 * Generates imageview of bat
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/batSprite.png"));
		sprite = new SpriteAnimation(imageView,2,Animation.INDEFINITE);
		sprite.play();
	}
	
	/**
	 * Returns next move for bat
	 */
	public int getMove() {
		if(move < 60) {
			move++;
			return -3;
		}
		if(move < 120) {
			move++;
			return 2;
		}if(move == 120) {
			move = 0;
			return 3;
		}
		return 0;
	}
	
	/**
	 * If mario runs into bat, it reduces his life by 1 and gives him
	 * a buffer time of 1 second
	 * @param object Object enemy is interacting with
	 * @param time Animation time used to set mario buffer
	 */
	public void interact(MapObjects object, long time) {
		if(object instanceof Mario) {
			Mario mario = (Mario) object;
			if(mario.getLastHitTime() == 0) {
				mario.reduceLife(1);
				mario.setLastHitTime(time);
			}else if(time - mario.getLastHitTime() > Duration.ofSeconds(1).toNanos()) {
				mario.reduceLife(1);
				mario.setLastHitTime(time);
			}
		}
	}

}
