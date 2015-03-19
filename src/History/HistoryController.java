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
		if (this.logList.size() == 5) {
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
		DATA previousData = this.logList.pop();
		return previousData;
	}
	
}
