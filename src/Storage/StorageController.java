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
	}
	
	public void run() throws IOException {
		initialiseDummyDataForTesting();			// generates dummy tasks for testing, creates a DATA object and store in storage
		storage = getAllData();						// retrieves all data from JSON and save into storage
		System.out.println(storeAllData(storage));	// stores all data from storage into JSON
	}
	
	public String storeAllData(DATA data) {
		boolean activeTaskList = writeTaskListToStorage(data.getActiveTaskList(), FILENAME_ACTIVE_TASKLIST);
		boolean archivedTaskList = writeTaskListToStorage(data.getArchivedTaskList(), FILENAME_ARCHIVE_TASKLIST);
		boolean dateList = writeDateListToStorage(data.getDateList(), FILENAME_DATE_LIST);
		boolean priorityList = writePriorityListToStorage(data.getPriorityList(), FILENAME_PRIORITY_LIST);
		boolean tagList = writeTagListToStorage(data.getTagList(), FILENAME_TAG_LIST);
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
	
	@SuppressWarnings({ "resource", "rawtypes" })
	private TagList retrieveTaglistFromStorage() {
		// do nothing if task list is empty 
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILENAME_TAG_LIST));
			if (br.readLine() == null) {
				System.out.println(FILENAME_TAG_LIST + " is empty");
				return new TagList();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JSONParser parser = new JSONParser();
		Gson gson = new Gson();
		Object obj = null;

		TagList tagList = new TagList();
		try {
			obj = parser.parse(new FileReader(FILENAME_TAG_LIST));
			JSONObject tagListJSON = (JSONObject) obj; 

			Iterator it = tagListJSON.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				JSONObject tagJSON = (JSONObject) tagListJSON.get(pair.getKey());
				TaskByTag tempTaskByTagObject = gson.fromJson(tagJSON.toJSONString(), TaskByTag.class);
				tagList.addNewTag(tempTaskByTagObject.getTag(), tempTaskByTagObject);
				it.remove(); // avoids a ConcurrentModificationException
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return tagList;
	}

	@SuppressWarnings({ "resource", "rawtypes" })
	private PriorityList retrievePrioritylistFromStorage() {
		// do nothing if task list is empty 
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILENAME_PRIORITY_LIST));
			if (br.readLine() == null) {
				System.out.println(FILENAME_PRIORITY_LIST + " is empty");
				return new PriorityList();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JSONParser parser = new JSONParser();
		Gson gson = new Gson();
		Object obj = null;

		PriorityList priorityList = new PriorityList();
		try {
			obj = parser.parse(new FileReader(FILENAME_PRIORITY_LIST));
			JSONObject priorityListJSON = (JSONObject) obj; 

			Iterator it = priorityListJSON.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				JSONObject priorityJSON = (JSONObject) priorityListJSON.get(pair.getKey());
				TaskByPriority tempTaskByPriorityObject = gson.fromJson(priorityJSON.toJSONString(), TaskByPriority.class);
				priorityList.addNewPriority(tempTaskByPriorityObject.getProirity(), tempTaskByPriorityObject);
				it.remove(); // avoids a ConcurrentModificationException
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return priorityList;
	}

	@SuppressWarnings({ "resource", "rawtypes" })
	private DateList retrieveDatelistFromStorage() {
		// do nothing if task list is empty 
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILENAME_DATE_LIST));
			if (br.readLine() == null) {
			    System.out.println(FILENAME_DATE_LIST + " is empty");
			    return new DateList();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		JSONParser parser = new JSONParser();
		Gson gson = new Gson();
		Object obj = null;
		
		DateList dateList = new DateList();
		try {
			obj = parser.parse(new FileReader(FILENAME_DATE_LIST));
			JSONObject dateListJSON = (JSONObject) obj; 
			
			Iterator it = dateListJSON.entrySet().iterator();
		    while (it.hasNext()) {
		    	Map.Entry pair = (Map.Entry)it.next();
		    	JSONObject dateJSON = (JSONObject) dateListJSON.get(pair.getKey());
		    	TaskByDate tempTaskByDateObject = gson.fromJson(dateJSON.toJSONString(), TaskByDate.class);
		    	dateList.addNewDate(tempTaskByDateObject.getDate(), tempTaskByDateObject);
		    	it.remove(); // avoids a ConcurrentModificationException
	        }
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}

	private boolean writeTagListToStorage(TagList tagList, String fileName) {
		Gson gson = new Gson();
		File file = new File(fileName);
		long timeBeforeModification = file.lastModified();
		long timeAfterModification = -1;
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(gson.toJson(tagList));
			fileWriter.flush();  
			fileWriter.close();
			timeAfterModification = file.lastModified();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeAfterModification > timeBeforeModification;
	}
	
	private boolean writePriorityListToStorage(PriorityList priorityList, String fileName) {
		Gson gson = new Gson();
		File file = new File(fileName);
		long timeBeforeModification = file.lastModified();
		long timeAfterModification = -1;
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(gson.toJson(priorityList));
			fileWriter.flush();  
			fileWriter.close();
			timeAfterModification = file.lastModified();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeAfterModification > timeBeforeModification;
	}
	
	private boolean writeDateListToStorage(DateList dateList, String fileName) {
		Gson gson = new Gson();
		File file = new File(fileName);
		long timeBeforeModification = file.lastModified();
		long timeAfterModification = -1;
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(gson.toJson(dateList));
			fileWriter.flush();  
			fileWriter.close();
			timeAfterModification = file.lastModified();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeAfterModification > timeBeforeModification;
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

	// this method will create the JSON files for the storage
	private DATA initializeStorage() throws IOException {
		processStorage(FILENAME_ACTIVE_TASKLIST);
		processStorage(FILENAME_ARCHIVE_TASKLIST);
		processStorage(FILENAME_DATE_LIST);
		processStorage(FILENAME_PRIORITY_LIST);
		processStorage(FILENAME_TAG_LIST);
		processStorage(FILENAME_TODO_SORTED_LIST);
		processStorage(FILENAME_ARCHIVE_SORTED_LIST);
		processStorage(FILENAME_PRIORITY_SORTED_LIST);
		
		storage = new DATA();
		storage.setActiveTaskList(new TaskList());
		storage.setArchivedTaskList(new TaskList());
		storage.setArchiveSortedList(new ArchiveSortedList());
		storage.setDateList(new DateList());
		storage.setPriorityList(new PriorityList());
		storage.setPrioritySortedList(new PrioritySortedList());
		storage.setTagList(new TagList());
		storage.setToDoSortedList(new ToDoSortedList());
		
		return storage;
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
	
	private void initialiseDummyDataForTesting() {
		try {
			storage = initializeStorage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// dummy GenericTask
		Task dummyGenericTask1 = new Task(1, "dummyGenericTask 1", 1, null, false);
		dummyGenericTask1.addTag("Personal");
		Task dummyGenericTask2 = new Task(2, "dummyGenericTask 2", 2, null, true);
		dummyGenericTask2.addTag("Work");
		Task dummyGenericTask3 = new Task(3, "dummyGenericTask 3", 3, null, false);
		dummyGenericTask3.addTag("School");
		// dummy DeadlineTask
		DeadlineTask dummyDeadlineTask1 = new DeadlineTask(4, "dummyDeadlineTask 1", null, 2, null, true);
		long deadlinetime1 = Long.valueOf("1424361600000"); // date: 20/02/2015
		Date deadline1 = new Date();
		deadline1.setTime(deadlinetime1);
		dummyDeadlineTask1.changeDeadline(deadline1);
		dummyDeadlineTask1.addTag("Personal");
		dummyDeadlineTask1.addTag("Work");
		DeadlineTask dummyDeadlineTask2 = new DeadlineTask(5, "dummyDeadlineTask 2", null, 2, null, false);
		long deadlinetime2 = Long.valueOf("1424797200000"); // date: 25/02/2015
		Date deadline2 = new Date();
		deadline2.setTime(deadlinetime2);
		dummyDeadlineTask2.changeDeadline(deadline2);
		dummyDeadlineTask2.addTag("Work");
		dummyDeadlineTask2.addTag("School");
		DeadlineTask dummyDeadlineTask3 = new DeadlineTask(6, "dummyDeadlineTask 3", null, 2, null, false);
		long deadlinetime3 = Long.valueOf("1425056400000"); // date: 28/02/2015 1 am
		Date deadline3 = new Date();
		deadline3.setTime(deadlinetime3);
		dummyDeadlineTask3.changeDeadline(deadline3);
		dummyDeadlineTask3.addTag("Personal");
		dummyDeadlineTask3.addTag("School");
		// dummy MeetingTask
		MeetingTask dummyMeetingTask1 = new MeetingTask(7, "dummyMeetingTask 1", null, null, 3, null, false);
		dummyMeetingTask1.changeStartTime(deadline2);
		dummyMeetingTask1.changeEndTime(deadline3);
		dummyMeetingTask1.addTag("Work");
		dummyMeetingTask1.addTag("Personal");
		MeetingTask dummyMeetingTask2 = new MeetingTask(8, "dummyMeetingTask 2", null, null, 3, null, true);
		dummyMeetingTask2.changeStartTime(deadline1);
		dummyMeetingTask2.changeEndTime(deadline2);
		dummyMeetingTask2.addTag("Personal");
		dummyMeetingTask2.addTag("School");
		MeetingTask dummyMeetingTask3 = new MeetingTask(9, "dummyMeetingTask 3", null, null, 3, null, false);
		dummyMeetingTask3.changeStartTime(deadline1);
		dummyMeetingTask3.changeEndTime(deadline3);
		dummyMeetingTask3.addTag("Work");
		dummyMeetingTask3.addTag("Personal");
		
		storage.setSerialNumber(10); // hard-coded serial number
		
		storage.getActiveTaskList().addTask(dummyGenericTask1.getId(), dummyGenericTask1);
		storage.getActiveTaskList().addTask(dummyGenericTask3.getId(), dummyGenericTask3);
		storage.getActiveTaskList().addTask(dummyDeadlineTask2.getId(), dummyDeadlineTask2);
		storage.getActiveTaskList().addTask(dummyDeadlineTask3.getId(), dummyDeadlineTask3);
		storage.getActiveTaskList().addTask(dummyMeetingTask1.getId(), dummyMeetingTask1);
		storage.getActiveTaskList().addTask(dummyMeetingTask3.getId(), dummyMeetingTask3);
		
		storage.getArchivedTaskList().addTask(dummyGenericTask2.getId(), dummyGenericTask2);
		storage.getArchivedTaskList().addTask(dummyDeadlineTask1.getId(), dummyDeadlineTask1);
		storage.getArchivedTaskList().addTask(dummyMeetingTask2.getId(), dummyMeetingTask2);
		
//		System.out.println(gson.toJson(storage.getActiveTaskList()));
//		System.out.println(gson.toJson(storage.getArchivedTaskList()));
		
		String newDate20150225 = new SimpleDateFormat("yyyyMMdd").format(convertStringToLong("20150225"));
		String newDate20150220 = new SimpleDateFormat("yyyyMMdd").format(convertStringToLong("20150220"));
		String newDate20150228 = new SimpleDateFormat("yyyyMMdd").format(convertStringToLong("20150228"));
		
		TaskByDate tbd20150220 = new TaskByDate(newDate20150220);
		tbd20150220.addToDoTask(9);
		tbd20150220.addArchivedTask(1);
		tbd20150220.addArchivedTask(8);
		storage.getDateList().addNewDate(newDate20150220, tbd20150220);
		TaskByDate tbd20150225 = new TaskByDate(newDate20150225);
		tbd20150225.addToDoTask(5);
		tbd20150225.addToDoTask(7);
		storage.getDateList().addNewDate(newDate20150225, tbd20150225);
		TaskByDate tbd20150228 = new TaskByDate(newDate20150228);
		tbd20150228.addToDoTask(6);
		storage.getDateList().addNewDate(newDate20150228, tbd20150228);
		
//		System.out.println(gson.toJson(storage.getDateList()));
		
		TaskByPriority tbp1 = new TaskByPriority(1);
		tbp1.addToDoTask(1);
		TaskByPriority tbp2 = new TaskByPriority(2);
		tbp2.addArchivedTask(2);
		tbp2.addArchivedTask(4);
		tbp2.addToDoTask(5);
		tbp2.addToDoTask(6);
		TaskByPriority tbp3 = new TaskByPriority(3);
		tbp3.addToDoTask(3);
		tbp3.addToDoTask(7);
		tbp3.addToDoTask(9);
		tbp3.addArchivedTask(8);
		
		storage.getPriorityList().addNewPriority(1, tbp1);
		storage.getPriorityList().addNewPriority(2, tbp2);
		storage.getPriorityList().addNewPriority(3, tbp3);
		
//		System.out.println(gson.toJson(storage.getPriorityList()));
		
		TaskByTag tbtPersonal = new TaskByTag("Personal");
		tbtPersonal.addToDoTask(1);
		tbtPersonal.addToDoTask(6);
		tbtPersonal.addToDoTask(7);
		tbtPersonal.addToDoTask(9);
		tbtPersonal.addArchivedTask(4);
		tbtPersonal.addArchivedTask(8);
		TaskByTag tbtWork = new TaskByTag("Work");
		tbtWork.addToDoTask(5);
		tbtWork.addToDoTask(7);
		tbtWork.addToDoTask(9);
		tbtWork.addArchivedTask(2);
		tbtWork.addArchivedTask(4);
		TaskByTag tbtSchool = new TaskByTag("School");
		tbtSchool.addToDoTask(3);
		tbtSchool.addToDoTask(5);
		tbtSchool.addToDoTask(6);
		tbtSchool.addArchivedTask(8);
		
		storage.getTagList().addNewTag("Personal", tbtPersonal);
		storage.getTagList().addNewTag("Work", tbtWork);
		storage.getTagList().addNewTag("School", tbtSchool);
		
//		System.out.println(gson.toJson(storage.getTagList()));
		
//		storage.getToDoSortedList().add(dummyGenericTask1);
//		storage.getToDoSortedList().add(dummyGenericTask3);
//		storage.getToDoSortedList().add(dummyDeadlineTask2);
//		storage.getToDoSortedList().addTask(dummyDeadlineTask3);
//		storage.getToDoSortedList().addTask(dummyMeetingTask1);
//		storage.getToDoSortedList().addTask(dummyMeetingTask3);
		
//		System.out.println(gson.toJson(storage.getToDoSortedList()));
		
//		storage.getArchiveSortedList().addTask(dummyGenericTask2);
//		storage.getArchiveSortedList().addTask(dummyDeadlineTask1);
//		storage.getArchiveSortedList().addTask(dummyMeetingTask2);
		
//		System.out.println(gson.toJson(storage.getArchiveSortedList()));
		
//		storage.getPrioritySortedList().add(dummyGenericTask1);
//		storage.getPrioritySortedList().add(dummyGenericTask2);
//		storage.getPrioritySortedList().add(dummyGenericTask3);
//		storage.getPrioritySortedList().add(dummyDeadlineTask1);
//		storage.getPrioritySortedList().add(dummyDeadlineTask2);
//		storage.getPrioritySortedList().add(dummyDeadlineTask3);
//		storage.getPrioritySortedList().add(dummyMeetingTask1);
//		storage.getPrioritySortedList().add(dummyMeetingTask2);
//		storage.getPrioritySortedList().add(dummyMeetingTask3);

//		System.out.println(gson.toJson(storage.getPrioritySortedList()));
		
//		System.out.println(gson.toJson(storage));
	}
	
	private long convertStringToLong(String stringDate) {
		Date date = new Date();
		try {
			java.util.Date myDate = new SimpleDateFormat("yyyyMMdd").parse(stringDate);
			date.setTime(myDate.getTime());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
}
