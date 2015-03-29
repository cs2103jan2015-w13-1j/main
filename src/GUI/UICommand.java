package GUI;

import javafx.beans.property.SimpleStringProperty;

public class UICommand {
	private final SimpleStringProperty function;
	private final SimpleStringProperty command;
	private final SimpleStringProperty shortcuts;
	
	public UICommand(String function, String command, String shortcuts){
		this.function = new SimpleStringProperty(function);
		this.command = new SimpleStringProperty(command);
		this.shortcuts = new SimpleStringProperty(shortcuts);
	}
	
	
	public String getFunction() {
		return function.get();
	}

	public String getCommand() {
		return command.get();
	}
	
	public String getShortcuts() {
		return shortcuts.get();
	}
	
	public void setFunction(String function){
		this.function.set(function);
	}
	
	public void setCommand(String command){
		this.command.set(command);
	}
	
	public void setShortcuts(String shortcuts){
		this.shortcuts.set(shortcuts);
	}
}
