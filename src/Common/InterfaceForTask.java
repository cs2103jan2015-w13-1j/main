package Common;

import java.util.ArrayList;

public interface InterfaceForTask {

	/**
	 * @return the type
	 */
	public String getType();

	/**
	 * @param type the type to set
	 */
	public void setType(String type);

	public int getId();

	public String getDescription();

	public int getPriority();

	public ArrayList<String> getTags();

	public boolean isArchived();

	/**
	 * @return the 'time' of the task
	 */
	public Date getTime();

	/**
	 * Change the original description to the new description provided
	 * @param newDescription
	 */
	public void changeDescription(String newDescription);

	/**
	 * Change the priority to the new priority provided
	 * @param newPriority
	 */
	public void changePriority(int newPriority);

	/**
	 * Add the new tag provided to the task
	 * @param newTag
	 */
	public void addTag(String newTag);

	/**
	 * Remove a tag specified from the tags
	 * @param toBeRemovedTag
	 */
	public void removeTag(String toBeRemovedTag);

	public boolean hasTagExact(String searchTag);

	public boolean containsTag(String searchTag);

	public Date getStartTime();

	public Date getEndTime();

	/**
	 * @return the duration of the Task in terms of milliseconds.
	 */
	public long getDuration();

	/**
	 * @return the duration of the Task in terms of minutes
	 */
	public int getDurationInMinutes();

	/**
	 * @return the duration of the Task in terms of hours
	 */
	public int getDurationInHours();

	/**
	 * Change the start time to the new start time provided
	 * @param newDate
	 */
	public void changeStartTime(Date newStartTime);

	/**
	 * Change the start time to the new start time provided
	 * @param newDate
	 */
	public void changeEndTime(Date newEndTime);

	/**
	 * Add start and end time
	 * @param start
	 * @param end
	 */
	public void addStartAndEndTime(Date start, Date end);

	public Date getDeadline();

	/**
	 * Change the deadline to the new deadline provided
	 * @param newDate
	 */
	public void changeDeadline(Date newDeadline);

	public void addDeadline(Date deadline);

	/**
	 * @return the finishedTime
	 */
	public Date getFinishedTime();

	/**
	 * @param finishedTime the finishedTime to set
	 */
	public void setFinishedTime(Date finishedTime);

	/**
	 * Archive the task
	 */
	public void moveToArchive(Date finishedTime);

	/**
	 * Remove the task from archive, basically means mark this task as unfinished
	 */
	public void removeFromArchive();

	public String toString();

	public int compareTo(Task other);

	/**
	 * @return the recurrenceId
	 */
	public int getRecurrenceId();

	/**
	 * @return whether the task is recurrent
	 */
	public boolean isRecurrence();

	/**
	 * @param recurrenceId the recurrenceId to set
	 */
	public void setRecurrenceId(int recurrenceId);

	/**
	 * Copy a current task to a certain date with the interval period given
	 * @param period the interval between this task to the copied task
	 * @return a new Task object representing the copied task
	 */
	public Task copyWithInterval(int id, long period);

	public boolean equals(Object other);

	public int getOverdueDays();

	public boolean isOnDate(String date);

}