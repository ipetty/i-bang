package net.ipetty.ibang.android.evaluation;

import java.util.List;

import net.ipetty.ibang.vo.EvaluationVO;

/**
 * EvaluationForm
 * 
 * @author luocanfeng
 * @date 2014年10月28日
 */
public class EvaluationForm {

	private EvaluationVO evaluation;
	private List<String> images;

	public EvaluationForm() {
		super();
	}

	public EvaluationForm(EvaluationVO evaluation, List<String> images) {
		super();
		this.evaluation = evaluation;
		this.images = images;
	}

	public EvaluationVO getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(EvaluationVO evaluation) {
		this.evaluation = evaluation;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

}
