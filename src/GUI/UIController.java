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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
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
	private Text outputMessageText;
	
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
	TableColumn<UITask, String> taskTags;

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
	
	final ObservableList<UITask> uiTaskList = FXCollections.observableArrayList();
	
	final ObservableList<UITask> uiArchiveList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
	
		taskId.setCellValueFactory(new PropertyValueFactory<UITask, Integer>("id"));
		taskDescription.setCellValueFactory(new PropertyValueFactory<UITask, String>("description"));
		taskPriority.setCellValueFactory(new PropertyValueFactory<UITask, String>("priority"));
		taskStart.setCellValueFactory(new PropertyValueFactory<UITask, String>("start"));
		taskEnd.setCellValueFactory(new PropertyValueFactory<UITask, String>("end"));
		taskDue.setCellValueFactory(new PropertyValueFactory<UITask, String>("due"));
		taskTags.setCellValueFactory(new PropertyValueFactory<UITask, String>("tags"));
		taskTable.setItems(uiTaskList);
		
		taskList = commandController.initialiseTasks();
		addToTaskDisplay();
		
		archiveId.setCellValueFactory(new PropertyValueFactory<UITask, Integer>("id"));
		archiveDescription.setCellValueFactory(new PropertyValueFactory<UITask, String>("description"));
		archiveTable.setItems(uiArchiveList);
		
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
		
		String message = commandController.executeCommand(input);
		System.out.println("Output: " + message);
		outputMessageText = new Text();
		outputMessageText.setText(message);
		
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
						
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
			
		}
		else if (firstCommand.equals("-delete")){
						
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
		    Stage stage = (Stage) commandField.getScene().getWindow();
		    stage.close();
		}
		
	}
	
	private void addToTaskDisplay() {
		uiTaskList.clear();
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		
		for(int i = 0; i < taskList.size(); i++){
			int id = i+1;
			String description = taskList.get(i).getDescription();
			String priority = Integer.toString(taskList.get(i).getPriority());
			if (priority.equals("-1")){
				priority = "-";
			}
			String start;
			String end;
			String due;
			
			try{
				start = tf.format(taskList.get(i).getStartTime());
			}catch (NullPointerException e){
				start = "-";
			}
			try{
				end = tf.format(taskList.get(i).getEndTime());
			}catch (NullPointerException e){
				end = "-";
			}
			try{
				due = df.format(taskList.get(i).getDeadline());
			}catch (NullPointerException e){
				due = "-";
			}
			
			String tags = "";
			
			for(int j = 0; j < taskList.get(i).getTags().size(); j++){
				if (j != 0) {
					tags = tags + ", " + taskList.get(i).getTags().get(j);
				}
				else{
					tags = taskList.get(i).getTags().get(j);
				}
			}
			
			UITask entry = new UITask(id, description, priority, start, end, due, tags);
			uiTaskList.add(entry);
			System.out.println("added: " + id + " " + description + priority + start + end + due + tags);
		}
	}
	
	private void addToArchiveDisplay() {
		uiArchiveList.clear();
		for(int i = 0; i < archiveList.size(); i++){
			int id = i+1;
			String description = archiveList.get(i).getDescription();
			String priority = Integer.toString(archiveList.get(i).getPriority());
			String start;
			String end;
			String due;
			
			try{
				start = archiveList.get(i).getStartTime().getDateRepresentation();
			}catch (NullPointerException e){
				start = "-";
			}
			try{
				end = archiveList.get(i).getEndTime().getDateRepresentation();
			}catch (NullPointerException e){
				end = "-";
			}
			try{
				due = archiveList.get(i).getDeadline().getDateRepresentation();
			}catch (NullPointerException e){
				due = "-";
			}
			String tags = "";
			UITask entry = new UITask(id, description, priority, start, end, due, tags);
			uiArchiveList.add(entry);
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
