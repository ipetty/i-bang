package net.ipetty.ibang.baidu;

import net.ipetty.ibang.baidu.vo.NearbyRetVO;
import net.ipetty.ibang.baidu.vo.PoiRetVO;
import net.ipetty.ibang.baidu.vo.RetVO;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

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
    public void lbsDeletePoi(@Field("loginName") Long id);

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
    @FormUrlEncoded
    @POST("/geodata/v3/poi/detail")
    public PoiRetVO lbsGetPoi(@Field("id") String id, @Field("ak") String ak, @Field("geotable_id") String geotable_id);

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
     * @param filter
     * @param pageIndex
     * @param pageSize
     */
    @FormUrlEncoded
    @POST("/geosearch/v3/nearby")
    public NearbyRetVO lbsGetNearby(@Field("ak") String ak, @Field("geotable_id") String geotable_id, @Field("q") String q,
        @Field("location") String location, @Field("coord_type") Integer coordType, @Field("radius") Integer radius,
        @Field("tags") String tags, @Field("sortby") String sortby, @Field("filter") String filter,
        @Field("page_index") Integer pageIndex, @Field("page_size") Integer pageSize);

    //TODO:以下两个方法还需各自定义返回值格式
    //http://developer.baidu.com/map/geocoding-api.htm
    /**
     * 地址转坐标
     */
    @FormUrlEncoded
    @GET("/geocoder")
    public PoiRetVO geoGetXY(@Field("key") String ak, @Field("address") String address, @Field("city") String city, @Field("output") String output);

    /**
     * 坐标转地址
     */
    @FormUrlEncoded
    @GET("/geocoder")
    public PoiRetVO geoGetAdress(@Field("key") String ak, @Field("location") String location, @Field("output") String output);

}
