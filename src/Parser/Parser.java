package Parser;


public class Parser {
	
	private static CommandController commandController = new CommandController();
	
	/*
	public static void main(String[] args){
		
		Parser parser = new Parser();
		parser.initialiseTasks();
		System.out.println(parser.parseIn("-add this generic task"));
		System.out.println(parser.returnTasks());
		System.out.println(parser.parseIn("-add second generic task"));
		System.out.println(parser.returnTasks());
		
		
	}
	*/



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
				result = commandController.addCommand(splitInput);
				break;
			}case("-search"):{
				result = commandController.searchCommand(splitInput);
				break;
			}case("-delete"):{
				result = commandController.deleteCommand(splitInput);
				break;
			}case("-archive"):{
				result = commandController.archiveCommand(splitInput);
				break;
			}case("-exit"):{
				result = commandController.exitCommand();
				break;
			}case("-change"):{
				result = commandController.modifyCommand(splitInput);
				break;
			}case("-directory"):{
				result = commandController.fileDirectoryCommand(splitInput);
				break;
			}case("-refresh"):{
				result = commandController.refreshCommand();
				break;
			}
			default:
				System.out.println("Invalid command");
		}
		
		return result;
		
	}
	
	

	
}
