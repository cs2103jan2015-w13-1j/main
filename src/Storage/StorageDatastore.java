// @author A0111866E

/**
 * This java class is the datastore for the Storage component in the software architecture.
 * 
 * Dependency files: 
 * - jar/json-simpler-1.1.1.jar for JSON library
 * 
 * Test driver: StorageADT.java
 * Interface:	InterfaceForStorage.java
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
	public void initialise() {
		if (isStorageExist(STRING_UTILITY_FILE_NAME) == false) {
			initPhaseOne();
		} else {
			initPhaseTwo();
		}
	}

	/**
	 * initialise phase two: get JSON object from file
	 */
	private void initPhaseTwo() {
		JSONObject utilJSON = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			FileReader readFile = new FileReader(STRING_UTILITY_FILE_NAME);
			Object obj = parser.parse(readFile);
			readFile.close();
			utilJSON = (JSONObject) obj;
			initPhaseThree(utilJSON);
			
		} catch (IOException | ParseException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * initialise phase three: read JSON attribute
	 * @param utilJSON
	 */
	private void initPhaseThree(JSONObject utilJSON) {
		if (utilJSON.containsKey(STRING_DIRECTORY) == false) {
			logger.log(Level.WARNING, MESSAGE_INIT_ERROR_INVALID_CONTENT);
			resetToDefaultSettings();
		} else {
			this._storageName = String.valueOf(utilJSON.get(STRING_STORAGE_NAME));
			this._directory = String.valueOf(utilJSON.get(STRING_DIRECTORY));					
		}
	}

	/**
	 * initialise phase one: check on utility.json 
	 */
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
		processStorage(getStorageFilePath());
		return storeJsonIntoStorage(json, getStorageFilePath());
	}
	
	/**
	 * @return data in JSON object 
	 */
	public JSONObject getData() {
		initialise();
		JSONObject dataJSON = new JSONObject();
		if (isStorageExist(getStorageFilePath()) == true) {
			logger.log(Level.INFO, MESSAGE_GET_ALL_DATA_STORAGE_EXIST);
			dataJSON = retrieveDataFromStorage(getStorageFilePath());
		} else {
			// must store data first, cannot create new storage because user might change directory
			logger.log(Level.WARNING, MESSAGE_GET_ALL_DATA_STORAGE_NOT_EXIST);
		}
		return dataJSON;
	}
	
	/**
	 * @param fileName
	 * @return data in JSON object 
	 */
	public JSONObject retrieveDataFromStorage(String fileName) {
		// do nothing if task list is empty
		JSONObject dataJSON = new JSONObject();
		try {
			FileReader fileRead = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fileRead);
			if (br.readLine() == null) {
				logger.log(Level.WARNING, MESSAGE_RETRIEVE_FROM_EMPTY_FILE);
				br.close();
				fileRead.close();
				return dataJSON;
			} 
			br.close();
			fileRead.close();
		} catch (IOException e1) {
			logger.log(Level.SEVERE, e1.getMessage());
		}
		
		try {
			JSONParser parser = new JSONParser();
			FileReader fileRead = new FileReader(fileName);
			Object obj = parser.parse(fileRead);
			fileRead.close();
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
	public void saveSettingsToUtility() {
		JSONObject utilJSON = new JSONObject();
		utilJSON.put(STRING_DIRECTORY, this._directory);
		utilJSON.put(STRING_STORAGE_NAME, this._storageName);
		storeJsonIntoStorage(utilJSON, STRING_UTILITY_FILE_NAME);
	}
	
	/**
	 * @param storage
	 * Create new storage if storage not found
	 */
	public void processStorage(String storage) {
		if (isStorageExist(storage) == false) {
			logger.log(Level.WARNING, MESSAGE_PROCESS_STORAGE_NOT_FOUND);
			createStorage(storage);
		}
	}
	
	/**
	 * @param json object
	 * @param fileName
	 * @return true if storing is successful
	 */
	public boolean storeJsonIntoStorage(JSONObject json, String fileName) {
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
	 * @param filePath Relative Path
	 * @return True if file exist
	 */
	public boolean isStorageExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	 * @param filePath
	 */
	private void createStorage(String filePath) {
		logger.log(Level.INFO, String.format(MESSAGE_CREATE_STORAGE_FILE, filePath));
		try {
			File storageFile = new File(filePath);
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
		initPhaseTwo();
		return this._directory;
	}
	/**
	 * @param directory the _directory to set
	 */
	public void setDirectory(String directory) {
		this._directory = directory;
	}
	/**
	 * @param directory the directory to set
	 */
	public void changeDirectory(String directory) {
		initialise();
		JSONObject dataJSON = new JSONObject();
		String previousDirectory = getStorageFilePath();
		
		dataJSON = changeDirectoryPhaseOne(dataJSON);
		changeDirectoryPhaseTwo(directory);
		changeDirectoryPhaseThree(dataJSON, previousDirectory);
		logger.log(Level.INFO, String.format(MESSAGE_NEW_FILE_DIRECTORY, this.getDirectory()));
	}

	/**
	 * change directory phase three: store existing storage data, if any, into new directory 
	 * @param dataJSON
	 * @param previousDirectory
	 */
	private void changeDirectoryPhaseThree(JSONObject dataJSON,	String previousDirectory) {
		if ((dataJSON.containsKey(STRING_SERIAL_NUMBER) == false || dataJSON.containsKey(STRING_ACTIVE_TASK_LIST) == false
				|| dataJSON.containsKey(STRING_RECURRENCE_ID) == false || dataJSON.containsKey(STRING_ARCHIVED_TASK_LIST) == false) == false) {
			processStorage(getStorageFilePath());
			storeJsonIntoStorage(dataJSON, getStorageFilePath());
		}
		deleteFile(previousDirectory);	// remove old storage file if it exists
	}

	/**
	 * change directory phase two: save new directory to utility.json
	 * @param directory
	 */
	private void changeDirectoryPhaseTwo(String directory) {
		URI uri = fileToUri(directory);
		String absolutePath = uriToString(uri);
		this._directory = new String();
		this._directory = absolutePath;
		saveSettingsToUtility();
	}

	/**
	 * change directory phase one: retrieve and delete existing storage file if any
	 * @param dataJSON
	 * @return dataJSON
	 */
	private JSONObject changeDirectoryPhaseOne(JSONObject dataJSON) {
		if (isStorageExist(getStorageFilePath()) == true) {
			dataJSON = retrieveDataFromStorage(getStorageFilePath());
			deleteFile(getStorageFilePath());	// remove old storage file if it exists
		}
		return dataJSON;
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
		initPhaseTwo();
		return this._storageName;
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
	public String getStorageFilePath() {
		String path = getDirectory().concat(getStorageName());
		return path;
	}
}
