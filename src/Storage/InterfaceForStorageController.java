// @author A0111866E

/**
 * This class is an interface for storage controller
 */

package Storage;

import Common.DATA;

public interface InterfaceForStorageController {
	
	/**
	 * @return a DATA object that is stored in the storage
	 */
	DATA getAllData();
	/**
	 * @param a DATA object
	 * @return a message that indicates the storage status
	 */
	String storeAllData(DATA data); 
}
