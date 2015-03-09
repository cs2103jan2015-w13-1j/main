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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Common.DATA;
import Common.Date;
import Common.StorageUtil;
import Common.Task;
import Common.TaskList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StorageController implements InterfaceForStorage {
	
	private static final String STRING_ARCHIVED_TASK_LIST = "archivedTaskList";
	private static final String STRING_ACTIVE_TASK_LIST = "activeTaskList";
	private static final String MESSAGE_STORAGE_INITIALISE_FAILURE = "Storage unable to initialise";
	private static final String MESSAGE_STORAGE_INITIALISE_SUCCESS = "Storage initialised";
	private static final String MESSAGE_RETRIEVE_SUCCESS = "Data is retrieved";
	private static final String MESSAGE_RETRIEVE_FROM_EMPTY_FILE = "File is empty. No data is retrieved.";
	private static final String MESSAGE_STORE_DATA_FAILURE = "failure in storing";
	private static final String MESSAGE_STORE_DATA_SUCCESS = "success in storing";
	private static final int STARTING_INDEX = 0;
	
	private static DATA data;
	private static StorageUtil util;
	
	public static void main(String[] args) {
		StorageController control = new StorageController();
		
		control.run();
//		control.getAllData();
//		control.setFileDirectory("tables/");
//		System.out.println(control.storeAllData(data));
		
	}
	
	private void run() {
		initialiseNewDataObject();
		createDummyData();
	}
	
	@Override
	public DATA getAllData() {
		// clear DATA object first
		// perform utility check, this will retrieve the data from utility file and store into util.
			// if utility file is does not exists or exists but empty, defaults will be saved into utility.
		// util will provide the directory of the storage, so need to check if storage exists
			// if storage does not exists or storage exists but empty, DATA = null
			// if storage exist and contains info, retrieve DATA from storage
		// return DATA
		initialiseNewDataObject();
		processUtil();
		if (isStorageExist() == true) {
			retriveDataFromStorage();
		} else {
//			createNewStorage();
			// must store data first, cannot create new storage because user might change directory
			return data; // empty data
		}
		return data;
	}

	@SuppressWarnings("static-access")
	@Override
	public String storeAllData(DATA data) {
		// perform utility check, this will retrieve the data from utility file and store into util.
			// if utility file is does not exists or exists but empty, defaults will be saved into utility.
		// util will provide the directory of the storage, so need to check if storage exists
			// if storage exists, overwrites the storage
			// if not exists, creates and write into storage
		processUtil();
		if (isStorageExist() == false) {
			createNewStorage();
		}
		this.data = data;
		if (storeDataIntoStorage(convertDataIntoJSONObject(), getFileRelativePath()) == true) {
			return MESSAGE_STORE_DATA_SUCCESS;
		}
		return MESSAGE_STORE_DATA_FAILURE;
	}
	
	private void deleteOldFile() {
		File oldFile = new File(getFileRelativePath());
		oldFile.setWritable(true);
		if (oldFile.exists()) {
			oldFile.delete();
//			System.out.println(oldFile + " deleted");
		}
	}
	
	private void convertJSONObjectIntoData(JSONObject dataJSON) {
		getSerialFromJSON(dataJSON);
		TaskList activeTaskList = getTaskListFromJSON(dataJSON, STRING_ACTIVE_TASK_LIST);
		data.setActiveTaskList(activeTaskList);
		TaskList archivedTaskList = getTaskListFromJSON(dataJSON, STRING_ARCHIVED_TASK_LIST);
	    data.setArchivedTaskList(archivedTaskList);
	}

	@SuppressWarnings("rawtypes")
	private TaskList getTaskListFromJSON(JSONObject dataJSON, String taskList) {
		TaskList taskListHashMap = new TaskList();
		Gson gson = new Gson();
		JSONObject tasklistJSON = (JSONObject) dataJSON.get(taskList);
		Iterator it = tasklistJSON.entrySet().iterator();
		
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	    	JSONObject taskJSON = (JSONObject) tasklistJSON.get(pair.getKey()); 

	    	// Generic Type attributes
	    	int id = gson.fromJson(String.valueOf(taskJSON.get("id")) , int.class);
	    	String description = String.valueOf(taskJSON.get("description"));
			String type = String.valueOf(taskJSON.get("type"));
			ArrayList<String> tags = gson.fromJson(String.valueOf(taskJSON.get("tags")) , new TypeToken<ArrayList<String>>() {}.getType());
			int priority = gson.fromJson(String.valueOf(taskJSON.get("priority")) , int.class);
			boolean archived = gson.fromJson(String.valueOf(taskJSON.get("archived")) , boolean.class);
			
			Task task = determineAndCreateTaskByType(taskJSON, id, description, type, tags, priority, archived);
			
			taskListHashMap.addTask(task.getId(), task);
	    	it.remove(); // avoids a ConcurrentModificationException
        }
		return taskListHashMap;
	}

	private Task determineAndCreateTaskByType(JSONObject taskJSON, int id, String description, String type, ArrayList<String> tags,	int priority, boolean archived) {
		Task task = createTaskByAttributes(taskJSON, id, description, type, tags, priority);
		verifyTaskArchiveBoolean(taskJSON, archived, task);
		return task;
	}

	private Task createTaskByAttributes(JSONObject taskJSON, int id, String description, String type, ArrayList<String> tags, int priority) {
		Task task;
		Gson gson = new Gson();
		if (type.equals("deadline")) {
			// Deadline Type
			long string_date = gson.fromJson(String.valueOf(taskJSON.get("deadline")) , long.class);
			Date deadline = new Date();
			deadline.setTime(string_date);
			task = new Task(id, description, deadline, priority, tags);
		} else if (type.equals("meeting")) {
			// Meeting Type
			long long_start_date = gson.fromJson(String.valueOf(taskJSON.get("startTime")) , long.class);
			Date startDate = new Date();
			startDate.setTime(long_start_date);
			
			long long_end_date = gson.fromJson(String.valueOf(taskJSON.get("endTime")) , long.class);
			Date endDate = new Date();
			endDate.setTime(long_end_date);
			
			task = new Task(id, description, startDate, endDate, priority, tags);
		} else {
			// Generic type
			task = new Task(id, description, priority, tags);
		}
		return task;
	}
	
	private void verifyTaskArchiveBoolean(JSONObject taskJSON,	boolean archived, Task task) {
		Gson gson = new Gson();
		if (archived == true) {
			long string_finished_time= gson.fromJson(String.valueOf(taskJSON.get("finishedTime")) , long.class);
			Date finishedTime = new Date();
			finishedTime.setTime(string_finished_time);
			task.moveToArchive(finishedTime);
		}
	}

	private void getSerialFromJSON(JSONObject dataJSON) {
		Gson gson = new Gson();
		// retrieve serial number and store into data
		data.setSerialNumber(gson.fromJson(String.valueOf(dataJSON.get("serialNumber")) , int.class));
	}

	@SuppressWarnings("resource")
	private String retriveDataFromStorage() {
		// do nothing if task list is empty
		String fileName = getFileRelativePath();
		JSONObject dataJSON = new JSONObject();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			if (br.readLine() == null) {
				System.out.println(fileName + " " + MESSAGE_RETRIEVE_FROM_EMPTY_FILE);
				return MESSAGE_RETRIEVE_FROM_EMPTY_FILE;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(fileName));
			dataJSON = (JSONObject) obj; 
			if (dataJSON.containsKey("serialNumber") == false) {
				System.out.println(fileName + " " + MESSAGE_RETRIEVE_FROM_EMPTY_FILE);
				return MESSAGE_RETRIEVE_FROM_EMPTY_FILE;
			}
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		convertJSONObjectIntoData(dataJSON);
		System.out.println(MESSAGE_RETRIEVE_SUCCESS);
		return MESSAGE_RETRIEVE_SUCCESS;
	}

	private boolean storeDataIntoStorage(JSONObject json, String fileName) {
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
			e.printStackTrace();
		}
		return timeAfterModification > timeBeforeModification;
	}

	private JSONObject convertDataIntoJSONObject() {
		JSONObject dataJSON = new JSONObject();
		try {
			Gson gson = new Gson();
			JSONParser parser = new JSONParser();
			dataJSON = (JSONObject) parser.parse(gson.toJson(getData()));
			processDataJsonByTaskListType(dataJSON, STRING_ACTIVE_TASK_LIST);
			processDataJsonByTaskListType(dataJSON, STRING_ARCHIVED_TASK_LIST);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dataJSON;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processDataJsonByTaskListType(JSONObject dataJSON, String taskList) {
		JSONObject taskListJSON = (JSONObject) dataJSON.get(taskList);
		Iterator it = null;
		it = determineTaskList(taskList, it);
		while (it.hasNext()) {
		    Map.Entry pair = (Map.Entry)it.next();
			Task task = (Task) pair.getValue();
			JSONObject taskJSON = (JSONObject) taskListJSON.get(String.valueOf(task.getId()));
			modifyNonGenericTaskForDataJson(task, taskJSON); 
			processArchivedTaskForDataJson(task, taskJSON);
			taskListJSON.replace(String.valueOf(task.getId()), taskJSON);
		}
		dataJSON.replace(taskList, taskListJSON);
//		System.out.println("done with " + taskList);
	}

	@SuppressWarnings("unchecked")
	private void processArchivedTaskForDataJson(Task task, JSONObject taskJSON) {
		if (task.isArchived() == true) {
			taskJSON.replace("finishedTime", task.getFinishedTime().getTime());
		}
	}

	@SuppressWarnings("unchecked")
	private void modifyNonGenericTaskForDataJson(Task task, JSONObject taskJSON) {
		if (taskJSON.containsKey("deadline")) {
			taskJSON.replace("deadline", task.getDeadline().getTime());
		} else if (taskJSON.containsKey("startTime")) {
			taskJSON.replace("startTime", task.getStartTime().getTime());
			taskJSON.replace("endTime", task.getEndTime().getTime());
		}
	}

	@SuppressWarnings("rawtypes")
	private Iterator determineTaskList(String taskList, Iterator it) {
		if (taskList.equals(STRING_ACTIVE_TASK_LIST)) {
			it = data.getActiveTaskList().entrySet().iterator();
		} else if (taskList.equals(STRING_ARCHIVED_TASK_LIST)) {
			it = data.getArchivedTaskList().entrySet().iterator();
		}
		return it;
	}

	// to retrieve utility data from storage
	public String processUtil() {
		JSONObject utilJSON = new JSONObject();
		Gson gson = new Gson();
		String output = "";
		if (isStorageExist("tables/utility.json") == false) {
			createStorage("tables/utility.json");
			output = output.concat("tables/utility.json" + " does not exist.\n");
			output = output.concat("created " + "tables/utility.json" + " for utility startup.");
			addDefaultsToUtil();
		} else {
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(new FileReader("tables/utility.json"));
				utilJSON = (JSONObject) obj;
				if (utilJSON.containsKey("directory") == false) {
					System.out.println("tables/utility.json" + " has no data. Add defaults to utility.");
					addDefaultsToUtil();
					return MESSAGE_RETRIEVE_FROM_EMPTY_FILE;
				}
				util = gson.fromJson(utilJSON.toJSONString() , StorageUtil.class);
				output = output.concat("check utility [OK]");
//				System.out.println(output);
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	private void addDefaultsToUtil() {
		util = new StorageUtil();
		util.setDirectory("tables/");			// default settings
		util.setStorageName("storage.json");	// default settings
//					System.out.println(output);
		saveUtilToStorage();
	}

	private void saveUtilToStorage() {
		JSONParser parser = new JSONParser();
		JSONObject utilJSON = new JSONObject();
		Gson gson = new Gson();
		try {
			utilJSON = (JSONObject) parser.parse(gson.toJson(util));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		storeDataIntoStorage(utilJSON, "tables/utility.json");
	}

	private void initialiseNewDataObject() {
		data = new DATA();
		data.setActiveTaskList(new TaskList());
		data.setArchivedTaskList(new TaskList());
		data.setSerialNumber(STARTING_INDEX);
	}
	
	public boolean isStorageExist() {
		return isStorageExist(getFileRelativePath());
	}
	
	public void createNewStorage() {
		createStorage(getFileRelativePath());
		System.out.println(getFileRelativePath() + " new storage created.");
	}
	
	private void createStorage(String fileRelativePath) {
		try {
			File storageFile = new File(fileRelativePath);
			if (storageFile.getParentFile() != null) {
				storageFile.getParentFile().mkdirs();
			}
			storageFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Overloading method
	private boolean isStorageExist(String fileRelativePath) {
		File file = new File(fileRelativePath);
		return file.exists();
	}

	public String getFileRelativePath() {
		return util.getDirectory().concat(util.getStorageName());
	}

	public DATA getData() {
		return data;
	}

	public void setData(DATA data) {
		StorageController.data = data;
	}

	@Override
	public String getFileDirectory() {
		return util.getDirectory();
	}

	@Override
	// change the file directory and update utility file
	public String setFileDirectory(String fileDirectory) {
		processUtil(); // in case this method is called before other methods
		util.setDirectory(fileDirectory);
		saveUtilToStorage();
//		System.out.println("new directory : " + util.getDirectory());
		return util.getDirectory();
	}
	
	// create 9 dummy tasks and store into data for testing
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

		data.getActiveTaskList().addTask(dummyGenericTask1.getId(), dummyGenericTask1);
		data.getActiveTaskList().addTask(dummyGenericTask3.getId(), dummyGenericTask3);
		data.getActiveTaskList().addTask(dummyDeadlineTask2.getId(), dummyDeadlineTask2);
		data.getActiveTaskList().addTask(dummyDeadlineTask3.getId(), dummyDeadlineTask3);
		data.getActiveTaskList().addTask(dummyMeetingTask1.getId(), dummyMeetingTask1);
		data.getActiveTaskList().addTask(dummyMeetingTask3.getId(), dummyMeetingTask3);

		data.getArchivedTaskList().addTask(dummyGenericTask2.getId(), dummyGenericTask2);
		data.getArchivedTaskList().addTask(dummyDeadlineTask1.getId(), dummyDeadlineTask1);
		data.getArchivedTaskList().addTask(dummyMeetingTask2.getId(), dummyMeetingTask2);

		return "Dummy data created";
	}
}
