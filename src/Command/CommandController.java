// @author A0110571W
package Command;

import java.text.SimpleDateFormat;
import java.util.*;

import Common.*;
import Common.Date;
import Logic.LogicController;
import Storage.StorageDirectory;
import Motivator.Motivator;

public class CommandController implements InterfaceForParser {

	private static LogicController logicController = new LogicController();
	private static StorageDirectory storageDirectory = new StorageDirectory();
	private static Motivator motivator = new Motivator();
	private static Parser parser = new Parser();
	private int newMaxID = 0;
	private ArrayList<Task> currentActiveTasks = new ArrayList<Task>();
	private ArrayList<Task> currentArchives = new ArrayList<Task>();
	private static final String DEADLINE_FORMAT_ERROR = "Wrong format for deadline.";
	private static final String MEETINGTIME_FORMAT_ERROR = "Wrong format for timing.";
	private static final String DATE_FORMAT_ERROR = "Wrong format for date.";
	private static final String INVALID_COMMAND = "Invalid command";
	private static final String BY_KEYWORD = "-by";
	private static final String BY_SHORTCUT = "-b";
	private static final String ON_KEYWORD = "-on";
	private static final String ON_SHORTCUT = "-o";
	private static final String AT_KEYWORD = "at";
	private static final String FROM_KEYWORD = "from";
	private static final String TO_KEYWORD = "to";
	private static final String DATE_KEYWORD = "date";
	private static final String TIME_KEYWORD = "time";
	private static final String TIME_SHORTCUT = "t";
	private static final String DESC_KEYWORD = "desc";
	private static final String PRIORITY = "priority";
	private static final String PRIORITY_SHORTCUT = "p";
	private static final String PRIORITY_KEYWORD = "-priority";
	private static final String PRIORITY_KEYWORD_SHORTCUT = "-p";
	private static final String ALL_KEYWORD = "all";
	private static final String START_KEYWORD = "start";
	private static final String END_KEYWORD = "end";
	private static final String GENERIC_TYPE = "generic";
	private static final String DEADLINE_TYPE = "deadline";
	private static final String MEETING_TYPE = "meeting";
	private static final String CURRENT_KEYWORD = "current";
	private static final String ARCHIVE_KEYWORD = "archive";
	private static final String TODAY_KEYWORD = "today";
	private static final String TMR_KEYWORD = "tmr";
	private static final String TAG = "tag";
	private static final String TAG_KEYWORD = "-tag";
	private static final String TAG_SHORTCUT = "-t";
	private static final String RECURRING_KEYWORD = "-recurring";
	private static final String RECURRING_SHORTCUT = "-r";
	private static final String WEEKLY = "weekly";
	private static final String HOURLY = "hourly";
	private static final String DAILY = "daily";
	private static final String MONTHLY = "monthly";
	private static final String YEARLY = "yearly";
	
	public static void main(String[] args) {

		CommandController test = new CommandController();
		test.initialiseTasks();

	}

	public ArrayList<Task> initialiseTasks() {
		logicController.initialise();
		newMaxID = logicController.getSerialNumber();
		// Upon application start-up, fetch the current tasklist
		ToDoSortedList retrievedCurrent = new ToDoSortedList();

		try {
			retrievedCurrent = logicController.viewActiveTasks();
			reflectChangeToCurrent(retrievedCurrent);
		} catch (NullPointerException e) {
			currentActiveTasks.clear();
			return currentActiveTasks;
		}

		return currentActiveTasks;

	}

	public ArrayList<Task> initialiseArchives() {
		// Upon application start-up, fetch the archived tasks to display
		ArchiveSortedList retrievedArchiveFromLogic = new ArchiveSortedList();

		currentArchives.clear();
		try {
			retrievedArchiveFromLogic = logicController.viewArchiveTasks();
			reflectChangeToArchives(retrievedArchiveFromLogic);
		} catch (NullPointerException e) {
			return currentArchives;
		}
		return currentArchives;
	}

	public ArrayList<Task> returnTasks() {
		return currentActiveTasks;
	}

	public ArrayList<Task> returnArchive() {

		ArchiveSortedList retrievedArchiveFromLogic = new ArchiveSortedList();
		try {
			retrievedArchiveFromLogic = logicController.viewArchiveTasks();
			currentArchives.clear();
			reflectChangeToArchives(retrievedArchiveFromLogic);
		} catch (NullPointerException e) {
			return currentArchives;
		}
		return currentArchives;
	}


	public String getRandomQuotes() {
		return motivator.getRandomQuotes();
	}

	public String getFileDirectory() {
		return storageDirectory.getFileDirectory();
	}

	public String getFileName() {
		return storageDirectory.getFileName();
	}

	public String executeCommand(String inputCommand) {
		int commandNum = parser.parseIn(inputCommand);
		String[] splitInput = inputCommand.split(" ");
		String result = new String();
		switch (commandNum) {
		case (0): 
			result = INVALID_COMMAND;
			break;
		case (1): 
			result = addCommand(splitInput);
			break;
		case (2):
			result = searchCommand(splitInput);
			break;
		case (3):
			result = deleteCommand(splitInput);
			break;
		case (4):
			result = archiveCommand(splitInput);
			break;
		case (5): 
			result = exitCommand();
			break;
		case (6): 
			result = modifyCommand(splitInput);
			break;
		case (7): 
			result = fileDirectoryCommand(splitInput);
			break;
		case (8): 
			result = refreshCommand();
			break;
		case (9): 
			result = undoCommand();
			break;
		case (10):
			result = addtagCommand(splitInput);
			break;
		case (11):
			result = removetagCommand(splitInput);
			break;
		case (12):
			result = sortCommand(splitInput);
			break;
		case (13):
			result = importCommand(splitInput);
			break;
		case (14):
			result = exportCommand(splitInput);
			break;
		case (15):
			result = redoCommand();
			break;
		case (16):
			result = unarchiveCommand(splitInput);
			break;
		default:
			result = INVALID_COMMAND;
		}
		return result;
	}

	private String unarchiveCommand(String[] splitInput) {
		// syntax unarchive <taskID>
		String result = new String();
		int positionOfTaskID = 1;
		ToDoSortedList retrievedActiveTaskList = new ToDoSortedList();
		int taskIDFromUI = Integer.parseInt(splitInput[positionOfTaskID]);
		int actualTaskID = taskIDFromUI-1;
		try {
			Task taskToUnArchive = currentArchives.get(actualTaskID);
			retrievedActiveTaskList = logicController.unArchive(taskToUnArchive);
			result = "Task moved back from archive: " + taskToUnArchive.getDescription();
		} catch (Exception e) {
			result = "Cannot unarchive task";
		}
		reflectChangeToCurrent(retrievedActiveTaskList);
		return result;
	}

	private String redoCommand() {
		boolean isSuccessful = logicController.redo();
		refreshCurrent();
		refreshArchive();
		if (isSuccessful) {
			return "Redo successful";
		} else {
			return "Redo unsuccessful";
		}

	}

	private String exportCommand(String[] splitInput) {
		String directoryToExport = new String();
		int positionOfTo = 1;
		int positionOfParameter = 2;
		int lengthForOneParameter = 3;
		if (splitInput[positionOfTo].equals(TO_KEYWORD)) {
			// execution
			try {
				directoryToExport = splitInput[positionOfParameter];
				if (splitInput.length > lengthForOneParameter) {
					for (int i = lengthForOneParameter; i < splitInput.length; i++) {
						directoryToExport = directoryToExport.concat(" ").concat(splitInput[i]);
					}
				}
				if (storageDirectory.exportToDirectory(directoryToExport) == true) {
					return "Current data exported to " + directoryToExport;
				} else {
					return "Fail to export to " + directoryToExport;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return "Please specify a file path to export to";
			}
		} else {
			return "Invalid command, try \"export to\"";
		}
	}

	private String importCommand(String[] splitInput) {
		String directoryToImport = new String();
		int positionOfFrom = 1;
		int positionOfParameter = 2;
		int lengthForOneParameter = 3;
		if (splitInput[positionOfFrom].equals(FROM_KEYWORD)) {
			try {
				directoryToImport = splitInput[positionOfParameter];
				if (splitInput.length > lengthForOneParameter) {
					for (int i = lengthForOneParameter; i < splitInput.length; i++) {
						directoryToImport = directoryToImport.concat(" ").concat(splitInput[i]);
					}
				}
				if (storageDirectory.importFromDirectory(directoryToImport) == true) {
					String relativeDirectory = storageDirectory.getFileDirectory();
					//reinitialise tasks from imported file
					initialiseTasks();
					initialiseArchives();
					return "Imported from " + relativeDirectory;
				} else {
					return "Fail to import file. File invalid.";
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return "Please specify a directory to import from";
			}
		} else {
			return "Invalid command, try \"import from \"";
		}

	}

	private String sortCommand(String[] splitInput) {
		String result = new String();
		String sortType = new String();
		int positionOfParameter = 1;
		try {
			sortType = splitInput[positionOfParameter];
			if (sortType.equalsIgnoreCase(TIME_KEYWORD) | sortType.equalsIgnoreCase(TIME_SHORTCUT)) {
				ToDoSortedList retrievedSortedList = logicController.sortByTime();
				reflectChangeToCurrent(retrievedSortedList);
				result = "Tasks sorted by time";
			} else if (sortType.equalsIgnoreCase(PRIORITY) | sortType.equalsIgnoreCase(PRIORITY_SHORTCUT)) {
				PrioritySortedList retrievedSortedList = logicController.sortByPriority();
				currentActiveTasks.clear();
				reflectChangeToCurrent(retrievedSortedList);
				result = "Tasks sorted by priority";
			} else {
				return result = "Invalid, can sort by time or priority only";
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			return result = "Please specify what to sort by";
		}
		return result;
	}

	private String removetagCommand(String[] splitInput) {
		String result = new String();
		int taskID = 0;
		String tagToRemove = new String();
		int positionOfFirstParameter = 1;
		int positionOfSecondParameter = positionOfFirstParameter+1;
		boolean isChangeAll = false;
		ToDoSortedList retrievedSortedList = new ToDoSortedList();
		if (splitInput[positionOfFirstParameter].equals(ALL_KEYWORD)) {
			positionOfFirstParameter = 2;
			positionOfSecondParameter = positionOfFirstParameter+1;
			isChangeAll = true;
		}
		try {
			taskID = Integer.parseInt(splitInput[positionOfFirstParameter]);
			tagToRemove = splitInput[positionOfSecondParameter];
			if (currentActiveTasks.size() < taskID) {
				return result = "Task does not exist";
			} else if (!currentActiveTasks.get(taskID - 1).getTags().contains(tagToRemove)) {
				return result = "Tag does not exist";
			} else {
				Task taskToChange = currentActiveTasks.get(taskID - 1);
				if (isChangeAll) {
					if (taskToChange.isRecurrence()) {
						retrievedSortedList = logicController.removeAlltag(taskToChange, tagToRemove);
					} else {
						return result = "Cannot change all, not recurring task";
					}
				} else {
					retrievedSortedList = logicController.removeTag(taskToChange, tagToRemove);
				}
				reflectChangeToCurrent(retrievedSortedList);
			}
		} catch (Exception e) {
			return "Error in removing tag";
		}
		return result = "Tag \"" + tagToRemove + "\" removed from task";

	}

	private String addtagCommand(String[] splitInput) {
		String result = new String();
		int taskID = 0;
		String tagToRemove = new String();
		int positionOfFirstParameter = 1;
		int positionOfSecondParameter = positionOfFirstParameter+1;
		boolean isChangeAll = false;
		ToDoSortedList retrievedSortedList = new ToDoSortedList();
		if (splitInput[positionOfFirstParameter].equals(ALL_KEYWORD)) {
			positionOfFirstParameter = 2;
			positionOfSecondParameter = positionOfFirstParameter+1;
			isChangeAll = true;
		}
		String tagToAdd = new String();
		try {
			taskID = Integer.parseInt(splitInput[positionOfFirstParameter]);
			tagToAdd = splitInput[positionOfSecondParameter];
			if (currentActiveTasks.size() < taskID) {
				return result = "Task does not exist";
			} else if (currentActiveTasks.get(taskID - 1).getTags().contains(tagToAdd)) {
				return result = "Tag already exists";
			} else {
				Task taskToChange = currentActiveTasks.get(taskID - 1);
				if (isChangeAll) {
					if (taskToChange.isRecurrence()) {
						retrievedSortedList = logicController.addAlltag(taskToChange, tagToAdd);
					} else {
						return result = "Cannot change all, not recurring task";
					}
				} else {
					retrievedSortedList = logicController.addTag(taskToChange,tagToAdd);
				}
				reflectChangeToCurrent(retrievedSortedList);
			}
		} catch (Exception e) {
			return "Error in adding tag";
		}
		return result = "Tag \"" + tagToAdd + "\" added to task";

	}

	private String undoCommand() {
		boolean isSuccessful = logicController.undo();
		refreshCurrent();
		refreshArchive();
		String result = new String();
		if (isSuccessful) {
			return result = "Undo successful";
		} else {
			return result = "Undo unsuccessful, nothing to undo";
		}

	}

	private void refreshArchive() {
		currentArchives.clear();
		ArchiveSortedList retrievedArchiveList = logicController.viewArchiveTasks();
		reflectChangeToArchives(retrievedArchiveList);
	}

	private void refreshCurrent() {
		currentActiveTasks.clear();
		ToDoSortedList retrievedSortedList = logicController.viewActiveTasks();
		reflectChangeToCurrent(retrievedSortedList);
	}

	private String refreshCommand() {
		String result = "Display refreshed to current tasks";

		try {
			refreshCurrent();
		} catch (NullPointerException e) {
			return result = "No active tasks to display";
		}

		return result;
	}

	private String fileDirectoryCommand(String[] splitInput) {
		String result = new String();
		int positionOfParameter = 1;
		int lengthForOneParameter = 2;
		String tempPath = splitInput[positionOfParameter];
		if (splitInput.length > lengthForOneParameter) {
			for (int i = lengthForOneParameter; i < splitInput.length; i++) {
				tempPath = tempPath.concat(" ").concat(splitInput[i]);
			}
		}
		String specifiedFileDirectory = tempPath;
		String acknowledgeCheck = storageDirectory.changeFileDirectory(specifiedFileDirectory);
		result = "File stored at: " + storageDirectory.getFileDirectory();
		return result;
	}

	private String modifyCommand(String[] splitInput) {
		// for recurring changes, change all <taskID> <..parameters>
		String result = new String();
		String modifyParameter = new String();
		ToDoSortedList retrievedSortedList = new ToDoSortedList();
		ArrayList<String> inputArray = new ArrayList<String>();
		boolean isChangeAll = false;
		int position = 0;
		for (int i = 0; i < splitInput.length; i++) {
			inputArray.add(splitInput[i]);
		}
		if (inputArray.contains(ALL_KEYWORD)) {
			isChangeAll = true;
			position = inputArray.indexOf(ALL_KEYWORD);
		}
		int taskID = 0;
		try {
			taskID = Integer.parseInt(inputArray.get(position + 1)) - 1;
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			return result = "Please specify task ID to change first.";
		}
		try {
			modifyParameter = inputArray.get(position + 2);
		} catch (ArrayIndexOutOfBoundsException e) {
			return result = "Please specify what to change";
		}

		Task taskToChange = currentActiveTasks.get(taskID);
		String taskType = taskToChange.getType();
		switch (modifyParameter) {
		case (TIME_KEYWORD): {
			ArrayList<String> dateAsString = new ArrayList<String>();
			for (int i = inputArray.indexOf(TIME_KEYWORD) + 1; i < inputArray.size(); i++) {
				dateAsString.add(inputArray.get(i));
			}
			if (isChangeAll) {
				if (taskType.equals(GENERIC_TYPE)) {
					return "Error, no time to change";
				} else if (taskType.equals(DEADLINE_TYPE)) {
					try {
						int positionOfTime = dateAsString.indexOf(TIME_KEYWORD) + 1;
						String timeAsString = dateAsString.get(positionOfTime);
						int positionOfMinuteMark = dateAsString.get(positionOfTime).indexOf(":");
						String hourAsString = timeAsString.substring(0,positionOfMinuteMark);
						String minutesAsString = timeAsString.substring(positionOfMinuteMark + 1,positionOfMinuteMark + 3);
						int newHour = Integer.parseInt(hourAsString);
						int newMinutes = Integer.parseInt(minutesAsString);
						retrievedSortedList = logicController.editAlldeadlineTime(taskToChange, newHour,newMinutes);
						reflectChangeToCurrent(retrievedSortedList);
						return "Time changed";
					} catch (NumberFormatException e) {
						return "Wrong format for time";
					}
				} else if (taskType.equals(MEETING_TYPE)) {
					if (dateAsString.contains(START_KEYWORD) && dateAsString.contains(END_KEYWORD)) {
						int positionOfTime = dateAsString.indexOf(START_KEYWORD) + 1;
						int positionOfMinuteMark = dateAsString.get(positionOfTime).indexOf(":");
						String timeAsString = dateAsString.get(positionOfTime);
						String hourAsString = timeAsString.substring(0,positionOfMinuteMark);
						String minutesAsString = timeAsString.substring(positionOfMinuteMark + 1,positionOfMinuteMark + 3);
						int newStartHour = Integer.parseInt(hourAsString);
						int newStartMinutes = Integer.parseInt(minutesAsString);
						positionOfTime = dateAsString.indexOf(END_KEYWORD) + 1;
						positionOfMinuteMark = dateAsString.get(positionOfTime).indexOf(":");
						timeAsString = dateAsString.get(positionOfTime);
						hourAsString = timeAsString.substring(0,positionOfMinuteMark);
						minutesAsString = timeAsString.substring(positionOfMinuteMark + 1,positionOfMinuteMark + 3);
						int newEndHour = Integer.parseInt(hourAsString);
						int newEndMinutes = Integer.parseInt(minutesAsString);
						retrievedSortedList = logicController.editAllStartTime(taskToChange, newStartHour, newStartMinutes);
						retrievedSortedList = logicController.editAllEndTime(taskToChange, newEndHour, newEndMinutes);
						reflectChangeToCurrent(retrievedSortedList);
						return "Start and end time changed";
					} else if (dateAsString.contains(END_KEYWORD)) {
						int positionOfTime = dateAsString.indexOf(END_KEYWORD) + 1;
						int positionOfMinuteMark = dateAsString.get(positionOfTime).indexOf(":");
						String timeAsString = dateAsString.get(positionOfTime);
						String hourAsString = timeAsString.substring(0,positionOfMinuteMark);
						String minutesAsString = timeAsString.substring(positionOfMinuteMark + 1,positionOfMinuteMark + 3);
						int newHour = Integer.parseInt(hourAsString);
						int newMinutes = Integer.parseInt(minutesAsString);
						retrievedSortedList = logicController.editAllEndTime(taskToChange, newHour, newMinutes);
						reflectChangeToCurrent(retrievedSortedList);
						return "End time changed";
					} else if (dateAsString.contains(START_KEYWORD)) {
						int positionOfTime = dateAsString.indexOf(START_KEYWORD) + 1;
						int positionOfMinuteMark = dateAsString.get(positionOfTime).indexOf(":");
						String timeAsString = dateAsString.get(positionOfTime);
						String hourAsString = timeAsString.substring(0,positionOfMinuteMark);
						String minutesAsString = timeAsString.substring(positionOfMinuteMark + 1,positionOfMinuteMark + 3);
						int newHour = Integer.parseInt(hourAsString);
						int newMinutes = Integer.parseInt(minutesAsString);
						retrievedSortedList = logicController.editAllStartTime(taskToChange, newHour, newMinutes);
						reflectChangeToCurrent(retrievedSortedList);
						return "Start time changed";
					}
				}
			} else {
				return "Error, only for recurring tasks";
			}

			break;
		}
		case (DATE_KEYWORD): {
			// find task ID, get task
			String dateInput = new String();
			ArrayList<String> dateAsString = new ArrayList<String>();
			for (int i = inputArray.indexOf(DATE_KEYWORD) + 1; i < inputArray.size(); i++) {
				dateAsString.add(inputArray.get(i));
			}
			// cases: generic task, deadline task, meeting task
			if (taskType.equalsIgnoreCase(GENERIC_TYPE)) {
				// i.e no date exists
				// parse in the new deadline/startend time

				if (dateAsString.contains(FROM_KEYWORD) && (dateAsString.contains(TO_KEYWORD))) {
					// if change to meeting task
					String positionCheck = FROM_KEYWORD;
					Date newStartTime = determineMeetingTime(positionCheck,dateAsString);
					positionCheck = TO_KEYWORD;
					Date newEndTime = determineMeetingTime(positionCheck,dateAsString);
					if (newStartTime == null | newEndTime == null) {
						return MEETINGTIME_FORMAT_ERROR;
					} else {
						retrievedSortedList = logicController.addStartAndEndTime(taskToChange, newStartTime,newEndTime);
						reflectChangeToCurrent(retrievedSortedList);
					}

				} else {
					// if change to deadline task
					// syntax: -change date dd/MM/yyyy

					Date newDeadline = determineDeadline(dateAsString);
					if (newDeadline == null) {
						return DEADLINE_FORMAT_ERROR;
					} else {
						retrievedSortedList = logicController.addDeadLine(taskToChange, newDeadline);
						reflectChangeToCurrent(retrievedSortedList);
					}
					return result = "Deadline changed";
				}
			} else if (taskType.equalsIgnoreCase(DEADLINE_TYPE)) {
				// date exists, change deadline
				// syntax: -change date dd/MM/yyyy
				Date newDeadline = determineDeadline(dateAsString);
				if (newDeadline == null) {
					return DEADLINE_FORMAT_ERROR;
				} else {
					retrievedSortedList = logicController.editDeadline(taskToChange, newDeadline);
					reflectChangeToCurrent(retrievedSortedList);
				}

				return result = "Deadline changed";

			} else if (taskType.equalsIgnoreCase(MEETING_TYPE)) {

				if (dateAsString.contains(START_KEYWORD) && dateAsString.contains(END_KEYWORD)) {
					// change start and end time
					// syntax:-change date dd/MM/yyyy HHmm HHmm
					String positionCheck = START_KEYWORD;
					Date newStartTime = determineMeetingTime(positionCheck,dateAsString);
					positionCheck = END_KEYWORD;
					Date newEndTime = determineMeetingTime(positionCheck,dateAsString);
					if (newStartTime == null | newEndTime == null) {
						return MEETINGTIME_FORMAT_ERROR;
					} else {
						retrievedSortedList = logicController.editStartTime(taskToChange, newStartTime);
						retrievedSortedList = logicController.editEndTime(taskToChange, newEndTime);
						reflectChangeToCurrent(retrievedSortedList);
					}
					return result = "Meeting start and end time changed";
				} else if (dateAsString.contains(START_KEYWORD)) {
					String positionCheck = START_KEYWORD;
					Date newStartTime = determineMeetingTime(positionCheck,dateAsString);
					if (newStartTime == null) {
						return DATE_FORMAT_ERROR;
					} else {
						retrievedSortedList = logicController.editStartTime(taskToChange, newStartTime);
						reflectChangeToCurrent(retrievedSortedList);
						return result = "Meeting start time changed";
					}
				} else if (dateAsString.contains(END_KEYWORD)) {
					String positionCheck = END_KEYWORD;
					Date newEndTime = determineMeetingTime(positionCheck,
							dateAsString);
					if (newEndTime == null) {
						return DATE_FORMAT_ERROR;
					} else {
						retrievedSortedList = logicController.editEndTime(taskToChange, newEndTime);
						reflectChangeToCurrent(retrievedSortedList);
						return result = "Meeting end time changed";
					}
				}

			} else {
				return result = "Type mismatch error, can't change date";
			}
			break;
		}
		case (PRIORITY): {
			// syntax: -change priority <taskID> <new priority>
			int newPriority = Integer.parseInt(inputArray.get(position + 3));
			if (isChangeAll) {
				if (taskToChange.isRecurrence()) {
					retrievedSortedList = logicController.editAllPriority(taskToChange, newPriority);
				} else {
					return result = "Cannot change all, not recurring task";
				}
			} else {
				retrievedSortedList = logicController.editPriority(taskToChange, newPriority);
			}
			// retrievedSortedList = logicController.editPriority(taskToChange,
			// newPriority);
			result = "Priority changed";
			break;
		}
		case (DESC_KEYWORD): {
			// syntax: -change desc <taskID> <new desc>
			// String newDescription = splitInput[3];
			int descStartPoint = position + 3;
			ArrayList<String> newDescription = new ArrayList<String>();
			for (int i = descStartPoint; i < inputArray.size(); i++) {
				newDescription.add(inputArray.get(i));
			}
			String newDescriptionString = new String();
			for (int i = 0; i < newDescription.size(); i++) {
				newDescriptionString = newDescriptionString.concat(newDescription.get(i) + " ");
			}
			newDescriptionString = newDescriptionString.trim();
			if (isChangeAll) {
				if (taskToChange.isRecurrence()) {
					retrievedSortedList = logicController.editAllDescription(taskToChange, newDescriptionString);
				} else {
					return result = "Cannot change all, not recurring task";
				}
			} else {
				retrievedSortedList = logicController.editDescription(taskToChange, newDescriptionString);
			}
			// retrievedSortedList =
			// logicController.editDescription(taskToChange,
			// newDescriptionString);
			result = "Description changed";
			break;
		}
		default: {
			return result = "Value to change does not exist. Please try again";
		}
		}

		reflectChangeToCurrent(retrievedSortedList);

		return result;
	}

	private void reflectChangeToCurrent(GeneralSortedList retrievedSortedList) {
		currentActiveTasks.clear();
		for (Task task : retrievedSortedList) {
			currentActiveTasks.add(task);
		}
	}
	
	private void reflectChangeToArchives(ArchiveSortedList retrievedArchiveFromLogic) {
		for (Task task : retrievedArchiveFromLogic) {
			currentArchives.add(task);
		}
	}

	private String exitCommand() {
		String result = new String();
		logicController.exit(newMaxID);
		logicController.setSerialNumber(logicController.getSerialNumber() + 1);
		result = "HeyBuddy! is closing";
		return result;
	}

	private String archiveCommand(String[] splitInput) {
		// syntax -archive [task ID]
		String result = new String();
		boolean isChangeAll = false;
		ToDoSortedList retrievedActiveTaskList = new ToDoSortedList();
		Date currentTime = new Date();
		int positionOfParameter = 1;
		if (splitInput[positionOfParameter].equals(ALL_KEYWORD)) {
			positionOfParameter = 2;
			isChangeAll = true;
		}
		int taskIDFromUI = Integer.parseInt(splitInput[positionOfParameter]);
		if (!currentActiveTasks.isEmpty()) {
			Task taskToArchive = currentActiveTasks.get(taskIDFromUI - 1);
			if (isChangeAll) {
				if (taskToArchive.isRecurrence()) {
					retrievedActiveTaskList = logicController.archiveAllTasks(taskToArchive, currentTime);
				} else {
					return result = "Cannot archive all selected, not recurrence task";
				}
			} else {
				retrievedActiveTaskList = logicController.moveToArchive(taskToArchive, currentTime);
			}
			result = "Task moved to archive: " + taskToArchive.getDescription();
			reflectChangeToCurrent(retrievedActiveTaskList);
		} else {
			result = "No tasks to archive";
		}
		return result;
	}

	private String deleteCommand(String[] splitInput) {
		// syntax : -delete <current/archive> [task ID]
		String result = new String();
		int positionOfParameter = 1;
		boolean isChangeAll = false;
		if (splitInput[positionOfParameter].equals(ALL_KEYWORD)) {
			positionOfParameter = 2;
			isChangeAll = true;
		}

		if (splitInput[positionOfParameter].equalsIgnoreCase(CURRENT_KEYWORD)) {
			positionOfParameter = positionOfParameter + 1;
			result = deleteFromCurrent(splitInput, positionOfParameter, isChangeAll);
		} else if (splitInput[positionOfParameter].equalsIgnoreCase(ARCHIVE_KEYWORD)) {
			positionOfParameter = positionOfParameter + 1;
			result = deleteFromArchive(splitInput, positionOfParameter, isChangeAll);
		} else {
			result = deleteFromCurrent(splitInput, positionOfParameter, isChangeAll);
		}

		return result;
	}

	private String deleteFromArchive(String[] splitInput, int position,boolean isChangeAll) {
		String result;
		int taskIDFromUI = Integer.parseInt(splitInput[position]);
		if (!currentArchives.isEmpty()) {
			Task taskToDelete = currentArchives.get(taskIDFromUI - 1);
			ArchiveSortedList retrievedListFromLogic = new ArchiveSortedList();
			if (isChangeAll) {
				if (taskToDelete.isRecurrence()) {
					retrievedListFromLogic = logicController.deleteAllRecurringInArchive(taskToDelete);
				} else {
					return result = "Cannot delete all, not recurring task";
				}
			} else {
				retrievedListFromLogic = logicController.deleteFromArchive(taskToDelete);
			}
			result = "Deleted task from archive: "+ taskToDelete.getDescription();
			currentArchives.clear();
			reflectChangeToArchives(retrievedListFromLogic);
		} else {
			result = "No tasks to delete";
		}
		return result;
	}

	private String deleteFromCurrent(String[] splitInput, int position,boolean isChangeAll) {
		String result;
		int taskIDFromUI = Integer.parseInt(splitInput[position]);
		if (!currentActiveTasks.isEmpty()) {
			Task taskToDelete = currentActiveTasks.get(taskIDFromUI - 1);
			ToDoSortedList retrievedListFromLogic = new ToDoSortedList();

			result = "Deleted task from current: " + taskToDelete.getDescription();
			if (isChangeAll) {
				if (taskToDelete.isRecurrence()) {
					retrievedListFromLogic = logicController.deleteAllRecurringTask(taskToDelete);
				} else {
					return result = "Cannot delete all, not recurring task";
				}
			} else {
				retrievedListFromLogic = logicController.deleteTask(taskToDelete);
			}
			reflectChangeToCurrent(retrievedListFromLogic);
		} else {
			result = "No tasks to delete";
		}
		return result;
	}

	private String searchCommand(String[] splitInput) {
		// check input for what to search for (date/tag/priority/desc)
		String result = new String();
		String searchType = new String();
		int positionOfFirstParameter = 1;
		int positionOfSecondParameter = 2;
		int lengthForOneParameter = 2;
		try {
			searchType = splitInput[positionOfFirstParameter];
		} catch (ArrayIndexOutOfBoundsException e) {
			return result = "Please specify what to search for";
		}
		currentActiveTasks.clear();
		switch (searchType) {
		case (TODAY_KEYWORD): {
			// search by date, today's date
			if (splitInput.length > lengthForOneParameter) {
				return "Search syntax error";
			} else {
				Calendar today = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				currentActiveTasks = logicController.searchByDate(formatter.format(today.getTime()));
				result = "Searched by date: today";
			}
			break;
		}
		case (TMR_KEYWORD): {
			// search by date, tomorrow's date
			if (splitInput.length > lengthForOneParameter) {
				return "Search syntax error";
			} else {
				Calendar tomorrow = Calendar.getInstance();
				tomorrow.add(Calendar.DATE, 1);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				currentActiveTasks = logicController.searchByDate(formatter.format(tomorrow.getTime()));
				result = "Searched by date: tomorrow";
			}

			break;
		}
		case (DATE_KEYWORD): {
			ArrayList<String> dateInput = new ArrayList<String>();
			Date searchDate = new Date();
			String dateInputString = new String();
			for (int i = positionOfSecondParameter; i < splitInput.length; i++) {
				dateInput.add(splitInput[i]);
			}
			for (int i = 0; i < dateInput.size(); i++) {
				dateInputString = dateInputString.concat(dateInput.get(i) + " ");
			}
			dateInputString = dateInputString.trim();
			try {
				searchDate = determineDeadline(dateInput);
				SimpleDateFormat dateOutput = new SimpleDateFormat("yyyyMMdd");
				String dateOutputString = dateOutput.format(searchDate);
				currentActiveTasks = logicController.searchByDate(dateOutputString);
				result = "Searched by date: " + dateInputString;
			} catch (Exception e) {
				return result = DATE_FORMAT_ERROR;
			}
			break;
		}
		case (TAG): {
			currentActiveTasks = logicController.searchByTag(splitInput[positionOfSecondParameter]);
			result = "Searched by tag: " + splitInput[positionOfSecondParameter];
			break;
		}
		case (PRIORITY): {
			int priority = Integer.parseInt(splitInput[positionOfSecondParameter]);
			currentActiveTasks = logicController.searchByPriority(priority);
			result = "Searched by priority: " + splitInput[positionOfSecondParameter];
			break;
		}
		case (DESC_KEYWORD): {
			String newDescription = new String();
			for (int i = positionOfSecondParameter; i < splitInput.length; i++) {
				newDescription = newDescription.concat(splitInput[i] + " ");
			}
			newDescription = newDescription.trim();
			if (newDescription.isEmpty()) {
				return result = "No description specified";
			} else {
				currentActiveTasks = logicController.searchByDesc(newDescription);
				result = "Searched by description: " + newDescription;
			}
			break;
		}

		}
		return result;
	}

	private String addCommand(String[] input) {

		// break the commands
		newMaxID = logicController.getSerialNumber() + 1;
		logicController.setSerialNumber(newMaxID);
		String result = new String();
		String description = new String();
		int inputLength = input.length;
		int priority = -1;
		Date startTime = new Date();
		Date endTime = new Date();
		Date deadLine = new Date();
		boolean isGenericTask = true; // default is Generic Task
		boolean isDeadlineTask = false;
		boolean isMeetingTask = false;
		boolean isRecurring = false;
		ToDoSortedList retrievedList = new ToDoSortedList();
		ArrayList<String> userInput = new ArrayList<String>();
		ArrayList<String> tags = new ArrayList<String>();
		ArrayList<String> dateAsString = new ArrayList<String>();
		ArrayList<String> descriptionArrayList = new ArrayList<String>();
		String recurringPeriod = new String();
		long recurringTime = 0;
		int recurrenceNum = 0;

		for (int i = 0; i < inputLength; i++) {
			userInput.add(input[i]);
		}

		for (int i = 1; i < userInput.size(); i++) {
			if (userInput.get(i).charAt(0) != '-') {
				description = description.concat(userInput.get(i) + " ");
				// descriptionArrayList.add(userInput.get(i));
			} else {
				break;
			}
		}
		description = description.trim();
		if (description.isEmpty()) {
			return result = "No description added, try again";
		}

		priority = checkAddWithPriority(priority, userInput);
		checkAddWithTag(userInput, tags);

		if (userInput.contains(BY_KEYWORD)) {
			isGenericTask = false;
			isDeadlineTask = true;
			getDateAsString(userInput, dateAsString, BY_KEYWORD);
		} else if (userInput.contains(BY_SHORTCUT)) {
			isGenericTask = false;
			isDeadlineTask = true;
			getDateAsString(userInput, dateAsString, BY_SHORTCUT);
		} else if (userInput.contains(ON_KEYWORD)) {
			isGenericTask = false;
			isMeetingTask = true;
			getDateAsString(userInput, dateAsString, ON_KEYWORD);
		} else if (userInput.contains(ON_KEYWORD)) {
			isGenericTask = false;
			isMeetingTask = true;
			getDateAsString(userInput, dateAsString, ON_SHORTCUT);
		}
		if (userInput.contains(RECURRING_KEYWORD)) {
			if (isGenericTask) {
				return "No date specified, cannot be recurring";
			} else {
				isRecurring = true;
				int point = userInput.indexOf(RECURRING_KEYWORD);
				try {
					if (userInput.get(point + 1).charAt(0) != '-') {
						recurrenceNum = Integer.parseInt(userInput.get(point + 1));
						recurringPeriod = userInput.get(point + 2);
						userInput.remove(point + 1);
						userInput.remove(point + 1);
					} else {
						return "No recurring period stated";
					}
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
					return "Error in command format. Try -recurring <num> <period>";
				}
			}
		} else if (userInput.contains(RECURRING_SHORTCUT)) {
			if (isGenericTask) {
				return result = "No date specified, cannot be recurring";
			} else {
				isRecurring = true;
				int point = userInput.indexOf(RECURRING_SHORTCUT);
				try {
					if (userInput.get(point + 1).charAt(0) != '-') {
						recurrenceNum = Integer.parseInt(userInput.get(point + 1));
						recurringPeriod = userInput.get(point + 2);
						userInput.remove(point + 1);
						userInput.remove(point + 1);
					} else {
						return result = "No recurring period stated";
					}
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
					return result = "Error in command format. Try -recurring <num> <period>";
				}
			}
		}

		// convert dates or time into Date object
		// ----Meeting Task------
		// DD-MM-YYYY from HHmm to HHmm

		// ----Deadline Task
		// DD-MM-YYYY

		if (!dateAsString.isEmpty()) {

			if (isDeadlineTask) {
				deadLine = determineDeadline(dateAsString);
				if (deadLine == null) {
					return DEADLINE_FORMAT_ERROR;
				}

			} else if (isMeetingTask) {
				String positionCheck = FROM_KEYWORD;
				startTime = determineMeetingTime(positionCheck, dateAsString);
				positionCheck = TO_KEYWORD;
				endTime = determineMeetingTime(positionCheck, dateAsString);
				if (startTime == null | endTime == null) {
					return MEETINGTIME_FORMAT_ERROR;
				} else if (startTime.getTime() > endTime.getTime()) {
					return MEETINGTIME_FORMAT_ERROR;
				}
			}

		}

		// parse the recurring portion

		if (recurringPeriod.length() > 0) {
			if (recurringPeriod.equalsIgnoreCase(WEEKLY)) {
				if (isDeadlineTask) {
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.DATE, 7);
				} else if (isMeetingTask) {
					recurringTime = setPeriodOfOccurence(startTime,Calendar.DATE, 7);
				}
			} else if (recurringPeriod.equalsIgnoreCase(DAILY)) {
				if (isDeadlineTask) {
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.DATE, 1);
				} else if (isMeetingTask) {
					recurringTime = setPeriodOfOccurence(startTime,Calendar.DATE, 1);
				}
			} else if (recurringPeriod.equalsIgnoreCase(HOURLY)) {
				if (isDeadlineTask) {
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.HOUR, 1);
				} else if (isMeetingTask) {
					recurringTime = setPeriodOfOccurence(endTime,Calendar.HOUR, 1);
				}
			} else if (recurringPeriod.equalsIgnoreCase(MONTHLY)) {
				if (isDeadlineTask) {
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.MONTH, 1);
				} else if (isMeetingTask) {
					recurringTime = setPeriodOfOccurence(startTime,Calendar.MONTH, 1);
				}
			} else if (recurringPeriod.equalsIgnoreCase(YEARLY)) {
				if (isDeadlineTask) {
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.YEAR, 1);
				} else if (isMeetingTask) {
					recurringTime = setPeriodOfOccurence(startTime,Calendar.YEAR, 1);
				}
			}
		}

		logicController.setSerialNumber(newMaxID + recurrenceNum);

		// format for tasks: integer ID, String description, int priority,
		// ArrayList<String> tags,archived
		if (isGenericTask) {
			// floating task
			Task newTask = new Task(newMaxID, description, priority, tags);
			retrievedList = logicController.addTask(newTask);
			result = "New task added: " + description;
		} else if (isDeadlineTask) {
			// deadline task
			Task newDeadlineTask = new Task(newMaxID, description, deadLine,priority, tags);
			if (isRecurring) {
				retrievedList = logicController.addRecurringTask(newDeadlineTask, recurringTime, recurrenceNum);
			} else {
				retrievedList = logicController.addTask(newDeadlineTask);
			}
			result = "New task added with deadline: " + description;
		} else if (isMeetingTask) {
			// meeting task
			Task newMeetingTask = new Task(newMaxID, description, startTime,endTime, priority, tags);
			if (isRecurring) {
				retrievedList = logicController.addRecurringTask(newMeetingTask, recurringTime, recurrenceNum);
			} else {
				retrievedList = logicController.addTask(newMeetingTask);
			}
			result = "New task added for meeting: " + description;
		}

		reflectChangeToCurrent(retrievedList);

		return result;
	}

	private boolean getDateAsString(ArrayList<String> userInput,ArrayList<String> dateAsString, String keyword) {
		boolean success = true;
		int point = userInput.indexOf(keyword);
		try {
			for (int j = point + 1; j < userInput.size(); j++) {
				if (userInput.get(j).charAt(0) != '-') {
					dateAsString.add(userInput.get(j));
				} else {
					break;
				}
			}
			for (int j = 0; j < dateAsString.size(); j++) {
				userInput.remove(dateAsString.get(j));
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			success = false;
		}
		return success;
	}

	private void checkAddWithTag(ArrayList<String> userInput,ArrayList<String> tags) {
		if (userInput.contains(TAG_KEYWORD)) {
			parseTags(userInput, tags, TAG_KEYWORD);
		} else if (userInput.contains(TAG_SHORTCUT)) {
			parseTags(userInput, tags, TAG_SHORTCUT);
		}
	}

	private boolean parseTags(ArrayList<String> userInput,ArrayList<String> tags, String keyword) {
		int point = userInput.indexOf(keyword);
		boolean success = true;
		try {
			for (int j = point + 1; j < userInput.size(); j++) {
				if (userInput.get(j).charAt(0) != '-') {
					tags.add(userInput.get(j));
				} else {
					break;
				}
			}
			for (int j = 0; j < tags.size(); j++) {
				userInput.remove(tags.get(j));
			}
			return success;
		} catch (ArrayIndexOutOfBoundsException e) {
			success = false;
			return success;
		}
	}

	private int checkAddWithPriority(int priority, ArrayList<String> userInput) {
		if (userInput.contains(PRIORITY_KEYWORD)) {
			// find position of info
			priority = parsePriority(priority, userInput, PRIORITY_KEYWORD);
		} else if (userInput.contains(PRIORITY_KEYWORD_SHORTCUT)) {
			priority = parsePriority(priority, userInput, PRIORITY_KEYWORD_SHORTCUT);
		}
		return priority;
	}

	private int parsePriority(int priority, ArrayList<String> userInput,String keyword) {
		int point = userInput.indexOf(keyword);
		// check if info needed is specified
		try {
			priority = Integer.parseInt(userInput.get(point + 1));
			userInput.remove(point + 1);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			return -1;
		}
		return priority;
	}

	private Date determineMeetingTime(String positionCheck, ArrayList<String> dateAsString) {
		int specifiedDay = 0;
		int specifiedMonth = 0;
		int specifiedYear = 0;
		int specifiedHour = 0;
		int specifiedMinutes = 0;
		boolean isSlashInputType = false; // i.e dd/MM/yyyy
		boolean isNormalInputType = false; // i.e dd MMMM yyyy
		Date meetingTime;
		int supposedDayPosition = 0;
		int supposedMonthPosition = 1;
		int supposedYearPosition = 2;
		int hourPosition = 0;
		int startIndexOfMinutes = 1;
		int endIndexOfMinutes = 3;
		int slashDatePosition = 0;
		if (dateAsString.get(0).contains("/")) {
			isSlashInputType = true;
		} else {
			isNormalInputType = true;
		}
		if (isSlashInputType) {
			// separate the fields
			String[] slashInput = dateAsString.get(slashDatePosition).split("/");
			specifiedDay = Integer.parseInt(slashInput[supposedDayPosition]);
			specifiedMonth = Integer.parseInt(slashInput[supposedMonthPosition]) - 1;
			int position = dateAsString.indexOf(positionCheck);
			if (dateAsString.get(position + 1).contains(":")) {
				int minutePosition = dateAsString.get(position + 1).indexOf(":");
				specifiedHour = Integer.parseInt(dateAsString.get(position + 1).substring(hourPosition, minutePosition));
				specifiedMinutes = Integer.parseInt(dateAsString.get(position + 1).substring(
						minutePosition + startIndexOfMinutes,
						minutePosition + endIndexOfMinutes));
			} else {
				return null;
			}
			if (slashInput.length > supposedYearPosition) {
				// if year is specified
				specifiedYear = Integer.parseInt(slashInput[supposedYearPosition]);
			} else {
				return meetingTime = Date.getDayTime(specifiedMonth,specifiedDay, specifiedHour, specifiedMinutes);
			}

		} else if (isNormalInputType) {
			specifiedDay = Integer.parseInt(dateAsString.get(0));
			specifiedMonth = getSpecifiedMonth(dateAsString, specifiedMonth);
			if (specifiedMonth == 0) {
				return null;
			}
			int position = dateAsString.indexOf(positionCheck);
			if (dateAsString.get(position + 1).contains(":")) {
				int minutePosition = dateAsString.get(position + 1).indexOf(":");
				specifiedHour = Integer.parseInt(dateAsString.get(position + 1).substring(hourPosition, minutePosition));
				specifiedMinutes = Integer.parseInt(dateAsString.get(position + 1).substring(
						minutePosition + startIndexOfMinutes,
						minutePosition + endIndexOfMinutes));
			} else {
				return null;
			}
			position = dateAsString.indexOf(FROM_KEYWORD);
			if (position < 3) {
				// i.e year unspecified
				return meetingTime = Date.getDayTime(specifiedMonth,specifiedDay, specifiedHour, specifiedMinutes);
			} else {
				specifiedYear = Integer.parseInt(dateAsString.get(supposedYearPosition));
			}

		}
		meetingTime = Date.getDayTime(specifiedYear, specifiedMonth,specifiedDay, specifiedHour, specifiedMinutes);
		return meetingTime;
	}

	private Date determineDeadline(ArrayList<String> dateAsString) {
		int specifiedDay = 0;
		int specifiedMonth = 0;
		int specifiedYear = 0;
		int specifiedHour = 0;
		int specifiedMinutes = 0;
		int supposedDayPosition = 0;
		int supposedMonthPosition = 1;
		int supposedYearPosition = 2;
		int hourPosition = 0;
		int startIndexOfMinutes = 1;
		int endIndexOfMinutes = 3;
		int slashDatePosition = 0;
		Date deadLine;
		boolean timeGiven = false;
		boolean isSlashInputType = false; // i.e dd/MM/yyyy
		boolean isNormalInputType = false; // i.e dd MMMM yyyy

		if (dateAsString.get(slashDatePosition).contains("/")) {
			isSlashInputType = true;
		} else {
			isNormalInputType = true;
		}

		if (dateAsString.contains(AT_KEYWORD)) {
			timeGiven = true;
		} else {
			timeGiven = false;
		}

		if (isSlashInputType) {
			// separate the fields
			String[] slashInput = dateAsString.get(slashDatePosition).split("/");
			specifiedDay = Integer.parseInt(slashInput[supposedDayPosition]);
			specifiedMonth = Integer.parseInt(slashInput[supposedMonthPosition]) - 1;
			if (timeGiven) {
				int position = dateAsString.indexOf(AT_KEYWORD);
				int minutePosition = dateAsString.get(position + 1).indexOf(":");
				specifiedHour = Integer.parseInt(dateAsString.get(position + 1).substring(hourPosition, minutePosition));
				specifiedMinutes = Integer.parseInt(dateAsString.get(position + 1).substring(
						minutePosition + startIndexOfMinutes,
						minutePosition + endIndexOfMinutes));
			} else {
				if (dateAsString.size() > 1) {
					// i.e time given, but no "at" dd/MM/yyyy HHmm -> syntax
					// error, need "at"
					return null;
				} else {
					specifiedHour = 23;
					specifiedMinutes = 59;
				}
			}
			if (slashInput.length > 2) {
				// if year is specified
				specifiedYear = Integer.parseInt(slashInput[supposedYearPosition]);
			} else {
				return deadLine = Date.getDayTime(specifiedMonth, specifiedDay,specifiedHour, specifiedMinutes);
			}

		} else if (isNormalInputType) {
			// get the specified values
			// separate the time from the date
			specifiedDay = Integer.parseInt(dateAsString.get(supposedDayPosition));
			specifiedMonth = getSpecifiedMonth(dateAsString, specifiedMonth);
			if (specifiedMonth == 12) {
				return null;
			}
			if (timeGiven) {
				int position = dateAsString.indexOf(AT_KEYWORD);
				if (dateAsString.get(position + 1).contains(":")) {
					int minutePosition = dateAsString.get(position + 1).indexOf(":");
					specifiedHour = Integer.parseInt(dateAsString.get(position + 1).substring(hourPosition, minutePosition));
					specifiedMinutes = Integer.parseInt(dateAsString.get(position + 1).substring(
							minutePosition + startIndexOfMinutes,
							minutePosition + endIndexOfMinutes));
				} else {
					return null;
				}
				if (position < 3) {
					// no year specified
					return deadLine = Date.getDayTime(specifiedMonth,specifiedDay, specifiedHour, specifiedMinutes);
				} else {
					specifiedYear = Integer.parseInt(dateAsString.get(supposedYearPosition));
					return deadLine = Date.getDayTime(specifiedYear,specifiedMonth, specifiedDay, specifiedHour,specifiedMinutes);
				}
			} else {
				if (dateAsString.size() >= 3) {
					// year specified
					if (dateAsString.get(supposedYearPosition).contains(":")) {
						return null; // its a time given, wrong format
					} else {
						specifiedYear = Integer.parseInt(dateAsString.get(2));
						return deadLine = Date.getDay2359(specifiedYear,specifiedMonth, specifiedDay);
					}
				} else {
					return deadLine = Date.getDay2359(specifiedMonth,specifiedDay);
				}
			}
		}
		deadLine = Date.getDayTime(specifiedYear, specifiedMonth, specifiedDay,specifiedHour, specifiedMinutes);
		return deadLine;
	}

	private int getSpecifiedMonth(ArrayList<String> dateAsString,
			int specifiedMonth) {
		switch (dateAsString.get(1).toLowerCase()) {
		case ("jan"): {
			specifiedMonth = 0;
			break;
		}
		case ("feb"): {
			specifiedMonth = 1;
			break;
		}
		case ("mar"): {
			specifiedMonth = 2;
			break;
		}
		case ("apr"): {
			specifiedMonth = 3;
			break;
		}
		case ("may"): {
			specifiedMonth = 4;
			break;
		}
		case ("jun"): {
			specifiedMonth = 5;
			break;
		}
		case ("jul"): {
			specifiedMonth = 6;
			break;
		}
		case ("aug"): {
			specifiedMonth = 7;
			break;
		}
		case ("sep"): {
			specifiedMonth = 8;
			break;
		}
		case ("oct"): {
			specifiedMonth = 9;
			break;
		}
		case ("nov"): {
			specifiedMonth = 10;
			break;
		}
		case ("dec"): {
			specifiedMonth = 11;
			break;
		}
		case ("january"): {
			specifiedMonth = 0;
			break;
		}
		case ("february"): {
			specifiedMonth = 1;
			break;
		}
		case ("march"): {
			specifiedMonth = 2;
			break;
		}
		case ("april"): {
			specifiedMonth = 3;
			break;
		}
		case ("june"): {
			specifiedMonth = 5;
			break;
		}
		case ("july"): {
			specifiedMonth = 6;
			break;
		}
		case ("august"): {
			specifiedMonth = 7;
			break;
		}
		case ("september"): {
			specifiedMonth = 8;
			break;
		}
		case ("october"): {
			specifiedMonth = 9;
			break;
		}
		case ("november"): {
			specifiedMonth = 10;
			break;
		}
		case ("december"): {
			specifiedMonth = 11;
			break;
		}
		default: {
			specifiedMonth = 12;
		}
		}
		return specifiedMonth;
	}

	private long setPeriodOfOccurence(Date dueDate, int type, int addValue) {

		Calendar nextOccurence = Calendar.getInstance();
		nextOccurence.setTime(dueDate);
		nextOccurence.add(type, addValue);
		return (nextOccurence.getTimeInMillis() - dueDate.getTime());
	}
}
