import java.io.Serializable;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;

/**
 * Connect4MoveMessage is used to store the most recent move done by a player
 * @author Lan Ngo
 * @author Alexander Miller
 */
public class Connect4MoveMessage{
	/**
	 * Most recent row where piece was placed
	 */
	private int row;
	/**
	 * Most recent col where piece was placed
	 */
	private int col;
	/**
	 * Color representing most recent player (red user, yellow computer)
	 */
	private Color color;
	/**
	 * Alert for display
	 */
	private Alert alert;
	/**
	 * Alert type
	 */
	private String type;
	
	/**
	 * Instansiates instance variables
	 * @param row Most recent row where piece was placed
	 * @param col Most recent col where piece was placed
	 * @param color Color representing most recent player (red user, yellow computer)
	 */
	public void set(int row, int col, Color color) {
		type = "MOVE";
		this.row = row;
		this.col = col;
		this.color = color;
	}
	
	public void setAlert(String alerttype, String title, String header, String context) {
		type = alerttype;
		if(alerttype == "INFO") {
			alert = new Alert(Alert.AlertType.INFORMATION);
		}else {
			alert = new Alert(Alert.AlertType.WARNING);
		}
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(context);
	}
	
	public void setClear() {
		type = "CLEAR";
	}
	
	/**
	 * Getter method for row
	 * @return Most recent row where piece was placed
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Getter method for col
	 * @return Most recent col where piece was placed
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * Getter method for color
	 * @return Color representing most recent player (red user, yellow computer)
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Getter method for alert
	 * @return alert to display
	 */
	public Alert getAlert() {
		return alert;
	}
	
	/**
	 * Getter method for type
	 * @return type of alert to display (null if none)
	 */
	public String getType() {
		return type;
	}
}
