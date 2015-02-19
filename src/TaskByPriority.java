
/**
 * Used to store the id's of tasks of a particular priority
 * Mainly for instant searching purpose
 * @author Yichen
 */

public class TaskByPriority extends TaskByProperty {
	private int priority;
	
	/**
	 * Construct by specifying the priority
	 * @param priority
	 */
	public TaskByPriority(int priority){
		this.priority = priority;
	}
	
	public int getProirity(){
		return this.priority;
	}
}
