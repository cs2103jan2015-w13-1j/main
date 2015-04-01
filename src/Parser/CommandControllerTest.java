package Parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommandControllerTest {
	
	CommandController test = new CommandController();
	
	@Test
	public void testAdd() {
		test.initialiseTasks();
		
		//no description
		assertEquals("No description added, try again", test.executeCommand("add"));
		//wrong syntax in add command
		assertEquals("Invalid command", test.executeCommand("a"));
		//wrong syntax in priority
		//wrong syntax in tags
		//wrong syntax in date
		//date end < start
		//date already passed
		//add generic
		assertEquals("New task added: generic task",test.executeCommand("add generic task"));
		//add deadline
		assertEquals("New task added with deadline: deadline task",test.executeCommand("add deadline task -by 22/11/2015"));
		//add meeting
		assertEquals("New task added for meeting: meeting task",test.executeCommand("add meeting task -on 11/11/2015 from 1100 to 1200"));
	}
	
	@Test
	public void testDelete() {
		
		test.initialiseTasks();
		int size =test.returnTasks().size();
		test.executeCommand("delete 1");
		int size2 =test.returnTasks().size();
		assertEquals(size-1,size2);
	}
	
	/*@Test
	public void testChange() {
		
	}*/
	
	/*@Test
	public void testSearch() {
		
	}*/
}
