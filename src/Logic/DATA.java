// @author A0113598X
package Logic;


/**
 * To store all the data
 * Used during initialisation and closing of the program
 */
public class DATA {
	
	private int serialNumber;
	private int recurrenceId;
	
	//The hashMaps
	private TaskList activeTaskList;
	private TaskList archivedTaskList;
	
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
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	/**
	 * increment the serialNumber
	 */
	public void incrementSerialNumber() {
		this.serialNumber ++;
	}
	
	/**
	 * @return the recurrenceId
	 */
	public int getRecurrenceId() {
		return this.recurrenceId;
	}
	
	/**
	 * @param recurrenceId the recurrenceId to set
	 */
	public void setRecurrenceId(int recurrenceId) {
		this.recurrenceId = recurrenceId;
	}
	
	public void increamentRecurrenceId() {
		this.recurrenceId++;
	}
	
	/**
	 * For testing purpose only
	 */
	public void clearAllData() {
		this.activeTaskList = new TaskList();
		this.archivedTaskList = new TaskList();
	}
}
