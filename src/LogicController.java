import basicElements.*;
import hashMaps.*;
import treeSets.*;

public class LogicController implements InterfaceForLogic{
	
	DatabaseController DC = new DatabaseController();
	
	public DATA initialise() {
		return DC.getAllData();
	}
	
	public String addTask(Task task) {
		return null;
	}
	
	public String exitProgram() {
		return DC.storeAllData();
	}

}
