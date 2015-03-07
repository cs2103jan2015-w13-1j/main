package Storage;

import Common.DATA;


public interface InterfaceForStorage {
	
	// for logic controller to retrieve DATA from storage
	DATA getAllData();
	// for logic controller to store DATA into storage
	String storeAllData(DATA data); 
	// for parser to insert relative path of directory for storage
	String insertFileDirectory(String directory);
	// for parser to retrieve current relative path of storage directory
	String getFileDirectory();
}
