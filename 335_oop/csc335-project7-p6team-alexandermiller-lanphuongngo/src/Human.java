import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

public class Human extends Player{
	private TilePane game;
	private boolean done;
	private Connect4Controller controller;
	private Computer comp;
	
	public Human(int player, TilePane game, Connect4Model model,Connect4Controller controller, Computer comp) {
		this.player = player;
		this.game = game;
		this.model = model;
		this.controller = controller;
		this.comp = comp;
		if(player == 1) {
			done = false;
		}else {
			done = true;
		}
		
		// Allows user to click on board to add pieces
		this.game.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouse) {
				if(!getDone()) {
					int colNum = 0;
					if(mouse.getX() > 48*(cols - 1) + 4) {
						colNum = cols - 1;
					}else {
						for(int i = 0; i < cols - 1; i++) {
							if(mouse.getX() <= 48* i + 52) {
								colNum = i;
								break;
							}
						}
					}
					// Displays alert if column is full
					try {
						validateMove(colNum);
					}catch (ColumnFullException ex) {
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setTitle("INVALID MOVE");
						alert.setHeaderText("INVALID MOVE");
						alert.setContentText("ATTENTION! Column full. Try again.");
						alert.showAndWait()
					      .filter(response -> response == ButtonType.OK)
					      .ifPresent(response -> formatSystem());
						return;
					}
					makeMove(colNum);
					done = true;
					if(controller != null) {
						if(!controller.checkForTie() && controller.checkForWinner() == 0) {
							comp.move();
						}
					}
				}
			}
		});
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}
	
	private boolean getDone() {
		return done;
	}
	
	
	/**
	 * Validates a players move and throws the corresponding error if it does not meet the criteria
	 * being an integer from 0 to 6.
	 * @param colNum Move to be validated
	 * @throws IllegalMoveException
	 * @throws ColumnFullException
	 */
	public void validateMove(int col) throws ColumnFullException{
		int[][] board = model.getBoard();
		if(board[0][col] != 0) {
			throw new ColumnFullException(col + " ");
		}
	}
	
	/**
	 * Required method used to satisfy the alert syntax:
	 */
	private void formatSystem() {}
}
