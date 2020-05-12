import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
import javafx.stage.WindowEvent;
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
	
	// NETWORK MOVES
	
	/**
	 * Network Dialog -> setting up network games
	 */
	private NetworkDialog networkDialog;
	
	// SOCKET
	private Socket connection;
	
	// OUTPUT STREAM
	private ObjectOutputStream output;
	
	// INPUT STREAM
	private ObjectInputStream input;
	
	// GAME FINISHED?
	public boolean gameFinished;
	
	// SERVER OR CLIENT?
	// SERVER = 1 CLIENT = 2 ; NEITHER(not-networking game) = null
	private Integer playerType;
	
	// READER OR WRITER (NETWORK)
	private boolean reader;
	
	// NETWORK GAME OR SINGLEPLAYER GAME?
	private boolean networkGame;
	
	/**
	 * Initializes all instance variables.
	 */
	public Connect4View() {
		game = new TilePane();
		controller = new SingleController(game);
		controller.getModel().addObserver(this);
		gameFinished = false;
	}

	/**
	 * Generates GUI for Connect 4 game and allows users to click on columns to add
	 * pieces to play the game.
	 * @param stage Stage used to display GUI
	 */
	public void start(Stage stage) {
		// set up network dialog from stage
		this.networkDialog = new NetworkDialog(stage,this.controller,this);
		// set to single player by default
		this.networkGame = false;
		
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
		
		// AUTO-LOAD LAST SAVE
		File starter = new File("Saves/save_game.dat");
		if(starter.exists()) {
			controller.open(starter);
		}
		
		// CREATE MENU
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".dat","*.dat"));
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("File");
		
		// SAVE
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
		
		// NEW GAME
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				gameFinished = false;
				// Goes back to regular non-networking game
				controller = new SingleController(game,controller.newGame());
			}
		});
		
		// LOAD GAME
		MenuItem loadGame = new MenuItem("Load Game");
		loadGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				fileChooser.setTitle("Open");
				fileChooser.setInitialDirectory(new File("Saves"));
				File openFile = fileChooser.showOpenDialog(null);
				if(openFile != null) {
					controller.open(openFile);
				}
			}
		});
		
		// NETWORKED GAME
		MenuItem networkedGame = new MenuItem("Networked Game");
		networkedGame.setOnAction(p -> {
			this.networkDialog.show();
		});
		
		// COMPILE & ADD MENU BAR
		file.getItems().addAll(newGame,loadGame,save, networkedGame);
		menuBar.getMenus().add(file);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(menuBar,game);
		
		// AUTO SAVE (IF GAME HAS NOT BEEN COMPLETED
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent w) {
				if(!gameFinished) {
					File saveFile = new File("Saves/save_game.dat");
					controller.save(saveFile);
				}
			}
		});
		
		// Displays GUI
		stage.setScene(new Scene(vbox));
		stage.show();
	}
	
	// ONLINE GAME
	// serverOrClient -> true: server, false: client
	// humanOrComputer -> true: human, false: computer
	public void onlineGame(Integer portNumber, String serverName, boolean server, boolean human) {
		// log as network game:
		networkGame = true;
		// boolean to record game progress:
		gameFinished = false;
		// establish player type, construct controller
		if (server) {
			playerType = 1;
		} else {
			playerType = 2;
		}
		controller = new MultiController(playerType,human,game,controller.newGame());

		// server-side setup
		if (server) {
			try {
				System.out.println("setting up server... ");
				ServerSocket serverSocket = new ServerSocket(portNumber);
				System.out.println("awaiting client...");
				connection = serverSocket.accept();
				System.out.println("connection established as server.");
				output = new ObjectOutputStream(connection.getOutputStream());
				input = new ObjectInputStream(connection.getInputStream());	
				System.out.println("streams established as server.");
				// we move, don't read
				this.reader = false;
				controller.move();
			} catch (IOException x) {
				x.printStackTrace();
			}
		} // client-side setup
		else {
			try {
				System.out.println("setting up client...");
				connection = new Socket(serverName,portNumber);
				System.out.println("connection established as client.");
				output = new ObjectOutputStream(connection.getOutputStream());
				input = new ObjectInputStream(connection.getInputStream());
				// we read before we move
				this.reader = true;
				System.out.println("streams established as client.");
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Updates the board if valid move has been made or notifies users if they have won/lost/tied or made
	 * an invalid move.
	 * @param o The observable model that is notifying the view that changes have been made
	 * @param arg Object passed to indicate the type of change in the view that is made
	 */
	public void update(Observable o, Object arg) {
		Connect4MoveMessage message = (Connect4MoveMessage) arg;
		
		// determine if this is a real move, or just a board clearing action
		boolean trivialUpdate = false;
		if (message.getColor() ==0) {
			trivialUpdate = true;
		}
		
		// COMMON OPS TO ALL UPDATES:
		// update view:
		updateView(message);
		// check for game end:
		checkForGameEnd();
		
		// SINGLE PLAYER GAME:
		if (!networkGame && !trivialUpdate) {
			// if computer has made move, allow human player to move
			if (!gameFinished && message.getColor()==1) {
				controller.move();
			}
		}
		// NETWORK GAME
		else if (networkGame && !trivialUpdate){
			// if our turn to read, then read move
			if (reader && message.getColor()!=playerType) {
				readNetMessage();
				// move... (?)
				controller.move();
			}
			
			// send message over network if it's our move
			if (!reader && message.getColor()==this.playerType) {
				sendNetMessage(message);
				reader = true;
			}
		}
	}
	
	//******************* HELPERS FOR UPATE ****************************
	// UPDATE VIEW
	// update our view
	private void updateView(Connect4MoveMessage message) {
		if(message.getColor() == message.YELLOW) {
			System.out.println(message.getColor());
			changeColor(message.getRow(),message.getColumn(),Color.YELLOW);
		} else if (message.getColor() == message.RED) {
			System.out.println(message.getColor());
			changeColor(message.getRow(),message.getColumn(),Color.RED);
		}else {
			changeColor(message.getRow(),message.getColumn(),Color.WHITE);
		}
		
	}
	
	// SEND NET MESSAGE
	// send message over network
	private void sendNetMessage(Connect4MoveMessage message) {
		System.out.println("sending move over network");
		System.out.println("sending row: "+message.getRow());
		System.out.println("sending col: "+message.getColumn());
		System.out.println("sending player: "+message.getColor());
		try {
			output.writeObject(message);
			output.flush();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	// READ NET MESSAGE
	private void readNetMessage() {
		System.out.println("reading over network");
		System.out.println("socket closed? :"+connection.isClosed());
		System.out.println("reading as player: "+playerType);
		reader = false; //prevents read -> update -> read again -> ...
		try {
			Connect4MoveMessage otherMessage = (Connect4MoveMessage) input.readObject();
			System.out.println("reading row: "+otherMessage.getRow());
			System.out.println("reading col: "+otherMessage.getColumn());
			System.out.println("reading player: "+otherMessage.getColor());
			controller.otherMove(otherMessage.getRow(), otherMessage.getColumn(), otherMessage.getColor());
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	
	// CHECK FOR GAME END
	private void checkForGameEnd() {
		int checkWin = controller.checkForWinner();
		if(playerType == null && checkWin == 1 || (playerType != null && playerType == checkWin)) {
			gameFinished = true;
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("VICTORY");
			alert.setHeaderText("VICTORY");
			alert.setContentText("CONGRATULATIONS! YOU WIN!");
			alert.showAndWait()
		      .filter(response -> response == ButtonType.OK)
		      .ifPresent(response -> formatSystem());
			
		}else if (playerType == null && checkWin == 2 || (playerType != null && playerType%2 + 1 == checkWin)) {
			gameFinished = true;
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("DEFEAT");
			alert.setHeaderText("DEFEAT");
			alert.setContentText("SHAME! YOU LOSE!");
			alert.showAndWait()
		      .filter(response -> response == ButtonType.OK)
		      .ifPresent(response -> formatSystem());
			
		}else if (controller.checkForTie()) {
			gameFinished = true;
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("STALEMATE");
			alert.setHeaderText("STALEMATE");
			alert.setContentText("ALAS! TIE GAME!");
			alert.showAndWait()
		      .filter(response -> response == ButtonType.OK)
		      .ifPresent(response -> formatSystem());
		}
		
		if(playerType != null && gameFinished) {
			System.out.println("closing connection");
			try {
				connection.close();
			}catch (Exception e) {
				System.out.println("ERROR CLOSING");
			}
		}
	}
	
	//******************* END HELPERS FOR UPATE ****************************
	
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
}
