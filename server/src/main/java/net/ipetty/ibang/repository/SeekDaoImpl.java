package net.ipetty.ibang.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.annotation.LoadFromCache;
import net.ipetty.ibang.cache.annotation.UpdateToCache;
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.vo.Constants;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * SeekDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Repository("seekDao")
public class SeekDaoImpl extends BaseJdbcDaoSupport implements SeekDao {

	static final RowMapper<Seek> ROW_MAPPER = new RowMapper<Seek>() {
		@Override
		public Seek mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, sn, seeker_id, contact_info_visible, category_l1,
			// category_l2, content, requirement, delegate_number, reward,
			// additional_reward, created_on, expire_date, closed_on, status
			Seek seek = new Seek();
			seek.setId(rs.getLong("id"));
			seek.setSn(rs.getString("sn"));
			seek.setSeekerId(rs.getInt("seeker_id"));
			seek.setContactInfoVisible(rs.getBoolean("contact_info_visible"));
			seek.setCategoryL1(rs.getString("category_l1"));
			seek.setCategoryL2(rs.getString("category_l2"));
			seek.setContent(rs.getString("content"));
			seek.setRequirement(rs.getString("requirement"));
			seek.setDelegateNumber(rs.getInt("delegate_number"));
			seek.setReward(rs.getString("reward"));
			seek.setAdditionalReward(rs.getString("additional_reward"));
			seek.setCreatedOn(rs.getTimestamp("created_on"));
			seek.setExipireDate(rs.getDate("expire_date"));
			seek.setClosedOn(rs.getTimestamp("closed_on"));
			seek.setStatus(rs.getString("status"));
			return seek;
		}
	};

	private static final String SAVE_SQL = "insert into seek(sn, seeker_id, contact_info_visible, category_l1, category_l2,"
			+ " content, requirement, delegate_number, reward, additional_reward, expire_date, status)"
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(Seek seek) {
		seek.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, seek.getSn());
			statement.setInt(2, seek.getSeekerId());
			statement.setBoolean(3, seek.isContactInfoVisible());
			statement.setString(4, seek.getCategoryL1());
			statement.setString(5, seek.getCategoryL2());
			statement.setString(6, seek.getContent());
			statement.setString(7, seek.getRequirement());
			statement.setInt(8, seek.getDelegateNumber());
			statement.setString(9, seek.getReward());
			statement.setString(10, seek.getAdditionalReward());
			statement.setDate(11, seek.getExipireDate() != null ? new java.sql.Date(seek.getExipireDate().getTime())
					: null);
			statement.setString(12, seek.getStatus());

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				seek.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", seek);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from seek where id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_SEEK_ID_TO_SEEK, key = "${id}")
	public Seek getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String LIST_LATEST_SQL = "select id from seek where created_on<=? and (status=? or status=?) order by created_on desc limit ?,?";

	/**
	 * 获取最新的未关闭求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<Long> listLatest(Date timeline, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_LATEST_SQL, LONG_ROW_MAPPER, timeline, Constants.SEEK_STATUS_CREATED,
				Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
	}

	private static final String LIST_BY_USER_ID_SQL = "select id from seek where seeker_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的求助列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_SQL, LONG_ROW_MAPPER, userId, pageNumber * pageSize,
				pageSize);
	}

	private static final String UPDATE_STATUS_SQL = "update seek set closed_on=?, status=? where id=?";

	/**
	 * 更新求助单状态
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_SEEK_ID_TO_SEEK, key = "${seekId}")
	public void updateStatus(Long seekId, String newStatus) {
		super.getJdbcTemplate().update(UPDATE_STATUS_SQL,
				Constants.SEEK_STATUS_CLOSED.equals(newStatus) ? new Date() : null, newStatus, seekId);
	}

}
