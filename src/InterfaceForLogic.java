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
	
	ArrayList<Task> searchByDate(Date searchDate);
	ArrayList<Task> searchByTag(String tag);	//not sure of return type and input type
	ArrayList<Task> searchByPriority(int priority);


	ToDoSortedList moveToArchive(Task task); //not sure of the return type
	ArchiveSortedList viewArchiveTasks();
	
	ToDoSortedList modifyTask(Task task);//not sure of input type
	
	ToDoSortedList deleteTask(Task task);
}
