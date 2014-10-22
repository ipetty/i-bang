package net.ipetty.ibang.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.annotation.LoadFromCache;
import net.ipetty.ibang.cache.annotation.UpdateToCache;
import net.ipetty.ibang.cache.annotation.UpdatesToCache;
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.User;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * UserDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Repository("userDao")
public class UserDaoImpl extends BaseJdbcDaoSupport implements UserDao {

	static final RowMapper<User> ROW_MAPPER = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, username, email, password, salt, nickname, gender, job,
			// phone, telephone, avatar, signature, province, city, district,
			// address, created_on, version
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setUsername(rs.getString("username"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setSalt(rs.getString("salt"));
			user.setNickname(rs.getString("nickname"));
			user.setGender(rs.getString("gender"));
			user.setJob(rs.getString("job"));
			user.setPhone(rs.getString("phone"));
			user.setTelephone(rs.getString("telephone"));
			user.setAvatar(rs.getString("avatar"));
			user.setSignature(rs.getString("signature"));
			user.setProvince(rs.getString("province"));
			user.setCity(rs.getString("city"));
			user.setDistrict(rs.getString("district"));
			user.setAddress(rs.getString("address"));
			user.setCreatedOn(rs.getTimestamp("created_on"));
			return user;
		}
	};

	private static final String SAVE_SQL = "insert into users(username, email, password, salt, nickname, gender, job, phone, telephone, avatar, signature, province, city, district, address, created_on) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存用户帐号
	 */
	@Override
	public void save(User user) {
		user.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getSalt());
			statement.setString(5, user.getNickname());
			statement.setString(6, user.getGender());
			statement.setString(7, user.getJob());
			statement.setString(8, user.getPhone());
			statement.setString(9, user.getTelephone());
			statement.setString(10, user.getAvatar());
			statement.setString(11, user.getSignature());
			statement.setString(12, user.getProvince());
			statement.setString(13, user.getCity());
			statement.setString(14, user.getDistrict());
			statement.setString(15, user.getAddress());
			statement.setTimestamp(16, new Timestamp(user.getCreatedOn().getTime()));

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				user.setId(rs.getInt(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", user);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from users where id=?";

	/**
	 * 根据ID获取用户帐号
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_ID_TO_USER, key = "${id}")
	public User getById(Integer id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String GET_USERID_BY_USERNAME_SQL = "select id from users where username=?";

	/**
	 * 根据帐号获取用户ID
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USERNAME_TO_USER_ID, key = "${username}")
	public Integer getUserIdByUsername(String username) {
		return super.queryUniqueEntity(GET_USERID_BY_USERNAME_SQL, INTEGER_ROW_MAPPER, username);
	}

	private static final String GET_USERID_BY_EMAIL_SQL = "select id from users where email=?";

	/**
	 * 根据邮箱获取用户ID
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_EMAIL_TO_USER_ID, key = "${email}")
	public Integer getUserIdByEmail(String email) {
		return super.queryUniqueEntity(GET_USERID_BY_EMAIL_SQL, INTEGER_ROW_MAPPER, email);
	}

	private static final String GET_USERID_BY_LOGIN_NAME_SQL = "select id from users where username=? or email=?";

	/**
	 * 根据登录帐号或邮箱获取用户ID
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_LOGIN_NAME_TO_USER_ID, key = "${loginName}")
	public Integer getUserIdByLoginName(String loginName) {
		return super.queryUniqueEntity(GET_USERID_BY_LOGIN_NAME_SQL, INTEGER_ROW_MAPPER, loginName, loginName);
	}

	private static final String UPDATE_USER_SQL = "update users set nickname=?, gender=?, job=?, phone=?, telephone=?, avatar=?, signature=?, province=?, city=?, district=?, address=?, version=version+1 where id=?";

	/**
	 * 更新用户帐号信息
	 */
	@Override
	@UpdatesToCache({ @UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_USER, key = "${user.id}"),
			@UpdateToCache(mapName = CacheConstants.CACHE_USERNAME_TO_USER_ID, key = "${user.username}"),
			@UpdateToCache(mapName = CacheConstants.CACHE_EMAIL_TO_USER_ID, key = "${user.email}"),
			@UpdateToCache(mapName = CacheConstants.CACHE_LOGIN_NAME_TO_USER_ID, key = "${user.username}"),
			@UpdateToCache(mapName = CacheConstants.CACHE_LOGIN_NAME_TO_USER_ID, key = "${user.email}") })
	public void update(User user) {
		super.getJdbcTemplate().update(UPDATE_USER_SQL, user.getNickname(), user.getGender(), user.getJob(),
				user.getPhone(), user.getTelephone(), user.getAvatar(), user.getSignature(), user.getProvince(),
				user.getCity(), user.getDistrict(), user.getAddress(), user.getId());
		logger.debug("updated {}", user);
	}

	private static final String UPDATE_EMAIL_SQL = "update users set email=?, version=version+1 where id=?";

	/**
	 * 更新邮箱
	 */
	@Override
	@UpdatesToCache({ @UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_USER, key = "${id}"),
			@UpdateToCache(mapName = CacheConstants.CACHE_EMAIL_TO_USER_ID, key = "${email}") })
	public void updateEmail(Integer id, String email) {
		super.getJdbcTemplate().update(UPDATE_EMAIL_SQL, email, id);
		logger.debug("updated email for user({}), email is {}", id, email);
	}

	private static final String CHANGE_PASSWORD_SQL = "update users set password=? where id=?";

	/**
	 * 修改密码
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_USER, key = "${id}")
	public void changePassword(Integer id, String newEncodedPassword) {
		super.getJdbcTemplate().update(CHANGE_PASSWORD_SQL, newEncodedPassword, id);
		logger.debug("changed password for user({})", id);
	}

	private static final String CHANGE_PASSWORD_WITH_SALT_SQL = "update users set password=?, salt=? where id=?";

	/**
	 * 修改密码与盐值
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_USER, key = "${id}")
	public void changePassword(Integer id, String newEncodedPassword, String salt) {
		super.getJdbcTemplate().update(CHANGE_PASSWORD_WITH_SALT_SQL, newEncodedPassword, salt, id);
		logger.debug("changed password and salt for user({})", id);
	}

}
