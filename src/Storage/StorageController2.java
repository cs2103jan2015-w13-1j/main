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

import Common.DATA;

public class StorageController2 {
	private DATA data;
	private static String FILE_NAME = "storage.json";
	private static String FILE_DIRECTORY = "";
	
	public static void main(String[] args) {
		StorageController2 control = new StorageController2();
		control.run();
	}
	
	public void run() {
		insertFileDirectory("tables/");
		initialiseStorage();
		createDummyData();
	}

	public static String createDummyData() {
		return "Dummy data created";
	}
	
	public static String initialiseStorage() {
		processStorage();
		if (isStorageExist(getFileRelativePath()) == true) {
			return "Storage initialised";
		} else {
			return "Storage unable to initialise";
		}
		
	}
	
	private static void processStorage() {
		if (isStorageExist(getFileRelativePath()) == false) {
			createStorage(getFileRelativePath());
		}
	}
	
	private static void createStorage(String fileRelativePath) {
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

	private static boolean isStorageExist(String fileRelativePath) {
		File file = new File(fileRelativePath);
		return file.exists();
	}

	private static String getFileRelativePath() {
		return FILE_DIRECTORY.concat(FILE_NAME);
	}

	public static String insertFileDirectory(String directory) {
		FILE_DIRECTORY = directory;
		return FILE_DIRECTORY;
	}

	public DATA getData() {
		return data;
	}

	public void setData(DATA data) {
		this.data = data;
	}
}
