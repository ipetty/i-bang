package net.ipetty.ibang.android.main;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	private boolean isLogin = false;

	private ViewPager viewPager;
	private final Fragment[] fragments = { new MainHomeFragment(), new MainPublishFragment(),
			new MainDiscoverFragment(), new MainMeFragment() };

	private View home_layout;
	private View publish_layout;
	private View discover_layout;
	private View me_layout;

	private TextView home_text;
	private TextView publish_text;
	private TextView discover_text;
	private TextView me_text;

	private ImageView home_image;
	private ImageView publish_image;
	private ImageView discover_image;
	private ImageView me_image;

	private MyFragmentPagerAdapter myFragmentPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActivityManager.getInstance().addActivity(this);
		viewPager = (ViewPager) findViewById(R.id.tabpager);
		myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(myFragmentPagerAdapter);
		viewPager.setOnPageChangeListener(myPageChangeListener);

		home_layout = this.findViewById(R.id.home_layout);
		publish_layout = this.findViewById(R.id.publish_layout);
		discover_layout = this.findViewById(R.id.discover_layout);
		me_layout = this.findViewById(R.id.me_layout);
		home_layout.setOnClickListener(new TabClickListener(0));
		publish_layout.setOnClickListener(new TabClickListener(1));
		discover_layout.setOnClickListener(new TabClickListener(2));
		me_layout.setOnClickListener(new TabClickListener(3));

		home_text = (TextView) this.findViewById(R.id.home_text);
		publish_text = (TextView) this.findViewById(R.id.publish_text);
		discover_text = (TextView) this.findViewById(R.id.discover_text);
		me_text = (TextView) this.findViewById(R.id.me_text);

		home_image = (ImageView) this.findViewById(R.id.home_image);
		publish_image = (ImageView) this.findViewById(R.id.publish_image);
		discover_image = (ImageView) this.findViewById(R.id.discover_image);
		me_image = (ImageView) this.findViewById(R.id.me_image);

		if (!isLogin) {
			viewPager.setCurrentItem(2);
		} else {
			viewPager.setCurrentItem(0);
		}
	}

	public class TabClickListener implements OnClickListener {

		private int index = 0;

		public TabClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewPager.setCurrentItem(index);
		}

	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		private Fragment[] fragments;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Override
		public Fragment getItem(int position) {
			return (Fragment) fragments[position];
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

		}
	}

	private final OnPageChangeListener myPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			home_text.setTextColor(getResources().getColor(R.color.tab_gray_color));
			publish_text.setTextColor(getResources().getColor(R.color.tab_gray_color));
			discover_text.setTextColor(getResources().getColor(R.color.tab_gray_color));
			me_text.setTextColor(getResources().getColor(R.color.tab_gray_color));

			home_image.setImageResource(R.drawable.tab_home_default);
			publish_image.setImageResource(R.drawable.tab_publish_default);
			discover_image.setImageResource(R.drawable.tab_search_default);
			me_image.setImageResource(R.drawable.tab_me_default);

			switch (arg0) {
			case 0: {
				home_text.setTextColor(getResources().getColor(R.color.base_color));
				home_image.setImageResource(R.drawable.tab_home_active);
				break;
			}
			case 1: {
				publish_text.setTextColor(getResources().getColor(R.color.base_color));
				publish_image.setImageResource(R.drawable.tab_publish_active);
				break;
			}
			case 2: {
				discover_text.setTextColor(getResources().getColor(R.color.base_color));
				discover_image.setImageResource(R.drawable.tab_search_active);
				break;
			}
			case 3: {
				me_text.setTextColor(getResources().getColor(R.color.base_color));
				me_image.setImageResource(R.drawable.tab_me_active);
				break;
			}

			}
		}

	};
}