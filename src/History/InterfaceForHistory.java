/**
 * This class is an interface for history. It contains the methods for logic controller.
 * 
 * @author Esmond
 */

package History;

import Common.DATA;

public interface InterfaceForHistory {
	
	/**
	 * @return true if successful log previous DATA into log list
	 */
	boolean log();
	
	/**
	 * @return previous DATA object 
	 */
	DATA undo();
}
