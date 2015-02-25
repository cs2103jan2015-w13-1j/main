import hashMaps.TaskList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import basicElements.Date;
import basicElements.DeadlineTask;
import basicElements.MeetingTask;
import basicElements.Task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
 * 4.	Once user is ready to exit the program, logic controller will call the storeAllData(DATA data) method to save the state of the modification.
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
	}
	
	public void run() throws IOException {
		storage = getAllData();
//		System.out.println(storage.getActiveTaskList());
//		System.out.println(storage.getArchivedTaskList());
		System.out.println(storeAllData(storage));
	}
	
	public String storeAllData(DATA data) {
		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
		boolean archivedTaskList = writeTaskListToStorage(data.getArchivedTaskList(), FILENAME_ARCHIVE_TASKLIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
		if (activeTaskList && archivedTaskList) {
			return "success";
		} else {
			return "failure";
		}
	}
	
	public DATA getAllData() {
		try {
			storage = initializeStorage();
			storage.setActiveTaskList(retrieveTasklistFromStorage(FILENAME_ACTIVE_TASKLIST));
			storage.setArchivedTaskList(retrieveTasklistFromStorage(FILENAME_ARCHIVE_TASKLIST));
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean writeTaskListToStorage(TaskList taskList, String fileName) {
		// do nothing if task list is empty 
		if (taskList.isEmpty()) {
			return true;
		}
		
		Gson gson = new Gson();
		JSONParser parser = new JSONParser();
		JSONObject taskListJSON = new JSONObject();
		Iterator it = taskList.entrySet().iterator();
	    while (it.hasNext()) {
	        try {
	        	Map.Entry pair = (Map.Entry)it.next();
		        Task genericTask = (Task) pair.getValue();
				JSONObject taskJSON = (JSONObject) parser.parse(gson.toJson(pair.getValue()));
				if (taskJSON.containsKey("deadline")) {
					DeadlineTask deadlineTask = (DeadlineTask) genericTask;
					taskJSON.replace("deadline", deadlineTask.getDeadline().getTime());
					taskJSON.put("type", deadlineTask.getType());
				} else if (taskJSON.containsKey("startTime")) {
					MeetingTask meetingTask = (MeetingTask) genericTask;
					taskJSON.replace("startTime", meetingTask.getStartTime().getTime());
					taskJSON.replace("endTime", meetingTask.getEndTime().getTime());
					taskJSON.put("type", meetingTask.getType());
				} else {
					taskJSON.put("type", genericTask.getType());
				}
				taskListJSON.put(genericTask.getId(), taskJSON);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(taskListJSON.toJSONString());
			fileWriter.flush();  
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return taskList.isEmpty();
	}

	@SuppressWarnings("rawtypes")
	private TaskList retrieveTasklistFromStorage(String storageName) {
		
		// do nothing if task list is empty 
		try {
			BufferedReader br = new BufferedReader(new FileReader(storageName));
			if (br.readLine() == null) {
			    System.out.println(storageName + " is empty");
			    return new TaskList();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}     
		
		
		JSONParser parser = new JSONParser();
		Gson gson = new Gson();
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
		    	int id = gson.fromJson(String.valueOf(taskJSON.get("id")) , int.class);
		    	String description = String.valueOf(taskJSON.get("description"));
				String type = String.valueOf(taskJSON.get("type"));
				ArrayList<String> tags = gson.fromJson(String.valueOf(taskJSON.get("tags")) , new TypeToken<ArrayList<String>>() {}.getType());
				int priority = gson.fromJson(String.valueOf(taskJSON.get("priority")) , int.class);
				boolean archived = gson.fromJson(String.valueOf(taskJSON.get("archived")) , boolean.class);
				if (type.equals("DeadlineTask")) {
					// Deadline Type
					long string_date = gson.fromJson(String.valueOf(taskJSON.get("deadline")) , long.class);
					Date deadline = new Date();
					deadline.setTime(string_date);
					DeadlineTask deadlineTask = new DeadlineTask(id, description, deadline, priority, tags, archived);
					taskListHashMap.addTask(deadlineTask.getId(), deadlineTask);
				} else if (type.equals("MeetingTask")) {
					// Meeting Type
					long long_start_date = gson.fromJson(String.valueOf(taskJSON.get("startTime")) , long.class);
					Date startDate = new Date();
					startDate.setTime(long_start_date);
					
					long long_end_date = gson.fromJson(String.valueOf(taskJSON.get("endTime")) , long.class);
					Date endDate = new Date();
					endDate.setTime(long_end_date);
					
					MeetingTask meetingTask = new MeetingTask(id, description, startDate, endDate, priority, tags, archived);
					taskListHashMap.addTask(meetingTask.getId(), meetingTask);
				} else {
					// Generic type
					Task task = new Task(id, description, priority, tags, archived);
					taskListHashMap.addTask(task.getId(), task);
				}
		    	it.remove(); // avoids a ConcurrentModificationException
	        }
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return taskListHashMap;
	}
	

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
