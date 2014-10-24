package net.ipetty.ibang.android.baidu.iface;

import net.ipetty.ibang.android.baidu.vo.CreatePoiVo;
import net.ipetty.ibang.android.baidu.vo.RetVO;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 *
 *
 * @author yenos
 */
public interface BaiduApi {

        @FormUrlEncoded
        @POST("geodata/v3/poi/create")
        public RetVO createPoi(@Body CreatePoiVo createPoiVo);

        @FormUrlEncoded
        @POST("/geodata/v3/poi/delete")
        public void deletePoi(@Field("loginName") Long id);

}
