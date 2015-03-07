

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import Common.Task;
import Parser.Parser;

public class FXController implements Initializable {
		
	Parser parser = new Parser();
	
	private static ArrayList<Task> list = new ArrayList<Task>();
	
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
			/*new FXTable(taskNumber++, "Do JavaFX", "L", "NIL", "NIL", "3/2/2015"),
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
			new FXTable(taskNumber++, "Eat", "H", "NIL", "NIL", "3/2/2015")*/
	);
	
	final ObservableList<FXTable> archive = FXCollections.observableArrayList(
			/*new FXTable(archiveNumber++, "Do JavaFX", "L", "NIL", "NIL", "3/2/2015"),
			new FXTable(archiveNumber++, "Sleep", "H", "NIL", "NIL", "3/2/2015"),
			new FXTable(archiveNumber++, "Eat", "H", "NIL", "NIL", "3/2/2015")*/
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
		
		//call initialise function from Parser HERE
		
	}
	
	public void inputCommand(ActionEvent event){
		
		String input = commandField.getText();
		commandField.clear();
		
		String[] splitCommand = input.split(" ");
		String firstCommand = splitCommand[0];
		
		list = parser.parseIn(input);
		
		//TEST---------------------------------------------------------------
		//list.clear();
		//for(int i = 0; i < 10; i++){
		//	list.add(new Task(0, "test " + i, -1, null, false));
		//}
		//-----------------------------------------------------------------------

		if (firstCommand.equals("-goto")){
			System.out.println(splitCommand[1]);
			executeGoTo(splitCommand[1]);
		}
		else if (firstCommand.equals("-add")){
			
			tasks.clear();
			
			for(int i = 0; i < list.size(); i++){
				int id = i+1;
				String description = list.get(i).getDescription();
				String priority = Integer.toString(list.get(i).getPriority());
				String start = "";
				String end = "";
				String due = "";
				FXTable entry = new FXTable(id, description, priority, start, end, due);
				tasks.add(entry);
				System.out.println("added: " + description + priority + start + end + due);
			}
			
		}
		else if (firstCommand.equals("archive")){
			
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
