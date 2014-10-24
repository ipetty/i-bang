package net.ipetty.ibang.android.publish;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ActivityManager;
import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.core.ui.BackClickListener;
import net.ipetty.ibang.android.core.ui.UploadView;
import net.ipetty.ibang.android.core.util.DateUtils;
import net.ipetty.ibang.android.core.util.DialogUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.user.UserEditActivity;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.UserVO;
import org.apache.commons.lang3.StringUtils;

public class PublishActivity extends Activity {

        private String categoryL1;
        private String categoryL2;
        private EditText contentView;
        private EditText exipireDateView;
        private TextView nicknameView;
        private TextView phoneView;
        private UserVO user;
        private EditText delegateNumberView;
        private TextView button;

        private UploadView uploadView;

        private Dialog exipireDateDialog;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_publish);
                ActivityManager.getInstance().addActivity(this);

                /* action bar */
                ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
                btnBack.setOnClickListener(new BackClickListener(this));
                categoryL1 = this.getIntent().getExtras().getString(Constants.INTENT_CATEGORY);
                categoryL2 = this.getIntent().getExtras().getString(Constants.INTENT_SUB_CATEGORY);
                ((TextView) this.findViewById(R.id.action_bar_title)).setText(categoryL1 + "-" + categoryL2);

                // 界面元素绑定
                contentView = (EditText) this.findViewById(R.id.content);
                delegateNumberView = (EditText) this.findViewById(R.id.num);

                // 事件初始化
                exipireDateView = (EditText) this.findViewById(R.id.exipireDate);
                exipireDateView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                exipireDateDialog = DialogUtils.datePopupDialog(PublishActivity.this, exipireDateClick, exipireDateView
                                        .getText().toString(), exipireDateDialog);
                        }
                });
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, 3);
                String str = DateUtils.toDateString(c.getTime());
                exipireDateView.setText(str);

                nicknameView = (TextView) this.findViewById(R.id.nickname);
                phoneView = (TextView) this.findViewById(R.id.phone);
                nicknameView.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_NICKNAME));
                phoneView.setOnClickListener(new EditOnClickListener(Constants.INTENT_USER_EDIT_TYPE_PHONE));

                initFileUpload();
                initUserInfo();

                // 发布
                button = (TextView) this.findViewById(R.id.button);
                button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (StringUtils.isBlank(contentView.getText().toString())) {
                                        Toast.makeText(PublishActivity.this, "求助内容不能为空", Toast.LENGTH_SHORT).show();
                                        contentView.requestFocus();
                                        return;
                                }

                                final SeekVO seek = new SeekVO();
                                seek.setSeekerId(user.getId());
                                seek.setCategoryL1(categoryL1);
                                seek.setCategoryL2(categoryL2);
                                seek.setContent(contentView.getText().toString());
                                seek.setDelegateNumber(Integer.valueOf(delegateNumberView.getText().toString()));
                                String exipireDateStr = exipireDateView.getText().toString();
                                seek.setExipireDate(DateUtils.fromDateString(exipireDateStr));

                                // 上传图片文件
                                final List<File> files = uploadView.getFiles();
                                final List<String> filePaths = new ArrayList<String>();
                                for (File file : files) {
                                        Log.d("----->", file.getAbsolutePath());
                                        filePaths.add(file.getAbsolutePath());
                                }

                                new PublishSeekTask(PublishActivity.this)
                                        .setListener(new PublishSeekTaskListener(PublishActivity.this)).execute(
                                                new SeekForm(seek, filePaths));
                        }
                });

                LinearLayout locateLyout = (LinearLayout) this.findViewById(R.id.locate_layout);
                locateLyout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                Intent intent = new Intent(PublishActivity.this, LocateActivity.class);
                                startActivity(intent);
                        }
                });
        }

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

}
