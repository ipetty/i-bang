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
import net.ipetty.ibang.model.Delegation;
import net.ipetty.ibang.vo.Constants;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * DelegationDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Repository("delegationDao")
public class DelegationDaoImpl extends BaseJdbcDaoSupport implements DelegationDao {

	static final RowMapper<Delegation> ROW_MAPPER = new RowMapper<Delegation>() {
		@Override
		public Delegation mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, sn, seek_id, seeker_id, offer_id, offerer_id, deadline,
			// created_on, closed_on, status
			Delegation delegation = new Delegation();
			delegation.setId(rs.getLong("id"));
			delegation.setSn(rs.getString("sn"));
			delegation.setSeekId(rs.getLong("seek_id"));
			delegation.setSeekerId(rs.getInt("seeker_id"));
			delegation.setOfferId(rs.getLong("offer_id"));
			delegation.setOffererId(rs.getInt("offerer_id"));
			delegation.setDeadline(rs.getDate("deadline"));
			delegation.setCreatedOn(rs.getTimestamp("created_on"));
			delegation.setClosedOn(rs.getTimestamp("closed_on"));
			delegation.setStatus(rs.getString("status"));
			return delegation;
		}
	};

	private static final String SAVE_SQL = "insert into delegation(sn, seek_id, seeker_id, offer_id, offerer_id, deadline, status) values(?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(Delegation delegation) {
		delegation.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, delegation.getSn());
			statement.setLong(2, delegation.getSeekId());
			statement.setInt(3, delegation.getSeekerId());
			statement.setLong(4, delegation.getOfferId());
			statement.setInt(5, delegation.getOffererId());
			statement.setDate(6, delegation.getDeadline() != null ? new java.sql.Date(delegation.getDeadline()
					.getTime()) : null);
			statement.setString(7, delegation.getStatus());

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				delegation.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", delegation);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from delegation where id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_DELEGATION_ID_TO_DELEGATION, key = "${id}")
	public Delegation getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String GET_BY_OFFER_ID_SQL = "select * from delegation where offer_id=?";

	/**
	 * 根据应征单ID获取委托单，如没有对应的委托则返回null
	 */
	public Delegation getByOfferId(Long offerId) {
		return super.queryUniqueEntity(GET_BY_OFFER_ID_SQL, ROW_MAPPER, offerId);
	}

	private static final String LIST_BY_USER_ID_SQL = "select id from delegation where seeker_id=? or offerer_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的委托ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_SQL, LONG_ROW_MAPPER, userId, userId,
				pageNumber * pageSize, pageSize);
	}

	private static final String LIST_BY_USER_ID_AND_STATUS_SQL = "select id from delegation where (seeker_id=? or offerer_id=?) and status=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的指定状态的委托ID列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<Long> listByUserIdAndStatus(Integer userId, String status, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_AND_STATUS_SQL, LONG_ROW_MAPPER, userId, userId, status,
				pageNumber * pageSize, pageSize);
	}

	private static final String LIST_BY_SEEK_ID_SQL = "select id from delegation where seek_id=?";

	/**
	 * 获取指定求助单的所有委托ID列表
	 */
	@Override
	public List<Long> listBySeekId(Long seekId) {
		return super.getJdbcTemplate().query(LIST_BY_SEEK_ID_SQL, LONG_ROW_MAPPER, seekId);
	}

	private static final String UPDATE_STATUS_SQL = "update delegation set closed_on=?, status=? where id=?";

	/**
	 * 更新求助单状态
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_DELEGATION_ID_TO_DELEGATION, key = "${delegationId}")
	public void updateStatus(Long delegationId, String newStatus) {
		super.getJdbcTemplate().update(UPDATE_STATUS_SQL,
				Constants.DELEGATE_STATUS_CLOSED.equals(newStatus) ? new Date() : null, newStatus, delegationId);
	}

}
