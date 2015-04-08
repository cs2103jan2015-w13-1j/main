// @author A0111866E

/**
 * This java class will be the automated driver test for Storage Controller
 */

package Storage;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StorageADT {
	
	private StorageController program;
	private StorageDirectory directory;
	
	/**
	 * Initialise storage utility file with its default settings and initialise new DATA object
	 */
	@Before
	public void initialise() {
		program = new StorageController();
		directory = new StorageDirectory();
		program.initialiseNewDataObject();
	}
	
	/**
	 * Test for creation of dummy data
	 */
	@Test
	public void testForDummyData() {
		assertEquals("9 Dummy data created.", program.createDummyData());
		assertEquals(6, program.getData().getActiveTaskList().size());
		assertEquals(3, program.getData().getArchivedTaskList().size());
	}
	
	/**
	 * Test for storing after dummy creation
	 */
	@Test
	public void testForStoring() {
		testForDummyData();
		assertEquals("success in storing", program.storeAllData(program.getData()));
	}
	
	/**
	 * Test for retrieval from storage
	 */
	@Test
	public void testForRetrieval() {
		testForStoring();
		assertEquals(6, program.getAllData().getActiveTaskList().size());
		assertEquals(3, program.getAllData().getArchivedTaskList().size());
	}
	
	@After
	public void cleanUp() {
		directory.changeFileDirectory("tables");
	}
}
