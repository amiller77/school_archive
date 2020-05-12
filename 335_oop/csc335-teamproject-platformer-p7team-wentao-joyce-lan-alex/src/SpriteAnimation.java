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
 * Class handles sprite animations
 * @author wentao, joyce, alex, lan
 *
 */
public class SpriteAnimation {
	
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
	private static int WIDTH = 300;
	
	/**
	 * Height of the frame
	 */
	private static int HEIGHT = 300;
	
	/**
	 * Speed of the transitions between frames
	 */
	private static Duration SPEED = Duration.millis(200);
	
	/**
	 * Duration of animation
	 */
	private int duration;
	
	/**
	 * Initializes instance variables and sets up animation
	 * @param imageView display of object to be animated
	 * @param frames Number of frames in animation
	 * @param duration Duration of animation
	 */
	public SpriteAnimation(ImageView imageView, int frames, int duration) {
		this.imageView = imageView;
		this.frames = frames;
		this.duration = duration;
		timeLine = new Timeline(new KeyFrame(SPEED, new AnimationHandler()));
		timeLine.setCycleCount(duration);
		}
	
		/**
		 * Plays animation
		 */
		public void play() {
			timeLine.play();
		}
		
		/**
		 * Stops animation
		 */
		public void stop() {
			timeLine.stop();
		}
		
		/**
		 * Notifies user if animation is currently playing
		 * @return true if playing, false otherwise
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
				imageView.setViewport(new Rectangle2D(tick*WIDTH, 0, WIDTH, HEIGHT));
				tick++;
				if(tick == frames) {
					tick = 0;
				}
			}}
}
