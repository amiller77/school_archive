import javafx.scene.image.Image;

public class Block extends Enemy{
	public Block(int x, int y, int width, int height) {
		super(x, y, width, height);
		lives = 3;
		points = 5;
		passable=false;
	}
	public void setUpVisuals() {
		super.setUpVisuals();
		imageView.setImage(new Image("file:Sprites/block.png"));
	}
}
