//@author A0110837L

package GUI;

import javafx.beans.property.SimpleStringProperty;

public class UICommand {
	private final SimpleStringProperty function;
	private final SimpleStringProperty command;
	private final SimpleStringProperty shortcuts;
	
	/**
	 * @param function
	 * @param command
	 * @param shortcuts
	 */
	public UICommand(String function, String command, String shortcuts){
		this.function = new SimpleStringProperty(function);
		this.command = new SimpleStringProperty(command);
		this.shortcuts = new SimpleStringProperty(shortcuts);
	}
	
	
	/**
	 * @return function
	 */
	public String getFunction() {
		return function.get();
	}

	/**
	 * @return command
	 */
	public String getCommand() {
		return command.get();
	}
	
	/**
	 * @return shortcuts
	 */
	public String getShortcuts() {
		return shortcuts.get();
	}
	
	/**
	 * @param function
	 */
	public void setFunction(String function){
		this.function.set(function);
	}
	
	/**
	 * @param command
	 */
	public void setCommand(String command){
		this.command.set(command);
	}
	
	/**
	 * @param shortcuts
	 */
	public void setShortcuts(String shortcuts){
		this.shortcuts.set(shortcuts);
	}
}
