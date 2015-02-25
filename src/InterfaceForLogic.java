import java.util.ArrayList;
import basicElements.*;
import treeSets.*;

public interface InterfaceForLogic {
	int getSerialNumber();
	TodoSortedList addTask(Task task); //sorted by date
	TodoSortedList editStartDate(Task task, Date start);
	TodoSortedList editDeadLine(Task task, Date deadLine);
	TodoSortedList addDeadLine(Task task, Date deadLine);
	ArrayList<Task> searchByDate(Date searchDate);
	ArrayList<Task> searchByTag(String tag);	//not sure of return type and input type
	ArrayList<Task> searchByPriority(); //has no priority object? how to store priority?
	ArchiveSortedList moveToArchive(Task task); //not sure of the return type
	ArchiveSortedList viewArchiveTasks();
	TodoSortedList deleteTask(Task task);
	TodoSortedList modifyTask(Task task);//not sure of input type
}
