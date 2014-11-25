package net.ipetty.ibang.android.publish;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.ModDialogItem;
import net.ipetty.ibang.android.core.ui.UploadView;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.core.util.JSONUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.UserEditActivity;
import net.ipetty.ibang.vo.LocationVO;
import net.ipetty.ibang.vo.SeekWithLocationVO;
import net.ipetty.ibang.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;

public class PublishActivity extends Activity {

	private String TAG = getClass().getSimpleName();

	private String categoryL1;
	private String categoryL2;
	private EditText contentView;
	private EditText exipireDateView;
	private TextView nicknameView;
	private TextView phoneView;
	private UserVO user;
	private EditText delegateNumberView;
	private TextView button;
	private EditText additionalRewardView;
	private EditText serviceDateView;

	private UploadView uploadView;

	private Dialog exipireDateDialog;
	private Dialog serviceDateDialog;

	private ArrayList<ModDialogItem> serviceDateItems;
	private ArrayList<ModDialogItem> exipireDateItems;

	private View help_me_layout;
	private View to_help_layout;
	private View num_layout;
	private View local_layout;
	private String type = net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK;

	// 定位结果
	private ReverseGeoCodeResult locationResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		ActivityManager.getInstance().addActivity(this);

		// 选择位置事件侦听
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BROADCAST_INTENT_LOCATION);
		this.registerReceiver(broadcastreciver, filter);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));
		categoryL1 = this.getIntent().getExtras().getString(Constants.INTENT_CATEGORY);
		categoryL2 = this.getIntent().getExtras().getString(Constants.INTENT_SUB_CATEGORY);
		((TextView) this.findViewById(R.id.action_bar_title)).setText(categoryL1 + "-" + categoryL2);

		help_me_layout = this.findViewById(R.id.help_me_layout);
		to_help_layout = this.findViewById(R.id.to_help_layout);
		help_me_layout.setOnClickListener(new TabClickListener(0));
		to_help_layout.setOnClickListener(new TabClickListener(1));

		num_layout = this.findViewById(R.id.num_layout);

		// 界面元素绑定
		local_layout = this.findViewById(R.id.local_layout);
		local_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(PublishActivity.this, LocateActivity.class);
				startActivity(intent);
			}
		});

		delegateNumberView = (EditText) this.findViewById(R.id.num);
		additionalRewardView = (EditText) this.findViewById(R.id.additionalReward);
		serviceDateView = (EditText) this.findViewById(R.id.serviceDate);

		exipireDateItems = new ArrayList<ModDialogItem>();
		exipireDateItems.add(new ModDialogItem(null, Constants.MAX_EXIPIREDATE_CONDITION,
				Constants.MAX_EXIPIREDATE_CONDITION, new OnClickListener() {

					@Override
					public void onClick(View view) {
						String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
						String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
						exipireDateView.setText(label);
						exipireDateDialog.cancel();
					}
				}));
		exipireDateItems.add(new ModDialogItem(null, "指定日期", "指定日期", new OnClickListener() {

			@Override
			public void onClick(View v) {
				exipireDateDialog.cancel();
				String str = exipireDateView.getText().toString();
				if (Constants.MAX_EXIPIREDATE_CONDITION.equals(str)) {
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					// c.add(Calendar.MONTH, 3);
					str = DateUtils.toDateString(c.getTime());
				}
				exipireDateDialog = DialogUtils.datePopupDialog(PublishActivity.this, exipireDateClick, str,
						exipireDateDialog);
			}
		}));
		// 事件初始化
		exipireDateView = (EditText) this.findViewById(R.id.exipireDate);
		exipireDateView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exipireDateDialog = DialogUtils.modPopupDialog(PublishActivity.this, exipireDateItems,
						exipireDateDialog);
			}
		});

		exipireDateView.setText(Constants.MAX_EXIPIREDATE_CONDITION);

		nicknameView = (TextView) this.findViewById(R.id.nickname);
		phoneView = (TextView) this.findViewById(R.id.phone);
		nicknameView.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_NICKNAME));
		phoneView.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_PHONE));

		initFileUpload();
		initUserInfo();

		serviceDateItems = new ArrayList<ModDialogItem>();
		serviceDateItems.add(new ModDialogItem(null, "不限", "不限", serviceDateClick));
		serviceDateItems.add(new ModDialogItem(null, "工作日", "工作日", serviceDateClick));
		serviceDateItems.add(new ModDialogItem(null, "双休日", "双休日", serviceDateClick));
		serviceDateItems.add(new ModDialogItem(null, "指定日期", "指定日期", selectServiceDateClick));

		serviceDateView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				serviceDateDialog = DialogUtils.modPopupDialog(PublishActivity.this, serviceDateItems,
						serviceDateDialog);
			}
		});

		// 发布
		button = (TextView) this.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (StringUtils.isBlank(contentView.getText().toString())) {
					Toast.makeText(PublishActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
					contentView.requestFocus();
					return;
				}

				final SeekWithLocationVO seek = new SeekWithLocationVO();
				seek.setType(type);
				seek.setSeekerId(user.getId());
				seek.setCategoryL1(categoryL1);
				seek.setCategoryL2(categoryL2);
				seek.setContent(contentView.getText().toString());

				int num = Integer.valueOf(delegateNumberView.getText().toString());
				if (Constants.MAX_DELEGATION_CONDITION == num) {
					num = Constants.MAX_DELEGATION;
				}
				seek.setDelegateNumber(num);
				seek.setAdditionalReward(additionalRewardView.getText().toString());

				String exipireDateStr = exipireDateView.getText().toString();
				if (Constants.MAX_EXIPIREDATE_CONDITION.equals(exipireDateStr)) {
					exipireDateStr = Constants.MAX_EXIPIREDATE;
				}
				seek.setServiceDate(serviceDateView.getText().toString());
				seek.setExipireDate(DateUtils.fromDateString(exipireDateStr));

				// 上传图片文件
				final List<File> files = uploadView.getFiles();
				final List<String> filePaths = new ArrayList<String>();
				for (File file : files) {
					filePaths.add(file.getAbsolutePath());
				}

				if (locationResult != null) {
					// location
					LocationVO location = new LocationVO();
					location.setAddress(locationResult.getAddress());
					location.setLatitude(locationResult.getLocation().latitude);
					location.setLongitude(locationResult.getLocation().longitude);
					seek.setLocation(location);

					// 省市县
					AddressComponent address = locationResult.getAddressDetail();
					location.setProvince(address.province);
					location.setCity(address.city);
					location.setDistrict(address.district);
					seek.setProvince(address.province);
					seek.setCity(address.city);
					seek.setDistrict(address.district);
				}

				new PublishSeekTask(PublishActivity.this).setListener(
						new PublishSeekTaskListener(PublishActivity.this, type)).execute(
						new SeekWithLocationForm(seek, filePaths));
			}
		});
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String jsonStr = intent.getStringExtra(Constants.BROADCAST_INTENT_DATA_NAME);
			Log.d(TAG, jsonStr);
			locationResult = JSONUtils.fromJSON(jsonStr, ReverseGeoCodeResult.class);
		}
	};

	private OnClickListener selectServiceDateClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			serviceDateDialog.cancel();
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			String str = DateUtils.toDateString(c.getTime());
			serviceDateDialog = DialogUtils.datePopupDialog(PublishActivity.this, serviceDateDialogClick, str,
					serviceDateDialog);
		}
	};

	private OnClickListener serviceDateClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
			String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
			serviceDateView.setText(label);
			serviceDateDialog.cancel();
		}
	};
	private OnDateSetListener serviceDateDialogClick = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			String str = DateUtils.toDateString(c.getTime());
			serviceDateView.setText(str);
			serviceDateDialog.cancel();
		}
	};

	private void initUserInfo() {
		user = ApiContext.getInstance(PublishActivity.this).getCurrentUser();
		nicknameView.setText(user.getNickname());
		phoneView.setText(user.getPhone());
	}

	private class EditOnClickListener implements OnClickListener {

		private String type = null;

		public EditOnClickListener(String type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PublishActivity.this, UserEditActivity.class);
			intent.putExtra(Constants.INTENT_USER_EDIT_TYPE, this.type);
			PublishActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_USER_EDIT);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uploadView.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.REQUEST_CODE_USER_EDIT) {
			// 从上下文重新获取用户信息
			initUserInfo();
		}
	}

	private OnDateSetListener exipireDateClick = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			String str = DateUtils.toDateString(c.getTime());
			exipireDateView.setText(str);
		}
	};

	private void initFileUpload() {
		uploadView = new UploadView(this);
	}

	public class TabClickListener implements OnClickListener {

		private int index = 0;

		public TabClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			if (index == 0) {
				help_me_layout.setBackgroundResource(R.drawable.news_tab_selected);
				to_help_layout.setBackgroundResource(R.drawable.trans);
				// 发布求助
				type = net.ipetty.ibang.vo.Constants.SEEK_TYPE_SEEK;
				num_layout.setVisibility(View.VISIBLE);
			} else {
				help_me_layout.setBackgroundResource(R.drawable.trans);
				to_help_layout.setBackgroundResource(R.drawable.news_tab_selected);
				// 发布帮助
				type = net.ipetty.ibang.vo.Constants.SEEK_TYPE_ASSISTANCE;
				num_layout.setVisibility(View.GONE);
			}
		}
	}

}
