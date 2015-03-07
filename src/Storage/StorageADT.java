/**
 * This java class will be the automated driver test for Storage Controller
 * 
 * @author Esmond
 */

package Storage;

import static org.junit.Assert.*;

import org.junit.Test;

public class StorageADT {

	@Test
	public void testForCreateDummyData() {
		assertEquals("Dummy data created", StorageController2.createDummyData());
	}

	@Test
	public void testForInitialiseStorage() {
		assertEquals("Storage initialised", StorageController2.initialiseStorage());
	}
	
	@Test
	public void testForInsertFileDirectory() {
		assertEquals("tables/", StorageController2.insertFileDirectory("tables/"));
	}
}
