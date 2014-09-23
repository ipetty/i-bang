package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Delegation;
import net.ipetty.ibang.repository.DelegationDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * DelegationService
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Service
@Transactional
public class DelegationService extends BaseService {

	@Resource
	private DelegationDao delegationDao;

	/**
	 * 生成委托
	 */
	public void save(Delegation delegation) {
		Assert.notNull(delegation, "委托单不能为空");

		// 生成序列号

		// 状态

		delegationDao.save(delegation);
	}

	/**
	 * 获取
	 */
	public Delegation getById(Long id) {
		return delegationDao.getById(id);
	}

	/**
	 * 获取指定用户的委托列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Delegation> listByUserId(Integer userId, int pageNumber, int pageSize) {
		List<Long> delegationIds = delegationDao.listByUserId(userId, pageNumber, pageSize);
		List<Delegation> delegations = new ArrayList<Delegation>();
		for (Long delegationId : delegationIds) {
			delegations.add(delegationDao.getById(delegationId));
		}
		return delegations;
	}

	/**
	 * 获取指定用户的指定状态的委托列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Delegation> listByUserIdAndStatus(Integer userId, String status, int pageNumber, int pageSize) {
		List<Long> delegationIds = delegationDao.listByUserIdAndStatus(userId, status, pageNumber, pageSize);
		List<Delegation> delegations = new ArrayList<Delegation>();
		for (Long delegationId : delegationIds) {
			delegations.add(delegationDao.getById(delegationId));
		}
		return delegations;
	}

	/**
	 * 更新求助单状态
	 */
	public void updateStatus(Long delegationId, String newStatus) {
		delegationDao.updateStatus(delegationId, newStatus);
	}

}
