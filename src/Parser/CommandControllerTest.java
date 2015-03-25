package Parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommandControllerTest {
	
	CommandController test = new CommandController();
	
	@Test
	public void testAdd() {
		test.initialiseTasks();
		
		//no description
		assertEquals("No description added, try again", test.executeCommand("-add"));
		//wrong syntax
		assertEquals("Invalid command", test.executeCommand("add"));
		//add generic
		assertEquals("New task added: generic task",test.executeCommand("-add generic task"));
		//add deadline
		assertEquals("New task added with deadline: deadline task",test.executeCommand("-add deadline task -date 22/11/2015"));
		//add meeting
		assertEquals("New task added for meeting: meeting task",test.executeCommand("-add meeting task -date 11/11/2015 1100 1200"));
	}

}
