import java.util.ArrayList;
import basicElements.*;
import treeSets.*;

public interface InterfaceForLogic {

	int getSerialNumber();
	
	ToDoSortedList addTask(Task task); //sorted by date
	
	ToDoSortedList addDeadLine(Task task, Date deadLine);
	ToDoSortedList addStartAndEndTime(Task task, Date start,Date end);
	
	ToDoSortedList editStartTime(MeetingTask task, Date start);
	ToDoSortedList editEndTime(MeetingTask task, Date end);
	ToDoSortedList editDeadline(DeadlineTask task, Date deadLine);
	
	ToDoSortedList editDescription(Task task, String newDescription);
	ToDoSortedList editPriority(Task task, int priority);
	ToDoSortedList addTag(Task task, String tag);
	ToDoSortedList removeTag(Task task, String tag);
	
	ArrayList<Task> searchByDate(Date searchDate);
	ArrayList<Task> searchByTag(String tag);	//not sure of return type and input type
	ArrayList<Task> searchByPriority(int priority);

	ToDoSortedList moveToArchive(Task task); //not sure of the return type
	ArchiveSortedList viewArchiveTasks();
	
	ToDoSortedList deleteTask(Task task);
	
}
