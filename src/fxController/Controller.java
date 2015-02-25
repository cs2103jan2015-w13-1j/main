package fxController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		
	}
	
	@FXML
	private Group taskGroup;
	
	@FXML
	private Group helpGroup;
	
	@FXML
	private Group settingsGroup;
	
	@FXML
	private TextField commandField;
	
	public void inputCommand(ActionEvent event){
		String input = commandField.getText();
		commandField.clear();
		if (input.equals("1")){
			taskGroup.toFront();
		}
		if (input.equals("2")){
			helpGroup.toFront();
		}
		if (input.equals("3")) {
			settingsGroup.toFront();
		}
	}
	
}
