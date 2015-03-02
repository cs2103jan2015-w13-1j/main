import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FXTable {
	private final SimpleIntegerProperty id;
	private final SimpleStringProperty description;
	private final SimpleStringProperty priority;
	private final SimpleStringProperty start;
	private final SimpleStringProperty end;
	private final SimpleStringProperty due;
	
	public FXTable(int id, String desc, String pri, String start, String end, String due){
		this.id = new SimpleIntegerProperty(id);
		this.description = new SimpleStringProperty(desc);
		this.priority = new SimpleStringProperty(pri);
		this.start = new SimpleStringProperty(start);
		this.end = new SimpleStringProperty(end);
		this.due = new SimpleStringProperty(due);
	}
	
	public Integer getId(){
		return id.get();
	}
	
	public String getDescription(){
		return description.get();
	}
	
	public String getPriority(){
		return priority.get();
	}
	
	public String getStart(){
		return start.get();
	}
	
	public String getEnd(){
		return end.get();
	}
	
	public String getDue(){
		return due.get();
	}
	
	public void setId(int id){

		this.id.set(id);
	}
	
	public void setDesc(String desc){
		this.description.set(desc);
	}
	
	public void setPri(String pri){
		this.priority.set(pri);
	}
	
	public void setStart(String start){
		this.start.set(start);
	}
	
	public void setEnd(String end){
		this.end.set(end);
	}
	
	public void setDue(String due){
		this.due.set(due);
	}
}