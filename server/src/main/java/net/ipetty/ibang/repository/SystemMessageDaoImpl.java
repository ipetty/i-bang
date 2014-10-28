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
import net.ipetty.ibang.model.SystemMessage;
import net.ipetty.ibang.util.JdbcDaoUtils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * SystemMessageDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Repository("systemMessageDao")
public class SystemMessageDaoImpl extends BaseJdbcDaoSupport implements SystemMessageDao {

	static final RowMapper<SystemMessage> ROW_MAPPER = new RowMapper<SystemMessage>() {
		@Override
		public SystemMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, from_user_id, receiver_id, type, seek_id, offer_id,
			// delegation_id, evaluation_id, title, content, is_read, created_on
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setId(rs.getLong("id"));
			systemMessage.setFromUserId(JdbcDaoUtils.getInteger(rs, "from_user_id"));
			systemMessage.setReceiverId(JdbcDaoUtils.getInteger(rs, "receiver_id"));
			systemMessage.setType(rs.getString("type"));
			systemMessage.setSeekId(JdbcDaoUtils.getLong(rs, "seek_id"));
			systemMessage.setOfferId(JdbcDaoUtils.getLong(rs, "offer_id"));
			systemMessage.setDelegationId(JdbcDaoUtils.getLong(rs, "delegation_id"));
			systemMessage.setEvaluationId(JdbcDaoUtils.getLong(rs, "evaluation_id"));
			systemMessage.setTitle(rs.getString("title"));
			systemMessage.setContent(rs.getString("content"));
			systemMessage.setRead(rs.getBoolean("is_read"));
			systemMessage.setCreatedOn(rs.getTimestamp("created_on"));
			return systemMessage;
		}
	};

	private static final String SAVE_SQL = "insert into system_message(from_user_id, receiver_id, type, seek_id, offer_id, delegation_id, evaluation_id, title, content) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(SystemMessage systemMessage) {
		systemMessage.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			JdbcDaoUtils.setInteger(statement, 1, systemMessage.getFromUserId());
			statement.setInt(2, systemMessage.getReceiverId());
			statement.setString(3, systemMessage.getType());
			JdbcDaoUtils.setLong(statement, 4, systemMessage.getSeekId());
			JdbcDaoUtils.setLong(statement, 5, systemMessage.getOfferId());
			JdbcDaoUtils.setLong(statement, 6, systemMessage.getDelegationId());
			JdbcDaoUtils.setLong(statement, 7, systemMessage.getEvaluationId());
			statement.setString(8, systemMessage.getTitle());
			statement.setString(9, systemMessage.getContent());

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				systemMessage.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", systemMessage);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from system_message where id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_SYS_MSG_ID_TO_SYS_MSG, key = "${id}")
	public SystemMessage getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String UPDATE_READ_SQL = "update system_message set is_read=true where id=?";

	/**
	 * 更新为已读状态
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_SYS_MSG_ID_TO_SYS_MSG, key = "${id}")
	public void updateRead(Long id) {
		super.getJdbcTemplate().update(UPDATE_READ_SQL, id);
	}

	private static final String LIST_BY_USER_ID_SQL = "select id from system_message where receiver_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的系统消息ID列表
	 */
	@Override
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_SQL, LONG_ROW_MAPPER, userId, pageNumber * pageSize,
				pageSize);
	}

	private static final String LIST_UNREAD_BY_USER_ID_SQL = "select id from system_message where receiver_id=? and !is_read order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的未读系统消息ID列表
	 */
	@Override
	public List<Long> listUnreadByUserId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_UNREAD_BY_USER_ID_SQL, LONG_ROW_MAPPER, userId,
				pageNumber * pageSize, pageSize);
	}

	private static final String GET_UNREAD_NUM_BY_USER_ID_SQL = "select count(id) from system_message where receiver_id=? and !is_read";

	/**
	 * 获取指定用户的未读系统消息数
	 */
	@Override
	public int getUnreadNumberByUserId(Integer userId) {
		Integer result = super.getJdbcTemplate().queryForObject(GET_UNREAD_NUM_BY_USER_ID_SQL, INTEGER_ROW_MAPPER,
				userId);
		return result == null ? 0 : result;
	}

}
