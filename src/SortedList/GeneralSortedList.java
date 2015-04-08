// @author A0113598X

/**
 * General sorted list used to store the tasks in sorted order when the program is running
 * Support add, delete, update, and extract top_n tasks.
 */
package SortedList;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.Map.Entry;

import Common.Task;
import Logic.TaskList;

@SuppressWarnings("serial")
public class GeneralSortedList extends TreeSet<Task>{
	
	private static final String MESSAGE_ERROR_DELETE = "Id does not exist";

	protected GeneralSortedList(Comparator<Task> c) {
		super(c);
	}
	
	public void addTask(Task task) {
		this.add(task);
	}
	
	@Override
	public boolean contains(Object task) {
		for (Task t: this) {
			if (t.equals(task)) {
				return true;
			}
		}
		return false;
	}
	
	public void deleteTaskById(int id) throws InvalidKeyException {
		Iterator<Task> taskIterator = this.iterator();
		Task toBeDeleted = null;
		while (taskIterator.hasNext()) {
			Task task = taskIterator.next();
			
			if (task.getId() == id) {
				toBeDeleted = task;
				break;
			}
		}
		if (toBeDeleted != null) {
			this.remove(toBeDeleted);
		} else {
			throw new InvalidKeyException(MESSAGE_ERROR_DELETE);
		}
	}
	
	public void deleteTask(Task task){
		assert(this.contains(task));
		this.remove(task);
	}
	
	/**
	 * When a task is updated in certain property, 
	 * we need to re-insert the task so that the order remains correct
	 * @param task
	 */
	public void updateTaskOrder(TaskList taskList) {
		this.clear();
		for (Entry<Integer, Task> entry: taskList.entrySet()) {
			this.addTask(entry.getValue());
		}
	}
	
	/**
	 * @param n
	 * @return arraylist
	 */
	public ArrayList<Task> getTopNTasks(int n) {
		ArrayList<Task> taskList = new ArrayList<Task>(n);
		Iterator<Task> taskIterator = this.iterator();
		for (int i = 0; i < n; i++) {
			if (taskIterator.hasNext()) {
				taskList.add(taskIterator.next());
			} else {
				break;
			}
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
