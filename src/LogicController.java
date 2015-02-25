import java.util.ArrayList;

import basicElements.*;
import hashMaps.*;
import treeSets.*;

public class LogicController implements InterfaceForLogic{
	
	private StorageController DC = new StorageController();
	private DATA data;
	
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

	public ToDoSortedList editStartDate(Task task, Date start) {
		// TODO Auto-generated method stub
		return null;
	}

	public ToDoSortedList editDeadLine(DeadlineTask task, Date deadline) {
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
		String newDate = deadline.getDateRepresentation();
		DeadlineTask newTask = new DeadlineTask(task, deadline);

		deleteTask(task);
		addTask(newTask);
		
		dateList.addToDoTask(newDate, newTask);
		return null;
	}


	@Override
	public ArchiveSortedList moveToArchive(Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArchiveSortedList viewArchiveTasks() {
		// TODO Auto-generated method stub
		return null;
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
	public int getSerialNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ToDoSortedList editStartDate(MeetingTask task, Date start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoSortedList editEndDate(MeetingTask task, Date end) {
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
	public ToDoSortedList addStartTime(Task task, Date start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoSortedList addEndTime(Task task, Date start) {
		// TODO Auto-generated method stub
		return null;
	}




}
