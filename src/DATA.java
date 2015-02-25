import hashMaps.*;
import treeSets.*;

/**
 * To store all the data
 * Used during initialization and closing of the program
 */
public class DATA {
	
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
	
	/**
	 * @return the activeTaskList
	 */
	public TaskList getActiveTaskList() {
		return activeTaskList;
	}
	/**
	 * @param activeTaskList the activeTaskList to set
	 */
	public void setActiveTaskList(TaskList activeTaskList) {
		this.activeTaskList = activeTaskList;
	}
	
	/**
	 * @return the archivedTaskList
	 */
	public TaskList getArchivedTaskList() {
		return archivedTaskList;
	}
	/**
	 * @param archivedTaskList the archivedTaskList to set
	 */
	public void setArchivedTaskList(TaskList archivedTaskList) {
		this.archivedTaskList = archivedTaskList;
	}
	
	/**
	 * @return the tagList
	 */
	public TagList getTagList() {
		return tagList;
	}
	/**
	 * @param tagList the tagList to set
	 */
	public void setTagList(TagList tagList) {
		this.tagList = tagList;
	}
	
	/**
	 * @return the dateList
	 */
	public DateList getDateList() {
		return dateList;
	}
	/**
	 * @param dateList the dateList to set
	 */
	public void setDateList(DateList dateList) {
		this.dateList = dateList;
	}
	
	/**
	 * @return the priorityList
	 */
	public PriorityList getPriorityList() {
		return priorityList;
	}
	/**
	 * @param priorityList the priorityList to set
	 */
	public void setPriorityList(PriorityList priorityList) {
		this.priorityList = priorityList;
	}
	
	/**
	 * @return the archiveSortedList
	 */
	public ArchiveSortedList getArchiveSortedList() {
		return archiveSortedList;
	}
	/**
	 * @param archiveSortedList the archiveSortedList to set
	 */
	public void setArchiveSortedList(ArchiveSortedList archiveSortedList) {
		this.archiveSortedList = archiveSortedList;
	}
	
	/**
	 * @return the prioritySortedList
	 */
	public PrioritySortedList getPrioritySortedList() {
		return prioritySortedList;
	}
	/**
	 * @param prioritySortedList the prioritySortedList to set
	 */
	public void setPrioritySortedList(PrioritySortedList prioritySortedList) {
		this.prioritySortedList = prioritySortedList;
	}
	
	/**
	 * @return the toDoSortedList
	 */
	public TodoSortedList getToDoSortedList() {
		return toDoSortedList;
	}
	/**
	 * @param toDoSortedList the toDoSortedList to set
	 */
	public void setToDoSortedList(TodoSortedList toDoSortedList) {
		this.toDoSortedList = toDoSortedList;
	}
	

	
}
