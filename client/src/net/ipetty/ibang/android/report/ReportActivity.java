package net.ipetty.ibang.android.report;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.seek.GetOfferByIdTask;
import net.ipetty.ibang.android.seek.GetSeekByIdTask;
import net.ipetty.ibang.android.user.GetUserByIdTask;
import net.ipetty.ibang.vo.OfferVO;
import net.ipetty.ibang.vo.ReportVO;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReportActivity extends Activity {

	private Long reportSeekId;
	private String seekType;
	private Long reportOfferId;
	private Integer reportUserId;
	private ImageView user_avatar; // 被举报用户
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private TextView user_nickname; // 被举报用户
	private View user_content_layout;
	private TextView user_content; // 被举报seek or offer内容
	private TextView content;
	private int[] check = {
			R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5
	};
	private List<String> checkList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		// get data from intent
		reportSeekId = this.getIntent().getExtras().getLong(Constants.INTENT_SEEK_ID);
		reportOfferId = this.getIntent().getExtras().getLong(Constants.INTENT_OFFER_ID);
		reportUserId = this.getIntent().getExtras().getInt(Constants.INTENT_USER_ID);

		// init view item
		user_avatar = (ImageView) this.findViewById(R.id.user_avatar);
		user_nickname = (TextView) this.findViewById(R.id.user_nickname);
		user_content = (TextView) this.findViewById(R.id.user_content);
		user_content_layout = this.findViewById(R.id.user_content_layout);
		content = (TextView) this.findViewById(R.id.content);

		// report button
		View reportButton = this.findViewById(R.id.button);
		reportButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// validate
				if (checkList.size() < 1) {
					Toast.makeText(ReportActivity.this, "请选择举报类型", Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringUtils.isBlank(content.getText())) {
					Toast.makeText(ReportActivity.this, "请描述举报说明", Toast.LENGTH_SHORT).show();
					content.requestFocus();
					return;
				}

				ReportVO report = new ReportVO();
				if (reportSeekId != null && reportSeekId != 0) {
					report.setType(net.ipetty.ibang.vo.Constants.REPORT_TYPE_SEEK);
					report.setSeekId(reportSeekId);
					report.setSeekType(seekType);
				} else if (reportOfferId != null && reportOfferId != 0) {
					report.setType(net.ipetty.ibang.vo.Constants.REPORT_TYPE_OFFER);
					report.setOfferId(reportOfferId);
				} else if (reportUserId != null && reportUserId != 0) {
					report.setType(net.ipetty.ibang.vo.Constants.REPORT_TYPE_USER);
					report.setUserId(reportUserId);
				}
				report.setBehave(StringUtils.join(checkList, ","));
				report.setContent(content.getText().toString());

				// report
				new ReportTask(ReportActivity.this).setListener(
						new DefaultTaskListener<ReportVO>(ReportActivity.this, "正在提交举报信息...") {

							@Override
							public void onSuccess(ReportVO result) {
								Toast.makeText(ReportActivity.this, "已成功提交举报信息", Toast.LENGTH_SHORT).show();
								finish();
							}
						}).execute(report);
			}
		});

		// report behave checkbox
		for (int i = 0; i < check.length; i++) {
			int res = check[i];
			CheckBox checkbox = (CheckBox) this.findViewById(res);
			checkbox.setOnCheckedChangeListener(myCheckedChangeListener);
		}

		// init data
		initData();
	}

	/** report behave checkbox */
	private CompoundButton.OnCheckedChangeListener myCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			CheckBox checkbox = (CheckBox) buttonView;
			if (isChecked) {
				checkList.add(checkbox.getText().toString());
			} else {
				checkList.remove(checkbox.getText().toString());
			}
		}
	};

	private void initData() {
		if (reportSeekId != null && reportSeekId != 0) {
			initReportSeek();
		} else if (reportOfferId != null && reportOfferId != 0) {
			initReportOffer();
		} else if (reportUserId != null && reportUserId != 0) {
			initReportUser();
		}
	}

	/** 举报的seek信息 */
	private void initReportSeek() {
		user_content_layout.setVisibility(View.VISIBLE);
		new GetSeekByIdTask(ReportActivity.this).setListener(new DefaultTaskListener<SeekVO>(ReportActivity.this) {

			@Override
			public void onSuccess(SeekVO seek) {
				seekType = seek.getType();

				String avatarPath = seek.getSeeker().getAvatar();
				if (StringUtils.isNotBlank(avatarPath)) {
					ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + avatarPath, user_avatar,
							options);
				} else {
					user_avatar.setImageResource(R.drawable.default_avatar);
				}
				user_nickname.setText(seek.getSeeker().getNickname());
				user_content.setText(seek.getContent());
			}
		}).execute(reportSeekId);
	}

	/** 举报的offer信息 */
	private void initReportOffer() {
		user_content_layout.setVisibility(View.VISIBLE);
		new GetOfferByIdTask(ReportActivity.this).setListener(new DefaultTaskListener<OfferVO>(ReportActivity.this) {

			@Override
			public void onSuccess(OfferVO offer) {
				String avatarPath = offer.getOfferer().getAvatar();
				if (StringUtils.isNotBlank(avatarPath)) {
					ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + avatarPath, user_avatar,
							options);
				} else {
					user_avatar.setImageResource(R.drawable.default_avatar);
				}
				user_nickname.setText(offer.getOfferer().getNickname());
				user_content.setText(offer.getContent());
			}
		}).execute(reportOfferId);
	}

	/** 举报的用户信息 */
	private void initReportUser() {
		user_content_layout.setVisibility(View.GONE);
		new GetUserByIdTask(ReportActivity.this).setListener(new DefaultTaskListener<UserVO>(ReportActivity.this) {

			@Override
			public void onSuccess(UserVO user) {
				String avatarPath = user.getAvatar();
				if (StringUtils.isNotBlank(avatarPath)) {
					ImageLoader.getInstance().displayImage(Constants.FILE_SERVER_BASE + avatarPath, user_avatar,
							options);
				} else {
					user_avatar.setImageResource(R.drawable.default_avatar);
				}
				user_nickname.setText(user.getNickname());
			}
		}).execute(reportUserId);
	}

}
