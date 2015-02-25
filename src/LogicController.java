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


	private void addTaskWithTags(Task task) {
		if (task.getTags()!=null) {
			for (String tag : task.getTags()) {
				tagList.addToDoTask(tag, task);
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

	public ToDoSortedList editDeadLine(DeadlineTask task, Date deadLine) {
		task.changeDeadline(deadLine);
		dateList.getTodoTaskIdOnDate(deadLine.getDateRepresentation())
		return toDoSortedList;
	}

	@Override
	public ToDoSortedList addDeadLine(Task task, Date deadLine) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralSortedList searchByDate(Date searchDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskByTag searchByTag(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrioritySortedList searchByPriority() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoSortedList modifyTask(Task task) {
		// TODO Auto-generated method stub
		return null;
	}


}
