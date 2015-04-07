package Logic;
import java.util.ArrayList;
import java.util.Map.Entry;

import Common.ArchiveSortedList;
import Common.DATA;
import Common.Date;
import Common.PrioritySortedList;
import Common.Task;
import Common.TaskList;
import Common.ToDoSortedList;
import History.HistoryController;
import Storage.StorageController;

public class LogicController implements InterfaceForLogic{
	
	protected boolean isTesting = false;
	
	protected StorageController storageController = new StorageController();
	protected DATA data;
	
	protected TaskList activeTaskList;
	protected TaskList archivedTaskList;
	
	protected ToDoSortedList toDoSortedList;
	protected HistoryController historyController;
	
	@Override
	public void initialise() {
		data = storageController.getAllData();
		historyController = new HistoryController();
		
		loadData();
	}
	
	/**
	 * Load relevant task lists from the data object
	 * @param data
	 */
	protected void loadData() {
		activeTaskList = data.getActiveTaskList();
		archivedTaskList = data.getArchivedTaskList();
		toDoSortedList = new ToDoSortedList();
		for (Entry<Integer, Task> e: activeTaskList.entrySet()) {
			toDoSortedList.addTask(e.getValue());
		}
	}
	
	/**
	 * Store the current data and return the todoSortedList
	 * @return the toDoSortedList
	 */
	private ToDoSortedList storeAndReturnToDo() {
		if (!isTesting) {
			data.setActiveTaskList(activeTaskList);
			data.setArchivedTaskList(archivedTaskList);
			storageController.storeAllData(data);
		}
		return toDoSortedList;
	}
	
	/**
	 * Update the todosortedlist and return it
	 * @return
	 */
	private ToDoSortedList update_Save_ReturnTodo() {
		toDoSortedList.updateTaskOrder(activeTaskList);
		return storeAndReturnToDo();
	}
	
	/**
	 * Log the current data object to history
	 */
	private void logToHistory() {
		if (!isTesting) {
			historyController.log();
		}
	}
	
	@Override
	public ToDoSortedList addTask(Task task) {
		assert(!toDoSortedList.contains(task));	
		logToHistory();
		
		toDoSortedList.add(task);
		activeTaskList.addTask(task.getId(), task);
		return storeAndReturnToDo();
	}
	
	@Override
	public ToDoSortedList addDeadLine(Task task, Date deadline) {
		assert(task.getType().equals("generic"));
		logToHistory();
		
		task.addDeadline(deadline);
		task.setType("deadline");
		return update_Save_ReturnTodo();
	}
	
	@Override
	public ToDoSortedList removeDeadline(Task task) {
		assert(task.getType().equals("deadline"));
		logToHistory();
		task.changeDeadline(null);
		task.setType("generic");
		return update_Save_ReturnTodo();
	}
	
	@Override
	public ToDoSortedList editDeadline(Task task, Date deadline) {
		assert (task.getType().equals("deadline"));
		logToHistory();
		
		task.changeDeadline(deadline);
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList addStartAndEndTime(Task task, Date start, Date end) {
		assert(task.getType().equals("generic"));
		logToHistory();
		
		task.addStartAndEndTime(start, end);
		task.setType("meeting");
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editStartTime(Task task, Date start) {
		assert (task.getType().equals("meeting"));
		assert (start.compareTo(task.getEndTime()) < 0);
		logToHistory();
		
		task.changeStartTime(start);
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editEndTime(Task task, Date end) {
		assert (task.getType().equals("meeting"));
		assert (task.getStartTime().compareTo(end) < 0);
		logToHistory();
		
		task.changeEndTime(end);
		return update_Save_ReturnTodo();
	}
	
	@Override
	public ToDoSortedList editDescription(Task task, String newDescription) {
		logToHistory();
		
		task.changeDescription(newDescription);
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList editPriority(Task task, int priority) {
		assert (priority <= 10);
		logToHistory();
		
		task.changePriority(priority);
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList addTag(Task task, String tag) {
		assert (!task.getTags().contains(tag));
		logToHistory();
		
		task.addTag(tag);
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList removeTag(Task task, String tag) {
		assert (task.getTags().contains(tag));
		logToHistory();
		
		task.removeTag(tag);
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList moveToArchive(Task task, Date finishedTime) {
		assert (activeTaskList.containsValue(task));
		assert (toDoSortedList.contains(task));
		logToHistory();
		
		task.moveToArchive(finishedTime);
		int id = task.getId();
		activeTaskList.removeTaskbyId(id);
		archivedTaskList.addTask(id, task);
		toDoSortedList.deleteTask(task);
		return storeAndReturnToDo();
	}
	
	@Override
	public ToDoSortedList unArchive(Task task) {
		assert (archivedTaskList.containsValue(task));
		logToHistory();
		
		task.removeFromArchive();
		int id = task.getId();
		archivedTaskList.removeTaskbyId(id);
		activeTaskList.addTask(id, task);
		return update_Save_ReturnTodo();
	}
	
	// Only for tasks in the to do list
	@Override
	public ArrayList<Task> searchByDate(String date) {
		ArrayList<Task> taskOnDate = new ArrayList<Task>();
		for (Task task: toDoSortedList) {
			if (task.isOnDate(date)) {
				taskOnDate.add(task);
			}
		}
		return taskOnDate;
	}

	// Only for tasks in the to do list
	@Override
	public ArrayList<Task> searchByTag(String tag) {
		ArrayList<Task> taskByTag = new ArrayList<Task>();
		for (Task task: toDoSortedList) {
			if (task.containsTag(tag)) {
				taskByTag.add(task);
			}
		}
		return taskByTag;
	}
	
	// Only for tasks in the to do list
	@Override
	public ArrayList<Task> searchByPriority(int priority) {
		ArrayList<Task> taskByPriority = new ArrayList<Task>();
		for (Task task: toDoSortedList) {
			if (task.getPriority() == priority) {
				taskByPriority.add(task);
			}
		}
		return taskByPriority;
	}

	// Only for tasks in the to do list
	@Override
	public ArrayList<Task> searchByDesc(String keyword) {
		ArrayList<Task> taskByKeyword = new ArrayList<Task>();
		for (Task task: toDoSortedList) {
			if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
				taskByKeyword.add(task);
			}
		}
		return taskByKeyword;	
	}
	
	@Override
	public int getSerialNumber() {
		return this.data.getSerialNumber();
	}
	
	@Override
	public void setSerialNumber(int n) {
		this.data.setSerialNumber(n);
	}

	@Override
	public PrioritySortedList sortByPriority() {
		PrioritySortedList prioritySortedList = new PrioritySortedList();
		for (Entry<Integer, Task> e: activeTaskList.entrySet()) {			
			Task task = e.getValue();
			prioritySortedList.addTask(task);
		}
		return prioritySortedList;
	}

	@Override
	public ToDoSortedList sortByTime() {
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList viewActiveTasks() {
		return toDoSortedList;
	}
	
	@Override
	public ArchiveSortedList viewArchiveTasks() {
		ArchiveSortedList archiveSortedList = new ArchiveSortedList();
		for (Entry<Integer, Task> e: archivedTaskList.entrySet()) {
			archiveSortedList.addTask(e.getValue());
		}
		return archiveSortedList;
	}
	
	@Override
	public ToDoSortedList deleteTask(Task task) {
		assert(activeTaskList.containsValue(task));
		logToHistory();
		toDoSortedList.deleteTask(task);
		activeTaskList.removeTaskbyId(task.getId());
		return storeAndReturnToDo();
	}
	
	@Override
	public ToDoSortedList deleteByTag(String tag) {
		logToHistory();
		for (Task task: toDoSortedList) {
			if (task.hasTagExact(tag)) {
				toDoSortedList.deleteTask(task);
				activeTaskList.removeTaskbyId(task.getId());
			}
		}
		return storeAndReturnToDo();
	}
	
	@Override
	public ArchiveSortedList deleteFromArchive(Task task) {
		assert(archivedTaskList.containsValue(task));
		logToHistory();
		archivedTaskList.removeTaskbyId(task.getId());
		storageController.storeAllData(data);
		return viewArchiveTasks();
	}
	

	@Override
	public ToDoSortedList addRecurringTask(Task task, long period, int recurrenceNum) {
		assert(!task.getType().equals("generic"));
		assert(period > 0);
		logToHistory();
		int id = task.getId();
		activeTaskList.addTask(id, task);
		return generateRecurringTasks(task, period, recurrenceNum, id);
	}

	@Override
	public ToDoSortedList repeatExistingTask(Task task, long period, int recurrenceNum) {
		assert(!task.getType().equals("generic"));
		assert(period > 0);
		logToHistory();
		int id = getSerialNumber();
		return generateRecurringTasks(task, period, recurrenceNum, id);
	}

	
	/**
	 * @param task
	 * @param period
	 * @param recurrenceNum
	 * @param id
	 * @return
	 */
	private ToDoSortedList generateRecurringTasks(Task task, long period, int recurrenceNum, int id) {
		int recurrenceId = data.getRecurrenceId();
		task.setRecurrenceId(recurrenceId);
		for (int i = 1; i < recurrenceNum ; i++) {
			createAndAddRecurringTask(task, period, id, recurrenceId, i);
		}
		data.increamentRecurrenceId();
		return update_Save_ReturnTodo();
	}
	
	/**
	 * @param task
	 * @param period
	 * @param id
	 * @param recurrenceId
	 * @param i
	 */
	private void createAndAddRecurringTask(Task task, long period, int id, int recurrenceId, int i) {
		Task newTask = task.copyWithInterval(id + i, i * period);
		newTask.setRecurrenceId(recurrenceId);
		activeTaskList.addTask(id + i, newTask);
	}
	
	/**
	 * @param task a recurrence task
	 * @return an array list of task objects with the same recurring id
	 */
	private ArrayList<Task> getRecurringTasks(Task task) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (Task t: activeTaskList.values()) {
			if (t.getRecurrenceId() == task.getRecurrenceId()) {
				tasks.add(t);
			}
		}
		assert(tasks.size()>1);
		return tasks;
	}
	
	@Override
	// Only for tasks in the active task list
	public ToDoSortedList deleteAllRecurringTask(Task task) {
		assert(activeTaskList.containsValue(task));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			activeTaskList.removeTaskbyId(t.getId());
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList archiveAllTasks(Task task, Date finishedTime) {
		assert(activeTaskList.containsValue(task));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			t.moveToArchive(finishedTime);
			int id = t.getId();
			activeTaskList.removeTaskbyId(id);
			archivedTaskList.addTask(id, t);
		}
		return update_Save_ReturnTodo();
	}
	
	@Override
	public ToDoSortedList editAlldeadlineTime(Task task, int hour, int minute) {
		assert(activeTaskList.containsValue(task));
		assert(task.getType().equals("deadline"));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			Date originalTime = t.getDeadline();
			Date newTime = originalTime.changeTime(hour, minute);
			t.changeDeadline(newTime);
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editAllStartTime(Task task, int hour, int minute) {
		assert(activeTaskList.containsValue(task));
		assert(task.getType().equals("meeting"));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			Date originalTime = t.getStartTime();
			Date newTime = originalTime.changeTime(hour, minute);
			t.changeStartTime(newTime);
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editAllEndTime(Task task, int hour, int minute) {
		assert(activeTaskList.containsValue(task));
		assert(task.getType().equals("meeting"));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			Date originalTime = t.getEndTime();
			Date newTime = originalTime.changeTime(hour, minute);
			t.changeEndTime(newTime);
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editAllPriority(Task task, int newPriority) {
		assert(activeTaskList.containsValue(task));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			t.changePriority(newPriority);
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList addAlltag(Task task, String tag) {
		assert(!task.getTags().contains(tag));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			if (!t.getTags().contains(tag)) {
				t.addTag(tag);
			}	
		}
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList removeAlltag(Task task, String tag) {
		assert(task.getTags().contains(tag));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			if (t.containsTag(tag)) {
				t.removeTag(tag);
			}
		}
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList editAllDescription(Task task, String description) {
		assert(activeTaskList.containsValue(task));
		logToHistory();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			t.changeDescription(description);
		}
		return storeAndReturnToDo();
	}

	@Override
	public ArchiveSortedList deleteAllRecurringInArchive(Task task) {
		assert(archivedTaskList.containsValue(task));
		logToHistory();
		ArrayList<Task> tasks = new ArrayList<Task>();
		for (Task t: archivedTaskList.values()) {
			if (t.getRecurrenceId() == task.getRecurrenceId()) {
				tasks.add(t);
			}
		}
		for (Task t: tasks) {
			archivedTaskList.removeTaskbyId(t.getId());
		}
		if (!isTesting) {
			data.setActiveTaskList(activeTaskList);
			data.setArchivedTaskList(archivedTaskList);
			storageController.storeAllData(data);
		}
		return viewArchiveTasks();
	}
	
	@Override
	public boolean undo() {
		DATA data = historyController.undo();
		return reloadData(data);
	}
	
	@Override
	public boolean redo() {
		DATA data = historyController.redo();
		return reloadData(data);
	}

	/**
	 * @param data
	 * @return true if data is not null
	 */
	private boolean reloadData(DATA data) {
		if (data == null) {
			return false;
		}
		this.data = data;
		loadData();
		storageController.storeAllData(data);
		return true;
	}
	
	@Override
	public String exit(int serialNumber) {
		data.setSerialNumber(serialNumber);
		return storageController.storeAllData(data);
	}
}
