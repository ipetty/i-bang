package net.ipetty.ibang.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.ibang.model.Report;
import net.ipetty.ibang.repository.ReportDao;
import net.ipetty.ibang.vo.Constants;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 举报
 * @author luocanfeng
 * @date 2014年11月13日
 */
@Service
@Transactional
public class ReportService extends BaseService {

	@Resource
	private ReportDao reportDao;

	/**
	 * 举报
	 */
	public void report(Report report) {
		Assert.notNull(report, "举报对象不能为空");
		Assert.hasText(report.getType(), "举报内容类型不能为空");
		if (Constants.REPORT_TYPE_SEEK.equals(report.getType())) {
			Assert.notNull(report.getSeekId(), "要举报的求助ID不能为空");
			Assert.hasText(report.getSeekType(), "求助类型不能为空");
		} else if (Constants.REPORT_TYPE_OFFER.equals(report.getType())) {
			Assert.notNull(report.getOfferId(), "要举报的帮助ID不能为空");
		} else if (Constants.REPORT_TYPE_USER.equals(report.getType())) {
			Assert.notNull(report.getUserId(), "要举报的用户ID不能为空");
		}
		Assert.hasText(report.getBehave(), "举报行为类型不能为空");
		Assert.notNull(report.getReporterId(), "举报人不能为空");

		reportDao.save(report);
	}

	/**
	 * 获取
	 */
	public Report getById(Long id) {
		return reportDao.getById(id);
	}

	/**
	 * 处理举报
	 */
	public void deal(Long id, String result) {
		Assert.hasText(result, "举报处理结果不能为空");
		// TODO 屏蔽相应信息，或禁用用户
		reportDao.deal(id, result);
	}

	/**
	 * 获取未处理举报列表
	 */
	public List<Report> listUndealed(int pageNumber, int pageSize) {
		List<Long> idList = reportDao.listUndealed(pageNumber, pageSize);
		return this.idListToEntityList(idList);
	}

	/**
	 * 获取未处理举报数
	 */
	public int getUndealedNum() {
		return reportDao.getUndealedNum();
	}

	/**
	 * 获取已处理举报列表
	 */
	public List<Report> listDealed(int pageNumber, int pageSize) {
		List<Long> idList = reportDao.listDealed(pageNumber, pageSize);
		return this.idListToEntityList(idList);
	}

	/**
	 * 获取已处理举报数
	 */
	public int getDealedNum() {
		return reportDao.getDealedNum();
	}

	/**
	 * 获取举报列表
	 */
	public List<Report> list(int pageNumber, int pageSize) {
		List<Long> idList = reportDao.list(pageNumber, pageSize);
		return this.idListToEntityList(idList);
	}

	/**
	 * 获取全部举报数
	 */
	public int getAllNum() {
		return reportDao.getAllNum();
	}

	private List<Report> idListToEntityList(List<Long> idList) {
		List<Report> entityList = new ArrayList<Report>();
		for (Long id : idList) {
			entityList.add(this.getById(id));
		}
		return entityList;
	}

}
