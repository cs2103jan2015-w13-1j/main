package hashMaps;
import java.util.*;

import basicElements.TaskByDate;

public class DateList extends GeneralSearchList<String, TaskByDate> {
	
	public boolean hasDate(String date){
		return super.containsKey(date);
	}
	
	public ArrayList<Integer> getTodoTaskIdOnDate(int date){
		return this.getTodoTaskIdOnDate(date);
	}
	
	public ArrayList<Integer> getArchivedTaskIdOnDate(int date){
		return this.getArchivedTaskIdOnDate(date);
	}
	
	public void addNewDate(String date, TaskByDate taskByDate){
		super.put(date, taskByDate);		
	}
	
}
