package net.ipetty.ibang.android.baidu;

import net.ipetty.ibang.android.baidu.RetVO;
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
        @POST("/geodata/v3/poi/create")
        public RetVO createPoi(@Field("ak") String ak, @Field("geotable_id") String geotable_id, @Field("coord_type") Integer coord_type,
                @Field("longitude") Double longitude, @Field("latitude") Double latitude, @Field("title") String title, @Field("tags") String tags,
                @Field("bid") String bid);

        @FormUrlEncoded
        @POST("/geodata/v3/poi/delete")
        public void deletePoi(@Field("loginName") Long id);

}
