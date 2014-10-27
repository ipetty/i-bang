package net.ipetty.ibang.android.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.util.SeekCategoryUtils;
import net.ipetty.ibang.vo.SeekCategory;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectUserCategoryActivity extends Activity {
	private String[] l1Categories = SeekCategoryUtils.listL1Categories();
	private String[] l2Categories = null;

	private List<SeekCategory> checkList = new ArrayList<SeekCategory>(0);
	private HashMap<SeekCategory, CheckBox> map = new HashMap<SeekCategory, CheckBox>();

	private UserVO user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_user_category);

		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_select_user_category));

		LinearLayout layout = (LinearLayout) this.findViewById(R.id.layout);
		TextView btn = (TextView) this.findViewById(R.id.button);

		user = ApiContext.getInstance(this).getCurrentUser();

		checkList = user.getOfferRange();

		for (String l1 : l1Categories) {
			layout.addView(getL1View(l1));
			addSubView(layout, l1);
		}

		setCheckList();

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getCheckList();
				if (checkList.size() == 0) {
					Toast.makeText(SelectUserCategoryActivity.this, "请选择特长", Toast.LENGTH_SHORT).show();
				} else {
					// TODO:save操作

				}
			}

		});

	}

	private void setCheckList() {
		// TODO Auto-generated method stub
		for (SeekCategory key : map.keySet()) {
			for (SeekCategory cat : checkList) {
				if (cat.getCategoryL1().equals(key.getCategoryL1()) && cat.getCategoryL2().equals(key.getCategoryL2())) {
					CheckBox cb = map.get(key);
					cb.setChecked(true);
				}
			}
		}
	}

	private void getCheckList() {
		// TODO Auto-generated method stub
		checkList.clear();
		for (SeekCategory key : map.keySet()) {
			CheckBox cb = map.get(key);
			if (cb.isChecked()) {
				checkList.add(key);
			}
		}
	}

	private View getL1View(String text) {
		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setHeight(AnimUtils.dip2px(this, 42));
		tv.setText(text);
		tv.setTextSize(AnimUtils.dip2px(this, 12));
		return tv;
	}

	private void addSubView(LinearLayout layout, String text) {
		l2Categories = SeekCategoryUtils.listL2Categories(text);
		int i = 0;
		while (i < l2Categories.length) {
			LinearLayout ly = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.select_user_category_item, null);
			CheckBox cb1 = (CheckBox) ly.findViewById(R.id.checkBox1);
			CheckBox cb2 = (CheckBox) ly.findViewById(R.id.checkBox2);
			CheckBox cb3 = (CheckBox) ly.findViewById(R.id.checkBox3);

			if (i < l2Categories.length) {
				String str1 = l2Categories[i++];
				cb1.setText(str1);
				map.put(new SeekCategory(text, str1), cb1);

			} else {
				cb1.setVisibility(View.INVISIBLE);
			}
			if (i < l2Categories.length) {
				String str2 = l2Categories[i++];
				cb2.setText(str2);
				map.put(new SeekCategory(text, str2), cb2);
			} else {
				cb2.setVisibility(View.INVISIBLE);
			}
			if (i < l2Categories.length) {
				String str3 = l2Categories[i++];
				cb3.setText(str3);
				map.put(new SeekCategory(text, str3), cb3);
			} else {
				cb3.setVisibility(View.INVISIBLE);
			}
			layout.addView(ly);
		}

	}

}
