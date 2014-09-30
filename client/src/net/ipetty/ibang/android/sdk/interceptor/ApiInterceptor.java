package net.ipetty.ibang.android.sdk.interceptor;

import net.ipetty.ibang.android.sdk.context.ApiContext;
import net.ipetty.ibang.api.Constants;
import net.ipetty.ibang.util.Encodes;

import org.apache.commons.lang3.StringUtils;

import retrofit.RequestInterceptor;
import android.content.Context;
import android.util.Log;

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
		// 设置user_token
		if (StringUtils.isNotBlank(ApiContext.getInstance(context).getUserToken())) {
			request.addHeader(Constants.HEADER_NAME_USER_TOKEN,
					Encodes.encodeBase64(ApiContext.getInstance(context).getUserToken().getBytes(Constants.UTF8)));
			Log.d(TAG, "set user token: " + ApiContext.getInstance(context).getUserToken());
		}
		if (StringUtils.isNotBlank(ApiContext.getInstance(context).getRefreshToken())) {
			request.addHeader(Constants.HEADER_NAME_REFRESH_TOKEN,
					Encodes.encodeBase64(ApiContext.getInstance(context).getRefreshToken().getBytes(Constants.UTF8)));
			Log.d(TAG, "set refresh token: " + ApiContext.getInstance(context).getRefreshToken());
		}
		request.addHeader(Constants.HEADER_NAME_DEVICE_UUID,
				Encodes.encodeBase64(ApiContext.getInstance(context).getDeviceUuid().getBytes(Constants.UTF8)));
		// request.addHeader(Constants.HEADER_NAME_DEVICE_ID,
		// Encodes.encodeBase64(ApiContext.getInstance(context).getDeviceId().getBytes(Constants.UTF8)));
		// request.addHeader(Constants.HEADER_NAME_DEVICE_MAC,
		// Encodes.encodeBase64(ApiContext.getInstance(context).getDeviceMac().getBytes(Constants.UTF8)));
	}

}
