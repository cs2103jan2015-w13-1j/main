import java.util.*;

/**
 * Used to store the id's of tasks on a particular date.
 * Mainly for instant searching purpose
 * @author Yichen
 */

public class TaskByDate extends TaskByProperty {
	private Date date;
	
	/**
	 * Construct an new instance by specifying a date
	 * @param date
	 */
	public TaskByDate(Date date){
		this.date = date;
	}
	
	public Date getDate(){
		return this.date;
	}
}
