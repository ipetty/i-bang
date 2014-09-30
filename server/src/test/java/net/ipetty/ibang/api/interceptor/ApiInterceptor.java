package net.ipetty.ibang.api.interceptor;

import net.ipetty.ibang.api.Constants;
import net.ipetty.ibang.api.context.ApiContext;
import net.ipetty.ibang.util.Encodes;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RequestInterceptor;

/**
 * Api请求拦截器
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class ApiInterceptor implements RequestInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public ApiInterceptor() {
	}

	/**
	 * 在每个请求中加入Basic验证头信息
	 */
	@Override
	public void intercept(RequestFacade request) {
		// 设置user_token
		if (StringUtils.isNotBlank(ApiContext.getInstance().getUserToken())) {
			request.addHeader(Constants.HEADER_NAME_USER_TOKEN,
					Encodes.encodeBase64(ApiContext.getInstance().getUserToken().getBytes(Constants.UTF8)));
			logger.debug("set user token: {}", ApiContext.getInstance().getUserToken());
		}
		if (StringUtils.isNotBlank(ApiContext.getInstance().getRefreshToken())) {
			request.addHeader(Constants.HEADER_NAME_REFRESH_TOKEN,
					Encodes.encodeBase64(ApiContext.getInstance().getRefreshToken().getBytes(Constants.UTF8)));
			logger.debug("set refresh token: {}", ApiContext.getInstance().getRefreshToken());
		}
		request.addHeader(Constants.HEADER_NAME_DEVICE_UUID,
				Encodes.encodeBase64(ApiContext.getInstance().getDeviceUuid().getBytes(Constants.UTF8)));
		// request.addHeader(Constants.HEADER_NAME_DEVICE_ID,
		// Encodes.encodeBase64(ApiContext.getInstance().getDeviceId().getBytes(Constants.UTF8)));
		// request.addHeader(Constants.HEADER_NAME_DEVICE_MAC,
		// Encodes.encodeBase64(ApiContext.getInstance().getDeviceMac().getBytes(Constants.UTF8)));
	}

}
