package net.ipetty.ibang.service;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Location;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * LocationServiceTest
 * @author luocanfeng
 * @date 2014年5月9日
 */
public class LocationServiceTest extends BaseServiceTest {

	@Resource
	private LocationService locationService;

	@Test
	public void testAll() {
		Location location = new Location(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true);
		locationService.save(location);
		Assert.assertNotNull(location.getId());

		location = locationService.getById(location.getId());
		Assert.assertNotNull(location);

		locationService.setLbsId(location.getId(), "test_lbs_id");
		location = locationService.getById(location.getId());
		Assert.assertNotNull(location);
		Assert.assertTrue(StringUtils.isNotBlank(location.getLbsId()));
	}

}
