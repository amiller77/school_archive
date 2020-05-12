package view;

import controller.Connect4Controller;

import java.util.Iterator;
import java.util.Observable;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


/**
 * CONNECT 4 VIEW
 * acts as the GUI interface of the program
 * @author Alexander Miller
 *
 */
public class Connect4View extends javafx.application.Application implements java.util.Observer{

	// CLASS VARIABLES
	public static int width = 344;
	public static int height = 296;
	
	// INSTANCE VARIABLES
	// stores column ranges
	private double[] columnDelineator;
	// create controller:
	private Connect4Controller controller;
	private TilePane game;
	
	// CONSTRUCTOR
	public Connect4View() {
		this.controller =null;
		// create board: with 7 columns, 6 rows:
		this.game = new TilePane();
		this.game.setPrefColumns(7);
		this.game.setPrefRows(6);
		// make sure our window has a fixed size, so we can find relative position of things:
		this.game.setMinWidth(width);
		this.game.setMaxWidth(width);
		this.game.setMinHeight(height);
		this.game.setMaxHeight(height);
		// make sure all the nodes have 8px of padding
		this.game.setPadding(new Insets(8));
		this.game.setHgap(8);
		this.game.setVgap(8);
		// style the board:
		BackgroundFill f = new BackgroundFill(Color.BLUE, null, null);
		Background b = new Background(f);
		this.game.setBackground(b);
		// add the circles:
		for (int i = 0; i<42; i++) {
			Circle circle = new Circle();
			circle.setRadius(20);
			circle.setFill(Color.WHITE);
			this.game.getChildren().add(circle);
		}
		// add event handler:
		ClickHandler clickHandler = new ClickHandler();
		this.game.setOnMouseClicked(clickHandler);
		// determine column ranges
		double baseMarker = ( (double) width ) /7;
		this.columnDelineator = new double[7];
		for (int i = 0; i<7; i++) {
			this.columnDelineator[i]=baseMarker*(i+1);
		}
	}
	
	/**
	 * ADD CONTROLLER
	 * allows us to add a controller to the view (one that has returned an observer
	 * view for the model
	 * @param controller
	 */
	public void addController(Connect4Controller controller) {
		this.controller = controller;
	}

	/**
	 * START
	 * called by main; start of our program
	 */
	public void start(Stage stage) {
		// first we create the controller, then pass it to the view
		// so that the observer instance passed to the model is the same one we have here
		Connect4Controller controller = new Connect4Controller();
		Connect4View view = controller.instantiateModel();
		view.addController(controller);
		// set up stage and show:
		stage.setScene(new Scene(view.getGame()));
		stage.show();
	}
	
	/**
	 * GET GAME
	 * @return
	 */
	public TilePane getGame() {
		return this.game;
	}
	
	/**
	 * UPDATE
	 * @param o
	 * @param arg will have two possible forms:
	 * 1. arg is an Integer : 0-> tie game, 1-> computer win, 2-> player win, 3-> full stack move violation
	 * 2. arg is an int[] containing at: 0-> row, 1-> col, 2-> value [1 : comp, 2: player]
	 */
	public void update(Observable o, Object arg) {
		// case 1: game is over
		if (arg instanceof Integer) {
			Integer situation = (Integer) arg;
			// if tie game:
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			if (situation==0) {
				// disable clicking, give alert:
				this.getGame().setOnMouseClicked(p->{return;});
				alert.setTitle("STALEMATE");
				alert.setHeaderText("STALEMATE");
				alert.setContentText("ALAS! TIE GAME!");
				alert.showAndWait()
			      .filter(response -> response == ButtonType.OK)
			      .ifPresent(response -> formatSystem());
			} // if computer win:
			else if (situation==1) {
				// disable clicking, give alert:
				this.getGame().setOnMouseClicked(p->{return;});
				alert.setTitle("DEFEAT");
				alert.setHeaderText("DEFEAT");
				alert.setContentText("SHAME! YOU LOSE!");
				alert.showAndWait()
			      .filter(response -> response == ButtonType.OK)
			      .ifPresent(response -> formatSystem());
			} // if player win:
			else if (situation==2) {
				// disable clicking, give alert:
				this.getGame().setOnMouseClicked(p->{return;});
				alert.setTitle("VICTORY");
				alert.setHeaderText("VICTORY");
				alert.setContentText("CONGRATULATIONS! YOU WIN!");
				alert.showAndWait()
			      .filter(response -> response == ButtonType.OK)
			      .ifPresent(response -> formatSystem());
			} // if full stack:
			else if (situation ==3 ) {
				// give alert:
				alert.setTitle("INVALID MOVE");
				alert.setHeaderText("INVALID MOVE");
				alert.setContentText("ATTENTION! Column full. Try again.");
				alert.setAlertType(Alert.AlertType.WARNING);
				 alert.showAndWait()
			      .filter(response -> response == ButtonType.OK)
			      .ifPresent(response -> formatSystem());
			}
		// case 2: move is made -> change color of tile
		} else {
			int[] moveData = (int[]) arg;
			changeColor(moveData[0],moveData[1],moveData[2]);
		}
	}

	/**
	 * CHANGE COLOR
	 * takes location of tile and encoding of target color, colors tile
	 * @param r
	 * @param c
	 * @param val
	 */
	private void changeColor(int r, int c, int val) {
		System.out.println(this);
		System.out.println("");
		System.out.println("r: "+r);
		System.out.println("c: "+c);
		System.out.println("val: "+val);
		int location = r*7+c;
		Color newColor = null;
		if (val == 1) {
			newColor = Color.RED;
		} else if (val == 2) {
			newColor = Color.YELLOW;
		}
		System.out.println("Change color. Length = :"+this.getGame().getChildren().size());
		Iterator itr = this.getGame().getChildren().iterator();
		for (int i = 0; i<this.getGame().getChildren().size(); i++) {
			Circle nextChild = (Circle) itr.next();
			if (i == location) {
				nextChild.setFill(newColor);
				return;
			}
		}
	}
	
	// to satisfy the alert syntax:
	private void formatSystem() {
	}
	
	/**
	 * INSIDE CLASS: CLICK HANDLER
	 * event-handles clicks on GUI
	 */
	private class ClickHandler implements EventHandler<MouseEvent> {
		
		// HANDLE
		// gets the column, makes the moves
		public void handle(MouseEvent e) {
			double x = e.getX();
			Integer column = mapCoordinatesToColumn(x);
			if (column!= null) {
				controller.move(column);
			} else {
				System.out.println("Error: invalid coordinate. "+x);
			}
		}
		
		/**
		 * MAP COORDINATES TO COLUMN
		 * takes x coordinate and returns column; null if invalid query
		 * @param x
		 * @return
		 */
		private Integer mapCoordinatesToColumn(double x) {
			for (int i = 0; i< columnDelineator.length; i++) {
				if (x <= columnDelineator[i]) {
					return i;
				}
			}
			return null;
		}
		
	}
	
}