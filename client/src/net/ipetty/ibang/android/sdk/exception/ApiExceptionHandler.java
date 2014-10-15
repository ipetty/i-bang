package net.ipetty.ibang.android.sdk.exception;

import java.io.IOException;

import net.ipetty.ibang.api.Constants;

import org.apache.commons.io.IOUtils;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;
import android.util.Log;

/**
 * Api客户端异常解析
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class ApiExceptionHandler implements ErrorHandler {

	private String TAG = getClass().getSimpleName();

	@Override
	public Throwable handleError(RetrofitError cause) {
		try {
			Response response = cause.getResponse();
			if (response != null) {
				int status = response.getStatus();
				if (status >= 400) {
					TypedInput bodyInputStream = response.getBody();
					String body = IOUtils.toString(bodyInputStream.in(), Constants.UTF8);
					Log.d(TAG, "status=" + status + ", body=" + body);
					return new ApiException(body);
				}
			}
		} catch (IOException e) {
			return new ApiException(e);
		}
		return cause;
	}

}
