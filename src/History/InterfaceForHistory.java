package History;

import Common.DATA;

public interface InterfaceForHistory {
	
	void saveState(DATA data);
	
	DATA retriveState();
}
