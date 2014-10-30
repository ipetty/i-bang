package net.ipetty.ibang.android.baidu;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import net.ipetty.ibang.android.sdk.exception.ApiExceptionHandler;
import net.ipetty.ibang.android.sdk.util.DateTypeAdapter;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * IBangApiFactory
 *
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class BaiduApiFactory {

        private static Context context;
        private static BaiduApiFactory instance;

        private static RestAdapter restAdapter;

        private static BaiduApi baiduApi;

        /**
         * Api客户端异常解析
         */
        private static final ErrorHandler errorHandler = new ApiExceptionHandler();
        /**
         * gson
         */
        private static final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();

        public static void init(Context ctx) {
                context = ctx;
                restAdapter = new RestAdapter.Builder().setEndpoint("http://api.map.baidu.com/")
                        .setConverter(new GsonConverter(gson))//.setLogLevel(RestAdapter.LogLevel.FULL)
                        .setErrorHandler(errorHandler).build();
                baiduApi = restAdapter.create(BaiduApi.class);
        }

        public static BaiduApi getBaiduApi() {
                return baiduApi;
        }

}
