import java.util.ArrayList;
import basicElements.*;
import treeSets.*;

public interface InterfaceForLogic {

	int getSerialNumber();
	ToDoSortedList addTask(Task task); //sorted by date
	ToDoSortedList editStartDate(Task task, Date start);
	ToDoSortedList editDeadLine(Task task, Date deadLine);
	ToDoSortedList addDeadLine(Task task, Date deadLine);
	ArrayList<Task> searchByDate(Date searchDate);
	ArrayList<Task> searchByTag(String tag);	//not sure of return type and input type
	ArrayList<Task> searchByPriority(); //has no priority object? how to store priority?



	ArchiveSortedList moveToArchive(Task task); //not sure of the return type
	ArchiveSortedList viewArchiveTasks();
	ToDoSortedList deleteTask(Task task);
	ToDoSortedList modifyTask(Task task);//not sure of input type
}
