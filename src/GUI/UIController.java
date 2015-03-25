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
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import Common.Task;
import Parser.CommandController;
import Storage.StorageController;

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
	private Label outputMessageText;
	@FXML
	private Label motivationalQuote;
	
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
	TableView<UICommand> commandTable;
	@FXML
	TableColumn<UICommand, String> commandFunction;
	@FXML
	TableColumn<UICommand, String> commandCommand;

	@FXML
	TableView<UITask> archiveTable;
	@FXML
	TableColumn<UITask, Integer> archiveId;
	@FXML
	TableColumn<UITask, String> archiveDescription;
	
	private int taskNumber = 1;
	private int archiveNumber = 1;
	
	CommandController commandController = new CommandController();
	StorageController storageController = new StorageController();
	public static ArrayList<Task> taskList = new ArrayList<Task>();
	private static ArrayList<Task> archiveList = new ArrayList<Task>();
	
	final ObservableList<UITask> uiTaskList = FXCollections.observableArrayList();
	final ObservableList<UITask> uiArchiveList = FXCollections.observableArrayList();
	final ObservableList<UICommand> uiCommandList = FXCollections.observableArrayList();
	
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
		
		commandFunction.setCellValueFactory(new PropertyValueFactory<UICommand, String>("function"));
		commandCommand.setCellValueFactory(new PropertyValueFactory<UICommand, String>("command"));
		commandTable.setItems(uiCommandList);
		
		//initialize uiCommandList here
		uiCommandList.add(new UICommand("Go to a panel", "-goto <panel name>"));
		uiCommandList.add(new UICommand("Add a new task", "-add <task description> [option: -date|-priority|-tag|-recurring] <value>"));
		uiCommandList.add(new UICommand("Delete an entry", "-delete <taskID>"));
		uiCommandList.add(new UICommand("Archive a task", "-archive <taskID>"));
		uiCommandList.add(new UICommand("Change an entry", "-change [option: desc|date|priority] <taskID> <new value>"));
		uiCommandList.add(new UICommand("Add tags to task","-addtag <taskID><value>"));
		uiCommandList.add(new UICommand("Removing tags from a task", "-remove -tag <taskID><value>"));
		uiCommandList.add(new UICommand("Searching for tasks", "-search [option: desc|date|priority|tag|keywords] <value>"));
		uiCommandList.add(new UICommand("Undo last user input","-undo"));
		uiCommandList.add(new UICommand("Sort list", "-sort [either: date|priority|tag]"));
		uiCommandList.add(new UICommand("Default tables view", "-refresh"));
		uiCommandList.add(new UICommand("Change motto of the day", "-changemotto"));
		
		motivationalQuote.setText(storageController.getMotivationQuotes());
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
		
		if (firstCommand.equals("-goto")){
			executeGoTo(splitCommand[1]);
			message = "You are at the " + splitCommand[1] + " panel";
		}
		else if (firstCommand.equals("-exit")){
		    Stage stage = (Stage) commandField.getScene().getWindow();
		    stage.close();
		}
		else if (firstCommand.equals("-changemotto")){
			message = "New motto for the day!";
			String quote = storageController.getMotivationQuotes();
			motivationalQuote.setText(quote);
		}
		else{
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
		}
		
		outputMessageText.setText(message);
	}
	
	public void addToTaskDisplay() {
		uiTaskList.clear();
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		
		for(int i = 0; i < taskList.size(); i++){
			int id = i+1;
			
			String description = taskList.get(i).getDescription();
			
			if (taskList.get(i).isRecurrence()){
				description = description + "(R)";
			}
			
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
				if (taskList.get(i).getEndTime() == null){
					end = tf.format(taskList.get(i).getDeadline());
				}
				else{
					end = tf.format(taskList.get(i).getEndTime());
				}
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
	
	public void addToArchiveDisplay() {
		uiArchiveList.clear();
		for(int i = 0; i < archiveList.size(); i++){
			int id = i+1;
			
			String description = archiveList.get(i).getDescription();
			
			if (archiveList.get(i).isRecurrence()){
				description = description + "(Reccur)";
			}
			
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
