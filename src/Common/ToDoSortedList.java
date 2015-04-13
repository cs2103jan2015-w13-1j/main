// @author A0113598X
/**
 * Used to store the tasks in sorted order when the program is running for the todo list.
 * Support add, delete, update, and extract top_n tasks.
 */
package Common;


@SuppressWarnings("serial")
public class ToDoSortedList extends GeneralSortedList{
	
	/**
	 * Comparator for the todo list sorted by deadline or starting time, then priority
	 */
	public ToDoSortedList(){
		super(Task.dateThenPriority);
	}

}
