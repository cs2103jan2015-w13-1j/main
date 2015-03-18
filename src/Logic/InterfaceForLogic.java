package Logic;

import java.util.ArrayList;

import javax.activity.InvalidActivityException;

import Common.ArchiveSortedList;
import Common.Date;
import Common.PrioritySortedList;
import Common.Task;
import Common.ToDoSortedList;

public interface InterfaceForLogic {

	/**
	 * Initialise the Logic Controller, retrieve all data from the storage
	 */
	void initialise();
	
	/**
	 * @return the current highest ID that is already used
	 */
	int getSerialNumber();
	
	/**
	 * @param number - the new serial number
	 */
	void setSerialNumber(int number);
	
	/**
	 * @param task a new task object
	 * @return the updated ToDoSortedList
	 * null if the task is already inside the list of tasks
	 */
	ToDoSortedList addTask(Task task);//sorted by date
	
	/**
	 * @param task an existing generic task
	 * @param deadline the deadline given to the task
	 * @return the updated ToDoSortedList
	 * null if the task was not 'generic' task
	 */
	ToDoSortedList addDeadLine(Task task, Date deadline);
	
	/**
	 * @param task an existing generic task
	 * @param start the start time of the task
	 * @param end the end time of the task
	 * @return the updated ToDoSortedList
	 * null if the task was not 'generic' task
	 */
	ToDoSortedList addStartAndEndTime(Task task, Date start, Date end);
	
	/**
	 * @param task an existing meeting task
	 * @param start the new start time
	 * @return the updated ToDoSortedList
	 * null if the task was not 'meeting' task
	 */
	ToDoSortedList editStartTime(Task task, Date start);
	
	/**
	 * 
	 * @param task an existing meeting task
	 * @param end the new end time
	 * @return the updated ToDoSortedList
	 * null if the task was not 'meeting' task
	 */
	ToDoSortedList editEndTime(Task task, Date end);
	
	/**
	 * 
	 * @param task an existing deadline task
	 * @param deadline the new deadline
	 * @return the updated ToDoSortedList
	 * null if the task was not 'deadline' task
	 */
	ToDoSortedList editDeadline(Task task, Date deadline);
	
	/**
	 * 
	 * @param task an existing task
	 * @param newDescription the new description
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList editDescription(Task task, String newDescription);
	
	/**
	 * 
	 * @param task an existing task
	 * @param priority the new priority
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList editPriority(Task task, int priority);
	
	/**
	 * 
	 * @param task an existing task
	 * @param tag a new tag
	 * @return the updated ToDoSortedList
	 * null if the tag already exist
	 */
	ToDoSortedList addTag(Task task, String tag);
	
	/**
	 * 
	 * @param task an existing task
	 * @param tag an existing tag to be removed
	 * @return the updated ToDoSortedList
	 * null if the tag does not exist
	 */
	ToDoSortedList removeTag(Task task, String tag);
	
	/**
	 * Search tasks on a particular date, the format of the string should be yyyyMMdd
	 * @param searchDate the date to be searched
	 * @return ArrayList<Task> on the specified date
	 */
	ArrayList<Task> searchByDate(String searchDate);
	
	/**
	 * Search tasks with a specific tag
	 * @param tag the tag string
	 * @return ArrayList<Task> with the specified tag
	 */
	ArrayList<Task> searchByTag(String tag);
	
	/**
	 * Search tasks with a specific priority
	 * @param priority the integer priority
	 * @return ArrayList<Task> with the specified priority
	 */
	ArrayList<Task> searchByPriority(int priority);

	/**
	 * Search tasks which contain a given keyword
	 * Done this for V0.1, may need to improve later to accept multiple key words
	 * @param keyword
	 * @return
	 */
	ArrayList<Task> searchByDesc(String keyword);
	
	/**
	 * Sort the tasks by priority
	 * @return the PrioritySortedList
	 */
	PrioritySortedList sortByPriority();
	
	/**
	 * Sort the tasks by time
	 * @return the ToDoSortedList
	 */
	ToDoSortedList sortByTime();

	/**
	 * move the task from to do list to archive
	 * @param task the task to be moved to archive
	 * @param finishedTime 
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList moveToArchive(Task task, Date finishedTime);
	
	/**
	 * @return the ArchiveSortedList
	 */ 
	ArchiveSortedList viewArchiveTasks();
	
	/**
	 * @return the ToDoSortedList
	 */
	ToDoSortedList viewActiveTasks();
	
	/**
	 * Delete a task
	 * @param task
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList deleteTask(Task task);
	
	/**
	 * delete a task from the archive
	 * @param task
	 * @return the updated ArchiveSortedList
	 */
	ArchiveSortedList deleteFromArchive(Task task);
	
	/**
	 * @param serialNumber the current highest ID that is already used
	 * @return a string message indicating the state of program
	 */
	String exit(int serialNumber);
	
}
