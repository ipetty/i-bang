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
import net.ipetty.ibang.model.Image;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * ImageDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
@Repository("imageDao")
public class ImageDaoImpl extends BaseJdbcDaoSupport implements ImageDao {

	static final RowMapper<Image> ROW_MAPPER = new RowMapper<Image>() {
		@Override
		public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, sn, seek_id, idx, small_url, original_url
			Image image = new Image();
			image.setId(rs.getLong("id"));
			image.setSn(rs.getString("sn"));
			image.setSmallUrl(rs.getString("small_url"));
			image.setOriginalUrl(rs.getString("original_url"));
			return image;
		}
	};

	private static final String SAVE_SQL = "insert into image(sn, small_url, original_url) values(?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(Image image) {
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, image.getSn());
			statement.setString(2, image.getSmallUrl());
			statement.setString(3, image.getOriginalUrl());

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				image.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", image);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String UPDATE_SQL = "update image set seek_id=?, idx=? where id=?";

	/**
	 * 将图片与求助单相关联
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_SEEK_ID_TO_IMAGE_IDS, key = "${seekId}")
	public void saveImageToSeek(final Long seekId, final List<Long> imageIds) {
		super.getJdbcTemplate().batchUpdate(UPDATE_SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, seekId);
				ps.setInt(2, i);
				ps.setLong(3, imageIds.get(i));
			}

			@Override
			public int getBatchSize() {
				return imageIds.size();
			}
		});
	}

	private static final String GET_BY_ID_SQL = "select * from image where id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_ID_TO_SEEKER_INFO, key = "${id}")
	public Image getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String LIST_BY_SEEK_ID_SQL = "select id from image where seek_id=? order by idx";

	/**
	 * 获取指定求助单的图片ID列表
	 */
	@LoadFromCache(mapName = CacheConstants.CACHE_SEEK_ID_TO_IMAGE_IDS, key = "${seekId}")
	public List<Long> listBySeekId(Long seekId) {
		return super.getJdbcTemplate().query(LIST_BY_SEEK_ID_SQL, LONG_ROW_MAPPER, seekId);
	}

}
