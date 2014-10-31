package net.ipetty.ibang.android.sdk.interceptor;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import net.ipetty.ibang.android.core.util.NetWorkUtils;
import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.android.sdk.exception.NetworkException;
import net.ipetty.ibang.api.Constants;
import org.apache.commons.lang3.StringUtils;
import retrofit.RequestInterceptor;

/**
 * Api请求拦截器
 *
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class ApiInterceptor implements RequestInterceptor {

    private String TAG = getClass().getSimpleName();

    private Context context;

    public ApiInterceptor(Context context) {
        this.context = context;
    }

    /**
     * 在每个请求中加入Basic验证头信息
     */
    @Override
    public void intercept(RequestFacade request) {
        if (!NetWorkUtils.isNetworkConnected(context)) {
            throw new NetworkException();
        }

        // 设置user_token
        ApiContext apiContext = ApiContext.getInstance(context);
        String userToken = apiContext.getUserToken();
        if (StringUtils.isNotBlank(userToken)) {
            request.addHeader(Constants.HEADER_NAME_USER_TOKEN,
                Base64.encodeToString(userToken.getBytes(Constants.UTF8), Base64.DEFAULT));
            Log.d(TAG, "set user token: " + userToken);
        }
        String refreshToken = apiContext.getRefreshToken();
        if (StringUtils.isNotBlank(refreshToken)) {
            request.addHeader(Constants.HEADER_NAME_REFRESH_TOKEN,
                Base64.encodeToString(refreshToken.getBytes(Constants.UTF8), Base64.DEFAULT));
            Log.d(TAG, "set refresh token: " + refreshToken);
        }
        String deviceUuid = apiContext.getDeviceUuid();
        if (StringUtils.isNotBlank(deviceUuid)) {
            request.addHeader(Constants.HEADER_NAME_DEVICE_UUID,
                Base64.encodeToString(deviceUuid.getBytes(Constants.UTF8), Base64.DEFAULT));
            Log.d(TAG, "set device uuid: " + deviceUuid);
        }
    }

}
