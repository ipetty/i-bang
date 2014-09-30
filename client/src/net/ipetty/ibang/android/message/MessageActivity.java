package net.ipetty.ibang.android.message;

import net.ipetty.ibang.R;
import net.ipetty.ibang.R.layout;
import net.ipetty.ibang.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

}
