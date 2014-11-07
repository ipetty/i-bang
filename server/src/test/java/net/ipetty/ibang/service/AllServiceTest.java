package net.ipetty.ibang.service;

import net.ipetty.ibang.test.util.DBUnitUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * AllServiceTest
 * @author luocanfeng
 * @date 2014年9月24日
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		UserServiceTest.class, LocationServiceTest.class, SeekServiceTest.class, OfferServiceTest.class,
		DelegationServiceTest.class, EvaluationServiceTest.class, SystemMessageServiceTest.class,
		LetterServiceTest.class, CrashLogServiceTest.class
})
public class AllServiceTest extends BaseServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBUnitUtils.cleanInsert();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

}
