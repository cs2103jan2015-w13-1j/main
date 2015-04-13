// @author A0113598X
package Common;


/**
 * Used to store the tasks in sorted order when the program is running for the archive list.
 * Support add, delete, update, and extract top_n tasks.
 */
@SuppressWarnings("serial")
public class ArchiveSortedList extends GeneralSortedList{

	/**
	 * Comparator for the archive list sorted by finished time, then priority
	 */
	public ArchiveSortedList(){
		super(Task.reverseDateThenPriority);
	}
}
