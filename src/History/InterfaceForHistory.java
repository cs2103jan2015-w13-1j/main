// @author A0111866E

/**
 * This class is an interface for history. It contains the methods for logic controller.
 */

package History;

import Logic.DATA;

public interface InterfaceForHistory {
	
	/**
	 * @return true if successful log previous DATA into log list
	 */
	boolean log();
	
	/**
	 * @return previous DATA object if logList is not empty. Otherwise, return null
	 */
	DATA undo();
	
	/**
	 * @return undo DATA object if redoList is not empty. Otherwise, return null
	 */
	DATA redo();
}
