package net.ipetty.ibang.android.sdk.factory;

import java.util.Date;

import net.ipetty.ibang.android.core.Constants;
import net.ipetty.ibang.android.sdk.exception.ApiExceptionHandler;
import net.ipetty.ibang.android.sdk.interceptor.ApiInterceptor;
import net.ipetty.ibang.android.sdk.util.DateTypeAdapter;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * IBangApiFactory
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class IbangApi {

	private static Context context;
	private static IbangApi instance;

	private static RestAdapter restAdapter;
	/** Api请求拦截器 */
	private static RequestInterceptor requestInterceptor;
	/** Api客户端异常解析 */
	private static final ErrorHandler errorHandler = new ApiExceptionHandler();
	/** gson */
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();

	private IbangApi(Context ctx) {
		context = ctx;

		requestInterceptor = new ApiInterceptor(context);
		restAdapter = new RestAdapter.Builder().setEndpoint(Constants.API_SERVER_BASE)
				.setConverter(new GsonConverter(gson)).setRequestInterceptor(requestInterceptor)
				.setErrorHandler(errorHandler).build();
	}

	public static IbangApi init(Context context) {
		if (instance == null) {
			instance = new IbangApi(context);
		}
		return instance;
	}

	/**
	 * 工厂方法
	 */
	public <T> T create(Class<T> clazz) {
		return restAdapter.create(clazz);
	}

}
