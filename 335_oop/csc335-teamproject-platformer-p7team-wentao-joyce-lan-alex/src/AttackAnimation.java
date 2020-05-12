import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Class used to implement sprite Animations used for all objects on map
 * @author wentao, joyce, alex, lan
 *
 */
public class AttackAnimation {
	/**
	 * The visual of the object to be animated
	 */
	private ImageView imageView;
	
	/**
	 * Number of frames in the animation
	 */
	private int frames;
	
	/**
	 * Timeline object that plays the animation
	 */
	private Timeline timeLine;
	
	/**
	 * Width of the frame
	 */
	private int width;
	
	/**
	 * Height of the frame
	 */
	private static int HEIGHT = 300;
	
	/**
	 * Speed of the transitions between frames
	 */
	private static Duration SPEED = Duration.millis(200);
	
	/**
	 * Initializes instance vairables and sets up event handler so that
	 * when attack animation is complete, it reverts the object to its
	 * original image and animation.
	 * @param object Character whose attack is being animated
	 * @param width Width of the frame
	 * @param frames Number of frames in animation
	 * @param oldSprite The orignal sprite of the object used to revert its state when complete
	 */
	public AttackAnimation(Character object,ImageView imageView, int width, int frames) {
		this.width = width;
		this.imageView = imageView;
		this.frames = frames;
		timeLine = new Timeline(new KeyFrame(SPEED, new AnimationHandler()));
		timeLine.setCycleCount(frames);
		// Event handler reverts animation to original state when attack complete
		timeLine.setOnFinished(e ->{
			object.endAttack();
		});
		}
	
		/**
		 * Plays animation
		 */
		public void play() {
			timeLine.play();
		}
		
		/**
		 * Notifies user if animation is playing
		 * @return true if it is playing, false otherwise
		 */
		public boolean isPlaying() {
			return timeLine.getStatus().equals(Animation.Status.RUNNING);
		}
		
		/**
		 * Animation class that creates animation
		 * @author Lan, Joyce, Wentao, Alex
		 */
		private class AnimationHandler implements EventHandler<ActionEvent>{
			/**
			 * Tick used to keep track of position of image
			 */
			int tick = 0;
			
			/**
			 * Handler that shifts sprite to create animation
			 */
			public void handle(ActionEvent e) {
				imageView.setViewport(new Rectangle2D(tick*width, 0, width, HEIGHT));
				tick++;
			}}
}
