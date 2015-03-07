/**
 * This java class will be the automated driver test for Storage Controller
 * 
 * @author Esmond
 */

package Storage;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StorageADT {
	
	StorageController2 program = new StorageController2();
	
	@Before
	public void initialise() {
		assertEquals("tables/", program.insertFileDirectory("tables/"));
		assertEquals("Storage initialised", program.initialiseStorage());
		assertEquals("Dummy data created", program.createDummyData());
		assertEquals("tables/storage.json", program.getFileRelativePath());
	}
	
	@Test
	public void testForInsertFileDirectory() {
		assertEquals("../tables/", program.setFileDirectory("../tables/"));
	}
	
	@After
	public void cleanUp() {
		assertEquals("Storage initialised", program.initialiseStorage());
	}
}
