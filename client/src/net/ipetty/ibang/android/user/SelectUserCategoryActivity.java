package net.ipetty.ibang.android.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.util.SeekCategoryUtils;
import net.ipetty.ibang.vo.SeekCategory;
import net.ipetty.ibang.vo.UserOfferRange;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

	private final String TAG = getClass().getSimpleName();

	private String[] l1Categories = SeekCategoryUtils.listL1Categories();
	private String[] l2Categories = null;

	private List<SeekCategory> selectedOfferRange = new ArrayList<SeekCategory>(0);
	private Map<SeekCategory, CheckBox> mapCategory2CheckBox = new HashMap<SeekCategory, CheckBox>();
	private Map<CheckBox, SeekCategory> mapCheckBox2Category = new HashMap<CheckBox, SeekCategory>();

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
		selectedOfferRange = user.getOfferRange();

		// 初始化分类选项界面
		for (String l1 : l1Categories) {
			layout.addView(getL1View(l1));
			addSubView(layout, l1);
		}

		// 初始化用户已选择选项
		initSelectedOfferRangeView();

		// 保存按钮
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getSelectedOfferRange();
				if (selectedOfferRange.size() == 0) {
					Toast.makeText(SelectUserCategoryActivity.this, "请选择特长", Toast.LENGTH_SHORT).show();
				} else {
					// 保存选项
					new UpdateOfferRangeTask(SelectUserCategoryActivity.this).setListener(
							new UpdateOfferRangeTaskListener(SelectUserCategoryActivity.this)).execute(
							new UserOfferRange(user.getId(), selectedOfferRange));
				}
			}
		});
	}

	/**
	 * 初始化用户已选择选项
	 */
	private void initSelectedOfferRangeView() {
		for (SeekCategory category : selectedOfferRange) {
			Log.d(TAG, "pre checked category: " + category.toString());
			CheckBox checkbox = mapCategory2CheckBox.get(category);
			if (checkbox != null) {
				checkbox.setChecked(true);
			}
		}
	}

	/**
	 * 获取到界面上用户选择的选项
	 */
	private void getSelectedOfferRange() {
		selectedOfferRange.clear();
		for (CheckBox checkbox : mapCheckBox2Category.keySet()) {
			if (checkbox.isChecked()) {
				Log.d(TAG, "user selected category: " + mapCheckBox2Category.get(checkbox).toString());
				selectedOfferRange.add(mapCheckBox2Category.get(checkbox));
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

	/**
	 * 为一级选项添加二级选项列表
	 */
	private void addSubView(LinearLayout layout, String l1CategoryLabel) {
		l2Categories = SeekCategoryUtils.listL2Categories(l1CategoryLabel);
		int i = 0;
		while (i < l2Categories.length) {
			LinearLayout ly = (LinearLayout) LayoutInflater.from(this)
					.inflate(R.layout.select_user_category_item, null);
			CheckBox cb1 = (CheckBox) ly.findViewById(R.id.checkBox1);
			CheckBox cb2 = (CheckBox) ly.findViewById(R.id.checkBox2);
			CheckBox cb3 = (CheckBox) ly.findViewById(R.id.checkBox3);
			this.bindCategoryToCheckBox(l1CategoryLabel, l2Categories, cb1, i++);
			this.bindCategoryToCheckBox(l1CategoryLabel, l2Categories, cb2, i++);
			this.bindCategoryToCheckBox(l1CategoryLabel, l2Categories, cb3, i++);
			layout.addView(ly);
		}
	}

	private void bindCategoryToCheckBox(String l1Category, String[] l2Categories, CheckBox checkbox, int idx) {
		if (idx < l2Categories.length) {
			checkbox.setText(l2Categories[idx]);
			SeekCategory category = new SeekCategory(l1Category, l2Categories[idx]);
			mapCategory2CheckBox.put(category, checkbox);
			Log.d(TAG, "init category: " + category.toString());
			mapCheckBox2Category.put(checkbox, category);
		} else {
			checkbox.setVisibility(View.INVISIBLE);
		}
	}

}
