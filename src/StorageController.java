import hashMaps.TaskList;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import basicElements.Date;
import basicElements.DeadlineTask;
import basicElements.MeetingTask;
import basicElements.Task;

/*
 * @author: Esmond Peh
 * 
 * This controller is to provide the read and write functionalities to the storage for the LogicController.
 * 
 * Storage files:
 * 1. 	list_activeTask.json
 * 2.	list_archiveTask.json
 * 3.	list_date.json
 * 4.	list_priority.json
 * 5.	list_tag.json
 * 6. 	sorted_todo.json
 * 7.	sorted_archive.json
 * 8.	sorted_priority.json
 * 
 * Program flow:
 * 1.	User start the program which will send a message to parser then logic and finally storage to retrieve data.
 * 2.	Storage controller will create a DATA object from the storage files.
 * 3.	Logic controller will then call the getData() method to retrieve the DATA object and hand over to parser controller.
 */

public class StorageController implements InterfaceForStorage {

	private static final String FILENAME_ACTIVE_TASKLIST = "tables/list_activeTask.json";
	private static final String FILENAME_ARCHIVE_TASKLIST = "tables/list_archiveTask.json";
	private static final String FILENAME_DATE_LIST = "tables/list_date.json";
	private static final String FILENAME_PRIORITY_LIST = "tables/list_priority.json";
	private static final String FILENAME_TAG_LIST = "tables/list_tag.json";
	private static final String FILENAME_TODO_SORTED_LIST = "tables/sorted_todo.json";
	private static final String FILENAME_ARCHIVE_SORTED_LIST= "tables/sorted_archive.json";
	private static final String FILENAME_PRIORITY_SORTED_LIST = "tables/sorted_priority.json";
	
	private DATA storage;

	public static void main (String[] args) throws IOException {
		StorageController control = new StorageController();
		control.run();	
//		Date now = new Date();
//		System.out.println(now.getTime());
//		
//		Date later = new Date();
//		later.setTime(now.getTime());
//		
//		System.out.println(later.getDate());
//		writeTaskListToStorage(initialiseDummyDataForTesting(), FILENAME_ACTIVE_TASKLIST);	// takes in TaskList and write to storage
//		
//		TaskList taskListHashMap = retrieveTaskListFromStorage(FILENAME_ACTIVE_TASKLIST);	// retrieve TaskList from selected storage
	}
	
	public void run() throws IOException {
		storage = getAllData();
		System.out.println(storage.getActiveTaskList().size());
		DeadlineTask dt = (DeadlineTask) storage.getActiveTaskList().getTaskbyId(1);
		System.out.println(dt);
		System.out.println(dt.getType());
	}
	
	public String storeAllData(DATA data) {
		this.storage = data;
		return "success";
	}
	
	public DATA getAllData() {
		try {
			storage = initializeStorage();
			storage.setActiveTaskList(retrieveTasklistFromStorage(FILENAME_ACTIVE_TASKLIST));
			//storage.setArchivedTaskList(retrieveTasklistFromStorage(FILENAME_ARCHIVE_TASKLIST));
//			storage.setDateList(retrieveDatelistFromStorage());
//			storage.setPriorityList(retrievePrioritylistFromStorage());
//			storage.setTagList(retrieveTaglistFromStorage());
//			storage.setToDoSortedList(retrieveSortedToDoFromStorage());
//			storage.setArchiveSortedList(retrieveSortedArchiveFromStorage());
//			storage.setPrioritySortedList(retrieveSortedPriorityFromStorage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return storage;
	}

	private TaskList retrieveTasklistFromStorage(String storageName) {
		JSONParser parser = new JSONParser(); 
		Object obj = null;

		TaskList taskListHashMap = new TaskList();
		try {
			obj = parser.parse(new FileReader(storageName));
			JSONObject taskListJSON = (JSONObject) obj; 
			
			Iterator it = taskListJSON.entrySet().iterator();
		    while (it.hasNext()) {
		    	Map.Entry pair = (Map.Entry)it.next();
		    	JSONObject taskJSON = (JSONObject) taskListJSON.get(pair.getKey()); 

		    	// Generic Type attributes
		    	int id = (int) (long) taskJSON.get("id");
				String description = (String) taskJSON.get("description");
				String type = (String) taskJSON.get("type");
				JSONArray tagsList = (JSONArray) taskJSON.get("tags");
				ArrayList<String> tags = tagsList;
				
				System.out.println(id + ": " + type + ", " + tags);
				int priority = (int) (long) taskJSON.get("priority");
				boolean archived = (boolean) taskJSON.get("archived");
				
				if (type.equals("DeadlineTask")) {
					long string_date = Long.parseLong((String) taskJSON.get("deadline"));
					Date deadline = new Date();
					deadline.setTime(string_date);
					DeadlineTask deadlineTask = new DeadlineTask(id, description, deadline, priority, tags, archived);
					//DeadlineTask deadlineTask = new DeadlineTask();
					System.out.println(">> " + deadlineTask.getType());
					taskListHashMap.addTask(deadlineTask.getId(), deadlineTask);
					System.out.println("added deadline, " + type);
				} else if (type.equals("MeetingTask")) {
					// Meeting Type
					long string_start_date = Long.parseLong((String) taskJSON.get("start"));
					Date startDate = new Date();
					startDate.setTime(string_start_date);
					
					long string_end_date = Long.parseLong((String) taskJSON.get("end"));
					Date endDate = new Date();
					endDate.setTime(string_end_date);
					
					MeetingTask meetingTask = new MeetingTask(id, description, startDate, endDate, priority, tags, archived);
					taskListHashMap.addTask(meetingTask.getId(), meetingTask);
					
					System.out.println("added meeting, " + type);
				} else {
					// Generic type
					Task task = new Task(id, description, priority, tags, archived);
					taskListHashMap.addTask(task.getId(), task);
				}
		    	it.remove(); // avoids a ConcurrentModificationException
	        }
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    
		return taskListHashMap;
	}
	
//	private static TaskList convertJSONObjectToHashMap(JSONObject taskListJSON) throws java.text.ParseException {
//		TaskList taskListHashMap = new TaskList();
//		
//		Iterator it = taskListJSON.entrySet().iterator();
//	    while (it.hasNext()) {
//	    	Map.Entry pair = (Map.Entry)it.next();
//	    	JSONObject taskJSON = (JSONObject) taskListJSON.get(pair.getKey()); 
//            Task task = convertJSONObjectToTask(taskJSON);
//    		taskListHashMap.addTask(task.getId(), task);
//	    	it.remove(); // avoids a ConcurrentModificationException
//        }
//	    
//	    return taskListHashMap;
//	}
//
//	private static Task convertJSONObjectToTask(JSONObject taskJSON) throws java.text.ParseException {
//		int id = (int) (long) taskJSON.get("ID");
//		String description = (String) taskJSON.get("Description");
////		String string_date = (String) taskJSON.get("Deadline");
////		Date date = convertDateToString(string_date);
//		String type = (String) taskJSON.get("Type");
//		JSONArray tagsList = (JSONArray) taskJSON.get("tags");
//		ArrayList<String> tags = tagsList;
//		int priority = (int) (long) taskJSON.get("Priority");
//		boolean archived = (boolean) taskJSON.get("Archived");
//
//		Task task = new Task(id, description, priority, tags);
//		if (archived == true) {
//			task.moveToArchive();
//		}
//		return task;
//	}
//
//	private static Date convertDateToString(String string) throws java.text.ParseException {
//		DateFormat format = new SimpleDateFormat(STRING_FORMAT_FOR_DATE);
//		Date date = format.parse(string);
//		return date;
//	}
//	
//	private static TaskList initialiseDummyDataForTesting() {
//		TaskList taskListHashMap = new TaskList();
//		
//		Task dummyTask = new Task(1, "dummy 1", 1, null);
//		Task dummyTask2 = new Task(2, "dummy 2", 2, null);
//		Task dummyTask3 = new Task(3, "dummy 3", 3, null);
//		dummyTask.addTag("Personal");
//		dummyTask3.addTag("Work");
//		dummyTask3.addTag("School");
//		
//		taskListHashMap.addTask(dummyTask.getId(), dummyTask);
//		taskListHashMap.addTask(dummyTask2.getId(), dummyTask2);
//		taskListHashMap.addTask(dummyTask3.getId(), dummyTask3);
//		return taskListHashMap;
//	}
//
//	public static void writeTaskListToStorage(TaskList taskListHashMap, String storageName)
//			throws IOException {
//		JSONObject taskListJSON = convertHashmapToJSONObject(taskListHashMap);
//		writeJSONObjectToStorage(taskListJSON, storageName);
//	}
//
//	private static JSONObject convertHashmapToJSONObject(TaskList activeTaskList) {
//		JSONObject activeTaskListJSON = new JSONObject();
//		Iterator it = activeTaskList.entrySet().iterator();
//	    while (it.hasNext()) {
//	        Map.Entry pair = (Map.Entry)it.next();
//	        Task tempTask = (Task) pair.getValue();
//	        addTaskToTaskListJSON(tempTask, activeTaskListJSON);
//	        it.remove(); // avoids a ConcurrentModificationException
//	    }
//	    return activeTaskListJSON;
//	}
//
//	private static void writeJSONObjectToStorage(JSONObject jsonObject, String storageName)
//			throws IOException {
//		FileWriter fileWriter = new FileWriter(storageName);  
//		fileWriter.write(jsonObject.toJSONString());  
//		fileWriter.flush();  
//		fileWriter.close();
//	}
//	
//	private static String convertDateToString(DeadlineTask task) {
//		DateFormat df = new SimpleDateFormat(STRING_FORMAT_FOR_DATE);
//		Date today = task.getDeadline();
//		String reportDate = df.format(today);
//		return reportDate;
//	}
//	
//	private static String convertStartDateToString(MeetingTask task) {
//		DateFormat df = new SimpleDateFormat(STRING_FORMAT_FOR_DATE);
//		Date today = task.getStartTime();
//		String reportDate = df.format(today);
//		return reportDate;
//	}
//	
//	private static String convertEndDateToString(MeetingTask task) {
//		DateFormat df = new SimpleDateFormat(STRING_FORMAT_FOR_DATE);
//		Date today = task.getEndTime();
//		String reportDate = df.format(today);
//		return reportDate;
//	}
//
//	// overloading method: add task to JSON object
//	@SuppressWarnings("unchecked")
//	private static void addTaskToTaskListJSON(Task task, JSONObject taskListJSON) {
//		JSONObject newTask = new JSONObject();
//		newTask.put("ID", task.getId());
//		newTask.put("Description", task.getDescription());
//		newTask.put("Type", task.getType());
//		newTask.put("Priority", task.getPriority());
//		newTask.put("Tags", task.getTags().toString());
//		newTask.put("Archived", task.isArchived());	
//		taskListJSON.put(task.getId(), newTask);
//	}
//	
//	// overloading method: add deadline task to JSON object
//	@SuppressWarnings("unchecked")
//	private static void addTaskToTaskListJSON(DeadlineTask task, JSONObject taskListJSON) {
//		
//		String reportDate = convertDateToString(task);
//		
//		JSONObject newTask = new JSONObject();
//		newTask.put("ID", task.getId());
//		newTask.put("Description", task.getDescription());
//		newTask.put("Deadline", reportDate);
//		newTask.put("Type", task.getType());
//		newTask.put("Priority", task.getPriority());
//		newTask.put("Tags", task.getTags().toString());
//		newTask.put("Archived", task.isArchived());	
//		taskListJSON.put(task.getId(), newTask);
//	}
//	
//	// overloading method: add meeting task to JSON object
//	@SuppressWarnings("unchecked")
//	private static void addTaskToTaskListJSON(MeetingTask task, JSONObject taskListJSON) {
//		
//		String startDate = convertStartDateToString(task);
//		String endDate = convertEndDateToString(task);
//		
//		JSONObject newTask = new JSONObject();
//		newTask.put("ID", task.getId());
//		newTask.put("Description", task.getDescription());
//		newTask.put("StartDate", startDate);
//		newTask.put("EndDate", endDate);
//		newTask.put("Type", task.getType());
//		newTask.put("Priority", task.getPriority());
//		newTask.put("Tags", task.getTags().toString());
//		newTask.put("Archived", task.isArchived());	
//		taskListJSON.put(task.getId(), newTask);
//	}

	// this method will create the JSON files for the storage
	private static DATA initializeStorage() throws IOException {
		processStorage(FILENAME_ACTIVE_TASKLIST);
		processStorage(FILENAME_ARCHIVE_TASKLIST);
		processStorage(FILENAME_DATE_LIST);
		processStorage(FILENAME_PRIORITY_LIST);
		processStorage(FILENAME_TAG_LIST);
		processStorage(FILENAME_TODO_SORTED_LIST);
		processStorage(FILENAME_ARCHIVE_SORTED_LIST);
		processStorage(FILENAME_PRIORITY_SORTED_LIST);
		
		return new DATA();
	}

	// check if JSON file exists, if not, create the JSON file.
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
