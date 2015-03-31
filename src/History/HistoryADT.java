/**
 * This java class will be the automated driver test for History Controller
 * 
 * @author Esmond
 */

package History;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		// Verify logList is empty
		assertEquals(0, program.getLogList().size());
		// Perform 1 unsuccessful undo()
		assertNull(program.undo());
	}
	
	/**
	 * Test for undo function with one log entry
	 */
	@Test
	public void testForUndoWithOneLog() {
		// Perform 1 log()
		assertEquals(0, program.getLogList().size());
		assertEquals(true, program.log());
		// Verify logList size is 1
		assertEquals(1, program.getLogList().size());
		// Perform 1 successful undo()
		assertNotNull(program.undo());
		// Verify logList is empty
		assertEquals(0, program.getLogList().size());
	}
	
	@Test
	public void testForRedoWithEmptyStack() {
		// Verify both logList and redoList are empty
		assertEquals(0, program.getLogList().size());
		assertEquals(0, program.getRedoList().size());
		// Perform 1 unsuccessful redo()
		assertNull(program.redo());
	}
	
	@Test
	public void testForRedoWithOneUndo() {
		// Perform 1 log()
		assertEquals(true, program.log());
		// Verify logList size is 1 and redoList is empty
		assertEquals(1, program.getLogList().size());
		assertEquals(0, program.getRedoList().size());
		// Perform 1 unsuccessful redo()
		assertNull(program.redo());
		// Perform 1 successful undo()
		assertNotNull(program.undo());
		// Verify logList is empty and redoList size is 1
		assertEquals(0, program.getLogList().size());
		assertEquals(1, program.getRedoList().size());
		// Perform 1 successful redo()
		assertNotNull(program.redo());
		// Verify both logList and redoList are empty
		assertEquals(0, program.getLogList().size());
		assertEquals(0, program.getRedoList().size());
	}
	
	@Test
	public void testForOneUndoThenLog() {
		// Perform 1 log()
		assertEquals(true, program.log());
		// Verify logList size is 1 and redoList is empty
		assertEquals(1, program.getLogList().size());
		assertEquals(0, program.getRedoList().size());
		// Perform 1 successful undo()
		assertNotNull(program.undo());
		// Verify logList is empty and redoList size is 1
		assertEquals(0, program.getLogList().size());
		assertEquals(1, program.getRedoList().size());
		// Perform 1 successful undo()
		assertEquals(true, program.log());
		// Verify logList size is 1 and redoList is empty
		assertEquals(1, program.getLogList().size());
		assertEquals(0, program.getRedoList().size());
	}
	
	/**
	 * clear stack entries
	 */
	@After
	public void cleanUp() {
		program.getLogList().clear();
		program.getRedoList().clear();
	}
}
