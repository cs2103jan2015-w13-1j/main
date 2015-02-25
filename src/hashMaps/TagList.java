package hashMaps;
import java.util.*;

import basicElements.TaskByTag;

public class TagList extends GeneralSearchList<String, TaskByTag>{
	
	public boolean hasTag(String tag){
		return super.hasProperty(tag);
	}
	
	public ArrayList<Integer> getTodoTaskIdWithtag(String tag){
		return this.getTodoTaskIdWithProperty(tag);
	}
	
	public ArrayList<Integer> getArchivedTaskIdWithtag(String tag){
		return this.getArchivedTaskIdWithProperty(tag);
	}
	
	public void addNewTag(String tag, TaskByTag taskBytag){
		super.put(tag, taskBytag);
	}
	
	public Set<String> getAllTags() {
		return this.keySet();
	}
	
	public Set<String> removeTag(String tag) {
		return this.getAllTags();
	}
	
	public void changeTagNmae(String originalTagName, String newTagName) {
		TaskByTag taskByTag = (TaskByTag) this.get(originalTagName);
		this.addNewTag(newTagName, taskByTag);
	}
}
