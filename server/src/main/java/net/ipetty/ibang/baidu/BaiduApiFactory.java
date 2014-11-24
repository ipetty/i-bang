package net.ipetty.ibang.baidu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * IBangApiFactory
 *
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class BaiduApiFactory {

    private static final String BAIDU_PROPERTIES_FILE = "baidu.properties";
    private static final String endPoint = "http://api.map.baidu.com/";

    public static String ak;
    public static String lbsTableId;
    public static Integer coordTypeValue;

    private static RestAdapter restAdapter;

    private static BaiduApi baiduApi;

    private static final Gson gson = new GsonBuilder().create();

    static {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            Resource resource = new ClassPathResource(BAIDU_PROPERTIES_FILE);
            File file = resource.getFile();
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ak = properties.getProperty("baidu.ak");
        lbsTableId = properties.getProperty("baidu.lbs.table.id");
        coordTypeValue = Integer.valueOf(properties.getProperty("baidu.coord_type.value"));

        initBaiduApi();
    }

    private static void initBaiduApi() {
        restAdapter = new RestAdapter.Builder()
            .setEndpoint(endPoint)
            .setConverter(new GsonConverter(gson)).setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
        baiduApi = restAdapter.create(BaiduApi.class);
    }

    public static BaiduApi getBaiduApi() {
        if (baiduApi == null) {
            initBaiduApi();
        }
        return baiduApi;
    }

}
