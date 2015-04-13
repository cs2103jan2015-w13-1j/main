// @author A0110571W

package Command;

public class Parser {
	
	//private static CommandController commandController = new CommandController();

	public int parseIn(String command) {

		
		String[] splitCommand = command.split(" ");
		String firstCommand = splitCommand[0];
		int commandNum=0;
		
		commandNum = commandCheck(firstCommand,splitCommand);
		return commandNum;
		
	}	
	private int commandCheck(String command, String[] splitInput) {
	
		int commandNum = 0;
		switch(command){
			case("add"):{
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
			}case("import"):{
				commandNum = 13;
				break;
			}case("export"):{
				commandNum = 14;
				break;
			}case("redo"):{
				commandNum = 15;
				break;
			}case("unarchive"):{
				commandNum = 16;
				break;
			}
		}
		
		return commandNum;
		
	}	

	
}
