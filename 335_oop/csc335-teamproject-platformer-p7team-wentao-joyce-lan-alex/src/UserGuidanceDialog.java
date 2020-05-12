import javafx.animation.Animation;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
 /**
 * the user guidance dialog
 * @author Alexander Miller
 * @author Lan Ngo
 * @author Joyce Wang
 * @author Wentao Zhou
 *
 */
public class UserGuidanceDialog extends Stage{
	/**
	 * tell user how to play the game
	 */
	public UserGuidanceDialog() {
		super();
		initModality(Modality.APPLICATION_MODAL);
		VBox dialogVbox = new VBox(8);
		HBox dialogHbox1 = new HBox(8);
		HBox dialogHbox2 = new HBox(8);
		HBox dialogHbox3 = new HBox(8);
		HBox dialogHbox4 = new HBox(8);
		HBox dialogHbox5 = new HBox(8);
		HBox dialogHbox6 = new HBox(8);
		dialogHbox1.setPadding(new Insets(8, 8, 8, 8));
		dialogHbox2.setPadding(new Insets(8, 8, 8, 8));
		dialogHbox3.setPadding(new Insets(8, 8, 8, 8));
		dialogHbox4.setPadding(new Insets(8, 8, 8, 8));
		dialogHbox5.setPadding(new Insets(8, 8, 8, 8));
		dialogHbox6.setPadding(new Insets(8, 8, 8, 8));
		Rectangle mapImageOne = new Rectangle(0,0,200,250);
		mapImageOne.setFill(new ImagePattern(new Image("file:map1/MapOverview.png")));
		Text text = new Text("Attack: A");
		text.setFont(new Font("Krungthep",16));
		ImageView imageView = new ImageView();
		imageView.setFitHeight(16);
		imageView.setFitWidth(16);
		imageView.setImage(new Image("file:Sprites/fireBall.png"));
		dialogHbox1.getChildren().addAll(text, imageView);
		Text text1 = new Text("pause/unpause: space");
		text1.setFont(new Font("Krungthep",16));
		dialogHbox4.getChildren().add(text1);
		Text text2 = new Text("moving left: <");
		text2.setFont(new Font("Krungthep",16));
		ImageView imageView2 = new ImageView();
		imageView2.setImage(new Image("file:Sprites/standingNoWeaponLeft.png"));
		imageView2.setFitHeight(16);
		imageView2.setFitWidth(16);  
		SpriteAnimation sprite = new SpriteAnimation(imageView2,5,Animation.INDEFINITE);
		sprite.play();
		dialogHbox2.getChildren().addAll(text2, imageView2);
		Text text3 = new Text("moving right: >");
		text3.setFont(new Font("Krungthep",16));
		ImageView imageView3 = new ImageView();
		imageView3.setImage(new Image("file:Sprites/standingNoWeapon.png"));
		imageView3.setFitHeight(16);
		imageView3.setFitWidth(16);  
		SpriteAnimation sprite2 = new SpriteAnimation(imageView3,5,Animation.INDEFINITE);
		sprite2.play();
		dialogHbox3.getChildren().addAll(text3, imageView3);
		Text text4 = new Text("jumping: ^ ");
		text4.setFont(new Font("Krungthep",16));
		dialogHbox5.getChildren().add(text4);
		Text text5 = new Text("use sythe: s ");
		text5.setFont(new Font("Krungthep",16));
		ImageView imageView4 = new ImageView();
		imageView4.setImage(new Image("file:Sprites/sytheSprite.png"));
		imageView4.setFitHeight(16);
		imageView4.setFitWidth(16);  
		SpriteAnimation sprite3 = new SpriteAnimation(imageView4,2,Animation.INDEFINITE);
		sprite3.play();
		dialogHbox6.getChildren().addAll(text5, imageView4);
		dialogVbox.getChildren().addAll(dialogHbox1, dialogHbox4, dialogHbox2, dialogHbox3, dialogHbox5,dialogHbox6);
		StackPane levelOne = new StackPane(mapImageOne,dialogVbox);
		Scene dialogScene = new Scene(levelOne);
		this.setResizable(false);
		setScene(dialogScene);
		showAndWait();
	}
}