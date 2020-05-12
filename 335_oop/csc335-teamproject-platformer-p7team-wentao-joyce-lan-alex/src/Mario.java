import java.io.File;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Implements Mario. Animates and stores his lives
 */
public class Mario extends Character{
	/**
	 * Number of coins mario has collected
	 */
	private int coins;
	
	/**
	 * Number of points mario has from killing enemies
	 */
	private int points;
	
	/**
	 * Animation time from when he was last hit
	 */
	private long lastHitTime;
	
	/**
	 * Current max lives (he cannot have more lives than this)
	 */
	private int maxLives;
	
	/**
	 * Flag if he is running (true if he is, flase otherwise)
	 */
	private boolean running;
	
	/**
	 * True if he has obtained the sythe, false otherwise
	 */
	private boolean weapon;
	 
	/**
	 * Initializes instance variables
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Mario width
	 * @param height Mario height
	 */
	public Mario(int x, int y,int width, int height) {
		super(x,y, width, height);
		// Stats
		attackPower = 2;
		lives = 3;
		coins = 0;
		points = 0;
		maxLives = 3;
		running = false;
		weapon = false;
		range = 100;
	}
	
	/**
	 * Generates imageview and starts animations
	 */
	public void setUpVisuals() {
		super.setUpVisuals();
		
		running = false;
		if(weapon) {
			if(faceDirection) {
				imageView.setImage(new Image("file:Sprites/standingSprite.png"));
			}else {
				imageView.setImage(new Image("file:Sprites/standLeftSprite.png"));
			}
		}else {
			if(faceDirection) {
				imageView.setImage(new Image("file:Sprites/standingNoWeapon.png"));
			}else {
				imageView.setImage(new Image("file:Sprites/standingNoWeaponLeft.png"));
			}
		}
		sprite = new SpriteAnimation(imageView,5,Animation.INDEFINITE);
		sprite.play();
	}
	
	/**
	 * Resets his position if he is placed in a new model
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Mario width
	 * @param height Mario height
	 */
	public void resetVisuals(int x, int y, int width, int height) {
		this.x = x;
		this.y = FLOOR - y - height;
		this.startingX = x;
		this.startingY = FLOOR - y - height;
		this.width = width;
		this.height = height;
		imageViewTranslateX = 0;
		imageViewTranslateY = 0;
	}
	
	/**
	 * Set last hit time
	 * @param time Current animation time
	 */
	public void setLastHitTime(long time) {
		lastHitTime = time;
	}
	
	/**
	 * Getter for last hit time
	 * @return long last time hit
	 */
	public long getLastHitTime() {
		return lastHitTime;
	}
	
	/**
	 * increases mario's life by 1 
	 */
	public void addLife() {
		if(lives < maxLives) {
			lives++;
		}
	}
	
	/**
	 * increases maxlives
	 */
	public void addMaxLife() {
		maxLives ++;
		lives = maxLives;
	}
	
	/**
	 * Getter for coins
	 * @return number of coins
	 */
	public int getCoins() {
		return coins;
	}
	
	/**
	 * adds one coin
	 */
	public void addCoins() {
		coins++;
	}
	
	/**
	 * Getter for points
	 * @return number of points
	 */
	public int getPoints() {
		return points;
	}
	
	/**
	 * Add or reduces points
	 */
	public void addReducePoints(int p) {
		points += p;
	}
	
	/**
	 * Sets direction mario is facing and displays correct image
	 */
	public void setFaceDirection(boolean direction) {
		faceDirection = direction;
		if(running) {
			if(weapon) {
				if(faceDirection) {
					imageView.setImage(new Image("file:Sprites/runSprite.png"));
				}else {
					imageView.setImage(new Image("file:Sprites/runLeftSprite.png"));
				}
			}else {
				if(faceDirection) {
					imageView.setImage(new Image("file:Sprites/runNoWSprite.png"));
				}else {
					imageView.setImage(new Image("file:Sprites/runNoWSpriteLeft.png"));
				}
			}
		}else {
			if(weapon) {
				if(faceDirection) {
					imageView.setImage(new Image("file:Sprites/standingSprite.png"));
				}else {
					imageView.setImage(new Image("file:Sprites/standLeftSprite.png"));
				}
			}else {
				if(faceDirection) {
					imageView.setImage(new Image("file:Sprites/standingNoWeapon.png"));
				}else {
					imageView.setImage(new Image("file:Sprites/standingNoWeaponLeft.png"));
				}
			}
		}
	}
	
	/**
	 * Sets running to true
	 */
	public void setRun() {
		
		running = true;
	}
	
	/**
	 * Sets running to false and resets image to standing image
	 */
	public void endRun() {
		running = false;
		setFaceDirection(faceDirection);
	}
	
	/**
	 * Sets that mario has obtained the syth
	 */
	public void setWeapon() {
		weapon = true;
	}
	
	/**
	 * Getter method for weapon
	 * @return true if mario has sythe, false otherwise
	 */
	public boolean hasWeapon() {
		return weapon;
	}
	
	
	/**
	 * Reduces character's life by the attack power of mario
	 * @param victim Victim being attacked
	 */
	public void attack(MapObjects victim) {
		File music = new File(System.getProperty("user.dir") + "/Sounds/whoosh.mp3");
		Media media = new Media(music.toURI().toString()); 
	    MediaPlayer whoosh = new MediaPlayer(media); 
	    whoosh.setStopTime(Duration.seconds(2));
	    whoosh.setAutoPlay(true);
	    whoosh.setVolume(1);
	    whoosh.play();
	    
		if(victim != null && victim instanceof Enemy) {
			Enemy e = (Enemy) victim;
			e.reduceLife(attackPower);
		}
		imageView.setVisible(false);
		sprite.stop();
		if(faceDirection) {
			imageView.setImage(new Image("file:Sprites/attackSprite.png"));
		}else {
			imageView.setTranslateX(imageView.getTranslateX() - 100);
			imageView.setImage(new Image("file:Sprites/attackSpriteLeft.png"));
		}
		imageView.setFitWidth(width*2);
		attackAnim = new AttackAnimation((Character)this, imageView,600,8);
		attackAnim.play();
		imageView.setVisible(true);
		
	}
	
	public void endAttack() {
		attackAnim = null;
		imageView.setVisible(false);
		imageView.setFitWidth(width);
		if(!faceDirection) {
			imageView.setTranslateX(imageView.getTranslateX() + 100);
		}
		setFaceDirection(faceDirection);
		sprite.play();
		imageView.setVisible(true);
	}
	
	/**
	 * Reduces life by passed in integer
	 */
	public void reduceLife(int red) {
		File music = new File(System.getProperty("user.dir") + "/Sounds/ouch.mp3");
		Media media = new Media(music.toURI().toString()); 
	    MediaPlayer ouch = new MediaPlayer(media); 
	    ouch.setAutoPlay(true);
	    ouch.setVolume(0.2);
	    ouch.play();
		lives-= red;
	}
	
	/**
	 * allows us to move mario for testing purposes
	 */
	public void debug_setX(int x) {
		this.x=x;
	}
	
	/**
	 * allows us to configure lives for testing purposes
	 */
	public void debug_setLives(int x) {
		this.lives = x;
	}
}