package net.ipetty.ibang.service;

import net.ipetty.ibang.test.DBUnitUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * AllServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月24日
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ UserServiceTest.class, SeekerInfoServiceTest.class, OffererInfoServiceTest.class,
		SeekServiceTest.class, OfferServiceTest.class })
public class AllServiceTest extends BaseServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBUnitUtils.cleanInsert();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

}
