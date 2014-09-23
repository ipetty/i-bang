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
import net.ipetty.ibang.model.Evaluation;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * EvaluationDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Repository("evaluationDao")
public class EvaluationDaoImpl extends BaseJdbcDaoSupport implements EvaluationDao {

	static final RowMapper<Evaluation> ROW_MAPPER = new RowMapper<Evaluation>() {
		@Override
		public Evaluation mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, delegation_id, type, evaluator_id, evaluate_target_id, point,
			// content, created_on
			Evaluation evaluation = new Evaluation();
			evaluation.setId(rs.getLong("id"));
			evaluation.setDelegationId(rs.getLong("delegation_id"));
			evaluation.setType(rs.getString("type"));
			evaluation.setEvaluatorId(rs.getInt("evaluator_id"));
			evaluation.setEvaluateTargetId(rs.getInt("evaluate_target_id"));
			evaluation.setPoint(rs.getInt("point"));
			evaluation.setContent(rs.getString("content"));
			evaluation.setCreatedOn(rs.getTimestamp("created_on"));
			return evaluation;
		}
	};

	private static final String SAVE_SQL = "insert into evaluation(delegation_id, type, evaluator_id, evaluate_target_id, point, content) values(?, ?, ?, ?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(Evaluation evaluation) {
		evaluation.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1, evaluation.getDelegationId());
			statement.setString(2, evaluation.getType());
			statement.setInt(3, evaluation.getEvaluatorId());
			statement.setInt(4, evaluation.getEvaluateTargetId());
			statement.setInt(5, evaluation.getPoint());
			statement.setString(6, evaluation.getContent());

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				evaluation.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", evaluation);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from evaluation where id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_EVALUATION_ID_TO_EVALUATION, key = "${id}")
	public Evaluation getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String LIST_BY_EVALUATOR_ID_SQL = "select id from evaluation where evaluator_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户给出的评价ID列表
	 */
	public List<Long> listByEvaluatorId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_EVALUATOR_ID_SQL, LONG_ROW_MAPPER, userId, pageNumber * pageSize,
				pageSize);
	}

	private static final String LIST_BY_EVALUATE_TARGET_ID_SQL = "select id from evaluation where evaluate_target_id=? order by created_on desc limit ?,?";

	/**
	 * 获取指定用户获得的评价ID列表
	 */
	@Override
	public List<Long> listByEvaluateTargetId(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_EVALUATE_TARGET_ID_SQL, LONG_ROW_MAPPER, userId,
				pageNumber * pageSize, pageSize);
	}

}
