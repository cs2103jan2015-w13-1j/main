import java.util.*;

/**
 * Used to store the id's of tasks on a particular date.
 * Mainly for instant searching purpose
 * @author Yichen
 */

public class TaskByDate {
	private Date date;
	private ArrayList<Integer> toDoList = new ArrayList<Integer>();
	private ArrayList<Integer> archivedtaskList = new ArrayList<Integer>();
	
	public Date getDate(){
		return this.date;
	}
	
	public ArrayList<Integer> getToDoTaskIds(){
		return this.toDoList;
	}
	
	public ArrayList<Integer> getArchivedTaskIds(){
		return this.archivedtaskList;
	}
	
	public void addToDoTask(int id){
		this.toDoList.add(id);
	}
	
	public void removeToDoTask(int id){
		this.toDoList.remove(id);
	}
	
	public void addArchivedTask(int id){
		this.archivedtaskList.add(id);
	}
	
	public void removeArchivedTask(int id){
		this.archivedtaskList.remove(id);
	}
	
	/**
	 * Move a task specified by an id from the todo list to the archived list
	 * @param id
	 */
	public void moveToArchive(int id){
		this.removeToDoTask(id);
		this.addArchivedTask(id);
	}
	
	/**
	 * Move a task specified by an id from the archived list to the todo list
	 * @param id
	 */
	public void extractFromArchive(int id){
		this.removeArchivedTask(id);
		this.addToDoTask(id);
	}
	

}
