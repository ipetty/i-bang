package net.ipetty.ibang.service;

import javax.annotation.Resource;

import net.ipetty.ibang.model.CrashLog;

import org.junit.Test;

/**
 * CrashLogServiceTest
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrashLogServiceTest extends BaseServiceTest {

	@Resource
	private CrashLogService crashLogService;

	@Test
	public void testSave() {
		crashLogService.save(new CrashLog(null, "user", "4.1.2", 8, "1.0.8", "crash", "test crash log"));
	}

}
