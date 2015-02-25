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

}
