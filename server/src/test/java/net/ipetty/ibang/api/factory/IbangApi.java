package net.ipetty.ibang.api.factory;

import java.util.Date;

import net.ipetty.ibang.api.UserApi;
import net.ipetty.ibang.api.UserApiImpl;
import net.ipetty.ibang.api.exception.ApiExceptionHandler;
import net.ipetty.ibang.api.interceptor.ApiInterceptor;
import net.ipetty.ibang.api.util.DateTypeAdapter;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * IBangApiFactory
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class IbangApi {

	private static IbangApi instance;

	private static RestAdapter restAdapter;
	/** Api请求拦截器 */
	private static RequestInterceptor requestInterceptor;
	/** Api客户端异常解析 */
	private static final ErrorHandler errorHandler = new ApiExceptionHandler();
	/** gson */
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();

	private IbangApi() {
		requestInterceptor = new ApiInterceptor();
		restAdapter = new RestAdapter.Builder().setEndpoint("http://localhost:8080/api")
				.setConverter(new GsonConverter(gson)).setRequestInterceptor(requestInterceptor)
				.setErrorHandler(errorHandler).build();
	}

	public static IbangApi init() {
		if (instance == null) {
			instance = new IbangApi();
		}
		return instance;
	}

	/**
	 * 工厂方法
	 */
	public <T> T create(Class<T> clazz) {
		return restAdapter.create(clazz);
	}

	/**
	 * 获取UserApiImpl
	 */
	public UserApi getUserApi() {
		return new UserApiImpl();
	}

}
