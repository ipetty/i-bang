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
import net.ipetty.ibang.cache.annotation.UpdateToCache;
import net.ipetty.ibang.exception.BusinessException;
import net.ipetty.ibang.model.Report;
import net.ipetty.ibang.util.JdbcDaoUtils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 举报
 * @author luocanfeng
 * @date 2014年11月13日
 */
@Repository("reportDao")
public class ReportDaoImpl extends BaseJdbcDaoSupport implements ReportDao {

	static final RowMapper<Report> ROW_MAPPER = new RowMapper<Report>() {

		@Override
		public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, sn, type, seek_id, seek_type, offer_id, user_id, behave,
			// content, reporter_id, created_on, result, dealed_on
			Report report = new Report();
			report.setId(rs.getLong("id"));
			report.setSn(rs.getString("sn"));
			report.setType(rs.getString("type"));
			report.setSeekId(JdbcDaoUtils.getLong(rs, "seek_id"));
			report.setSeekType(rs.getString("seek_type"));
			report.setOfferId(JdbcDaoUtils.getLong(rs, "offer_id"));
			report.setUserId(JdbcDaoUtils.getInteger(rs, "user_id"));
			report.setBehave(rs.getString("behave"));
			report.setContent(rs.getString("content"));
			report.setReporterId(JdbcDaoUtils.getInteger(rs, "reporter_id"));
			report.setCreatedOn(rs.getTimestamp("created_on"));
			report.setResult(rs.getString("result"));
			report.setDealedOn(rs.getTimestamp("dealed_on"));
			return report;
		}
	};

	private static final String SAVE_SQL = "insert into report(sn, type, seek_id, seek_type, offer_id, user_id, behave, content, reporter_id, created_on) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 举报
	 */
	@Override
	public void save(Report report) {
		if (report.getCreatedOn() == null) {
			report.setCreatedOn(new Date());
		}
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, report.getSn());
			statement.setString(2, report.getType());
			JdbcDaoUtils.setLong(statement, 3, report.getSeekId());
			statement.setString(4, report.getSeekType());
			JdbcDaoUtils.setLong(statement, 5, report.getOfferId());
			JdbcDaoUtils.setInteger(statement, 6, report.getUserId());
			statement.setString(7, report.getBehave());
			statement.setString(8, report.getContent());
			JdbcDaoUtils.setInteger(statement, 9, report.getReporterId());
			statement.setTimestamp(10, new Timestamp(report.getCreatedOn().getTime()));

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				report.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", report);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from report where id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_REPORT_ID_TO_REPORT, key = "${id}")
	public Report getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String DEAL_SQL = "update report set result=?, dealed_on=now() where id=?";

	/**
	 * 处理举报
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_REPORT_ID_TO_REPORT, key = "${id}")
	public void deal(Long id, String result) {
		super.getJdbcTemplate().update(DEAL_SQL, result, id);
	}

	private static final String LIST_UNDEALED_SQL = "select id from report where dealed_on is null order by created_on desc limit ?,?";

	/**
	 * 获取未处理举报ID列表
	 */
	@Override
	public List<Long> listUndealed(int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_UNDEALED_SQL, LONG_ROW_MAPPER, pageNumber * pageSize, pageSize);
	}

	private static final String GET_UNDEALED_NUMBER_SQL = "select count(id) from report where dealed_on is null";

	/**
	 * 获取未处理举报数
	 */
	public int getUndealedNum() {
		Integer result = super.queryUniqueEntity(GET_UNDEALED_NUMBER_SQL, INTEGER_ROW_MAPPER);
		return result == null ? 0 : result;
	}

	private static final String LIST_DEALED_SQL = "select id from report where dealed_on is not null order by created_on desc limit ?,?";

	/**
	 * 获取已处理举报ID列表
	 */
	@Override
	public List<Long> listDealed(int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_DEALED_SQL, LONG_ROW_MAPPER, pageNumber * pageSize, pageSize);
	}

	private static final String GET_DEALED_NUMBER_SQL = "select count(id) from report where dealed_on is not null";

	/**
	 * 获取已处理举报数
	 */
	public int getDealedNum() {
		Integer result = super.queryUniqueEntity(GET_DEALED_NUMBER_SQL, INTEGER_ROW_MAPPER);
		return result == null ? 0 : result;
	}

	private static final String LIST_SQL = "select id from report order by created_on desc limit ?,?";

	/**
	 * 获取举报ID列表
	 */
	@Override
	public List<Long> list(int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_SQL, LONG_ROW_MAPPER, pageNumber * pageSize, pageSize);
	}

	private static final String GET_ALL_NUMBER_SQL = "select count(id) from report";

	/**
	 * 获取全部举报数
	 */
	public int getAllNum() {
		Integer result = super.queryUniqueEntity(GET_ALL_NUMBER_SQL, INTEGER_ROW_MAPPER);
		return result == null ? 0 : result;
	}

}
