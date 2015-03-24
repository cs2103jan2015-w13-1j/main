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
import Common.Motivator;
import Common.Task;
import Common.TaskList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StorageController implements InterfaceForStorage {
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
	private static final String MESSAGE_CONVERT_DATA_FAILURE = "Unable to get DATA from storage.";
	private static final String MESSAGE_DUMMY_DATA = "9 Dummy data created.";
	private static final String MESSAGE_INITIALISE_NEW_DATA_OBJECT = "Initialise new DATA object.";
	private static final String MESSAGE_STORE_DATA_FAILURE = "failure in storing";
	private static final String MESSAGE_STORE_DATA_SUCCESS = "success in storing";
	private static final String STRING_ARCHIVED_TASK_LIST = "archivedTaskList";
	private static final String STRING_ACTIVE_TASK_LIST = "activeTaskList";
	private static final int STARTING_INDEX = 0;

	private final static Logger logger = Logger.getLogger(StorageController.class.getName());
	
	private DATA data;
	private Motivator motivator = new Motivator();
	private StorageDatastore datastore = new StorageDatastore();
	
	public static void main(String[] args) {
		StorageController control = new StorageController();
		System.out.println(control.getFileDirectory());
		control.setFileDirectory("tables/");
		control.testForStoreFunction();
//		control.getAllData();
//		System.out.println(control.getMotivationQuotes());
	}
	
	/**
	 * for testing purpose
	 */
	private void testForStoreFunction() {
		this.data = initialiseNewDataObject();
		createDummyData();
		storeAllData(this.data);
	}
	
	@Override
	public DATA getAllData() {
		this.data = convertJSONObjectToData(datastore.getData());
		return data;
	}
	
	/**
	 * @param dataJSON
	 * @return DATA object
	 */
	private DATA convertJSONObjectToData(JSONObject dataJSON) {
		if (dataJSON.containsKey(STRING_SERIAL_NUMBER) == false) {
			logger.log(Level.WARNING, MESSAGE_CONVERT_DATA_FAILURE);
			return initialiseNewDataObject();
		}
		
		DATA newData = new DATA();
		Gson gson = new Gson();
		// retrieve serial number and store into data
		newData.setSerialNumber(gson.fromJson(String.valueOf(dataJSON.get(STRING_SERIAL_NUMBER)) , int.class));
		// retrieve recurrence ID and store into data
		newData.setRecurrenceId(gson.fromJson(String.valueOf(dataJSON.get(STRING_RECURRENCE_ID)) , int.class));
		TaskList activeTaskList = getTaskListFromJSONByTaskList(dataJSON, STRING_ACTIVE_TASK_LIST);
		newData.setActiveTaskList(activeTaskList);
		TaskList archivedTaskList = getTaskListFromJSONByTaskList(dataJSON, STRING_ARCHIVED_TASK_LIST);
		newData.setArchivedTaskList(archivedTaskList);
	    return newData;
	}

	@Override
	public String storeAllData(DATA data) {
		this.data = data;
		if (datastore.storeJSONIntoStorage(convertDataToJSONObject()) == true) {
			logger.log(Level.INFO, MESSAGE_STORE_DATA_SUCCESS);
			return MESSAGE_STORE_DATA_SUCCESS;
		}
		logger.log(Level.WARNING, MESSAGE_STORE_DATA_FAILURE);
		return MESSAGE_STORE_DATA_FAILURE;
	}

	/**
	 * @param dataJSON
	 * @param taskList type
	 * @return taskList
	 */
	@SuppressWarnings("rawtypes")
	private TaskList getTaskListFromJSONByTaskList(JSONObject dataJSON, String taskList) {
		TaskList taskListHashMap = new TaskList();
		try {
			JSONObject tasklistJSON = (JSONObject) dataJSON.get(taskList);
			Iterator it = tasklistJSON.entrySet().iterator();
			
		    while (it.hasNext()) {
		    	Map.Entry pair = (Map.Entry)it.next();
		    	JSONObject taskJSON = (JSONObject) tasklistJSON.get(pair.getKey()); 

		    	Task task = convertJSONObjectToTask(taskJSON);
				
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
	private Task convertJSONObjectToTask(JSONObject taskJSON) {
		// Generic Type attributes
		Gson gson = new Gson();
		int id = gson.fromJson(String.valueOf(taskJSON.get(STRING_ID)) , int.class);
		String description = String.valueOf(taskJSON.get(STRING_DESCRIPTION));
		String type = String.valueOf(taskJSON.get(STRING_TYPE));
		ArrayList<String> tags = gson.fromJson(String.valueOf(taskJSON.get(STRING_TAGS)) , new TypeToken<ArrayList<String>>() {}.getType());
		int priority = gson.fromJson(String.valueOf(taskJSON.get(STRING_PRIORITY)) , int.class);
		boolean archived = gson.fromJson(String.valueOf(taskJSON.get(STRING_ARCHIVED)) , boolean.class);
		
		Task task = createTaskFromAttribute(taskJSON, id, description, type, tags, priority, archived);
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
	private Task createTaskFromAttribute(JSONObject taskJSON, int id, String description, String type, ArrayList<String> tags,	int priority, boolean archived) {
		Task task = createTask(taskJSON, id, description, type, tags, priority);
		determineTaskArchive(taskJSON, archived, task);
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
	private Task createTask(JSONObject taskJSON, int id, String description, String type, ArrayList<String> tags, int priority) {
		Task task = null;
		Gson gson = new Gson();
		try {
			if (type.equals(STRING_DEADLINE)) {
				// Deadline Type
				long string_date = gson.fromJson(String.valueOf(taskJSON.get(STRING_DEADLINE)) , long.class);
				Date deadline = new Date();
				deadline.setTime(string_date);
				task = new Task(id, description, deadline, priority, tags);
			} else if (type.equals(STRING_MEETING)) {
				// Meeting Type
				long long_start_date = gson.fromJson(String.valueOf(taskJSON.get(STRING_START_TIME)) , long.class);
				Date startDate = new Date();
				startDate.setTime(long_start_date);
				
				long long_end_date = gson.fromJson(String.valueOf(taskJSON.get(STRING_END_TIME)) , long.class);
				Date endDate = new Date();
				endDate.setTime(long_end_date);
				
				task = new Task(id, description, startDate, endDate, priority, tags);
			} else {
				// Generic type
				task = new Task(id, description, priority, tags);
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
	private void determineTaskArchive(JSONObject taskJSON,	boolean archived, Task task) {
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
			dataJSON = (JSONObject) parser.parse(gson.toJson(this.data));
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
			it = data.getActiveTaskList().entrySet().iterator();
		} else if (taskList.equals(STRING_ARCHIVED_TASK_LIST)) {
			it = data.getArchivedTaskList().entrySet().iterator();
		}
		return it;
	}

	/**
	 * Intialise new DATA object
	 */
	public DATA initialiseNewDataObject() {
		logger.log(Level.FINE, MESSAGE_INITIALISE_NEW_DATA_OBJECT);
		this.data = new DATA();
		this.data.setActiveTaskList(new TaskList());
		this.data.setArchivedTaskList(new TaskList());
		this.data.setSerialNumber(STARTING_INDEX);
		this.data.setRecurrenceId(STARTING_INDEX);
		return this.data;
	}

	@Override
	public String getFileDirectory() {
		return datastore.getDirectory();
	}

	@Override
	// change the file directory
	public String setFileDirectory(String fileDirectory) {
		datastore.setDirectory(fileDirectory);
		return datastore.getDirectory();
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

		data.setSerialNumber(10); // hard-coded serial number
		data.setRecurrenceId(STARTING_INDEX); // hard-coded recurrence id

		data.getActiveTaskList().addTask(dummyGenericTask1.getId(), dummyGenericTask1);
		data.getActiveTaskList().addTask(dummyGenericTask3.getId(), dummyGenericTask3);
		data.getActiveTaskList().addTask(dummyDeadlineTask2.getId(), dummyDeadlineTask2);
		data.getActiveTaskList().addTask(dummyDeadlineTask3.getId(), dummyDeadlineTask3);
		data.getActiveTaskList().addTask(dummyMeetingTask1.getId(), dummyMeetingTask1);
		data.getActiveTaskList().addTask(dummyMeetingTask3.getId(), dummyMeetingTask3);

		data.getArchivedTaskList().addTask(dummyGenericTask2.getId(), dummyGenericTask2);
		data.getArchivedTaskList().addTask(dummyDeadlineTask1.getId(), dummyDeadlineTask1);
		data.getArchivedTaskList().addTask(dummyMeetingTask2.getId(), dummyMeetingTask2);
		logger.log(Level.INFO, MESSAGE_DUMMY_DATA);
		return MESSAGE_DUMMY_DATA;
	}

	/**
	 * @return datastore.getStorageRelativePath();
	 */
	public String getFileRelativePath() {
		return datastore.getStorageRelativePath();
	}

	/**
	 * @return the data
	 */
	public DATA getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(DATA data) {
		this.data = data;
	}

	@Override
	public String getMotivationQuotes() {
		return motivator.getRandomQuotes();
	}
}
