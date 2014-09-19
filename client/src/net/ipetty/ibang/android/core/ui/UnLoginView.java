package net.ipetty.ibang.android.core.ui;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.login.LoginActivity;
import net.ipetty.ibang.android.login.RegisterActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class UnLoginView {
	private Activity activity;
	private View unlogin;

	public UnLoginView(final Activity activity, View view, int resId) {
		this.activity = activity;
		unlogin = view.findViewById(R.id.fragment_unlogin);
		View reg = unlogin.findViewById(R.id.register);
		TextView un_login_text = (TextView) unlogin.findViewById(R.id.un_login_text);
		un_login_text.setText(this.activity.getResources().getString(resId));
		reg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, RegisterActivity.class);
				activity.startActivity(intent);
			}
		});
		View login = unlogin.findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
			}
		});
	}

	public void show() {
		unlogin.setVisibility(View.VISIBLE);
	}

	public void hide() {
		unlogin.setVisibility(View.GONE);
	}

}
