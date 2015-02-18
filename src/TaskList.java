import java.util.*;

public class TaskList extends HashMap<Integer, Task>{

	public void addTask(int id, Task task){
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
