package hashMaps;
import java.util.*;

import basicElements.TaskByTag;

public class TagList extends HashMap<String, TaskByTag>{
	
	public boolean hasTag(String tag){
		return this.containsKey(tag);
	}
	
	public Set<String> getAllTags() {
		return this.keySet();
	}
	
	private TaskByTag getTaskWithTag(String tag){
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
	
	public TaskByTag removeTag(String tag) {
		return this.remove(tag);
	}
	
	public void changeTagNmae(String originalTagName, String newTagName) {
		TaskByTag taskByTag = this.removeTag(originalTagName);
		this.addNewTag(newTagName, taskByTag);
	}
}
