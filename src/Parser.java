import basicElements.*;
import java.util.*;

public class Parser {
	
	LogicController logicController = new LogicController();
	public int newMaxID = 0; //logicController.getMaxID()+1;

	public void main(String[] args){
		//read in command from ui
		//get current max ID
		String command = "-this is a test command";
		parseIn(command);
	}

	private boolean parseIn(String command) {
		
		String[] splitCommand = command.split(" ");
		String firstCommand = splitCommand[0];
	
		boolean passCheck = false;
		
		//instead of searching whole string for command, maybe search only first
		//word for command, then search sequentially down the string
		
		//check first word for command
		if(firstCommand.charAt(0)=='-'){
			//command found
			commandCheck(firstCommand,splitCommand);
			passCheck = true;
		}
		
		return passCheck;
		
	}

	private void commandCheck(String command, String[] splitInput) {
		// TODO Auto-generated method stub
		switch(command){
			case("-goto"):{
				//look at next elements in string
				break;
			}
			case("-add"):{
				//run "add" functions
				addCommand(splitInput);
				//look for next commands
				break;
			}
		}
		
	}
	
	private void addCommand(String[] input){
		
		//break the commands
		String description = input[1];
		int inputLength = input.length;
		int currentInputPoint = 2;
		int priority = -1;
		ArrayList<String> tags = new ArrayList<String>();
		
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
			}
		}
		
		//format for tasks: int ID, String description, int priority, ArrayList<String> tags
		Task newTask = new Task(newMaxID,description, priority,tags);
		//logicController.addTask(newTask);
		newMaxID++;
	}
	
	
}
