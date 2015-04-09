// @author A0111866E

/**
 * This java class is the controller for the History component in the software architecture.
 */

package History;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Storage.StorageController;
import Common.DATA;

public class HistoryController implements InterfaceForHistory {

	private static final String MESSAGE_ERROR_REDO = "Redo not successful. No previous command entry found.";
	private static final String MESSAGE_ERROR_UNDO = "Undo not successful. No previous command entry found.";
	private static final String MESSAGE_ERROR_LOG = "Log previous DATA was not successful.";
	private static final String MESSAGE_SUCCESS_LOG = "Log previous DATA successfully.";
	private static final String MESSAGE_SUCCESS_INITIALISE = "Log list is initialised.";
	private final int SIZE = 10;
	private LinkedList<DATA> _logList;
	private LinkedList<DATA> _redoList;
	private StorageController _storageControl;
	private final static Logger logger = Logger.getLogger(StorageController.class.getName());
	
	public HistoryController() {
		this._logList = new LinkedList<DATA>();
		this._redoList = new LinkedList<DATA>();
		_storageControl = new StorageController();
		logger.log(Level.INFO, MESSAGE_SUCCESS_INITIALISE);
	}

	@Override
	public boolean log() {
		manageLogListEntryLimit();
		// clear redoList
		this._redoList.clear();
		return executeLog();
	}

	private boolean executeLog() {
		DATA previousData = _storageControl.getAllData();
		this._logList.push(previousData);
		if (this._logList.contains(previousData) == true) {
			logger.log(Level.INFO, MESSAGE_SUCCESS_LOG);
			return true;
		} else {
			logger.log(Level.WARNING, MESSAGE_ERROR_LOG);
			return false;
		}
	}

	private void manageLogListEntryLimit() {
		if (this._logList.size() == SIZE) {
			this._logList.removeLast();
		}
	}

	@Override
	public DATA undo() {
		if (this._logList.isEmpty() == true) {
			logger.log(Level.WARNING, MESSAGE_ERROR_UNDO);
			return null;
		}
		return executeUndo();
	}

	private DATA executeUndo() {
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
			logger.log(Level.WARNING, MESSAGE_ERROR_REDO);
			return null;
		}
		return executeRedo();
	}

	private DATA executeRedo() {
		// retrieve DATA from storage and push into redoList
		DATA currentData = _storageControl.getAllData();
		this._logList.push(currentData);
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
