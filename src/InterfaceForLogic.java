import java.util.ArrayList;
import basicElements.*;
import treeSets.*;

public interface InterfaceForLogic {
<<<<<<< HEAD
	int getSerialNumber();
	TodoSortedList addTask(Task task); //sorted by date
	TodoSortedList editStartDate(Task task, Date start);
	TodoSortedList editDeadLine(Task task, Date deadLine);
	TodoSortedList addDeadLine(Task task, Date deadLine);
	ArrayList<Task> searchByDate(Date searchDate);
	ArrayList<Task> searchByTag(String tag);	//not sure of return type and input type
	ArrayList<Task> searchByPriority(); //has no priority object? how to store priority?
=======
	ToDoSortedList addTask(Task task); //sorted by date
	ToDoSortedList editStartDate(Task task, Date start);
	ToDoSortedList editDeadLine(Task task, Date deadLine);
	ToDoSortedList addDeadLine(Task task, Date deadLine);
	GeneralSortedList searchByDate(Date searchDate);
	TaskByTag searchByTag(String tag);	//not sure of return type and input type
	PrioritySortedList searchByPriority(); //has no priority object? how to store priority?
>>>>>>> c6517ad09beb1179d61d65e90408dfb21f3c3556
	ArchiveSortedList moveToArchive(Task task); //not sure of the return type
	ArchiveSortedList viewArchiveTasks();
	ToDoSortedList deleteTask(Task task);
	ToDoSortedList modifyTask(Task task);//not sure of input type
}
