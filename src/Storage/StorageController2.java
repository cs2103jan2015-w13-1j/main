/**
 * This java class is the controller for the Storage component in the software architecture.
 * 
 * Dependency files: 
 * - jar/gson-2.3.1.jar for Gson library 
 * - jar/json-simpler-1.1.1.jar for JSON library
 * 
 * Test driver: StorageADT.java
 * 
 * @author Esmond
 */

package Storage;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;

import Common.DATA;
import Common.Date;
import Common.Task;
import Common.TaskList;

public class StorageController2 implements InterfaceForStorage {
	private static final int STARTING_INDEX = 0;
	private static DATA data;
	private static String FILE_NAME = "storage.json";
	private static String FILE_DIRECTORY = "";
	
	public static void main(String[] args) {
		StorageController2 control = new StorageController2();
		control.run();
	}
	
	@Override
	public DATA getAllData() {
		// perform storage initialization 
		// retrieve DATA from storage
		// return DATA
		return null;
	}

	@Override
	public String storeAllData(DATA data) {
		// convert DATA into JSON object
		// store JSON object into storage
		// return success/failure message
		return null;
	}
	
	private void run() {
		insertFileDirectory("tables/");	
		initialiseStorage();
		createDummyData();
		convertDataIntoJSONObject(); // return a JSON object
		storeDataIntoStorage();
		retriveDateFromStorage();
		convertJSONObjectIntoData();
	}
	
	private void convertJSONObjectIntoData() {
		// TODO Auto-generated method stub
		
	}

	private void retriveDateFromStorage() {
		// TODO Auto-generated method stub
		
	}

	private void storeDataIntoStorage() {
		// TODO Auto-generated method stub
		
	}

	private JSONObject convertDataIntoJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}

	// create the storage using the directory indicated by the users and initialize data
	public String initialiseStorage() {
		processStorage();
		initialiseNewDataObject();
		if (isStorageExist(getFileRelativePath()) == true) {
			return "Storage initialised";
		} else {
			return "Storage unable to initialise";
		}
		
	}

	private void initialiseNewDataObject() {
		data = new DATA();
		data.setActiveTaskList(new TaskList());
		data.setArchivedTaskList(new TaskList());
		data.setSerialNumber(STARTING_INDEX);
	}
	
	private void processStorage() {
		if (isStorageExist(getFileRelativePath()) == false) {
			createStorage(getFileRelativePath());
		}
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

	private boolean isStorageExist(String fileRelativePath) {
		File file = new File(fileRelativePath);
		return file.exists();
	}

	public String getFileRelativePath() {
		return FILE_DIRECTORY.concat(FILE_NAME);
	}

	@Override
	// this method will be call by parser for the user to select the directory for the storage
	public String insertFileDirectory(String directory) {
		setFileDirectory(directory);
		return getFileDirectory();
	}

	public DATA getData() {
		return data;
	}

	public void setData(DATA data) {
		StorageController2.data = data;
	}

	@Override
	public String getFileDirectory() {
		return FILE_DIRECTORY;
	}

	public String setFileDirectory(String fILE_DIRECTORY) {
		FILE_DIRECTORY = fILE_DIRECTORY;
		return FILE_DIRECTORY;
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
