/**
 * @author Esmond
 */

package Storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class StorageDatastore {
	
	private String _directory;
	private String _storageName;
	private static final int NEGATIVE_ONE = -1;
	private static final String STRING_DIRECTORY = "directory";
	private static final String STRING_STORAGE_NAME = "storageName";
	private static final String STRING_UTILITY_FILE_NAME = "tables/utility.json";
	private static final String STRING_DEFAULT_STORAGE_NAME_VALUE = "storage.json";
	private static final String STRING_DEFAULT_DIRECTORY_VALUE = "tables/";
	private static final String STRING_RECURRENCE_ID = "recurrenceId";
	private static final String STRING_SERIAL_NUMBER = "serialNumber";
	private static final String STRING_ARCHIVED_TASK_LIST = "archivedTaskList";
	private static final String STRING_ACTIVE_TASK_LIST = "activeTaskList";
	private static final String MESSAGE_INIT_ERROR_INVALID_CONTENT = STRING_UTILITY_FILE_NAME + " content invalid.";
	private static final String MESSAGE_INIT_ERROR_STORAGE_NOT_FOUND = STRING_UTILITY_FILE_NAME + " does not exist.";
	private static final String MESSAGE_NEW_FILE_DIRECTORY = "New directory : %1$s";
	private static final String MESSAGE_CREATE_STORAGE_FILE = "Creating storage file : %1$s";
	private static final String MESSAGE_ADD_DEFAULT_UTIL_SETTINGS = "Add default settings to utility. Directory: \"tables/\" Storage: \"storage.json\"";
	private static final String MESSAGE_PROCESS_STORAGE_NOT_FOUND = "Storage does not exist.";
	private static final String MESSAGE_GET_ALL_DATA_STORAGE_NOT_EXIST = "Storage file does not exist.";
	private static final String MESSAGE_GET_ALL_DATA_STORAGE_EXIST = "Storage file exists. Retrieving DATA from storage.";
	private static final String MESSAGE_RETRIEVE_SUCCESS = "Data is retrieved.";
	private static final String MESSAGE_RETRIEVE_FROM_EMPTY_FILE = "File is empty. No data is retrieved.";
	private static final String MESSAGE_DELETE_FILE = "%1$s is deleted.";
	private static final String MESSAGE_STORE_FAILURE = "%1$s not saved.";
	private static final String MESSAGE_STORE_SUCCESS = "%1$s is saved successfully.";
	private static final String MESSAGE_RETRIEVE_ERROR_FILE_CONTENT_INVALID = "File content invalid. No data is retrieved.";
	private final static Logger logger = Logger.getLogger(StorageController.class.getName());
	
	public static void main(String[] args) {
//		StorageDatastore control = new StorageDatastore();
//		control.setDirectory("test");
//		System.out.println(control.getData().toJSONString());
	}
	
	/**
	 * Default constructor
	 */
	public StorageDatastore() {
		this.initialise();
	}
	
	/**
	 * Retrieve datastore settings if exists, otherwise, creates new utility file and set it with default settings
	 */
	private void initialise() {
		if (isStorageExist(STRING_UTILITY_FILE_NAME) == false) {
			initPhaseOne();
		} else {
			initPhaseTwo();
		}
	}

	private void initPhaseTwo() {
		JSONObject utilJSON = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(STRING_UTILITY_FILE_NAME));
			utilJSON = (JSONObject) obj;
			initPhaseThree(utilJSON);
			
		} catch (IOException | ParseException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	private void initPhaseThree(JSONObject utilJSON) {
		if (utilJSON.containsKey(STRING_DIRECTORY) == false) {
			logger.log(Level.WARNING, MESSAGE_INIT_ERROR_INVALID_CONTENT);
			resetToDefaultSettings();
		} else {
			this._storageName = String.valueOf(utilJSON.get(STRING_STORAGE_NAME));
			this._directory = String.valueOf(utilJSON.get(STRING_DIRECTORY));					
		}
	}

	private void initPhaseOne() {
		logger.log(Level.WARNING, MESSAGE_INIT_ERROR_STORAGE_NOT_FOUND);
		createStorage(STRING_UTILITY_FILE_NAME);
		resetToDefaultSettings();
	}
	
	/**
	 * @param json object
	 * @return true if storing is successful
	 */
	public boolean storeJsonIntoStorage(JSONObject json) {
		initialise();
		processStorage();
		return storeJsonIntoStorage(json, getStorageRelativePath());
	}
	
	/**
	 * @return data in JSON object 
	 */
	public JSONObject getData() {
		initialise();
		JSONObject dataJSON = new JSONObject();
		if (isStorageExist(getStorageRelativePath()) == true) {
			logger.log(Level.INFO, MESSAGE_GET_ALL_DATA_STORAGE_EXIST);
			dataJSON = retrieveDataFromStorage(getStorageRelativePath());
		} else {
			// must store data first, cannot create new storage because user might change directory
			logger.log(Level.WARNING, MESSAGE_GET_ALL_DATA_STORAGE_NOT_EXIST);
		}
		return dataJSON;
	}
	
	/**
	 * @return data in JSON object 
	 */
	public JSONObject retrieveDataFromStorage(String fileName) {
		// do nothing if task list is empty
		JSONObject dataJSON = new JSONObject();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			if (br.readLine() == null) {
				logger.log(Level.WARNING, MESSAGE_RETRIEVE_FROM_EMPTY_FILE);
				br.close();
				return dataJSON;
			}
			br.close();
		} catch (IOException e1) {
			logger.log(Level.SEVERE, e1.getMessage());
		}
		
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(fileName));
			dataJSON = (JSONObject) obj; 
			if (dataJSON.containsKey(STRING_SERIAL_NUMBER) == false || dataJSON.containsKey(STRING_ACTIVE_TASK_LIST) == false
					|| dataJSON.containsKey(STRING_RECURRENCE_ID) == false || dataJSON.containsKey(STRING_ARCHIVED_TASK_LIST) == false) {
				logger.log(Level.WARNING, MESSAGE_RETRIEVE_ERROR_FILE_CONTENT_INVALID);
				dataJSON = new JSONObject();
				return dataJSON;
			}
			
		} catch (IOException | ParseException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		logger.log(Level.INFO, MESSAGE_RETRIEVE_SUCCESS);
		return dataJSON;
	}
	
	/**
	 * Reset datastore directory and storage name to default
	 */
	private void resetToDefaultSettings() {
		logger.log(Level.INFO, MESSAGE_ADD_DEFAULT_UTIL_SETTINGS);
		this._directory = STRING_DEFAULT_DIRECTORY_VALUE;			// default settings
		this._storageName = STRING_DEFAULT_STORAGE_NAME_VALUE;	// default settings
		saveSettingsToUtility();
	}

	/**
	 * Save settings to utility file
	 */
	@SuppressWarnings("unchecked")
	private void saveSettingsToUtility() {
		JSONObject utilJSON = new JSONObject();
		utilJSON.put(STRING_DIRECTORY, this._directory);
		utilJSON.put(STRING_STORAGE_NAME, this._storageName);
		storeJsonIntoStorage(utilJSON, STRING_UTILITY_FILE_NAME);
	}
	
	/**
	 * Create new storage if storage not found
	 */
	private void processStorage() {
		if (isStorageExist(getStorageRelativePath()) == false) {
			logger.log(Level.WARNING, MESSAGE_PROCESS_STORAGE_NOT_FOUND);
			createStorage(getStorageRelativePath());
		}
	}
	
	/**
	 * @param json object
	 * @param file name
	 * @return true if storing is successful
	 */
	private boolean storeJsonIntoStorage(JSONObject json, String fileName) {
		File file = new File(fileName);
		long timeBeforeModification = file.lastModified();
		long timeAfterModification = NEGATIVE_ONE;
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(json.toJSONString());
			fileWriter.flush();  
			fileWriter.close();
			timeAfterModification = file.lastModified();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		boolean result = timeAfterModification > timeBeforeModification;
		logStoreResult(fileName, result);
		return result;
	}

	/**
	 * @param fileName
	 * @param result
	 */
	private void logStoreResult(String fileName, boolean result) {
		if (result == true) {
			logger.log(Level.INFO, String.format(MESSAGE_STORE_SUCCESS, fileName));
		} else {
			logger.log(Level.WARNING, String.format(MESSAGE_STORE_FAILURE, fileName));
		}
	}
	
	/**
	 * @param File Relative Path
	 * @return True if file exist
	 */
	public boolean isStorageExist(String fileRelativePath) {
		File file = new File(fileRelativePath);
		return file.exists();
	}
	
	/**
	 * @param File Relative Path
	 */
	private void createStorage(String fileRelativePath) {
		logger.log(Level.INFO, String.format(MESSAGE_CREATE_STORAGE_FILE, fileRelativePath));
		try {
			File storageFile = new File(fileRelativePath);
			if (storageFile.getParentFile() != null) {
				storageFile.getParentFile().mkdirs();
			}
			storageFile.createNewFile();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	/**
	 * @return directory
	 */
	public String getDirectory() {
		return _directory;
	}
	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		deleteFile(getStorageRelativePath());	// remove old storage file if it exists
		URI uri = fileToUri(directory);
		String absolutePath = uriToString(uri);
		this._directory = new String();
		this._directory = absolutePath;
		saveSettingsToUtility();
		logger.log(Level.INFO, String.format(MESSAGE_NEW_FILE_DIRECTORY, this.getDirectory()));
	}

	/**
	 * @param uri
	 * @return string
	 */
	private String uriToString(URI uri) {
		String absolutePath = uri.getPath().replaceFirst("/", "");
		if (absolutePath.endsWith("/") == false) {
			absolutePath = absolutePath.concat("/");
		}
		return absolutePath;
	}

	/**
	 * @param directory
	 * @return Uri
	 */
	private URI fileToUri(String directory) {
		File newDirectory = new File(directory);
		URI uri = newDirectory.toURI();
		return uri;
	}
	
	/**
	 * @param fileName
	 */
	private void deleteFile(String fileName) {
		File oldFile = new File(fileName);
		oldFile.setWritable(true);
		if (oldFile.exists()) {
			oldFile.delete();
			logger.log(Level.INFO, String.format(MESSAGE_DELETE_FILE, fileName));
		}
	}
	
	/**
	 * @return the storageName
	 */
	public String getStorageName() {
		return _storageName;
	}
	/**
	 * @param storageName the storageName to set
	 */
	public void setStorageName(String storageName) {
		this._storageName = storageName;
	}
	
	/**
	 * @return directory + storageName
	 */
	public String getStorageRelativePath() {
		String relativePath = getDirectory().concat(getStorageName());
		return relativePath;
	}
}
