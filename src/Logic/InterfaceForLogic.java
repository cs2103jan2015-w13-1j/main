package Logic;

import java.util.ArrayList;
import Common.ArchiveSortedList;
import Common.Date;
import Common.PrioritySortedList;
import Common.Task;
import Common.ToDoSortedList;

public interface InterfaceForLogic {
	
/***********************************************
		Initialisation and ID methods
***********************************************/	
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
	
/***********************************************
					Create
***********************************************/	
	/**
	 * @param task a new task object
	 * @return the updated ToDoSortedList
	 * null if the task is already inside the list of tasks
	 */
	ToDoSortedList addTask(Task task);
	
	/**
	 * @param task a single task object which is the first task to be repeated.
	 * 		The task must have a specific start and/or end time.
	 * @param period the interval between each two recurrence tasks
	 * @param recurrenceNum the number of recurrence tasks, should be at least 2
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList addRecurringTask(Task task, long period, int recurrenceNum);
	
	/**
	 * @param task a single existing task object which is the first task to be repeated.
	 * 		The task must have a specific start and/or end time.
	 * 		The task must not be a recurrence task at the start
	 * @param period the interval between each two recurrence tasks
	 * @param recurrenceNum the number of recurrence tasks, should be at least 2
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList repeatExistingTask(Task task, long period, int recurrenceNum);
	
/***********************************************
				Read -> Search
***********************************************/
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

/***********************************************
				Read -> sort
***********************************************/
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
	 * @return the ArchiveSortedList
	 */ 
	ArchiveSortedList viewArchiveTasks();
	
	/**
	 * @return the ToDoSortedList
	 */
	ToDoSortedList viewActiveTasks();

/***********************************************
			Delete & Archive
***********************************************/
		
	/**
	 * Delete a task
	 * @param task
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList deleteTask(Task task);
	
	/**
	 * This will delete all the linked recurrence tasks in the active task list
	 * @param task one member of the recurring tasks
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList deleteAllRecurringTask(Task task);
	
	/**
	 * delete a task from the archive
	 * @param task
	 * @return the updated ArchiveSortedList
	 */
	ArchiveSortedList deleteFromArchive(Task task);

	/**
	 * This will delete all the linked recurrence tasks in the archived task list
	 * @param task
	 * @return the updated archiveSortedList
	 */
	ArchiveSortedList deleteAllRecurringInArchive(Task task);
	
	/**
	 * This will delete all the tasks with the specified tag in the todolist
	 * @param tag
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList deleteByTag(String tag);
	
	/**
	 * move the task from todo list to archive
	 * @param task the task to be moved to archive
	 * @param finishedTime 
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList moveToArchive(Task task, Date finishedTime);
	
	/**
	 * This will archive all the linked recurrence tasks
	 * @param task one member of the recurring tasks
	 * @param finishedTime the time when the task is moved to archive
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList archiveAllTasks(Task task, Date finishedTime);
	
	/**
	 * move the task from archive to todo list
	 * @param task
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList unArchive(Task task);
	
//	/**
//	 * This will un-archive all the linked recurrence tasks
//	 * @param task one member of the recurring tasks
//	 * @return the updated ToDoSortedList
//	 */
//	ToDoSortedList unArchiveAllRecurring(Task task);
	

/***********************************************
			Update -> time related
***********************************************/
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
	 * @param task an existing meeting task
	 * @param end the new end time
	 * @return the updated ToDoSortedList
	 * null if the task was not 'meeting' task
	 */
	ToDoSortedList editEndTime(Task task, Date end);
	
	/**
	 * @param task an existing deadline task
	 * @param deadline the new deadline
	 * @return the updated ToDoSortedList
	 * null if the task was not 'deadline' task
	 */
	ToDoSortedList editDeadline(Task task, Date deadline);
	
	/**
	 * Remove the deadline of a deadline task, the task will become a generic task.
	 * @param task an existing deadline task
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList removeDeadline(Task task);
	
	/**
	 * This will modify all the linked recurrence tasks
	 * @param task one member of the recurring tasks
	 * @param hour the HH to be changed to
	 * @param minute the mm to be changed to
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList editAlldeadlineTime(Task task, int hour, int minute);
	
	/**
	 * This will modify all the linked recurrence tasks
	 * @param task one member of the recurring tasks
	 * @param hour the HH to be changed to
	 * @param minute the mm to be changed to
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList editAllStartTime(Task task, int hour, int minute);
	
	/**
	 * This will modify all the linked recurrence tasks
	 * @param task one member of the recurring tasks
	 * @param hour the HH to be changed to
	 * @param minute the mm to be changed to
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList editAllEndTime(Task task, int hour, int minute);
	
	
/***********************************************
		Update -> tag, desc, priority
***********************************************/	
	
	/**
	 * Modify the description of a task
	 * @param task an existing task
	 * @param newDescription the new description
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList editDescription(Task task, String newDescription);
	
	/**
	 * Modify the priority of a task
	 * @param task an existing task
	 * @param priority the new priority
	 * @return the updated ToDoSortedList
	 */
	ToDoSortedList editPriority(Task task, int priority);
	
	/**
	 * @param task an existing task
	 * @param tag a new tag
	 * @return the updated ToDoSortedList
	 * null if the tag already exist
	 */
	ToDoSortedList addTag(Task task, String tag);
	
	/**
	 * @param task an existing task
	 * @param tag an existing tag to be removed
	 * @return the updated ToDoSortedList
	 * null if the tag does not exist
	 */
	ToDoSortedList removeTag(Task task, String tag);
	
	/**
	 * This will modify all the linked recurrence tasks
	 * @param task one member of the recurring tasks
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList editAllPriority(Task task, int newPriority);
	
	/**
	 * This will modify all the linked recurrence tasks
	 * @param task one member of the recurring tasks
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList addAlltag(Task task, String tag);
	
	/**
	 * This will modify all the linked recurrence tasks
	 * @param task one member of the recurring tasks
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList removeAlltag(Task task, String tag);
	
	/**
	 * This will modify all the linked recurrence tasks
	 * @param task one member of the recurring tasks
	 * @return the updated toDoSortedList
	 */
	ToDoSortedList editAllDescription(Task task, String description);
	
/***********************************************
				Undo & Redo
***********************************************/
	/**
	 * undo an action
	 * @return a boolean indicating whether this is successful
	 */
	boolean undo();
	
	/**
	 * redo an action
	 * @return a boolean indicating whether this is successful
	 */
	boolean redo();
	
/***********************************************
					Exit
***********************************************/
	/**
	 * @param serialNumber the current highest ID that is already used
	 * @return a string message indicating the state of program
	 */
	String exit(int serialNumber);
	
}
