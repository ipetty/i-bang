package net.ipetty.ibang.android.evaluation;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class EvaluationActivity extends Activity {
	private Integer point = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_evaluation));
		btnBack.setOnClickListener(new BackClickListener(this));

		TextView evaluation = (TextView) this.findViewById(R.id.evaluation);
		evaluation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 评价

			}
		});

		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
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
}
