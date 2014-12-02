package net.ipetty.ibang.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 求助
 *
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class SeekVO extends BaseVO {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3457713874329718078L;

    private Long id; // 非业务主键
    private String sn; // 求助单流水号
    private Integer seekerId; // 求助者ID
    private UserVO seeker;
    private boolean contactInfoVisible; // 是否公开求助者联系信息，默认隐藏
    private String categoryL1; // 一级分类
    private String categoryL2; // 二级分类
    private String type; // 类型，求助 | 帮忙
    private String title; // 求助标题
    private String content; // 求助内容
    private List<ImageVO> images = new ArrayList<ImageVO>(); // 图片
    private String requirement; // 要求
    private int delegateNumber; // 委托数量
    private String reward; // 奖励
    private String additionalReward; // 附加奖励
    private String serviceDate; // 服务时间
    private String province; // 省
    private String city; // 市
    private String district; // 区/县
    private String address; // 详细地址
    private Long locationId; // 地理位置
    private Date createdOn; // 求助创建时间
    private Date exipireDate; // 求助有效日期
    private Date closedOn; // 关闭时间
    private String status; // 状态
    private boolean enable = true; // 是否有效

    private Integer distance;//距离

    public SeekVO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Integer seekerId) {
        this.seekerId = seekerId;
    }

    public UserVO getSeeker() {
        return seeker;
    }

    public void setSeeker(UserVO seeker) {
        this.seeker = seeker;
    }

    public boolean isContactInfoVisible() {
        return contactInfoVisible;
    }

    public void setContactInfoVisible(boolean contactInfoVisible) {
        this.contactInfoVisible = contactInfoVisible;
    }

    public String getCategoryL1() {
        return categoryL1;
    }

    public void setCategoryL1(String categoryL1) {
        this.categoryL1 = categoryL1;
    }

    public String getCategoryL2() {
        return categoryL2;
    }

    public void setCategoryL2(String categoryL2) {
        this.categoryL2 = categoryL2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ImageVO> getImages() {
        return images;
    }

    public void setImages(List<ImageVO> images) {
        this.images = images;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public int getDelegateNumber() {
        return delegateNumber;
    }

    public void setDelegateNumber(int delegateNumber) {
        this.delegateNumber = delegateNumber;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getAdditionalReward() {
        return additionalReward;
    }

    public void setAdditionalReward(String additionalReward) {
        this.additionalReward = additionalReward;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getExipireDate() {
        return exipireDate;
    }

    public void setExipireDate(Date exipireDate) {
        this.exipireDate = exipireDate;
    }

    public Date getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @return the distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}
