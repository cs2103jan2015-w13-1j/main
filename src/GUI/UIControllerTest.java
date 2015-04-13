//@author A0110837L

package GUI;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Command.CommandController;
import Common.Task;

public class UIControllerTest {

	UIController uiController = new UIController();
	CommandController commandController = new CommandController();
	
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static ArrayList<Task> uiTaskList = new ArrayList<Task>();
	
	@Before
	public void setUp(){
		taskList.add(new Task(1, "dummyMeetingTask 3", 3,null));
	}
	
	@Test
	public void testMessageAdd(){
		String message = commandController.executeCommand("-add generic1");
		assertEquals("New task added: generic1", message);
	}
	
	@Test
	public void testAddToTaskDisplay(){
		commandController.executeCommand("-add generic1");
		uiTaskList = commandController.returnTasks();
		assertEquals(taskList, uiTaskList);
	}
}
