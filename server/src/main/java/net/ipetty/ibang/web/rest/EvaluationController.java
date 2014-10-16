package net.ipetty.ibang.web.rest;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Evaluation;
import net.ipetty.ibang.service.EvaluationService;
import net.ipetty.ibang.vo.EvaluationVO;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * EvaluationController
 * 
 * @author luocanfeng
 * @date 2014年10月16日
 */
@Controller
public class EvaluationController extends BaseController {

	@Resource
	private EvaluationService evaluationService;

	/**
	 * 评价
	 */
	@RequestMapping(value = "/evaluate", method = RequestMethod.POST)
	public EvaluationVO evaluate(@RequestBody EvaluationVO evaluation) {
		Assert.notNull(evaluation, "评价不能为空");
		Assert.notNull(evaluation.getDelegationId(), "对应的委托单不能为空");
		Assert.notNull(evaluation.getType(), "评价类型不能为空");
		Assert.notNull(evaluation.getEvaluatorId(), "评价发起人不能为空");
		Assert.notNull(evaluation.getEvaluateTargetId(), "评价对象人不能为空");

		Evaluation e = Evaluation.fromVO(evaluation);
		evaluationService.evaluate(e);
		e = evaluationService.getById(e.getId());

		return e.toVO();
	}

	/**
	 * 获取
	 */
	@RequestMapping(value = "/evaluation", method = RequestMethod.GET)
	public EvaluationVO getById(Long id) {
		Assert.notNull(id, "评价ID不能为空");

		return evaluationService.getById(id).toVO();
	}

	/**
	 * 获取委托对应的评价
	 */
	@RequestMapping(value = "/evaluationlist/bydelegation", method = RequestMethod.GET)
	public List<EvaluationVO> listByDelegationId(Long delegationId) {
		Assert.notNull(delegationId, "委托单ID不能为空");

		List<Evaluation> evaluations = evaluationService.listByDelegationId(delegationId);
		return Evaluation.listToVoList(evaluations);
	}

	/**
	 * 获取指定用户给出的评价列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/evaluationlist/byevaluator", method = RequestMethod.GET)
	public List<EvaluationVO> listByEvaluatorId(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<Evaluation> evaluations = evaluationService.listByEvaluatorId(userId, pageNumber, pageSize);
		return Evaluation.listToVoList(evaluations);
	}

	/**
	 * 获取指定用户获得的评价列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/evaluationlist/byevaluatetarget", method = RequestMethod.GET)
	public List<EvaluationVO> listByEvaluateTargetId(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<Evaluation> evaluations = evaluationService.listByEvaluateTargetId(userId, pageNumber, pageSize);
		return Evaluation.listToVoList(evaluations);
	}

}
