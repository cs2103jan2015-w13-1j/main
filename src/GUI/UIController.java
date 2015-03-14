package GUI;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import Common.Task;
import Parser.CommandController;;

public class UIController implements Initializable {
	
	@FXML
	private Group taskGroup;
	@FXML
	private Group helpGroup;
	@FXML
	private Group settingsGroup;
	@FXML
	private TextField commandField;
	@FXML
	private Button directoryChooserBtn;
	
	@FXML
	TableView<UITask> taskTable;
	@FXML
	TableColumn<UITask, Integer> taskId;
	@FXML
	TableColumn<UITask, String> taskDescription;
	@FXML
	TableColumn<UITask, String> taskPriority;
	@FXML
	TableColumn<UITask, String> taskStart;
	@FXML
	TableColumn<UITask, String> taskEnd;
	@FXML
	TableColumn<UITask, String> taskDue;

	@FXML
	TableView<UITask> archiveTable;
	@FXML
	TableColumn<UITask, Integer> archiveId;
	@FXML
	TableColumn<UITask, String> archiveDescription;
	
	private int taskNumber = 1;
	private int archiveNumber = 1;
	
	CommandController commandController = new CommandController();
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static ArrayList<Task> archiveList = new ArrayList<Task>();
	
	final ObservableList<UITask> tasks = FXCollections.observableArrayList();
	
	final ObservableList<UITask> archive = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
	
		taskId.setCellValueFactory(new PropertyValueFactory<UITask, Integer>("id"));
		taskDescription.setCellValueFactory(new PropertyValueFactory<UITask, String>("description"));
		taskPriority.setCellValueFactory(new PropertyValueFactory<UITask, String>("priority"));
		taskStart.setCellValueFactory(new PropertyValueFactory<UITask, String>("start"));
		taskEnd.setCellValueFactory(new PropertyValueFactory<UITask, String>("end"));
		taskDue.setCellValueFactory(new PropertyValueFactory<UITask, String>("due"));
		taskTable.setItems(tasks);
		
		//possible null pointer exception here when list is empty
		taskList = commandController.initialiseTasks();
		addToTaskDisplay();
		
		archiveId.setCellValueFactory(new PropertyValueFactory<UITask, Integer>("id"));
		archiveDescription.setCellValueFactory(new PropertyValueFactory<UITask, String>("description"));
		archiveTable.setItems(archive);
		
		//possible null pointer exception here when list is empty
		archiveList = commandController.initialiseArchives();
		addToArchiveDisplay();
		
	}
	
	public void chooseDirectory(ActionEvent event){
		
		String directoryPath;
		
		System.out.println("Directory button clicked");
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(null);
		if (selectedDirectory == null){
			System.out.println("No directory selected");
		}
		else {
			directoryPath = selectedDirectory.getAbsolutePath();
			System.out.println(directoryPath);
		}
	}
	
	public void inputCommand(ActionEvent event){
		
		String input = commandField.getText();
		commandField.clear();
		
		String[] splitCommand = input.split(" ");
		String firstCommand = splitCommand[0];
		
		System.out.println("User input: " + input);
		String outputMessage = commandController.executeCommand(input);
		System.out.println(outputMessage);

		if (firstCommand.equals("-goto")){
			executeGoTo(splitCommand[1]);
		}
		else if (firstCommand.equals("-add")){
			
			System.out.println("Ading tasks in progress...");
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			
			System.out.println(taskList);			
		}
		else if (firstCommand.equals("-archive")){
			
			int deleteIndex = Integer.parseInt(splitCommand[1]);
			
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
			
		}
		else if (firstCommand.equals("-undo")){
			
			//parser.executeUndo();????? Maybe no need.
			
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
			
		}
		else if (firstCommand.equals("-delete")){
			
			int deleteIndex = Integer.parseInt(splitCommand[1]);
			
			//parser.executeDelete(taskList.get(deleteIndex-1).getId());????? Maybe no need.
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
			
		}
		else if (firstCommand.equals("-search")){
			
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
			
		}
		else if (firstCommand.equals("-change")){
			
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
			
		}
		else if (firstCommand.equals("-refresh")){
			
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
			
		}
		else if (firstCommand.equals("-exit")){
			
			
			
		}
		
	}
	
	private void addToTaskDisplay() {
		tasks.clear();
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		
		for(int i = 0; i < taskList.size(); i++){
			int id = i+1;
			String description = taskList.get(i).getDescription();
			String priority = Integer.toString(taskList.get(i).getPriority());
			if (priority.equals("-1")){
				priority = "-";
			}
			String start = "-";
			String end = "-";
			String due = "-";
			if (taskList.get(i).getStartTime() != null){
				//start = taskList.get(i).getStartTime().getDateRepresentation();
				start = tf.format(taskList.get(i).getStartTime());
			}
			if (taskList.get(i).getEndTime() != null){
				end = tf.format(taskList.get(i).getEndTime());
			}
			if (taskList.get(i).getDeadline() != null){
				//due = taskList.get(i).getDeadline().getDateRepresentation();
				due = df.format(taskList.get(i).getDeadline());
			}
			UITask entry = new UITask(id, description, priority, start, end, due);
			tasks.add(entry);
			System.out.println("added: " + id + " " + description + priority + start + end + due);
		}
	}
	
	private void addToArchiveDisplay() {
		archive.clear();
		for(int i = 0; i < archiveList.size(); i++){
			int id = i+1;
			String description = archiveList.get(i).getDescription();
			String priority = Integer.toString(archiveList.get(i).getPriority());
			String start = "";
			String end = "";
			String due = "";
			if (archiveList.get(i).getStartTime() != null){
				start = archiveList.get(i).getStartTime().getDateRepresentation();
			}
			if (archiveList.get(i).getEndTime() != null){
				end = archiveList.get(i).getEndTime().getDateRepresentation();
			}
			if (archiveList.get(i).getDeadline() != null){
				due = archiveList.get(i).getDeadline().getDateRepresentation();
			}
			UITask entry = new UITask(id, description, priority, start, end, due);
			archive.add(entry);
			System.out.println("Initialized: " + description + priority + start + end + due);
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