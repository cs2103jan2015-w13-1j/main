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


public class LogicControllerTest {
	static LogicController lc = new LogicController();

	
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
		
		lc.isTesting = true;
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
//		lc.historyController = new HistoryController();
//		lc.activeTaskList = new TaskList();
//		lc.archivedTaskList = new TaskList();
//		lc.toDoSortedList = new ToDoSortedList();
		
		t1 = new Task(1, "do a", 3, null);
		t2 = new Task(2, "get b", 6, null);
		t3 = new Task(3, "c waht", 2, tags1);
		t4 = new Task(4, "deadline d", d2,  5, tags2);
		t5 = new Task(5, "meeting e", d1, d4, 2, tags3);
		t6 = new Task(6, "f deadline", d3, 7, null);
		t7 = new Task(7, "g meeting", d5, d3, 2, tags4);
		t8 = new Task(8, "h deadline", d6, 4, tags4);
		t9 = new Task(9, "i deadline", d3, 9, null);
		
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
		ToDoSortedList td1 = lc.addTask(t1);
		assertEquals(1, td1.size());
		ToDoSortedList td2 = lc.addTask(t2);
		assertEquals(2, td2.size());
		assertEquals("2\n1\n", td2.toString());
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
	 * Adding two tasks from UI
	 */
	@Test
	public void testAddPotentialError() {
		ToDoSortedList td1 = lc.addTask(t00);
		assertEquals(1, td1.size());
		ToDoSortedList td2 = lc.addTask(t01);
		assertEquals(2, td2.size());
		assertEquals("1\n2\n", td2.toString());
		assertEquals(2, lc.activeTaskList.size());
		assertEquals(t00, lc.activeTaskList.get(1));
		assertEquals(t01, lc.activeTaskList.get(2));
		assertEquals(2, lc.toDoSortedList.size());
		assertEquals(true, lc.toDoSortedList.contains(t00));
		assertEquals(true, lc.toDoSortedList.contains(t01));
		assertEquals("1\n2\n", lc.toDoSortedList.toString());
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
	
	/**
	 * Edit meeting
	 */
	@Test
	public void testEditMeeting() {
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
		lc.editEndTime(t2, d3);
		assertEquals(2, lc.activeTaskList.size());
		assertEquals(2, lc.toDoSortedList.size());
		assertEquals("2\n1\n", lc.toDoSortedList.toString());
	}
	
	/**
	 * Edit deadline
	 */
	@Test
	public void testEditDeadline() {
		lc.addTask(t1);
		lc.addTask(t2);
		lc.addDeadLine(t1, d1);
		assertEquals(d1, t1.getDeadline());
		lc.editDeadline(t1, d3);
		assertEquals("1\n2\n", lc.toDoSortedList.toString());
		assertEquals(d3, t1.getDeadline());
	}
	
	/**
	 * Edit priority
	 */
	@Test
	public void testEditPriority() {
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
		lc.editPriority(t3, 7);
		assertEquals("3\n2\n1\n", lc.toDoSortedList.toString());
	}
	
	/**
	 * add tag
	 */
	@Test
	public void testAddTag() {
		lc.addTask(t1);
		lc.addTask(t2);
		lc.addTask(t3);
		lc.addTask(t5);
		ArrayList<Task> result1 = lc.searchByTag(tag1);
		assertEquals(2, result1.size());
		assertEquals(t5, result1.get(0));
		ArrayList<Task> result2 = lc.searchByTag(tag2);
		assertEquals(0, result2.size());
		ArrayList<Task> result3 = lc.searchByTag(tag4);
		assertEquals(1, result3.size());
		lc.addTag(t1, tag1);
		ArrayList<Task> result4= lc.searchByTag(tag1);
		assertEquals(3, result4.size());
		assertEquals(t5, result4.get(0));
		assertEquals(t1, result4.get(1));
	}
	
	/**
	 * remove tag
	 */
	@Test
	public void testRemoveTag() {
		lc.addTask(t1);
		lc.addTask(t2);
		lc.addTask(t3);
		lc.addTask(t5);
		lc.removeTag(t5, tag1);
		ArrayList<Task> result4= lc.searchByTag(tag1);
		assertEquals(1, result4.size());
		assertEquals(t3, result4.get(0));
	}
	
	/**
	 * search by date
	 */
	@Test
	public void testSearchDate() {
		lc.addTask(t4);
		lc.addTask(t5);
		lc.addTask(t6);
		lc.addTask(t7);
		lc.addTask(t8);
		lc.addTask(t9);
		ArrayList<Task> result = lc.searchByDate("19700110");
		assertEquals(4, result.size());
		assertEquals(t8, result.get(0));
		assertEquals(t7, result.get(1));
		assertEquals(t9, result.get(2));
		assertEquals(t6, result.get(3));
		ArrayList<Task> result2 = lc.searchByDate("19700103");
		assertEquals(2, result2.size());
		assertEquals(t5, result2.get(0));
		assertEquals(t4, result2.get(1));
	}
	
	/**
	 * Sort by date or priority
	 */
	@Test
	public void testSort() {
		lc.addTask(t1);
		lc.addTask(t2);
		lc.addTask(t3);
		lc.addTask(t4);
		lc.addTask(t5);
		lc.addTask(t6);
		lc.addTask(t7);
		lc.addTask(t8);
		lc.addTask(t9);
		PrioritySortedList priorityLsit = lc.sortByPriority();
		assertEquals(9, priorityLsit.size());
		assertEquals("9\n6\n2\n4\n8\n1\n5\n7\n3\n",priorityLsit.toString());
		
		ToDoSortedList todoSorted = lc.sortByTime();
		assertEquals(9, todoSorted.size());
		assertEquals("5\n4\n8\n7\n9\n6\n2\n1\n3\n",todoSorted.toString());
	}
}
