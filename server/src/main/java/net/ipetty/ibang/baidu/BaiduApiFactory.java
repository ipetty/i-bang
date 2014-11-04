package net.ipetty.ibang.baidu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * IBangApiFactory
 *
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class BaiduApiFactory {

    //临时写死
    private static String ak = "8691f5dfa58c9acccf7bb4c72e529382";
    //临时写死
    private static String lbsTableId = "82284";
    //临时写死
    private static Integer coordTypeValue = 3;

    private static final String endPoint = "http://api.map.baidu.com/";

    private static RestAdapter restAdapter;

    private static BaiduApi baiduApi;

    private static final Gson gson = new GsonBuilder().create();

    private static void init() {
        restAdapter = new RestAdapter.Builder()
            .setEndpoint(endPoint)
            .setConverter(new GsonConverter(gson))//.setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
        baiduApi = restAdapter.create(BaiduApi.class);
    }

    public static BaiduApi getBaiduApi() {
        if (baiduApi == null) {
            init();
        }
        return baiduApi;
    }

}
