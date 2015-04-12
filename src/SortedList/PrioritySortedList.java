// @author A0113598X
/**
 * Used to store the tasks in sorted order when the program is running for the priority-sorted list.
 * Support add, delete, update, and extract top_n tasks.
 */
package SortedList;

import Common.Task;

@SuppressWarnings("serial")
public class PrioritySortedList extends GeneralSortedList{

	/**
	 * Comparator for the todo list sorted by priority, then date
	 */
	public PrioritySortedList(){
		super(Task.priorityThenDate);
	}
	
	
}
