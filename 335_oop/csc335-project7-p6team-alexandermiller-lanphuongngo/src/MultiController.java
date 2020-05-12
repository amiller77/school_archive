import java.io.ObjectOutputStream;

import javafx.scene.layout.TilePane;

public class MultiController extends Connect4Controller{
	private int playerNum;
	private Player player;

	public MultiController(int playerNum, boolean type, TilePane game, Connect4Model model) {
		super(model);
		this.playerNum = playerNum;
		if(type) {
			player = new Human(playerNum,game,model,null,null);
		}else {
			player = new Computer(playerNum, model);
		}
	}
	public void move() {
		if(player instanceof Human) {
			((Human) player).setDone(false);
		}else {
			player.move();
		}
		
	}
	
	public void otherMove(int row,int col,int player) {
		model.getBoard()[row][col]=player;
		model.set(row, col, player);
	}
	
	
}
