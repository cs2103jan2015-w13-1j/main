/**
 * @author Yichen
 *
 */

package Common;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressWarnings("serial")
public class Date extends java.util.Date{

/***********************************************
				Constructors
***********************************************/
	public Date() {
		super();
	}
	
	public Date(long time) {
		this();
		this.setTime(time);
	}
	
/***********************************************
				Private methods
***********************************************/
	
	/**
	 * Set the hour, minute, second of a given date to certain values
	 * @param cal
	 * @param hour
	 * @param minute
	 * @param second
	 * @param milisecond
	 * @return a date object with specified time values
	 */
	private static Date setTime(Calendar cal, int hour, int minute, int second, int milisecond) {
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, milisecond);
        Date date = new Date(cal.getTime().getTime());
        return date;
	}

	/**
	 * @param cal
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return a Date object representing the time specified by the inputs
	 */
	private static Date getDayTime(Calendar cal, int month, int day, int hour, int minute ) {
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        if (cal.compareTo(new GregorianCalendar())<0) {
        	cal.add(Calendar.YEAR, 1);
        }
        return setTime(cal, hour, minute, 59, 0);
	}
	
	/**
	 * @param cal
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return a Date object representing the time specified by the inputs
	 */
	private static Date getDayTime(Calendar cal, int year, int month, int day, int hour, int minute ) {
        cal.set(Calendar.YEAR, year);
        return getDayTime(cal, month, day, hour, minute);
	}

	
/***********************************************
				Public methods
***********************************************/	
	
	public String getDateRepresentation() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(this);
	}
	
	/**
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return a Date object representing the time specified by the inputs
	 */
	public static Date getDayTime(int month, int day, int hour, int minute ) {
		Calendar cal = new GregorianCalendar();
        return getDayTime(cal, month, day, hour, minute);
	}
	
	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return a Date object representing the time specified by the inputs
	 */
	public static Date getDayTime(int year, int month, int day, int hour, int minute ) {
		Calendar cal = new GregorianCalendar();
        return getDayTime(cal, year, month, day, hour, minute);
	}
	
	/**
	 * Get the yyMMdd 2359 of the specified date
	 * @param year
	 * @param month
	 * @param day
	 * @return the Date object
	 */
	public static Date getDay2359(int year, int month, int day) {
		return getDayTime(year, month, day, 23, 59);
	}
	
	/**
	 * Get the MMdd 2359 of the specified date
	 * @param month
	 * @param day
	 * @return the Date object
	 */
	public static Date getDay2359(int month, int day) {
		return getDayTime(month, day, 23, 59);
	}
	
	/**
	 * @return a date object represents 23:59 of the current day
	 */
	public static Date today2359() {
		Calendar cal = new GregorianCalendar();
		return setTime(cal, 23, 59, 59, 0);
	}
	
	/**
	 * @return a date object represents 23:59 of the next day
	 */
	public static Date tmr2359() {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, 1);
		return setTime(cal, 23, 59, 59, 0);
	}
	
	/**
	 * @param hour
	 * @param minute
	 * @return a date object represents HH:mm of the current day
	 */
	public static Date todayTime(int hour, int minute) {
		Calendar cal = new GregorianCalendar();
		return setTime(cal, hour, minute, 0, 0);
	}
	
	/**
	 * @param hour
	 * @param minute
	 * @return a date object represents HH:mm of the next day
	 */
	public static Date tmrTime(int hour, int minute) {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, 1);
		return setTime(cal, hour, minute, 0, 0);
	}
	
	/**
	 * Change the HH:mm of a date object to the hour and minute specified,
	 * without changing the yyMMdd
	 * @param hour
	 * @param minute
	 * @return the new Date object
	 */
	public Date changeTime(int hour, int minute) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(this);
		Date changedDate = setTime(cal, hour, minute, 0, 0);
		return changedDate;
	}
	
	/**
	 * @param interval a string indicating the interval
	 * @return the number of miliseconds representing the interval
	 */
	public static long getMiliseconds(String interval) {
		switch(interval) {
			case "oneHour" :
				return 3600000;
			case "oneDay" :
				return 86400000;
			case "oneWeek" :
				return 604800000;
		}
		return 0;
	}
	
	/**
	 * @param deadline the deadline or endtime of a task 
	 * @return number of days overdue
	 */
	public static int getOverdueDays(Date deadline) {
		Calendar cal = new GregorianCalendar();
		Date today = setTime(cal, 0, 0, 0, 0);
		long deadlineTime = deadline.getTime();
		long todayTime = today.getTime();
		int numOfDays = (int) ((float)(todayTime - deadlineTime)/getMiliseconds("oneDay") + 1);
		return numOfDays;
	}
}
