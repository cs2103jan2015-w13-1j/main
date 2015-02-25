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
	private TodoSortedList toDoSortedList;
	
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
	
	public String addTask(Task task) {
		return null;
	}
	
	public String exitProgram() {
		return DC.storeAllData(data);
	}

}
