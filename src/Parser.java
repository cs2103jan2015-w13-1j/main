import basicElements.*;

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
			commandCheck(firstCommand);
			passCheck = true;
		}
		
		return passCheck;
		
	}

	private void commandCheck(String string) {
		// TODO Auto-generated method stub
		switch(string){
			case("-goto"):{
				//look at next elements in string
				break;
			}
			case("-add"):{
				//run "add" functions
				addCommand(string);
				//look for next commands
				break;
			}
		}
		
	}
	
	private void addCommand(String command){
		//get date to generate ID
		Task newTask = new Task(newMaxID,);
		//break the commands
		newMaxID++;
	}
	
	
}
