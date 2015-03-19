/**
 * @author Esmond
 */

package Storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class StorageDatastore {
	private String directory;
	private String storageName;
	private static final String STRING_UTILITY_FILE_NAME = "tables/utility.json";
	private static final String MESSAGE_NEW_FILE_DIRECTORY = "New directory : ";
	private static final String MESSAGE_CREATE_STORAGE_FILE = "Creating storage file ";
	private static final String MESSAGE_ADD_DEFAULT_UTIL_SETTINGS = "Add default settings to utility. Directory: \"tables/\" Storage: \"storage.json\"";
	private static final String MESSAGE_PROCESS_STORAGE_NOT_FOUND = "Storage does not exist.";
	private static final String MESSAGE_GET_ALL_DATA_STORAGE_NOT_EXIST = "Storage file does not exist.";
	private static final String MESSAGE_GET_ALL_DATA_STORAGE_EXIST = "Storage file exists. Retrieving DATA from storage.";
	private static final String MESSAGE_RETRIEVE_SUCCESS = "Data is retrieved.";
	private static final String MESSAGE_RETRIEVE_FROM_EMPTY_FILE = "File is empty. No data is retrieved.";
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
		JSONObject utilJSON = new JSONObject();
		if (isStorageExist(STRING_UTILITY_FILE_NAME) == false) {
			logger.log(Level.WARNING, STRING_UTILITY_FILE_NAME + " does not exist.");
			createStorage(STRING_UTILITY_FILE_NAME);
			resetToDefaultSettings();
		} else {
			logger.log(Level.FINE, STRING_UTILITY_FILE_NAME + " exists.");
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(new FileReader(STRING_UTILITY_FILE_NAME));
				utilJSON = (JSONObject) obj;
				if (utilJSON.containsKey("directory") == false) {
					logger.log(Level.WARNING, STRING_UTILITY_FILE_NAME + " content invalid.");
					resetToDefaultSettings();
				} else {
					this.storageName = String.valueOf(utilJSON.get("storageName"));
					this.directory = String.valueOf(utilJSON.get("directory"));					
				}
				
			} catch (IOException | ParseException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		logger.log(Level.INFO, "process storage utility [OK]");
	}
	
	/**
	 * @param json object
	 * @return true if storing is successful
	 */
	public boolean storeJSONIntoStorage(JSONObject json) {
		processStorage();
		return storeJSONIntoStorage(json, getStorageRelativePath());
	}
	
	/**
	 * @return data in JSON object 
	 */
	public JSONObject getData() {
		JSONObject dataJSON = new JSONObject();
		if (isStorageExist(getStorageRelativePath()) == true) {
			logger.log(Level.INFO, MESSAGE_GET_ALL_DATA_STORAGE_EXIST);
			dataJSON = retrieveDataFromStorage();
		} else {
			// must store data first, cannot create new storage because user might change directory
			logger.log(Level.WARNING, MESSAGE_GET_ALL_DATA_STORAGE_NOT_EXIST);
		}
		return dataJSON;
	}
	
	/**
	 * @return data in JSON object 
	 */
	private JSONObject retrieveDataFromStorage() {
		// do nothing if task list is empty
		String fileName = getStorageRelativePath();
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
			if (dataJSON.containsKey("serialNumber") == false) {
				logger.log(Level.WARNING, "File content invalid. No data is retrieved.");
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
		this.directory = "tables/";			// default settings
		this.storageName = "storage.json";	// default settings
		saveSettingsToUtility();
	}

	/**
	 * Save settings to utility file
	 */
	@SuppressWarnings("unchecked")
	private void saveSettingsToUtility() {
		JSONObject utilJSON = new JSONObject();
		utilJSON.put("directory", this.directory);
		utilJSON.put("storageName", this.storageName);
		storeJSONIntoStorage(utilJSON, STRING_UTILITY_FILE_NAME);
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
	private boolean storeJSONIntoStorage(JSONObject json, String fileName) {
		File file = new File(fileName);
		long timeBeforeModification = file.lastModified();
		long timeAfterModification = -1;
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
		if (result == true) {
			logger.log(Level.INFO, fileName + " is saved successfully.");
		} else {
			logger.log(Level.WARNING, fileName + " not saved.");
		}
		return result;
	}
	
	/**
	 * @param File Relative Path
	 * @return True if file exist
	 */
	private boolean isStorageExist(String fileRelativePath) {
		File file = new File(fileRelativePath);
		return file.exists();
	}
	
	/**
	 * @param File Relative Path
	 */
	private void createStorage(String fileRelativePath) {
		logger.log(Level.INFO, MESSAGE_CREATE_STORAGE_FILE + fileRelativePath);
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
		return directory;
	}
	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		deleteFile(getStorageRelativePath());	// remove old storage file if it exists
		this.directory = directory;
		saveSettingsToUtility();
		logger.log(Level.INFO, MESSAGE_NEW_FILE_DIRECTORY + this.getDirectory());
	}
	
	/**
	 * @param fileName
	 */
	private void deleteFile(String fileName) {
		File oldFile = new File(fileName);
		oldFile.setWritable(true);
		if (oldFile.exists()) {
			oldFile.delete();
			logger.log(Level.INFO, fileName + " is deleted.");
		}
	}
	
	/**
	 * @return the storageName
	 */
	public String getStorageName() {
		return storageName;
	}
	/**
	 * @param storageName the storageName to set
	 */
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	
	/**
	 * @return directory + storageName
	 */
	public String getStorageRelativePath() {
		return directory.concat(storageName);
	}
}