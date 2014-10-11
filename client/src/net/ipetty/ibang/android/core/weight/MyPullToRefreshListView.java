package net.ipetty.ibang.android.core.weight;

import net.ipetty.ibang.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyPullToRefreshListView extends PullToRefreshListView implements OnScrollListener {
	private LayoutInflater mInflater;
	private RelativeLayout mMoreView;
	private TextView mMoreViewText;
	private ProgressBar mMoreViewProgress;
	private OnLoadMoreListener onLoadMoreListener;
	private OnScrollListener mOnScrollListener;
	private static boolean MORE_LOAD_PROCESS = false;

	public MyPullToRefreshListView(Context context) {
		super(context);
		this.initFooter(context);
	}

	public MyPullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initFooter(context);
	}

	public MyPullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initFooter(context);
	}

	private void initFooter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMoreView = (RelativeLayout) mInflater.inflate(R.layout.drop_to_load_footer, this, false);
		mMoreView.setOnClickListener(moreOnClickListener);
		mMoreViewText = (TextView) mMoreView.findViewById(R.id.drop_to_refresh_text);
		mMoreViewProgress = (ProgressBar) mMoreView.findViewById(R.id.drop_to_refresh_progress);
		addFooterView(mMoreView);
		super.setOnScrollListener(this);
	}

	public void prepareForMore() {
		MORE_LOAD_PROCESS = true;
		mMoreViewProgress.setVisibility(View.VISIBLE);
		mMoreViewText.setText(R.string.drop_to_more_load_label);
	}

	public void setFooterVisiblity(int visiblity) {
		mMoreView.setVisibility(visiblity);
	}

	public void resetFooter(int visiblity) {
		setFooterVisiblity(visiblity);
		mMoreViewProgress.setVisibility(View.GONE);
		mMoreViewText.setText(R.string.drop_to_more_label);
	}

	private final OnClickListener moreOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			prepareForMore();
			onLoadMore();
		}
	};

	public void onLoadMore() {
		if (onLoadMoreListener != null) {
			onLoadMoreListener.LoadMore();
		}
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		this.onLoadMoreListener = onLoadMoreListener;
	}

	public interface OnLoadMoreListener {
		public void LoadMore();
	}

	public void onLoadMoreComplete() {
		// TODO Auto-generated method stub
		this.onLoadMoreComplete(View.VISIBLE);
	}

	public void onLoadMoreComplete(int visiblity) {
		// TODO Auto-generated method stub
		this.resetFooter(visiblity);
		MORE_LOAD_PROCESS = false;
	}

	public void hideMoveView() {
		mMoreView.setVisibility(GONE);
	}

	public void showMoveView() {
		mMoreView.setVisibility(VISIBLE);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

		if (firstVisibleItem > 0 && visibleItemCount + firstVisibleItem == totalItemCount && !MORE_LOAD_PROCESS) {
			if (mMoreView.getVisibility() != View.GONE) {
				prepareForMore();
				onLoadMore();
			}
		}

		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}

	}
}
