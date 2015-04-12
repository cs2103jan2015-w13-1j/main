// @author A0111866E

/**
 * This class is an interface for storage directory
 */

package Storage;

public interface InterfaceForStorageDirectory {
	/**
	 * @param directory path of the directory
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
