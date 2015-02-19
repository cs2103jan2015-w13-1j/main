package basicElements;


/**
 * Used to store the id's of tasks on a particular date.
 * Mainly for instant searching purpose
 * @author Yichen
 */

public class TaskByDate extends TaskByProperty {
	private String date;
	/**
	 * Construct an new instance by specifying a date
	 * @param date
	 */
	public TaskByDate(String date){
		this.date = date;
	}
	
	public String getDate(){
		return this.date;
	}
}
