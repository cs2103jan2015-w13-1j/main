package Logic;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.activity.InvalidActivityException;

import Common.ArchiveSortedList;
import Common.DATA;
import Common.Date;
import Common.PrioritySortedList;
import Common.Task;
import Common.TaskList;
import Common.ToDoSortedList;
import Storage.StorageController;

public class LogicController implements InterfaceForLogic{
	
	private StorageController DC = new StorageController();
	private DATA data;
	
	protected TaskList activeTaskList;
	protected TaskList archivedTaskList;

	protected ToDoSortedList toDoSortedList;
	
	@Override
	public void initialise() {
		data = DC.getAllData();
		
		activeTaskList = data.getActiveTaskList();
		archivedTaskList = data.getArchivedTaskList();

		toDoSortedList = new ToDoSortedList();
		for (Entry<Integer, Task> e: activeTaskList.entrySet()) {
			toDoSortedList.addTask(e.getValue());
		}
	}
	
	@Override
	public ToDoSortedList addTask(Task task) {
		if (toDoSortedList.contains(task)) {
			String original = "";
			for (Task t: toDoSortedList) {
				if (t.equals(task)){
					System.out.println(t + " "+task);
				}
				original += t.toString()+ "\n";
			}
			String added = task.toString();
			String message = "original list:\n" + original + "newly added:\n"+added;
			try {
				throw new InvalidActivityException(message);
			} catch (InvalidActivityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		toDoSortedList.add(task);
		activeTaskList.addTask(task.getId(), task);
		DC.storeAllData(data);
		return toDoSortedList;
	}
	
	@Override
	public ToDoSortedList addDeadLine(Task task, Date deadline) {
		if (task.getType().equals("generic")) {
			task.addDeadline(deadline);
			task.setType("deadline");
			toDoSortedList.updateTaskOrder(activeTaskList);
			DC.storeAllData(data);
			return toDoSortedList;
		}
		else {
			return null;
		}
	}
	
	@Override
	public ToDoSortedList editDeadline(Task task, Date deadline) {
		if (!task.getType().equals("deadline")) {
			return null;
		}
		task.changeDeadline(deadline);
		toDoSortedList.updateTaskOrder(activeTaskList);
		DC.storeAllData(data);
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList addStartAndEndTime(Task task, Date start, Date end) {
		if (!task.getType().equals("generic")) {
			task.addStartAndEndTime(start, end);
			task.setType("meeting");
			toDoSortedList.updateTaskOrder(activeTaskList);
			DC.storeAllData(data);
			return toDoSortedList;
		}
		else {
			return null;
		}
	}

	@Override
	public ToDoSortedList editStartTime(Task task, Date start) {
		
		if (!task.getType().equals("meeting")) {
			return null;
		}
		if (start.compareTo(task.getEndTime()) >= 0){
			throw new InvalidParameterException("Start time cannot be earlier than end time!");
		}
		task.changeStartTime(start);
		toDoSortedList.updateTaskOrder(activeTaskList);
		DC.storeAllData(data);
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList editEndTime(Task task, Date end) {
		if (!task.getType().equals("meeting")) {
			return null;
		}
		task.changeEndTime(end);
		DC.storeAllData(data);
		return toDoSortedList;
	}
	
	@Override
	public ToDoSortedList editDescription(Task task, String newDescription) {
		task.changeDescription(newDescription);
		DC.storeAllData(data);
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList editPriority(Task task, int priority) {
		task.changePriority(priority);
		toDoSortedList.updateTaskOrder(activeTaskList);
		DC.storeAllData(data);
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList addTag(Task task, String tag) {
		if (task.getTags().contains(tag)) {
			return null;
		}
		task.addTag(tag);
		DC.storeAllData(data);
		return toDoSortedList;
	}
	
	@Override
	public ToDoSortedList removeTag(Task task, String tag) {
		if (!task.getTags().contains(tag)) {
			return null;
		}
		task.removeTag(tag);
		DC.storeAllData(data);
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList moveToArchive(Task task, Date finishedTime) {
		task.moveToArchive(finishedTime);
		int id = task.getId();
		activeTaskList.removeTaskbyId(id);
		archivedTaskList.addTask(id, task);
		try {
			toDoSortedList.deleteTask(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DC.storeAllData(data);
		return toDoSortedList;
	}
	
	@Override
	public ToDoSortedList deleteTask(Task task) {
		if (!task.isArchived()){
			try {
				toDoSortedList.deleteTask(task);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			activeTaskList.removeTaskbyId(task.getId());
		}
		else{
			archivedTaskList.removeTaskbyId(task.getId());
		}
		DC.storeAllData(data);
		return toDoSortedList;
	}
	
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
		try {
			archivedTaskList.removeTaskbyId(task.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DC.storeAllData(data);
		return viewArchiveTasks();
	}
	
	@Override
	public String exit(int serialNumber) {
		data.setSerialNumber(serialNumber);
		return DC.storeAllData(data);
	}
}
