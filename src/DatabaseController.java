import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * @author: Esmond Peh
 * 
 * This controller is to provide the read and write functionalities to the storage for the LogicController.
 * 
 * Storage files:
 * 1. 	activeTaskList.json
 * 2.	archiveTaskList.json
 * 3.	dateList.json
 * 4.	priorityList.json
 * 5.	tagList.json
 * 
 */

public class DatabaseController {

	private static final String STRING_FORMAT_FOR_DATE = "MM/dd/yyyy HH:mm:ss";
	private static final String FILENAME_ACTIVE_TASKLIST = "tables/activeTaskList.json";
	private static final String FILENAME_ARCHIVE_TASKLIST = "tables/archiveTaskList.json";
	private static final String FILENAME_DATE_LIST = "tables/dateList.json";
	private static final String FILENAME_PRIORITY_LIST = "tables/priorityList.json";
	private static final String FILENAME_TAG_LIST = "tables/tagList.json";

	public static void main (String[] args) throws IOException, ParseException, java.text.ParseException {
		
		initializeStorage();	// initialize all 5 tables for first time startup
		
		writeTaskListToStorage(initialiseDummyDataForTesting(), FILENAME_ACTIVE_TASKLIST);	// takes in TaskList and write to storage
		
		TaskList taskListHashMap = retrieveTaskListFromStorage(FILENAME_ACTIVE_TASKLIST);	// retrieve TaskList from selected storage
	}

	private static TaskList retrieveTaskListFromStorage(String storageName)
			throws IOException, ParseException, FileNotFoundException,
			java.text.ParseException {
		
		JSONParser parser = new JSONParser(); 
		Object obj = parser.parse(new FileReader(storageName));  
		JSONObject taskListJSON = (JSONObject) obj; 
		TaskList taskListHashMap = convertJSONObjectToHashMap(taskListJSON);
		return taskListHashMap;
	}

	private static TaskList convertJSONObjectToHashMap(JSONObject taskListJSON) throws java.text.ParseException {
		TaskList taskListHashMap = new TaskList();
		
		Iterator it = taskListJSON.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	    	JSONObject taskJSON = (JSONObject) taskListJSON.get(pair.getKey()); 
            Task task = convertJSONObjectToTask(taskJSON);
    		taskListHashMap.addTask(task.getId(), task);
	    	it.remove(); // avoids a ConcurrentModificationException
        }
	    
	    return taskListHashMap;
	}

	private static Task convertJSONObjectToTask(JSONObject taskJSON)
			throws java.text.ParseException {
		int id = (int) (long) taskJSON.get("ID");
		String description = (String) taskJSON.get("Description");
		String string_date = (String) taskJSON.get("Deadline");
		Date date = convertDateToString(string_date);
		JSONArray tagsList = (JSONArray) taskJSON.get("tags");
		ArrayList<String> tags = tagsList;
		int priority = (int) (long) taskJSON.get("Priority");
		boolean archived = (boolean) taskJSON.get("Archived");

		Task task = new Task(id, description, date, priority, tags);
		if (archived == true) {
			task.moveToArchive();
		}
		return task;
	}

	private static Date convertDateToString(String string)
			throws java.text.ParseException {
		DateFormat format = new SimpleDateFormat(STRING_FORMAT_FOR_DATE);
		Date date = format.parse(string);
		return date;
	}
	
	private static TaskList initialiseDummyDataForTesting() {
		TaskList taskListHashMap = new TaskList();
		
		Task dummyTask = new Task(1, "dummy 1", new Date(), 1, null);
		Task dummyTask2 = new Task(2, "dummy 2", new Date(), 2, null);
		Task dummyTask3 = new Task(3, "dummy 3", new Date(), 3, null);
		
		taskListHashMap.addTask(dummyTask.getId(), dummyTask);
		taskListHashMap.addTask(dummyTask2.getId(), dummyTask2);
		taskListHashMap.addTask(dummyTask3.getId(), dummyTask3);
		return taskListHashMap;
	}

	private static void writeTaskListToStorage(TaskList taskListHashMap, String storageName)
			throws IOException {
		JSONObject taskListJSON = convertHashmapToJSONObject(taskListHashMap);
		writeJSONObjectToStorage(taskListJSON, storageName);
	}

	private static JSONObject convertHashmapToJSONObject(TaskList activeTaskList) {
		JSONObject activeTaskListJSON = new JSONObject();
		Iterator it = activeTaskList.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Task tempTask = (Task) pair.getValue();
	        addTaskToTaskListJSON(activeTaskListJSON, tempTask);
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    return activeTaskListJSON;
	}

	private static void writeJSONObjectToStorage(JSONObject taskListJSON, String storageName)
			throws IOException {
		FileWriter fileWriter = new FileWriter(storageName);  
		fileWriter.write(taskListJSON.toJSONString());  
		fileWriter.flush();  
		fileWriter.close();
	}

	@SuppressWarnings("unchecked")
	private static void addTaskToTaskListJSON(JSONObject taskListJSON,
			Task task) {
		
		String reportDate = convertDateToString(task);
		
		JSONObject newTask = new JSONObject();
		newTask.put("ID", task.getId());
		newTask.put("Description", task.getDescription());
		newTask.put("Deadline", reportDate);
		newTask.put("Priority", task.getPriority());
		newTask.put("Tags", task.getTags().toString());
		newTask.put("Archived", task.isArchived());	
		taskListJSON.put(task.getId(), newTask);
	}

	private static String convertDateToString(Task task) {
		DateFormat df = new SimpleDateFormat(STRING_FORMAT_FOR_DATE);
		Date today = task.getDeadline();
		String reportDate = df.format(today);
		return reportDate;
	}

	private static void initializeStorage() throws IOException {
		// check if 5 tables exist, if not, create the tables.
		processStorage(FILENAME_ACTIVE_TASKLIST);
		processStorage(FILENAME_ARCHIVE_TASKLIST);
		processStorage(FILENAME_DATE_LIST);
		processStorage(FILENAME_PRIORITY_LIST);
		processStorage(FILENAME_TAG_LIST);
	}

	private static void processStorage(String storageName) throws IOException {
		if (isStorageExist(storageName) == false) {
			createStorage(storageName);
		}
	}

	private static boolean isStorageExist(String storageName) {
		File file = new File(storageName);
		return file.exists();
	}

	private static void createStorage(String storageName) throws IOException {
		File storageFile = new File(storageName);
		analyseStorageDirectory(storageFile);
		storageFile.createNewFile();
	}

	private static void analyseStorageDirectory(File storageFile) {
		if (storageFile.getParentFile() != null) {
			storageFile.getParentFile().mkdirs();
		}
	}
}
