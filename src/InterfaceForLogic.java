import java.util.ArrayList;
import basicElements.*;
import treeSets.*;

public interface InterfaceForLogic {

	int getSerialNumber();
	
	ToDoSortedList addTask(Task task); //sorted by date
	
	ToDoSortedList addDeadLine(Task task, Date deadLine);
	ToDoSortedList addStartTime(Task task, Date start);
	ToDoSortedList addEndTime(Task task, Date start);
	
	ToDoSortedList editStartDate(MeetingTask task, Date start);
	ToDoSortedList editEndDate(MeetingTask task, Date end);
	ToDoSortedList editDeadLine(DeadlineTask task, Date deadLine);
	
	ArrayList<Task> searchByDate(Date searchDate);
	ArrayList<Task> searchByTag(String tag);	//not sure of return type and input type
	ArrayList<Task> searchByPriority(int priority); //has no priority object? how to store priority?


	ArchiveSortedList moveToArchive(Task task); //not sure of the return type
	ArchiveSortedList viewArchiveTasks();
	
	ToDoSortedList modifyTask(Task task);//not sure of input type
	
	ToDoSortedList deleteTask(Task task);
}
