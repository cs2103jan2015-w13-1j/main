/**
 * Used to store the tasks in sorted order when the program is running for the todo list.
 * Support add, delete, update, and extract top_n tasks.
 * @author Yichen
 */

package Common;

@SuppressWarnings("serial")
public class ToDoSortedList extends GeneralSortedList{
	
	/**
	 * Compare the tasks in the todo list. A task is 'smaller' if it has an earlier date.
	 * If two tasks has the same date, the task with higher priority is 'smaller'
	 * @Override 
	 * @param t1
	 * @param t2
	 * @return an integer indicating the comparison result of t1 and t2
	 */
	public ToDoSortedList(){
		super(Task.dateThenPriority);
	}

}
