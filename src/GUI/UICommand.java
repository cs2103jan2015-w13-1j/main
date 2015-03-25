package GUI;

import javafx.beans.property.SimpleStringProperty;

public class UICommand {
	private final SimpleStringProperty function;
	private final SimpleStringProperty command;
	
	public UICommand(String function, String command){
		this.function = new SimpleStringProperty(function);
		this.command = new SimpleStringProperty(command);
	}
	
	
	public String getFunction() {
		return function.get();
	}

	public String getCommand() {
		return command.get();
	}
	
	public void setFunction(String function){
		this.function.set(function);
	}
	
	public void setCommand(String command){
		this.command.set(command);
	}
}
