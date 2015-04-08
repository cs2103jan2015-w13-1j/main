package Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import Common.Date;
import Common.Task;
import Logic.LogicController;
import SortedList.ArchiveSortedList;
import SortedList.PrioritySortedList;
import SortedList.ToDoSortedList;
import Storage.StorageController;

public class CommandController implements InterfaceForParser {

	private static LogicController logicController = new LogicController();
	private static StorageController storageController = new StorageController();
	private static Parser parser = new Parser();
	private int newMaxID = 0;
	private ArrayList<Task> currentActiveTasks = new ArrayList<Task>();
	private ArrayList<Task> currentArchives = new ArrayList<Task>();
	private static final String DEADLINE_FORMAT_ERROR = "Wrong format for deadline. Please use dd/MM/yyyy";
	private static final String MEETINGTIME_FORMAT_ERROR = "Wrong format for timing. Please use dd/MM/yyyy HHmm HHmm";
	private static final String DATE_FORMAT_ERROR = "Wrong format for date. Please use dd/MM/yyyy";
	
	
	
	public static void main(String[] args){
		
		CommandController test = new CommandController();
		test.initialiseTasks();
		System.out.println(test.executeCommand("add new deadline -by 12 apr"));
		System.out.println(test.returnTasks());
		System.out.println(test.executeCommand("add new deadline -by 12/04"));
		System.out.println(test.returnTasks());
		System.out.println(test.executeCommand("add new deadline -by 12 apr at 1200"));
		System.out.println(test.returnTasks());
		//System.out.println(test.executeCommand("-add tagstest -tags hehehe -date 23/03/2015"));
		//System.out.println(test.returnTasks());
		//System.out.println(test.executeCommand("-add second generic task"));
		//System.out.println(test.returnTasks());
		
		
	}
	
	
	
	
	public ArrayList<Task> initialiseTasks(){
		logicController.initialise();
		newMaxID = logicController.getSerialNumber();
		//Upon application start-up, fetch the current tasklist
		ToDoSortedList retrievedCurrent = new ToDoSortedList();
		
		try{
			retrievedCurrent = logicController.viewActiveTasks();
			reflectChangeToCurrent(retrievedCurrent);
		}catch(NullPointerException e){
			currentActiveTasks.clear();
			return currentActiveTasks;
		}
		
		return currentActiveTasks;		
		
	}
	public ArrayList<Task> initialiseArchives(){
		//Upon application start-up, fetch the archived tasks to display
		ArchiveSortedList retrievedArchiveFromLogic = new ArchiveSortedList();
		
		
		currentArchives.clear();
		try{
			retrievedArchiveFromLogic = logicController.viewArchiveTasks();
			for(Task task : retrievedArchiveFromLogic){
				currentArchives.add(task);
			}
		}catch(NullPointerException e){
			return currentArchives;
		}
		
		
		return currentArchives;		
	}
	
	public ArrayList<Task> returnTasks(){

		return currentActiveTasks;
	}
	
	public ArrayList<Task> returnArchive(){
		
		ArchiveSortedList retrievedArchiveFromLogic = new ArchiveSortedList();

		try{
			retrievedArchiveFromLogic = logicController.viewArchiveTasks();
			currentArchives.clear();
			for(Task task : retrievedArchiveFromLogic){
				currentArchives.add(task);
			}
		}catch(NullPointerException e){
			return currentArchives;
		}
		
		return currentArchives;	
		

	}
	
	public String executeCommand(String inputCommand){
		int commandNum = parser.parseIn(inputCommand);
		String[] splitInput = inputCommand.split(" ");
		String result = new String();
		switch(commandNum){
			case(0):{
				result = "Invalid command";
				break;
			}case(1):{
				result = addCommand(splitInput);
				break;
			}case(2):{
				result = searchCommand(splitInput);
				break;
			}case(3):{
				result = deleteCommand(splitInput);
				break;
			}case(4):{
				result = archiveCommand(splitInput);
				break;
			}case(5):{
				result = exitCommand();
				break;
			}case(6):{
				result = modifyCommand(splitInput);
				break;
			}case(7):{
				result = fileDirectoryCommand(splitInput);
				break;
			}case(8):{
				result = refreshCommand();
				break;
			}case(9):{
				result = undoCommand();
				break;
			}case(10):{
				result = addtagCommand(splitInput);
				break;
			}case(11):{
				result = removetagCommand(splitInput);
				break;
			}case(12):{
				result = sortCommand(splitInput);
				break;
			}case(13):{
				result = importCommand(splitInput);
				break;
			}case(14):{
				result = exportCommand(splitInput);
				break;
			}
		}
		return result;
	}
	private String exportCommand(String[] splitInput) {
		String directoryToExport = new String();
		if(splitInput[1].equals("to")){
			//execution
			try{
				directoryToExport = splitInput[2];
				if (splitInput.length > 3) {
					for (int i = 3; i < splitInput.length; i++) {
						directoryToExport = directoryToExport.concat(" ").concat(splitInput[i]);
					}
				}
				if (storageController.exportToDirectory(directoryToExport) == true) {
					return "Current data exported to " + directoryToExport ;
				} else {
					return "Fail to export to " + directoryToExport;
				}
			}catch(ArrayIndexOutOfBoundsException e){
				return "Please specify a file path to export to";
			}
		}else{
			return "Invalid command, try \"export to\"";
		}
	}




	private String importCommand(String[] splitInput) {
		String directoryToImport = new String();
		int position = 0;
		if(splitInput[1].equals("from")){
			position = 2;
			try{
				directoryToImport = splitInput[2];
				if (splitInput.length > 3) {
					for (int i = 3; i < splitInput.length; i++) {
						directoryToImport = directoryToImport.concat(" ").concat(splitInput[i]);
					}
				}
				if (storageController.importFromDirectory(directoryToImport) == true) {
					String relativeDirectory = storageController.getFileDirectory();
					initialiseTasks();
					initialiseArchives();
					System.out.println("success");
					return "Imported from " + relativeDirectory ;
				} else {
					return "Fail to import file. File invalid.";
				}
			}catch(ArrayIndexOutOfBoundsException e){
				return "Please specify a directory to import from";
			}
		}else{
			return "Invalid command, try \"import from \"";
		}
		
		

	}




	private String sortCommand(String[] splitInput) {
		String result = new String();
		String sortType = new String();
		try{
			sortType = splitInput[1];
			if(splitInput[1].equalsIgnoreCase("time")|splitInput[1].equalsIgnoreCase("t")){
				ToDoSortedList retrievedSortedList = logicController.sortByTime();
				reflectChangeToCurrent(retrievedSortedList);
				result = "Tasks sorted by time";
			}else if(splitInput[1].equalsIgnoreCase("priority")|splitInput[1].equalsIgnoreCase("p")){
				PrioritySortedList retrievedSortedList = logicController.sortByPriority();
				currentActiveTasks.clear();
				for(Task task : retrievedSortedList){
					currentActiveTasks.add(task);
				}
				result = "Tasks sorted by priority";
			}else{
				return result = "Invalid, can sort by time or date only";
			}
			
		}catch(ArrayIndexOutOfBoundsException e){
			return result = "Please specify what to sort by";
		}
		return result;
	}


	private String removetagCommand(String[] splitInput) {
		String result = new String();
		int taskID = 0;
		String tagToRemove = new String();
		int position = 0;
		boolean isChangeAll = false;
		ToDoSortedList retrievedSortedList = new ToDoSortedList();
		if(splitInput[1].equals("all")){
			position=1;
			isChangeAll = true;
		}
		try{
			taskID = Integer.parseInt(splitInput[position+1]);
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			return result = "Please specify ID of task to remove tag from";
		}
		try{
			tagToRemove = splitInput[position+2];
		}catch(ArrayIndexOutOfBoundsException e){
			return result = "Please specify the tag to remove";
		}
		if(currentActiveTasks.size()<taskID){
			return result = "Task does not exist";
		}else if(!currentActiveTasks.get(taskID-1).getTags().contains(tagToRemove)){
			return result = "Tag does not exist";
		}else{
			Task taskToChange = currentActiveTasks.get(taskID-1);
			if(isChangeAll){
				if(taskToChange.isRecurrence()){
					retrievedSortedList = logicController.removeAlltag(taskToChange, tagToRemove);
				}else{
					return result ="Cannot change all, not recurring task";
				}
				reflectChangeToCurrent(retrievedSortedList);
			}else{
			
			retrievedSortedList = logicController.removeTag(taskToChange, tagToRemove);
			
			}
			reflectChangeToCurrent(retrievedSortedList);
		}
		return result = "Tag \"" +tagToRemove +"\" removed from task";

	}

	private String addtagCommand(String[] splitInput) {
		String result = new String();
		int taskID = 0;
		int position = 0;
		boolean isChangeAll = false;
		ToDoSortedList retrievedSortedList = new ToDoSortedList();
		if(splitInput[1].equals("all")){
			position=1;
			isChangeAll = true;
		}
		String tagToAdd = new String();
		try{
			taskID = Integer.parseInt(splitInput[position+1]);
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			return result = "Please specify ID of task to remove tag from";
		}
		try{
			tagToAdd = splitInput[position+2];
		}catch(ArrayIndexOutOfBoundsException e){
			return result = "Please specify the tag to remove";
		}
		if(currentActiveTasks.size()<taskID){
			return result = "Task does not exist";
		}else if(currentActiveTasks.get(taskID-1).getTags().contains(tagToAdd)){
			return result = "Tag already exists";
		}else{
			Task taskToChange = currentActiveTasks.get(taskID-1);
			if(isChangeAll){
				if(taskToChange.isRecurrence()){
					retrievedSortedList = logicController.addAlltag(taskToChange, tagToAdd);
				}else{
					return result ="Cannot change all, not recurring task";
				}
				reflectChangeToCurrent(retrievedSortedList);
			}else{
			
			retrievedSortedList = logicController.addTag(taskToChange, tagToAdd);
			
			}
			reflectChangeToCurrent(retrievedSortedList);
		}
		
		return result = "Tag \"" +tagToAdd +"\" added to task";
		
	}

	private String undoCommand() {
		boolean isSuccessful = logicController.undo();
		refreshCurrent();
		refreshArchive();
		String result = new String();
		if(isSuccessful){
			return result = "Undo successful";
		} else{
			return result = "Undo unsuccessful, nothing to undo";
		}

	}
	private void refreshArchive() {
		currentArchives.clear();
		ArchiveSortedList retrievedArchiveList = logicController.viewArchiveTasks();
		for(Task task : retrievedArchiveList){
			currentArchives.add(task);
		}
	}
	private void refreshCurrent() {
		currentActiveTasks.clear();
		ToDoSortedList retrievedSortedList = logicController.viewActiveTasks();
		for(Task task : retrievedSortedList){
			currentActiveTasks.add(task);
		}
	}
	String refreshCommand() {
		String result = "Display refreshed to current tasks";
		
		try{
			refreshCurrent();
		}catch(NullPointerException e){
			return result = "No active tasks to display";
		}
		
		return result;
	}


	String fileDirectoryCommand(String[] splitInput) {
		String result = new String();
		String tempPath = splitInput[1];
		if (splitInput.length > 2) {
			for (int i = 2; i < splitInput.length; i++) {
				tempPath = tempPath.concat(" ").concat(splitInput[i]);
			}
		}
		String specifiedFileDirectory = tempPath;
		String acknowledgeCheck = storageController.changeFileDirectory(specifiedFileDirectory);
		result = "File stored at: " + storageController.getFileDirectory();
		return result;
	}


	String modifyCommand(String[] splitInput) {
		//for recurring changes, change all <taskID> <..parameters>
		String result = new String();
		String modifyParameter = new String();
		ToDoSortedList retrievedSortedList = new ToDoSortedList();
		ArrayList<String> inputArray = new ArrayList<String>();
		boolean isChangeAll = false;
		int position = 0;
		for(int i = 0; i< splitInput.length; i++){
			inputArray.add(splitInput[i]);
		}
		if(inputArray.contains("all")){
			isChangeAll = true;
			position = inputArray.indexOf("all");
		}
		int taskID =0;
		try{
			taskID = Integer.parseInt(inputArray.get(position+1))-1;
		}catch(NumberFormatException|ArrayIndexOutOfBoundsException e){
			return result = "Please specify task ID to change first.";
		}
		try{
			modifyParameter = inputArray.get(position+2);
		}catch(ArrayIndexOutOfBoundsException e){
			return result = "Please specify what to change";
		}
		
		Task taskToChange = currentActiveTasks.get(taskID);
		switch(modifyParameter){
			case("date"):{
				//find task ID, get task
				String taskType = taskToChange.getType();
				String dateInput = new String();
				ArrayList<String> dateAsString = new ArrayList<String>();
				for(int i=inputArray.indexOf("date")+1; i<inputArray.size(); i++){
					dateAsString.add(inputArray.get(i));
				}
				//cases: generic task, deadline task, meeting task 
				if(taskType.equalsIgnoreCase("generic")){
					//i.e no date exists
					//parse in the new deadline/startend time
					
					if(dateAsString.contains("from")&&(dateAsString.contains("to"))){
						//if change to meeting task
						String positionCheck = "from";
						Date newStartTime = determineMeetingTime(positionCheck, dateAsString);
						positionCheck = "to";
						Date newEndTime = determineMeetingTime(positionCheck, dateAsString);
						if(newStartTime == null | newEndTime == null){
							return MEETINGTIME_FORMAT_ERROR;
						}else{
							retrievedSortedList = logicController.addStartAndEndTime(taskToChange, newStartTime,newEndTime);
							reflectChangeToCurrent(retrievedSortedList);
						}
								
					}else if(dateAsString.contains("at")){
						//if change to deadline task
						//syntax: -change date dd/MM/yyyy

						Date newDeadline = determineDeadline(dateAsString);
						if(newDeadline == null){
							return DEADLINE_FORMAT_ERROR;
						}else{
							retrievedSortedList = logicController.addDeadLine(taskToChange, newDeadline);
							reflectChangeToCurrent(retrievedSortedList);
						}
						return result = "Deadline changed";
						
					}else{
						return DATE_FORMAT_ERROR;
					}
				}else if(taskType.equalsIgnoreCase("deadline")){
					//date exists, change deadline
					//syntax: -change date dd/MM/yyyy
					if(isChangeAll){
						try{
							int positionOfTime = dateAsString.indexOf("date") +1;
							String timeAsString = dateAsString.get(positionOfTime);
							String hourAsString = timeAsString.substring(0, 2);
							String minutesAsString = timeAsString.substring(2, 4);
							int newHour = Integer.parseInt(hourAsString);
							int newMinutes = Integer.parseInt(minutesAsString);
							retrievedSortedList = logicController.editAlldeadlineTime(taskToChange, newHour, newMinutes);
							reflectChangeToCurrent(retrievedSortedList);
						}catch(NumberFormatException e){
							return "Wrong format for time";
						}
					}else{
						Date newDeadline = determineDeadline(dateAsString);
						if(newDeadline == null){
							return DEADLINE_FORMAT_ERROR;
						}else{
							retrievedSortedList = logicController.editDeadline(taskToChange, newDeadline);
							reflectChangeToCurrent(retrievedSortedList);
						}
					}
					return result = "Deadline changed";
					
				}else if(taskType.equalsIgnoreCase("meeting")){
					if(isChangeAll){
						if(dateAsString.contains("start") && dateAsString.contains("end")){
							int positionOfTime = dateAsString.indexOf("start") +1;
							String timeAsString = dateAsString.get(positionOfTime);
							String hourAsString = timeAsString.substring(0, 2);
							String minutesAsString = timeAsString.substring(2, 4);
							int newStartHour = Integer.parseInt(hourAsString);
							int newStartMinutes = Integer.parseInt(minutesAsString);
							positionOfTime = dateAsString.indexOf("end")+1;
							timeAsString = dateAsString.get(positionOfTime);
							hourAsString = timeAsString.substring(0, 2);
							minutesAsString = timeAsString.substring(2, 4);
							int newEndHour = Integer.parseInt(hourAsString);
							int newEndMinutes = Integer.parseInt(minutesAsString);
							retrievedSortedList = logicController.editAllStartTime(taskToChange, newStartHour, newStartMinutes);
							retrievedSortedList = logicController.editAllEndTime(taskToChange, newEndHour, newEndMinutes);
							reflectChangeToCurrent(retrievedSortedList);
							return "Start and end time changed";
						}else if(dateAsString.contains("end")){
							int positionOfTime = dateAsString.indexOf("end") +1;
							String timeAsString = dateAsString.get(positionOfTime);
							String hourAsString = timeAsString.substring(0, 2);
							String minutesAsString = timeAsString.substring(2, 4);
							int newHour = Integer.parseInt(hourAsString);
							int newMinutes = Integer.parseInt(minutesAsString);
							retrievedSortedList = logicController.editAllEndTime(taskToChange, newHour, newMinutes);
							reflectChangeToCurrent(retrievedSortedList);
							return "End time changed";
						}else if(dateAsString.contains("start")){
							int positionOfTime = dateAsString.indexOf("start") +1;
							String timeAsString = dateAsString.get(positionOfTime);
							String hourAsString = timeAsString.substring(0, 2);
							String minutesAsString = timeAsString.substring(2, 4);
							int newHour = Integer.parseInt(hourAsString);
							int newMinutes = Integer.parseInt(minutesAsString);
							retrievedSortedList = logicController.editAllStartTime(taskToChange, newHour, newMinutes);
							reflectChangeToCurrent(retrievedSortedList);
							return "Start time changed";
						}
					}else if(dateAsString.contains("start") && dateAsString.contains("end")){
						//change start and end time
						//syntax:-change date dd/MM/yyyy HHmm HHmm
						String positionCheck = "start";
						Date newStartTime = determineMeetingTime(positionCheck, dateAsString);
						positionCheck = "end";
						Date newEndTime = determineMeetingTime(positionCheck, dateAsString);
						if(newStartTime == null | newEndTime == null){
							return MEETINGTIME_FORMAT_ERROR;
						}else{
							retrievedSortedList = logicController.editStartTime(taskToChange, newStartTime);
							retrievedSortedList = logicController.editEndTime(taskToChange, newEndTime);
							reflectChangeToCurrent(retrievedSortedList);
						}
						return result = "Meeting start and end time changed";
					}else if(dateAsString.contains("start")){
						String positionCheck = "start";
						Date newStartTime = determineMeetingTime(positionCheck, dateAsString);
						if(newStartTime == null){
							return DATE_FORMAT_ERROR;
						}else{
							retrievedSortedList = logicController.editStartTime(taskToChange, newStartTime);
							reflectChangeToCurrent(retrievedSortedList);
							return result = "Meeting start time changed";
						}
					}else if(dateAsString.contains("end")){
						String positionCheck = "end";
						Date newEndTime = determineMeetingTime(positionCheck, dateAsString);
						if(newEndTime == null){
							return DATE_FORMAT_ERROR;
						}else{
							retrievedSortedList = logicController.editEndTime(taskToChange, newEndTime);
							reflectChangeToCurrent(retrievedSortedList);
							return result = "Meeting end time changed";
						}
					}					
					
				}else{
					return result = "Type mismatch error, can't change date";
				}
				break;
			}case("priority"):{
				//syntax: -change priority <taskID> <new priority>
				int newPriority = Integer.parseInt(inputArray.get(position+3));
				if(isChangeAll){
					if(taskToChange.isRecurrence()){
						retrievedSortedList = logicController.editAllPriority(taskToChange, newPriority);
					}else{
						return result = "Cannot change all, not recurring task";
					}
				}else{
					retrievedSortedList = logicController.editPriority(taskToChange, newPriority);
				}
				//retrievedSortedList = logicController.editPriority(taskToChange, newPriority);
				result = "Priority changed";
				break;
			}case("desc"):{
				//syntax: -change desc <taskID> <new desc>
				//String newDescription = splitInput[3];
				int descStartPoint = position+3;
				ArrayList<String> newDescription = new ArrayList<String>();
				for(int i= descStartPoint; i<inputArray.size();i++){
					newDescription.add(inputArray.get(i));
				}
				String newDescriptionString = new String();
				for(int i=0;i<newDescription.size();i++){
					newDescriptionString = newDescriptionString.concat(newDescription.get(i)+" ");
				}
				newDescriptionString = newDescriptionString.trim();
				if(isChangeAll){
					if(taskToChange.isRecurrence()){
						retrievedSortedList = logicController.editAllDescription(taskToChange, newDescriptionString);
					}else{
						return result = "Cannot change all, not recurring task";
					}
				}else{
					retrievedSortedList = logicController.editDescription(taskToChange, newDescriptionString);
				}
				//retrievedSortedList = logicController.editDescription(taskToChange, newDescriptionString);
				result = "Description changed";
				break;
			}default:{
				return result = "Value to change does not exist. Please try again";
			}
		}
		
		reflectChangeToCurrent(retrievedSortedList);
		
		
		return result;
	}




	private void reflectChangeToCurrent(ToDoSortedList retrievedSortedList) {
		currentActiveTasks.clear();
		for(Task task : retrievedSortedList){
			currentActiveTasks.add(task);
		}
	}


	String exitCommand() {
		String result = new String();
		logicController.exit(newMaxID);
		logicController.setSerialNumber(logicController.getSerialNumber()+1);
		result = "HeyBuddy! is closing";
		return result;
	}


	String archiveCommand(String[] splitInput) {
		//syntax -archive [task ID]
		String result = new String();
		
		int position = 0;
		boolean isChangeAll = false;
		ToDoSortedList retrievedActiveTaskList = new ToDoSortedList();
		Date currentTime = new Date();
		if(splitInput[1].equals("all")){
			position = 1;
			isChangeAll = true;
		}
		int taskIDFromUI = Integer.parseInt(splitInput[position+1]);


		if(!currentActiveTasks.isEmpty()){
			Task taskToArchive = currentActiveTasks.get(taskIDFromUI-1);
			if(isChangeAll){
				if(taskToArchive.isRecurrence()){
					retrievedActiveTaskList = logicController.archiveAllTasks(taskToArchive, currentTime);
					result = "Task moved to archive: " + taskToArchive.getDescription();
				}else{
					return result = "Cannot archive all selected, not recurrence task";
				}
			}else{
				retrievedActiveTaskList = logicController.moveToArchive(taskToArchive, currentTime);
				result = "Task moved to archive: " + taskToArchive.getDescription();
				
			}
			reflectChangeToCurrent(retrievedActiveTaskList);
		}else{
			result = "No tasks to archive";
		}
		
		return result;
	}


	String deleteCommand(String[] splitInput) {
		//syntax : -delete <current/archive> [task ID]
		String result = new String();
		int position = 0;
		boolean isChangeAll = false;
		if(splitInput[1].equals("all")){
			position = 1;
			isChangeAll=true;
		}
		
		if(splitInput[position+1].equalsIgnoreCase("current")){
			int taskIDFromUI = Integer.parseInt(splitInput[position+2]);
			if(!currentActiveTasks.isEmpty()){
				Task taskToDelete = currentActiveTasks.get(taskIDFromUI-1);
				ToDoSortedList retrievedListFromLogic = new ToDoSortedList();
	
				result = "Deleted task from current: " + taskToDelete.getDescription();
				if(isChangeAll){
					if(taskToDelete.isRecurrence()){
						retrievedListFromLogic =  logicController.deleteAllRecurringTask(taskToDelete);
					}else{
						return result = "Cannot delete all, not recurring task";
					}
				}else{
					retrievedListFromLogic = logicController.deleteTask(taskToDelete);
				}
				reflectChangeToCurrent(retrievedListFromLogic);
			}else{
				result = "No tasks to delete";
			}
		}else if(splitInput[position+1].equalsIgnoreCase("archive")){
			int taskIDFromUI = Integer.parseInt(splitInput[position+2]);
			if(!currentArchives.isEmpty()){
				Task taskToDelete = currentArchives.get(taskIDFromUI-1);
				ArchiveSortedList retrievedListFromLogic = new ArchiveSortedList();
				if(isChangeAll){
					if(taskToDelete.isRecurrence()){
						retrievedListFromLogic = logicController.deleteAllRecurringInArchive(taskToDelete);
					}else{
						return result = "Cannot delete all, not recurring task";
					}
				}else{
					
					retrievedListFromLogic = logicController.deleteFromArchive(taskToDelete);
					
				}
				result = "Deleted task from archive: " + taskToDelete.getDescription();
				currentArchives.clear();
				for(Task task : retrievedListFromLogic){
					currentArchives.add(task);
				}
			}else{
				result = "No tasks to delete";
			}
		}else{
			int taskIDFromUI = Integer.parseInt(splitInput[position+1]);
			if(!currentActiveTasks.isEmpty()){
				Task taskToDelete = currentActiveTasks.get(taskIDFromUI-1);
				ToDoSortedList retrievedListFromLogic = new ToDoSortedList();
	
				result = "Deleted task from current: " + taskToDelete.getDescription();
				if(isChangeAll){
					if(taskToDelete.isRecurrence()){
						retrievedListFromLogic =  logicController.deleteAllRecurringTask(taskToDelete);
					}else{
						return result ="Cannot delete all, not recurring task";
					}
				}else{
					retrievedListFromLogic = logicController.deleteTask(taskToDelete);
				}
				reflectChangeToCurrent(retrievedListFromLogic);
			}else{
				result = "No tasks to delete";
			}
		}
		
		return result;
	}


	String searchCommand(String[] splitInput) {
		// check input for what to search for (date/tag/priority/desc)
		String result = new String();
		String searchParameter = new String();
		try{
			searchParameter = splitInput[1];
		}catch (ArrayIndexOutOfBoundsException e){
			return result = "Please specify what to search for";
		}
		currentActiveTasks.clear();
		switch(searchParameter){
			case("today"):{
				//search by date, today's date
				Calendar today = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				currentActiveTasks = logicController.searchByDate(formatter.format(today));
				result = "Searched by date: today";
				break;
			}case("tmr"):{
				//search by date, tomorrow's date
				Calendar tomorrow = Calendar.getInstance();
				tomorrow.add(Calendar.DATE, 1);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				currentActiveTasks = logicController.searchByDate(formatter.format(tomorrow));
				result = "Searched by date: tomorrow";
				break;				
			}case("date"):{
				SimpleDateFormat dateInput = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat dateOutput = new SimpleDateFormat("yyyyMMdd");
				try {
					java.util.Date tempDate = dateInput.parse(splitInput[2]);
					String dateOutputString = dateOutput.format(tempDate);
					currentActiveTasks = logicController.searchByDate(dateOutputString);
					result = "Searched by date: " + splitInput[2];
				} catch (ParseException e) {
					return result = DATE_FORMAT_ERROR ;
				}
				break;
			}case("tag"):{
				currentActiveTasks = logicController.searchByTag(splitInput[2]);
				result = "Searched by tag: " + splitInput[2];
				break;
			}case("priority"):{
				int priority = Integer.parseInt(splitInput[2]);
				currentActiveTasks = logicController.searchByPriority(priority);
				result = "Searched by priority: " + splitInput[2];
				break;
			}
			
		}

		
		return result;
	}

	String addCommand(String[] input){
		
		//break the commands
		newMaxID = logicController.getSerialNumber() +1;
		String result = new String();
		String description = new String();
		int inputLength = input.length;
		int currentInputPoint = 2;
		int priority = -1;
		Date startTime = new Date();
		Date endTime = new Date();
		Date deadLine = new Date();
		boolean isGenericTask = true; //default is Generic Task
		boolean isDeadlineTask =false;
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
		
		for(int i=0;i<inputLength;i++){
			userInput.add(input[i]);
		}
		
		for(int i=1;i<userInput.size();i++){
			if(userInput.get(i).charAt(0)!='-'){
				description = description.concat(userInput.get(i)+" ");
				//descriptionArrayList.add(userInput.get(i));
			}else{
				break;
			}
		}
		description = description.trim();
		if(description.isEmpty()){
			return result = "No description added, try again";
		}
		
		if(userInput.contains("-priority")){
			//find position of info
			int point = userInput.indexOf("-priority");
			//check if info needed is specified
			try{
				priority = Integer.parseInt(userInput.get(point+1));
				userInput.remove(point+1);
			}catch(ArrayIndexOutOfBoundsException|NumberFormatException e){
				return result = "Error in priority given. Should be a number.";
			}
		}else if(userInput.contains("-p")){
			//find position of info
			int point = userInput.indexOf("-p");
			//check if info needed is specified
			try{
				priority = Integer.parseInt(userInput.get(point+1));
				userInput.remove(point+1);
			}catch(ArrayIndexOutOfBoundsException|NumberFormatException e){
				return result = "Error in priority given. Should be a number.";
			}
		}
		if(userInput.contains("-tags")){
			int point = userInput.indexOf("-tags");
			try{
				for(int j = point+1;j<userInput.size();j++){
					if(userInput.get(j).charAt(0)!='-'){
						tags.add(userInput.get(j));
					}else{
						break;
					}
				}
				for(int j=0;j<tags.size();j++){
					userInput.remove(tags.get(j));
				}
				
			}catch(ArrayIndexOutOfBoundsException e){
				return result = "Error in tags given.";
			}
		}else if(userInput.contains("-t")){
			int point = userInput.indexOf("-t");
			try{
				for(int j = point+1;j<userInput.size();j++){
					if(userInput.get(j).charAt(0)!='-'){
						tags.add(userInput.get(j));
					}else{
						break;
					}
				}
				for(int j=0;j<tags.size();j++){
					userInput.remove(tags.get(j));
				}
				
			}catch(ArrayIndexOutOfBoundsException e){
				return result = "Error in tags given.";
			}
		}
		
		if(userInput.contains("-by")){
			int point = userInput.indexOf("-by");		
			isGenericTask = false;
			isDeadlineTask = true;
			try{
				for(int j = point+1;j<userInput.size();j++){
					if(userInput.get(j).charAt(0)!='-'){
						dateAsString.add(userInput.get(j));
					}else{
						break;
					}
				}
				for(int j=0;j<dateAsString.size();j++){
					userInput.remove(dateAsString.get(j));
				}
			}catch(ArrayIndexOutOfBoundsException e){
				return result = "Error in date given.";
			}
		}else if(userInput.contains("-b")){
			int point = userInput.indexOf("-b");		
			isGenericTask = false;
			isDeadlineTask = true;
			try{
				for(int j = point+1;j<userInput.size();j++){
					if(userInput.get(j).charAt(0)!='-'){
						dateAsString.add(userInput.get(j));
					}else{
						break;
					}
				}
				for(int j=0;j<dateAsString.size();j++){
					userInput.remove(dateAsString.get(j));
				}
			}catch(ArrayIndexOutOfBoundsException e){
				return result = "Error in date given.";
			}
		}else if(userInput.contains("-on")){
			int point = userInput.indexOf("-on");		
			isGenericTask = false;
			isMeetingTask = true;
			try{
				for(int j = point+1;j<userInput.size();j++){
					if(userInput.get(j).charAt(0)!='-'){
						dateAsString.add(userInput.get(j));
					}else{
						break;
					}
				}
				for(int j=0;j<dateAsString.size();j++){
					userInput.remove(dateAsString.get(j));
				}
			}catch(ArrayIndexOutOfBoundsException e){
				return result = "Error in date given.";
			}
		}else if(userInput.contains("-o")){
			int point = userInput.indexOf("-o");		
			isGenericTask = false;
			isMeetingTask =true;
			try{
				for(int j = point+1;j<userInput.size();j++){
					if(userInput.get(j).charAt(0)!='-'){
						dateAsString.add(userInput.get(j));
					}else{
						break;
					}
				}
				for(int j=0;j<dateAsString.size();j++){
					userInput.remove(dateAsString.get(j));
				}
			}catch(ArrayIndexOutOfBoundsException e){
				return result = "Error in date given.";
			}
		}
		if(userInput.contains("-recurring")){
			if(isGenericTask){
				return result = "No date specified, cannot be recurring";
			}else{
				isRecurring = true;
				int point = userInput.indexOf("-recurring");
				try{
					if(userInput.get(point+1).charAt(0)!='-'){
						recurrenceNum = Integer.parseInt(userInput.get(point+1));
						recurringPeriod = userInput.get(point+2);
						userInput.remove(point+1);
						userInput.remove(point+1);
					}else{
						return result = "No recurring period stated";
					}
				}catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
					return result = "Error in command format. Try -recurring <num> <period>";
				}
			}
		}else if(userInput.contains("-r")){
			if(isGenericTask){
				return result = "No date specified, cannot be recurring";
			}else{
				isRecurring = true;
				int point = userInput.indexOf("-r");
				try{
					if(userInput.get(point+1).charAt(0)!='-'){
						recurrenceNum = Integer.parseInt(userInput.get(point+1));
						recurringPeriod = userInput.get(point+2);
						userInput.remove(point+1);
						userInput.remove(point+1);
					}else{
						return result = "No recurring period stated";
					}
				}catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
					return result = "Error in command format. Try -recurring <num> <period>";
				}
			}
		}

			

		
		
		//convert dates or time into Date object
		//----Meeting Task------
		// DD-MM-YYYY from HHmm to HHmm
		
		//----Deadline Task
		// DD-MM-YYYY
		
		if(!dateAsString.isEmpty()){
			
			//do deadline check first
			//deadline syntax options (-by) : dd MMMM yyyy at HHmm OR dd/MM/yyyy at HHmm 
			
			if(isDeadlineTask){
				deadLine = determineDeadline(dateAsString);
				if(deadLine == null){
					return DEADLINE_FORMAT_ERROR;
				}
				
			}else if(isMeetingTask){
				String positionCheck = "from";
				startTime = determineMeetingTime(positionCheck, dateAsString);
				positionCheck = "to";
				endTime = determineMeetingTime(positionCheck, dateAsString);
				if(startTime == null | endTime == null){
					return MEETINGTIME_FORMAT_ERROR;
				}	
			}
			
		}
		
		
		//parse the recurring portion
		
		if(recurringPeriod.length()>0){
			if(recurringPeriod.equalsIgnoreCase("weekly")){
				if(isDeadlineTask){
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.DATE,7);
				}else if(isMeetingTask){
					recurringTime = setPeriodOfOccurence(startTime,Calendar.DATE,7); 
				}
			}else if(recurringPeriod.equalsIgnoreCase("daily")){
				if(isDeadlineTask){
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.DATE,1); 
				}else if(isMeetingTask){
					recurringTime = setPeriodOfOccurence(startTime,Calendar.DATE,1); 
				}
			}else if(recurringPeriod.equalsIgnoreCase("hourly")){
				if(isDeadlineTask){
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.HOUR,1); 
				}else if(isMeetingTask){
					recurringTime = setPeriodOfOccurence(endTime,Calendar.HOUR,1); 
				}
			}else if(recurringPeriod.equalsIgnoreCase("monthly")){
				if(isDeadlineTask){
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.MONTH,1); 
				}else if(isMeetingTask){
					recurringTime = setPeriodOfOccurence(startTime,Calendar.MONTH,1); 
				}
			}else if(recurringPeriod.equalsIgnoreCase("yearly")){
				if(isDeadlineTask){
					recurringTime = setPeriodOfOccurence(deadLine,Calendar.YEAR,1); 
				}else if(isMeetingTask){
					recurringTime = setPeriodOfOccurence(startTime,Calendar.YEAR,1); 
				}
			}
		}
		
		//format for tasks: integer ID, String description, int priority, ArrayList<String> tags,archived
		if(isGenericTask){
		//floating task
			Task newTask = new Task(newMaxID,description, priority,tags);
			retrievedList = logicController.addTask(newTask);
		}else if(isDeadlineTask){
		//deadline task
			Task newDeadlineTask = new Task(newMaxID, description,deadLine, priority,tags);
			if(isRecurring){
				retrievedList = logicController.addRecurringTask(newDeadlineTask, recurringTime, recurrenceNum);
			}else{
				retrievedList = logicController.addTask(newDeadlineTask);
			}
		}else if(isMeetingTask){
		//meeting task
			Task newMeetingTask = new Task(newMaxID, description, startTime, endTime, priority, tags);
			if(isRecurring){
				retrievedList = logicController.addRecurringTask(newMeetingTask, recurringTime, recurrenceNum);
			}else{
				retrievedList = logicController.addTask(newMeetingTask);
			}
		}
		
		if(isGenericTask){
			result = "New task added: " + description;
		}else if(isDeadlineTask){
			result = "New task added with deadline: " + description;
		}else if(isMeetingTask){
			result = "New task added for meeting: " + description;
		}
		reflectChangeToCurrent(retrievedList);
		
		newMaxID+=recurrenceNum;
		logicController.setSerialNumber(newMaxID);
		return result;
	}




	private Date determineMeetingTime(String positionCheck,
			ArrayList<String> dateAsString) {
		//meeting syntax (-on): dd MMM yyyy from HHmm to HHmm OR dd/MM/yyyy from HHmm to HHmm
		int specifiedDay = 0;
		int specifiedMonth = 0;
		int specifiedYear = 0;
		int specifiedHour = 0;
		int specifiedMinutes = 0;
		boolean isSlashInputType = false; //i.e dd/MM/yyyy
		boolean isNormalInputType = false; //i.e dd MMMM yyyy
		Date meetingTime;
		/*if(!dateAsString.contains("from") | !dateAsString.contains("to")){
			return null;
		}*/
		if(dateAsString.get(0).contains("/")){
			isSlashInputType = true;
		}else{
			isNormalInputType = true;
		}
		if(isSlashInputType){
			//separate the fields
			String[] slashInput = dateAsString.get(0).split("/");
			specifiedDay = Integer.parseInt(slashInput[0]);
			specifiedMonth = Integer.parseInt(slashInput[1]);
			if(slashInput.length > 2){
				//if year is specified
				specifiedYear = Integer.parseInt(slashInput[2]);
			}else{
				Calendar today = Calendar.getInstance();
				specifiedYear = today.get(Calendar.YEAR);
			}
			
			int position = dateAsString.indexOf(positionCheck);
			specifiedHour = Integer.parseInt(dateAsString.get(position+1).substring(0, 2));
			specifiedMinutes = Integer.parseInt(dateAsString.get(position+1).substring(2,4));
			
			/*
			position = dateAsString.indexOf("to");
			specifiedHour = Integer.parseInt(dateAsString.get(position+1).substring(0, 2));
			specifiedMinutes = Integer.parseInt(dateAsString.get(position+1).substring(2,4));
			endTime = Date.getDayTime(specifiedYear, specifiedMonth, specifiedDay, specifiedHour, specifiedMinutes);
			*/
			
		}else if(isNormalInputType){
			specifiedDay = Integer.parseInt(dateAsString.get(0));
			specifiedMonth = getSpecifiedMonth(dateAsString,
					specifiedMonth);
			if(specifiedMonth ==0){
				return null;
			}
			int position = dateAsString.indexOf("from");
			if(position <3){
				//i.e year unspecified
				Calendar today = Calendar.getInstance();
				specifiedYear = today.get(Calendar.YEAR);
				System.out.println(specifiedYear);
			}else{
				specifiedYear = Integer.parseInt(dateAsString.get(2));
			}
			position = dateAsString.indexOf(positionCheck);
			specifiedHour = Integer.parseInt(dateAsString.get(position+1).substring(0, 2));
			specifiedMinutes = Integer.parseInt(dateAsString.get(position+1).substring(2,4));
			
			/*
			position = dateAsString.indexOf("to");
			specifiedHour = Integer.parseInt(dateAsString.get(position+1).substring(0, 2));
			specifiedMinutes = Integer.parseInt(dateAsString.get(position+1).substring(2,4));
			endTime = Date.getDayTime(specifiedYear, specifiedMonth, specifiedDay, specifiedHour, specifiedMinutes);
			*/
			
		}
		meetingTime = Date.getDayTime(specifiedYear, specifiedMonth, specifiedDay, specifiedHour, specifiedMinutes);
		return meetingTime;
	}

	private Date determineDeadline(ArrayList<String> dateAsString) {
		int specifiedDay = 0;
		int specifiedMonth = 0;
		int specifiedYear = 0;
		int specifiedHour = 0;
		int specifiedMinutes = 0;
		Date deadLine;
		boolean timeGiven = false;
		boolean isSlashInputType = false; //i.e dd/MM/yyyy
		boolean isNormalInputType = false; //i.e dd MMMM yyyy
		
		if(dateAsString.get(0).contains("/")){
			isSlashInputType = true;
		}else{
			isNormalInputType = true;
		}
		if(dateAsString.contains("at")){
			timeGiven = true;
		}else{
			timeGiven = false;
		}
		if(isSlashInputType){
			//separate the fields
			String[] slashInput = dateAsString.get(0).split("/");
			specifiedDay = Integer.parseInt(slashInput[0]);
			specifiedMonth = Integer.parseInt(slashInput[1])-1;
			if(slashInput.length > 2){
				//if year is specified
				specifiedYear = Integer.parseInt(slashInput[2]);
			}else{
				Calendar today = Calendar.getInstance();
				specifiedYear = today.get(Calendar.YEAR);
			}
			if(timeGiven){
				int position = dateAsString.indexOf("at");
				specifiedHour = Integer.parseInt(dateAsString.get(position+1).substring(0, 2));
				specifiedMinutes = Integer.parseInt(dateAsString.get(position+1).substring(2,4));
			}else{
				if(dateAsString.size()>1){
					//i.e time given, but no "at" dd/MM/yyyy HHmm -> syntax error, need "at"
					return null;
				}else{
					specifiedHour = 23;
					specifiedMinutes = 59;
				}
			}
		}else if(isNormalInputType){
			//get the specified values
			//separate the time from the date
			specifiedDay = Integer.parseInt(dateAsString.get(0));
			specifiedMonth = getSpecifiedMonth(dateAsString,
					specifiedMonth);
			if(specifiedMonth ==12){
				return null;
			}
			if(timeGiven){
				int position = dateAsString.indexOf("at");
				if(position<3){
					//no year specified
					Calendar today = Calendar.getInstance();
					specifiedYear = today.get(Calendar.YEAR);
				}else{
					//year specified
					specifiedYear = Integer.parseInt(dateAsString.get(2));
				}
				specifiedHour = Integer.parseInt(dateAsString.get(position+1).substring(0, 2));
				specifiedMinutes = Integer.parseInt(dateAsString.get(position+1).substring(2,4));
				
			}else{
				if(dateAsString.size()<3){
					//no year specified: dd MMM
					Calendar today = Calendar.getInstance();
					specifiedYear = today.get(Calendar.YEAR);
					
				}else{
					//dd MM yyyy OR dd MM HHmm
					specifiedYear = Integer.parseInt(dateAsString.get(2));
				}
				specifiedHour = 23;
				specifiedMinutes =59;
			}
		}
		deadLine = Date.getDayTime(specifiedYear, specifiedMonth, specifiedDay, specifiedHour, specifiedMinutes);
		return deadLine;
	}

	private int getSpecifiedMonth(ArrayList<String> dateAsString,
			int specifiedMonth) {
		switch(dateAsString.get(1).toLowerCase()){
			case("jan"):{
				specifiedMonth = 0;
				break;
			}case("feb"):{
				specifiedMonth = 1;
				break;
			}case("mar"):{
				specifiedMonth = 2;
				break;
			}case("apr"):{
				specifiedMonth = 3;
				break;
			}case("may"):{
				specifiedMonth = 4;
				break;
			}case("jun"):{
				specifiedMonth = 5;
				break;
			}case("jul"):{
				specifiedMonth = 6;
				break;
			}case("aug"):{
				specifiedMonth = 7;
				break;
			}case("sep"):{
				specifiedMonth = 8;
				break;
			}case("oct"):{
				specifiedMonth = 9;
				break;
			}case("nov"):{
				specifiedMonth = 10;
				break;
			}case("dec"):{
				specifiedMonth = 11;
				break;
			}
			case("january"):{
				specifiedMonth = 0;
				break;
			}case("february"):{
				specifiedMonth = 1;
				break;
			}case("march"):{
				specifiedMonth = 2;
				break;
			}case("april"):{
				specifiedMonth = 3;
				break;
			}case("june"):{
				specifiedMonth = 5;
				break;
			}case("july"):{
				specifiedMonth = 6;
				break;
			}case("august"):{
				specifiedMonth = 7;
				break;
			}case("september"):{
				specifiedMonth = 8;
				break;
			}case("october"):{
				specifiedMonth = 9;
				break;
			}case("november"):{
				specifiedMonth = 10;
				break;
			}case("december"):{
				specifiedMonth = 11;
				break;
			}default:{
				specifiedMonth = 12;
			}
		}
		return specifiedMonth;
	}
	private long setPeriodOfOccurence(Date dueDate, int type, int addValue) {
		
		Calendar nextOccurence = Calendar.getInstance();
		nextOccurence.setTime(dueDate);
		nextOccurence.add(type, addValue);
		return (nextOccurence.getTimeInMillis()-dueDate.getTime());
	}
}
