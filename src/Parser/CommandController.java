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
		System.out.println(test.executeCommand("-add recur -date 23/03/2015 -recurring 2 monthly"));
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
			currentActiveTasks.clear();
			for(Task task : retrievedCurrent){
				currentActiveTasks.add(task);
		}
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
			}
		}
		return result;
	}
	private String undoCommand() {
		boolean isSuccessful = logicController.undo();
		refreshCurrent();
		refreshArchive();
		String result = new String();
		if(isSuccessful){
			return result = "Undo successful";
		} else{
			return result = "Undo unsuccessful, maybe nothing to undo";
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
		String specifiedFileDirectory = splitInput[1];
		String acknowledgeCheck = storageController.setFileDirectory(specifiedFileDirectory);
		result = "File stored at: " + specifiedFileDirectory;
		return result;
	}


	String modifyCommand(String[] splitInput) {
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
							return result = MEETINGTIME_FORMAT_ERROR;
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
							return result = DEADLINE_FORMAT_ERROR;
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
						if(taskToChange.isRecurrence()){
							retrievedSortedList = logicController.editAlldeadline(taskToChange, newDeadline);
						}else{
							retrievedSortedList = logicController.editDeadline(taskToChange, newDeadline);
						}
						result = "Deadline changed";
					} catch (ParseException e) {
						return result = DEADLINE_FORMAT_ERROR;
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
						//TODO check this later
						if(taskToChange.isRecurrence()){
							retrievedSortedList = logicController.editAllStartTime(taskToChange, newStartTime);
							retrievedSortedList = logicController.editAllEndTime(taskToChange, newEndTime);
						}else{
							retrievedSortedList = logicController.editStartTime(taskToChange, newStartTime);
							retrievedSortedList = logicController.editEndTime(taskToChange, newEndTime);
						}
						result = "Meeting time changed";
						
					} catch (ParseException e) {
						return result = MEETINGTIME_FORMAT_ERROR;
					}
				}else{
					return result = "Type mismatch error, can't change date";
				}
				break;
			}case("priority"):{
				//syntax: -change priority <taskID> <new priority>
				int newPriority = Integer.parseInt(splitInput[3]);
				if(taskToChange.isRecurrence()){
					retrievedSortedList = logicController.editAllPriority(taskToChange, newPriority);
				}else{
					retrievedSortedList = logicController.editPriority(taskToChange, newPriority);
				}
				result = "Priority changed";
				break;
			}case("desc"):{
				//syntax: -change desc <taskID> <new desc>
				String newDescription = splitInput[3];
				if(taskToChange.isRecurrence()){
					retrievedSortedList = logicController.editAllDescription(taskToChange, newDescription);
				}else{
					retrievedSortedList = logicController.editDescription(taskToChange, newDescription);
				}
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


	String exitCommand() {
		String result = new String();
		logicController.exit(newMaxID);
		result = "HeyBuddy! is closing";
		return result;
	}


	String archiveCommand(String[] splitInput) {
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


	String deleteCommand(String[] splitInput) {
		//syntax : -delete <current/archive> [task ID]
		String result = new String();
		
		if(splitInput[1].equalsIgnoreCase("current")){
			int taskIDFromUI = Integer.parseInt(splitInput[2]);
			if(!currentActiveTasks.isEmpty()){
				Task taskToDelete = currentActiveTasks.get(taskIDFromUI-1);
				ToDoSortedList retrievedListFromLogic = new ToDoSortedList();
	
				result = "Deleted task from current: " + taskToDelete.getDescription();
				/*if(taskToDelete.isRecurrence()){
					retrievedListFromLogic =  logicController.deleteAllRecurringTask(taskToDelete);
				}else{
					retrievedListFromLogic = logicController.deleteTask(taskToDelete);
				}*/
				retrievedListFromLogic = logicController.deleteTask(taskToDelete);
				currentActiveTasks.clear();
				for(Task task : retrievedListFromLogic){
					currentActiveTasks.add(task);
				}
			}else{
				result = "No tasks to delete";
			}
		}else if(splitInput[1].equalsIgnoreCase("archive")){
			int taskIDFromUI = Integer.parseInt(splitInput[2]);
			if(!currentArchives.isEmpty()){
				Task taskToDelete = currentArchives.get(taskIDFromUI-1);
				result = "Deleted task from archive: " + taskToDelete.getDescription();
				ArchiveSortedList retrievedListFromLogic = logicController.deleteFromArchive(taskToDelete);
				currentArchives.clear();
				for(Task task : retrievedListFromLogic){
					currentArchives.add(task);
				}
			}else{
				result = "No tasks to delete";
			}
		}
		
		return result;
	}


	String searchCommand(String[] splitInput) {
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
		newMaxID = logicController.getSerialNumber();
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
		}
		if(userInput.contains("-date")){
			int point = userInput.indexOf("-date");		
			isGenericTask = false;
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
		}
			
		//need to loop to get the remaining inputs into descriptions
		/*if(inputLength>1){
			try{
				if(input[currentInputPoint].charAt(0)!='-'){
					description = description.concat(" " + input[currentInputPoint]).trim();
				}else{
					return result =  "No description added.";
				}
			}catch(ArrayIndexOutOfBoundsException e){
				return result = "No description added";
			}
		}*/
	
		
		
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
				return result = MEETINGTIME_FORMAT_ERROR;
			}
				
		}else if(dateAsString.size()==1){
		
			isDeadlineTask = true;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				java.util.Date tempDate = dateFormat.parse(dateAsString.get(0));
				deadLine.setTime(tempDate.getTime());
				
			} catch (ParseException e) {
				
				return result = DEADLINE_FORMAT_ERROR;
	
			}
		}else if(dateAsString.size()==2){
		
			isDeadlineTask = true;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HHmm");
			try {
				java.util.Date tempDate = dateFormat.parse(dateAsString.get(0)+ " " + dateAsString.get(1));
				deadLine.setTime(tempDate.getTime());
				
			} catch (ParseException e) {
				
				return result = DEADLINE_FORMAT_ERROR;
	
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
		
		result = "New task added: " + description;
		//clear the locally stored tasklist to add the new results
		currentActiveTasks.clear();

		//convert ToDoSortedList from logicController into an ArrayList of String
		for(Task task : retrievedList){
			currentActiveTasks.add(task);
		}
		
		newMaxID++;
		logicController.setSerialNumber(newMaxID);
		return result;
	}
	private long setPeriodOfOccurence(Date dueDate, int type, int addValue) {
		
		Calendar nextOccurence = Calendar.getInstance();
		nextOccurence.setTime(dueDate);
		//System.out.println("1-1-1-1"+nextOccurence.getTime());
		nextOccurence.add(type, addValue);
		//System.out.println("1-1-1-1"+nextOccurence.getTime());
		return (nextOccurence.getTimeInMillis()-dueDate.getTime());
	}
}
