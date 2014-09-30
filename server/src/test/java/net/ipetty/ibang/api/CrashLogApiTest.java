package net.ipetty.ibang.api;

import net.ipetty.ibang.api.factory.IbangApi;
import net.ipetty.ibang.vo.CrashLogVO;

import org.junit.Test;

/**
 * CrashLogApiTest
 * 
 * @author luocanfeng
 * @date 2014年9月30日
 */
public class CrashLogApiTest extends BaseApiTest {

	private CrashLogApi crashLogApi = IbangApi.init().create(CrashLogApi.class);

	@Test
	public void testSave() {
		crashLogApi.save(new CrashLogVO(null, "api", "4.1.2", 8, "1.0.8", "crash", "test crash log"));
	}

}
