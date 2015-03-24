package Logic;
import static org.junit.Assert.*;

import java.util.*;

import Common.*;
import Common.Date;
import History.HistoryController;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class LogicControllerTestRecurrence {
	LogicController lc = new LogicController();

	
	static String tag1 = "tagA";
	static String tag2 = "tagB";
	static String tag3 = "tagC";
	static String tag4 = "tagD";
	
	static ArrayList<String> tags1 = new ArrayList<String>();
	static ArrayList<String> tags2 = new ArrayList<String>();
	static ArrayList<String> tags3 = new ArrayList<String>();
	static ArrayList<String> tags4 = new ArrayList<String>();	
	static ArrayList<String> tags5 = new ArrayList<String>();
	
	static Date d1;
	static Date d2;
	static Date d3;
	static Date d4;
	static Date d5;
	static Date d6;
	
	static Task t1;
	static Task t2;
	static Task t3;
	static Task t4;
	static Task t5;
	static Task t6;
	static Task t7;
	static Task t8;
	static Task t9;
	static Task t00;
	static Task t01;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		d1 = new Date();
		d2 = new Date();
		d3 = new Date();
		d4 = new Date();
		d5 = new Date();
		d6 = new Date();
		d1.setTime(111111111);
		d2.setTime(222222222);
		d3.setTime(777777777);
		d4.setTime(555555555);
		d5.setTime(777677777);
		d6.setTime(776677777);
		
	}
	@Before
	public void setUp() throws Exception {
		tags1.add(tag1);
		
		tags2.add(tag1);
		tags2.add(tag2);
		
		tags3.add(tag1);
		tags3.add(tag4);

		tags4.add(tag2);
		tags4.add(tag3);
		tags4.add(tag4);

		tags5.add(tag2);
		
		lc.initialise();
		lc.data = new DATA();
//		lc.historyController = new HistoryController();
//		lc.activeTaskList = new TaskList();
//		lc.archivedTaskList = new TaskList();
//		lc.toDoSortedList = new ToDoSortedList();
		
		t1 = new Task(1, "do a", 3, null);
		t2 = new Task(2, "get b", 6, null);
		t3 = new Task(3, "c waht", 2, tags1);
		t4 = new Task(4, "deadline d", d2,  5, tags2);
		t5 = new Task(555, "meeting e", d1, d4, 2, tags3);
		t6 = new Task(666, "f deadline", d3, 7, null);
		t7 = new Task(777, "g meeting", d5, d3, 2, tags4);
		t8 = new Task(888, "h deadline", d6, 4, tags4);
		t9 = new Task(999, "i deadline", d3, 9, null);
		
		t00 = new Task(1, "task", -1, null);
		t01 = new Task(2, "test", -1, null);
	}
	
	@After
	public void clear() throws Exception {
		tags1.clear();
		tags2.clear();
		tags3.clear();
		tags4.clear();
		tags5.clear();
		
		lc.data.clearAllData();
		lc.DC.storeAllData(lc.data);
	}
	
	/**
	 * Adding one recurrence task
	 */
	@Test
	public void testAdd1R() {
		lc.addRecurringTask(t4, Date.getMiliseconds("oneWeek"), 10);
		assertEquals(10, lc.activeTaskList.size());
		assertEquals(t4, lc.activeTaskList.getTaskbyId(4));
		assertEquals(10, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t4));
		assertEquals(0, lc.archivedTaskList.size());
	}
	
	/**
	 * Adding two recurrence task
	 */
	@Test
	public void testAdd2R() {
		lc.addRecurringTask(t4, Date.getMiliseconds("oneWeek"), 10);
		assertEquals(10, lc.activeTaskList.size());
		assertEquals(t4, lc.activeTaskList.getTaskbyId(4));
		assertEquals(10, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t4));
		assertEquals(0, lc.archivedTaskList.size());
		lc.addRecurringTask(t5, Date.getMiliseconds("oneWeek"), 10);
		assertEquals(20, lc.activeTaskList.size());	
	}
	
	/**
	 * Archive an recurrence task
	 */
	@Test
	public void testArchive() {
		lc.addRecurringTask(t4, Date.getMiliseconds("oneWeek"), 10);
		assertEquals(10, lc.activeTaskList.size());
		assertEquals(t4, lc.activeTaskList.getTaskbyId(4));
		assertEquals(10, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t4));
		assertEquals(0, lc.archivedTaskList.size());
		lc.addRecurringTask(t5, Date.getMiliseconds("oneWeek"), 10);
		assertEquals(20, lc.activeTaskList.size());	
		lc.archiveAllTasks(t5, new Date());
		assertEquals(10, lc.activeTaskList.size());
		assertEquals(t4, lc.activeTaskList.getTaskbyId(4));
		assertEquals(10, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t4));
		assertEquals(10, lc.archivedTaskList.size());
	}
	
	/**
	 * delete an recurrence task
	 */
	@Test
	public void testDelete() {
		lc.addRecurringTask(t4, Date.getMiliseconds("oneWeek"), 5);
		assertEquals(5, lc.activeTaskList.size());
		assertEquals(t4, lc.activeTaskList.getTaskbyId(4));
		assertEquals(5, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t4));
		assertEquals(0, lc.archivedTaskList.size());
		System.out.println(lc.toDoSortedList);
		lc.addRecurringTask(t5, Date.getMiliseconds("oneWeek"), 5);
		assertEquals(10, lc.activeTaskList.size());	
		lc.deleteAllRecurringTask(t5);
		assertEquals(5, lc.activeTaskList.size());
		assertEquals(t4, lc.activeTaskList.getTaskbyId(4));
		assertEquals(5, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t4));
		assertEquals(0, lc.archivedTaskList.size());
	}
}
