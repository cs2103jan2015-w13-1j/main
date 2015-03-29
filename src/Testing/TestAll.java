package Testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Storage.HistoryADT;
import Storage.StorageADT;

@RunWith(Suite.class)
@SuiteClasses({HistoryADT.class, StorageADT.class, Logic.LogicControllerTest.class, Logic.LogicControllerTestRecurrence.class})
public class TestAll {

}
