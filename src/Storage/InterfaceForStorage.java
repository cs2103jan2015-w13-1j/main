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
	 * @param path of the directory
	 * @return the given path of the directory
	 */
	String changeFileDirectory(String directory);
	/**
	 * @return the current relative path of the directory
	 */
	String getFileDirectory();
	/**
	 * @return the current relative path of the directory
	 */
	String getFileName();
	/**
	 * @param file
	 * @return true if import is successful
	 */
	boolean importFromDirectory(String file);
	/**
	 * @param file
	 * @return true if export is successful
	 */
	boolean exportToDirectory(String file);
}
