package Testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({History.HistoryADT.class, Storage.StorageADT.class, Logic.LogicControllerTest.class, Logic.LogicControllerTestRecurrence.class})
public class TestAll {

}
