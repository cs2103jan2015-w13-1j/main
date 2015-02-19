package basicElements;
import java.text.SimpleDateFormat;

public class Date extends java.util.Date{
	
	public String getDateRepresentation() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(this);
	}
}
