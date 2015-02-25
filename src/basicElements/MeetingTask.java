package basicElements;
import java.util.*;

/**
 * Use to represent one single Task with a start time and end time (like a meeting)
 * @author Yichen
 */
public class MeetingTask extends Task{
	
	protected static final String PRINT_TASK_DETAILS = "Task ID : %1$s \nDescription : \"%2$s\" \n"
														+ "StartTime : %3$s \nEndTime: %4$s \n"
														+ "Priority : %5$s \nTags : %5$s \nArchived : %6$s";
	
	private Date startTime;
	private Date endTime;
	protected static String type = "MeetingTask";
	
	/**
	 * The Constructor for a task
	 * @param id
	 * @param description
	 * @param start
	 * @param end
	 * @param priority
	 * 			-1 if not specified
	 * @param tags
	 * 			null if not specified
	 */
	public MeetingTask(int id, String description, Date start, Date end, int priority, ArrayList<String> tags, boolean archived){
		super(id, description, priority, tags, archived);
		this.startTime = start;
		this.endTime = end;
	}
	
	public MeetingTask(Task task, Date start, Date end){
		super(task.getId(), task.getDescription(), task.getPriority(), task.getTags(), task.isArchived());
		this.startTime = start;
		this.endTime = end;
	}
	
	public Date getStartTime(){
		return this.startTime;
	}
	
	public Date getEndTime(){
		return this.endTime;
	}
	
	/**
	 * For a meeting task, return the start time as the time
	 */
	public Date getTime() {
		return this.getStartTime();
	}
	
	/**
	 * @return the duration of the Task in terms of milliseconds.
	 */
	public long getDuration() {
		return this.endTime.getTime() - this.startTime.getTime();
	}
	
	/**
	 * @return the duration of the Task in terms of minutes
	 */
	public int getDurationInMinutes() {
		return (int)(this.getDuration()/60000);
	}
	
	/**
	 * @return the duration of the Task in terms of hours
	 */
	public int getDurationInHours() {
		return (int)(this.getDuration()/3600000);
	}
	
	/**
	 * Change the start time to the new start time provided
	 * @param newDate
	 */	
	public void changeStartTime(Date newStartTime){
		this.startTime = newStartTime;
	}
	
	/**
	 * Change the start time to the new start time provided
	 * @param newDate
	 */	
	public void changeEndTime(Date newEndTime){
		this.endTime = newEndTime;
	}
	
	public String toString() {
		return String.format(PRINT_TASK_DETAILS, getId(), getDescription(), 
												getStartTime(), getEndTime(),
												getPriority(), getTags(), isArchived());
	}
	
}
