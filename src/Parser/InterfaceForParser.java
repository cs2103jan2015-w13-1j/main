package Parser;

import java.util.ArrayList;
import Common.Task;

public interface InterfaceForParser {
	
	public ArrayList<Task> initialiseTasks();
	public ArrayList<Task> initialiseArchives();
	public ArrayList<Task> parseIn(String command);
	
}
