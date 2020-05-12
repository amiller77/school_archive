import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimationPractice extends Application {

	public static int a = 0;
	public static int b = 0;
	public static Circle circle;
	public static Pane pane;
	
	public void start(Stage stage) {
		pane = new Pane();
		pane.setPrefWidth(800);
		pane.setPrefHeight(500);
		circle = new Circle(100,100,50);
		pane.getChildren().add(circle);
		timeline();
		stage.setScene(new Scene(pane));
		stage.show();
	}
	
	
	public void basicTranslation() {
		TranslateTransition t = new TranslateTransition();
		t.setDuration(Duration.millis(2000));
		t.setNode(circle);
		t.setByX(625);
		t.setCycleCount(5);
		t.setAutoReverse(true);
		t.play();
	}
	
	public void pathTranslation() {
		Path path = new Path();
		path.getElements().add(new MoveTo(100,100));
		path.getElements().add(new LineTo(700,100));
		path.getElements().add(new LineTo(700,400));
		path.getElements().add(new LineTo(100,400));
		path.getElements().add(new LineTo(100,100));
		PathTransition p = new PathTransition();
		p.setDuration(Duration.millis(2000));
		p.setCycleCount(2);
		p.setNode(circle);
		p.setPath(path);
		p.play();
	}
	
	public void timeline() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),new AnimationHandler()));
		timeline.setCycleCount(20);
		timeline.setAutoReverse(true);
		timeline.play();
		System.out.println("yo");
	}
	
	
	private class AnimationHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent event) {
			Image coin = new Image("file:coinSprite.png");
			ImageView imgview = new ImageView(coin);
			Rectangle2D viewport = new Rectangle2D(a,b,300,300);
			if (a < 1800) {
				a = a+300;
			} else {
				a = 0;
			}
			imgview.setViewport(viewport);
			pane.getChildren().add(imgview);
			//.fillProperty((Paint) new ImagePattern(coin));
		}
	}
	
	
}
