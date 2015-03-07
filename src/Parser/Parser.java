package Parser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Common.ArchiveSortedList;
import Common.Date;
import Common.Task;
import Common.ToDoSortedList;
import Logic.LogicController;

public class Parser implements InterfaceForParser {
	
	private static LogicController logicController = new LogicController();
	
	public int newMaxID = 0; //logicController.getMaxID()+1;

	public static void main(String[] args){
		Parser run = new Parser();
		//read in command from ui
		//get current max ID
		
		run.parseIn("-add this generic task");
		run.parseIn("-add this deadline task -date 7/3/2015");
		run.parseIn("-add this meeting task -date 7/3/2015 1200 1300");
	}
	
	public ArrayList<Task> initialiseTasks(){
		//Upon application start-up, fetch the current tasklist
		ToDoSortedList retrievedCurrent = new ToDoSortedList();
		ArrayList<Task> taskListForUI = new ArrayList<Task>();
		
		retrievedCurrent = logicController.viewActiveTasks();
		for(Task task : retrievedCurrent){
			taskListForUI.add(task);
		}
		
		return taskListForUI;
	}
	
	public ArrayList<Task> initialiseArchives(){
		//Upon application start-up, fetch the archived tasks to display
		ArchiveSortedList retrievedArchive = new ArchiveSortedList();
		ArrayList<Task> archiveListForUI = new ArrayList<Task>();
		
		retrievedArchive = logicController.viewArchiveTasks();
		for(Task task : retrievedArchive){
			archiveListForUI.add(task);
		}
		
		return archiveListForUI;
	}

	public ArrayList<Task> parseIn(String command) {
		//logicController.initialise();
		
		String[] splitCommand = command.split(" ");
		String firstCommand = splitCommand[0];
		ArrayList<Task> result = new ArrayList<Task>();
		
		
		//check first word for command
		if(firstCommand.charAt(0)=='-'){
			//command found
			result = commandCheck(firstCommand,splitCommand);
		}
		
		return result;
		
	}

	private ArrayList<Task> commandCheck(String command, String[] splitInput) {
	
		ArrayList<Task> result = new ArrayList<Task>();
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
			}
			default:
				System.out.println("Invalid command");
		}
		
		return result;
		
	}
	
	private ArrayList<Task> searchCommand(String[] splitInput) {
		// check input for what to search for (date/tag/priority/desc)
		int inputLength = splitInput.length;
		for(int i=1;i<inputLength;i++){
			
		}
		return null;
	}

	private ArrayList<Task> addCommand(String[] input){
		
		//break the commands
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
		ArrayList<Task> taskListForUI = new ArrayList<Task>();
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
		

		
		//convert ToDoSortedList from logicController into an ArrayList of String
		for(Task task : retrievedList){
			taskListForUI.add(task);
		}
		newMaxID++;
		return taskListForUI;
	}

	@Override
	public ArrayList<Task> returnTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Task> returnArchive() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
