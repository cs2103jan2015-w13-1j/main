package Storage;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



import Common.ArchiveSortedList;
import Common.DATA;
import Common.Date;
import Common.PrioritySortedList;
import Common.Task;
import Common.TaskList;
import Common.ToDoSortedList;



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

	private static final String FILENAME = "tables/storage.json";
	
	private DATA storage;

	public static void main (String[] args) throws IOException {
		
	}
	
	public String storeAllData(DATA data) {
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
//		boolean archivedTaskList = writeTaskListToStorage(data.getArchivedTaskList(), FILENAME_ARCHIVE_TASKLIST);
//		boolean dateList = writeDateListToStorage(data.getDateList(), FILENAME_DATE_LIST);
//		boolean priorityList = writePriorityListToStorage(data.getPriorityList(), FILENAME_PRIORITY_LIST);
//		boolean tagList = writeTagListToStorage(data.getTagList(), FILENAME_TAG_LIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
//		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
		
		if (activeTaskList && archivedTaskList && dateList && priorityList & tagList) {
			return "success";
		} else {
			return "failure";
		}
	}

	public DATA getAllData() {
		Gson gson = new Gson();
		try {
			storage = initializeStorage();
			storage.setActiveTaskList(retrieveTasklistFromStorage(FILENAME_ACTIVE_TASKLIST));
			storage.setArchivedTaskList(retrieveTasklistFromStorage(FILENAME_ARCHIVE_TASKLIST));
			storage.setDateList(retrieveDatelistFromStorage());
			storage.setPriorityList(retrievePrioritylistFromStorage());
			storage.setTagList(retrieveTaglistFromStorage());
//			storage.setToDoSortedList(retrieveSortedToDoFromStorage());
//			storage.setArchiveSortedList(retrieveSortedArchiveFromStorage());
//			storage.setPrioritySortedList(retrieveSortedPriorityFromStorage());
			
//			System.out.println(gson.toJson(storage));
			
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
	    File file = new File(fileName);
		long timeBeforeModification = file.lastModified();
		long timeAfterModification = -1;
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(taskListJSON.toJSONString());
			fileWriter.flush();  
			fileWriter.close();
			timeAfterModification = file.lastModified();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeAfterModification > timeBeforeModification;
	}

	@SuppressWarnings({ "rawtypes", "resource" })
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
		
		System.out.println("Retrieving from " + storageName);
		
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
		System.out.println(gson.toJson(taskListHashMap));
		return taskListHashMap;
	}

	@Override
	public String insertFileDirectory(String directory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileDirectory() {
		// TODO Auto-generated method stub
		return null;
	}
}
