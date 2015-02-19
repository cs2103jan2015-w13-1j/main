import java.util.*;

/**
 * Use to represent one single Task
 * @author Yichen
 */
public class Task {
	
	private static final String PRINT_TASK_DETAILS = "Task ID : %1$s \nDescription : \"%2$s\" \nDate : %3$s \nPriority : %4$s \nTags : %5$s \nArchived : %6$s";
	
	private int id;
	private String description;
	private Date deadline;
	private int priority = -1;
	private ArrayList<String> tags = new ArrayList<String>();
	private boolean archived = false;
	
	/**
	 * The Constructor for a task
	 * @param id
	 * @param description
	 * @param date
	 * 			null if not specified
	 * @param priority
	 * 			-1 if not specified
	 * @param tags
	 * 			null if not specified
	 */
	public Task(int id, String description, Date date, int priority, ArrayList<String> tags){
		this.id = id;
		this.description = description;
		this.deadline = date;
		if (priority > 0){
			this.priority = priority;
		}
		if (tags != null){
			this.tags = tags;
		}
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public Date getDeadline(){
		return this.deadline;
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
	 * Change the original description to the new description provided
	 * @param newDescription
	 */
	public void changeDescription(String newDescription){
		this.description = newDescription;
	}
	
	/**
	 * Change the date to the new date provided
	 * @param newDate
	 */	
	public void changeDeadline(Date newDate){
		this.deadline = newDate;
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
		return String.format(PRINT_TASK_DETAILS, getId(), getDescription(), getDeadline(), getPriority(), getTags(), isArchived());
	}
}
