/**
 * This java class will be the automated driver test for Storage Controller
 * 
 * @author Esmond
 */

package Storage;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StorageADT {
	
	StorageController program = new StorageController();
	
	@Before
	public void initialise() {
//		assertEquals("Storage initialised", program.initialiseStorage());
//		assertEquals("Dummy data created", program.createDummyData());
//		assertEquals("tables/storage.json", program.getFileRelativePath());
	}
	
//	@Test
//	public void testForGetAllDataEmpty() {
//		assertEquals(6, program.getAllData().getActiveTaskList().size());
//	}
	
	@Test
	public void testForDirectoryChange() {
		assertEquals("esmond/", program.setFileDirectory("esmond/"));
		program.storeAllData(program.getData());
	}
	
//	@After
//	public void cleanUp() {
//		program.deleteAllTables();
//	}
}
