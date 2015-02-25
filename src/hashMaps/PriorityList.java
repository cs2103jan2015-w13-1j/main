package hashMaps;
import java.util.*;

import basicElements.TaskByPriority;

public class PriorityList extends GeneralSearchList<Integer, TaskByPriority>{
	
	public boolean hasPriority(int priority){
		return super.hasProperty(priority);
	}
	
	public ArrayList<Integer> getTodoTaskIdWithPriority(int priority){
		return this.getTodoTaskIdWithPriority(priority);
	}
	
	public ArrayList<Integer> getArchivedTaskIdWithPriority(int priority){
		return this.getArchivedTaskIdWithPriority(priority);
	}
	
	public void addNewPriority(int priority, TaskByPriority taskByPriority){
		super.put(priority, taskByPriority);
	}
}
