package net.ipetty.ibang.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import net.ipetty.ibang.baidu.BaiduApi;
import net.ipetty.ibang.baidu.BaiduApiFactory;
import net.ipetty.ibang.baidu.vo.NearbyRetVO;
import net.ipetty.ibang.baidu.vo.PoiVO;
import net.ipetty.ibang.baidu.vo.RetVO;

import net.ipetty.ibang.context.UserContext;
import net.ipetty.ibang.context.UserPrincipal;
import net.ipetty.ibang.model.Location;
import net.ipetty.ibang.model.Seek;
import net.ipetty.ibang.model.User;
import net.ipetty.ibang.service.LocationService;
import net.ipetty.ibang.service.SeekService;
import net.ipetty.ibang.service.UserService;
import net.ipetty.ibang.util.DateUtils;
import net.ipetty.ibang.vo.Constants;
import net.ipetty.ibang.vo.LocationVO;
import net.ipetty.ibang.vo.SeekCategory;
import net.ipetty.ibang.vo.SeekVO;
import net.ipetty.ibang.vo.SeekWithLocationVO;
import net.ipetty.ibang.web.rest.exception.RestException;
import org.apache.commons.collections.CollectionUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SeekController
 *
 * @author luocanfeng
 * @date 2014年10月10日
 */
@Controller
public class SeekController extends BaseController {

    @Resource
    private SeekService seekService;

    @Resource
    private LocationService locationService;

    @Resource
    private UserService userService;

    private String CATEGORY_MY_STRING = "我的特长";

    /**
     * 发布求助
     */
    @RequestMapping(value = "/seek/publish", method = RequestMethod.POST)
    public SeekVO publish(@RequestBody SeekVO seek) {
        Assert.notNull(seek, "求助单不能为空");

        UserPrincipal currentUser = UserContext.getContext();
        if (currentUser == null || currentUser.getId() == null) {
            throw new RestException("用户登录后才能发布求助");
        }

        // 设置求助人
        seek.setSeekerId(currentUser.getId());
        seek.setType(Constants.SEEK_TYPE_SEEK);

        Seek s = Seek.fromVO(seek);
        seekService.publish(s);
        s = seekService.getById(s.getId());

        return entityToVo(s);
    }

    /**
     * 发布求助（带地理位置）
     */
    @RequestMapping(value = "/seek/publishWithLocation", method = RequestMethod.POST)
    public SeekVO publish(@RequestBody SeekWithLocationVO seek) {
        Assert.notNull(seek, "求助单不能为空");

        UserPrincipal currentUser = UserContext.getContext();
        if (currentUser == null || currentUser.getId() == null) {
            throw new RestException("用户登录后才能发布求助");
        }

        // 设置求助人
        seek.setSeekerId(currentUser.getId());
        seek.setType(Constants.SEEK_TYPE_SEEK);

        // 保存地理位置
        LocationVO location = seek.getLocation();
        Location loc = null;
        if (location != null) {
            loc = Location.fromVO(location);
            loc.setCoorType(BaiduApiFactory.coordTypeValue.toString());
            locationService.save(loc);
        }

        // 保存求助单
        Seek s = Seek.fromVO(seek);
        if (loc != null) {
            s.setLocationId(loc.getId());
        }
        seekService.publish(s);
        s = seekService.getById(s.getId());
        publishLocation(s, loc);

        return entityToVo(s);
    }

    /**
     * 发送到百度LBS
     */
    private void publishLocation(Seek s, Location loc) {
        if (loc == null) {
            return;
        }

        String title = s.getContent();
        if (title.length() > 100) {
            title = title.substring(0, 99);
        }
        Integer type = 1;
        if (s.getType().equals("帮忙")) {
            type = 2;
        }

        BaiduApi baiduApi = BaiduApiFactory.getBaiduApi();
        RetVO ret = baiduApi.lbsCreatePoi(BaiduApiFactory.ak, BaiduApiFactory.lbsTableId,
            BaiduApiFactory.coordTypeValue, loc.getLatitude(), loc.getLongitude(),
            title, s.getCategoryL1() + " " + s.getCategoryL2(), s.getId().toString(), type);
        locationService.setLbsId(loc.getId(), ret.getId());

    }

    /**
     * 发布帮忙（带地理位置）
     */
    @RequestMapping(value = "/seek/publishAssistance", method = RequestMethod.POST)
    public SeekVO publishAssistance(@RequestBody SeekWithLocationVO seek) {
        Assert.notNull(seek, "帮忙不能为空");

        UserPrincipal currentUser = UserContext.getContext();
        if (currentUser == null || currentUser.getId() == null) {
            throw new RestException("用户登录后才能发布帮忙");
        }

        // 设置帮忙者
        seek.setSeekerId(currentUser.getId());
        seek.setType(Constants.SEEK_TYPE_ASSISTANCE);

        // 保存地理位置
        LocationVO location = seek.getLocation();
        Location loc = null;
        if (location != null) {
            loc = Location.fromVO(location);
            loc.setCoorType(BaiduApiFactory.coordTypeValue.toString());
            locationService.save(loc);
        }

        // 保存帮忙
        Seek s = Seek.fromVO(seek);
        if (loc != null) {
            s.setLocationId(loc.getId());
        }
        seekService.publish(s);
        s = seekService.getById(s.getId());
        publishLocation(s, loc);

        return entityToVo(s);
    }

    /**
     * 获取
     */
    @RequestMapping(value = "/seek", method = RequestMethod.GET)
    public SeekVO getById(Long id) {
        Assert.notNull(id, "求助单ID不能为空");

        Seek s = seekService.getById(id);
        return entityToVo(s);
    }

    /**
     * 获取最新的未关闭求助列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeklist/latest", method = RequestMethod.GET)
    public List<SeekVO> listLatest(String timeline, int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatest(Constants.SEEK_TYPE_SEEK, date, pageNumber, pageSize);
        return listToVoList(seeks);
    }

    private List<SeekVO> listToVoList(List<Seek> list) {
        List<SeekVO> voList = new ArrayList<SeekVO>();
        for (Seek entity : list) {
            voList.add(entityToVo(entity));
        }
        return voList;
    }

    private SeekVO entityToVo(Seek entity) {
        SeekVO vo = entity.toVO();
        Integer userId = entity.getSeekerId();
        if (userId != null) {
            vo.setSeeker(userService.getById(userId).toVO());
        }
        return vo;
    }

    /**
     * 获取最新的未关闭求助/帮忙列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeks/latest", method = RequestMethod.GET)
    public List<SeekVO> listLatest(String type, String timeline, int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatest(type, date, pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 获取指定分类中最新的未关闭求助列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeklist/latestbycategory", method = RequestMethod.GET)
    public List<SeekVO> listLatestByCategory(String categoryL1, String categoryL2, String timeline, int pageNumber,
        int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatestByCategory(Constants.SEEK_TYPE_SEEK, categoryL1, categoryL2, date,
            pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 获取附近指定分类中未关闭求助/帮忙，按距离排序
     *
     * @param pageNumber 分页页码，从0开始
     * @param radius 搜索半径(米)
     * @param type 类型，1:求助 | 2:帮忙
     */
    @RequestMapping(value = "/seeklist/nearlybycategory", method = RequestMethod.GET)
    public List<SeekVO> listNearlyByCategory(String latitude, String longitude, String categoryL1, String categoryL2,
        Integer type, int radius, int pageNumber, int pageSize) {
        Assert.notNull(latitude, "经度值不能为空");
        Assert.notNull(longitude, "纬度值不能为空");
        Assert.notNull(type, "类型不能为空");
        //参数处理
        String location = longitude + "," + latitude;
        String filter = "btype:[" + type + "]";
        String keyword = "";
        String tags = "";
        if (StringUtils.isNotEmpty(categoryL1)) {
            tags = tags + categoryL1 + " ";
        }
        if (StringUtils.isNotEmpty(categoryL2)) {
            if (CATEGORY_MY_STRING.equals(categoryL2)) {
                UserPrincipal currentUser = UserContext.getContext();
                if (currentUser == null || currentUser.getId() == null) {
                    throw new RestException("用户登录后才能获取特长范围");
                }
                tags = getMyOfferRangeStr(currentUser.getId());

            } else {
                tags = tags + categoryL2;
            }
        }

        BaiduApi baiduApi = BaiduApiFactory.getBaiduApi();

        //附近
        NearbyRetVO nearbyRetVO = baiduApi.lbsGetNearby(BaiduApiFactory.ak, BaiduApiFactory.lbsTableId, keyword, location, BaiduApiFactory.coordTypeValue, radius,
            tags, "distance:1", filter, pageNumber, pageSize);
        if (nearbyRetVO.getStatus() != 0) {
            throw new RestException(nearbyRetVO.getMessage());
        }

        List<Long> seekIds = new ArrayList<Long>();
        Map<String, PoiVO> poiMap = new HashMap<String, PoiVO>();
        for (PoiVO poi : nearbyRetVO.getContents()) {
            if (poi != null && StringUtils.isNotEmpty(poi.getBid())) {
                seekIds.add(Long.valueOf(poi.getBid()));
                poiMap.put(poi.getBid(), poi);
            }
        }
        List<Seek> seeks = seekService.listByIds(seekIds);
        List<SeekVO> seekVOList = listToVoList(seeks);
        //填写距离字段
        for (SeekVO seekVO : seekVOList) {
            if (seekVO != null && seekVO.getId() != null) {
                seekVO.setDistance(poiMap.get(seekVO.getId().toString()).getDistance());
            }
        }
        return seekVOList;
    }

    //获取我的特长字符串，以空格分割
    private String getMyOfferRangeStr(Integer userId) {
        String tags = "";
        User user = userService.getById(userId);
        List<SeekCategory> offerRange = user.getOffererInfo().getOfferRange();
        if (CollectionUtils.isNotEmpty(offerRange)) {
            for (SeekCategory c : offerRange) {
                tags += c.getCategoryL1() + " " + c.getCategoryL2() + " ";
            }
        }
        return tags;
    }

    /**
     * 获取指定分类中最新的未关闭求助/帮忙列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeks/latestbycategory", method = RequestMethod.GET)
    public List<SeekVO> listLatestByCategory(String type, String categoryL1, String categoryL2, String timeline,
        int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatestByCategory(type, categoryL1, categoryL2, date, pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 获取所在城市指定分类中最新的未关闭求助列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeklist/latestbycityorcategory", method = RequestMethod.GET)
    public List<SeekVO> listLatestByCityOrCategory(String city, String district, String categoryL1, String categoryL2,
        String timeline, int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatestByCityOrCategory(Constants.SEEK_TYPE_SEEK, city, district, categoryL1,
            categoryL2, date, pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 获取所在城市指定分类中最新的未关闭求助/帮忙列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeks/latestbycityorcategory", method = RequestMethod.GET)
    public List<SeekVO> listLatestByCityOrCategory(String type, String city, String district, String categoryL1,
        String categoryL2, String timeline, int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatestByCityOrCategory(type, city, district, categoryL1, categoryL2, date,
            pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 获取所在城市指定用户帮忙范围内的最新未关闭求助列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeklist/latestbycityAndOfferRange", method = RequestMethod.GET)
    public List<SeekVO> listLatestByCityAndOfferRange(String city, String district, Integer userId, String timeline,
        int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatestByCityAndOfferRange(Constants.SEEK_TYPE_SEEK, city, district, userId,
            date, pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 获取所在城市指定用户帮忙范围内的最新未关闭求助/帮忙列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeks/latestbycityAndOfferRange", method = RequestMethod.GET)
    public List<SeekVO> listLatestByCityAndOfferRange(String type, String city, String district, Integer userId,
        String timeline, int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatestByCityAndOfferRange(type, city, district, userId, date, pageNumber,
            pageSize);
        return listToVoList(seeks);
    }

    /**
     * 根据关键字搜索最新的未关闭求助列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeklist/latestbykeyword", method = RequestMethod.GET)
    public List<SeekVO> listLatestByKeyword(String keyword, String timeline, int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatestByKeyword(Constants.SEEK_TYPE_SEEK, keyword, date, pageNumber,
            pageSize);
        return listToVoList(seeks);
    }

    /**
     * 根据关键字搜索最新的未关闭求助/帮忙列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeks/latestbykeyword", method = RequestMethod.GET)
    public List<SeekVO> listLatestByKeyword(String type, String keyword, String timeline, int pageNumber, int pageSize) {
        Date date = StringUtils.isBlank(timeline) ? new Date() : DateUtils.fromDatetimeString(timeline);
        List<Seek> seeks = seekService.listLatestByKeyword(type, keyword, date, pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 获取指定用户的求助列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeklist/byuser", method = RequestMethod.GET)
    public List<SeekVO> listByUserId(Integer userId, int pageNumber, int pageSize) {
        Assert.notNull(userId, "用户ID不能为空");

        List<Seek> seeks = seekService.listByUserId(Constants.SEEK_TYPE_SEEK, userId, pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 获取指定用户的求助/帮忙列表
     *
     * @param pageNumber 分页页码，从0开始
     */
    @RequestMapping(value = "/seeks/byuser", method = RequestMethod.GET)
    public List<SeekVO> listByUserId(String type, Integer userId, int pageNumber, int pageSize) {
        Assert.notNull(userId, "用户ID不能为空");

        List<Seek> seeks = seekService.listByUserId(type, userId, pageNumber, pageSize);
        return listToVoList(seeks);
    }

    /**
     * 根据id列表获取求助列表
     */
    @RequestMapping(value = "/seeklist/byids", method = RequestMethod.GET)
    public List<SeekVO> listByIds(Long... seekIds) {
        Assert.notNull(seekIds, "ID列表不能为空");

        List<Seek> seeks = seekService.listByIds(Arrays.asList(seekIds));
        return listToVoList(seeks);
    }

    /**
     * 关闭求助
     */
    @RequestMapping(value = "/seek/close", method = RequestMethod.POST)
    public boolean close(Long seekId) {
        Assert.notNull(seekId, "求助单ID不能为空");

        Seek s = seekService.getById(seekId);
        Long loId = s.getLocationId();
        if (loId != null) {
            Location lo = locationService.getById(loId);
            if (lo != null && StringUtils.isNotEmpty(lo.getLbsId())) {
                //删除LBS位置
                BaiduApi baiduApi = BaiduApiFactory.getBaiduApi();
                baiduApi.lbsDeletePoi(BaiduApiFactory.ak, BaiduApiFactory.lbsTableId, lo.getLbsId());
            }
        }

        seekService.close(seekId);

        return true;
    }

}
