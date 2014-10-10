package net.ipetty.ibang.android.seek;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.main.SeekAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyDelegationActivity extends Activity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_delegation);

		listView = (ListView) this.findViewById(R.id.listView);
		SeekAdapter adapter = new SeekAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyDelegationActivity.this, DelegationActivity.class);
				// intent.putExtra(Constants.INTENT_SEEK_ID, id);
				startActivity(intent);
			}
		});
	}

}
