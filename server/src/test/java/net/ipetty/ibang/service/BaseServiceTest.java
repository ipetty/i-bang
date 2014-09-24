package net.ipetty.ibang.service;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * BaseServiceTest
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class BaseServiceTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

}
