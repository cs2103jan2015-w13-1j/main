package basicElements;
import java.util.*;

/**
 * Use to represent one single Task with a specific deadline
 * @author Yichen
 */
public class DeadlineTask extends Task{
	
	protected static final String PRINT_TASK_DETAILS = "Task ID : %1$s \nDescription : \"%2$s\" \nDeadline : %3$s \n"
														+ "Priority : %4$s \nTags : %5$s \nArchived : %6$s";
	
	private Date deadline;
	protected static String type = "DeadlineTask";

	
	/**
	 * The Constructor for a task
	 * @param id
	 * @param description
	 * @param deadline
	 * @param priority
	 * 			-1 if not specified
	 * @param tags
	 * 			null if not specified
	 */
	public DeadlineTask(int id, String description, Date deadline, int priority, ArrayList<String> tags){
		super(id, description, priority, tags);
		this.deadline = deadline;
	}
	
	public DeadlineTask(Task task, Date deadline) {
		super(task.getId(), task.getDescription(), task.getPriority(), task.getTags());
		this.deadline = deadline;
	}
	
	public Date getDeadline(){
		return this.deadline;
	}
	
	/**
	 * For a deadline task, return the deadline as the time
	 */
	public Date getTime() {
		return this.getDeadline();
	}
	
	/**
	 * Change the deadline to the new deadline provided
	 * @param newDate
	 */	
	public void changeDeadline(Date newDeadline){
		this.deadline = newDeadline;
	}
	
	public String toString() {
		return String.format(PRINT_TASK_DETAILS, getId(), getDescription(), getDeadline(),
												getPriority(), getTags(), isArchived());
	}
	
}
