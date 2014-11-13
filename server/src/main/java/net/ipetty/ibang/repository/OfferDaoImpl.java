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
import net.ipetty.ibang.model.Offer;
import net.ipetty.ibang.vo.Constants;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * OfferDaoImpl
 * @author luocanfeng
 * @date 2014年9月22日
 */
@Repository("offerDao")
public class OfferDaoImpl extends BaseJdbcDaoSupport implements OfferDao {

	static final RowMapper<Offer> ROW_MAPPER = new RowMapper<Offer>() {

		@Override
		public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, sn, offerer_id, seek_id, content, description, deadline,
			// created_on, closed_on, status
			Offer offer = new Offer();
			offer.setId(rs.getLong("id"));
			offer.setSn(rs.getString("sn"));
			offer.setOffererId(rs.getInt("offerer_id"));
			offer.setSeekId(rs.getLong("seek_id"));
			offer.setContent(rs.getString("content"));
			offer.setDescription(rs.getString("description"));
			offer.setDeadline(rs.getDate("deadline"));
			offer.setCreatedOn(rs.getTimestamp("created_on"));
			offer.setClosedOn(rs.getTimestamp("closed_on"));
			offer.setStatus(rs.getString("status"));
			return offer;
		}
	};

	private static final String SAVE_SQL = "insert into offer(sn, offerer_id, seek_id, content, description, deadline, status) values(?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(Offer offer) {
		offer.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, offer.getSn());
			statement.setInt(2, offer.getOffererId());
			statement.setLong(3, offer.getSeekId());
			statement.setString(4, offer.getContent());
			statement.setString(5, offer.getDescription());
			statement.setDate(6, offer.getDeadline() != null ? new java.sql.Date(offer.getDeadline().getTime()) : null);
			statement.setString(7, offer.getStatus());

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				offer.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", offer);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from offer where id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_OFFER_ID_TO_OFFER, key = "${id}")
	public Offer getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String LIST_BY_SEEK_ID_SQL = "select id from offer where enable and seek_id=? order by created_on desc";

	/**
	 * 获取指定求助单的应征单ID列表
	 */
	@Override
	public List<Long> listBySeekId(Long seekId) {
		return super.getJdbcTemplate().query(LIST_BY_SEEK_ID_SQL, LONG_ROW_MAPPER, seekId);
	}

	private static final String LIST_BY_USER_ID_SQL = "select id from offer where enable and offerer_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的应征单ID列表
	 * @param pageNumber 分页页码，从0开始
	 */
	@Override
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_SQL, LONG_ROW_MAPPER, userId, pageNumber * pageSize,
				pageSize);
	}

	private static final String UPDATE_STATUS_SQL = "update offer set closed_on=?, status=? where id=?";

	/**
	 * 更新应征单状态
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_OFFER_ID_TO_OFFER, key = "${offerId}")
	public void updateStatus(Long offerId, String newStatus) {
		super.getJdbcTemplate().update(UPDATE_STATUS_SQL,
				Constants.OFFER_STATUS_CLOSED.equals(newStatus) ? new Date() : null, newStatus, offerId);
	}

	private static final String DISABLE_SQL = "update offer set enable=false, disabled_on=now() where id=?";

	/**
	 * 禁用/软删除
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_OFFER_ID_TO_OFFER, key = "${offerId}")
	public void disable(Long offerId) {
		super.getJdbcTemplate().update(DISABLE_SQL, offerId);
	}

}
