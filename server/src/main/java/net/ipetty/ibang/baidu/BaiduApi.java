package net.ipetty.ibang.baidu;

import net.ipetty.ibang.baidu.vo.GeoGetByAdressRetVO;
import net.ipetty.ibang.baidu.vo.GeoGetByXYRetVO;
import net.ipetty.ibang.baidu.vo.NearbyRetVO;
import net.ipetty.ibang.baidu.vo.PoiRetVO;
import net.ipetty.ibang.baidu.vo.RetVO;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 *
 *
 * @author yenos
 */
public interface BaiduApi {

    /**
     * 增
     */
    @FormUrlEncoded
    @POST("/geodata/v3/poi/create")
    public RetVO lbsCreatePoi(@Field("ak") String ak, @Field("geotable_id") String geotable_id, @Field("coord_type") Integer coord_type,
        @Field("longitude") Double longitude, @Field("latitude") Double latitude, @Field("title") String title, @Field("tags") String tags,
        @Field("bid") String bid);

    /**
     * 删除
     */
    @FormUrlEncoded
    @POST("/geodata/v3/poi/delete")
    public RetVO lbsDeletePoi(@Field("ak") String ak, @Field("geotable_id") String geotable_id, @Field("id") String id);

    /**
     * 改
     */
    @FormUrlEncoded
    @POST("/geodata/v3/poi/update")
    public RetVO lbsUpdatePoi(@Field("id") String id, @Field("ak") String ak, @Field("geotable_id") String geotable_id, @Field("coord_type") Integer coord_type,
        @Field("longitude") Double longitude, @Field("latitude") Double latitude, @Field("title") String title, @Field("tags") String tags,
        @Field("bid") String bid);

    /**
     * 查
     */
    @GET("/geodata/v3/poi/detail")
    public PoiRetVO lbsGetPoi(@Query("id") String id, @Query("ak") String ak, @Query("geotable_id") String geotable_id);

    /**
     * 查附近
     *
     * @param ak
     * @param geotable_id
     * @param q 关键字,可以为空
     * @param location 检索的中心点,样例：116.4321,38.76623
     * @param coordType 坐标系,3代表百度经纬度坐标系统
     * @param radius 检索半径,样例：500
     * @param tags 标签,样例：美食 小吃
     * @param sortby 排序字段,格式为sortby={key1}:value1|{key2:val2|key3:val3}。 最多支持16个字段排序 {keyname}:1 升序 {keyname}:-1 降序
     * 以下keyname为系统预定义的： distance 距离排序 weight 权重排序
     * @param pageIndex
     * @param pageSize
     */
    @GET("/geosearch/v3/nearby")
    public NearbyRetVO lbsGetNearby(@Query("ak") String ak, @Query("geotable_id") String geotable_id, @Query("q") String q,
        @Query("location") String location, @Query("coord_type") Integer coordType, @Query("radius") Integer radius,
        @Query("tags") String tags, @Query("sortby") String sortby,
        @Query("page_index") Integer pageIndex, @Query("page_size") Integer pageSize);

    /**
     * 地址转坐标
     */
    @GET("/geocoder")
    public GeoGetByAdressRetVO geoGetByAdress(@Query("key") String ak, @Query("address") String address, @Query("city") String city, @Query("output") String output);

    /**
     * 坐标转地址
     */
    @GET("/geocoder")
    public GeoGetByXYRetVO geoGetByXY(@Query("key") String ak, @Query("location") String location, @Query("output") String output);

}
