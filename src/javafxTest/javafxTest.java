package javafxTest;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
		
		Text sceneTitle = new Text("HeyBuddy!");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 50));
		sceneTitle.setFill(Color.WHITE);
		grid.add(sceneTitle, 0, 0);
		
		//mid section-------------------------------
		Group mid = new Group();
		
		final Group taskPanel = new Group();
        createPanel(taskPanel, 0, "Tasks", "49CEFF");
        
        //initialise and add task table here
        TableView taskTable = new TableView();
        TableColumn taskList = new TableColumn("Tasks");
        TableColumn archive = new TableColumn("Archive");
        TableColumn id = new TableColumn("#");
        TableColumn description = new TableColumn("Description");
        TableColumn date = new TableColumn("Date");
        taskList.getColumns().addAll(id, description, date);
        archive.getColumns().addAll(id, description);
        taskTable.getColumns().addAll(taskList, archive);
        taskTable.setMaxHeight(360);
        taskTable.setTranslateX(30);
        taskTable.setTranslateY(70);
 
        taskPanel.getChildren().addAll(taskTable);
        //create an observable array list
        
         
        final Group helpPanel = new Group();
        createPanel(helpPanel, 300, "Commands", "41BAE5");
                
        final Group settingPanel = new Group();
        createPanel(settingPanel, 600, "Settings", "369BBF");
         
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
		command.setFont(Font.font("Tahmoma", FontWeight.BOLD, 25));
		command.setFill(Color.WHITE);
		commandPane.add(command, 0, 0);
		
		final Text actionTarget = new Text();
		actionTarget.setFont(Font.font("Tahmoma", FontWeight.BOLD, 25));
		actionTarget.setFill(Color.web("123440"));
		commandPane.add(actionTarget, 1, 2);
				
		final TextField commandTextField = new TextField();
		commandTextField.setPrefWidth(760); 
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
		
		Scene scene = new Scene(grid, 950, 700);
		stage.setScene(scene);
		scene.getStylesheets().add(javafxTest.class.getResource("style.css").toExternalForm());
		stage.show();
	}

	private void createPanel(Group panel, int x, String title, String color) {
		Rectangle tab = new Rectangle(x, 0, 300, 80);
		tab.setFill(Color.web(color));
        tab.setArcHeight(30);
        tab.setArcWidth(30);
		Rectangle rect = new Rectangle(0, 50, 900, 400);
        rect.setFill(Color.web(color));
        rect.setArcHeight(50);
        rect.setArcWidth(50);
        Text panelTitle = new Text(title);
        panelTitle.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 35));
        panelTitle.setFill(Color.web("123440"));
        panelTitle.setX(tab.getX() + 40);
        panelTitle.setY(tab.getY() + 40);
        panel.getChildren().addAll(rect, tab, panelTitle);
	}

}
