package net.ipetty.ibang.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.annotation.LoadFromCache;
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.Letter;
import net.ipetty.ibang.util.JdbcDaoUtils;
import net.ipetty.ibang.vo.LetterContacts;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 站内信
 * @author luocanfeng
 * @date 2014年11月6日
 */
@Repository("letterDao")
public class LetterDaoImpl extends BaseJdbcDaoSupport implements LetterDao {

	static final RowMapper<Letter> ROW_MAPPER = new RowMapper<Letter>() {

		@Override
		public Letter mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, owner_id, cooperator_id, content, is_inbox, is_read,
			// created_on, read_on
			Letter letter = new Letter();
			letter.setId(rs.getLong("id"));
			letter.setOwnerId(JdbcDaoUtils.getInteger(rs, "owner_id"));
			letter.setCooperatorId(JdbcDaoUtils.getInteger(rs, "cooperator_id"));
			letter.setContent(rs.getString("content"));
			letter.setInbox(rs.getBoolean("is_inbox"));
			letter.setRead(rs.getBoolean("is_read"));
			letter.setCreatedOn(rs.getTimestamp("created_on"));
			letter.setReadOn(rs.getTimestamp("read_on"));
			return letter;
		}
	};

	static final RowMapper<LetterContacts> ROW_MAPPER_LETTER_CONTACTS = new RowMapper<LetterContacts>() {

		@Override
		public LetterContacts mapRow(ResultSet rs, int rowNum) throws SQLException {
			LetterContacts letterContacts = new LetterContacts();
			letterContacts.setUserId(JdbcDaoUtils.getInteger(rs, "cooperator_id"));
			letterContacts.setLatestContent(rs.getString("content"));
			letterContacts.setRead(rs.getBoolean("is_read"));
			letterContacts.setUnreadNumber(rs.getInt("unread_num"));
			return letterContacts;
		}
	};

	private static final String SAVE_SQL = "insert into letter(owner_id, cooperator_id, content, is_inbox, is_read, created_on) values(?, ?, ?, ?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(Letter letter) {
		if (letter.getCreatedOn() == null) {
			letter.setCreatedOn(new Date());
		}
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			JdbcDaoUtils.setInteger(statement, 1, letter.getOwnerId());
			JdbcDaoUtils.setInteger(statement, 2, letter.getCooperatorId());
			statement.setString(3, letter.getContent());
			statement.setBoolean(4, letter.isInbox());
			statement.setBoolean(5, letter.isRead());
			statement.setTimestamp(6, new Timestamp(letter.getCreatedOn().getTime()));

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				letter.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", letter);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from letter where id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_LETTER_ID_TO_LETTER, key = "${id}")
	public Letter getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String UPDATE_READ_SQL = "update letter set is_read=true, read_on=now() where owner_id=? and cooperator_id=? and !is_read";

	/**
	 * 将某联系人的所有站内信更新为已读状态
	 */
	@Override
	public void updateReadByContactUserId(Integer userId, Integer contactUserId) {
		super.getJdbcTemplate().update(UPDATE_READ_SQL, userId, contactUserId);
	}

	private static final String LIST_CONTACTS_BY_USER_ID_SQL = "select distinct cooperator_id, content, is_read, count(!is_read) as unread_num from letter where owner_id=? group by owner_id, cooperator_id order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的联系人列表
	 */
	@Override
	public List<LetterContacts> listContactsByUserId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_CONTACTS_BY_USER_ID_SQL, ROW_MAPPER_LETTER_CONTACTS, userId,
				pageNumber * pageSize, pageSize);
	}

	private static final String LIST_BY_USER_ID_CONTACT_USER_ID_SQL = "select id from letter where owner_id=? and cooperator_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户与某个联系人的站内信ID列表
	 */
	@Override
	public List<Long> listByUserIdAndContactUserId(Integer userId, Integer contactUserId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_CONTACT_USER_ID_SQL, LONG_ROW_MAPPER, userId,
				contactUserId, pageNumber * pageSize, pageSize);
	}

	private static final String GET_UNREAD_NUM_BY_USER_ID_SQL = "select count(id) from letter where owner_id=? and !is_read";

	/**
	 * 获取指定用户的未读站内信数
	 */
	@Override
	public int getUnreadNumberByUserId(Integer userId) {
		Integer result = super.getJdbcTemplate().queryForObject(GET_UNREAD_NUM_BY_USER_ID_SQL, INTEGER_ROW_MAPPER,
				userId);
		return result == null ? 0 : result;
	}

}
