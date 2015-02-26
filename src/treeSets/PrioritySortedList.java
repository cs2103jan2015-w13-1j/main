package treeSets;

import basicElements.Task;

/**
 * Used to store the tasks in sorted order when the program is running for the priority-sorted list.
 * Support add, delete, update, and extract top_n tasks.
 * @author Yichen
 */
public class PrioritySortedList extends GeneralSortedList{

	/**
	 * Compare the tasks in the priority-sorted list. A task is 'smaller' if it has a higher priority.
	 * If two tasks has the same priority, the task with earlier date is 'smaller'
	 * @Override 
	 * @param t1
	 * @param t2
	 * @return an integer indicating the comparison result of t1 and t2
	 */
	public PrioritySortedList(){
		super(Task.priorityThenDate);
	}
	
	
}
