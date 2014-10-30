package net.ipetty.ibang.android.boot;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.DefaultTaskListener;
import net.ipetty.ibang.android.core.util.AnimUtils;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.login.LoginActivity;
import net.ipetty.ibang.android.main.MainActivity;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.update.UpdateManager;
import net.ipetty.ibang.android.update.UpdateUtils;

public class SplashActivity extends Activity {

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new CheckUpdateTask(this).setListener(new DefaultTaskListener<Boolean>(this) {
            @Override
            public void onSuccess(Boolean hasUpdate) {

                // 如果有更新则停留此界面进行升级
                if (hasUpdate) {
                    UpdateManager updateManager = new UpdateManager(SplashActivity.this);
                    updateManager.setOnCancelListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SplashActivity.this.startUP();
                        }
                    });
                    updateManager.showNoticeDialog(UpdateUtils.getUpdaeInfo());
                } else {
                    // 启动流程
                    SplashActivity.this.startUP();
                }

            }
        }).execute();
    }

    private void startUP() {
        // 如果用户上次已登录，则启动时会重新登录
        if (ApiContext.getInstance(SplashActivity.this).isAuthorized()) {
            Log.d(TAG, "重新登录");
            String userToken = ApiContext.getInstance(SplashActivity.this).getUserToken();
            String refreshToken = ApiContext.getInstance(SplashActivity.this).getRefreshToken();
            new ReloginTask(SplashActivity.this).setListener(new ReloginTaskListener(SplashActivity.this)).execute(
                userToken, refreshToken);
        } else {
            Log.d(TAG, "启动");
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    goMain();
                    SplashActivity.this.finish();
                }
            }.start();
        }
    }

    // 转向主界面
    public void goMain() {
        AppUtils.goTo(this, MainActivity.class);
        AnimUtils.fadeInToOut(this);
        finish();
    }

    // 转向登陆界面
    public void goLogin() {
        AppUtils.goTo(this, LoginActivity.class);
        AnimUtils.fadeInToOut(this);
        finish();
    }

}
