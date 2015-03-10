package Parser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Common.ArchiveSortedList;
import Common.Date;
import Common.Task;
import Common.ToDoSortedList;
import Logic.LogicController;
import Storage.StorageController;

public class Parser implements InterfaceForParser {
	
	private static LogicController logicController = new LogicController();
	private static StorageController storageController = new StorageController();
	

	public int newMaxID = 0; 
	private ArrayList<Task> currentActiveTasks = new ArrayList<Task>();
	private ArrayList<Task> currentArchives = new ArrayList<Task>();

	
	public static void main(String[] args){
		/*
		Parser parser = new Parser();
		parser.initialiseTasks();
		System.out.println(parser.parseIn("-add this generic task"));
		System.out.println(parser.returnTasks());
		System.out.println(parser.parseIn("-add second generic task"));
		System.out.println(parser.returnTasks());*/
		
	}

	
	public ArrayList<Task> initialiseTasks(){
		logicController.initialise();
		newMaxID = logicController.getSerialNumber();
		//Upon application start-up, fetch the current tasklist
		ToDoSortedList retrievedCurrent = new ToDoSortedList();
		
		/*
		//Dummy data
		ArrayList<String> dummyTags = new ArrayList<String>();
		SimpleDateFormat dummyDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date tempDummyDate = new Date();
		Date dummyDate = new Date();
		dummyDate.setTime(tempDummyDate.getTime());

		
		taskListForUI.add(new Task(1,"test generic",-1,dummyTags));
		taskListForUI.add(new Task(2,"test deadline", dummyDate,-1,dummyTags));
		taskListForUI.add(new Task(3,"test meeting", dummyDate, dummyDate, -1, dummyTags));
		*/
		
		retrievedCurrent = logicController.viewActiveTasks();
		currentActiveTasks.clear();
		for(Task task : retrievedCurrent){
			currentActiveTasks.add(task);
		}
		
		return currentActiveTasks;		
		
	}
	
	public ArrayList<Task> initialiseArchives(){
		//Upon application start-up, fetch the archived tasks to display
		ArchiveSortedList retrievedArchiveFromLogic = new ArchiveSortedList();
		
		/*
		//Dummy Data
		ArrayList<String> dummyTags = new ArrayList<String>();
		SimpleDateFormat dummyDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date tempDummyDate = new Date();
		Date dummyDate = new Date();
		dummyDate.setTime(tempDummyDate.getTime());

		
		archiveListForUI.add(new Task(1,"test generic",-1,dummyTags));
		archiveListForUI.add(new Task(2,"test deadline", dummyDate,-1,dummyTags));
		archiveListForUI.add(new Task(3,"test meeting", dummyDate, dummyDate, -1, dummyTags));
		*/
		
		currentArchives.clear();
		retrievedArchiveFromLogic = logicController.viewArchiveTasks();
		for(Task task : retrievedArchiveFromLogic){
			currentArchives.add(task);
		}
		
		return currentArchives;		
	}
	
	public ArrayList<Task> returnTasks(){

		return currentActiveTasks;
	}
	
	public ArrayList<Task> returnArchive(){
		
		ArchiveSortedList retrievedArchiveFromLogic = new ArchiveSortedList();

		retrievedArchiveFromLogic = logicController.viewArchiveTasks();
		currentArchives.clear();
		for(Task task : retrievedArchiveFromLogic){
			currentArchives.add(task);
		}
		
		return currentArchives;	
		

	}

	public String parseIn(String command) {

		
		String[] splitCommand = command.split(" ");
		String firstCommand = splitCommand[0];
		String result = new String();
		
		
		//check first word for command
		if(firstCommand.charAt(0)=='-'){
			//command found
			result = commandCheck(firstCommand,splitCommand);
		}
		
		return result;
		
	}

	private String commandCheck(String command, String[] splitInput) {
	
		String result = new String();
		switch(command){
			case("-add"):{
				//run "add" functions
				result = addCommand(splitInput);
				break;
				//look for next commands
				//break;
			}case("-search"):{
				result = searchCommand(splitInput);
				break;
			}case("-delete"):{
				result = deleteCommand(splitInput);
				break;
			}case("-archive"):{
				result = archiveCommand(splitInput);
				break;
			}case("-exit"):{
				result = exitCommand();
				break;
			}case("-change"):{
				result = modifyCommand(splitInput);
				break;
			}case("-directory"):{
				result = fileDirectoryCommand(splitInput);
				break;
			}case("-refresh"):{
				result = refreshCommand();
				break;
			}
			default:
				System.out.println("Invalid command");
		}
		
		return result;
		
	}
	
	private String refreshCommand() {
		String result = "Display refreshed to current tasks";
		ToDoSortedList retrievedCurrent = logicController.viewActiveTasks();
		currentActiveTasks.clear();
		for(Task task : retrievedCurrent){
			currentActiveTasks.add(task);
		}
		
		return null;
	}


	private String fileDirectoryCommand(String[] splitInput) {
		String result = new String();
		String specifiedFileDirectory = splitInput[1];
		String acknowledgeCheck = storageController.setFileDirectory(specifiedFileDirectory);
		result = "File stored at: " + specifiedFileDirectory;
		return result;
	}


	private String modifyCommand(String[] splitInput) {
		String result = new String();
		String modifyParameter = splitInput[1];
		ToDoSortedList retrievedSortedList = new ToDoSortedList();
		int taskID = Integer.parseInt(splitInput[2])-1;
		Task taskToChange = currentActiveTasks.get(taskID);
		
		switch(modifyParameter){
			case("date"):{
				//find task ID, get task
				String taskType = taskToChange.getType();
				//cases: generic task, deadline task, meeting task 
				if(taskType.equalsIgnoreCase("generic")){
					//i.e no date exists
					//parse in the new deadline/startend time
					if(splitInput.length>4){
						//if change to meeting task
						//syntax: -change date <taskID> dd/MM/yyyy HHmm HHmm
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
						String startTimeString = splitInput[3] + " " + splitInput[4];
						String endTimeString = splitInput[3] + " " + splitInput[5];
						try {
							java.util.Date tempStart = formatter.parse(startTimeString);
							Date newStartTime = new Date();
							newStartTime.setTime(tempStart.getTime());
							java.util.Date tempEnd = formatter.parse(endTimeString);
							Date newEndTime = new Date();
							newEndTime.setTime(tempEnd.getTime());
							retrievedSortedList = logicController.addStartAndEndTime(taskToChange, newStartTime, newEndTime);
							result = "New start and end time added";
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
					}else{
						//if change to deadline task
						//syntax: -change date dd/MM/yyyy
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						try {
							java.util.Date tempDate = formatter.parse(splitInput[3]);
							Date newDeadline = new Date();
							newDeadline.setTime(tempDate.getTime());
							retrievedSortedList = logicController.addDeadLine(taskToChange, newDeadline);
							result = "New deadline added";
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else if(taskType.equalsIgnoreCase("deadline")){
					//date exists, change deadline
					//syntax: -change date dd/MM/yyyy
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					try {
						java.util.Date tempDate = formatter.parse(splitInput[3]);
						Date newDeadline = new Date();
						newDeadline.setTime(tempDate.getTime());
						retrievedSortedList = logicController.editDeadline(taskToChange, newDeadline);
						result = "Deadline changed";
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(taskType.equalsIgnoreCase("meeting")){
					//change start and end time
					//syntax:-change date dd/MM/yyyy HHmm HHmm
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
					String startTimeString = splitInput[3] + " " + splitInput[4];
					String endTimeString = splitInput[3] + " " + splitInput[5];
					try {
						java.util.Date tempStart = formatter.parse(startTimeString);
						Date newStartTime = new Date();
						newStartTime.setTime(tempStart.getTime());
						java.util.Date tempEnd = formatter.parse(endTimeString);
						Date newEndTime = new Date();
						newEndTime.setTime(tempEnd.getTime());
						//change this later
						retrievedSortedList = logicController.editStartTime(taskToChange, newStartTime);
						result = "Meeting time changed";
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					result = "type mismatch error, can't change date";
				}
				break;
			}case("priority"):{
				//syntax: -change priority <taskID> <new priority>
				int newPriority = Integer.parseInt(splitInput[3]);
				retrievedSortedList = logicController.editPriority(taskToChange, newPriority);
				result = "Priority changed";
				break;
			}case("desc"):{
				//syntax: -change desc <taskID> <new desc>
				String newDescription = splitInput[3];
				retrievedSortedList = logicController.editDescription(taskToChange, newDescription);
				result = "Description changed";
				break;
			}
		}
		
		//update current tasks
		currentActiveTasks.clear();
		for(Task task : retrievedSortedList){
			currentActiveTasks.add(task);
		}
		
		
		return result;
	}


	private String exitCommand() {
		String result = new String();
		logicController.exit(newMaxID);
		result = "HeyBuddy! is closing";
		return result;
	}


	private String archiveCommand(String[] splitInput) {
		//syntax -archive [task ID]
		String result = new String();
		int taskIDFromUI = Integer.parseInt(splitInput[1]);


		if(!currentActiveTasks.isEmpty()){
			Task taskToArchive = currentActiveTasks.get(taskIDFromUI-1);

			result = "Task moved to archive: " + taskToArchive.getDescription();
			Date currentTime = new Date();
			ToDoSortedList retrievedActiveTaskList = logicController.moveToArchive(taskToArchive, currentTime);
			currentActiveTasks.clear();
			for(Task task : retrievedActiveTaskList){
				currentActiveTasks.add(task);
			}
		}else{
			result = "No tasks to archive";
		}
		
		return result;
	}


	private String deleteCommand(String[] splitInput) {
		//syntax : -delete [task ID]
		String result = new String();

		int taskIDFromUI = Integer.parseInt(splitInput[1]);
		if(!currentActiveTasks.isEmpty()){
			Task taskToDelete = currentActiveTasks.get(taskIDFromUI-1);

			result = "Deleted task: " + taskToDelete.getDescription();
			ToDoSortedList retrievedListFromLogic = logicController.deleteTask(taskToDelete);
			currentActiveTasks.clear();
			System.out.println(retrievedListFromLogic);
			for(Task task : retrievedListFromLogic){
				currentActiveTasks.add(task);
			}
		}else{
			result = "No tasks to delete";
		}
		
		return result;
	}


	private String searchCommand(String[] splitInput) {
		// check input for what to search for (date/tag/priority/desc)
		String result = new String();
		String searchParameter = splitInput[1];
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
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	private String addCommand(String[] input){
		
		//break the commands
		String result = new String();
		String description = input[1];
		int inputLength = input.length;
		int currentInputPoint = 2;
		int priority = -1;
		Date startTime = new Date();
		Date endTime = new Date();
		Date deadLine = new Date();
		boolean isGenericTask = true; //default is Generic Task
		boolean isDeadlineTask =false;
		boolean isMeetingTask = false;
		ToDoSortedList retrievedList = new ToDoSortedList();
		ArrayList<String> tags = new ArrayList<String>();
		ArrayList<String> dateAsString = new ArrayList<String>();
		
		//date, priority and tags optional
		//look through remaining input for more commands
		for(int i =currentInputPoint; i<inputLength;i++){
			if(input[i].equalsIgnoreCase("-priority")){
				priority = Integer.parseInt(input[i+1]);
			}else if(input[i].equalsIgnoreCase("-tags")){
				int pointAfterTag = i+1;
				for(int j=pointAfterTag;j<inputLength;j++){
					if(input[j].charAt(0)!='-'){
						tags.add(input[j]);
					}
				}
				break;
				
			}else if(input[i].equalsIgnoreCase("-date")){

				int pointAfterTag=i+1;
				for(int j=pointAfterTag;j<inputLength;j++){
					if(input[j].charAt(0)!='-'){
						dateAsString.add(input[j]);
					}
				}
				isGenericTask = false;
				break;
			}else{
				//i.e no other command, add the rest as description
				description = description.concat(" " + input[i]).trim();
			}
		}
		
		//convert dates or time into Date object
		//----Meeting Task------
		// DD-MM-YYYY from HHmm to HHmm
		
		//----Deadline Task
		// DD-MM-YYYY

		if(dateAsString.size() == 3){
			//cannot be more than 2
			//start and end time
			isMeetingTask = true;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HHmm");
			String startTimeString = dateAsString.get(0) + " " + dateAsString.get(1);
			String endTimeString = dateAsString.get(0) + " " + dateAsString.get(2);

			try {
				java.util.Date tempDate = dateFormat.parse(startTimeString);
				startTime.setTime(tempDate.getTime());
				tempDate = dateFormat.parse(endTimeString);
				endTime.setTime(tempDate.getTime());
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}else if(dateAsString.size()==1){
		
			isDeadlineTask = true;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				java.util.Date tempDate = dateFormat.parse(dateAsString.get(0));
				deadLine.setTime(tempDate.getTime());
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			retrievedList = logicController.addTask(newDeadlineTask);
		}else if(isMeetingTask){
		//meeting task
			Task newMeetingTask = new Task(newMaxID, description, startTime, endTime, priority, tags);
			retrievedList = logicController.addTask(newMeetingTask);
		}
		
		result = "New task added: " + description;
		//clear the locally stored tasklist to add the new results
		currentActiveTasks.clear();

		//convert ToDoSortedList from logicController into an ArrayList of String
		for(Task task : retrievedList){
			currentActiveTasks.add(task);
		}
		
		newMaxID++;
		return result;
	}


	
}
