package net.ipetty.ibang.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.annotation.LoadFromCache;
import net.ipetty.ibang.cache.annotation.UpdateToCache;
import net.ipetty.ibang.model.OffererInfo;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * OffererInfoDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Repository("offererInfoDao")
public class OffererInfoDaoImpl extends BaseJdbcDaoSupport implements OffererInfoDao {

	static final RowMapper<OffererInfo> ROW_MAPPER = new RowMapper<OffererInfo>() {
		@Override
		public OffererInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id, offer_count, total_point, title
			OffererInfo offererInfo = new OffererInfo();
			offererInfo.setUserId(rs.getInt("user_id"));
			offererInfo.setOfferCount(rs.getInt("offer_count"));
			offererInfo.setTotalPoint(rs.getInt("total_point"));
			offererInfo.setTitle(rs.getString("title"));
			return offererInfo;
		}
	};

	private static final String SAVE_OR_UPDATE_SQL = "replace into offerer_info(user_id, offer_count, total_point, title) values(?, ?, ?, ?)";

	/**
	 * 保存或更新
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_OFFERER_INFO, key = "${offererInfo.userId}")
	public void saveOrUpdate(OffererInfo offererInfo) {
		super.getJdbcTemplate().update(SAVE_OR_UPDATE_SQL, offererInfo.getUserId(), offererInfo.getOfferCount(),
				offererInfo.getTotalPoint(), offererInfo.getTitle());
	}

	private static final String GET_BY_USER_ID_SQL = "select * from offerer_info where user_id=?";

	/**
	 * 获取帮助者相应积分信息
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_ID_TO_OFFERER_INFO, key = "${userId}")
	public OffererInfo getByUserId(Integer userId) {
		return super.queryUniqueEntity(GET_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

}
