/**
 * This java class is the controller for the History component in the software architecture.
 * 
 * @author Esmond
 */

package History;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Storage.StorageController;
import Common.DATA;

public class HistoryController implements InterfaceForHistory{

	private final int SIZE = 10;
	private LinkedList<DATA> logList;
	private StorageController storageControl;
	private final static Logger logger = Logger.getLogger(StorageController.class.getName());
	
	public HistoryController() {
		this.logList = new LinkedList<DATA>();
		storageControl = new StorageController();
		logger.log(Level.INFO, "Log list is initialised.");
	}

	@Override
	public boolean log() {
		if (this.logList.size() == SIZE) {
			this.logList.removeLast();
		}
		
		DATA previousData = storageControl.getAllData();
		this.logList.push(previousData);
		if (this.logList.contains(previousData) == true) {
			logger.log(Level.INFO, "Log previous DATA successfully.");
			return true;
		} else {
			logger.log(Level.WARNING, "Log previous DATA was not successful.");
			return false;
		}
	}

	@Override
	public DATA undo() {
		if (this.logList.isEmpty() == true) {
			logger.log(Level.WARNING, "Undo not successful. No previous command entry found.");
			return null;
		}
		DATA previousData = this.logList.pop();
		return previousData;
	}

	/**
	 * @return the logList
	 */
	public LinkedList<DATA> getLogList() {
		return logList;
	}

	/**
	 * @param logList the logList to set
	 */
	public void setLogList(LinkedList<DATA> logList) {
		this.logList = logList;
	}
	
}
