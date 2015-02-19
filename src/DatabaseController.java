import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/*
 * @author: Esmond Peh
 * 
 * This controller is to provide the read and write functionalities 
 * to the storage for the LogicController.
 * 
 * JSON files:
 * 1. 	activeTaskList.json
 * 2.	archiveTaskList.json
 * 3.	dateList.json
 * 4.	priorityList.json
 * 5.	tagList.json
 * 
 */

public class DatabaseController {

	private static final String FILENAME_ACTIVE_TASKLIST = "tables/activeTaskList.json";
	private static final String FILENAME_ARCHIVE_TASKLIST = "tables/archiveTaskList.json";
	private static final String FILENAME_DATE_LIST = "tables/dateList.json";
	private static final String FILENAME_PRIORITY_LIST = "tables/priorityList.json";
	private static final String FILENAME_TAG_LIST = "tables/tagList.json";

	public static void main (String[] args) throws IOException, ParseException, java.text.ParseException {
		// initialize all 5 tables for first time startup
		initializeStorage();
		// 3 dummy tasks
		Task dummyTask = new Task(1, "dummy 1", new Date(), 1, null);
		Task dummyTask2 = new Task(2, "dummy 2", new Date(), 2, null);
		Task dummyTask3 = new Task(3, "dummy 3", new Date(), 3, null);

		TaskList activeTaskList = new TaskList();
		activeTaskList.addTask(dummyTask.getId(), dummyTask);
		activeTaskList.addTask(dummyTask2.getId(), dummyTask2);
		activeTaskList.addTask(dummyTask3.getId(), dummyTask3);

		JSONObject activeTaskListJSON = new JSONObject();
		convertHashmapToJSONObject(activeTaskList, activeTaskListJSON);
		
		writeToStorage(activeTaskListJSON, FILENAME_ACTIVE_TASKLIST);
	}

	private static void convertHashmapToJSONObject(TaskList activeTaskList,
			JSONObject activeTaskListJSON) {
		Iterator it = activeTaskList.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Task tempTask = (Task) pair.getValue();
	        addTaskToTaskListJSON(activeTaskListJSON, tempTask);
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	private static void writeToStorage(JSONObject taskListJSON, String storageName)
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
		newTask.put("Description", task.getDescription());
		newTask.put("Deadline", reportDate);
		newTask.put("Priority", task.getPriority());
		newTask.put("Tags", task.getTags().toString());
		newTask.put("Archived", task.isArchived());	
		taskListJSON.put(task.getId(), newTask);
	}

	private static String convertDateToString(Task task) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
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
