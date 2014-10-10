package net.ipetty.ibang.android.seek;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.vo.DelegationVO;
import net.ipetty.ibang.vo.OfferVO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SeekActivity extends Activity {

	private TextView offerBtn;
	private LinearLayout offerListView;
	private LinearLayout delegationListView;

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
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SeekActivity.this, PublishOfferActivity.class);
				startActivity(intent);
			}
		});

		delegationListView = (LinearLayout) this.findViewById(R.id.delegationList);
		List<DelegationVO> delegationList = new ArrayList<DelegationVO>();
		DelegationVO tt = new DelegationVO();
		delegationList.add(tt);
		delegationList.add(tt);
		initDelegationView(delegationList);

		offerListView = (LinearLayout) this.findViewById(R.id.offerList);
		List<OfferVO> offerList = new ArrayList<OfferVO>();
		OfferVO t = new OfferVO();
		offerList.add(t);
		offerList.add(t);
		initOfferView(offerList);
	}

	private void initDelegationView(List<DelegationVO> delegationList) {
		// TODO Auto-generated method stub
		delegationListView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);
		for (DelegationVO delegationVO : delegationList) {
			View view = inflater.inflate(R.layout.list_delegation_simple_item, null);
			delegationListView.addView(view);
		}
	}

	private void initOfferView(List<OfferVO> offerList) {
		// TODO Auto-generated method stub
		offerListView.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);
		for (OfferVO offer : offerList) {
			View view = inflater.inflate(R.layout.list_offer_item, null);
			offerListView.addView(view);
		}
	}
}
