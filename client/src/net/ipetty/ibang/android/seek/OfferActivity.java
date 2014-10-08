package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class OfferActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_offer));
		btnBack.setOnClickListener(new BackClickListener(this));

	}
}
