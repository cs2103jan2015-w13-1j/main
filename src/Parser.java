import basicElements.*;
import basicElements.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import treeSets.*;

public class Parser {
	
	LogicController logicController = new LogicController();
	public int newMaxID = 0; //logicController.getMaxID()+1;

	public static void main(String[] args){
		Parser run = new Parser();
		//read in command from ui
		//get current max ID
		String command = "-add this is a test command";
		run.parseIn(command);
	}

	public ArrayList<String> parseIn(String command) {
		
		String[] splitCommand = command.split(" ");
		String firstCommand = splitCommand[0];
		ArrayList<String> result = new ArrayList<String>();
		
		//instead of searching whole string for command, maybe search only first
		//word for command, then search sequentially down the string
		
		//check first word for command
		if(firstCommand.charAt(0)=='-'){
			//command found
			result = commandCheck(firstCommand,splitCommand);
		}
		
		return result;
		
	}

	private ArrayList<String> commandCheck(String command, String[] splitInput) {
	
		ArrayList<String> result = new ArrayList<String>();
		switch(command){
			case("-goto"):{
				//look at next elements in string
				break;
			}
			case("-add"):{
				//run "add" functions
				result = addCommand(splitInput);
				break;
				//look for next commands
				//break;
			}
			default:
				System.out.println("Invalid command");
		}
		
		return result;
		
	}
	
	private ArrayList<String> addCommand(String[] input){
		
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
		ArrayList<String> taskListForUI = new ArrayList<String>();
		ArrayList<String> dateAsString = new ArrayList<String>();
		
		//date, priority and tags optional
		//look through remaining input for more commands
		for(int i =currentInputPoint; i<inputLength;i++){
			if(input[i] == "-priority"){
				priority = Integer.parseInt(input[i+1]);
			}else if(input[i] == "-tags"){
				int pointAfterTag = i+1;
				while(input[pointAfterTag].charAt(0)!= '-'){
					tags.add(input[pointAfterTag]);
					pointAfterTag++;
				}
			}else if(input[i]=="-date"){

				int pointAfterTag=i+1;
				while(input[pointAfterTag].charAt(0)!='-'){
					dateAsString.add(input[pointAfterTag]);
					pointAfterTag++;
				}
				isGenericTask = false;
			}
		}
		
		//convert dates or time into Date object
		//if have two dates, its a meeting task with start and end date
		//else its a deadline task, with deadline date
		if(dateAsString.size() == 2){
			//cannot be more than 2
			//start and end time
			isMeetingTask = true;
			SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
			try {
				startTime = (Date) timeFormat.parse(dateAsString.get(0));
				endTime = (Date) timeFormat.parse(dateAsString.get(1));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}else{
		
			isDeadlineTask = true;
			SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
			try {
				deadLine = (Date) dateFormat.parse(dateAsString.get(0));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//format for tasks: integer ID, String description, int priority, ArrayList<String> tags,archived
		if(isGenericTask){
		//floating task
		Task newTask = new Task(newMaxID,description, priority,tags,false);
		//retrievedList = logicController.addTask(newTask);
		}else if(isDeadlineTask){
		//deadline task
		Task newDeadlineTask = new DeadlineTask(newMaxID, description,deadLine, priority,tags,false);
		//retrievedList = logicController.addTask(newDeadlineTask);
		}else if(isMeetingTask){
		//meeting task
		Task newMeetingTask = new MeetingTask(newMaxID, description,startTime,endTime, priority,tags,false);
		//retrievedList = logicController.addTask(newMeetingTask);
		}
		

		
		//convert ToDoSortedList from logicController into an ArrayList of String
		for(Task task : retrievedList){
			taskListForUI.add(task.toString());
		}
		newMaxID++;
		return taskListForUI;
	}
	
	
}
