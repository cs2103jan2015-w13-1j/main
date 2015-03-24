package Common;
import java.util.*;

/**
 * Use to represent one single Task
 * @author Yichen
 */
public class Task implements Comparable<Task>{
	
	/*
	 * Might need to be edited here
	 */
	protected static final String PRINT_TASK_DETAILS = "Task ID : %1$s \nDescription : \"%2$s\" \n"
								+ "Priority : %3$s \nTags : %4$s \nArchived : %5$s \nType : %6$s\n"
								+ "Deadline : %7$s \nStartTime : %8$s \n"
								+ "EndTime : %9$s \nfinishedTime : %10$s\n";
	
	private String type = "generic";
	private int id;
	private String description;
	private int priority = -1;
	private ArrayList<String> tags = new ArrayList<String>();
	private boolean archived = false;
	private Date startTime = null;
	private Date endTime = null;
	private Date deadline = null;
	private Date finishedTime = null;
	private int recurrenceId = -1;
	
	/**
	 * The default Constructor for a task
	 * @param id
	 * @param description
	 * @param priority
	 * 			-1 if not specified
	 * @param tags
	 * 			null if not specified
	 */
	public Task(int id, String description, int priority, ArrayList<String> tags){
		this.id = id;
		this.description = description;
		if (priority > 0){
			this.priority = priority;
		}
		if (tags != null){
			this.tags = tags;
		}
	}
	
	/**
	 * Constructor for a meeting task.
	 * @param id
	 * @param description
	 * @param start
	 * @param end
	 * @param priority
	 * @param tags
	 */
	public Task(int id, String description, Date start, Date end, int priority, ArrayList<String> tags){
		this(id, description, priority, tags);
		this.startTime = start;
		this.endTime = end;
		this.setType("meeting");
	}
	
	/**
	 * Constructor for a deadline task
	 * @param id
	 * @param description
	 * @param deadline
	 * @param priority
	 * @param tags
	 */
	public Task(int id, String description, Date deadline, int priority, ArrayList<String> tags){
		this(id, description, priority, tags);
		this.deadline = deadline;
		this.setType("deadline");
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public int getId(){
		return this.id;
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
	 * @return the 'time' of the task
	 */
	public Date getTime() {
		if (this.getType() == "meeting") {
			return this.getStartTime();
		}
		else if (this.getType() == "deadline") {
			return this.getDeadline();
		}
		else {
			return null;
		}
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
	
	public Date getStartTime(){
		return this.startTime;
	}
	
	public Date getEndTime(){
		return this.endTime;
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
	
	/**
	 * Add start and end time
	 * @param start
	 * @param end
	 */
	public void addStartAndEndTime(Date start, Date end) {
		this.startTime = start;
		this.endTime = end;
	}
	
	public Date getDeadline(){
		return this.deadline;
	}

	/**
	 * Change the deadline to the new deadline provided
	 * @param newDate
	 */	
	public void changeDeadline(Date newDeadline){
		this.deadline = newDeadline;
	}
	
	public void addDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	/**
	 * @return the finishedTime
	 */
	public Date getFinishedTime() {
		return finishedTime;
	}

	/**
	 * @param finishedTime the finishedTime to set
	 */
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
	
	/**
	 * Archive the task
	 */
	public void moveToArchive(Date finishedTime){
		this.archived = true;
		this.finishedTime = finishedTime;
	}
	
	/**
	 * Remove the task from archive, basically means mark this task as unfinished
	 */
	public void removeFromArchive(){
		this.archived = false;
		this.finishedTime = null;
	}
	
	public String toString() {
		return String.format(PRINT_TASK_DETAILS, getId(), getDescription(),
								getPriority(), getTags(), isArchived(), getType(),
							getDeadline(), getStartTime(), getEndTime(), getFinishedTime());
	}
	
	public static int compareTime(Task t1, Task t2) {
		if (t2.getTime() == null) {
			if (t1.getTime() == null) {
					return 0;
			}
			else {
				return -1;
			}
		}
		else if (t1.getTime() == null) {
			return 1;
		}
		else {
			return t1.getTime().compareTo(t2.getTime());
		}
	}
	
	public static int comparePriority(Task t1, Task t2) {
		return t2.getPriority()-t1.getPriority();
	}
	
	public static int compareFinishTime(Task t1, Task t2) {
		return t2.getFinishedTime().compareTo(t1.getFinishedTime());
	}
	
	public static int compareId(Task t1, Task t2) {
		return t1.getId() - t2.getId();
	}
	
	/**
	 * Comparator for the todo list sorted by deadline or starting time, then priority
	 */
	public static Comparator<Task> dateThenPriority = new Comparator<Task>(){
		public int compare(Task t1, Task t2) {
			int dateCompare = compareTime(t1, t2);
			if(dateCompare != 0) {
				return dateCompare;
			}
			else {
				int priorityCompare = comparePriority(t1, t2);
				if (priorityCompare != 0) {
					return priorityCompare;
				}
				else {
					return compareId(t1, t2);
				}
			}
		}
	};
	
	/**
	 * Comparator for the archive list sorted by finished time, then priority
	 */
	public static Comparator<Task> reverseDateThenPriority = new Comparator<Task>(){
		public int compare(Task t1, Task t2) {
			int finishCompare = compareFinishTime(t1, t2);
			if(finishCompare != 0) {
				return finishCompare;
			}
			else {
				int priorityCompare = comparePriority(t1, t2);
				if (priorityCompare != 0) {
					return priorityCompare;
				}
				else {
					int dateCompare = compareTime(t2, t1);
					if(dateCompare != 0) {
						return dateCompare;
					}
					else {
						return compareId(t2, t1);
					}
				}
			}
		}
	};
	
	/**
	 * Comparator for the todo list sorted by priority, then date
	 */
	public static Comparator<Task> priorityThenDate = new Comparator<Task>(){
		public int compare(Task t1, Task t2) {
			int priorityCompare = comparePriority(t1, t2);
			if(priorityCompare != 0) {
				return priorityCompare;
			}
			else {
				int dateCompare = compareTime(t1, t2);
				if(dateCompare != 0) {
					return dateCompare;
				}
				else {
					return compareId(t1, t2);
				}
			}
		}
	};

	@Override
	public int compareTo(Task other) {
		return this.getId() - other.getId();
	}

	/**
	 * @return the recurrenceId
	 */
	public int getRecurrenceId() {
		return recurrenceId;
	}
	
	/**
	 * @return whether the task is recurrent
	 */
	public boolean isRecurrence() {
		return recurrenceId>-1;
	}

	/**
	 * @param recurrenceId the recurrenceId to set
	 */
	public void setRecurrenceId(int recurrenceId) {
		this.recurrenceId = recurrenceId;
	}

	/**
	 * Copy a current task to a certain date with the interval period given
	 * @param period the interval between this task to the copied task
	 * @return a new Task object representing the copied task
	 */
	public Task copyWithInterval(int id, long period) {
		Task task;
		if (this.type.equals("meeting")) {
			Date startTime = new Date();
			Date endTime = new Date();
			startTime.setTime(this.startTime.getTime() + period);
			endTime.setTime(this.endTime.getTime() + period);
			task = new Task(id, this.description, startTime, endTime, this.priority, this.tags);
		}
		else { // the type is a deadline task
			Date deadline = new Date();
			deadline.setTime(this.getDeadline().getTime()+period);
			task = new Task(id, this.description, deadline, this.priority, this.tags);
		}
		return task;
	}
	
	@Override
	public boolean equals(Object other) {	
		return this.toString().equals(other.toString());
	}
//	
//	@Override
//	public int hashCode() {
//		return this.getId();
//	}
}