package net.ipetty.ibang.android.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.UploadView;
import net.ipetty.ibang.vo.EvaluationVO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class EvaluationActivity extends Activity {

	private Integer point = 10;
	private Long delegationId;
	private String evaluatorType;
	private Integer evaluatorId;
	private Integer evaluateTargetId;
	private EditText contentView;
	private UploadView uploadView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_evaluation));

		delegationId = this.getIntent().getExtras().getLong(Constants.INTENT_DELEGATION_ID);
		evaluatorType = this.getIntent().getExtras().getString(Constants.INTENT_EVALUATOR_TYPE);
		evaluatorId = this.getIntent().getExtras().getInt(Constants.INTENT_EVALUATOR_ID);
		evaluateTargetId = this.getIntent().getExtras().getInt(Constants.INTENT_EVALUATE_TARGET_ID);

		contentView = (EditText) this.findViewById(R.id.content);
		uploadView = new UploadView(this);

		TextView evaluation = (TextView) this.findViewById(R.id.evaluation);
		evaluation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final List<File> files = uploadView.getFiles();
				final List<String> filePaths = new ArrayList<String>();
				for (File file : files) {
					filePaths.add(file.getAbsolutePath());
				}

				// 评价
				EvaluationVO e = new EvaluationVO();
				e.setContent(contentView.getText().toString());
				e.setDelegationId(delegationId);
				e.setType(evaluatorType);
				e.setEvaluatorId(evaluatorId);
				e.setEvaluateTargetId(evaluateTargetId);
				e.setPoint(point);
				new EvaluateTask(EvaluationActivity.this)
						.setListener(new EvaluateTaskListener(EvaluationActivity.this)).execute(
								new EvaluationForm(e, filePaths));
			}
		});

		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int radioButtonId = group.getCheckedRadioButtonId();
				switch (radioButtonId) {
				case R.id.radio0: {
					point = 10;
					break;
				}
				case R.id.radio1: {
					point = 7;
					break;
				}
				case R.id.radio2: {
					point = 4;
					break;
				}
				case R.id.radio3: {
					point = 2;
					break;
				}
				case R.id.radio4: {
					point = 1;
					break;
				}
				case R.id.radio5: {
					point = 0;
					break;
				}
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uploadView.onActivityResult(requestCode, resultCode, data);
	}
}
