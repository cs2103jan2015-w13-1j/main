package Common;
import java.text.SimpleDateFormat;

public class Date extends java.util.Date{
	
	public String getDateRepresentation() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(this);
	}

	/**
	 * @param interval a string indicating the interval
	 * @return the number of miliseconds representing the interval
	 */
	public static long getMiliseconds(String interval) {
		switch(interval) {
		case "oneHour":
			return 3600000;
		case "oneDay":
			return 86400000;
		case "oneWeek":
			return 604800000;
		}
		return 0;
	}
	
	public Date() {
		super();
	}
}
