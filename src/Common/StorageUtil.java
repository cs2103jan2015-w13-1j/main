/**
 * @author Esmond
 */

package Common;

public class StorageUtil {
	private String directory;
	private String storageName;

	public StorageUtil() {

	}
	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}
	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	/**
	 * @return the storageName
	 */
	public String getStorageName() {
		return storageName;
	}
	/**
	 * @param storageName the storageName to set
	 */
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	
	@Override
	public String toString() {
		return directory.concat(storageName);
	}
}
