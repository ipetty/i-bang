package net.ipetty.ibang.api;

import net.ipetty.ibang.test.util.DBUnitUtils;
import net.ipetty.ibang.test.util.JettyServer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * AllApiTest
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ UserApiTest.class, SeekApiTest.class, OfferApiTest.class, DelegationApiTest.class,
		SystemMessageApiTest.class, CrashLogApiTest.class })
public class AllApiTest extends BaseApiTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBUnitUtils.cleanInsert();
		JettyServer.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		JettyServer.stop();
	}

}
