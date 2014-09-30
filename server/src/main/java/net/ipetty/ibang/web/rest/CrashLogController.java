package net.ipetty.ibang.web.rest;

import java.io.IOException;

import javax.annotation.Resource;

import net.ipetty.ibang.model.CrashLog;
import net.ipetty.ibang.service.CrashLogService;
import net.ipetty.ibang.vo.CrashLogVO;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * CrashLogController
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
@Controller
public class CrashLogController extends BaseController {

	@Resource
	private CrashLogService crashLogService;

	/**
	 * 保存崩溃日志
	 */
	@RequestMapping(value = "/crash", method = RequestMethod.POST)
	@ResponseBody
	public boolean save(@RequestBody CrashLogVO crashLog) throws IOException {
		Assert.notNull(crashLog, "崩溃日志不能为空");
		crashLogService.save(CrashLog.fromVO(crashLog));
		return true;
	}

}
