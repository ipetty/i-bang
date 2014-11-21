package net.ipetty.ibang.android.report;

import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.ReportApi;
import net.ipetty.ibang.vo.ReportVO;
import android.app.Activity;
import android.util.Log;

/**
 * ReportTask
 * @author luocanfeng
 * @date 2014年11月21日
 */
public class ReportTask extends Task<ReportVO, ReportVO> {

	private String TAG = getClass().getSimpleName();

	public ReportTask(Activity activity) {
		super(activity);
	}

	@Override
	protected ReportVO myDoInBackground(ReportVO... args) {
		Log.d(TAG, "report");
		return IbangApi.init(activity).create(ReportApi.class).report(args[0]);
	}

}
