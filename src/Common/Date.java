package Common;
import java.text.SimpleDateFormat;

public class Date extends java.util.Date{
	
	public String getDateRepresentation() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(this);
	}
	
	public Date(String interval) {
		super();
		switch(interval) {
		case "oneHour":
			this.setTime(3600000);
			break;
		case "oneDay":
			this.setTime(86400000);
			break;
		case "oneWeek":
			this.setTime(604800000);
			break;
		}
	}
	
	public Date() {
		super();
	}
}
