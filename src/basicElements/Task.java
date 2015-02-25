package basicElements;
import java.util.*;

/**
 * Use to represent one single Task
 * @author Yichen
 */
public class Task {
	
	protected static final String PRINT_TASK_DETAILS = "Task ID : %1$s \nDescription : \"%2$s\" \n"
														+ "Priority : %3$s \nTags : %4$s \nArchived : %6$s";
	
	protected static String type = "GenericTask";
	private int id;
	private String description;
	private int priority = -1;
	private ArrayList<String> tags = new ArrayList<String>();
	private boolean archived = false;
	
	/**
	 * The Constructor for a task
	 * @param id
	 * @param description
	 * @param priority
	 * 			-1 if not specified
	 * @param tags
	 * 			null if not specified
	 */
	public Task(int id, String description, int priority, ArrayList<String> tags, boolean archived){
		this.id = id;
		this.description = description;
		if (priority > 0){
			this.priority = priority;
		}
		if (tags != null){
			this.tags = tags;
		}
		this.archived = archived;
	}
	
	// Default constructor
	public Task() {
		
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public int getPriority(){
		return this.priority;
	}
	
	public ArrayList<String> getTags(){
		return this.tags;
	}
	
	public boolean isArchived(){
		return this.archived;
	}
	
	/**
	 * @return null as time is not specified for a generic task
	 */
	public Date getTime() {
		return null;
	}
	
	/**
	 * Change the original description to the new description provided
	 * @param newDescription
	 */
	public void changeDescription(String newDescription){
		this.description = newDescription;
	}
	
	/**
	 * Change the priority to the new priority provided
	 * @param newPriority
	 */
	public void changePriority(int newPriority){
		this.priority = newPriority;
	}
	
	/**
	 * Add the new tag provided to the task
	 * @param newTag
	 */
	public void addTag(String newTag){
		this.tags.add(newTag);
	}
	
	/**
	 * Remove a tag specified from the tags
	 * @param toBeRemovedTag
	 */
	public void removeTag(String toBeRemovedTag){
		this.tags.remove(toBeRemovedTag);
	}
	
	/**
	 * Archive the task
	 */
	public void moveToArchive(){
		this.archived = true;
	}
	
	/**
	 * Remove the task from archive, basically means mark this task as unfinished
	 */
	public void removeFromArchive(){
		this.archived = false;
	}
	
	public String toString() {
		return String.format(PRINT_TASK_DETAILS, getId(), getDescription(),
												getPriority(), getTags(), isArchived());
	}

}