package net.ipetty.ibang.repository;

import net.ipetty.ibang.model.CrashLog;

/**
 * CrashLogDao
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public interface CrashLogDao {

	/**
	 * 保存崩溃日志
	 */
	public void save(CrashLog crashLog);

}
