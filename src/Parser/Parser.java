package Parser;


public class Parser {
	
	private static CommandController commandController = new CommandController();
	
	/*
	public static void main(String[] args){
		
		Parser parser = new Parser();
		//parser.initialiseTasks();
		System.out.println(parser.parseIn("-add this generic task"));
		//System.out.println(parser.returnTasks());
		System.out.println(parser.parseIn("-add second generic task"));
		//System.out.println(parser.returnTasks());
		
		
	}
	*/



	public int parseIn(String command) {

		
		String[] splitCommand = command.split(" ");
		String firstCommand = splitCommand[0];
		int commandNum=0;
		
		commandNum = commandCheck(firstCommand,splitCommand);
		//check first word for command
		/*if(firstCommand.charAt(0)==''){
			//command found
			
		}*/
		
		return commandNum;
		
	}

	
	
	private int commandCheck(String command, String[] splitInput) {
	
		int commandNum = 0;
		switch(command){
			case("add"):{
				//run "add" functions
				//return to commandController what command to execute
				//commandController.result(integer)
				commandNum = 1;
				break;
			}case("search"):{
				commandNum = 2;
				break;
			}case("delete"):{
				commandNum = 3;
				break;
			}case("archive"):{
				commandNum = 4;
				break;
			}case("exit"):{
				commandNum = 5;
				break;
			}case("change"):{
				commandNum = 6;
				break;
			}case("directory"):{
				commandNum = 7;
				break;
			}case("refresh"):{
				commandNum = 8;
				break;
			}case("undo"):{
				commandNum = 9;
				break;
			}case("addtag"):{
				commandNum = 10;
				break;
			}case("removetag"):{
				commandNum = 11;
				break;
			}case("sort"):{
				commandNum = 12;
				break;
			}
			default:
				System.out.println("Invalid command");
		}
		
		return commandNum;
		
	}
	
	

	
}
