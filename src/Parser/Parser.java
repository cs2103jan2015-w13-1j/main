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

public class Parser implements InterfaceForParser {
	
	private static LogicController logicController = new LogicController();
	
	public int newMaxID = 0; //logicController.getMaxID()+1;
	private ArrayList<Task> retrievedTasks = new ArrayList<Task>();
	private ArrayList<Task> retrievedArchives = new ArrayList<Task>();
	
	public static void main(String[] args){
		Parser parser = new Parser();
		parser.initialiseTasks();
		System.out.println(parser.parseIn("-add this generic task"));
		System.out.println(parser.returnTasks());
		
	}

	
	public ArrayList<Task> initialiseTasks(){
		logicController.initialise();
		//Upon application start-up, fetch the current tasklist
		ToDoSortedList retrievedCurrent = new ToDoSortedList();
		ArrayList<Task> taskListForUI = new ArrayList<Task>();
		
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
		for(Task task : retrievedCurrent){
			taskListForUI.add(task);
		}
		
		return taskListForUI;		
		
	}
	
	public ArrayList<Task> initialiseArchives(){
		//Upon application start-up, fetch the archived tasks to display
		ArchiveSortedList retrievedArchiveFromLogic = new ArchiveSortedList();
		ArrayList<Task> archiveListForUI = new ArrayList<Task>();
		
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
		
		
		retrievedArchiveFromLogic = logicController.viewArchiveTasks();
		for(Task task : retrievedArchiveFromLogic){
			archiveListForUI.add(task);
		}
		
		return archiveListForUI;		
	}
	
	public ArrayList<Task> returnTasks(){

		return retrievedTasks;
	}
	
	public ArrayList<Task> returnArchive(){
		
		ArchiveSortedList retrievedArchiveFromLogic = new ArchiveSortedList();
		ArrayList<Task> archiveListForUI = new ArrayList<Task>();

		retrievedArchiveFromLogic = logicController.viewArchiveTasks();
		for(Task task : retrievedArchiveFromLogic){
			archiveListForUI.add(task);
		}
		
		return archiveListForUI;	
		

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
			}
			default:
				System.out.println("Invalid command");
		}
		
		return result;
		
	}
	
	private String archiveCommand(String[] splitInput) {
		//syntax -archive [task ID]
		String result = new String();
		int taskIDFromUI = Integer.parseInt(splitInput[1]);
		Task taskToArchive = retrievedArchives.get(taskIDFromUI);
		result = "Task moved to archive: " + taskToArchive.getDescription();
		Date currentTime = new Date();
		ToDoSortedList retrievedActiveTaskList = logicController.moveToArchive(taskToArchive, currentTime);
		retrievedTasks.clear();
		for(Task task : retrievedActiveTaskList){
			retrievedTasks.add(task);
		}
		
		return result;
	}


	private String deleteCommand(String[] splitInput) {
		//syntax : -delete [task ID]
		String result = new String();
		int taskIDFromUI = Integer.parseInt(splitInput[1]);
		Task taskToDelete = retrievedTasks.get(taskIDFromUI);
		result = "Deleted task: " + taskToDelete.getDescription();
		ToDoSortedList retrievedListFromLogic = logicController.deleteTask(taskToDelete);
		retrievedTasks.clear();
		for(Task task : retrievedListFromLogic){
			retrievedTasks.add(task);
		}
		
		return result;
	}


	private String searchCommand(String[] splitInput) {
		// check input for what to search for (date/tag/priority/desc)
		String result = new String();
		String searchParameter = splitInput[1];
		retrievedTasks.clear();
		switch(searchParameter){
			case("today"):{
				//search by date, today's date
				Calendar today = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				retrievedTasks = logicController.searchByDate(formatter.format(today));
				result = "Searched by date: today";
				break;
			}case("tmr"):{
				//search by date, tomorrow's date
				Calendar tomorrow = Calendar.getInstance();
				tomorrow.add(Calendar.DATE, 1);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				retrievedTasks = logicController.searchByDate(formatter.format(tomorrow));
				result = "Searched by date: tomorrow";
				break;				
			}case("date"):{
				SimpleDateFormat dateInput = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat dateOutput = new SimpleDateFormat("yyyyMMdd");
				try {
					java.util.Date tempDate = dateInput.parse(splitInput[2]);
					String dateOutputString = dateOutput.format(tempDate);
					retrievedTasks = logicController.searchByDate(dateOutputString);
					result = "Searched by date: " + splitInput[2];
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}case("tag"):{
				retrievedTasks = logicController.searchByTag(splitInput[2]);
				result = "Searched by tag: " + splitInput[2];
				break;
			}case("priority"):{
				int priority = Integer.parseInt(splitInput[2]);
				retrievedTasks = logicController.searchByPriority(priority);
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
		retrievedTasks.clear();
		/*
		//Dummy Data
		ArrayList<String> dummyTags = new ArrayList<String>();
		SimpleDateFormat dummyDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date tempDummyDate = new Date();
		Date dummyDate = new Date();
		dummyDate.setTime(tempDummyDate.getTime());

		
		retrievedTasks.add(new Task(1,"test generic",-1,dummyTags));
		retrievedTasks.add(new Task(2,"test deadline", dummyDate,-1,dummyTags));
		retrievedTasks.add(new Task(3,"test meeting", dummyDate, dummyDate, -1, dummyTags));
		*/
		
		//convert ToDoSortedList from logicController into an ArrayList of String
		for(Task task : retrievedList){
			retrievedTasks.add(task);
		}
		newMaxID++;
		return result;
	}


	
}
