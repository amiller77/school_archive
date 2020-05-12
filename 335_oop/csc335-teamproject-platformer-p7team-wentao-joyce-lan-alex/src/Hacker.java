import java.time.Duration;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 * Bat class implements Hacker enemy
 * @author Lan, Joyce, Wentao, Alex
 */
public class Hacker extends Enemy{

	/**
	 * Initializes instance variables
	 * @param x X coordinate of Hacker
	 * @param y Y coordinate of Hacker
	 * @param width Width of Hacker
	 * @param height Height of Hacker
	 */
	public Hacker(int x, int y, int width, int height) {
		super(x, y, width, height);
		points = 50;
		range = 75;
		lives = 10;
		attackPower = 3;
		passable=false;
		
	}
	
	/**
	 * If mario runs into hacker, it reduces his life by 1 and gives him
	 * a buffer time of 3 second
	 * @param object Object enemy is interacting with
	 * @param time Animation time used to set mario buffer
	 */
	public void interact(MapObjects object, long time) {
		if(object instanceof Mario) {
			Mario mario = (Mario) object;
			if(mario.getLastHitTime() == 0) {
				mario.reduceLife(3);
				mario.setLastHitTime(time);
			}else if(time - mario.getLastHitTime() > Duration.ofSeconds(1).toNanos()) {
				mario.reduceLife(3);
				mario.setLastHitTime(time);
			}
		}
	}
	
	/**
	 * Generates imageview of hacker
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/HackerSprite.png"));
		sprite = new SpriteAnimation(imageView,3,Animation.INDEFINITE);
		sprite.play();
	}
	
	/**
	 * Sets direction hacker is facing and displays correct image
	 */
	public void setFaceDirection(boolean direction) {
		faceDirection = direction;
		if(faceDirection) {
			imageView.setImage(new Image("file:Sprites/hackerSprite.png"));
		}else {
			imageView.setImage(new Image("file:Sprites/hackerSpriteLeft.png"));
		}
		
	}
	
	/**
	 * Returns next move for hacker
	 */
	public int getMove() {
		if(move < 50) {
			move++;
			return -1;
		}
		if(move < 100) {
			move++;
			return 1;
		}if(move == 100) {
			move = 0;
			return 1;
		}
		return 0;
	}
	
	public void attack(MapObjects victim) {
		Mario m = (Mario) victim;
		m.reduceLife(attackPower);
		sprite.stop();
		if(faceDirection) {
			imageView.setImage(new Image("file:Sprites/hackAttackSprite.png"));
		}else {
			imageView.setImage(new Image("file:Sprites/hackAttackSpriteLeft.png"));
		}
		imageView.setViewport(new Rectangle2D(0,0,300,300));
		attackAnim = new AttackAnimation((Character) this, imageView, 300,8);
		attackAnim.play();
	}
	
	public void endAttack() {
		attackAnim = null;
		setFaceDirection(faceDirection);
		sprite.play();
	}

}
