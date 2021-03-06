// @author A0113598X
/**
 * Used for creating task lists for both todo list and archived list
 */

package Logic;
import java.util.*;

import Common.Task;

@SuppressWarnings("serial")
public class TaskList extends HashMap<Integer, Task>{

	public void addTask(int id, Task task){
		if (this.containsKey(id)) {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("ID: " + id + " already exists in the tasklist");
				System.out.println(task);
				e.printStackTrace();
				return;
			}
		}
		this.put(id, task);
	}
	
	public Task getTaskbyId(int id){
		return this.get(id);
	}
	
	public boolean removeTaskbyId(int id){
		if (this.containsKey(id)){
			this.remove(id);
			return true;
		}
		return false;
	}
	
}
