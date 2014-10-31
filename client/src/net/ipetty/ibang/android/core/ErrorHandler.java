/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.android.core;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import net.ipetty.ibang.android.core.util.AppUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.sdk.exception.ApiException;
import net.ipetty.ibang.android.sdk.factory.IbangApi;
import net.ipetty.ibang.api.CrashLogApi;
import net.ipetty.ibang.vo.CrashLogVO;
import net.ipetty.ibang.vo.UserVO;
import org.apache.http.conn.ConnectTimeoutException;

/**
 *
 * @author Administrator
 */
public class ErrorHandler {

    private String TAG = getClass().getSimpleName();

    private final Context context;

    public ErrorHandler(Context context) {
        this.context = context;
    }

    public void handleError(Throwable ex) {
        Log.d(TAG, "handleError");
        Log.e(TAG, "", ex);
        // 应用异常 界面层
        if (ex instanceof AppException) {
            AppException e = (AppException) ex;
            showError(e.getMessage());
            return;
        }

        // 超时
        if (ex instanceof ConnectTimeoutException) {
            // ConnectTimeoutException e = (ConnectTimeoutException) ex;
            showError("请求超时，请检查网络后重试");
            return;
        }

        // API异常 任务层
        if (ex instanceof ApiException) {
            ApiException e = (ApiException) ex;
            if (null == e.getMessage() || "".equals(e.getMessage())) {
                showError("API异常");
                reportUnknowError(ex);
            } else {
                showError(e.getMessage());
            }

            return;
        }

        showError("未知异常");
        reportUnknowError(ex);
    }

    private void reportUnknowError(Throwable ex) {
        Log.d(TAG, "reportUnknowError");
        try {

            Writer writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            ex.printStackTrace(pw);
            Throwable cause = ex.getCause();
            // 循环着把所有的异常信息写入writer中
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
            pw.close();// 记得关闭
            String errorInfo = writer.toString();

            final CrashLogVO crashVO = new CrashLogVO();
            crashVO.setUserId(ApiContext.getInstance(context).getCurrentUserId());
            UserVO user = ApiContext.getInstance(context).getCurrentUser();
            String nickName = user == null ? "" : user.getNickname();
            crashVO.setUserName(nickName);
            crashVO.setAndroidVersion(android.os.Build.VERSION.RELEASE);
            crashVO.setAppVersionCode(AppUtils.getAppVersionCode(context));
            crashVO.setAppVersionName(AppUtils.getAppVersionName(context));
            crashVO.setCrashType("error");
            crashVO.setLog(errorInfo);
            new Thread() {
                @Override
                public void run() {
                    IbangApi.init(context).create(CrashLogApi.class).save(crashVO);
                }
            }.start();
        } catch (Exception e) {
            Log.e(TAG, "", e);
            // 忽略异常
        }
    }

    // 显示错误信息
    private void showError(final String msg) {
        Log.d(TAG, "showError");
        // 支持非UI线程进行UI界面操作
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
    }
}
