package net.ipetty.ibang.api;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * BaseSDKTest
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-api-test.xml")
public class BaseApiTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

}
