package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Evaluation;
import net.ipetty.ibang.repository.EvaluationDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * EvaluationService
 * 
 * @author luocanfeng
 * @date 2014年9月23日
 */
@Service
@Transactional
public class EvaluationService extends BaseService {

	@Resource
	private EvaluationDao evaluationDao;

	/**
	 * 评价
	 */
	public void evaluate(Evaluation evaluation) {
		evaluationDao.save(evaluation);
	}

	/**
	 * 获取评价
	 */
	public Evaluation getById(Long id) {
		return evaluationDao.getById(id);
	}

	/**
	 * 获取指定用户给出的评价列表
	 */
	public List<Evaluation> listByEvaluatorId(Integer userId, int pageNumber, int pageSize) {
		List<Long> evaluationIds = evaluationDao.listByEvaluatorId(userId, pageNumber, pageSize);
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		for (Long evaluationId : evaluationIds) {
			evaluations.add(evaluationDao.getById(evaluationId));
		}
		return evaluations;
	}

	/**
	 * 获取指定用户获得的评价列表
	 */
	public List<Evaluation> listByEvaluateTargetId(Integer userId, int pageNumber, int pageSize) {
		List<Long> evaluationIds = evaluationDao.listByEvaluateTargetId(userId, pageNumber, pageSize);
		List<Evaluation> evaluations = new ArrayList<Evaluation>();
		for (Long evaluationId : evaluationIds) {
			evaluations.add(evaluationDao.getById(evaluationId));
		}
		return evaluations;
	}

}
