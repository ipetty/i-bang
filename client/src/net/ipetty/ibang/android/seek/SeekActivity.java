package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SeekActivity extends Activity {

	private TextView offerBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seek);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_seek));
		btnBack.setOnClickListener(new BackClickListener(this));

		offerBtn = (TextView) this.findViewById(R.id.offer);
		offerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SeekActivity.this, OfferActivity.class);
				startActivity(intent);
			}
		});

	}

}
