package Logic;

import java.util.ArrayList;

import Common.ArchiveSortedList;
import Common.Date;
import Common.PrioritySortedList;
import Common.Task;
import Common.ToDoSortedList;

public interface InterfaceForLogic {

	int getSerialNumber();
	
	/**
	 * @param task
	 * @return null if the task is already inside the todolist
	 */
	ToDoSortedList addTask(Task task); //sorted by date
	
	// Returns null if the task was not 'generic'
	ToDoSortedList addDeadLine(Task task, Date deadLine);
	ToDoSortedList addStartAndEndTime(Task task, Date start,Date end);
	
	// Returns null if the task was not 'meeting'
	ToDoSortedList editStartTime(Task task, Date start);
	ToDoSortedList editEndTime(Task task, Date end);
	
	// Returns null if the task was not 'deadline'
	ToDoSortedList editDeadline(Task task, Date deadLine);
	ToDoSortedList editDescription(Task task, String newDescription);
	ToDoSortedList editPriority(Task task, int priority);
	
	// Returns null if the tag already exist
	ToDoSortedList addTag(Task task, String tag);
	
	// Returns null if the tag was not there
	ToDoSortedList removeTag(Task task, String tag);
	
	// The search date should be in the format yyyyMMdd
	/**
	 * Search tasks on a particular date, the format of the string should be yyyyMMdd
	 * @param searchDate
	 * @return Tasks on the specified date
	 */
	ArrayList<Task> searchByDate(String searchDate);
	ArrayList<Task> searchByTag(String tag);
	ArrayList<Task> searchByPriority(int priority);
	
	// Done this for V0.1, may need to improve later
	ArrayList<Task> searchByDesc(String keyword);
	

	PrioritySortedList sortByPriority();
	ToDoSortedList sortByTime();

	ToDoSortedList moveToArchive(Task task, Date finishedTime);
	ArchiveSortedList viewArchiveTasks();
	
	ToDoSortedList viewActiveTasks();
	
	ToDoSortedList deleteTask(Task task);
	
}
