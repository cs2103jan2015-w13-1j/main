import java.io.File;
import java.io.IOException;

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
	
	public static void main (String[] args) throws IOException {
		// initialize all 5 tables for first time startup
		initializeStorage();
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
