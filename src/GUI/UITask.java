/**
 * @author Kangsoon
 */
package GUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UITask {
	private final SimpleIntegerProperty id;
	private final SimpleStringProperty description;
	private final SimpleStringProperty priority;
	private final SimpleStringProperty start;
	private final SimpleStringProperty end;
	private final SimpleStringProperty due;
	private SimpleStringProperty tags;
	
	/**
	 * @param id
	 * @param desc
	 * @param pri
	 * @param start
	 * @param end
	 * @param due
	 * @param tags
	 */
	public UITask(int id, String desc, String pri, String start, String end, String due, String tags){
		this.id = new SimpleIntegerProperty(id);
		this.description = new SimpleStringProperty(desc);
		this.priority = new SimpleStringProperty(pri);
		this.start = new SimpleStringProperty(start);
		this.end = new SimpleStringProperty(end);
		this.due = new SimpleStringProperty(due);
		this.tags = new SimpleStringProperty(tags);
	}
	
	/**
	 * @return Tags
	 */
	public String getTags() {
		return tags.get();
	}
	
	/**
	 * @return id
	 */
	public Integer getId() {
		return id.get();
	}
	
	/**
	 * @return description
	 */
	public String getDescription() {
		return description.get();
	}
	
	/**
	 * @return priority
	 */
	public String getPriority() {
		return priority.get();
	}
	
	/**
	 * @return start
	 */
	public String getStart() {
		return start.get();
	}
	
	/**
	 * @return end
	 */
	public String getEnd() {
		return end.get();
	}
	
	/**
	 * @return due
	 */
	public String getDue() {
		return due.get();
	}
	
	/**
	 * @param tags
	 */
	public void setTags(String tags) {
		this.tags.set(tags);
	}
	
	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id.set(id);
	}
	
	/**
	 * @param desc
	 */
	public void setDesc(String desc) {
		this.description.set(desc);
	}
	
	/**
	 * @param pri
	 */
	public void setPri(String pri) {
		this.priority.set(pri);
	}
	
	/**
	 * @param start
	 */
	public void setStart(String start){
		this.start.set(start);
	}
	
	/**
	 * @param end
	 */
	public void setEnd(String end){
		this.end.set(end);
	}
	
	/**
	 * @param due
	 */
	public void setDue(String due){
		this.due.set(due);
	}
}