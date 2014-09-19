package net.ipetty.ibang.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.annotation.LoadFromCache;
import net.ipetty.ibang.cache.annotation.UpdateToCache;
import net.ipetty.ibang.model.SeekerInfo;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * SeekerInfoDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Repository("seekerInfoDao")
public class SeekerInfoDaoImpl extends BaseJdbcDaoSupport implements SeekerInfoDao {

	static final RowMapper<SeekerInfo> ROW_MAPPER = new RowMapper<SeekerInfo>() {
		@Override
		public SeekerInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id, seek_count, total_point, title
			SeekerInfo seekerInfo = new SeekerInfo();
			seekerInfo.setUserId(rs.getInt("user_id"));
			seekerInfo.setSeekCount(rs.getInt("seek_count"));
			seekerInfo.setTotalPoint(rs.getInt("total_point"));
			seekerInfo.setTitle(rs.getString("title"));
			return seekerInfo;
		}
	};

	private static final String SAVE_OR_UPDATE_SQL = "replace into seeker_info(user_id, seek_count, total_point, title) values(?, ?, ?, ?)";

	/**
	 * 保存或更新
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_SEEKER_INFO, key = "${seekerInfo.userId}")
	public void saveOrUpdate(SeekerInfo seekerInfo) {
		super.getJdbcTemplate().update(SAVE_OR_UPDATE_SQL, seekerInfo.getUserId(), seekerInfo.getSeekCount(),
				seekerInfo.getTotalPoint(), seekerInfo.getTitle());
	}

	private static final String GET_BY_USER_ID_SQL = "select * from seeker_info where user_id=?";

	/**
	 * 获取求助者相应积分信息
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_ID_TO_SEEKER_INFO, key = "${userId}")
	public SeekerInfo getByUserId(Integer userId) {
		return super.queryUniqueEntity(GET_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

}
