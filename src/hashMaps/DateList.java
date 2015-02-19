package hashMaps;
import java.util.*;

import basicElements.TaskByDate;

public class DateList extends HashMap<String, TaskByDate> {
	
	public boolean hasDate(String date){
		return this.containsKey(date);
	}
	
	private TaskByDate getTaskOnDate(int date){
		return this.get(date);
	}
	
	public ArrayList<Integer> getTodoTaskIdOnDate(int date){
		return this.getTaskOnDate(date).getToDoTaskIds();
	}
	
	public ArrayList<Integer> getArchivedTaskIdOnDate(int date){
		return this.getTaskOnDate(date).getArchivedTaskIds();
	}
	
	public void addNewDate(String date, TaskByDate taskByDate){
		this.put(date, taskByDate);		
	}
	
}
