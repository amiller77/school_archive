import javafx.scene.image.ImageView;

/**
 * Character class contains all objects in game with lives and 
 * the ability to attack
 * @author Lan, Joyce, Wentao, Alex
 */
public class Character extends MapObjects{
	
	/**
	 * Number of lives character has
	 */
	protected int lives;
	
	/**
	 * Direction character is facing (true = right, false = left)
	 */
	protected boolean faceDirection;
	
	/**
	 * The number of lives a character takes if it attacks
	 */
	protected int attackPower;
	
	/**
	 * The attack animation of the character
	 */
	protected transient AttackAnimation attackAnim;
	
	/**
	 * The range in which a character can hit a victim
	 */
	protected int range;
	
	/**
	 * Initializes instance variables
	 */
	public Character(int x, int y, int width, int height) {
		super(x,y, width, height);
		faceDirection = true; 
	}
	
	/**
	 * Getter method for lives
	 * @return number of lives
	 */
	public int getLives() {
		return lives;
	}
	
	/**
	 * Reduces life by passed in integer
	 */
	public void reduceLife(int red) {
		lives-= red;
	}
	
	/**
	 * Method used to set direction character is facing
	 */
	public void setFaceDirection(boolean direction) {
		faceDirection = direction;
	}
	
	/**
	 * Getter method for the direction the character is facing
	 * @return true if right, left otherwise
	 */
	public boolean getFaceDirection() {
		return faceDirection;
	}
	
	
	/**
	 * Getter method for character attack power.
	 * @return the int attack power
	 */
	public int getAttackPower() {
		return attackPower;
	}
	
	/**
	 * Reduces character's life by the attack power of attacker
	 */
	public void attack(MapObjects victim) {}
	
	/**
	 * Method resets sprite after an attack animation
	 */
	public void endAttack() {}
	
	/**
	 * Notifies user if character is in the process of attacking
	 * @return true if character is attacking, false otherwise
	 */
	public boolean attacking() {
		return attackAnim != null;
	}
	
	/**
	 * Getter method for the range of the character
	 * @return int range of the character
	 */
	public int getRange() {
		return range;
	}
}