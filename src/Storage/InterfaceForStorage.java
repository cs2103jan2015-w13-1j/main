/**
 * This class is an interface for storage. It contains the methods for logic controller and parser
 * 
 * @author Esmond
 */

package Storage;

import Common.DATA;


public interface InterfaceForStorage {
	
	/**
	 * @return a DATA object that is stored in the storage
	 */
	DATA getAllData();
	/**
	 * @param a DATA object
	 * @return a message that indicates the storage status
	 */
	String storeAllData(DATA data); 
	/**
	 * @param relative path of the directory
	 * @return the given relative path of the directory
	 */
	String setFileDirectory(String directory);
	/**
	 * @return the current relative path of the directory
	 */
	String getFileDirectory();
	
	/**
	 * @return a random motivation quotes
	 */
	String getMotivationQuotes();
}
