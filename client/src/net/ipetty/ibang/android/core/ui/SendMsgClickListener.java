package net.ipetty.ibang.android.core.ui;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.letter.LetterActivity;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.vo.UserVO;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SendMsgClickListener implements OnClickListener {
	private Activity activity;
	private int id;
	private UserVO userVO;

	public SendMsgClickListener(Activity activity, int id) {
		this.activity = activity;
		this.id = id;
		this.userVO = ApiContext.getInstance(activity).getCurrentUser();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (userVO.getId().equals(this.id)) {
			Toast.makeText(activity, "不能给自己发送消息", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(activity, LetterActivity.class);
		intent.putExtra(Constants.INTENT_LETTER_USER_ID, id);
		activity.startActivity(intent);
	}

}
