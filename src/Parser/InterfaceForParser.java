package Parser;

import java.util.ArrayList;
import Common.Task;

public interface InterfaceForParser {
	
	public ArrayList<Task> initialiseTasks();
	public ArrayList<Task> initialiseArchives();
	public String executeCommand(String command);
	public ArrayList<Task> returnTasks();
	public ArrayList<Task> returnArchive();
	
}
