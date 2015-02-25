import basicElements.*;
import hashMaps.*;
import treeSets.*;

public class LogicController implements InterfaceForLogic{
	
	private DatabaseController DC = new DatabaseController();
	private DATA data;
	public DATA initialise() {
		return DC.getAllData();
	}
	
	public String addTask(Task task) {
		return null;
	}
	
	public String exitProgram() {
		return DC.storeAllData(data);
	}

}
