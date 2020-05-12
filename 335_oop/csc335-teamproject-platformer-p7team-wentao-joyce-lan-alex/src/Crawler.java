 import java.time.Duration;

import javafx.animation.Animation;
import javafx.scene.image.Image;

/**
 * Bat class implements bat enemy
 * @author Lan, Joyce, Wentao, Alex
 */
public class Crawler extends Enemy{

	/**
	 * Initializes instance variables
	 * @param x X coordinate of Crawler
	 * @param y Y coordinate of Crawler
	 * @param width Width of Crawler
	 * @param height Height of Crawler
	 */
	public Crawler(int x, int y, int width, int height) {
		super(x, y, width, height);
		points = 20;
		range = 0;
		lives = 3;
		passable=false;
		
	}
	
	/**
	 * If mario runs into crawler, it reduces his life by 1 and gives him
	 * a buffer time of 1 second
	 * @param object Object enemy is interacting with
	 * @param time Animation time used to set mario buffer
	 */
	public void interact(MapObjects object, long time) {
		if(object instanceof Mario) {
			Mario mario = (Mario) object;
			if(mario.getLastHitTime() == 0) {
				mario.reduceLife(2);
				mario.setLastHitTime(time);
			}else if(time - mario.getLastHitTime() > Duration.ofSeconds(1).toNanos()) {
				mario.reduceLife(2);
				mario.setLastHitTime(time);
			}
		}
	}
	
	/**
	 * Generates imageview of crawler
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/crawlerSprite.png"));
		sprite = new SpriteAnimation(imageView,4,Animation.INDEFINITE);
		sprite.play();
	}
	
	/**
	 * Returns next move for crawler
	 */
	public int getMove() {
		if(move < 75) {
			move++;
			return -2;
		}
		if(move < 150) {
			move++;
			return 2;
		}if(move == 150) {
			move = 0;
			return 2;
		}
		return 0;
	}

}
