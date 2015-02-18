import java.util.*;

public class DateList extends HashMap<Date, TaskByDate> {
	
	public boolean hasDate(Date date){
		return this.containsKey(date);
	}
	
	public TaskByDate getTaskOnDate(Date date){
		return this.get(date);
	}
	
	public ArrayList<Integer> getTodoTaskIdOnDate(Date date){
		return this.getTaskOnDate(date).getToDoTaskIds();
	}
	
	public ArrayList<Integer> getArchivedTaskIdOnDate(Date date){
		return this.getTaskOnDate(date).getArchivedTaskIds();
	}
	
	public void addNewDate(Date date, TaskByDate taskByDate){
		this.put(date, taskByDate);		
	}
	
}
