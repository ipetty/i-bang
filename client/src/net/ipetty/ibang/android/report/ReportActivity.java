package net.ipetty.ibang.android.report;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportActivity extends Activity {
	private Long reportSeekId;
	private Long reportUserId;
	private Long reportOfferId;
	private ImageView user_avatar; // 被举报用户
	private TextView user_nickname; // 被举报用户
	private View user_content_layout;
	private TextView user_content; // 被举报seek or offer内容
	private TextView content;
	private int[] check = { R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5 };
	private List<String> checkList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		reportSeekId = this.getIntent().getExtras().getLong(Constants.INTENT_SEEK_ID);
		reportUserId = this.getIntent().getExtras().getLong(Constants.INTENT_USER_ID);
		reportOfferId = this.getIntent().getExtras().getLong(Constants.INTENT_OFFER_ID);

		user_avatar = (ImageView) this.findViewById(R.id.user_avatar);
		user_nickname = (TextView) this.findViewById(R.id.user_nickname);
		user_content = (TextView) this.findViewById(R.id.user_content);
		user_content_layout = this.findViewById(R.id.user_content_layout);
		content = (TextView) this.findViewById(R.id.content);

		View button = this.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		for (int i = 0; i < check.length; i++) {
			int res = check[i];
			CheckBox checkbox = (CheckBox) this.findViewById(res);
			checkbox.setOnCheckedChangeListener(myCheckedChangeListener);
		}

		if (reportSeekId != null) {
			initReportSeek();
			return;
		}
		if (reportUserId != null) {
			initReportUser();
			return;
		}
		if (reportOfferId != null) {
			initReportOffer();
			return;
		}
	}

	private CompoundButton.OnCheckedChangeListener myCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			CheckBox checkbox = (CheckBox) buttonView;
			if (isChecked) {
				checkList.add(checkbox.getText().toString());
			} else {
				checkList.remove(checkbox.getText().toString());
			}
		}
	};

	// 举报seek
	private void initReportSeek() {
		user_content_layout.setVisibility(View.VISIBLE);
		// TODO 获取到seekVO填充

	}

	private void initReportUser() {
		user_content_layout.setVisibility(View.GONE);
		// TODO 获取到user填充

	}

	private void initReportOffer() {
		user_content_layout.setVisibility(View.VISIBLE);
		// TODO 获取到offer填充

	}
}
