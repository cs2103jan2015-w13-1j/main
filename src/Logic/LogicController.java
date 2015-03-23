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
	
	protected StorageController DC = new StorageController();
	protected DATA data;
	
	protected TaskList activeTaskList;
	protected TaskList archivedTaskList;

	protected ToDoSortedList toDoSortedList;
	protected HistoryController historyController;
	
	@Override
	public void initialise() {
		data = DC.getAllData();
		historyController = new HistoryController();
		
		loadData();
	}
	
	/**
	 * Load relevant task lists from the data object
	 * @param data
	 */
	private void loadData() {
		activeTaskList = data.getActiveTaskList();
		archivedTaskList = data.getArchivedTaskList();
		toDoSortedList = new ToDoSortedList();
		for (Entry<Integer, Task> e: activeTaskList.entrySet()) {
			toDoSortedList.addTask(e.getValue());
		}
	}
	
	private ToDoSortedList storeAndReturnToDo() {
		data.setActiveTaskList(activeTaskList);
		data.setArchivedTaskList(archivedTaskList);
		DC.storeAllData(data);
		return toDoSortedList;
	}
	
	private ToDoSortedList update_Save_ReturnTodo() {
		toDoSortedList.updateTaskOrder(activeTaskList);
		return storeAndReturnToDo();
	}
	
	@Override
	public ToDoSortedList addTask(Task task) {
		assert(!toDoSortedList.contains(task));	
		historyController.log();
		
		toDoSortedList.add(task);
		activeTaskList.addTask(task.getId(), task);
		return storeAndReturnToDo();
	}
	
	@Override
	public ToDoSortedList addDeadLine(Task task, Date deadline) {
		assert(task.getType().equals("generic"));
		historyController.log();
		
		task.addDeadline(deadline);
		task.setType("deadline");
		return update_Save_ReturnTodo();
	}
	
	@Override
	public ToDoSortedList editDeadline(Task task, Date deadline) {
		assert (task.getType().equals("deadline"));
		historyController.log();
		
		task.changeDeadline(deadline);
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList addStartAndEndTime(Task task, Date start, Date end) {
		assert(task.getType().equals("generic"));
		historyController.log();
		
		task.addStartAndEndTime(start, end);
		task.setType("meeting");
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editStartTime(Task task, Date start) {
		assert (task.getType().equals("meeting"));
		assert (start.compareTo(task.getEndTime()) < 0);
		historyController.log();
		
		task.changeStartTime(start);
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editEndTime(Task task, Date end) {
		assert (task.getType().equals("meeting"));
		assert (task.getStartTime().compareTo(end) < 0);
		historyController.log();
		
		task.changeEndTime(end);
		return storeAndReturnToDo();
	}
	
	@Override
	public ToDoSortedList editDescription(Task task, String newDescription) {
		historyController.log();
		
		task.changeDescription(newDescription);
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList editPriority(Task task, int priority) {
		assert (priority <= 10);
		historyController.log();
		
		task.changePriority(priority);
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList addTag(Task task, String tag) {
		assert (!task.getTags().contains(tag));
		historyController.log();
		
		task.addTag(tag);
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList removeTag(Task task, String tag) {
		assert (task.getTags().contains(tag));
		historyController.log();
		
		task.removeTag(tag);
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList moveToArchive(Task task, Date finishedTime) {
		assert (activeTaskList.containsValue(task));
		assert (toDoSortedList.contains(task));
		historyController.log();
		
		task.moveToArchive(finishedTime);
		int id = task.getId();
		activeTaskList.removeTaskbyId(id);
		archivedTaskList.addTask(id, task);
		toDoSortedList.deleteTask(task);
		return storeAndReturnToDo();
	}
	
	@Override
	public ToDoSortedList deleteTask(Task task) {
		assert(activeTaskList.containsValue(task) || archivedTaskList.containsValue(task));
		historyController.log();
		
		if (!task.isArchived()){
			toDoSortedList.deleteTask(task);
			activeTaskList.removeTaskbyId(task.getId());
		}
		else{
			archivedTaskList.removeTaskbyId(task.getId());
		}
		return storeAndReturnToDo();
	}
	
	// Only for tasks in the to do list
	@Override
	public ArrayList<Task> searchByDate(String date) {
		ArrayList<Task> taskOnDate = new ArrayList<Task>();
		for (Task task: toDoSortedList) {
			Date dateOfTask = task.getTime();
			if (dateOfTask != null) {
				if (dateOfTask.getDateRepresentation().equals(date)) {
					taskOnDate.add(task);
				}
				else {
					if (task.getEndTime() != null) {
						if (task.getEndTime().getDateRepresentation().equals(date)) {
							taskOnDate.add(task);
						}
						if (task.getStartTime().getDateRepresentation().compareTo(date)<0 &&
							task.getEndTime().getDateRepresentation().compareTo(date)>0) {
							taskOnDate.add(task);
						}
					}
				}
			}
		}
		return taskOnDate;
	}

	// Only for tasks in the to do list
	@Override
	public ArrayList<Task> searchByTag(String tag) {
		ArrayList<Task> taskByTag = new ArrayList<Task>();
		for (Task task: toDoSortedList) {
			ArrayList<String> tags = task.getTags();
			if (tags.contains(tag)) {
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
	public ArchiveSortedList deleteFromArchive(Task task) {
		assert(archivedTaskList.containsValue(task));
		historyController.log();
		
		archivedTaskList.removeTaskbyId(task.getId());
		DC.storeAllData(data);
		return viewArchiveTasks();
	}

	@Override
	public ToDoSortedList addRecurringTask(Task task, long period, int recurrenceNum) {
		assert(!task.getType().equals("generic"));
		historyController.log();
		
		int id = task.getId();
		int recurrenceId = data.getRecurrenceId();
		task.setRecurrenceId(recurrenceId);
		activeTaskList.addTask(task.getId(), task);
		for (int i = 1; i < recurrenceNum ; i++) {
			Task newTask = task.copyWithInterval(id + i, i * period);
			newTask.setRecurrenceId(recurrenceId);
			activeTaskList.addTask(id + i, newTask);
		}
		toDoSortedList.updateTaskOrder(activeTaskList);
		data.increamentRecurrenceId();
		return toDoSortedList;
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
		historyController.log();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			activeTaskList.removeTaskbyId(t.getId());
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList archiveAllTasks(Task task, Date finishedTime) {
		assert(activeTaskList.containsValue(task));
		historyController.log();
		
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
	public ToDoSortedList editAlldeadline(Task task, Date deadline) {
		assert(activeTaskList.containsValue(task));
		assert(task.getType().equals("deadline"));
		historyController.log();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			t.changeDeadline(deadline);
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editAllStartTime(Task task, Date newDate) {
		assert(activeTaskList.containsValue(task));
		assert(task.getType().equals("meeting"));
		historyController.log();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			t.changeStartTime(newDate);
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editAllEndTime(Task task, Date newDate) {
		assert(activeTaskList.containsValue(task));
		assert(task.getType().equals("meeting"));
		historyController.log();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			t.changeEndTime(newDate);
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList editAllPriority(Task task, int newPriority) {
		assert(activeTaskList.containsValue(task));
		historyController.log();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			t.changePriority(newPriority);
		}
		return update_Save_ReturnTodo();
	}

	@Override
	public ToDoSortedList addAlltag(Task task, String tag) {
		assert(!task.getTags().contains(tag));
		historyController.log();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			if (t.getTags().contains(tag)) {
				return null;
			}
			t.addTag(tag);
		}
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList removeAlltag(Task task, String tag) {
		assert(task.getTags().contains(tag));
		historyController.log();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			if (!t.getTags().contains(tag)) {
				return null;
			}
			t.removeTag(tag);
		}
		return storeAndReturnToDo();
	}

	@Override
	public ToDoSortedList editAllDescription(Task task, String description) {
		assert(activeTaskList.containsValue(task));
		historyController.log();
		
		ArrayList<Task> tasks = getRecurringTasks(task);
		for (Task t: tasks) {
			t.changeDescription(description);
		}
		return storeAndReturnToDo();
	}

	@Override
	public boolean undo() {
		DATA data = historyController.undo();
		if (data == null) {
			return false;
		}
		this.data = data;
		loadData();
		DC.storeAllData(data);
		return true;
	}
	
	@Override
	public String exit(int serialNumber) {
		data.setSerialNumber(serialNumber);
		return DC.storeAllData(data);
	}
}
