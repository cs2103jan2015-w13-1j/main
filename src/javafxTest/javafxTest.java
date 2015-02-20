package javafxTest;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class javafxTest extends Application{
	
	public static void main(String args[]){
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setTitle("HeyBuddy_v0.001");
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		Text scenetitle = new Text("HeyBuddy!");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
		grid.add(scenetitle, 0, 0);
		
		//mid section-------------------------------
		Group mid = new Group();
		
		Group taskPanel = new Group();
        Rectangle rect1 = new Rectangle(60, 100, 750, 400);
        rect1.setFill(Color.WHITE);
        rect1.setStroke(Color.web("#00687F"));
        rect1.setStrokeWidth(10);
        rect1.setArcHeight(50);
        rect1.setArcWidth(50);
        Text taskTitle = new Text("Tasks");
        taskTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        taskTitle.setX(rect1.getX() + 40);
        taskTitle.setY(rect1.getY() + 35);
        taskPanel.getChildren().addAll(rect1, taskTitle);
         
        Group helpPanel = new Group();
        Rectangle rect2 = new Rectangle(30, 50, 750, 400);
        rect2.setFill(Color.WHITE);
        rect2.setStroke(Color.web("#009CBF"));
        rect2.setStrokeWidth(10);
        rect2.setArcHeight(50);
        rect2.setArcWidth(50);
        Text helpTitle = new Text("Commands");
        helpTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        helpTitle.setX(rect2.getX() + 40);
        helpTitle.setY(rect2.getY() + 35);
        helpPanel.getChildren().addAll(rect2, helpTitle);
                
        Group settingPanel = new Group();
        Rectangle rect3 = new Rectangle(0, 0, 750, 400);
        rect3.setFill(Color.WHITE);
        rect3.setStroke(Color.web("#00D0FF"));
        rect3.setStrokeWidth(10);
        rect3.setArcHeight(50);
        rect3.setArcWidth(50);
        Text settingTitle = new Text("Settings");
        settingTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        settingTitle.setX(rect3.getX() + 40);
        settingTitle.setY(rect3.getY() + 35);
        settingPanel.getChildren().addAll(rect3, settingTitle);
         
        mid.getChildren().addAll(settingPanel, helpPanel, taskPanel);
        
        grid.add(mid,0,1);
		//---------------------------------------
		
		// commandPane section----------------
		GridPane commandPane = new GridPane();
		commandPane.setHgap(10);
		commandPane.setVgap(10);
		commandPane.setPadding(new Insets(25,25,25,25));
		grid.add(commandPane, 0, 2);
		
		Text command = new Text("Command: ");
		command.setFont(Font.font("Tahmoma", FontWeight.NORMAL, 20));
		commandPane.add(command, 0, 0);
		
		Text actionTarget = new Text();
		actionTarget.setFont(Font.font("Tahmoma", FontWeight.NORMAL, 20));
		commandPane.add(actionTarget, 1, 2);
				
		TextField commandTextField = new TextField();
		commandTextField.setPrefWidth(700); 
		commandPane.add(commandTextField, 1, 0);
		commandTextField.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				String input = commandTextField.getText();
				actionTarget.setText("You entered \"" + commandTextField.getText() + "\"");
				commandTextField.clear();
				
				//Verification of user inputs (parser/controller) here //---------------------
				//For HANIF: Set a boolean value for each type of command, to be used in the instructions section below
				//Example:
				//	boolean isGoto = false;
				//	boolean isAdd = false;
				//	etc etc etc....
				boolean isGoto = true;
				//----------------------------------------------------------------------------
				
				//Carry out instructions here //----------------------------------------------
				//Here is a sample for the -goto commands
				if (isGoto == true){
					if (input.equals("2")){
						taskPanel.toBack();
						helpPanel.toFront();
					}
					else if (input.equals("3")){
						helpPanel.toBack();
						settingPanel.toFront();
					}
					else {
						settingPanel.toBack();
						taskPanel.toFront();
					}	
				}
				//----------------------------------------------------------------------------
			}
		});
		
		
		//------------------------------------
		
		Scene scene = new Scene(grid, 900, 700);
		stage.setScene(scene);
		scene.getStylesheets().add(javafxTest.class.getResource("style.css").toExternalForm());
		stage.show();
	}

}
