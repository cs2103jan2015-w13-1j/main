import java.util.ArrayList;
import java.util.Map.Entry;

import basicElements.*;
import hashMaps.*;
import treeSets.*;

public class LogicController implements InterfaceForLogic{
	
	private StorageController DC = new StorageController();
	private DATA data;
	
	private int serialNumber;

	private TaskList activeTaskList;
	private TaskList archivedTaskList;

	private ToDoSortedList toDoSortedList;
	
	public void initialise() {
		data = DC.getAllData();
		
		serialNumber = data.getSerialNumber();
		activeTaskList = data.getActiveTaskList();
		archivedTaskList = data.getArchivedTaskList();

		toDoSortedList = new ToDoSortedList();
		for (Entry<Integer, Task> e: activeTaskList.entrySet()) {
			toDoSortedList.addTask(e.getValue());
		}
	}
	
	public ToDoSortedList addTask(Task task) {
		toDoSortedList.add(task);
		activeTaskList.addTask(task.getId(), task);
		return toDoSortedList;
	}
	
	public String exitProgram() {
		return DC.storeAllData(data);
	}

	public ToDoSortedList addDeadLine(Task task, Date deadline) {
		if (task.getType() == "generic") {
			task.addDeadline(deadline);
			task.setType("deadline");
			toDoSortedList.updateTaskOrder(task);
			return toDoSortedList;
		}
		else {
			return null;
		}
	}
	
	public ToDoSortedList editDeadline(Task task, Date deadline) {
		if (task.getType() != "deadline") {
			return null;
		}
		task.changeDeadline(deadline);
		toDoSortedList.updateTaskOrder(task);
		return toDoSortedList;
	}

	public ToDoSortedList addStartAndEndTime(Task task, Date start, Date end) {
		if (task.getType() == "generic") {
			task.addStartAndEndTime(start, end);
			task.setType("meeting");
			toDoSortedList.updateTaskOrder(task);
			return toDoSortedList;
		}
		else {
			return null;
		}
	}

	public ToDoSortedList editStartTime(Task task, Date start) {	
		if (task.getType() != "meeting") {
			return null;
		}
		task.changeStartTime(start);
		toDoSortedList.updateTaskOrder(task);
		return toDoSortedList;
	}

	public ToDoSortedList editEndTime(Task task, Date end) {
		if (task.getType() != "meeting") {
			return null;
		}
		task.changeEndTime(end);
		return toDoSortedList;
	}

	
	public ToDoSortedList moveToArchive(Task task, Date finishedTime) {
		task.moveToArchive(finishedTime);
		int id = task.getId();
		activeTaskList.removeTaskbyId(id);
		archivedTaskList.addTask(id, task);
		toDoSortedList.deleteTask(task);
		return toDoSortedList;
	}

	public ArchiveSortedList viewArchiveTasks() {
		ArchiveSortedList archiveSortedList = new ArchiveSortedList();
		for (Entry<Integer, Task> e: archivedTaskList.entrySet()) {
			archiveSortedList.addTask(e.getValue());
		}
		return archiveSortedList;
	}
	

	public ToDoSortedList deleteTask(Task task) {
		if (!task.isArchived()){
			toDoSortedList.deleteTask(task);
			activeTaskList.removeTaskbyId(task.getId());
		}
		else{
			archivedTaskList.removeTaskbyId(task.getId());
		}
		return toDoSortedList;
	}

	
	@Override
	// Only for tasks in the to do list
	public ArrayList<Task> searchByDate(Date searchDate) {
		String date = searchDate.getDateRepresentation();
		ArrayList<Task> taskOnDate = new ArrayList<Task>();
		for (Entry<Integer, Task> e: activeTaskList.entrySet()) {
			Task task = e.getValue();
			Date dateOfTask = task.getTime();
			if (dateOfTask != null) {
				if (dateOfTask.getDateRepresentation() == date) {
					taskOnDate.add(task);
				}
				else {
					if (task.getEndTime().getDateRepresentation() == date) {
						taskOnDate.add(task);
					}
				}
			}
		}
		return taskOnDate;
	}

	// Only for tasks in the to do list
	public ArrayList<Task> searchByTag(String tag) {
		ArrayList<Task> taskByTag = new ArrayList<Task>();
		for (Entry<Integer, Task> e: activeTaskList.entrySet()) {
			Task task = e.getValue();
			ArrayList<String> tags = task.getTags();
			if (tags.contains(tag)) {
				taskByTag.add(task);
			}
		}
		return taskByTag;
	}
	
	// Only for tasks in the to do list
	public ArrayList<Task> searchByPriority(int priority) {
		ArrayList<Task> taskByPriority = new ArrayList<Task>();
		for (Entry<Integer, Task> e: activeTaskList.entrySet()) {
			Task task = e.getValue();
			if (task.getPriority() == priority) {
				taskByPriority.add(task);
			}
		}
		return taskByPriority;
	}

	@Override
	public int getSerialNumber() {
		return serialNumber;
	}

	@Override
	public ToDoSortedList editDescription(Task task, String newDescription) {
		task.changeDescription(newDescription);
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList editPriority(Task task, int priority) {
		task.changePriority(priority);
		toDoSortedList.updateTaskOrder(task);
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList addTag(Task task, String tag) {
		if (task.getTags().contains(tag)) {
			return null;
		}
		task.addTag(tag);
		return toDoSortedList;
	}
	
	@Override
	public ToDoSortedList removeTag(Task task, String tag) {
		if (!task.getTags().contains(tag)) {
			return null;
		}
		task.removeTag(tag);
		return toDoSortedList;
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
}
