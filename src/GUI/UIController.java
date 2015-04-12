//@author A0110837L

package GUI;

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
//import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import Common.Task;
import Parser.CommandController;

public class UIController implements Initializable {

	private static final int NOT_FOUND = -1;
	private static final char CHAR_S = 's';
	private static final char CHAR_C = 'c';
	private static final char CHAR_T = 't';
	private static final char CHAR_G = 'g';
	private static final String NOT_APPLICABLE = "-";
	private static final String MESSAGE_SETTINGS_PANEL = "You are at the settings panel";
	private static final String MESSAGE_COMMANDS_PANEL = "You are at the commands panel";
	private static final String MESSAGE_TASK_PANEL = "You are at the task panel";
	private static final String MESSAGE_NEW_MOTTO = "New motto for the day!";
	private static final String STRING_CHANGEMOTTO = "changemotto";
	private static final String STRING_EXIT = "exit";
	private static final String STRING_FROM = "from ";
	private static final String STRING_IMPORT = "import";
	private static final String STRING_AT = "at: ";
	private static final String STRING_DIRECTORY = "directory";
	private static final String STRING_SHORTCUTS = "shortcuts";
	private static final String STRING_COMMAND = "command";
	private static final String STRING_FUNCTION = "function";
	private static final String STRING_TAGS = "tags";
	private static final String STRING_DUE = "due";
	private static final String STRING_END = "end";
	private static final String STRING_START = "start";
	private static final String STRING_PRIORITY = "priority";
	private static final String STRING_DESCRIPTION = "description";
	private static final String STRING_ID = "id";
	private static final String STRING_HH_MM = "HH:mm";
	private static final String STRING_DD_MM_YYYY = "dd/MM/YYYY";
	private static final String STRING_DAYS_OVERDUE = " days overdue";
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
	private Label fileDirectory;
	@FXML
	private Label storageFileName;

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
	TableColumn<UICommand, String> commandShortcuts;

	@FXML
	TableView<UITask> archiveTable;
	@FXML
	TableColumn<UITask, Integer> archiveId;
	@FXML
	TableColumn<UITask, String> archiveDescription;

	private CommandController commandController = new CommandController();
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static ArrayList<Task> archiveList = new ArrayList<Task>();

	final ObservableList<UITask> uiTaskList = FXCollections.observableArrayList();
	final ObservableList<UITask> uiArchiveList = FXCollections.observableArrayList();
	final ObservableList<UICommand> uiCommandList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources){

		initialiseTasks();

		initialiseArchive();

		initialiseCommand();

		motivationalQuote.setText(commandController.getRandomQuotes());

		fileDirectory.setText(commandController.getFileDirectory());

		storageFileName.setText(commandController.getFileName());

	}
	
	public void inputCommand(ActionEvent event) {

		String input = commandField.getText();
		commandField.clear();

		String[] splitCommand = input.split(" ");
		String firstCommand = splitCommand[0];

		String message = commandController.executeCommand(input);

		message = commandFilter(splitCommand, firstCommand, message);

		outputMessageText.setText(message);
	}

	private void initialiseCommand() {
		commandFunction.setCellValueFactory(new PropertyValueFactory<UICommand, String>(STRING_FUNCTION));
		commandCommand.setCellValueFactory(new PropertyValueFactory<UICommand, String>(STRING_COMMAND));
		commandShortcuts.setCellValueFactory(new PropertyValueFactory<UICommand, String>(STRING_SHORTCUTS));
		commandTable.setItems(uiCommandList);
		uiCommandList.add(new UICommand("Add a new task", "add <task description> [option: -date|-priority|-tag|-recurring] <value>", "add <task description> -d|p|t|r <value>"));
		uiCommandList.add(new UICommand("Add tags to task","addtag <taskID> <value>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Archive a task", "archive <taskID>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Change an entry", "change <taskID> [option: desc|priority] <new value>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Change a deadline", "change <taskID> date <new date> at <new time>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Change a meeting", "change <taskID> date <new date> start <new start> end <new end>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Change a recurring entry", "change all <taskID> [option: desc|priority] <new value>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Change a recurring deadline", "change all <taskID> date <new time>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Change a recurring meeting", "change all <taskID> date start <new start> end <new end>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Change directory", "directory <folder>|<absolute path of the folder>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Change motto of the day", STRING_CHANGEMOTTO, NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Default tables view", "refresh", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Delete an entry", "delete current|archive <taskID>", "delete <taskID>, delete archive <taskID>"));
		uiCommandList.add(new UICommand("Exit program", STRING_EXIT, NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Export existing storage file", "export to <absolute path of the file>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Go to a panel", "goto <panel name>", "g t/c/s"));
		uiCommandList.add(new UICommand("Import from existing storage file", "import from <absolute path of the file>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Removing tags from a task", "removetag <taskID> <value>", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Searching for tasks", "search [option: desc|date|priority|tag] <value> or search today|tmr", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Sort list", "sort [either: date|priority|tag]", NOT_APPLICABLE));
		uiCommandList.add(new UICommand("Undo last user input","undo", NOT_APPLICABLE));

	}

	private void initialiseArchive() {
		archiveId.setCellValueFactory(new PropertyValueFactory<UITask, Integer>(STRING_ID));
		archiveDescription.setCellValueFactory(new PropertyValueFactory<UITask, String>(STRING_DESCRIPTION));
		archiveTable.setItems(uiArchiveList);
		archiveList = commandController.initialiseArchives();
		addToArchiveDisplay();
	}

	private void initialiseTasks() {
		taskId.setCellValueFactory(new PropertyValueFactory<UITask, Integer>(STRING_ID));
		taskDescription.setCellValueFactory(new PropertyValueFactory<UITask, String>(STRING_DESCRIPTION));
		taskPriority.setCellValueFactory(new PropertyValueFactory<UITask, String>(STRING_PRIORITY));
		taskStart.setCellValueFactory(new PropertyValueFactory<UITask, String>(STRING_START));
		taskEnd.setCellValueFactory(new PropertyValueFactory<UITask, String>(STRING_END));
		taskDue.setCellValueFactory(new PropertyValueFactory<UITask, String>(STRING_DUE));
		taskTags.setCellValueFactory(new PropertyValueFactory<UITask, String>(STRING_TAGS));
		taskTable.setItems(uiTaskList);
		taskList = commandController.initialiseTasks();
		addToTaskDisplay();
	}

	private String commandFilter(String[] splitCommand, String firstCommand,
			String message) {
		if (firstCommand.charAt(0) == CHAR_G) {
			executeGoTo(splitCommand[1]);
			message = displayGotoMessage(splitCommand, message);
		} else if (firstCommand.equals(STRING_DIRECTORY)) {
			displayDirectory(message);
		} else if (firstCommand.equals(STRING_IMPORT)) {
			displayImport(message);
		} else if (firstCommand.equals(STRING_EXIT)) {
			executeExit();
		} else if (firstCommand.equals(STRING_CHANGEMOTTO)) {
			message = displayMotivationalQuote();
		} else {
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
		}
		return message;
	}

	private String displayMotivationalQuote() {
		String message;
		message = MESSAGE_NEW_MOTTO;
		String quote = commandController.getRandomQuotes();
		motivationalQuote.setText(quote);
		return message;
	}

	private void displayDirectory(String message) {
		String[] splitMessage = message.split(STRING_AT);
		fileDirectory.setText(splitMessage[1]);
		storageFileName.setText(commandController.getFileName());
	}

	private void displayImport(String message) {
		if (message.contains(STRING_FROM)) {
			String[] splitMessage = message.split(STRING_FROM);
			taskList = commandController.returnTasks();
			addToTaskDisplay();
			archiveList = commandController.returnArchive();
			addToArchiveDisplay();
			fileDirectory.setText(commandController.getFileDirectory());
			storageFileName.setText(commandController.getFileName());
		}
	}

	private String displayGotoMessage(String[] splitCommand, String message) {
		if (splitCommand[1].charAt(0) == CHAR_T) {
			message = MESSAGE_TASK_PANEL;
		} else if (splitCommand[1].charAt(0) == CHAR_C) {
			message = MESSAGE_COMMANDS_PANEL;
		} else if (splitCommand[1].charAt(0) == CHAR_S) {
			message = MESSAGE_SETTINGS_PANEL;
		}
		return message;
	}
	
	private void executeExit() {
		Stage stage = (Stage) commandField.getScene().getWindow();
		stage.close();
	}
	
	private void executeGoTo(String input) {
		if (input.charAt(0) == CHAR_T) {
			taskGroup.toFront();
		} else if (input.charAt(0) == CHAR_C) {
			helpGroup.toFront();
		} else if (input.charAt(0) == CHAR_S) {
			settingsGroup.toFront();
		}
	}

	private void addToTaskDisplay() {
		uiTaskList.clear();

		SimpleDateFormat df = new SimpleDateFormat(STRING_DD_MM_YYYY);
		SimpleDateFormat tf = new SimpleDateFormat(STRING_HH_MM);

		for (int i = 0; i < taskList.size(); i++) {
			int id = i + 1;

			String description = getDescription(i);

			String priority = getPriority(i);

			String start = getStart(tf, i);
			String end = getEnd(tf, i);
			String due = getDue(df, i);

			String tags = "";

			tags = getTags(i, tags);

			UITask entry = new UITask(id, description, priority, start, end, due, tags);
			uiTaskList.add(entry);
		}
	}
	
	private void addToArchiveDisplay() {
		uiArchiveList.clear();
		for (int i = 0; i < archiveList.size(); i++) {
			int id = i + 1;

			String description = archiveList.get(i).getDescription();

			String priority = Integer.toString(archiveList.get(i).getPriority());
			String start = null;
			String end = null;
			String due = null;
			String tags = null;
			UITask entry = new UITask(id, description, priority, start, end, due, tags);
			uiArchiveList.add(entry);
		}
	}

	private String getTags(int i, String tags) {
		for (int j = 0; j < taskList.get(i).getTags().size(); j++) {
			if (j != 0) {
				tags = tags + ", " + taskList.get(i).getTags().get(j);
			} else {
				tags = taskList.get(i).getTags().get(j);
			}
		}
		return tags;
	}

	private String getDue(SimpleDateFormat df, int i) {
		String due;
		try {
			if (taskList.get(i).getDeadline() == null) {
				if (taskList.get(i).getOverdueDays() == NOT_FOUND) {
					due = df.format(taskList.get(i).getStartTime());
				} else {
					due = Integer.toString(taskList.get(i).getOverdueDays()) + STRING_DAYS_OVERDUE;
				}
			} else {
				if (taskList.get(i).getOverdueDays() == NOT_FOUND) {
					due = df.format(taskList.get(i).getDeadline());
				} else {
					due = Integer.toString(taskList.get(i).getOverdueDays()) + STRING_DAYS_OVERDUE;
				}
			}
		} catch (NullPointerException e) {
			due = NOT_APPLICABLE;
		}
		return due;
	}

	private String getEnd(SimpleDateFormat tf, int i) {
		String end;
		try {
			if (taskList.get(i).getEndTime() == null) {
				end = tf.format(taskList.get(i).getDeadline());
			} else {
				end = tf.format(taskList.get(i).getEndTime());
			}
		} catch (NullPointerException e) {
			end = NOT_APPLICABLE;
		}
		return end;
	}

	private String getStart(SimpleDateFormat tf, int i) {
		String start;
		try {
			start = tf.format(taskList.get(i).getStartTime());
		} catch (NullPointerException e){
			start = NOT_APPLICABLE;
		}
		return start;
	}

	private String getDescription(int i) {
		String description = taskList.get(i).getDescription();
		if (taskList.get(i).isRecurrence() == true){
			description = description + " [R]";
		}
		return description;
	}

	private String getPriority(int i) {
		String priority = Integer.toString(taskList.get(i).getPriority());
		if (priority.equals("-1")) {
			priority = NOT_APPLICABLE;
		}
		return priority;
	}

}
