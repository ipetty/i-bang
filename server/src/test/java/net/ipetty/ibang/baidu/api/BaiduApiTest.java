package net.ipetty.ibang.baidu.api;

import net.ipetty.ibang.baidu.BaiduApi;
import net.ipetty.ibang.baidu.BaiduApiFactory;
import net.ipetty.ibang.baidu.vo.GeoGetByAdressRetVO;
import net.ipetty.ibang.baidu.vo.GeoGetByXYRetVO;
import net.ipetty.ibang.baidu.vo.PoiRetVO;
import net.ipetty.ibang.baidu.vo.RetVO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LetterServiceTest
 *
 * @author luocanfeng
 * @date 2014年11月7日
 */
public class BaiduApiTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testAll() {
        BaiduApi baiduApi = BaiduApiFactory.getBaiduApi();
        //创建POI
        RetVO ret = baiduApi.lbsCreatePoi(BaiduApiFactory.ak, BaiduApiFactory.lbsTableId, BaiduApiFactory.coordTypeValue, 121.487899, 31.249162,
            "测试Title", "测试,LBS", "测试Bid");
        ret = baiduApi.lbsUpdatePoi(ret.getId(), BaiduApiFactory.ak, BaiduApiFactory.lbsTableId, BaiduApiFactory.coordTypeValue, 121.487899, 31.249162,
            "测试Title-Update", "测试,LBS", "测试Bid");

        PoiRetVO poiRetVO = baiduApi.lbsGetPoi(ret.getId(), BaiduApiFactory.ak, BaiduApiFactory.lbsTableId);
        //删除POI
        //ret = baiduApi.lbsDeletePoi(BaiduApiFactory.ak, BaiduApiFactory.lbsTableId, ret.getId());
        //获取经纬度
        GeoGetByAdressRetVO geoGetByAdressRetVO = baiduApi.geoGetByAdress(BaiduApiFactory.ak, "上海市", "上海市", "json");
        logger.debug(ToStringBuilder.reflectionToString(geoGetByAdressRetVO.getResult()));
        //获取详细地址
        GeoGetByXYRetVO geoGetByXYRetVO = baiduApi.geoGetByXY(BaiduApiFactory.ak, "31.249162,121.487899", "json");
        logger.debug(ToStringBuilder.reflectionToString(geoGetByXYRetVO.getResult()));
    }

}
