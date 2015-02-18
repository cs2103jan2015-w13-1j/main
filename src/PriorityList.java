import java.util.*;

public class PriorityList extends HashMap<Integer, TaskByPriority>{
	
	public boolean hasPriority(int priority){
		return this.containsKey(priority);
	}
	
	public TaskByPriority getTaskWithPriority(int priority){
		return this.get(priority);
	}
	
	public ArrayList<Integer> getTodoTaskIdWithPriority(int priority){
		return this.getTaskWithPriority(priority).getToDoTaskIds();
	}
	
	public ArrayList<Integer> getArchivedTaskIdWithPriority(int priority){
		return this.getTaskWithPriority(priority).getArchivedTaskIds();
	}
	
	public void addNewPriority(int priority, TaskByPriority taskByPriority){
		this.put(priority, taskByPriority);
	}
}
