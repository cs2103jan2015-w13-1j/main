import java.util.ArrayList;

import basicElements.*;
import hashMaps.*;
import treeSets.*;

public class LogicController implements InterfaceForLogic{
	
	private StorageController DC = new StorageController();
	private DATA data;
	
	private int serialNumber;
	//The hashMaps
	private TaskList activeTaskList;
	private TaskList archivedTaskList;
	private TagList tagList;
	private DateList dateList;
	private PriorityList priorityList;
	
	//The treeSets
	private ArchiveSortedList archiveSortedList;
	private PrioritySortedList prioritySortedList;
	private ToDoSortedList toDoSortedList;
	
	public void initialise() {
		data = DC.getAllData();
		
		serialNumber = data.getSerialNumber();
		activeTaskList = data.getActiveTaskList();
		archivedTaskList = data.getArchivedTaskList();
		tagList = data.getTagList();
		dateList = data.getDateList();
		priorityList = data.getPriorityList();
		
		archiveSortedList = data.getArchiveSortedList();
		prioritySortedList = data.getPrioritySortedList();
		toDoSortedList = data.getToDoSortedList();
	}
	
	public ToDoSortedList addTask(Task task) {
		toDoSortedList.add(task);
		prioritySortedList.add(task);
		
		activeTaskList.addTask(task.getId(), task);
		priorityList.addToDoTask(task.getPriority(), task);
		dateList.addToDoTask(task.getTime().getDateRepresentation(), task);

		addTaskWithTags(task);
		
		return toDoSortedList;
	}

	// Need to modify for archive task
	private void addTaskWithTags(Task task) {
		if (task.getTags()!=null) {
			for (String tag : task.getTags()) {
				tagList.addToDoTask(tag, task);
			}
		}
	}
	
	// Need to modify for archive task
	private void removeTaskWithTags(Task task) {
		if (task.getTags()!=null) {
			for (String tag : task.getTags()) {
				tagList.removeToDoTask(tag, task);
			}
		}
	}
	
	public String exitProgram() {
		return DC.storeAllData(data);
	}

	
	@Override
	public ToDoSortedList editDeadline(DeadlineTask task, Date deadline) {
		String originalDate = task.getTime().getDateRepresentation();
		String newDate = deadline.getDateRepresentation();
		
		dateList.removeToDoTask(originalDate, task);
		dateList.addToDoTask(newDate, task);
		
		task.changeDeadline(deadline);
		toDoSortedList.updateTaskOrder(task);
		
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList addDeadLine(Task task, Date deadline) {
		DeadlineTask newTask = new DeadlineTask(task, deadline);

		deleteTask(task);
		addTask(newTask);

		return null;
	}

	@Override
	public ToDoSortedList addStartAndEndTime(Task task, Date start, Date end) {
		MeetingTask newTask = new MeetingTask(task, start, end);
		
		deleteTask(task);
		addTask(newTask);

		return null;
	}

	@Override
	public ToDoSortedList editStartTime(MeetingTask task, Date start) {
		String originalStart = task.getTime().getDateRepresentation();
		String newDate = start.getDateRepresentation();
		
		dateList.removeToDoTask(originalStart, task);
		dateList.addToDoTask(newDate, task);
		
		task.changeStartTime(start);
		toDoSortedList.updateTaskOrder(task);
		
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList editEndTime(MeetingTask task, Date end) {
		String originalEnd = task.getTime().getDateRepresentation();
		String newDate = end.getDateRepresentation();
		
		dateList.removeToDoTask(originalEnd, task);
		dateList.addToDoTask(newDate, task);
		
		task.changeEndTime(end);
		toDoSortedList.updateTaskOrder(task);
		
		return toDoSortedList;
	}
	
	@Override
	public ToDoSortedList moveToArchive(Task task) {
		task.moveToArchive();
		int id = task.getId();
		
		dateList.get(task.getTime()).moveToArchive(id);
		priorityList.get(task.getPriority()).moveToArchive(id);
		
		activeTaskList.removeTaskbyId(id);
		archivedTaskList.addTask(id, task);
		
		toDoSortedList.deleteTask(task);
		archiveSortedList.addTask(task);
		prioritySortedList.deleteTask(task);
		
		return toDoSortedList;
	}

	@Override
	public ArchiveSortedList viewArchiveTasks() {
		return archiveSortedList;
	}
	

	
	@Override
	public ToDoSortedList deleteTask(Task task) {
		if (!task.isArchived()){
			toDoSortedList.deleteTask(task);
			
			dateList.removeToDoTask(task.getTime().getDateRepresentation(), task);
			priorityList.removeToDoTask(task.getPriority(), task);
			activeTaskList.removeTaskbyId(task.getId());
		}
		else{
			archiveSortedList.deleteTask(task);
			
			dateList.removeArchivedTask(task.getTime().getDateRepresentation(), task);
			priorityList.removeArchivedTask(task.getPriority(), task);
			archivedTaskList.removeTaskbyId(task.getId());
		}
		prioritySortedList.deleteTask(task);
		removeTaskWithTags(task);
			
		return toDoSortedList;
	}



	@Override
	public ToDoSortedList modifyTask(Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	// Only for tasks in the to do list
	public ArrayList<Task> searchByDate(Date searchDate) {
		String date = searchDate.getDateRepresentation();
		if (dateList.hasDate(date)) {
			ArrayList<Integer> ids = dateList.getTodoTaskIdOnDate(date);
			return getActiveTasksByIds(ids);
		}
		return null;
	}
	
	@Override
	// Only for tasks in the to do list
	public ArrayList<Task> searchByTag(String tag) {
		if (tagList.hasTag(tag)) {
			ArrayList<Integer> ids = tagList.getTodoTaskIdWithtag(tag);
			return getActiveTasksByIds(ids);
		}
		return null;
	}
	
	@Override
	// Only for tasks in the to do list
	public ArrayList<Task> searchByPriority(int priority) {
		if (priorityList.hasPriority(priority)) {
			ArrayList<Integer> ids = priorityList.getTodoTaskIdWithPriority(priority);
			return getActiveTasksByIds(ids);
		}
		return null;
	}

	private ArrayList<Task> getActiveTasksByIds(ArrayList<Integer> ids) {
		ArrayList<Task> requiredTasks = new ArrayList<Task>();
		for (int id : ids) {
			requiredTasks.add(activeTaskList.getTaskbyId(id));
		}
		return requiredTasks;
	}

	@Override
	public int getSerialNumber() {
		return serialNumber;
	}
}
