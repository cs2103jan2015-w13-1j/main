/**
 * This java class will be the automated driver test for History Controller
 * 
 * @author Esmond
 */

package Storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import History.HistoryController;

public class HistoryADT {
	HistoryController program;
	
	/**
	 * Initialise stack in history controller
	 */
	@Before
	public void initialise() {
		program = new HistoryController();
	}
	
	/**
	 * Test for single log function
	 */
	@Test
	public void testForSingleLog() {
		assertEquals(true, program.log());
		assertEquals(1, program.getLogList().size());
	}
	
	/**
	 * Test for multiple log function
	 */
	@Test
	public void testForMultipleLog() {
		assertEquals(true, program.log());
		assertEquals(true, program.log());
		assertEquals(true, program.log());
		assertEquals(true, program.log());
		assertEquals(true, program.log());
		assertEquals(5, program.getLogList().size());
	}
	
	/**
	 * Test for undo function with empty stack
	 */
	@Test
	public void testForUndoWithEmptyStack() {
		assertEquals(0, program.getLogList().size());
		assertNull(program.undo());
	}
	
	/**
	 * Test for undo function with one log entry
	 */
	@Test
	public void testForUndoWithOneLog() {
		assertEquals(0, program.getLogList().size());
		testForSingleLog();
		assertEquals(1, program.getLogList().size());
		assertNotNull(program.undo());
		assertEquals(0, program.getLogList().size());
	}
	
	/**
	 * clear stack entries
	 */
	@After
	public void cleanUp() {
		program.getLogList().clear();
	}
}
