package treeSets;
import java.util.*;

import basicElements.Task;

/**
 * General sorted list used to store the tasks in sorted order when the program is running
 * Support add, delete, update, and extract top_n tasks.
 * @author Yichen
 */
public class GeneralSortedList extends TreeSet<Task>{
	
	protected GeneralSortedList(Comparator<Task> c) {
		super(c);
	}
	
	public void addTask(Task task) {
		this.add(task);
	}

	public void deleteTask(Task task) {
		this.remove(task);
	}
	
	/**
	 * When a task is updated in certain priority, 
	 * we need to re-insert the task so that the order remains correct
	 * @param task
	 */
	public void updateTaskOrder(Task task) {
		this.deleteTask(task);
		this.addTask(task);
	}
	
	public ArrayList<Task> getTopNTasks(int n) {
		ArrayList<Task> taskList = new ArrayList<Task>(n);
		Iterator<Task> taskIterator = this.iterator();
		for (int i=0; i<n; i++) {
			if (taskIterator.hasNext()) {
				taskList.add(taskIterator.next());
			}
			else break;
		}
		return taskList;
	}
	
	@Override
	public String toString() {
		String output = "";
		Iterator<Task> taskIterator = this.iterator();
		while (taskIterator.hasNext()) {
			output += taskIterator.next().getId() + "\n";
		}
		return output;
	}
}
