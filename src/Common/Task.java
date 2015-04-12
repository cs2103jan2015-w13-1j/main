// @author A0113598X

/**
 * Use to represent one single Task
 */
package Common;
import java.util.*;

public class Task implements Comparable<Task>, InterfaceForTask{
	
	private static final int MILISECONDS_IN_HOUR = 3600000;
	private static final int MILISECONDS_IN_MINUTE = 60000;
	private static final String STRING_DEADLINE = "deadline";
	private static final String STRING_MEETING = "meeting";

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
	 * Constructor for a generic task.
	 * @param id
	 * @param description
	 * @param priority
	 * 			-1 if not specified
	 * @param tags
	 * 			null if not specified
	 * @param recurrenceId
	 */
	public Task(int id, String description, int priority, ArrayList<String> tags, int recurrenceId) {
		super();
		this.id = id;
		this.description = description;
		this.priority = priority;
		this.tags = tags;
		this.recurrenceId = recurrenceId;
	}
	
	/**
	 * Constructor for a meeting task.
	 * @param id
	 * @param description
	 * @param start
	 * @param end
	 * @param priority
	 * @param tags
	 * @param recurrenceId
	 */
	public Task(int id, String description, Date start, Date end, int priority, ArrayList<String> tags, int recurrenceId){
		this(id, description, priority, tags, recurrenceId);
		this.startTime = start;
		this.endTime = end;
		this.setType(STRING_MEETING);
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
		this.setType(STRING_MEETING);
	}
	
	/**
	 * Constructor for a deadline task
	 * @param id
	 * @param description
	 * @param deadline
	 * @param priority
	 * @param tags
	 * @param recurrenceId
	 */
	public Task(int id, String description, Date deadline, int priority, ArrayList<String> tags, int recurrenceId){
		this(id, description, priority, tags, recurrenceId);
		this.deadline = deadline;
		this.setType(STRING_DEADLINE);
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
		this.setType(STRING_DEADLINE);
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getId()
	 */
	@Override
	public int getId(){
		return this.id;
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getDescription()
	 */
	@Override
	public String getDescription(){
		return this.description;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getPriority()
	 */
	@Override
	public int getPriority(){
		return this.priority;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getTags()
	 */
	@Override
	public ArrayList<String> getTags(){
		return this.tags;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#isArchived()
	 */
	@Override
	public boolean isArchived(){
		return this.archived;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getTime()
	 */
	@Override
	public Date getTime() {
		if (this.getType() == STRING_MEETING) {
			return this.getStartTime();
		} else if (this.getType() == STRING_DEADLINE) {
			return this.getDeadline();
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#changeDescription(java.lang.String)
	 */
	@Override
	public void changeDescription(String newDescription){
		this.description = newDescription;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#changePriority(int)
	 */
	@Override
	public void changePriority(int newPriority){
		this.priority = newPriority;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#addTag(java.lang.String)
	 */
	@Override
	public void addTag(String newTag){
		this.tags.add(newTag);
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#removeTag(java.lang.String)
	 */
	@Override
	public void removeTag(String toBeRemovedTag){
		this.tags.remove(toBeRemovedTag);
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#hasTagExact(java.lang.String)
	 */
	@Override
	public boolean hasTagExact(String searchTag) {
		for (String tag : getTags()) {
			if (tag.equals(searchTag)){
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#containsTag(java.lang.String)
	 */
	@Override
	public boolean containsTag(String searchTag) {
		for (String tag : getTags()) {
			if (tag.toLowerCase().contains(searchTag.toLowerCase())){
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getStartTime()
	 */
	@Override
	public Date getStartTime(){
		return this.startTime;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getEndTime()
	 */
	@Override
	public Date getEndTime(){
		return this.endTime;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getDuration()
	 */
	@Override
	public long getDuration() {
		return this.endTime.getTime() - this.startTime.getTime();
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getDurationInMinutes()
	 */
	@Override
	public int getDurationInMinutes() {
		return (int)(this.getDuration() / MILISECONDS_IN_MINUTE);
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getDurationInHours()
	 */
	@Override
	public int getDurationInHours() {
		return (int)(this.getDuration() / MILISECONDS_IN_HOUR);
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#changeStartTime(Common.Date)
	 */	
	@Override
	public void changeStartTime(Date newStartTime){
		this.startTime = newStartTime;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#changeEndTime(Common.Date)
	 */	
	@Override
	public void changeEndTime(Date newEndTime){
		this.endTime = newEndTime;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#addStartAndEndTime(Common.Date, Common.Date)
	 */
	@Override
	public void addStartAndEndTime(Date start, Date end) {
		this.startTime = start;
		this.endTime = end;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getDeadline()
	 */
	@Override
	public Date getDeadline(){
		return this.deadline;
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#changeDeadline(Common.Date)
	 */	
	@Override
	public void changeDeadline(Date newDeadline){
		this.deadline = newDeadline;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#addDeadline(Common.Date)
	 */
	@Override
	public void addDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getFinishedTime()
	 */
	@Override
	public Date getFinishedTime() {
		return finishedTime;
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#setFinishedTime(Common.Date)
	 */
	@Override
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#moveToArchive(Common.Date)
	 */
	@Override
	public void moveToArchive(Date finishedTime){
		this.archived = true;
		this.finishedTime = finishedTime;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#removeFromArchive()
	 */
	@Override
	public void removeFromArchive(){
		this.archived = false;
		this.finishedTime = null;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#toString()
	 */
	@Override
	public String toString() {
		return String.format(PRINT_TASK_DETAILS, getId(), getDescription(),
								getPriority(), getTags(), isArchived(), getType(),
							getDeadline(), getStartTime(), getEndTime(), getFinishedTime());
	}
	
	public static int compareTime(Task t1, Task t2) {
		if (t2.getTime() == null) {
			if (t1.getTime() == null) {
					return 0;
			} else {
				return -1;
			}
		} else if (t1.getTime() == null) {
			return 1;
		} else {
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
			if (dateCompare != 0) {
				return dateCompare;
			} else {
				int priorityCompare = comparePriority(t1, t2);
				if (priorityCompare != 0) {
					return priorityCompare;
				} else {
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
			if (finishCompare != 0) {
				return finishCompare;
			} else {
				int priorityCompare = comparePriority(t1, t2);
				if (priorityCompare != 0) {
					return priorityCompare;
				} else {
					int dateCompare = compareTime(t2, t1);
					if (dateCompare != 0) {
						return dateCompare;
					} else {
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
			if (priorityCompare != 0) {
				return priorityCompare;
			} else {
				int dateCompare = compareTime(t1, t2);
				if (dateCompare != 0) {
					return dateCompare;
				} else {
					return compareId(t1, t2);
				}
			}
		}
	};

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#compareTo(Common.Task)
	 */
	@Override
	public int compareTo(Task other) {
		return this.getId() - other.getId();
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getRecurrenceId()
	 */
	@Override
	public int getRecurrenceId() {
		return recurrenceId;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#isRecurrence()
	 */
	@Override
	public boolean isRecurrence() {
		return recurrenceId>-1;
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#setRecurrenceId(int)
	 */
	@Override
	public void setRecurrenceId(int recurrenceId) {
		this.recurrenceId = recurrenceId;
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#copyWithInterval(int, long)
	 */
	@Override
	public Task copyWithInterval(int id, long period) {
		Task task;
		ArrayList<String> tags = new ArrayList<String>();
		tags.addAll(this.tags);
		if (this.type.equals(STRING_MEETING)) {
			Date startTime = new Date();
			Date endTime = new Date();
			startTime.setTime(this.startTime.getTime() + period);
			endTime.setTime(this.endTime.getTime() + period);
			task = new Task(id, this.description, startTime, endTime, this.priority, tags);
		} else { // the type is a deadline task
			Date deadline = new Date();
			deadline.setTime(this.getDeadline().getTime() + period);
			task = new Task(id, this.description, deadline, this.priority, tags);
		}
		System.out.println(task);
		return task;
	}
	
	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {	
		return this.toString().equals(other.toString());
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#getOverdueDays()
	 */
	@Override
	public int getOverdueDays() {
		if (this.getFinishedTime()==null) {
			Date now = new Date();
			if (this.getType().equals("deadline") && (now.compareTo(this.getDeadline()) > 0)) {
				return Date.getOverdueDays(this.getDeadline());
			}
			if (this.getType().equals("meeting") && (now.compareTo(this.getEndTime()) > 0)) {
				return Date.getOverdueDays(this.getEndTime());
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see Common.InterfaceForTask#isOnDate(java.lang.String)
	 */
	@Override
	public boolean isOnDate(String date) {
		Date dateOfTask = this.getTime();
		if (dateOfTask != null) {
			if (dateOfTask.getDateRepresentation().equals(date)) {
				return true;
			} else {
				// if this is a meeting task
				if (this.getEndTime() != null) {
					// if the start date is the date given, return true
					if (this.getEndTime().getDateRepresentation().equals(date)) {
						return true;
					}
					// if the date given is between the start and end, return true
					if (this.getStartTime().getDateRepresentation().compareTo(date)<0 &&
						this.getEndTime().getDateRepresentation().compareTo(date)>0) {
						return true;
					}
				}
			}
		}
		return false;
	}
}