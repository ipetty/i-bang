package net.ipetty.ibang.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.annotation.LoadFromCache;
import net.ipetty.ibang.cache.annotation.UpdateToCache;
import net.ipetty.ibang.vo.SeekCategory;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * OfferRangeDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Repository("offerRangeDao")
public class OfferRangeDaoImpl extends BaseJdbcDaoSupport implements OfferRangeDao {

	static final RowMapper<SeekCategory> ROW_MAPPER = new RowMapper<SeekCategory>() {
		@Override
		public SeekCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id, category_l1, category_l2
			SeekCategory seekCategory = new SeekCategory();
			seekCategory.setCategoryL1(rs.getString("category_l1"));
			seekCategory.setCategoryL2(rs.getString("category_l2"));
			return seekCategory;
		}
	};

	private static final String DELETE_SQL = "delete from offerer_range where user_id=?";
	private static final String SAVE_SQL = "insert into offerer_range(user_id, category_l1, category_l2) values(?, ?, ?)";

	/**
	 * 保存或更新帮助者的帮忙范围
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_OFFER_RANGE, key = "${userId}")
	public void saveOrUpdate(final Integer userId, final List<SeekCategory> offerRange) {
		super.getJdbcTemplate().update(DELETE_SQL, userId);

		super.getJdbcTemplate().batchUpdate(SAVE_SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				SeekCategory category = offerRange.get(i);
				ps.setInt(1, userId);
				ps.setString(2, category.getCategoryL1());
				ps.setString(3, category.getCategoryL2());
			}

			@Override
			public int getBatchSize() {
				return offerRange.size();
			}
		});
	}

	private static final String GET_BY_USER_ID_SQL = "select * from offerer_range where user_id=?";

	/**
	 * 获取帮助者的帮忙范围信息
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_ID_TO_OFFER_RANGE, key = "${userId}")
	public List<SeekCategory> getByUserId(Integer userId) {
		return super.getJdbcTemplate().query(GET_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

}
