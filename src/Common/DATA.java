package Common;

/**
 * To store all the data
 * Used during initialization and closing of the program
 */
public class DATA {
	
	private int serialNumber;
	
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
	

	
}
