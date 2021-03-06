package net.ipetty.ibang.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户
 * @author luocanfeng
 * @date 2014年9月16日
 */
public class UserVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 4299626643470437373L;

	private Integer id; // 非业务主键
	private String username; // 用户名
	private String email; // 邮箱
	private boolean identityVerified = false; // 是否通过身份验证
	private String nickname; // 昵称
	private String gender; // 性别
	private String job; // 职位
	private String phone; // 手机号码
	private String telephone; // 联系电话
	private String avatar; // 头像
	private String signature; // 个性签名
	private String speciality; // 特长
	private String preference; // 偏好
	private String province; // 省
	private String city; // 市
	private String district; // 区/县
	private String address; // 所在地
	private Date createdOn; // 注册时间

	private int seekCount; // 总的完成求助次数
	private int seekerTotalPoint; // 作为求助者身份的总积分
	private String seekerTitle; // 作为求助者身份获得的头衔

	private int offerCount; // 总的完成帮助次数
	private int offererTotalPoint; // 作为帮助者身份的总积分
	private String offererTitle; // 作为帮助者身份获得的头衔
	private List<SeekCategory> offerRange = new ArrayList<SeekCategory>(); // 帮忙范围

	public UserVO() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isIdentityVerified() {
		return identityVerified;
	}

	public void setIdentityVerified(boolean identityVerified) {
		this.identityVerified = identityVerified;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public int getSeekCount() {
		return seekCount;
	}

	public void setSeekCount(int seekCount) {
		this.seekCount = seekCount;
	}

	public int getSeekerTotalPoint() {
		return seekerTotalPoint;
	}

	public void setSeekerTotalPoint(int seekerTotalPoint) {
		this.seekerTotalPoint = seekerTotalPoint;
	}

	public String getSeekerTitle() {
		return seekerTitle;
	}

	public void setSeekerTitle(String seekerTitle) {
		this.seekerTitle = seekerTitle;
	}

	public int getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(int offerCount) {
		this.offerCount = offerCount;
	}

	public int getOffererTotalPoint() {
		return offererTotalPoint;
	}

	public void setOffererTotalPoint(int offererTotalPoint) {
		this.offererTotalPoint = offererTotalPoint;
	}

	public String getOffererTitle() {
		return offererTitle;
	}

	public void setOffererTitle(String offererTitle) {
		this.offererTitle = offererTitle;
	}

	public List<SeekCategory> getOfferRange() {
		return offerRange;
	}

	public void setOfferRange(List<SeekCategory> offerRange) {
		this.offerRange = offerRange;
	}

}
