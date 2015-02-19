package treeSets;
import java.util.*;

import basicElements.Task;

/**
 * Used to store the tasks in sorted order when the program is running for the todo list.
 * Support add, delete, update, and extract top_n tasks.
 * @author Yichen
 */
public class TodoSortedList extends GeneralSortedList implements Comparator<Task>{

	/**
	 * Compare the tasks in the todo list. A task is 'smaller' if it has an earlier date.
	 * If two tasks has the same date, the task with higher priority is 'smaller'
	 * @Override 
	 * @param t1
	 * @param t2
	 * @return an integer indicating the comparison result of t1 and t2
	 */
	public int compare(Task t1, Task t2) {
		int dateCompare = t1.getTime().compareTo(t2.getTime());
		if(dateCompare != 0) {
			return dateCompare;
		}
		else {
			return t2.getPriority()-t1.getPriority();
		}
	}
}
