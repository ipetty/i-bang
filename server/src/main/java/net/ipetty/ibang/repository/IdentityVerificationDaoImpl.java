package net.ipetty.ibang.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.cache.CacheConstants;
import net.ipetty.ibang.cache.annotation.LoadFromCache;
import net.ipetty.ibang.cache.annotation.UpdateToCache;
import net.ipetty.ibang.model.IdentityVerification;
import net.ipetty.ibang.util.JdbcDaoUtils;
import net.ipetty.ibang.vo.Constants;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 身份审核
 * @author luocanfeng
 * @date 2014年11月7日
 */
@Repository("identityVerificationDao")
public class IdentityVerificationDaoImpl extends BaseJdbcDaoSupport implements IdentityVerificationDao {

	static final RowMapper<IdentityVerification> ROW_MAPPER = new RowMapper<IdentityVerification>() {

		@Override
		public IdentityVerification mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id, real_name, id_number, id_card_front,
			// id_card_reverse_side, id_card_in_hand, description, submitted_on,
			// verifier_id, verify_info, verified_on, status
			IdentityVerification identityVerification = new IdentityVerification();
			identityVerification.setUserId(JdbcDaoUtils.getInteger(rs, "user_id"));
			identityVerification.setRealName(rs.getString("real_name"));
			identityVerification.setIdNumber(rs.getString("id_number"));
			identityVerification.setIdCardFront(rs.getString("id_card_front"));
			identityVerification.setIdCardReverseSide(rs.getString("id_card_reverse_side"));
			identityVerification.setIdCardInHand(rs.getString("id_card_in_hand"));
			identityVerification.setDescription(rs.getString("description"));
			identityVerification.setSubmittedOn(rs.getTimestamp("submitted_on"));
			identityVerification.setVerifierId(JdbcDaoUtils.getInteger(rs, "verifier_id"));
			identityVerification.setVerifyInfo(rs.getString("verify_info"));
			identityVerification.setVerifiedOn(rs.getTimestamp("verified_on"));
			identityVerification.setStatus(rs.getString("status"));
			return identityVerification;
		}
	};

	private static final String SAVE_OR_UPDATE_SQL = "replace into identity_verification(user_id, real_name, id_number, id_card_front, id_card_reverse_side, id_card_in_hand, description, submitted_on, verifier_id, status) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存/更新
	 */
	@Override
	public void save(IdentityVerification identityVerification) {
		super.getJdbcTemplate().update(SAVE_OR_UPDATE_SQL, identityVerification.getUserId(),
				identityVerification.getRealName(), identityVerification.getIdNumber(),
				identityVerification.getIdCardFront(), identityVerification.getIdCardReverseSide(),
				identityVerification.getIdCardInHand(), identityVerification.getDescription(),
				identityVerification.getSubmittedOn(), identityVerification.getVerifierId(),
				identityVerification.getStatus());
	}

	private static final String GET_BY_USER_ID_SQL = "select * from identity_verification where user_id=?";

	/**
	 * 获取
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_ID_TO_IDENTITY_VERIFICATION, key = "${userId}")
	public IdentityVerification getByUserId(Integer userId) {
		return super.queryUniqueEntity(GET_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

	private static final String VERIFY_SQL = "update identity_verification set verify_info=?, verified_on=?, status=? where user_id=?";

	/**
	 * 审核
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_IDENTITY_VERIFICATION, key = "${identityVerification.userId}")
	public void verify(IdentityVerification identityVerification) {
		if (identityVerification.getVerifiedOn() == null) {
			identityVerification.setVerifiedOn(new Date());
		}
		super.getJdbcTemplate().update(VERIFY_SQL, identityVerification.getVerifyInfo(),
				identityVerification.getVerifiedOn(), identityVerification.getStatus(),
				identityVerification.getUserId());
	}

	private static final String SET_VERIFIED_SQL = "update users set identity_verified=true where id=?";

	/**
	 * 将用户标记为已通过身份验证状态
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_ID_TO_USER, key = "${userId}")
	public void setVerified(Integer userId) {
		super.getJdbcTemplate().update(SET_VERIFIED_SQL, userId);
	}

	private static final String LIST_VERIFYING_SQL = "select user_id from identity_verification where status=? order by submitted_on asc limit ?,?";

	/**
	 * 获取待审核列表
	 */
	@Override
	public List<Integer> listVerifying(int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_VERIFYING_SQL, INTEGER_ROW_MAPPER,
				Constants.ID_VERIFICATION_VERIFYING, pageNumber * pageSize, pageSize);
	}

	private static final String LIST_SQL = "select user_id from identity_verification order by submitted_on asc limit ?,?";

	/**
	 * 获取身份审核列表
	 */
	@Override
	public List<Integer> list(int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_SQL, INTEGER_ROW_MAPPER, pageNumber * pageSize, pageSize);
	}

}
