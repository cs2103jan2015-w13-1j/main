
import java.net.URL;
import java.util.ResourceBundle;







import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXController implements Initializable {
		
	//Parser parser = new Parser();
	
	@FXML
	private Group taskGroup;
	@FXML
	private Group helpGroup;
	@FXML
	private Group settingsGroup;
	@FXML
	private TextField commandField;
	
	@FXML
	TableView<FXTable> taskTable;
	@FXML
	TableColumn<FXTable, Integer> taskId;
	@FXML
	TableColumn<FXTable, String> taskDescription;
	@FXML
	TableColumn<FXTable, String> taskPriority;
	@FXML
	TableColumn<FXTable, String> taskStart;
	@FXML
	TableColumn<FXTable, String> taskEnd;
	@FXML
	TableColumn<FXTable, String> taskDue;

	@FXML
	TableView<FXTable> archiveTable;
	@FXML
	TableColumn<FXTable, Integer> archiveId;
	@FXML
	TableColumn<FXTable, String> archiveDescription;
	
	private int taskNumber = 1;
	private int archiveNumber = 1;
	
	final ObservableList<FXTable> tasks = FXCollections.observableArrayList(
			new FXTable(taskNumber++, "Do JavaFX", "L", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Sleep", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Eat", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Do JavaFX", "L", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Sleep", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Eat", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Do JavaFX", "L", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Sleep", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Eat", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Do JavaFX", "L", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Sleep", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Eat", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Do JavaFX", "L", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Sleep", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(taskNumber++, "Eat", "H", "NIL", "NIL", "3/2/2015")
	);
	
	final ObservableList<FXTable> archive = FXCollections.observableArrayList(
			new FXTable(archiveNumber++, "Do JavaFX", "L", "NIL", "NIL", "3/2/2015"),
			new FXTable(archiveNumber++, "Sleep", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(archiveNumber++, "Eat", "H", "NIL", "NIL", "3/2/2015")
	);
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		
		taskId.setCellValueFactory(new PropertyValueFactory<FXTable, Integer>("id"));
		taskDescription.setCellValueFactory(new PropertyValueFactory<FXTable, String>("description"));
		taskPriority.setCellValueFactory(new PropertyValueFactory<FXTable, String>("priority"));
		taskStart.setCellValueFactory(new PropertyValueFactory<FXTable, String>("start"));
		taskEnd.setCellValueFactory(new PropertyValueFactory<FXTable, String>("end"));
		taskDue.setCellValueFactory(new PropertyValueFactory<FXTable, String>("due"));
		taskTable.setItems(tasks);
		
		archiveId.setCellValueFactory(new PropertyValueFactory<FXTable, Integer>("id"));
		archiveDescription.setCellValueFactory(new PropertyValueFactory<FXTable, String>("description"));
		archiveTable.setItems(archive);
		
	}
	
	public void inputCommand(ActionEvent event){
		
		String input = commandField.getText();
		commandField.clear();
		
		//input = parser.parseIn(input);
		boolean isGoTo = false;
		boolean isAdd = false;
		boolean isArchive = true; 
		
		if (isGoTo){
			executeGoTo(input);
		}
		else if (isAdd){
			
			int id = taskNumber++;
			String description = input.substring(0, input.indexOf(" "));
			input = input.substring(input.indexOf(" ") + 1);
			String priority = input.substring(0, input.indexOf(" "));
			input = input.substring(input.indexOf(" ") + 1);
			String start = input.substring(0, input.indexOf(" "));
			input = input.substring(input.indexOf(" ") + 1);
			String end = input.substring(0, input.indexOf(" "));
			input = input.substring(input.indexOf(" ") + 1);
			String due = input;
			
			FXTable entry = new FXTable(id, description, priority, start, end, due);
			
			tasks.add(entry);
			
		}
		else if (isArchive){
			
			int deleteIndex = Integer.parseInt(input);
			FXTable entry = tasks.get(deleteIndex-1);
			entry.setId(archiveNumber++);
			taskNumber--;
			tasks.remove(deleteIndex-1);
			archive.add(entry);
			for(int i = deleteIndex-1; i < tasks.size(); i++){
				tasks.get(i).setId(i+1);
			}
		}
		
	}

	private void executeGoTo(String input) {
		if (input.equals("tasks")){
			taskGroup.toFront();
		}
		if (input.equals("commands")){
			helpGroup.toFront();
		}
		if (input.equals("settings")) {
			settingsGroup.toFront();
		}
	}
	
}
