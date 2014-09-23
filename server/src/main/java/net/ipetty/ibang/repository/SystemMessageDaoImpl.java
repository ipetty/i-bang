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
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.SystemMessage;

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
			// id, from_user_id, receiver_id, type, content, created_on
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setId(rs.getLong("id"));
			systemMessage.setFromUserId(rs.getInt("from_user_id"));
			systemMessage.setReceiverId(rs.getInt("receiver_id"));
			systemMessage.setType(rs.getString("type"));
			systemMessage.setContent(rs.getString("content"));
			systemMessage.setCreatedOn(rs.getTimestamp("created_on"));
			return systemMessage;
		}
	};

	private static final String SAVE_SQL = "insert into system_message(from_user_id, receiver_id, type, content) values(?, ?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(SystemMessage systemMessage) {
		systemMessage.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, systemMessage.getFromUserId());
			statement.setInt(2, systemMessage.getReceiverId());
			statement.setString(3, systemMessage.getType());
			statement.setString(4, systemMessage.getContent());

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

	private static final String LIST_BY_USER_ID_SQL = "select id from system_message where receiver_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的系统消息ID列表
	 */
	@Override
	public List<Long> listByUserId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_SQL, LONG_ROW_MAPPER, userId, pageNumber * pageSize,
				pageSize);
	}

}
