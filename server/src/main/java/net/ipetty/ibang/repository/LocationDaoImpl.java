package net.ipetty.ibang.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.annotation.LoadFromCache;
import net.ipetty.ibang.cache.annotation.UpdateToCache;
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.Location;
import net.ipetty.ibang.util.JdbcDaoUtils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * LocationDaoImpl
 * @author luocanfeng
 * @date 2014年5月8日
 */
@Repository("locationDao")
public class LocationDaoImpl extends BaseJdbcDaoSupport implements LocationDao {

	static final RowMapper<Location> ROW_MAPPER = new RowMapper<Location>() {

		@Override
		public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, longitude, latitude, geo_hash, coor_type, radius, province,
			// city, district, street, street_number, address, silent, lbs_id
			Location location = new Location();
			location.setId(rs.getLong("id"));
			location.setLongitude(JdbcDaoUtils.getDouble(rs, "longitude"));
			location.setLatitude(JdbcDaoUtils.getDouble(rs, "latitude"));
			location.setGeoHash(rs.getString("geo_hash"));
			location.setCoorType(rs.getString("coor_type"));
			location.setRadius(JdbcDaoUtils.getFloat(rs, "radius"));
			location.setProvince(rs.getString("province"));
			location.setCity(rs.getString("city"));
			location.setDistrict(rs.getString("district"));
			location.setStreet(rs.getString("street"));
			location.setStreetNumber(rs.getString("street_number"));
			location.setAddress(rs.getString("address"));
			location.setSilent(rs.getBoolean("silent"));
			location.setLbsId(rs.getString("lbs_id"));
			return location;
		}
	};

	private static final String SAVE_SQL = "insert into location(longitude, latitude, geo_hash, coor_type, radius, province, city, district, street, street_number, address, silent, lbs_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存位置信息
	 */
	@Override
	public void save(Location location) {
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			JdbcDaoUtils.setDouble(statement, 1, location.getLongitude());
			JdbcDaoUtils.setDouble(statement, 2, location.getLatitude());
			statement.setString(3, location.getGeoHash());
			statement.setString(4, location.getCoorType());
			JdbcDaoUtils.setFloat(statement, 5, location.getRadius());
			statement.setString(6, location.getProvince());
			statement.setString(7, location.getCity());
			statement.setString(8, location.getDistrict());
			statement.setString(9, location.getStreet());
			statement.setString(10, location.getStreetNumber());
			statement.setString(11, location.getAddress());
			statement.setBoolean(12, location.isSilent());
			statement.setString(13, location.getLbsId());
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				location.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", location);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from location where id=?";

	/**
	 * 根据ID获取位置信息
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_LOCATION_ID_TO_LOCATION, key = "${id}")
	public Location getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String SET_LBS_ID_SQL = "update location set lbs_id=? where id=?";

	/**
	 * 设置lbsId字段信息
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_LOCATION_ID_TO_LOCATION, key = "${id}")
	public void setLbsId(Long id, String lbsId) {
		super.getJdbcTemplate().update(SET_LBS_ID_SQL, lbsId, id);
	}

	private static final String LIST_BY_SEEK_IDS_SQL = "select l.* from location l inner join (select location_id from seek where id in (?)) lid on l.id=lid.location_id";

	/**
	 * 获取指定求助列表的位置信息列表
	 */
	@Override
	public List<Location> listBySeekIds(Long... seekIds) {
		String inStatement = seekIds.length > 0 ? StringUtils.arrayToCommaDelimitedString(seekIds) : "null";
		return super.getJdbcTemplate().query(LIST_BY_SEEK_IDS_SQL.replace("?", inStatement), ROW_MAPPER);
	}

}
