package History;

import Common.DATA;

public interface InterfaceForHistory {
	
	boolean log();
	
	DATA undo();
}
