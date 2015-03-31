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

	private static final String MESSAGE_REDO_NOT_SUCCESSFUL = "Redo not successful. No previous command entry found.";
	private static final String MESSAGE_UNDO_NOT_SUCCESSFUL = "Undo not successful. No previous command entry found.";
	private static final String MESSAGE_LOG_PREVIOUS_DATA_WAS_NOT_SUCCESSFUL = "Log previous DATA was not successful.";
	private static final String MESSAGE_LOG_PREVIOUS_DATA_SUCCESSFULLY = "Log previous DATA successfully.";
	private static final String MESSAGE_LOG_LIST_IS_INITIALISED = "Log list is initialised.";
	private final int SIZE = 10;
	private LinkedList<DATA> _logList;
	private LinkedList<DATA> _redoList;
	private StorageController _storageControl;
	private final static Logger logger = Logger.getLogger(StorageController.class.getName());
	
	public HistoryController() {
		this._logList = new LinkedList<DATA>();
		this._redoList = new LinkedList<DATA>();
		_storageControl = new StorageController();
//		logger.log(Level.INFO, MESSAGE_LOG_LIST_IS_INITIALISED);
	}

	@Override
	public boolean log() {
		if (this._logList.size() == SIZE) {
			this._logList.removeLast();
		}
		// clear redoList
		this._redoList.clear();
		
		DATA previousData = _storageControl.getAllData();
		this._logList.push(previousData);
		if (this._logList.contains(previousData) == true) {
//			logger.log(Level.INFO, MESSAGE_LOG_PREVIOUS_DATA_SUCCESSFULLY);
			return true;
		} else {
			logger.log(Level.WARNING, MESSAGE_LOG_PREVIOUS_DATA_WAS_NOT_SUCCESSFUL);
			return false;
		}
	}

	@Override
	public DATA undo() {
		if (this._logList.isEmpty() == true) {
			logger.log(Level.WARNING, MESSAGE_UNDO_NOT_SUCCESSFUL);
			return null;
		}
		// retrieve DATA from storage and push into redoList
		DATA currentData = _storageControl.getAllData();
		this._redoList.push(currentData);
		// pop DATA from logList and return DATA
		DATA previousData = this._logList.pop();
		return previousData;
	}

	@Override
	public DATA redo() {
		if (this._redoList.isEmpty() == true) {
			logger.log(Level.WARNING, MESSAGE_REDO_NOT_SUCCESSFUL);
			return null;
		}
		// pop DATA from redoList and return DATA
		DATA previousData = this._redoList.pop();
		return previousData;
	}

	/**
	 * @return the logList
	 */
	public LinkedList<DATA> getLogList() {
		return _logList;
	}

	/**
	 * @param logList the logList to set
	 */
	public void setLogList(LinkedList<DATA> logList) {
		this._logList = logList;
	}

	/**
	 * @return the redoList
	 */
	public LinkedList<DATA> getRedoList() {
		return _redoList;
	}

	/**
	 * @param redoList
	 */
	public void setRedoList(LinkedList<DATA> redoList) {
		this._redoList = redoList;
	}
	
}
