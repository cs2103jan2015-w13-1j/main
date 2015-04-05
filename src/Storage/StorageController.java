/**
 * This java class is the controller for the Storage component in the software architecture.
 * 
 * Dependency files: 
 * - jar/gson-2.3.1.jar for Gson library 
 * - jar/json-simpler-1.1.1.jar for JSON library
 * 
 * Test driver: StorageADT.java
 * Interface:	InterfaceForStorage.java
 * 
 * @author Esmond
 */

package Storage;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Common.DATA;
import Common.Date;
import Common.Task;
import Common.TaskList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StorageController implements InterfaceForStorage {
	private static final String STRING_GENERIC = "generic";
	private static final String STRING_FINISHED_TIME = "finishedTime";
	private static final String STRING_END_TIME = "endTime";
	private static final String STRING_START_TIME = "startTime";
	private static final String STRING_MEETING = "meeting";
	private static final String STRING_DEADLINE = "deadline";
	private static final String STRING_ARCHIVED = "archived";
	private static final String STRING_PRIORITY = "priority";
	private static final String STRING_TAGS = "tags";
	private static final String STRING_TYPE = "type";
	private static final String STRING_DESCRIPTION = "description";
	private static final String STRING_ID = "id";
	private static final String STRING_RECURRENCE_ID = "recurrenceId";
	private static final String STRING_SERIAL_NUMBER = "serialNumber";
	private static final String STRING_ARCHIVED_TASK_LIST = "archivedTaskList";
	private static final String STRING_ACTIVE_TASK_LIST = "activeTaskList";
	private static final String MESSAGE_ERROR_CREATE_TASK = "Unable to create task due to invalid task type";
	private static final String MESSAGE_ERROR_CONVERT_DATA = "Unable to get DATA from storage.";
	private static final String MESSAGE_DUMMY_DATA = "9 Dummy data created.";
	private static final String MESSAGE_INITIALISE_NEW_DATA_OBJECT = "Initialise new DATA object.";
	private static final String MESSAGE_ERROR_STORE = "failure in storing";
	private static final String MESSAGE_SUCCESS_STORE = "success in storing";
	private static final int STARTING_INDEX = 0;

	private final static Logger logger = Logger.getLogger(StorageController.class.getName());
	
	private DATA _data;
	private StorageDatastore _datastore = new StorageDatastore();
	
	public static void main(String[] args) {
		StorageController control = new StorageController();
//		System.out.println(control.getFileDirectory());
//		control.setFileDirectory("C:\\Users\\Esmond\\Google Drive\\esmond");
		control.testForStoreFunction();
//		control.importFromFile("C:\\Users\\Esmond\\Google Drive\\NUS\\Essentials of Clear Writing\\storage.json");
//		control.getAllData();
//		System.out.println(control.getMotivationQuotes());
	}

	@Override
	public boolean exportToDirectory(String fileName) {
		File exportFile = new File(fileName);
		URI uri = exportFile.toURI();
		String importedFileAbsolutePath = uri.getPath().replaceFirst("/", "");
		System.out.println("export to: " + importedFileAbsolutePath);
		_datastore.processStorage(importedFileAbsolutePath);
		if (_datastore.storeJsonIntoStorage(_datastore.getData(), importedFileAbsolutePath) == true) {
			logger.log(Level.INFO, "Successful export to " + importedFileAbsolutePath);
			return true;
		} else {
			logger.log(Level.WARNING, "Unsuccessful export.");
			return false;
		}
	}
	
	@Override
	public boolean importFromDirectory(String fileName) {
		// check if file is valid, 
		//		if yes, store parent folder into utility
		// 		if not, return false
		File importedFile = new File(fileName);
		URI uri = importedFile.toURI();
		String importedFileAbsolutePath = uri.getPath().replaceFirst("/", "");
//		System.out.println("import from: " + importedFileAbsolutePath);
		if (_datastore.isStorageExist(importedFileAbsolutePath)) {
			JSONObject importedJsonData = _datastore.retrieveDataFromStorage(importedFileAbsolutePath);
			if (importedJsonData.containsKey(STRING_SERIAL_NUMBER) == false || importedJsonData.containsKey(STRING_ACTIVE_TASK_LIST) == false
					|| importedJsonData.containsKey(STRING_RECURRENCE_ID) == false || importedJsonData.containsKey(STRING_ARCHIVED_TASK_LIST) == false) {
				// wrong format of data
				return false;
			} else {
				String importedFileFolderAbsolutePath = importedFile.getParentFile().toURI().getPath().replaceFirst("/", "");
//				System.out.println("folder: " + importedFileFolderAbsolutePath);
				String importedFileName= importedFile.getName();
//				System.out.println("file name: " + importedFileName);
//				this.setFileDirectory(importedFileFolderAbsolutePath);
				
				_datastore.setDirectory(importedFileFolderAbsolutePath);
				_datastore.setStorageName(importedFileName);
				_datastore.saveSettingsToUtility();
				_datastore.initialise();
				
				if (_datastore.getDirectory().equals(importedFileFolderAbsolutePath) && _datastore.getStorageName().equals(importedFileName)) {
					logger.log(Level.INFO, "Successful import from " + importedFileAbsolutePath);
				} else {
					logger.log(Level.WARNING, "Unsuccessful import from " + importedFileAbsolutePath);
				}
//				if (_datastore.storeJsonIntoStorage(importedJsonData, _datastore.getStorageFilePath()) == true) {
//					logger.log(Level.INFO, "Successful import from " + importedFileAbsolutePath);
//				} else {
//					logger.log(Level.WARNING, "Unsuccessful import from " + importedFileAbsolutePath);
//				}
				
				return true;
			}
		} else {
			// file not found
			return false;
		}
		
	}
	
	/**
	 * for testing purpose
	 */
	private void testForStoreFunction() {
		this._data = initialiseNewDataObject();
		createDummyData();
		storeAllData(this._data);
	}
	
	@Override
	public DATA getAllData() {
		this._data = convertJsonObjectToData(_datastore.getData());
		return _data;
	}
	
	/**
	 * @param dataJSON
	 * @return DATA object
	 */
	private DATA convertJsonObjectToData(JSONObject dataJSON) {		
		if (dataJSON.containsKey(STRING_SERIAL_NUMBER) == false || dataJSON.containsKey(STRING_ACTIVE_TASK_LIST) == false
				|| dataJSON.containsKey(STRING_RECURRENCE_ID) == false || dataJSON.containsKey(STRING_ARCHIVED_TASK_LIST) == false) {
			logger.log(Level.WARNING, MESSAGE_ERROR_CONVERT_DATA);
			return initialiseNewDataObject();
		} else {
			DATA newData = new DATA();
			Gson gson = new Gson();
			// retrieve serial number and store into data
			newData.setSerialNumber(gson.fromJson(String.valueOf(dataJSON.get(STRING_SERIAL_NUMBER)) , int.class));
			// retrieve recurrence ID and store into data
			newData.setRecurrenceId(gson.fromJson(String.valueOf(dataJSON.get(STRING_RECURRENCE_ID)) , int.class));
			// retrieve active list and store into data
			TaskList activeTaskList = getTaskListFromJsonByTaskList(dataJSON, STRING_ACTIVE_TASK_LIST);
			newData.setActiveTaskList(activeTaskList);
			// retrieve archive list and store into data
			TaskList archivedTaskList = getTaskListFromJsonByTaskList(dataJSON, STRING_ARCHIVED_TASK_LIST);
			newData.setArchivedTaskList(archivedTaskList);
			return newData;
		}
	}

	@Override
	public String storeAllData(DATA data) {
		this._data = data;
		if (_datastore.storeJsonIntoStorage(convertDataToJSONObject()) == true) {
			logger.log(Level.INFO, MESSAGE_SUCCESS_STORE);
			return MESSAGE_SUCCESS_STORE;
		} else {
			logger.log(Level.WARNING, MESSAGE_ERROR_STORE);
			return MESSAGE_ERROR_STORE;
		}
	}

	/**
	 * @param dataJSON
	 * @param taskList type
	 * @return taskList
	 */
	@SuppressWarnings("rawtypes")
	private TaskList getTaskListFromJsonByTaskList(JSONObject dataJSON, String taskList) {
		TaskList taskListHashMap = new TaskList();
		try {
			JSONObject tasklistJSON = (JSONObject) dataJSON.get(taskList);
			Iterator it = tasklistJSON.entrySet().iterator();
			
		    while (it.hasNext()) {
		    	Map.Entry pair = (Map.Entry)it.next();
		    	JSONObject taskJSON = (JSONObject) tasklistJSON.get(pair.getKey()); 
		    	Task task = convertJsonObjectToTask(taskJSON);
				taskListHashMap.addTask(task.getId(), task);
		    	it.remove(); // avoids a ConcurrentModificationException
	        }
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return taskListHashMap;
	}

	/**
	 * @param taskJSON
	 * @return task object
	 */
	private Task convertJsonObjectToTask(JSONObject taskJSON) {
		// Generic Type attributes
		Gson gson = new Gson();
		int id = gson.fromJson(String.valueOf(taskJSON.get(STRING_ID)) , int.class);
		int recurrenceId = gson.fromJson(String.valueOf(taskJSON.get(STRING_RECURRENCE_ID)) , int.class);
		String description = String.valueOf(taskJSON.get(STRING_DESCRIPTION));
		String type = String.valueOf(taskJSON.get(STRING_TYPE));
		ArrayList<String> tags = gson.fromJson(String.valueOf(taskJSON.get(STRING_TAGS)) , new TypeToken<ArrayList<String>>() {}.getType());
		int priority = gson.fromJson(String.valueOf(taskJSON.get(STRING_PRIORITY)) , int.class);
		boolean archived = gson.fromJson(String.valueOf(taskJSON.get(STRING_ARCHIVED)) , boolean.class);
		
		Task task = createTaskFromAttribute(taskJSON, id, description, type, tags, priority, archived, recurrenceId);
		return task;
	}

	/**
	 * @param taskJSON
	 * @param id
	 * @param description
	 * @param type
	 * @param tags
	 * @param priority
	 * @param archived
	 * @return task object
	 */
	private Task createTaskFromAttribute(JSONObject taskJSON, int id, String description, String type, ArrayList<String> tags,	int priority, boolean archived, int recurrenceId) {
		Task task = createTask(taskJSON, id, description, type, tags, priority, recurrenceId);
		determineTaskIsArchived(taskJSON, archived, task);
		return task;
	}

	/**
	 * @param taskJSON
	 * @param id
	 * @param description
	 * @param type
	 * @param tags
	 * @param priority
	 * @return task object
	 */
	private Task createTask(JSONObject taskJSON, int id, String description, String type, ArrayList<String> tags, int priority, int recurrenceId) {
		Task task = null;
		Gson gson = new Gson();
		try {
			if (type.equals(STRING_DEADLINE)) {
				// Deadline Type
				long string_date = gson.fromJson(String.valueOf(taskJSON.get(STRING_DEADLINE)) , long.class);
				Date deadline = new Date();
				deadline.setTime(string_date);
				task = new Task(id, description, deadline, priority, tags, recurrenceId);
			} else if (type.equals(STRING_MEETING)) {
				// Meeting Type
				long long_start_date = gson.fromJson(String.valueOf(taskJSON.get(STRING_START_TIME)) , long.class);
				Date startDate = new Date();
				startDate.setTime(long_start_date);
				
				long long_end_date = gson.fromJson(String.valueOf(taskJSON.get(STRING_END_TIME)) , long.class);
				Date endDate = new Date();
				endDate.setTime(long_end_date);
				
				task = new Task(id, description, startDate, endDate, priority, tags, recurrenceId);
			} else if (type.equals(STRING_GENERIC)) {
				// Generic type
				task = new Task(id, description, priority, tags, recurrenceId);
			} else {
				logger.log(Level.WARNING, MESSAGE_ERROR_CREATE_TASK);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return task;
	}
	
	/**
	 * @param taskJSON
	 * @param archived
	 * @param task
	 */
	private void determineTaskIsArchived(JSONObject taskJSON, boolean archived, Task task) {
		Gson gson = new Gson();
		try {
			if (archived == true) {
				long string_finished_time= gson.fromJson(String.valueOf(taskJSON.get(STRING_FINISHED_TIME)) , long.class);
				Date finishedTime = new Date();
				finishedTime.setTime(string_finished_time);
				task.moveToArchive(finishedTime);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	// pre-requisite: DATA object must be valid
	/**
	 * @return json object
	 */
	private JSONObject convertDataToJSONObject() {
		JSONObject dataJSON = new JSONObject();
		try {
			Gson gson = new Gson();
			JSONParser parser = new JSONParser();
			dataJSON = (JSONObject) parser.parse(gson.toJson(this._data));
			modifyDataJsonByTaskList(dataJSON, STRING_ACTIVE_TASK_LIST);
			modifyDataJsonByTaskList(dataJSON, STRING_ARCHIVED_TASK_LIST);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return dataJSON;
	}

	/**
	 * @param dataJSON
	 * @param taskList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void modifyDataJsonByTaskList(JSONObject dataJSON, String taskList) {
		try {
			JSONObject taskListJSON = (JSONObject) dataJSON.get(taskList);
			Iterator it = null;
			it = setIteratorByTaskList(taskList, it);
			while (it.hasNext()) {
			    Map.Entry pair = (Map.Entry)it.next();
				Task task = (Task) pair.getValue();
				JSONObject taskJSON = (JSONObject) taskListJSON.get(String.valueOf(task.getId()));
				modifyNonGenericTaskForDataJson(task, taskJSON); 
				processArchivedTaskForDataJson(task, taskJSON);
				taskListJSON.replace(String.valueOf(task.getId()), taskJSON);
			}
			dataJSON.replace(taskList, taskListJSON);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * @param task
	 * @param taskJSON
	 */
	@SuppressWarnings("unchecked")
	private void processArchivedTaskForDataJson(Task task, JSONObject taskJSON) {
		if (task.isArchived() == true) {
			taskJSON.replace(STRING_FINISHED_TIME, task.getFinishedTime().getTime());
		}
	}

	/**
	 * @param task
	 * @param taskJSON
	 */
	@SuppressWarnings("unchecked")
	private void modifyNonGenericTaskForDataJson(Task task, JSONObject taskJSON) {
		if (taskJSON.containsKey(STRING_DEADLINE)) {
			taskJSON.replace(STRING_DEADLINE, task.getDeadline().getTime());
		} else if (taskJSON.containsKey(STRING_START_TIME)) {
			taskJSON.replace(STRING_START_TIME, task.getStartTime().getTime());
			taskJSON.replace(STRING_END_TIME, task.getEndTime().getTime());
		}
	}

	/**
	 * @param taskList
	 * @param it
	 * @return it
	 */
	@SuppressWarnings("rawtypes")
	private Iterator setIteratorByTaskList(String taskList, Iterator it) {
		if (taskList.equals(STRING_ACTIVE_TASK_LIST)) {
			it = _data.getActiveTaskList().entrySet().iterator();
		} else if (taskList.equals(STRING_ARCHIVED_TASK_LIST)) {
			it = _data.getArchivedTaskList().entrySet().iterator();
		}
		return it;
	}

	/**
	 * Intialise new DATA object
	 */
	public DATA initialiseNewDataObject() {
		logger.log(Level.FINE, MESSAGE_INITIALISE_NEW_DATA_OBJECT);
		this._data = new DATA();
		this._data.setActiveTaskList(new TaskList());
		this._data.setArchivedTaskList(new TaskList());
		this._data.setSerialNumber(STARTING_INDEX);
		this._data.setRecurrenceId(STARTING_INDEX);
		return this._data;
	}

	@Override
	public String getFileDirectory() {
		return _datastore.getDirectory();
	}

	@Override
	// change the file directory
	public String changeFileDirectory(String fileDirectory) {
		_datastore.changeDirectory(fileDirectory);
		return _datastore.getDirectory();
	}

	/**
	 * @return datastore.getStorageRelativePath();
	 */
	public String getFileRelativePath() {
		return _datastore.getStorageFilePath();
	}

	/**
	 * @return the data
	 */
	public DATA getData() {
		return _data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(DATA data) {
		this._data = data;
	}
	
	// create 9 dummy tasks and store into data for testing
	/**
	 * @return a message to show 9 dummy tasks created
	 */
	public String createDummyData() {
		// dummy GenericTask
		Task dummyGenericTask1 = new Task(1, "dummyGenericTask 1", 1, null);
		dummyGenericTask1.addTag("Personal"); 
		Task dummyGenericTask2 = new Task(2, "dummyGenericTask 2", 2, null);
		dummyGenericTask2.addTag("Work");	
		Task dummyGenericTask3 = new Task(3, "dummyGenericTask 3", 3, null);
		dummyGenericTask3.addTag("School");

		// dummy DeadlineTask
		Task dummyDeadlineTask1 = new Task(4, "dummyDeadlineTask 1", null, 2, null);
		long deadlinetime1 = Long.valueOf("1424361600000"); // date: 20/02/2015
		Date deadline1 = new Date();
		deadline1.setTime(deadlinetime1);
		dummyDeadlineTask1.changeDeadline(deadline1);
		dummyDeadlineTask1.addTag("Personal");
		dummyDeadlineTask1.addTag("Work");
		Task dummyDeadlineTask2 = new Task(5, "dummyDeadlineTask 2", null, 2, null);
		long deadlinetime2 = Long.valueOf("1424797200000"); // date: 25/02/2015
		Date deadline2 = new Date();
		deadline2.setTime(deadlinetime2);
		dummyDeadlineTask2.changeDeadline(deadline2);	
		dummyDeadlineTask2.addTag("Work");
		dummyDeadlineTask2.addTag("School");
		Task dummyDeadlineTask3 = new Task(6, "dummyDeadlineTask 3", null, 2, null);
		long deadlinetime3 = Long.valueOf("1425056400000"); // date: 28/02/2015 1 am
		Date deadline3 = new Date();
		deadline3.setTime(deadlinetime3);
		dummyDeadlineTask3.changeDeadline(deadline3);
		dummyDeadlineTask3.addTag("Personal");
		dummyDeadlineTask3.addTag("School");

		// dummy MeetingTask
		Task dummyMeetingTask1 = new Task(7, "dummyMeetingTask 1", null, null, 3, null);
		dummyMeetingTask1.changeStartTime(deadline2); 
		dummyMeetingTask1.changeEndTime(deadline3);
		dummyMeetingTask1.addTag("Work");
		dummyMeetingTask1.addTag("Personal");
		Task dummyMeetingTask2 = new Task(8, "dummyMeetingTask 2", null, null, 3, null);
		dummyMeetingTask2.changeStartTime(deadline1);
		dummyMeetingTask2.changeEndTime(deadline2);
		dummyMeetingTask2.addTag("Personal");
		dummyMeetingTask2.addTag("School");
		Task dummyMeetingTask3 = new Task(9, "dummyMeetingTask 3", null, null, 3, null);
		dummyMeetingTask3.changeStartTime(deadline1); 
		dummyMeetingTask3.changeEndTime(deadline3);
		dummyMeetingTask3.addTag("Work");
		dummyMeetingTask3.addTag("Personal");

		// tasks set to archive
		dummyGenericTask2.moveToArchive(deadline1);
		dummyDeadlineTask1.moveToArchive(deadline2);
		dummyMeetingTask2.moveToArchive(deadline3);

		_data.setSerialNumber(10); // hard-coded serial number
		_data.setRecurrenceId(STARTING_INDEX); // hard-coded recurrence id

		_data.getActiveTaskList().addTask(dummyGenericTask1.getId(), dummyGenericTask1);
		_data.getActiveTaskList().addTask(dummyGenericTask3.getId(), dummyGenericTask3);
		_data.getActiveTaskList().addTask(dummyDeadlineTask2.getId(), dummyDeadlineTask2);
		_data.getActiveTaskList().addTask(dummyDeadlineTask3.getId(), dummyDeadlineTask3);
		_data.getActiveTaskList().addTask(dummyMeetingTask1.getId(), dummyMeetingTask1);
		_data.getActiveTaskList().addTask(dummyMeetingTask3.getId(), dummyMeetingTask3);

		_data.getArchivedTaskList().addTask(dummyGenericTask2.getId(), dummyGenericTask2);
		_data.getArchivedTaskList().addTask(dummyDeadlineTask1.getId(), dummyDeadlineTask1);
		_data.getArchivedTaskList().addTask(dummyMeetingTask2.getId(), dummyMeetingTask2);
		logger.log(Level.INFO, MESSAGE_DUMMY_DATA);
		return MESSAGE_DUMMY_DATA;
	}

	@Override
	public String getFileName() {
		return _datastore.getStorageName();
	}
}
