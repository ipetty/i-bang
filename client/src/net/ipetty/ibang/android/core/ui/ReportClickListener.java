package net.ipetty.ibang.android.core.ui;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.report.ReportActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ReportClickListener implements OnClickListener {

	public static String TYPE_SEEK = net.ipetty.ibang.vo.Constants.REPORT_TYPE_SEEK;
	public static String TYPE_OFFER = net.ipetty.ibang.vo.Constants.REPORT_TYPE_OFFER;
	public static String TYPE_USER = net.ipetty.ibang.vo.Constants.REPORT_TYPE_USER;

	private Activity activity;
	private String type;
	private long id;
	private OnClickListener ck;

	public ReportClickListener(Activity activity) {
		this.activity = activity;
	}

	public ReportClickListener(Activity activity, String type, long id, OnClickListener ck) {
		this.activity = activity;
		this.type = type;
		this.id = id;
		this.ck = ck;
	}

	@Override
	public void onClick(View v) {
		Long reportSeekId = null;
		Long reportOfferId = null;
		Integer reportUserId = null;

		Intent intent = new Intent(activity, ReportActivity.class);
		if (TYPE_SEEK.equals(type)) {
			reportSeekId = id;
			intent.putExtra(Constants.INTENT_SEEK_ID, reportSeekId);
		} else if (TYPE_OFFER.equals(type)) {
			reportOfferId = id;
			intent.putExtra(Constants.INTENT_OFFER_ID, reportOfferId);
		} else if (TYPE_USER.equals(type)) {
			reportUserId = (int) id;
			intent.putExtra(Constants.INTENT_USER_ID, reportUserId);
		}

		activity.startActivity(intent);
		ck.onClick(v);
	}

}
