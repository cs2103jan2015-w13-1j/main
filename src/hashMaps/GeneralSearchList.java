package hashMaps;

import java.util.ArrayList;
import java.util.HashMap;

import basicElements.*;

public class GeneralSearchList<K, V> extends HashMap<K, TaskByProperty>{
	
	public void addToDoTask(K key, Task task) {
		if (this.containsKey(key)) {
			this.get(key).addToDoTask(task.getId());
		}
		else {
			TaskByProperty tbp = new TaskByProperty();
			tbp.addToDoTask(task.getId());
			this.put(key, tbp);
		}
	}
	
	public void removeToDoTask(K key, Task task) {
		this.get(key).removeToDoTask(task.getId());
	}
	
	public void addArchivedTask(K key, Task task) {
		if (this.containsKey(key)) {
			this.get(key).addArchivedTask(task.getId());
		}
		else {
			TaskByProperty tbp = new TaskByProperty();
			tbp.addArchivedTask(task.getId());
			this.put(key, tbp);
		}
	}
	
	public boolean hasProperty(K property){
		return this.containsKey(property);
	}
	
	public ArrayList<Integer> getTodoTaskIdWithProperty(K property){
		return this.get(property).getToDoTaskIds();
	}
	
	public ArrayList<Integer> getArchivedTaskIdWithProperty(K property){
		return this.get(property).getArchivedTaskIds();
	}
	
	public void addNewProperty(K property, TaskByProperty taskByProperty){
		this.put(property, taskByProperty);
	}
}
