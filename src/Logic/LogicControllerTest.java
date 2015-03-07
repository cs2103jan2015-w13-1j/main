package Logic;
import static org.junit.Assert.*;

import java.util.*;

import Common.*;
import Common.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;




public class LogicControllerTest {
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
	
	
	static Date d1 = new Date();
	static Date d2 = new Date();
	static Date d3 = new Date();
	static Date d4 = new Date();
	static Date d5 = new Date();
	static Date d6 = new Date();
	
	static Task t1;
	static Task t2;
	static Task t3;
	
	static Task t4;
	static Task t5;
	static Task t6;
	static Task t7;
	static Task t8;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	@Before
	public void setUp() throws Exception {
		
		lc.activeTaskList = new TaskList();
		lc.archivedTaskList = new TaskList();
		lc.toDoSortedList = new ToDoSortedList();

		tags1.add(tag1);
		
		tags2.add(tag1);
		tags2.add(tag2);
		
		tags3.add(tag1);
		tags3.add(tag4);

		tags4.add(tag2);
		tags4.add(tag3);
		tags4.add(tag4);

		tags5.add(tag2);
		
		Date d1 = new Date();
		Date d2 = new Date();
		Date d3 = new Date();
		Date d4 = new Date();
		Date d5 = new Date();
		Date d6 = new Date();
		d1.setTime(111111111);
		d2.setTime(222222222);
		d3.setTime(777777777);
		d4.setTime(555555555);
		d5.setTime(777677777);
		d6.setTime(776677777);
		
		t1 = new Task(1, "do a", 3, null);
		t2 = new Task(2, "get b", 6, null);
		t3 = new Task(3, "c waht", 2, tags1);
		t4 = new Task(4, "deadline d", d2,  5, tags2);
		t5 = new Task(5, "meeting e", d1, d4, 2, tags3);
		t6 = new Task(6, "f deadline", d3, 7, null);
		t7 = new Task(7, "g meeting", d5, d3, 2, tags4);
		t8 = new Task(8, "h deadline", d6, 4, tags4);
	}
	/**
	 * Adding one task
	 */
	@Test
	public void testAdd1() {
		lc.addTask(t1);
		assertEquals(1, lc.activeTaskList.size());
		assertEquals(t1, lc.activeTaskList.get(1));
		assertEquals(1, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t1));
		assertEquals(0, lc.archivedTaskList.size());
	}
	
	/**
	 * Adding two tasks
	 */
	@Test
	public void testAdd2() {
		lc.addTask(t1);
		lc.addTask(t2);
		assertEquals(2, lc.activeTaskList.size());
		assertEquals(t1, lc.activeTaskList.get(1));
		assertEquals(t2, lc.activeTaskList.get(2));
		assertEquals(2, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t1));
		assertEquals(true, lc.toDoSortedList.contains(t2));
		assertEquals("2\n1\n", lc.toDoSortedList.toString());
		assertEquals(0, lc.archivedTaskList.size());
	}
	
	/**
	 * Adding deadline
	 */
	@Test
	public void testAdddeadline() {
		lc.addTask(t1);
		lc.addTask(t2);
		lc.addDeadLine(t1, d1);
		assertEquals(2, lc.activeTaskList.size());
		assertEquals(t1, lc.activeTaskList.get(1));
		assertEquals(t2, lc.activeTaskList.get(2));
		assertEquals(2, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t1));
		assertEquals(true, lc.toDoSortedList.contains(t2));
		assertEquals("1\n2\n", lc.toDoSortedList.toString());
		assertEquals(0, lc.archivedTaskList.size());
		assertEquals("2\n1\n", lc.sortByPriority().toString());
		assertEquals(d1, t1.getDeadline());
	}
	
	/**
	 * Adding three tasks
	 */
	@Test
	public void testAdd3() {
		lc.addTask(t1);
		lc.addTask(t2);
		lc.addTask(t3);
		assertEquals(3, lc.activeTaskList.size());
		assertEquals(t1, lc.activeTaskList.get(1));
		assertEquals(t2, lc.activeTaskList.get(2));
		assertEquals(3, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t1));
		assertEquals(true, lc.toDoSortedList.contains(t2));
		assertEquals(true, lc.toDoSortedList.contains(t3));
		assertEquals("2\n1\n3\n", lc.toDoSortedList.toString());
		assertEquals(0, lc.archivedTaskList.size());
	}

	/**
	 * Searching by tag
	 */
	@Test
	public void testSearchTag() {
		lc.addTask(t1);
		lc.addTask(t2);
		lc.addTask(t3);
		ArrayList<Task> result1 = lc.searchByTag(tag1);
		assertEquals(1, result1.size());
		assertEquals(t3, result1.get(0));
		ArrayList<Task> result2 = lc.searchByTag(tag2);
		assertEquals(0, result2.size());
	}
	
	/**
	 * Adding meeting
	 */
	@Test
	public void testAddMeeting() {
		lc.addTask(t1);
		lc.addTask(t2);
		lc.addDeadLine(t1, d1);
		lc.addStartAndEndTime(t2, d1, d2);
		assertEquals(2, lc.activeTaskList.size());
		assertEquals(t1, lc.activeTaskList.get(1));
		assertEquals(t2, lc.activeTaskList.get(2));
		assertEquals(2, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t1));
		assertEquals(true, lc.toDoSortedList.contains(t2));
		assertEquals("2\n1\n", lc.toDoSortedList.toString());
		assertEquals(0, lc.archivedTaskList.size());
		assertEquals("2\n1\n", lc.sortByPriority().toString());
		assertEquals(d1, t1.getDeadline());
	}
}
