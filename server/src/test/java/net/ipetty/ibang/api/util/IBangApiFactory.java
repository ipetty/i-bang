package net.ipetty.ibang.api.util;

import java.util.Date;

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
public class IBangApiFactory {

	private static final RestAdapter restAdapter;

	/** Api请求拦截器 */
	private static RequestInterceptor requestInterceptor = new ApiInterceptor();

	/** Api客户端异常解析 */
	private static ErrorHandler errorHandler = new ApiExceptionHandler();

	/** gson */
	private static Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();

	static {
		restAdapter = new RestAdapter.Builder().setEndpoint("http://localhost:8080/api")
				.setConverter(new GsonConverter(gson)).setRequestInterceptor(requestInterceptor)
				.setErrorHandler(errorHandler).build();
	}

	/**
	 * 工厂方法
	 */
	public static <T> T create(Class<T> clazz) {
		return restAdapter.create(clazz);
	}

}
