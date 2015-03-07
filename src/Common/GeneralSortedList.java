package Common;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.Map.Entry;

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
		}
		else {
			throw new InvalidKeyException("Id dose not exist");
		}
	}
	
	public void deleteTask(Task task) throws Exception {
		boolean removed = this.remove(task);
		if (!removed) {
			throw new Exception("Task not inside the sorted list");
		}
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
