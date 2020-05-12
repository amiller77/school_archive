import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class NetworkDialog extends Stage {

	// CONTROLLER POINTER
	private Connect4Controller controller;
	private Connect4View view;
	
	// GUI INSTANCE VARIABLES
	private ToggleGroup networkButtons;
	private RadioButton serverRadio;
	private RadioButton clientRadio;
	private ToggleGroup playerButtons;
	private RadioButton humanRadio;
	private RadioButton computerRadio;
	private TextField serverField;
	private TextField portField;
	
	// CONSTRUCTOR
		public NetworkDialog(Window owner, Connect4Controller controller, Connect4View view) {
			super();
			this.controller = controller;
			this.view = view;
			// assign a parent stage from parameter
			initOwner(owner);
			// make showAndWait() call on object modal
			initModality(Modality.APPLICATION_MODAL);
			
			// create layout:
			// outermost container: vertical spacing 20, padding 8
			VBox exoskeleton = new VBox(20);
			exoskeleton.setPadding(new Insets(8));
			
			// network selection buttons:
			HBox networkButtonContainer = new HBox(8);
			Text create = new Text("Create: ");
			networkButtons = new ToggleGroup();
			serverRadio = new RadioButton("Server ");
			clientRadio = new RadioButton("Client ");
			// make server default setting, and add to hbox
			serverRadio.setToggleGroup(networkButtons);
			serverRadio.setSelected(true);
			clientRadio.setToggleGroup(networkButtons);
			// note -> can't add the togglegroup to pane, have to add individ. buttons
			networkButtonContainer.getChildren().addAll(create,serverRadio,clientRadio);
			
			// player buttons:
			HBox playerButtonContainer = new HBox(8);
			Text playAs = new Text("Play as: ");
			playerButtons = new ToggleGroup();
			humanRadio = new RadioButton("Human ");
			computerRadio = new RadioButton("Computer ");
			// make human default setting, and add to hbox
			humanRadio.setToggleGroup(playerButtons);
			humanRadio.setSelected(true);
			computerRadio.setToggleGroup(playerButtons);
			playerButtonContainer.getChildren().addAll(playAs,humanRadio,computerRadio);
			
			// network setting fields:
			HBox networkFieldsContainer = new HBox(8);
			Text serverText = new Text("Server ");
			Text portText = new Text("Port ");
			serverField = new TextField("localhost");
			portField = new TextField("4000");
			networkFieldsContainer.getChildren().addAll(serverText,serverField,portText,portField);
			
			// confirmation buttons:
			HBox confirmationButtonContainer = new HBox(8);
			Button ok = new Button("OK");
			// assign event handler to OK
			OkButtonHandler handler = new OkButtonHandler();
			ok.setOnAction(handler);
			// have stage close if cancel selected
			Button cancel = new Button("Cancel");
			cancel.setOnAction(p->{close();});
			// add to Hbox
			confirmationButtonContainer.getChildren().addAll(ok,cancel);
			
			// compile exoskeleton
			exoskeleton.getChildren().addAll(networkButtonContainer,playerButtonContainer,networkFieldsContainer,confirmationButtonContainer);
			
			// set scene
			this.setScene(new Scene(exoskeleton));
		}
		
		private class OkButtonHandler implements EventHandler<ActionEvent> {
		
			public void handle(ActionEvent event) {
				
				// decide if human or computer:
				boolean humanOrComputer = false;
				// play as human?
				if (playerButtons.getSelectedToggle().equals(humanRadio)) {
					humanOrComputer = true;
				} // ... or as computer?
				else {
					humanOrComputer = false;
				}
			
				// decide if server or client:
				boolean serverOrClient = false;
				// play as server?
				if (networkButtons.getSelectedToggle().equals(serverRadio)) {
					serverOrClient = true;
				} // ... or as client?
				else {
					serverOrClient = false;	
				}
				// close the window
				close();
				// run the online game
				view.onlineGame(Integer.parseInt(portField.getText()),serverField.getText(),serverOrClient,humanOrComputer);
			}
			
		}
		
}