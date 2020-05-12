import java.io.File;
import java.util.Iterator;
import java.util.Observable;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * @author Lan Ngo
 * @author Alexander Miller
 * Generates Connect 4 game gui and allows user to interact with board.
 */
public class Connect4View extends javafx.application.Application implements java.util.Observer{
	final FileChooser fileChooser = new FileChooser();
	/**
	 * Number of columns on board
	 */
	public static int cols = 7;
	/**
	 * Number of rows on board
	 */
	public static int rows = 6;
	/**
	 * Height of board
	 */
	public static int height = rows*40 + 8*(rows + 1);
	/**
	 * Width of board
	 */
	public static int width = cols*40 + 8*(cols + 1);
	/**
	 * Controller that allows view to communicate changes to model
	 */
	private Connect4Controller controller;
	/**
	 * TilePane used to store positions of board
	 */
	private TilePane game;
	
	private boolean done;
	
	/**
	 * Initializes all instance variables.
	 */
	public Connect4View() {
		controller = new Connect4Controller();
		controller.getModel().addObserver(this);
		game = new TilePane();
		done = false;
	}
	
	/**
	 * Generates GUI for Connect 4 game and allows users to click on columns to add
	 * pieces to play the game.
	 * @param stage Stage used to display GUI
	 */
	public void start(Stage stage) {
		// create board: with 7 columns, 6 rows:
		game.setPrefColumns(cols);
		game.setPrefRows(rows);
		// make sure our window has a fixed size, so we can find relative position of things:
		game.setMinWidth(width);
		game.setMaxWidth(width);
		game.setMinHeight(height);
		game.setMaxHeight(height);
		// make sure all the nodes have 8px of padding
		game.setPadding(new Insets(8));
		game.setHgap(8);
		game.setVgap(8);
		
		// Creates board display and adds board circles
		BackgroundFill f = new BackgroundFill(Color.BLUE, null, null);
		Background b = new Background(f);
		game.setBackground(b);
		for (int i = 0; i<rows*cols; i++) {
			Circle circle = new Circle();
			circle.setRadius(20);
			circle.setFill(Color.WHITE);
			game.getChildren().add(circle);
		}
		
		// Allows user to click on board to add pieces
		ClickHandler clickHandler = new ClickHandler();
		this.game.setOnMouseClicked(clickHandler);
		
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".dat","*.dat"));
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("File");
		MenuItem save = new MenuItem("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				fileChooser.setTitle("Save");
				fileChooser.setInitialDirectory(new File("Saves"));
				File saveFile = fileChooser.showSaveDialog(null);
				if(saveFile != null) {
					controller.save(saveFile);
				}
			}
		});
		
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				done = false;
				controller.newGame();
			}
		});
		
		MenuItem loadGame = new MenuItem("Load Game");
		loadGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				fileChooser.setTitle("Open");
				fileChooser.setInitialDirectory(new File("Saves"));
				File openFile = fileChooser.showOpenDialog(null);
				if(openFile != null) {
					done = false;
					controller.open(openFile);
				}
			}
		});
		file.getItems().addAll(newGame,loadGame,save);
		menuBar.getMenus().add(file);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(menuBar,game);
		
		// Displays GUI
		stage.setScene(new Scene(vbox));
		stage.show();
	}
	
	/**
	 * Updates the board if valid move has been made or notifies users if they have won/lost/tied or made
	 * an invalid move.
	 * @param o The observable model that is notifying the view that changes have been made
	 * @param arg Object passed to indicate the type of change in the view that is made
	 */
	public void update(Observable o, Object arg) {
		if(!done) {
			Connect4MoveMessage message = (Connect4MoveMessage) arg;
			// Changes in board for moves
			if(message.getType() == "MOVE") {
				changeColor(message.getRow(),message.getCol(),message.getColor());
			// Notifications for win/loss/tie
			}else if(message.getType() == "INFO"){
				done = true;
				message.getAlert().showAndWait()
			      .filter(response -> response == ButtonType.OK)
			      .ifPresent(response -> formatSystem());
			// Error message for full column
			}else if(message.getType() == "WARNING"){
				message.getAlert().showAndWait()
			      .filter(response -> response == ButtonType.OK)
			      .ifPresent(response -> formatSystem());
			// Clear board
			}else {
				Iterator itr = game.getChildren().iterator();
				for (int i = 0; i<game.getChildren().size(); i++) {
					Circle nextChild = (Circle) itr.next();
					nextChild.setFill(Color.WHITE);
				}
			}
		}
	}
	
	/**
	 * Changes the color of the desired circle on the board
	 * @param r The row of the desired circle
	 * @param c The column of the desired circle
	 * @param val An integer indicating the color to change the circle to
	 */
	private void changeColor(int r, int c, Color color) {
		int location = r*7+c;
		Iterator itr = game.getChildren().iterator();
		for (int i = 0; i<game.getChildren().size(); i++) {
			Circle nextChild = (Circle) itr.next();
			if (i == location) {
				nextChild.setFill(color);
				return;
			}
		}
	}
	
	/**
	 * Required method used to satisfy the alert syntax:
	 */
	private void formatSystem() {}
	
	
	// **** INSIDE CLASS: CLICK HANDLER **** 
	/**
	 * @author Alexander Miller
	 * @author Lan Ngo
	 * Determines which column the user clicked in then communicates with controller
	 * to make a move in that column
	 */
	private class ClickHandler implements EventHandler<MouseEvent> {
		/**
		 * Handler determines which column the user clicked in then communicates with 
		 * controller to make a move in that column.
		 * @param mouse
		 */
		public void handle(MouseEvent mouse) {
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
			controller.move(colNum);;
		}		
	}
	
	
}
