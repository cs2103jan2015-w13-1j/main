package Storage;

import Common.DATA;


public interface InterfaceForStorage {
	
	DATA getAllData();
	
	String storeAllData(DATA data); 
	
}
