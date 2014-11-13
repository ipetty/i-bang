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
import net.ipetty.ibang.util.JdbcDaoUtils;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.SeekCategory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * SeekDaoImpl
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Repository("seekDao")
public class SeekDaoImpl extends BaseJdbcDaoSupport implements SeekDao {

	static final RowMapper<Seek> ROW_MAPPER = new RowMapper<Seek>() {

		@Override
		public Seek mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, sn, seeker_id, contact_info_visible, category_l1,
			// category_l2, type, title, content, requirement, delegate_number,
			// reward, additional_reward, service_date, province, city,
			// district, address, location_id, created_on, expire_date,
			// closed_on, status
			Seek seek = new Seek();
			seek.setId(rs.getLong("id"));
			seek.setSn(rs.getString("sn"));
			seek.setSeekerId(rs.getInt("seeker_id"));
			seek.setContactInfoVisible(rs.getBoolean("contact_info_visible"));
			seek.setCategoryL1(rs.getString("category_l1"));
			seek.setCategoryL2(rs.getString("category_l2"));
			seek.setType(rs.getString("type"));
			seek.setTitle(rs.getString("title"));
			seek.setContent(rs.getString("content"));
			seek.setRequirement(rs.getString("requirement"));
			seek.setDelegateNumber(rs.getInt("delegate_number"));
			seek.setReward(rs.getString("reward"));
			seek.setAdditionalReward(rs.getString("additional_reward"));
			seek.setServiceDate(rs.getString("service_date"));
			seek.setProvince(rs.getString("province"));
			seek.setCity(rs.getString("city"));
			seek.setDistrict(rs.getString("district"));
			seek.setAddress(rs.getString("address"));
			seek.setLocationId(JdbcDaoUtils.getLong(rs, "location_id"));
			seek.setCreatedOn(rs.getTimestamp("created_on"));
			seek.setExipireDate(rs.getDate("expire_date"));
			seek.setClosedOn(rs.getTimestamp("closed_on"));
			seek.setStatus(rs.getString("status"));
			return seek;
		}
	};

	private static final String SAVE_SQL = "insert into seek(sn, seeker_id, contact_info_visible, category_l1, category_l2, type,"
			+ " title, content, requirement, delegate_number, reward, additional_reward, service_date, province, city, district, address, location_id, expire_date, status)"
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
			statement.setString(6, seek.getType());
			statement.setString(7, seek.getTitle());
			statement.setString(8, seek.getContent());
			statement.setString(9, seek.getRequirement());
			statement.setInt(10, seek.getDelegateNumber());
			statement.setString(11, seek.getReward());
			statement.setString(12, seek.getAdditionalReward());
			statement.setString(13, seek.getServiceDate());
			statement.setString(14, seek.getProvince());
			statement.setString(15, seek.getCity());
			statement.setString(16, seek.getDistrict());
			statement.setString(17, seek.getAddress());
			JdbcDaoUtils.setLong(statement, 18, seek.getLocationId());
			statement.setDate(19, seek.getExipireDate() != null ? new java.sql.Date(seek.getExipireDate().getTime())
					: null);
			statement.setString(20, seek.getStatus());

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

	private static final String FRAGMENT_SELECT_FROM_WHERE = "select id from seek where type=?";
	private static final String FRAGMENT_TIME_STATUS_ORDER_LIMIT = " and enable and created_on<=? and (status=? or status=?) order by created_on desc limit ?,?";

	private static final String LIST_LATEST_SQL = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_TIME_STATUS_ORDER_LIMIT;

	/**
	 * 获取最新的未关闭求助单ID列表
	 * @param pageNumber 分页页码，从0开始
	 */
	@Override
	public List<Long> listLatest(String type, Date timeline, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_LATEST_SQL, LONG_ROW_MAPPER, type, timeline,
				Constants.SEEK_STATUS_CREATED, Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
	}

	private static final String FRAGMENT_CATEGORY_L1 = " and category_l1=?";
	private static final String FRAGMENT_CATEGORY_L12 = FRAGMENT_CATEGORY_L1 + " and category_l2=?";

	private static final String LIST_LATEST_BY_CATEGORYL1_SQL = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_CATEGORY_L1
			+ FRAGMENT_TIME_STATUS_ORDER_LIMIT;
	private static final String LIST_LATEST_BY_CATEGORYL12_SQL = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_CATEGORY_L12
			+ FRAGMENT_TIME_STATUS_ORDER_LIMIT;

	/**
	 * 获取指定分类中最新的未关闭求助单ID列表
	 * @param pageNumber 分页页码，从0开始
	 */
	@Override
	public List<Long> listLatestByCategory(String type, String categoryL1, String categoryL2, Date timeline,
			int pageNumber, int pageSize) {
		if (StringUtils.isBlank(categoryL1)) {
			return listLatest(type, timeline, pageNumber, pageSize);
		} else if (StringUtils.isBlank(categoryL2)) {
			return super.getJdbcTemplate().query(LIST_LATEST_BY_CATEGORYL1_SQL, LONG_ROW_MAPPER, type, categoryL1,
					timeline, Constants.SEEK_STATUS_CREATED, Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize,
					pageSize);
		} else {
			return super.getJdbcTemplate().query(LIST_LATEST_BY_CATEGORYL12_SQL, LONG_ROW_MAPPER, type, categoryL1,
					categoryL2, timeline, Constants.SEEK_STATUS_CREATED, Constants.SEEK_STATUS_OFFERED,
					pageNumber * pageSize, pageSize);
		}
	}

	// private static final String FRAGMENT_PROVINCE = " and province=?";
	private static final String FRAGMENT_CITY = " and city=?";
	private static final String FRAGMENT_DISTRICT = FRAGMENT_CITY + " and district=?";

	private static final String LIST_LATEST_BY_CITY_SQL = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_CITY
			+ FRAGMENT_TIME_STATUS_ORDER_LIMIT;
	private static final String LIST_LATEST_BY_CITY_AND_CATEGORYL1_SQL = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_CITY
			+ FRAGMENT_CATEGORY_L1 + FRAGMENT_TIME_STATUS_ORDER_LIMIT;
	private static final String LIST_LATEST_BY_CITY_AND_CATEGORYL12_SQL = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_CITY
			+ FRAGMENT_CATEGORY_L12 + FRAGMENT_TIME_STATUS_ORDER_LIMIT;

	private static final String LIST_LATEST_BY_DISTRICT_SQL = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_DISTRICT
			+ FRAGMENT_TIME_STATUS_ORDER_LIMIT;
	private static final String LIST_LATEST_BY_DISTRICT_AND_CATEGORYL1_SQL = FRAGMENT_SELECT_FROM_WHERE
			+ FRAGMENT_DISTRICT + FRAGMENT_CATEGORY_L1 + FRAGMENT_TIME_STATUS_ORDER_LIMIT;
	private static final String LIST_LATEST_BY_DISTRICT_AND_CATEGORYL2_SQL = FRAGMENT_SELECT_FROM_WHERE
			+ FRAGMENT_DISTRICT + FRAGMENT_CATEGORY_L12 + FRAGMENT_TIME_STATUS_ORDER_LIMIT;

	/**
	 * 获取所在城市指定分类中最新的未关闭求助ID列表
	 * @param pageNumber 分页页码，从0开始
	 */
	@Override
	public List<Long> listLatestByCityOrCategory(String type, String city, String district, String categoryL1,
			String categoryL2, Date timeline, int pageNumber, int pageSize) {
		if (StringUtils.isBlank(city)) {
			return listLatestByCategory(type, categoryL1, categoryL2, timeline, pageNumber, pageSize);
		} else if (StringUtils.isBlank(district)) {
			if (StringUtils.isBlank(categoryL1)) {
				return super.getJdbcTemplate().query(LIST_LATEST_BY_CITY_SQL, LONG_ROW_MAPPER, type, city, timeline,
						Constants.SEEK_STATUS_CREATED, Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
			} else if (StringUtils.isBlank(categoryL2)) {
				return super.getJdbcTemplate().query(LIST_LATEST_BY_CITY_AND_CATEGORYL1_SQL, LONG_ROW_MAPPER, type,
						city, categoryL1, timeline, Constants.SEEK_STATUS_CREATED, Constants.SEEK_STATUS_OFFERED,
						pageNumber * pageSize, pageSize);
			} else {
				return super.getJdbcTemplate().query(LIST_LATEST_BY_CITY_AND_CATEGORYL12_SQL, LONG_ROW_MAPPER, type,
						city, categoryL1, categoryL2, timeline, Constants.SEEK_STATUS_CREATED,
						Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
			}
		} else {
			if (StringUtils.isBlank(categoryL1)) {
				return super.getJdbcTemplate().query(LIST_LATEST_BY_DISTRICT_SQL, LONG_ROW_MAPPER, type, city,
						district, timeline, Constants.SEEK_STATUS_CREATED, Constants.SEEK_STATUS_OFFERED,
						pageNumber * pageSize, pageSize);
			} else if (StringUtils.isBlank(categoryL2)) {
				return super.getJdbcTemplate().query(LIST_LATEST_BY_DISTRICT_AND_CATEGORYL1_SQL, LONG_ROW_MAPPER, type,
						city, district, categoryL1, timeline, Constants.SEEK_STATUS_CREATED,
						Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
			} else {
				return super.getJdbcTemplate().query(LIST_LATEST_BY_DISTRICT_AND_CATEGORYL2_SQL, LONG_ROW_MAPPER, type,
						city, district, categoryL1, categoryL2, timeline, Constants.SEEK_STATUS_CREATED,
						Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
			}
		}
	}

	private static final String FRAGMENT_SELECT_FROM_WHERE_CITY = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_CITY;
	private static final String FRAGMENT_SELECT_FROM_WHERE_DISTRICT = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_DISTRICT;

	/**
	 * 获取所在城市指定用户帮忙范围内的最新未关闭求助列表
	 * @param pageNumber 分页页码，从0开始
	 */
	@Override
	public List<Long> listLatestByCityAndOfferRange(String type, String city, String district,
			List<SeekCategory> offerRange, Date timeline, int pageNumber, int pageSize) {
		String offerRangeClause = populateOfferRangeClause(offerRange);
		if (StringUtils.isBlank(city)) {
			return super.getJdbcTemplate().query(
					FRAGMENT_SELECT_FROM_WHERE + offerRangeClause + FRAGMENT_TIME_STATUS_ORDER_LIMIT, LONG_ROW_MAPPER,
					type, timeline, Constants.SEEK_STATUS_CREATED, Constants.SEEK_STATUS_OFFERED,
					pageNumber * pageSize, pageSize);
		} else if (StringUtils.isBlank(district)) {
			return super.getJdbcTemplate().query(
					FRAGMENT_SELECT_FROM_WHERE_CITY + offerRangeClause + FRAGMENT_TIME_STATUS_ORDER_LIMIT,
					LONG_ROW_MAPPER, type, city, timeline, Constants.SEEK_STATUS_CREATED,
					Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
		} else {
			return super.getJdbcTemplate().query(
					FRAGMENT_SELECT_FROM_WHERE_DISTRICT + offerRangeClause + FRAGMENT_TIME_STATUS_ORDER_LIMIT,
					LONG_ROW_MAPPER, type, city, district, timeline, Constants.SEEK_STATUS_CREATED,
					Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
		}
	}

	private String populateOfferRangeClause(List<SeekCategory> offerRange) {
		StringBuffer sb = new StringBuffer("and (");
		for (int i = 0; i < offerRange.size(); i++) {
			SeekCategory category = offerRange.get(i);
			if (category != null) {
				if (i != 0) {
					sb.append(" or ");
				}

				String categoryL1 = category.getCategoryL1();
				String categoryL2 = category.getCategoryL2();
				if (StringUtils.isNotBlank(categoryL1)) {
					sb.append("(category_l1='").append(categoryL1);
					if (StringUtils.isNotBlank(categoryL2)) {
						sb.append("' and category_l2='").append(categoryL2);
					}
					sb.append("')");
				}
			}
		}
		sb.append(")");
		return sb.toString();
	}

	private static final String FRAGMENT_KEYWORD = " and (title like ? or content like ? or requirement like ? or reward like ? or additional_reward like ?)";
	private static final String LIST_LATEST_BY_KEYWORD_SQL = FRAGMENT_SELECT_FROM_WHERE + FRAGMENT_KEYWORD
			+ FRAGMENT_TIME_STATUS_ORDER_LIMIT;

	/**
	 * 根据关键字搜索最新的未关闭求助单ID列表
	 * @param pageNumber 分页页码，从0开始
	 */
	@Override
	public List<Long> listLatestByKeyword(String type, String keyword, Date timeline, int pageNumber, int pageSize) {
		if (StringUtils.isEmpty(keyword)) {
			return listLatest(type, timeline, pageNumber, pageSize);
		}
		String likeStatement = "%" + keyword + "%";
		return super.getJdbcTemplate().query(LIST_LATEST_BY_KEYWORD_SQL, LONG_ROW_MAPPER, type, likeStatement,
				likeStatement, likeStatement, likeStatement, likeStatement, timeline, Constants.SEEK_STATUS_CREATED,
				Constants.SEEK_STATUS_OFFERED, pageNumber * pageSize, pageSize);
	}

	private static final String LIST_BY_USER_ID_SQL = FRAGMENT_SELECT_FROM_WHERE
			+ " and enable and seeker_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户的求助单ID列表
	 * @param pageNumber 分页页码，从0开始
	 */
	@Override
	public List<Long> listByUserId(String type, Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_SQL, LONG_ROW_MAPPER, type, userId, pageNumber * pageSize,
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

	private static final String DISABLE_SQL = "update seek set enable=false, disabled_on=now() where id=?";

	/**
	 * 禁用/软删除
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_SEEK_ID_TO_SEEK, key = "${seekId}")
	public void disable(Long seekId) {
		super.getJdbcTemplate().update(DISABLE_SQL, seekId);
	}

}
