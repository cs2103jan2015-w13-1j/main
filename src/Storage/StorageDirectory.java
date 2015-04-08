// @author A0111866E

/**
 * This java class is the directory class for the Storage component in the software architecture.
 * 
 * Interface:	InterfaceForStorageDirectory.java
 */
package Storage;

import java.io.File;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

public class StorageDirectory implements InterfaceForStorageDirectory {

	private static final String STRING_RECURRENCE_ID = "recurrenceId";
	private static final String STRING_SERIAL_NUMBER = "serialNumber";
	private static final String STRING_ARCHIVED_TASK_LIST = "archivedTaskList";
	private static final String STRING_ACTIVE_TASK_LIST = "activeTaskList";
	private static final String MESSAGE_SUCCESS_EXPORT = "Successful export to %1$s";
	private static final String MESSAGE_ERROR_EXPORT = "Unsuccessful export.";
	private StorageDatastore _datastore = new StorageDatastore();
	private final static Logger logger = Logger.getLogger(StorageDirectory.class.getName());
	

	@Override
	public String changeFileDirectory(String directory) {
		_datastore.changeDirectory(directory);
		return _datastore.getDirectory();
	}

	@Override
	public String getFileDirectory() {
		return _datastore.getDirectory();
	}

	@Override
	public String getFileName() {
		return _datastore.getStorageName();
	}

	@Override
	public boolean importFromDirectory(String file) {
		File importedFile = new File(file);
		URI uri = importedFile.toURI();
		String importedFileAbsolutePath = uri.getPath().replaceFirst("/", "");
		if (_datastore.isStorageExist(importedFileAbsolutePath)) {
			return importPhaseOne(importedFileAbsolutePath);
		} else {
			// file not found
			return false;
		}
	}
	
	/**
	 * @param importedFileAbsolutePath
	 * @return result
	 */
	private boolean importPhaseOne(String importedFileAbsolutePath) {
		JSONObject importedJsonData = _datastore.retrieveDataFromStorage(importedFileAbsolutePath);
		if (importedJsonData.containsKey(STRING_SERIAL_NUMBER) == false || importedJsonData.containsKey(STRING_ACTIVE_TASK_LIST) == false
				|| importedJsonData.containsKey(STRING_RECURRENCE_ID) == false || importedJsonData.containsKey(STRING_ARCHIVED_TASK_LIST) == false) {
			// wrong format of data
			return false;
		} else {
			File file = new File(importedFileAbsolutePath);
			String importedFileFolderAbsolutePath = file.getParentFile().toURI().getPath().replaceFirst("/", "");
			String importedFileName= file.getName();
			importPhaseTwo(importedFileFolderAbsolutePath, importedFileName);
			importPhaseThree(importedFileAbsolutePath, importedFileFolderAbsolutePath, importedFileName);
			return true;
		}
	}

	/**
	 * @param importedFileAbsolutePath
	 * @param importedFileFolderAbsolutePath
	 * @param importedFileName
	 */
	private void importPhaseThree(String importedFileAbsolutePath, String importedFileFolderAbsolutePath, String importedFileName) {
		if (_datastore.getDirectory().equals(importedFileFolderAbsolutePath) && _datastore.getStorageName().equals(importedFileName)) {
			logger.log(Level.INFO, "Successful import from " + importedFileAbsolutePath);
		} else {
			logger.log(Level.WARNING, "Unsuccessful import from " + importedFileAbsolutePath);
		}
	}

	/**
	 * @param importedFileFolderAbsolutePath
	 * @param importedFileName
	 */
	private void importPhaseTwo(String importedFileFolderAbsolutePath, String importedFileName) {
		_datastore.setDirectory(importedFileFolderAbsolutePath);
		_datastore.setStorageName(importedFileName);
		_datastore.saveSettingsToUtility();
		_datastore.initialise();
	}

	@Override
	public boolean exportToDirectory(String file) {
		File exportFile = new File(file);
		URI uri = exportFile.toURI();
		String importedFileAbsolutePath = uri.getPath().replaceFirst("/", "");
		_datastore.processStorage(importedFileAbsolutePath);
		return logExportResult(importedFileAbsolutePath);
	}

	/**
	 * @param importedFileAbsolutePath
	 * @return result
	 */
	private boolean logExportResult(String importedFileAbsolutePath) {
		if (_datastore.storeJsonIntoStorage(_datastore.getData(), importedFileAbsolutePath) == true) {
			logger.log(Level.INFO, String.format(MESSAGE_SUCCESS_EXPORT, importedFileAbsolutePath));
			return true;
		} else {
			logger.log(Level.WARNING, MESSAGE_ERROR_EXPORT);
			return false;
		}
	}
}
