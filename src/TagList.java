import java.util.*;

public class TagList extends HashMap<String, TaskByTag>{
	
	public boolean hasTag(String tag){
		return this.containsKey(tag);
	}
	
	public TaskByTag getTaskWithTag(String tag){
		return this.get(tag);
	}
	
	public ArrayList<Integer> getTodoTaskIdWithtag(String tag){
		return this.getTaskWithTag(tag).getToDoTaskIds();
	}
	
	public ArrayList<Integer> getArchivedTaskIdWithtag(String tag){
		return this.getTaskWithTag(tag).getArchivedTaskIds();
	}
	
	public void addNewTag(String tag, TaskByTag taskBytag){
		this.put(tag, taskBytag);
	}
	
	
}
