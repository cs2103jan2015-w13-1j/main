package Common;


/**
 * Used to store the tasks in sorted order when the program is running for the archive list.
 * Support add, delete, update, and extract top_n tasks.
 * @author Yichen
 */
@SuppressWarnings("serial")
public class ArchiveSortedList extends GeneralSortedList{

	/**
	 * Compare the tasks in the archive list. A task is 'smaller' if it has an later date.
	 * If two tasks has the same date, the task with higher priority is 'smaller'
	 * @Override 
	 * @param t1
	 * @param t2
	 * @return an integer indicating the comparison result of t1 and t2
	 */
	public ArchiveSortedList(){
		super(Task.reverseDateThenPriority);
	}
}
